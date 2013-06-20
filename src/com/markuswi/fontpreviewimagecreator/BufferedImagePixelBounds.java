package com.markuswi.fontpreviewimagecreator;

public class BufferedImagePixelBounds {

	private int minY = -1;
	private int maxY = -1;
	private int maxX = -1;

	public int getMinY() {
		return minY;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	public int getMaxX() {
		return maxX;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}
	
	public int getWidth() {
		return this.maxX - this.getX()+1;
	}
	
	public int getHeight() {
		return this.maxY - this.minY;
	}
	
	public int getX() {
		return 0;
	}
	
	public int getY() {
		return this.minY;
	}
	
	

}
