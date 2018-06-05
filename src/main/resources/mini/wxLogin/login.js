const app = getApp()

Page({
  data: {
  },

  onLoad: function (params) {
    
  },

  onGotUserInfo: function (e) {
    console.log(e.detail.errMsg)
    console.log(e.detail.userInfo)
    console.log(e.detail.rawData)

    this.doLogin();
  },

  // 登录  
  doLogin: function (e) {
    var me = this;
    
    //调用登录接口，获取 code
    wx.login({
      success: function (res) {
        wx.getSetting({
          success: function (setRes) {
            
            // 判断是否已授权
            if (!setRes.authSetting['scope.userInfo']) {
              
              // 跳转到登录页面，让用户去做授权
            } else {
              
              //获取用户信息
              wx.getUserInfo({
                lang: "zh_CN",
                success: function (userRes) {
                  
                  //发起网络请求
                  console.log("获取用户信息：" + userRes);
                  wx.login({
                    success: function (res) {
                      console.log(res);
                      if (res.code) {
                        // 发起网络请求
                        wx.request({
                          url: app.serverUrl + '/wxLogin?code=' + res.code,
                          method: "POST",
                          success:function() {
                            // 保存用户信息到缓存
                            app.setGlobalUserInfo(userRes);
                          }
                        })
                        
                      } else {
                        console.log('登录失败！' + res.errMsg)
                      }
                    }
                  });
                }
              })
            }
          }
        })
      }
    })
  }
})