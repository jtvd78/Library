package com.hoosteen.tree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.hoosteen.graphics.Circle;
import com.hoosteen.graphics.GraphicsWrapper;
import com.hoosteen.graphics.Rect;

public class TreeComp extends JScrollPane {

	/**
	 *  Root node
	 */
	Node root;

	/**
	 * The parent frame to this Component
	 */
	JFrame parentFrame;

	/**
	 *  The "actual" TreeComp which is within the JScrollPane
	 */
	InnerTreeComp inner;

	/**
	 * Node currently selected. If there is no node which is currently selected,
	 * this should be null
	 */	 
	Node selectedNode = null;

	/**
	 *  Node currently being dragged. If there is no node being dragged
	 *  currently, this should be null
	 */
	Node draggingNode = null;
	
	/**
	 * The remove X's will only appear/work if this is true
	 */
	boolean nodeRemoval = true;

	/**
	 *  The popupMenu being used by any treeComp
	 */
	static JPopupMenu popupMenu = new JPopupMenu();
	
	// These variables are used whenever a node is being dragged.
	int draggingOffsetX = 0;
	int draggingOffsetY = 0;

	/**
	 *  List of Strings used for the right click menu
	 */
	ArrayList<String> rightClickMenuStrings = new ArrayList<String>();

	/**
	 *  List of NodeEventListeners to notify when a Node is clicked on
	 */
	ArrayList<NodeEventListener> nodeListenerList = new ArrayList<NodeEventListener>();

	// SETTINGS//
	/**
	 *  The radius of the close circles on each node
	 */
	public static final int circleRadius = 6;

	/**
	 *  With and height of the expand boxes
	 */
	public static final int boxSize = 9;

	/**
	 *  Height of each node
	 */
	public static final int nodeHeight = 20;

	/**
	 *  X-Spacing between each level of a tree
	 */
	public static final int levelSpacing = 15;

	// Start Color Settings
	private final Color fgColor = Color.BLACK;
	private final Color bgColor = Color.WHITE;
	private final Color textColor = Color.BLACK;
	private final Color nodeBgColor = Color.WHITE;
	private final Color nodeOutlineColor = Color.GRAY;
	private final Color selectedNodeColor = new Color(200, 200, 255, 150);

	/**
	 * Creates a TreeComp, which displays a Node. The root node is not shown
	 * @param parentFrame The owning JFrame
	 * @param root The root node to display
	 */
	public TreeComp(JFrame parentFrame, Node root) {

		this.root = root;
		this.parentFrame = parentFrame;
		this.inner = new InnerTreeComp();
		
		//Set the root's tree comp so it can notify the tree comp when a change is made
		root.setTreeComp(this);

		// Add InnerTreeComp to the ScrollPane
		setViewportView(inner);

		// Set ScrollPane increment to the height of each node
		getVerticalScrollBar().setUnitIncrement(nodeHeight);

		// Disable arrow keys from moving the scroll pane
		disableArrowKeys();
	}

	/**
	 * Disables the arrow keys for MainFrame's treeComp. Allows the scrollPane
	 * not to move up and down when arrow keys are pressed, but the selection
	 * will still go up and down
	 */
	private void disableArrowKeys() {
		String[] keystrokeNames = { "UP", "DOWN", "LEFT", "RIGHT" };
		for (int i = 0; i < keystrokeNames.length; ++i) {
			KeyStroke keystroke = KeyStroke.getKeyStroke(keystrokeNames[i]);
			getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(keystroke, "none");
		}
	}

	/**
	 * Adds a String to be used as a MenuItem for the PopupMenu when a Node is
	 * right clicked
	 * 
	 * @param text
	 */
	public void addRightClickMenuItem(String text) {
		rightClickMenuStrings.add(text);
	}

	/**
	 * Add a NodeEventListener to be notified when a Node is clicked
	 * 
	 * @param listener
	 *            Listener to be notified
	 */
	public void addNodeEventListner(NodeEventListener listener) {
		nodeListenerList.add(listener);
	}

