package com.hoosteen.graphics;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class TaskWindow extends JDialog{
	
	Task task;
	
	Frame owner;
	JLabel message;
	JProgressBar bar;
	
	Thread thread;
	
	public TaskWindow(Frame owner, String title){
		super(owner, title, true);
		
		this.owner = owner;
		
		setSize(400,100);
		setLocationRelativeTo(owner);
		addComponentListener(new CompListener());
		
		message = new JLabel("");
		message.setAlignmentX(Component.CENTER_ALIGNMENT);
		bar = new JProgressBar(0,1000);
		
		Container content = this.getContentPane();		
		
		JPanel p = new JPanel();
		
		BoxLayout layout = new BoxLayout(p, BoxLayout.PAGE_AXIS);
		
		p.setLayout(layout);
		p.add(message);
		p.add(bar);
		
		content.add(p, BorderLayout.CENTER);
		
	}
	
	public void runTask(Task task){
		task.setTaskWindow(this);
		this.task = task;		
		thread = new Thread(task);
		thread.start();
		
		setVisible(true);
		
	}
	
	class CompListener implements ComponentListener{

		
		//End the task if they close the window
		@Override
		public void componentHidden(ComponentEvent arg0) {
			task.interrupt();
			dispose();
		}

		@Override
		public void componentMoved(ComponentEvent arg0) {}

		@Override
		public void componentResized(ComponentEvent arg0) {}

		@Override
		public void componentShown(ComponentEvent arg0) {}
		
	}
	
	public void updateProgress(String message, double d){		
		this.message.setText(message);		
		bar.setValue((int) (1000*d));
	}
	
	public abstract class Task implements Runnable{

		protected TaskWindow window;
		protected boolean interrupted = false;
		
		@Override
		public void run(){
			if(window != null){
				begin();
			}
			TaskWindow.this.dispose();
			owner.repaint();
		}
		
		public void interrupt() {			
			interrupted = true;
		}

		public abstract void begin();
		
		public void setTaskWindow(TaskWindow window){
			this.window = window;
		}
	}
}