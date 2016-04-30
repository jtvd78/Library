package com.hoosteen.ssl;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLClient {
	
	public void begin(){
		SSLSocketFactory factory = SSL.getSSLSocketFactory();
		try {
			SSLSocket socket = (SSLSocket) factory.createSocket(InetAddress.getByName("localhost"), 31415);
			printSuites(socket);
			
			//Disable everything but the good stuff
			//I commented this out in order to simplify the code for debugging
		//	socket.setEnabledCipherSuites(new String[]{"TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256"});	
			
			//Make a bufferedWriter
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			//Send any input text to the server
			Scanner scanner = new Scanner(System.in);
			while(true){
				bw.write(scanner.nextLine());
				bw.flush();
			}			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void printSuites(SSLSocket socket){
		String[] supported = socket.getSupportedCipherSuites();
		String[] enabled = socket.getEnabledCipherSuites();
		
		System.out.println("=====Socket Suites=====");
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