	/**
	 * Draws the actual Tree. This is an inner class since the outer class must be a JScrollPane
	 * @author Justin
	 *
	 */
	public class InnerTreeComp extends JPanel {

		/**
		 * Creates an InnerTreeComp
		 */
		public InnerTreeComp() {
			// Allows keyboard input
			setFocusable(true);

			// Configure input
			Listener l = new Listener();
			addMouseListener(l);
			addMouseMotionListener(l);
			addKeyListener(l);
		}	

		/**
		 * Draws the tree to the Graphics object g
		 */
		public void paintComponent(Graphics gOld) {
			TreeGraphics g = new TreeGraphics(gOld);

			// Draw Background
			g.setColor(bgColor);
			g.fillRect(new Rect(0, 0, getWidth(), getHeight()));

			// Draw Tree, draw children
			g.drawNode(root, true);

			// Draw dragging node on top of tree
			// Do not draw children
			if (draggingNode != null) {
				g.drawNode(draggingNode, false);
			}

			// Update scroll panel so scroll bar is correct
			updateScrollPaneDimensions();
		}
		
		/**
		 * Returns width of component. Helps auto-fit the InnerTreeComp to the
		 * ScrollPane
		 */
		@Override
		public int getWidth() {
			return this.getParent().getWidth();
		}
		
		/**
		 * Updates the parent scrollPanel dimensions by updating this component's
		 * preferred size Should be called whenever the height of the tree changes
		 */
		private void updateScrollPaneDimensions() {
			setPreferredSize(new Dimension(getWidth(), nodeHeight * root.getExpandedNodeCount()));

			//Reset size to preferred size
			revalidate();
		}
		
		/**
		 * Gets a Cirle where the area where a user can press to remove a node
		 * @param n Node to get the remove Circle of
		 * @return The Remove Circle
		 */
		private Circle getRemoveCircle(Node n) {
			int x = getWidth() - 5 - circleRadius;
			int y = (n.getNodeNumber() + 1) * nodeHeight - nodeHeight / 2;
			return new Circle(x, y, circleRadius);
		}

		/**
		 * Gets the expand Rect location for a Node
		 * @param n Node to get Expand Rect of
		 * @return The Expand Rect
		 */
		private Rect getExpandRect(Node n) {
			return new Rect((n.getLevel() - 1) * levelSpacing + (levelSpacing) / 2 - boxSize / 2,
					n.getNodeNumber() * nodeHeight + nodeHeight / 2 - boxSize / 2, boxSize, boxSize);
		}

		/**
		 * Gets a Rect of the space that a Node takes up
		 * @param n Node to get the bounding Rect of
		 * @return The node's bounding Rect
		 */
		private Rect getNodeRect(Node n) {
			int x = n.getLevel() * levelSpacing;
			return new Rect(x, n.getNodeNumber() * nodeHeight, getWidth() - x - 1, nodeHeight);
		}

		/**
		 * Extends GraphicsWrapper and implements functionality to draw nodes
		 * @author Justin
		 *
		 */
		class TreeGraphics extends GraphicsWrapper {

			/**
			 * Creates a TreeGraphics object with a Graphics object
			 * @param g
			 */
			public TreeGraphics(Graphics g) {
				super(g);
			}

