package com.hoosteen.file;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.hoosteen.Tools;
import com.hoosteen.tree.Node;

public class FileNode extends Node{

	File file;
	boolean directory;
	boolean childrenLoaded;
	
	public FileNode(File file) {
		super(false);
		
		this.file = file;
		
		if(file.isDirectory()){
			directory = true;
			childrenLoaded = false;
		}else{
			directory = false;
			childrenLoaded = true;
		}
	}
	
	@Override
	public int size(){
		if(childrenLoaded){
			return super.size();
		}else{
			String[] list = file.list();
			if(list != null){
				return list.length;
			}else{
				return 0;
			}
		}
	}
	
	@Override
	public void setExpanded(boolean expanded){
		
		if(!childrenLoaded && expanded){
			Runnable r = new Runnable(){

				@Override
				public void run() {
					
					loadChildren();
					FileNode.super.setExpanded(expanded);
				}
				
			};
			
			new Thread(r).start();
		}else{
			super.setExpanded(expanded);
		}
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
			
			if(f.isDirectory()){
				addNode(new FileNode(f));
			}else{
				String name = f.getName();				
				String fileType = name.substring(name.indexOf(".") + 1, name.length());
				
				if(fileType.equals("wav") || fileType.equals("mp3") && !f.isDirectory()){
					addNode(new SoundFileNode(f));
				}else{
					addNode(new FileNode(f));
				}
			}		
		}
		
		childrenLoaded = true;
	}
	
	public void addNode(Node n){
		super.addNode(n);		
	}
	
	public File getFile(){
		return file;
	}
}


class SoundFileNode extends FileNode{

	public SoundFileNode(File file) {
		super(file);
		
		this.addRightClickOption("Load Sound");
	}
}