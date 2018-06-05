package com.imooc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.common.HttpClientUtil;
import com.imooc.common.IMoocJSONResult;
import com.imooc.common.JsonUtils;
import com.imooc.common.RedisOperator;
import com.imooc.model.WXSessionModel;

@RestController
public class WXLoginController {
	
	@Autowired
	public RedisOperator redis;
	
	public static final String USER_REDIS_SESSION = "user-redis-session";
	
	@PostMapping("/wxLogin")
	public IMoocJSONResult wxLogin(String code) throws Exception {
		
		System.out.println(code);
		
//		https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code
		
		String url = "https://api.weixin.qq.com/sns/jscode2session";
		Map<String, String> param = new HashMap<>();
		param.put("appid", "wxa2049f5aead89372");
		param.put("secret", "0084de7c6e56170b095b575460641b5c");
		param.put("js_code", code);
		param.put("grant_type", "authorization_code");
		
		String wxResult = HttpClientUtil.doGet(url, param);
		System.out.println(wxResult);
		
		WXSessionModel model = JsonUtils.jsonToPojo(wxResult, WXSessionModel.class);
			
		redis.set(USER_REDIS_SESSION + ":" + model.getOpenid(), 
								model.getSession_key(), 
								1000 * 60 * 30);
		
		return IMoocJSONResult.ok();
	}
	
}
