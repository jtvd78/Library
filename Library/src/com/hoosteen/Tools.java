package com.hoosteen;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Some random stuff that I use. 
 * @author justin
 *
 */
public class Tools  {
	
	/**
	 * @param arr - Array to print
	 * @param spacer - Spacer String to append between the array values
	 * @return Returns each element in the array, separated by a spacer. 
	 */
	public static String arrToString(Object[] arr, String spacer){
		if(arr == null){
			return null;
		}
		
		StringBuffer out = new StringBuffer("");
		for(int i = 0; i < arr.length; i++){
			out.append(arr[i].toString());
			if(i < arr.length -1){
				out.append(spacer);
			}
		}
		
		return out.toString();
	}
	
	/**
	 * @return Returns a random color
	 */
	public static Color getRandomColor(){
		Random random = new Random();		
		return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}

	/**
	 * Displays a string of text within a text box
	 * @param description - Text to display
	 * @param title - Title of window
	 */
	public static void displayText(String description, String title) {
		JTextArea jta = new JTextArea(description);
		jta.setLineWrap(true);
		jta.setWrapStyleWord(true);
        JScrollPane jsp = new JScrollPane(jta);
        jsp.setPreferredSize(new Dimension(480, 320));
        JOptionPane.showMessageDialog( null, jsp, title, JOptionPane.DEFAULT_OPTION);	
	}

	/**
	 * Makes the window look nice
	 */
	public static void setNativeUI(){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
}