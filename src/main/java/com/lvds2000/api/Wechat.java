package com.lvds2000.api;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.lvds2000.api.auth.Auth;
import com.lvds2000.logging.AppLogger;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Web Wechat object
 *
 */
public class Wechat {

	private OkHttpClient client;
	
	private WechatCookieJar cookieJar;
	
	public Auth auth;

	public Wechat(){
		// disable handshake alert
		System.setProperty("jsse.enableSNIExtension", "false");
		cookieJar = new WechatCookieJar();
		this.client = new OkHttpClient.Builder()
				.cookieJar(cookieJar)
				.build();
		auth = new Auth(client);
	}
	
	public void login(){
		String uuid = auth.getQRuuid();
		new Thread(new Runnable(){
			 @Override
			 public void run(){
				 JOptionPane.showConfirmDialog(null, null, "Scan to log in to WeChat", JOptionPane.DEFAULT_OPTION, 0, new ImageIcon(auth.getQR(uuid)));
			 }
		}).start();
		boolean loggedIn = false;
		while(!loggedIn){
			int status = auth.checkLogin(uuid);
			System.out.println(status);
			if(status == 200)
				loggedIn = true;
			else if(status == 201){
				//if(loggedIn != null){
				//	AppLogger.getLogger().info("Please press confirm on your phone.");
				//	loggedIn = null;
				//}
			}
			//else if(status != 408)
			//	break;
		}
		AppLogger.getLogger().info("Logged in!");
	}
	
	
	public static void main(String[] args) throws SecurityException, IOException {
		System.out.println( "Hello World!" );
		AppLogger.setUp();
		Wechat w = new Wechat();
		w.login();
		
	}
}
