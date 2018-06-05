const app = getApp()

Page({
  data: {
  },

  onLoad: function (params) {

  },

  // 登录  
  doLogin: function (e) {
    console.log(e.detail.errMsg)
    console.log(e.detail.userInfo)
    console.log(e.detail.rawData)

    wx.login({
      success: function(res) {
        console.log(res)
        // 获取登录的临时凭证
        var code = res.code;
        // 调用后端，获取微信的session_key, secret
        wx.request({
          url: "http://192.168.1.2:8080/wxLogin?code=" + code,
          method: "POST",
          success: function(result) {
            console.log(result);
            // 保存用户信息到本地缓存，可以用作小程序端的拦截器
            app.setGlobalUserInfo(e.detail.userInfo);
            wx.redirectTo({
              url: '../index/index',
            })
          }
        })


      }
    })
  }
})