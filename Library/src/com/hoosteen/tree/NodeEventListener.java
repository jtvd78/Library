package com.hoosteen.tree;

public interface NodeEventListener {
	
	public void nodeRightClicked(String text, NodeEvent event);

	public void nodeLeftClicked(NodeEvent nodeEvent);

	public void nodeDoubleClicked(NodeEvent nodeEvent);
	
}
