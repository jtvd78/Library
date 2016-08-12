package com.hoosteen.settings;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;

public abstract class SettingsWindow extends JDialog{
	
	Settings<?> settings;
	
	public SettingsWindow(JFrame owner, Settings<?> settings){
		super(owner, "Settings", true);
		
		this.settings = settings;
		
		setSize(640,480);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		addWindowListener(new CloseListener());
		
		add(new ButtonPanel(), BorderLayout.SOUTH);		
	}
	
	protected void save(){
		updateSettings();
		settings.save();
	}
	
	private class ButtonPanel extends JPanel implements ActionListener{
		
		JButton ok = new JButton("OK");
		JButton cancel = new JButton("Cancel");
		
		public ButtonPanel(){
			
			super(new BorderLayout());
			
			JPanel rightSide = new JPanel(new GridLayout(1,0));
			
			ok.addActionListener(this);
			cancel.addActionListener(this);
			
			rightSide.add(ok);
			rightSide.add(cancel);			
			
			add(rightSide, BorderLayout.EAST);
			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource() == ok){
				save();
			}
			
			//Dispose if either OK or Cancel is clicked
			dispose();
		}		
	}
	
	/**
	 * Creates a named border for sections of the settings
	 * @param Text to set as the title of the border
	 * @return The new border
	 */
	protected CompoundBorder createBorder(String text){
		return BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(text),BorderFactory.createEmptyBorder(5, 5, 5, 5));
	}	
	
	/**
	 * Refreshes the settings options within the window. Called whenever the window is shown, 
	 * so that the window always has the most up to date settings
	 */
	public abstract void refreshOptions();
	
	/**
	 * Updates the settings file whenever the window is closed or the user presses OK
	 */
	protected abstract void updateSettings();
	
	/**
	 * Used solely for the purpose of detecting when the window closes
	 * Saves the settings when the user closes the window	
	 * @author Justin
	 *
	 */
	private class CloseListener implements WindowListener{	

		@Override
		public void windowClosing(WindowEvent arg0) {
			save();
		}
		
		@Override
		public void windowClosed(WindowEvent arg0) {}		
		@Override
		public void windowDeactivated(WindowEvent arg0) {}
		@Override
		public void windowDeiconified(WindowEvent arg0) {}
		@Override
		public void windowIconified(WindowEvent arg0) {}
		@Override
		public void windowOpened(WindowEvent arg0) {}
		@Override
		public void windowActivated(WindowEvent arg0) {}		
	}
}