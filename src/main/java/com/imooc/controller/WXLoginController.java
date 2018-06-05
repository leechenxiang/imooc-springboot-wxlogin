package com.imooc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.annotation.JsonAppend.Attr;
import com.imooc.common.HttpClientUtil;
import com.imooc.common.IMoocJSONResult;
import com.imooc.common.JsonUtils;
import com.imooc.common.RedisOperator;
import com.imooc.model.WXSessionModel;

@RestController
public class WXLoginController {
	
	@Autowired
	private RedisOperator redis;
	

	@PostMapping("/wxLogin")
	public IMoocJSONResult wxLogin(String code) {
		
		System.out.println("wxlogin - code: " + code);
		
//		https://api.weixin.qq.com/sns/jscode2session?
//				appid=APPID&
//				secret=SECRET&
//				js_code=JSCODE&
//				grant_type=authorization_code
		
		String url = "https://api.weixin.qq.com/sns/jscode2session";
		Map<String, String> param = new HashMap<>();
		param.put("appid", "wxa2049f5aead89372");
		param.put("secret", "3a62d9b55028c644bacdd8412fada021");
		param.put("js_code", code);
		param.put("grant_type", "authorization_code");
		
		String wxResult = HttpClientUtil.doGet(url, param);
		System.out.println(wxResult);
		
		WXSessionModel model = JsonUtils.jsonToPojo(wxResult, WXSessionModel.class);
		
		// 存入session到redis
		redis.set("user-redis-session:" + model.getOpenid(), 
							model.getSession_key(), 
							1000 * 60 * 30);
		
		return IMoocJSONResult.ok();
	}
	
}
