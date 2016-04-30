package com.hoosteen.ssl;
import java.io.IOException;
import java.util.ArrayList;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class SSLServer {
	
	boolean ready = false;
	boolean running = true;
	
	int connectionCounter = 0;
	
	ArrayList<ServerConnection> connectionList = new ArrayList<ServerConnection>();
	
	public void begin(){
		new Thread(new ServerWorker()).start();
			
	}
	
	class ServerWorker implements Runnable{
		
		public void run(){
			SSLServerSocketFactory factory = SSL.getSSLServerSocketFactory();
			try {
				SSLServerSocket ss = (SSLServerSocket) factory.createServerSocket(31415);
				printSuites(ss);				
				
				//Disable everything but the good stuff
				//I commented this out in order to simplify the code for debugging
		//		ss.setEnabledCipherSuites(new String[]{"TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256"});	
				
				//Sets ready to true so the client can start
				ready = true;
				
				while(running){
					
					//Creates the socket
					SSLSocket sock = (SSLSocket) ss.accept();
					
					connectionCounter++;
					
					//Starts a new Server connection
					//A server connection runs in a new thread so that the
					//serverSocket can accept new connections
					ServerConnection c = new ServerConnection(sock,connectionCounter);
					c.start();
					connectionList.add(c);					
				}
				
				//Close serverSocket after finishing
				ss.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	//Returns ready
	//The SSLServer is ready when the ServerSocket is created
	public boolean isReady() {
		return ready;
	}
	
	private void printSuites(SSLServerSocket serverSocket){
		String[] supported = serverSocket.getSupportedCipherSuites();
		String[] enabled = serverSocket.getEnabledCipherSuites();
		
		System.out.println("=====Server Suites=====");
		for(String s : supported){
			if(s.contains("AES")){
				
				boolean contains = false;
				for(String ss : enabled){
					if(ss.equals(s)){
						contains = true;
					}
				}				
				
				System.out.println(s + " : " + contains);
			}			
		}	
	}
}