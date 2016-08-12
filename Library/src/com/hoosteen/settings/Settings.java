package com.hoosteen.settings;

import java.io.Serializable;

import javax.swing.JFrame;

public abstract class Settings<T extends SettingsWindow> implements Serializable{
	
	String version;
	String programName;

	static SettingsWindow settingsWindow;
	
	public String getVersion(){
		return version;
	}
	
	public String getProgramName(){
		return programName;
	}	
	
	void setVersion(String version){
		this.version = version;
	}
	
	void setProgramName(String programName){
		this.programName = programName;
	}	
	
	protected abstract T getSettingsWindow(JFrame owner);
	
	public void showSettingsWindow(JFrame owner){
		if(settingsWindow == null){		
			settingsWindow = getSettingsWindow(owner);
		}
		
		settingsWindow.refreshOptions();
		settingsWindow.setLocationRelativeTo(owner);
		settingsWindow.setVisible(true);
	}	
	
	public void save(){
		SettingsLoader.saveSettings(this, getProgramName(), getVersion());
	}

	public static <E extends Settings<?>> void saveSettings(E settings, String programName, String version){
		
		SettingsLoader.saveSettings(settings, programName, version);
	}
	
	public static <E extends Settings<?>> E loadSettings(Class<E> settingsClass, String programName, String version){
		
		return SettingsLoader.loadSettings(settingsClass, programName, version);
		
	}
}