/*
  Created by Davide Cossu (gjkf), 6/10/2016
 */
package com.gjkf.seriousEngine.core.gui;

import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * 	Abstract class representing a screen.
 * 	<p>
 * 	Add widget using {@link GuiScreenWidget#add(GuiWidget)} and passing a {@link GuiWidget}.
 *
 * 	@see GuiWidget
 */

public abstract class GuiScreenWidget extends GuiWidget{

	private ArrayList<GuiWidget> widgets;

	public GuiScreenWidget(int width, int height){
		super(0, 0, width, height, null);
		widgets = new ArrayList<>();
	}

	/**
	 * 	Adds a widget to the screen
	 *
	 * 	@param widget	The widget to be added
	 */

	public void add(GuiWidget widget){
		widgets.add(widget);
	}

	/**
	 * 	Draws the screen background
	 */

	public abstract void drawBackground();

	/**
	 * 	Draws the screen foreground
	 */

	public abstract void drawForeground();

	@Override
	public void draw(){
		GL11.glPushMatrix();

		drawBackground();
		widgets.forEach(GuiWidget::draw);
		drawForeground();

		GL11.glPopMatrix();
	}

	@Override
	public void update(){
		this.draw();
		widgets.forEach(GuiWidget::update);
	}

	@Override
	public void mouseClicked(){
		widgets.forEach(GuiWidget::mouseClicked);
	}
}
