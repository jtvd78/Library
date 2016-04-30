package com.hoosteen.ssl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.SocketException;

import javax.net.ssl.SSLSocket;

public class ServerConnection {
	
	InputStream is;
	OutputStream os;
	
	SSLSocket socket;
	int connectionNumber;
	boolean connected = false;
	
	public ServerConnection(SSLSocket socket, int connectionNumber) throws IOException{
		this.socket = socket;
		this.connectionNumber = connectionNumber;
		
		is = socket.getInputStream();		
		os = socket.getOutputStream();
	
		connected = true;
	}
	
	//Starts the thread to monitor the connection
	public void start() {
		new Thread(new RequestListener()).start();
	}
	
	//Closes the ServerConenction
	public void close(){		
		try {
			is.close();
			os.close();
			socket.close();
		} catch (IOException e) {
			//Do Nothing. There is nothing you can do. 
		}
		connected = false;
	}	
	
	//Worker which waits for input
	class RequestListener implements Runnable{

		public void run() {
			
			try {
				
				BufferedReader br= new BufferedReader(new InputStreamReader(is));
				
				while(connected){					
					//Print anything that gets input
					System.out.println(br.read());
				}
				
			}catch(SocketException e){	
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}finally{
				close();
			}
		}
	}
}