package com.hoosteen.file;

import javax.swing.JFrame;

import com.hoosteen.tree.TreeComp;

public class FileComp extends TreeComp{

	public FileComp(JFrame parentFrame, FileNode fileTreeRoot) {
		super(parentFrame, fileTreeRoot);
		fileTreeRoot.setExpanded(true);
	}
}