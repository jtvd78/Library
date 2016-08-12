package com.hoosteen.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class SettingsLoader{
	
	//Nobody can make one of these
	private SettingsLoader(){
		
	}

	public static <E extends Settings<?>> E loadSettings(Class<E> settingsClass, String programName, String version) {
		
		String settingsPath = getSettingsPath(programName, version);
		File settingsFile = new File(settingsPath + "Settings.dat");
		
		E settingsOut = null;
		
		if(!settingsFile.exists()){
			System.out.println("Settings does not Exist. Creating Settings File");
			new File(settingsPath).mkdirs();
			settingsOut = makeSettings(settingsFile, settingsClass, programName, version);
		}else{
			
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(new FileInputStream(settingsFile));
				settingsOut = settingsClass.cast(ois.readObject());
			} catch(InvalidClassException e){
				settingsOut = makeSettings(settingsFile, settingsClass, programName, version);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				settingsOut = makeSettings(settingsFile, settingsClass, programName, version);
			} finally{
				if(ois != null){
					try {
						ois.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}				
			}			
		}	
		
		return settingsOut;
	}
	
	private static <E extends Settings<?>> E makeSettings(File settingsFile, Class<E> settingsClass, String programName, String version){
		
		try {
			settingsFile.delete();
			settingsFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			
			E s = settingsClass.newInstance();
			
			s.setProgramName(programName);
			s.setVersion(version);
			
			saveSettings(settingsFile, s);

			return s;
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public static <E extends Settings<?>> void saveSettings(E settings, String programName, String version){
		
		File settingsFile = new File(getSettingsPath(programName, version) + "Settings.dat");
		saveSettings(settingsFile, settings);

	}
	
	private static <E extends Settings<?>>  void saveSettings(File settingsFile, E settings){
		
		
		ObjectOutputStream oos = null;
		try {
			
			oos = new ObjectOutputStream(new FileOutputStream(settingsFile));
			oos.writeObject(settings);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(oos != null){
				try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}				
		}	
	}
	
	private static String getSettingsPath(String programName, String version){
		return System.getProperty("user.home") + File.separator + "." + programName 
		+ File.separator + version + File.separator;
	}
}