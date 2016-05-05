package com.hoosteen.file;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.hoosteen.tree.Node;

public class FileNode extends Node{

	File file;
	boolean directory;
	boolean childrenLoaded;
	
	public FileNode(File file) {
		super(true);
		
		this.file = file;
		
		if(file.isDirectory()){
			directory = true;
			childrenLoaded = false;
		}else{
			directory = false;
			childrenLoaded = true;
		}
		
		loadChildren();
		
	}
	
	@Override
	public String toString(){
		return file.getName();
	}
	
	public void loadChildren(){
		if(childrenLoaded){
			return;
		}
		
		for(File f : file.listFiles()){
			addNode(new FileNode(f));
		}
		
		childrenLoaded = true;
	}
	
	ArrayList<JMenuItem> menuItems = new ArrayList<JMenuItem>();
	
	public void addNode(Node n){
		super.addNode(n);
		
		
	}
	
	public void addRightClickOption(JMenuItem item){
		menuItems.add(item);
	}
	
	@Override
	public void addPopupMenuOptions(JPopupMenu popupMenu){
		super.addPopupMenuOptions(popupMenu);
		
		for(JMenuItem item : menuItems){
			popupMenu.add(item);
		}
	}
	
	public File getFile(){
		return file;
	}
}


class SoundFileNode extends FileNode{

	public SoundFileNode(File file) {
		super(file);
	}
}