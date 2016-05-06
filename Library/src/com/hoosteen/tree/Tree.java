package com.hoosteen.tree;

import java.io.Serializable;

public abstract class Tree implements Serializable{
	
	Node root;
	
	public Tree(Node root){
		this.root = root;
	}

	public Node getRoot() {
		return root;
	}

	public int getExpandedNodeCount() {
		return root.getExpandedNodeCount();
	}

	//Public method for get visible node
	//Automatically calls the recursive getVisibleNode
	//with root as the initial Node
	public Node getVisibleNode(int i) {
		return getVisibleNode(i, root);
	}
	
	/**
	 * Returns the visible node under this node, which corresponds to the argument nodeIndex. 
	 * Starts with 0
	 * @param nodeIndex - Node number to get
	 * @return The desired node
	 */
	private Node getVisibleNode(int nodeIndex, Node startNode){
		for(Node n : startNode){			
			//This is the desired node, return this node.
			if(nodeIndex == 0){
				return n;
			}
			
			//to account for current node
			nodeIndex -= 1; 
			
			//Total number of node visible within this node
			int count = n.getExpandedNodeCount();
			
			//If there are visible nodes within this node
			if(count > 0){
				if(nodeIndex >= count){
					nodeIndex -= count; // Skip to the next n since we need to go farther down
				}else{
					return getVisibleNode(nodeIndex,n); //Desired node is within this node
				}
			}			
		}
		
		//No Visible node found
		return null;
	}	
	
	/**
	 * @return Returns the index of the node, relative to the first node.
	 *  Essentially, it is the number of nodes from the top that this node is
	 */
	public int getNodeNumber(Node node){ 
		
		//This just means that the top node (which should not be visible),
		//has a visible node number of -1
		//so it is not displayed. 
		if(node.getLevel() == 0){
			return -1;
		}
		
		int out = 0;
		
		int index = node.getParent().getIndex(node);
		for(int i = 0; i < index; i++){
			out += node.getParent().getNode(i).getExpandedNodeCount();
		}		
		return node.getParent().getIndex(node)+out+getNodeNumber(node.getParent())+1;
	}
	
	/**
	 * Removes this node from its parent, then removes the parent itself if it is empty. 
	 */	
	public static void remove(Node removeNode) {
		removeNode.getParent().removeNode(removeNode);
		
		if(removeNode.getParent().size() == 0){
			remove(removeNode.getParent());
		}
	}
}
