Page({
  data: {
    hotspots: [],
    platforms: ['全部', '抖音', '快手'],
    selectedPlatform: '全部',
    types: ['全部', '科技', '美食', '旅游', '娱乐'],
    selectedType: '全部',
    loading: false,
    refreshing: false
  },

  onLoad() {
    this.loadHotspots();
  },

  onPullDownRefresh() {
    this.setData({ refreshing: true });
    this.loadHotspots(() => {
      wx.stopPullDownRefresh();
      this.setData({ refreshing: false });
    });
  },

  loadHotspots(callback) {
    this.setData({ loading: true });
    
    let url = '/hotspots';
    if (this.data.selectedPlatform !== '全部') {
      url = `/hotspots/platform/${this.data.selectedPlatform}`;
    }
    
    getApp().request({
      url: url,
      success: (res) => {
        if (res.statusCode === 200) {
          this.setData({ hotspots: res.data });
        }
      },
      fail: (err) => {
        console.error('获取热点数据失败', err);
        wx.showToast({
          title: '获取数据失败',
          icon: 'none'
        });
      },
      complete: () => {
        this.setData({ loading: false });
        if (callback) callback();
      }
    });
  },

  onPlatformChange(e) {
    const platform = this.data.platforms[e.detail.value];
    this.setData({ selectedPlatform: platform });
    this.loadHotspots();
  },

  onTypeChange(e) {
    const type = this.data.types[e.detail.value];
    this.setData({ selectedType: type });
    // 这里可以添加按类型筛选的逻辑
  },

  generateScript(e) {
    const hotspotId = e.currentTarget.dataset.id;
    
    wx.showLoading({
      title: '生成中...'
    });
    
    // 调用后端API生成文案
    getApp().request({
      url: '/scripts/generate',
      method: 'POST',
      data: {
        userId: wx.getStorageSync('userInfo').userId,
        hotspotId: hotspotId
      },
      success: (res) => {
        wx.hideLoading();
        if (res.statusCode === 200) {
          wx.showToast({
            title: '生成成功',
            icon: 'success'
          });
          // 跳转到文案页面
          wx.navigateTo({
            url: '/pages/scripts/scripts'
          });
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