package com.hoosteen.tree;

import javax.swing.JComponent;

public abstract class ComponentNode extends Node{
	
	public ComponentNode() {
		super();
	}
	
	public ComponentNode(boolean expanded) {
		super(expanded);
	}

	public abstract JComponent getComponent();

}
