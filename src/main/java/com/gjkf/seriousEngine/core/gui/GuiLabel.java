/**
 * Created by Davide Cossu (gjkf), 6/13/2016
 */
package com.gjkf.seriousEngine.core.gui;

import com.gjkf.seriousEngine.core.render.Color3f;
import com.gjkf.seriousEngine.core.render.Renderer;
import org.lwjgl.stb.STBEasyFont;

/**
 * 	Object representing a label. Just text with no interaction.
 */

public class GuiLabel extends GuiWidget{

	private String text;
	private float scale;
	private Color3f color;

	public GuiLabel(int x, int y, float scale, Color3f color, String text){
		// Why isn't there a height in the class but there is in the Javadoc?
		super(x, y, (int)(STBEasyFont.stb_easy_font_width(text)*scale), (int)(12*scale), null);
		this.text = text;
		this.scale = scale;
		this.color = color;
	}

	@Override
	public void draw(){
		super.draw();
		Renderer.renderFont(x, y, this.color, this.scale, this.text);
	}

}
