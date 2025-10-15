App({
  globalData: {
    userInfo: null,
    token: null,
    baseUrl: 'http://localhost:8080/api',
    websocketUrl: 'ws://localhost:8080/ws/teleprompter'
  },
  
  onLaunch() {
    // 小程序初始化
    console.log('小程序启动');
    
    // 检查登录状态
    this.checkLoginStatus();
  },
  
  checkLoginStatus() {
    // 检查本地存储的token
    const token = wx.getStorageSync('token');
    if (token) {
      this.globalData.token = token;
      // 验证token有效性
      this.verifyToken(token);
    }
  },
  
  verifyToken(token) {
    // 验证token是否有效
    // 这里可以调用后端API验证token
    console.log('验证token:', token);
  },
  
  login(username, password, successCallback, errorCallback) {
    // 用户登录
    wx.request({
      url: this.globalData.baseUrl + '/users/login',
      method: 'POST',
      data: {
        username: username,
        password: password
      },
      success: (res) => {
        if (res.statusCode === 200 && res.data.token) {
          // 保存token
          this.globalData.token = res.data.token;
          wx.setStorageSync('token', res.data.token);
          wx.setStorageSync('userInfo', res.data.user);
          
          if (successCallback) successCallback(res.data);
        } else {
          if (errorCallback) errorCallback(res.data);
        }
      },
      fail: (err) => {
        if (errorCallback) errorCallback(err);
      }
    });
  },
  
  logout() {
    // 用户登出
    wx.removeStorageSync('token');
    wx.removeStorageSync('userInfo');
    this.globalData.token = null;
    this.globalData.userInfo = null;
  },
  
  request(options) {
    // 封装网络请求，自动添加token
    const header = options.header || {};
    if (this.globalData.token) {
      header['Authorization'] = 'Bearer ' + this.globalData.token;
    }
    
    wx.request({
      url: this.globalData.baseUrl + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: header,
      success: options.success,
      fail: options.fail
    });
  }
})