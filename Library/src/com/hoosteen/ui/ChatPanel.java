package com.hoosteen.ui;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

public abstract class ChatPanel extends JPanel{
	
	private ChatLog chat;
	private InputPanel input;
	
	public ChatPanel(){
		super(new BorderLayout());
		
		chat = new ChatLog();
		input = new InputPanel();
		
		//Divider between Top and Bottom
		JSplitPane vert = new JSplitPane(JSplitPane.VERTICAL_SPLIT,chat, input);
		vert.setResizeWeight(1);
		
		add(vert,BorderLayout.CENTER);		
	}
	
	public abstract void send(String str);
	
	protected void updateChat(String str){
		chat.updateChat(str);
	}
	
	public class ChatLog extends JPanel{
		
		JTextArea chatLogTextArea;
		
		public ChatLog(){
			super(new BorderLayout());
			
			//Main Chat Text Area
			chatLogTextArea = new JTextArea();
			chatLogTextArea.setEditable(false);
			chatLogTextArea.setMargin(new Insets(10,10,10,10));
			chatLogTextArea.setWrapStyleWord(true);
			chatLogTextArea.setLineWrap(true);
			
			//JScroll Panes
			JScrollPane chatScrollPane = new JScrollPane(chatLogTextArea);
			add(chatScrollPane,BorderLayout.CENTER);
		}
		
		void updateChat(String str){
			chatLogTextArea.append(str+"\n");
			chatLogTextArea.setCaretPosition(chatLogTextArea.getDocument().getLength());
		}		
	}
	
	public class InputPanel extends JPanel{
		
		JTextArea inputTextArea;
		
		public InputPanel(){
			super(new BorderLayout());
			
			//Input Text Area
			inputTextArea = new JTextArea();
			inputTextArea.setEditable(true);
			inputTextArea.setMargin(new Insets(10,10,10,10));
			inputTextArea.setWrapStyleWord(true);
			inputTextArea.setLineWrap(true);
			
			
			//Event that occurs when "Send" button is pressed
			Action action = new AbstractAction(){
				public void actionPerformed(ActionEvent arg0) {
					if(inputTextArea.getText().equals("")){
						return;
					}
					send(inputTextArea.getText());
					inputTextArea.setText("");
				}
			};
			
			//Send Button
			JButton send = new JButton("Send");		
			send.addActionListener(action);
			
			//Send when ENTER is pressed
			KeyStroke keyStroke = KeyStroke.getKeyStroke("ENTER");
			InputMap im = inputTextArea.getInputMap();
			inputTextArea.getActionMap().put(im.get(keyStroke), action);		
			
			//Input Scroll Pane
			add(new JScrollPane(inputTextArea));		
			add(send,BorderLayout.EAST);
		}
	}	
}