			/**
			 * Draws a specific node, and any child nodes to the Graphics object
			 * g
			 * 
			 * @param node
			 *            Node to be drawn
			 * @param g
			 *            Graphics object to be drawn on
			 */
			private void drawNode(Node node, boolean drawChildren) {

				// Original Node Rect without offset
				Rect ogNodeRect = getNodeRect(node);

				// Node Rect which is offset by the dragging offset if
				// The node is being dragged
				Rect nodeRect = ogNodeRect;

				// If the current node being drawn is the node being dragged,
				// adjust the node's rect to the offset position.
				if (node == draggingNode) {
					nodeRect = nodeRect.offset(draggingOffsetX, draggingOffsetY);
				}

				// Sets color to draw node. if node is hidden, draw background color
				Color currentNodeBgColor = node.isHidden() ? nodeBgColor : node.getDisplayColor();
				fillRect(nodeRect, currentNodeBgColor);				

				// Draw node text
				setColor(textColor);
				drawString(node.toString(), nodeRect);

				// X and Y starting positions for the lines being drawn
				int lineX = ogNodeRect.getX() - levelSpacing / 2 - 1;
				int lineY = ogNodeRect.getY() + nodeHeight / 2;

				// Horizontal line to the left of the node
				g.setColor(fgColor);
				drawLine(lineX - levelSpacing, lineY, lineX, lineY);

				//Draw child nodes if the node is expanded, and if children should be drawn
				if (node.isExpanded() && drawChildren) {

					// Vertical line under expand box

					// This code is here to make sure that the vertical line is
					// the correct height
					// If the last child in node exists and is expanded, then
					// the line should not extend
					// past the last open node. This code finds the number of
					// nodes that we should remove
					// from the total count of nodes when determining the line
					// height
					Node lastNode = node.getNode(node.size() - 1);

					// If the last node does not exist, remove 0 nodes
					int lastNodeRemove = ((lastNode == null) ? 0 : lastNode.getExpandedNodeCount());

					// The height of the line in pixels
					int lineHeight = (node.getExpandedNodeCount() - lastNodeRemove) * nodeHeight;

					// Draw the line
					drawLine(lineX, lineY, lineX, lineY + lineHeight);

					// Draw child nodes
					for (Node child : node) {
						drawNode(child, drawChildren);
					}
				}
				
				// Draw Expand box if node has children.
				if (node.size() != 0) {
					drawExpandBox(node);
				}

				// Don't draw X if you're dragging the node
				if (nodeRemoval && node != draggingNode) {
					
					int x = nodeRect.getX() + nodeRect.getWidth();
					int y = nodeRect.getY();
					
					//Weight of the X
					int weight = 3;		
					
					//One fourth and three fourths into a node's rect
					int oneFourth = nodeHeight / 4;
					int threeFourths = 3 * nodeHeight / 4;
					
					//Some reused expressions
					int x1 = x - threeFourths;
					int x2 = x - oneFourth;
					
					int y1 = y + oneFourth;
					int y2 = y + threeFourths;					
					
					
					// Draw box behind x to hide text
					fillRect(x - nodeHeight, y, nodeHeight, nodeHeight, currentNodeBgColor);					
					
					//Set removeX color
					setColor(fgColor);
					
					//Draw removeX with weight
					for (int i = 0; i < weight; i++) {

						// Backslash
						g.drawLine(x1 + i, y1, x2, y2 - i);
						g.drawLine(x1, y1 + i, x2 - i, y2);

						// Forward-Slash
						g.drawLine(x1, y2 - i, x2 - i, y1);
						g.drawLine(x1 + i, y2, x2, y1 + i);
					}
				}
				
				// Highlight selected node
				if (node.equals(selectedNode)) {
					setColor(selectedNodeColor);
					fillRect(nodeRect);

					//Thick border around selected node
					setColor(fgColor);
					drawRect(nodeRect, 3);
				}
				
				// Node border
				drawRect(nodeRect, nodeOutlineColor);
			}

			/**
			 * Draws the expand box to the left of a node, on the graphics
			 * object
			 * 
			 * @param Node
			 *            for which the expand box should be drawn
			 * @param Graphics
			 *            object to draw on.
			 */
			private void drawExpandBox(Node n) {
				Rect r = getExpandRect(n);

				// Expand Box
				fillRect(r, fgColor);

				// minus
				setColor(bgColor);
				drawLine(r.getX() + 1, r.getY() + r.getHeight() / 2, r.getX() + r.getWidth() - 2,
						r.getY() + r.getHeight() / 2);

				// plus
				if (!n.isExpanded()) {
					drawLine(r.getX() + r.getWidth() / 2, r.getY() + 1, r.getX() + r.getWidth() / 2,
							r.getY() + r.getHeight() - 2);
				}
			}
		}

