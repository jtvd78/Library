package com.hoosteen.ui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Console extends JFrame{
	
	JTextArea ta;
	PrintStream ps;
	
	public static Console out;	
	
	private Console(){
		ps = new TextAreaPrintStream();
		
		ta = new JTextArea();
		ta.setEditable(false);
		ta.setWrapStyleWord(true);
		ta.setLineWrap(true);
		
		JScrollPane sp = new JScrollPane(ta);
		add(sp);
		
		setTitle("Server");
		setSize(800,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void init(){
		Console.out = new Console();
	}
	
	public static String getTime(){
		return new SimpleDateFormat("h:m:s a").format(new Date(System.currentTimeMillis()));
	}
	
	public PrintStream getPrintStream(){
		return ps;
	}
	
	public class TextAreaPrintStream extends PrintStream{
		public TextAreaPrintStream() {
			super(new TextAreaOutputStream());
		}
	}
	
	public class TextAreaOutputStream extends OutputStream{

		@Override
		public void write(int b) throws IOException {
			writeChar((char) b);
		}
	}
	
	//Print Methods
	public void writeChar(char c){
		ta.append(Character.toString(c));
		ta.setCaretPosition(ta.getDocument().getLength());
	}
	
	public void print(short s){
		printTime();
		ta.append(Short.toString(s));
		ta.setCaretPosition(ta.getDocument().getLength());
	}
	
	public void print(int i){
		printTime();
		ta.append(Integer.toString(i));
		ta.setCaretPosition(ta.getDocument().getLength());
	}
	
	public void print(long l){
		printTime();
		ta.append(Long.toString(l));
		ta.setCaretPosition(ta.getDocument().getLength());
	}
	
	public void print(float f){
		printTime();
		ta.append(Float.toString(f));
		ta.setCaretPosition(ta.getDocument().getLength());
	}
	
	public void print(double d){
		printTime();
		ta.append(Double.toString(d));
		ta.setCaretPosition(ta.getDocument().getLength());
	}
	
	public void print(boolean b){
		printTime();
		ta.append(Boolean.toString(b));
		ta.setCaretPosition(ta.getDocument().getLength());
	}
	
	public void print(char c){
		printTime();
		ta.append(Character.toString(c));
		ta.setCaretPosition(ta.getDocument().getLength());
	}
		
	public void print(String s){
		printTime();
		ta.append(s);
		ta.setCaretPosition(ta.getDocument().getLength());
	}
	
	public void println(short s){
		printTime();
		ta.append(Short.toString(s) + "\n");
		ta.setCaretPosition(ta.getDocument().getLength());
	}
	
	public void println(int i){
		printTime();
		ta.append(Integer.toString(i) + "\n");
		ta.setCaretPosition(ta.getDocument().getLength());
	}
	
	public void println(long l){
		printTime();
		ta.append(Long.toString(l) + "\n");
		ta.setCaretPosition(ta.getDocument().getLength());
	}
	
	public void println(float f){
		printTime();
		ta.append(Float.toString(f) + "\n");
		ta.setCaretPosition(ta.getDocument().getLength());
	}
	
	public void println(double d){
		printTime();
		ta.append(Double.toString(d) + "\n");
		ta.setCaretPosition(ta.getDocument().getLength());
	}
	
	public void println(boolean b){
		printTime();
		ta.append(Boolean.toString(b) + "\n");
		ta.setCaretPosition(ta.getDocument().getLength());
	}
	
	public void println(char c){
		printTime();
		ta.append(Character.toString(c) + "\n");
		ta.setCaretPosition(ta.getDocument().getLength());
	}
		
	public void println(String s){
		printTime();
		ta.append(s + "\n");
		ta.setCaretPosition(ta.getDocument().getLength());
	}
	
	public void printTime(){
		ta.append("[" + getTime() + "] ");
	}

	public void println(Object obj) {
		printTime();
		ta.append(obj.toString() + "\n");
		ta.setCaretPosition(ta.getDocument().getLength());
	}
}
