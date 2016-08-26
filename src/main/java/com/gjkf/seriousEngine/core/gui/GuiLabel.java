/*
  Created by Davide Cossu (gjkf), 6/13/2016
 */
package com.gjkf.seriousEngine.core.gui;

import com.gjkf.seriousEngine.core.render.Color3f;
import com.gjkf.seriousEngine.core.render.Renderer;
import org.lwjgl.stb.STBEasyFont;

/**
 * 	Object representing a label. Just text with no interaction.
 */

public class GuiLabel extends GuiWidget{

    /**
     * The text to be displayed
     */
	private String text;
    /**
     * The scale of the text if {@link Renderer#getFont()} is null, else the size of the text
     */
	private float scale;
    /**
     * The text color
     */
	private Color3f color;

    /**
     * The label constructor
     *
     * @param x The x coordinate of the label
     * @param y The y coordinate of the label
     * @param scale The scale
     * @param color The text color
     * @param text The text
     */

	public GuiLabel(float x, float y, float scale, Color3f color, String text){
		// Why isn't there a height in the class but there is in the Javadoc?
		super(x, y, (int)(STBEasyFont.stb_easy_font_width(text)*scale), (int)(12*scale), null);
		this.text = text;
		this.scale = scale;
		this.color = color;
	}

	@Override
	public void draw(){
		super.draw();
        if(Renderer.getFont() != null){
            Renderer.drawText(x, y, this.text, (int)this.scale, this.color);
        }else{
            Renderer.renderFont(x, y, this.text, this.scale, this.color);
        }
	}

}