		/**
		 * Handles all user input within this component
		 * 
		 * @author Justin
		 */
		private class Listener implements MouseListener, MouseMotionListener, KeyListener {

			// Previous x, previous y.
			int px = 0;
			int py = 0;

			/**
			 * Called when the mouse is dragged within this component. Used to
			 * drag a node up and down within the tree.
			 */
			public void mouseDragged(MouseEvent e) {
				// Must be left click to drag
				if (!SwingUtilities.isLeftMouseButton(e)) {
					return;
				}

				Node clickedNode;
				if ((clickedNode = root.getVisibleNode(e.getY() / nodeHeight)) == null) {
					selectNode(null);

					// If the clicked node is null, that means that no node was
					// clicked on
					return;
				}

				if (draggingNode != null) {
					// Add change in position to offset
					draggingOffsetX += e.getX() - px;
					draggingOffsetY += e.getY() - py;

					// Update previous X and Y
					px = e.getX();
					py = e.getY();

					// Number of nodes to adjust by.
					int adj = draggingOffsetY / nodeHeight;

					// Moving Down
					if (adj > 0) {

						Node nodeBelow = draggingNode.getNodeBelow();
						int below = (nodeBelow == null) ? 0 : nodeBelow.getExpandedNodeCount();

						int move = adj - below - draggingNode.getExpandedNodeCount();

						if (move > 0) {
							draggingOffsetY -= move * nodeHeight + below * nodeHeight;
							draggingNode.move(move);
							parentFrame.repaint();
						}

						// Moving up
					} else if (adj < 0) {

						Node nodeAbove = draggingNode.getNodeAbove();
						int above = (nodeAbove == null) ? 0 : nodeAbove.getExpandedNodeCount();

						int move = adj + above;

						// If you are above the next node
						if (move < 0) {
							draggingOffsetY -= move * nodeHeight - above * nodeHeight;
							draggingNode.move(move);
							parentFrame.repaint();
						}
					}

					repaint();

					// DraggingNode is null
					// Set DraggingNode to the clicked node so it can be dragged
					// the next time this method is called
				} else {
					draggingNode = clickedNode;
				}
			}

			/**
			 * Handles the event when a mouse is pressed. Uses: Expand / Remove
			 * / Left and Right click node
			 */
			public void mousePressed(MouseEvent e) {

				Node clickedNode;
				if ((clickedNode = root.getVisibleNode(e.getY() / nodeHeight)) == null) {
					selectNode(null);
					
					// If the clicked node is null, that means that no node was
					// clicked on
					return;
				}

				int button = e.getButton();
 
				Rect nodeRect = getNodeRect(clickedNode);
				Rect expandRect = getExpandRect(clickedNode);

				// Clicked on node
				if (nodeRect.contains(e.getX(), e.getY())) {

					// Hit Remove circle:
					if (nodeRemoval && getRemoveCircle(clickedNode).contains(e.getX(), e.getY()) && e.getButton() == 1) {
						clickedNode.remove();
						parentFrame.repaint();
						return;
					}

					// Left, middle, or right cick
					switch (button) {
					case 1:
						nodeLeftClicked(clickedNode);
						parentFrame.repaint();
						break;
					case 2:
						selectNode(clickedNode);
						clickedNode.toggleHidden();
						parentFrame.repaint();
						break;
					case 3:
						nodeRightClicked(clickedNode, e.getX(), e.getY());
						break;
					}
					repaint();

					// Clicked on ExpandRect for Node
				} else if (expandRect.contains(e.getX(), e.getY())) {
					clickedNode.toggleExpanded();
					updateScrollPaneDimensions();
					repaint();
				}
			}

