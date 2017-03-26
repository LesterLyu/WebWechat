package com.lvds2000.api.auth;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
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
	
	private OkHttpClient client;
	
	public Auth(OkHttpClient client){
		this.client = client;
	}
	
	public boolean login(){
		return false;
	}
	
	
	public String getUuid(){
		String url = UUID_URL;
		int code = -1;
		String uuid = "";
		
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
	
	
}
