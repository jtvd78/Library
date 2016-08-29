package com.hoosteen.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import com.hoosteen.graphics.GraphicsWrapper.Position;


/**
 * Makes it easy to perform special operations with a Graphics object. Including drawing rectangles, cirles, and strings
 * @author Justin
 *
 */
public class GraphicsWrapper {
	
	/**
	 * Graphics object on which to draw on
	 */
	protected Graphics g;
	private FontMetrics fm;	
	
	/**
	 * Creates a new Graphics Wrapper, with a graphics object g
	 * @param g - Graphics object to wrap around
	 */
	public GraphicsWrapper(Graphics g){
		this.g = g;
		fm = g.getFontMetrics();
	}
	
	/**
	 * Draws a string on the graphics object, centered vertically within the rectangle. 
	 * @param s - String to draw
	 * @param r - Rectangle to draw within
	 */
	public void drawString(String s, Rect r){
		
		int y = r.getHeight()/2 + r.getY() - (fm.getAscent() + fm.getDescent())/2 + fm.getAscent();
		g.drawString(s, r.getX() + 5, y);
	}
	
	public enum HorizAlign{
		LEFT, CENTER, RIGHT
	}
	
	public enum VertAlign{
		TOP, MIDDLE, BOTTOM
	}
	
	public enum Position{
		TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, TOP_LEFT, CENTER
	}
	
	public void drawCenteredString(String s, Rect r, HorizAlign hori, VertAlign vert ){
		
		Rectangle2D bounds = fm.getStringBounds(s, g);
		
		
		int centerX = r.getX() + r.getWidth() / 2;
		int centerY = r.getY() + r.getHeight() / 2;
		
		int x = 0;
		int y = 0;
		
		switch(hori){
		case CENTER:	x = (int)(centerX - bounds.getWidth()/2); 					break;
		case LEFT:		x = r.getX();												break;
		case RIGHT:		x = (int) (r.getX() + r.getWidth() - bounds.getWidth()); 	break;
		}
		
		int amt = -(fm.getAscent() + fm.getDescent())/2 + fm.getAscent();
		
		switch(vert){
		case BOTTOM:	y = r.getY() + r.getHeight() - fm.getDescent();				break;
		case MIDDLE:	y = centerY + amt;											break;
		case TOP:		y = r.getY() + fm.getAscent();								break;
		
		}
		
		g.drawString(s, x, y);
		
		
	}
	
	/**
	 * Draws a string, centered within:
	 * @param s - string to draw
	 * @param r - rect to center within
	 */
	public void drawCenteredString(String s, Rect r){
		drawCenteredString(s, r.getX() + r.getWidth() / 2, r.getY() + r.getHeight()/2);
	}
	
	/**
	 * Draws a string centered on the point (centerX, centerY)
	 * @param s	String to draw
	 * @param centerX - X Coordinate to center string on 
	 * @param centerY - Y Coordinate to center string on 
	 */
	public void drawCenteredString(String s, int centerX, int centerY){		
		Rectangle2D bounds = fm.getStringBounds(s, g);
		
		int x = (int)(centerX - bounds.getWidth()/2);
		int y = (int)(centerY - (fm.getAscent() + fm.getDescent())/2 + fm.getAscent());
		
		g.drawString(s, x, y);
	}
	
	/**
	 * Draws several lines of text within a rect
	 * @param s String to draw
	 * @param r Rectangle to draw within
	 */
	public void drawMultiLineCenteredString(String s, Rect r){		
		int centerX = r.getX() + r.getWidth() /2;
		int centerY = r.getY() + r.getHeight() /2;

		int lineHeight = fm.getAscent();
		
		String[] lines = s.split("\n");
		double ctr = 0;
		for(String line : lines){
			
			double adj = ctr - (((double)lines.length)-1)/2.0;
			int lineOffset = (int)(adj*lineHeight);
			
			drawCenteredString(line, centerX, centerY +  lineOffset);
			ctr++;
		}
	}
	
	public void fillRect(Rect r, Color c){
		g.setColor(c);
		r.fill(g);
	}
	
	/**
	 * Fills a rectangle
	 * @param r - Rect to fill
	 */
	public void fillRect(Rect r){
		r.fill(g);
	}
	
	public void drawRect(Rect r, Color c) {
		g.setColor(c);
		r.draw(g);
	}
	
	/**
	 * Draws a rectangle
	 * @param r - Rect to draw
	 */
	public void drawRect(Rect r){
		r.draw(g);
	}
	