			/**
			 * Keyboard input
			 */
			public void keyPressed(KeyEvent e) {

				if (selectedNode != null) {
					switch (e.getKeyCode()) {

					// Select the previous node
					case KeyEvent.VK_UP:
						selectNode(root.getVisibleNode(selectedNode.getNodeNumber() - 1));

						break;

					// Select the next node
					case KeyEvent.VK_DOWN:
						selectNode(root.getVisibleNode(selectedNode.getNodeNumber() + 1));

						break;

					// Toggle the selectedNode's hidden property
					case KeyEvent.VK_SPACE:
						selectedNode.toggleHidden();

						break;
					}

				} else {

					// If the selected node is null, select either the top or
					// the bottom of the tree
					switch (e.getKeyCode()) {
					case KeyEvent.VK_UP:
						selectNode(root.getVisibleNode(root.getExpandedNodeCount() - 1));

						break;
					case KeyEvent.VK_DOWN:
						selectNode(root.getVisibleNode(0));

						break;
					}
				}

				parentFrame.repaint();

			}

			/**
			 * Update previous X and Y variables to help with mouse input
			 */
			public void mouseMoved(MouseEvent e) {
				px = e.getX();
				py = e.getY();
			}

			/**
			 * Called whenever the mouse is released. Used for purposes of
			 * ending a node being dragged
			 */
			public void mouseReleased(MouseEvent arg0) {
				draggingNode = null;
				draggingOffsetX = 0;
				draggingOffsetY = 0;
				repaint();
			}

			// Unused methods
			public void mouseClicked(MouseEvent e) {
				
				Node clickedNode;
				if ((clickedNode = root.getVisibleNode(e.getY() / nodeHeight)) == null) {
					selectNode(null);
					
					// If the clicked node is null, that means that no node was
					// clicked on
					return;
				}
				
				if(e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)){
					nodeDoubleClicked(clickedNode);
				}
				
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}

		}

	}
	
	//TODO should not be public

	/**
	 * Called when a node is left clicked
	 * 
	 * @param Node
	 *            clicked on
	 */
	private void nodeLeftClicked(Node n) {
		
		if(n == selectedNode){
			selectNode(null);
		}else{
			selectNode(n);
		}		
	}
	
	private void nodeDoubleClicked(Node n){
		for(NodeEventListener listener : nodeListenerList){
			listener.nodeDoubleClicked(new NodeEvent(n));
		}
	}

	/**
	 * Called when a node is right clicked
	 * 
	 * @param Node
	 *            right clicked
	 * @param X
	 *            coordinate of click
	 * @param Y
	 *            coordinate of click
	 */
	private void nodeRightClicked(final Node n, int x, int y) {
		selectNode(n);
		popupMenu.removeAll();	

		for (String text : rightClickMenuStrings) {
			popupMenu.add(new AbstractAction(text) {
				public void actionPerformed(ActionEvent arg0) {
					for (NodeEventListener listener : nodeListenerList) {
						listener.nodeRightClicked(text, new NodeEvent(n));
					}
				}
			});
		}

		n.addPopupMenuOptions(popupMenu, nodeListenerList);
		
		if(nodeRemoval){
			popupMenu.add(new AbstractAction("Remove"){
				public void actionPerformed(ActionEvent e) {
					n.remove();
				}	
			});
		}
		
		popupMenu.show(inner, x, y);
		
		repaint();	
	}

	/**
	 * Selects a given node. If parameter is null, de-selects all nodes.
	 * 
	 * @param n
	 */
	public void selectNode(Node n) {
		selectedNode = n;
		
		if(n == null){
			return;
		}
		
		for (NodeEventListener listener : nodeListenerList) {
			listener.nodeLeftClicked(new NodeEvent(n));
		}
	}

	public Node getSelectedNode() {
		return selectedNode;
	}
	
	public void allowNodeRemoval(boolean value){
		nodeRemoval = value;
	}
}