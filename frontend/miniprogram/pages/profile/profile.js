Page({
  data: {
    userInfo: null,
    isLoggedIn: false,
    username: '',
    password: '',
    broadcastType: '娱乐',
    accountStyle: '幽默风趣',
    platformSettings: '抖音'
  },

  onLoad() {
    this.checkLoginStatus();
  },

  onShow() {
    this.checkLoginStatus();
  },

  checkLoginStatus() {
    const token = wx.getStorageSync('token');
    const userInfo = wx.getStorageSync('userInfo');
    
    if (token && userInfo) {
      this.setData({
        isLoggedIn: true,
        userInfo: userInfo,
        username: userInfo.username,
        broadcastType: userInfo.broadcastType || '娱乐',
        accountStyle: userInfo.accountStyle || '幽默风趣',
        platformSettings: userInfo.platformSettings || '抖音'
      });
    }
  },

  onUsernameInput(e) {
    this.setData({ username: e.detail.value });
  },

  onPasswordInput(e) {
    this.setData({ password: e.detail.value });
  },

  onBroadcastTypeChange(e) {
    this.setData({ broadcastType: e.detail.value });
  },

  onAccountStyleChange(e) {
    this.setData({ accountStyle: e.detail.value });
  },

  onPlatformSettingsChange(e) {
    this.setData({ platformSettings: e.detail.value });
  },

  login() {
    if (!this.data.username || !this.data.password) {
      wx.showToast({
        title: '请输入用户名和密码',
        icon: 'none'
      });
      return;
    }
    
    wx.showLoading({
      title: '登录中...'
    });
    
    getApp().login(
      this.data.username,
      this.data.password,
      (res) => {
        wx.hideLoading();
        wx.showToast({
          title: '登录成功',
          icon: 'success'
        });
        
        this.setData({
          isLoggedIn: true,
          userInfo: res.user
        });
      },
      (err) => {
        wx.hideLoading();
        wx.showToast({
          title: '登录失败',
          icon: 'none'
        });
        console.error('登录失败', err);
      }
    );
  },

  register() {
    if (!this.data.username || !this.data.password) {
      wx.showToast({
        title: '请输入用户名和密码',
        icon: 'none'
      });
      return;
    }
    
    wx.showLoading({
      title: '注册中...'
    });
    
    getApp().request({
      url: '/users/register',
      method: 'POST',
      data: {
        username: this.data.username,
        password: this.data.password,
        broadcastType: this.data.broadcastType
      },
      success: (res) => {
        wx.hideLoading();
        if (res.statusCode === 200) {
          wx.showToast({
            title: '注册成功',
            icon: 'success'
          });
          
          this.setData({
            isLoggedIn: true,
            userInfo: res.data.user
          });
          
          // 保存用户信息到本地存储
          wx.setStorageSync('userInfo', res.data.user);
        } else {
          wx.showToast({
            title: '注册失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        wx.hideLoading();
        wx.showToast({
          title: '网络错误',
          icon: 'none'
        });
        console.error('注册失败', err);
      }
    });
  },

  logout() {
    wx.showModal({
      title: '确认退出',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          getApp().logout();
          this.setData({
            isLoggedIn: false,
            userInfo: null,
            username: '',
            password: ''
          });
          
          wx.showToast({
            title: '已退出',
            icon: 'success'
          });
        }
      }
    });
  },

  updateProfile() {
    if (!this.data.userInfo) return;
    
    wx.showLoading({
      title: '保存中...'
    });
    
    // 更新账号风格
    getApp().request({
      url: `/users/${this.data.userInfo.userId}/account-style`,
      method: 'PUT',
      data: this.data.accountStyle,
      success: (res) => {
        if (res.statusCode === 200) {
          // 更新平台设置
          getApp().request({
            url: `/users/${this.data.userInfo.userId}/platform-settings`,
            method: 'PUT',
            data: this.data.platformSettings,
            success: (res) => {
              wx.hideLoading();
              if (res.statusCode === 200) {
                wx.showToast({
                  title: '保存成功',
                  icon: 'success'
                });
                
                // 更新本地存储的用户信息
                const updatedUser = res.data;
                wx.setStorageSync('userInfo', updatedUser);
                this.setData({ userInfo: updatedUser });
              } else {
                wx.showToast({
                  title: '保存失败',
                  icon: 'none'
                });
              }
            },
            fail: (err) => {
              wx.hideLoading();
              wx.showToast({
                title: '网络错误',
                icon: 'none'
              });
              console.error('更新平台设置失败', err);
            }
          });
        }
      },
      fail: (err) => {
        wx.hideLoading();
        wx.showToast({
          title: '网络错误',
          icon: 'none'
        });
        console.error('更新账号风格失败', err);
      }
    });
  }
})