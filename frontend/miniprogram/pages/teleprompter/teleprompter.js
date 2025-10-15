Page({
  data: {
    script: null,
    scriptId: null,
    content: '',
    speed: 200, // 每分钟字数
    isPlaying: false,
    scrollPosition: 0,
    scrollDuration: 0,
    lineHeight: 40, // 每行高度(像素)
    timer: null,
    websocket: null,
    isConnected: false
  },

  onLoad(options) {
    if (options.scriptId) {
      this.setData({ scriptId: options.scriptId });
      this.loadScript(options.scriptId);
    }
    
    // 连接WebSocket
    this.connectWebSocket();
  },

  onUnload() {
    // 页面卸载时关闭WebSocket连接
    if (this.data.websocket) {
      this.data.websocket.close();
    }
    
    // 清除定时器
    if (this.data.timer) {
      clearInterval(this.data.timer);
    }
  },

  loadScript(scriptId) {
    getApp().request({
      url: `/scripts/${scriptId}`,
      success: (res) => {
        if (res.statusCode === 200) {
          this.setData({ 
            script: res.data,
            content: res.data.content
          });
          
          // 计算推荐语速
          this.calculateOptimalSpeed();
        }
      },
      fail: (err) => {
        console.error('获取文案失败', err);
        wx.showToast({
          title: '获取文案失败',
          icon: 'none'
        });
      }
    });
  },

  connectWebSocket() {
    const url = getApp().globalData.websocketUrl;
    
    const socket = wx.connectSocket({
      url: url,
      success: () => {
        console.log('WebSocket连接成功');
      },
      fail: (err) => {
        console.error('WebSocket连接失败', err);
        wx.showToast({
          title: '连接失败',
          icon: 'none'
        });
      }
    });
    
    socket.onOpen(() => {
      console.log('WebSocket连接已打开');
      this.setData({ 
        websocket: socket,
        isConnected: true 
      });
      
      // 注册用户
      const userInfo = wx.getStorageSync('userInfo');
      if (userInfo) {
        socket.send({
          data: `REGISTER:${userInfo.userId}`
        });
      }
    });
    
    socket.onMessage((res) => {
      this.handleWebSocketMessage(res.data);
    });
    
    socket.onError((err) => {
      console.error('WebSocket错误', err);
      this.setData({ isConnected: false });
      wx.showToast({
        title: '连接错误',
        icon: 'none'
      });
    });
    
    socket.onClose(() => {
      console.log('WebSocket连接已关闭');
      this.setData({ isConnected: false });
    });
  },

  handleWebSocketMessage(message) {
    if (message.startsWith('SCRIPT:')) {
      // 接收到新的文案内容
      const content = message.substring(7);
      this.setData({ content: content });
    } else if (message.startsWith('CONTROL:')) {
      // 接收到控制命令
      const command = message.substring(8);
      this.handleControlCommand(command);
    } else if (message.startsWith('REGISTERED:')) {
      // 注册成功
      console.log('WebSocket注册成功');
    }
  },

  handleControlCommand(command) {
    switch (command) {
      case 'PLAY':
        this.play();
        break;
      case 'PAUSE':
        this.pause();
        break;
      case 'STOP':
        this.stop();
        break;
      default:
        console.log('未知控制命令:', command);
    }
  },

  calculateOptimalSpeed() {
    if (!this.data.script) return;
    
    getApp().request({
      url: `/teleprompter/speed?scriptId=${this.data.script.scriptId}`,
      method: 'POST',
      success: (res) => {
        if (res.statusCode === 200 && res.data > 0) {
          this.setData({ speed: res.data });
        }
      },
      fail: (err) => {
        console.error('计算语速失败', err);
      }
    });
  },

  play() {
    if (!this.data.content || this.data.isPlaying) return;
    
    this.setData({ isPlaying: true });
    
    // 开始滚动动画
    this.startScrolling();
    
    // 发送播放命令到其他设备
    if (this.data.websocket && this.data.isConnected) {
      this.data.websocket.send({
        data: 'CONTROL:PLAY'
      });
    }
  },

  pause() {
    this.setData({ isPlaying: false });
    
    // 停止滚动动画
    if (this.data.timer) {
      clearInterval(this.data.timer);
      this.setData({ timer: null });
    }
    
    // 发送暂停命令到其他设备
    if (this.data.websocket && this.data.isConnected) {
      this.data.websocket.send({
        data: 'CONTROL:PAUSE'
      });
    }
  },

  stop() {
    this.setData({ 
      isPlaying: false,
      scrollPosition: 0
    });
    
    // 停止滚动动画
    if (this.data.timer) {
      clearInterval(this.data.timer);
      this.setData({ timer: null });
    }
    
    // 发送停止命令到其他设备
    if (this.data.websocket && this.data.isConnected) {
      this.data.websocket.send({
        data: 'CONTROL:STOP'
      });
    }
  },

  startScrolling() {
    // 清除之前的定时器
    if (this.data.timer) {
      clearInterval(this.data.timer);
    }
    
    // 计算滚动参数
    const contentLength = this.data.content.length;
    const pixelsPerSecond = (this.data.speed * this.data.lineHeight) / 60; // 每秒滚动像素数
    const scrollInterval = 50; // 每50毫秒更新一次位置
    const pixelsPerInterval = (pixelsPerSecond * scrollInterval) / 1000; // 每次更新滚动的像素数
    
    // 获取更精确的滚动持续时间
    getApp().request({
      url: `/teleprompter/duration?scriptId=${this.data.script.scriptId}&speed=${this.data.speed}`,
      method: 'POST',
      success: (res) => {
        if (res.statusCode === 200 && res.data > 0) {
          // 设置滚动持续时间(用于CSS transition)
          this.setData({ scrollDuration: res.data });
        } else {
          // 使用默认值
          const scrollDuration = scrollInterval / 1000;
          this.setData({ scrollDuration: scrollDuration });
        }
      },
      fail: (err) => {
        console.error('计算滚动持续时间失败', err);
        // 使用默认值
        const scrollDuration = scrollInterval / 1000;
        this.setData({ scrollDuration: scrollDuration });
      }
    });
    
    // 开始定时滚动
    const timer = setInterval(() => {
      if (!this.data.isPlaying) {
        clearInterval(timer);
        return;
      }
      
      this.setData({
        scrollPosition: this.data.scrollPosition - pixelsPerInterval
      });
    }, scrollInterval);
    
    this.setData({ timer: timer });
  },

  onSpeedChange(e) {
    const speed = parseInt(e.detail.value);
    this.setData({ speed: speed });
    
    // 如果正在播放，重新开始滚动以应用新的速度
    if (this.data.isPlaying) {
      this.startScrolling();
    }
  },

  pushToTeleprompter() {
    if (!this.data.script) return;
    
    // 推送到提词器
    getApp().request({
      url: `/teleprompter/push?scriptId=${this.data.script.scriptId}`,
      method: 'POST',
      success: (res) => {
        if (res.statusCode === 200) {
          wx.showToast({
            title: '推送成功',
            icon: 'success'
          });
        } else {
          wx.showToast({
            title: '推送失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        console.error('推送失败', err);
        wx.showToast({
          title: '网络错误',
          icon: 'none'
        });
      }
    });
  }
})