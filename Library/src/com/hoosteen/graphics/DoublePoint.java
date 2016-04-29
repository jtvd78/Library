package com.hoosteen.graphics;

public class DoublePoint {
	
	double x, y;
	
	public DoublePoint(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public String toString(){
		return "(" + x + "," + y + ")";
	}
}