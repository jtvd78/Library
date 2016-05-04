package com.hoosteen.file;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.hoosteen.Tools;
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
	
	public File getFile(){
		return file;
	}
}


class SoundFileNode extends FileNode{

	public SoundFileNode(File file) {
		super(file);
	}
	
}