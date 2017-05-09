package com.lvds2000.api.auth;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.lvds2000.logging.AppLogger;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Auth {
	
	private static final String APPID = "wx782c26e4c19acffb"; // web wechat appid
	
	private static final String UUID_URL = "https://login.weixin.qq.com/jslogin";
	
	private static final String QR_URL = "https://login.weixin.qq.com/qrcode/";
	
	private static final String CHECK_LOGIN_URL = "https://login.weixin.qq.com/cgi-bin/mmwebwx-bin/login";
	
	private OkHttpClient client;
	
	private Map<String, String> loginInfo;
	
	public Auth(OkHttpClient client){
		this.client = client;
		loginInfo = new HashMap<String, String>();
	}
	
	public boolean login(){
		return false;
	}
	
	
	public String getUuid(){
		int code = -1;
		String uuid = "";
		
		FormBody.Builder formBuilder = new FormBody.Builder()
				.add("appid", APPID)
				.add("fun", "new")
				.add("lang", "zh_CN")
				.add("_", System.currentTimeMillis() + "");
		RequestBody formBody = formBuilder.build();
		
		Request request = new Request.Builder()
				.url(UUID_URL)
				.post(formBody)
				.build();

		try {
			Response response = client.newCall(request).execute();
			String pattern = "window.QRLogin.code = (\\d+); window.QRLogin.uuid = \"(\\S+?)\";";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(response.body().string());
			if(m.find()){
				code = Integer.parseInt(m.group(1));
				uuid = m.group(2);
			}
			AppLogger.getLogger().info("code: " + code + ", " + "uuid: " + uuid);
			return uuid;
		} catch(Exception e){
			e.printStackTrace();
		}
		return uuid;
	}
	
	
	public BufferedImage getQR(String uuid){
		BufferedImage img = null;
		try {
			img = ImageIO.read(new URL(QR_URL + uuid));
		} catch (IOException e) {
			AppLogger.getLogger().warning(e.getMessage());
			e.printStackTrace();
		}
		return img;
	}
	
	
	public int checkLogin(String uuid){
		int code;
		long currentTime = System.currentTimeMillis();
		
		FormBody.Builder formBuilder = new FormBody.Builder()
				.add("loginicon", "true")
				.add("uuid", uuid)
				.add("tip", "0")
				.add("r", currentTime / 1579 + "")
				.add("_", currentTime + "");
		RequestBody formBody = formBuilder.build();
		
		Request request = new Request.Builder()
				.url(CHECK_LOGIN_URL)
				.post(formBody)
				.build();

		try {
			Response response = client.newCall(request).execute();
			String res = response.body().string();
			System.out.println(res);
			String pattern = "window.code=(\\d+)";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(res);
			if(m.find()){
				code = Integer.parseInt(m.group(1));
				if(code == 200){
					processLoginInfo(res);
				}
			}
			else{
				code = 400;
			}
			AppLogger.getLogger().info("code: " + code);
			return code;
		} catch(java.net.SocketTimeoutException e){
			System.out.println(e.getMessage());
		} catch(Exception e){
			e.printStackTrace();
		}
		return 400;
	}
	
	/**
	 * when finish login (scanning qrcode)
	 * syncUrl and fileUploadingUrl will be fetched
	 * deviceid and msgid will be generated
	 * skey, wxsid, wxuin, pass_ticket will be fetched
	 * @param loginContent
	 */
	public void processLoginInfo(String loginContent){
		 String regx = "window.redirect_uri=\"(\\S+)\";";
		 
		 
		 
	}
	
	
}
