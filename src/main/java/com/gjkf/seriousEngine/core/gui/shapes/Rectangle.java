/*
  Created by Davide Cossu (gjkf), 7/10/2016
 */
package com.gjkf.seriousEngine.core.gui.shapes;

import com.gjkf.seriousEngine.core.gui.GuiWidget;
import com.gjkf.seriousEngine.core.gui.MouseListener;
import com.gjkf.seriousEngine.core.render.Color3f;
import com.gjkf.seriousEngine.core.render.Renderer;

public class Rectangle extends GuiWidget{

	private int x, y, width, height;
	private Color3f color;

	/**
	 * 	The Rectangle constructor
	 *
	 * 	@param x				The X coordinate
	 * 	@param y				The X coordinate
	 * 	@param width			The width
	 * 	@param height			The height
	 *	@param color			The fill color
	 */

	public Rectangle(int x, int y, int width, int height, Color3f color){
		super(x, y, width, height, null);

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}

	/**
	 * 	The Rectangle constructor
	 *
	 * 	@param x				The X coordinate
	 * 	@param y				The X coordinate
	 * 	@param width			The width
	 * 	@param height			The height
	 *	@param color			The fill color
	 *	@param mouseListener 	The callback function
	 */

	public Rectangle(int x, int y, int width, int height, Color3f color, MouseListener mouseListener){
		super(x, y, width, height, mouseListener);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}

	@Override
	public void draw(){
		super.draw();
		Renderer.drawRect(x, y, width, height, color);
	}

}
