package com.hoosteen.ssl;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;

public class SSL {
	
	private static SSLContext context;
	
	//I believe that my problem is here,
	//when I init the context with (null, null, null)
	public static SSLContext getSSLContext(){
		if(context == null){
			try {
				context = SSLContext.getInstance("TLSv1.2");
				context.init(null,null,null);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (KeyManagementException e) {
				e.printStackTrace();
			}			
		}
		return context;
	}
	
	/**
	 * Gets a Server Socket Factory
	 * @return
	 */
	public static SSLServerSocketFactory getSSLServerSocketFactory(){
		return getSSLContext().getServerSocketFactory();
	}
	
	/**
	 * Gets a Socket Factory
	 * @return
	 */
	public static SSLSocketFactory getSSLSocketFactory(){
		return getSSLContext().getSocketFactory();
	}
}