	/**
	 * Fill a circle
	 * @param c - Circle to draw
	 */
	public void fillCircle(Circle c){
		c.fill(g);		
	}
	
	/**
	 * Draw a circle
	 * @param c - Circle to draw
	 */
	public void drawCircle(Circle c){
		c.draw(g);		
	}	

	
	public void fillRect(int x, int y, int width, int height, Color c){
		g.setColor(c);
		g.fillRect(x, y, width, height);
	}
	
	/**
	 * Draws a rectangle with a given outline width
	 * @param r - Rectangle to draw
	 * @param weight - Width of outline
	 */
	public void drawRect(Rect r, int weight){
		r.draw(g, weight);
	}
	
	/**
	 * Gets the Rect for a given string with the (x, y) values set to (0,0)
	 * 
	 * @param s The string to get the rect of
	 * @return The String's rect
	 */
	public Rect getStringRect(String s){
		Rectangle2D bounds = fm.getStringBounds(s, g);
		
		return new Rect(0,0,(int)bounds.getWidth(), fm.getAscent() + fm.getDescent());
	}
	
	/**
	 * Gets the Rect for a given string with the Rect positioned to the
	 * right of the x Value and centered around the Y value
	 * 
	 * @param s The string to get the rect of
	 * @return The String's rect
	 */	
	public Rect getStringRect(String s, int x, int y, Position pos){
		Rectangle2D bounds = fm.getStringBounds(s, g);
		
		int width = (int) bounds.getWidth();
		int height = (int) bounds.getHeight();		
		
		switch(pos){
		case BOTTOM: 		return new Rect(x-width/2,y,width,height);
		case BOTTOM_LEFT: 	return new Rect(x-width,y,width,height);
		case BOTTOM_RIGHT:	return new Rect(x,y,width,height);
		case CENTER: 		return new Rect(x-width/2, y-height/2,width,height);
		case LEFT: 			return new Rect(x-width,y-height/2,width,height);
		case RIGHT: 		return new Rect(x, y-height/2,width,height);
		case TOP: 			return new Rect(x-width/2,y-height, width,height);
		case TOP_LEFT: 		return new Rect(x-width, y-height,width,height);
		case TOP_RIGHT: 	return new Rect(x,y-height,width,height);		
		default: 			return new Rect(x,y,width,height);
		}	
		
//		return new Rect(x,y-(int)bounds.getHeight()/2,(int)bounds.getWidth(), (int)bounds.getHeight());
	}
	
	public void drawCenteredString(String s, double x, double y) {
		drawCenteredString(s, (int)x, (int)y);
	}

	public void drawString(String string, int x, int y, Position position) {
		Rect r = getStringRect(string, x, y, position);
		drawCenteredString(string, r, HorizAlign.CENTER, VertAlign.MIDDLE);
	}

	/**
	 * Sets the color to draw
	 * @param c - Color to draw
	 */
	public void setColor(Color c){
		g.setColor(c);
	}
	
	
	/**
	 * Draws a line between the input points
	 * Wrapper for drawLine method in graphics
	 * @param x1 - X Coordinate of first point
	 * @param y1 - Y Coordinate of first point
	 * @param x2 - X Coordinate of second point
	 * @param y2 - Y Coordinate of second point
	 */
	public void drawLine(int x1, int y1, int x2, int y2){
		g.drawLine(x1, y1, x2, y2);
	}
	
	public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		g.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
	}
	
	public void fillOval(int x, int y, int width, int height) {
		g.fillOval(x, y, width, height);		
	}

	public void drawOval(int x, int y, int width, int height) {
		g.drawOval(x, y, width, height);		
	}

	public void drawPolyline(int[] xPoints, int[] yPoints, int length) {
		g.drawPolyline(xPoints, yPoints, length);
	}

	public void fillPolygon(int[] xPointsArr, int[] yPointsArr, int size) {
		g.fillPolygon(xPointsArr, yPointsArr, size);
	}

	public Graphics getGraphics() {
		return g;
	}

	public void fillRect(int x, int y, int width, int height) {
		g.fillRect(x, y, width, height);
	}

	public void fillRoundRect(Rect r, int radius) {
		g.fillRoundRect(r.getX(), r.getY(), r.getWidth(), r.getHeight(), radius, radius);
	}

	public void setLineWeight(int i) {
		Graphics2D g2 = (Graphics2D)g;
		
		//3 Point (thickness) line
		g2.setStroke(new BasicStroke(i));
	}
}