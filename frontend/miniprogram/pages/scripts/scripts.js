Page({
  data: {
    scripts: [],
    loading: false,
    selectedScript: null
  },

  onLoad() {
    this.loadScripts();
  },

  onShow() {
    // 页面显示时刷新数据
    this.loadScripts();
  },

  loadScripts() {
    this.setData({ loading: true });
    
    getApp().request({
      url: '/scripts',
      success: (res) => {
        if (res.statusCode === 200) {
          this.setData({ scripts: res.data });
        }
      },
      fail: (err) => {
        console.error('获取文案数据失败', err);
        wx.showToast({
          title: '获取数据失败',
          icon: 'none'
        });
      },
      complete: () => {
        this.setData({ loading: false });
      }
    });
  },

  selectScript(e) {
    const scriptId = e.currentTarget.dataset.id;
    const script = this.data.scripts.find(s => s.scriptId === scriptId);
    this.setData({ selectedScript: script });
  },

  goToTeleprompter(e) {
    const scriptId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/teleprompter/teleprompter?scriptId=${scriptId}`
    });
  },

  deleteScript(e) {
    const scriptId = e.currentTarget.dataset.id;
    
    wx.showModal({
      title: '确认删除',
      content: '确定要删除这个文案吗？',
      success: (res) => {
        if (res.confirm) {
          getApp().request({
            url: `/scripts/${scriptId}`,
            method: 'DELETE',
            success: (res) => {
              if (res.statusCode === 204) {
                wx.showToast({
                  title: '删除成功',
                  icon: 'success'
                });
                // 重新加载数据
                this.loadScripts();
              } else {
                wx.showToast({
                  title: '删除失败',
                  icon: 'none'
                });
              }
            },
            fail: (err) => {
              console.error('删除文案失败', err);
              wx.showToast({
                title: '网络错误',
                icon: 'none'
              });
            }
          });
        }
      }
    });
  }
})