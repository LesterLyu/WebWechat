package com.lvds2000;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Hello world!
 *
 */
public class Wechat {

	private static final String APPID = "wx782c26e4c19acffb";

	private OkHttpClient client;
	
	private WechatCookieJar cookieJar;

	public Wechat(){
		System.setProperty("jsse.enableSNIExtension", "false");
		cookieJar = new WechatCookieJar();
		this.client = new OkHttpClient.Builder()
				.cookieJar(cookieJar)
				.build();
	}
	
	public boolean login(){
		String url = "https://login.weixin.qq.com/jslogin";
		
		FormBody.Builder formBuilder = new FormBody.Builder()
				.add("appid", APPID)
				.add("fun", "new")
				.add("lang", "zh_CN")
				.add("_", System.currentTimeMillis() + "");
		RequestBody formBody = formBuilder.build();
		
		Request request = new Request.Builder()
				.url(url)
				.post(formBody)
				.build();

		try {
			Response response = client.newCall(request).execute();

			System.out.println(response.body().string());
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	


	public static void main( String[] args ) {
		System.out.println( "Hello World!" );
		Wechat w = new Wechat();
		w.login();
	}
}
