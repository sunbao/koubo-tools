Page({
  data: {
    userInfo: null,
    isLoggedIn: false,
    hotspots: [],
    scripts: [],
    loading: false
  },

  onLoad() {
    // 页面加载时检查登录状态
    this.checkLoginStatus();
  },

  onShow() {
    // 页面显示时刷新数据
    if (this.data.isLoggedIn) {
      this.loadDashboardData();
    }
  },

  checkLoginStatus() {
    const token = wx.getStorageSync('token');
    const userInfo = wx.getStorageSync('userInfo');
    
    if (token && userInfo) {
      this.setData({
        isLoggedIn: true,
        userInfo: userInfo
      });
      this.loadDashboardData();
    }
  },

  loadDashboardData() {
    this.setData({ loading: true });
    
    // 获取热点数据
    getApp().request({
      url: '/hotspots',
      success: (res) => {
        if (res.statusCode === 200) {
          // 取前3个热点
          const hotspots = res.data.slice(0, 3);
          this.setData({ hotspots: hotspots });
        }
      },
      fail: (err) => {
        console.error('获取热点数据失败', err);
      }
    });
    
    // 获取文案数据
    getApp().request({
      url: '/scripts',
      success: (res) => {
        if (res.statusCode === 200) {
          // 取前3个文案
          const scripts = res.data.slice(0, 3);
          this.setData({ 
            scripts: scripts,
            loading: false
          });
        }
      },
      fail: (err) => {
        console.error('获取文案数据失败', err);
        this.setData({ loading: false });
      }
    });
  },

  goToLogin() {
    wx.navigateTo({
      url: '/pages/profile/profile'
    });
  },

  goToHotspots() {
    wx.navigateTo({
      url: '/pages/hotspots/hotspots'
    });
  },

  goToScripts() {
    wx.navigateTo({
      url: '/pages/scripts/scripts'
    });
  },

  goToTeleprompter() {
    wx.navigateTo({
      url: '/pages/teleprompter/teleprompter'
    });
  },

  generateScript(e) {
    const hotspotId = e.currentTarget.dataset.id;
    
    if (!this.data.userInfo) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      return;
    }
    
    wx.showLoading({
      title: '生成中...'
    });
    
    // 调用后端API生成文案
    getApp().request({
      url: '/scripts/generate',
      method: 'POST',
      data: {
        userId: this.data.userInfo.userId,
        hotspotId: hotspotId
      },
      success: (res) => {
        wx.hideLoading();
        if (res.statusCode === 200) {
          wx.showToast({
            title: '生成成功',
            icon: 'success'
          });
          // 刷新数据
          this.loadDashboardData();
        } else {
          wx.showToast({
            title: '生成失败',
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
        console.error('生成文案失败', err);
      }
    });
  }
})