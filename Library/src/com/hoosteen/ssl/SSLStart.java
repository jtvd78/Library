package com.hoosteen.ssl;

import java.security.Provider;
import java.security.Security;

public class SSLStart {
	
	public static void main(String[] args) {
		
		//Print all providers for fun
		//I Believe that SunJSSE version 1.8 is chosen by default
		Provider[] provs = Security.getProviders();
		
		System.out.println("Available Providers: ");
		for(Provider p : provs){
			System.out.println(p);
		}
		
		System.out.println();
		
		//Start the program
		new SSLStart().begin();		
		
	}
	
	public void begin(){
		//Create both the client and the server
		SSLClient client = new SSLClient();
		SSLServer server = new SSLServer();
		
		//Start the server
		server.begin();
		
		//Wait until the server is ready
		while(!server.isReady()){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Start the client
		client.begin();
	}
}
