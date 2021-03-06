package com.hoosteen.graphics;

import java.awt.Graphics;

/**
 * Rectangle. Has an (x, y) coordinate in the top left, with a width and and height. 
 * @author justin
 *
 */
public class Rect{
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	/**
	 * Constructor. Takes (x, y) coordinate of top left point,
	 * and a width and height 
	 * @param x - X coordinate of top left point
	 * @param y - Y coordinate of top left point
	 * @param width - Width of Rect
	 * @param height - Height of Rect
	 */
	public Rect(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Checks if this Rect contains a point (X, Y)
	 * @param xx - x coordinate
	 * @param yy - y coordinate
	 * @return result
	 */
	public boolean contains(int xx, int yy){
		if(xx >= x && xx <= (x+width)){
			if(yy >= y && yy <=(y+height)){
				return true;
			}
		}
		return false;
	}

	/**
	 * @return X coordinate of top-left point
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * @return Y coordinate of top-left point
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return With of Rect
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * @return Height of Rect
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Returns a new, offset rectangle
	 * @param offsetX - Integer X to offset Rect
	 * @param offsetY - Integer Y to offset Rect
	 * @return new Rect, offset by the input parameters
	 */
	public Rect offset(int offsetX, int offsetY){
		return new Rect(x + offsetX, y + offsetY, width, height);
	}

	/**
	 * Fills the rectangle onto the graphics object
	 * @param g - Graphics object to draw onto
	 */
	public void fill(Graphics g) {
		g.fillRect(x, y, width, height);
	}
	
	/**
	 * Draws the rectangle onto the graphics object
	 * @param g - Graphics object to draw onto
	 */
	public void draw(Graphics g) {
		g.drawRect(x, y, width, height);
	}
	
	/**
	 * Draws a weighted rectangle
	 * @param g - Graphics object to draw on
	 * @param weight - Pixel thickness of outline
	 */
	public void draw(Graphics g, int weight){
		for(int i = 0; i < weight; i++){
			g.drawRect(x + i, y + i, width - i*2, height -  i*2);			
		}
	}
	
	@Override
	public String toString(){
		return "(" + x + ", " + y + ") Width: " + width + " - Height: " + height;
	}
}