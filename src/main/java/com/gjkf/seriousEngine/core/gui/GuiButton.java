/*
  Created by Davide Cossu (gjkf), 6/10/2016
 */
package com.gjkf.seriousEngine.core.gui;

import com.gjkf.seriousEngine.core.controls.MouseInput;
import com.gjkf.seriousEngine.core.render.Colors;
import com.gjkf.seriousEngine.core.render.Renderer;
import org.lwjgl.stb.STBEasyFont;

/**
 * 	Class representing a button.
 * 	Pass the callback function as a {@code Lambda} expression.
 *
 * 	@see MouseListener#mouseClicked()
 *	@see MouseInput#initMouseButton()
 */

public class GuiButton extends GuiWidget{

    /**
     * The text to be displayed
     */
	private String text;
    /**
     * The scale of the text if {@link Renderer#getFont()} is null, else the size of the text
     */
	private float scale;

    /**
     * Button constructor
     *
     * @param x The x coordinate of the button
     * @param y The y coordinate of the button
     * @param text The text
     * @param scale The scale
     * @param listener The callback function to be called when pressed
     */

	public GuiButton(float x, float y, String text, float scale, MouseListener listener){
        super(x, y,
                (
                        (Renderer.getFont() == null) ?
                        (STBEasyFont.stb_easy_font_width(text)*scale) :
                        (scale*text.length())
                ),
                (
                        (Renderer.getFont() == null) ?
                        (12*scale) :
                        (scale)
                ),
                listener);
		this.text = text;
		this.scale = scale;
	}

	@Override
	public void draw(){
		super.draw();

        if(Renderer.getFont() != null){
	        if(pointInside(MouseInput.getMouseX(), MouseInput.getMouseY())){
                Renderer.drawText(this.x, this.y+this.scale/2, this.text, (int)this.scale, Colors.GREEN.color);
	        }else{
                Renderer.drawText(this.x, this.y+this.scale/2, this.text, (int)this.scale, Colors.WHITE.color);
            }
        }else{
            Renderer.renderFont(this.x, this.y + this.height / 4, this.text, this.scale, Colors.WHITE.color);
        }

        if(pointInside(MouseInput.getMouseX(), MouseInput.getMouseY())){
            Renderer.drawLine(x - 15, y - 15, x - 15, y + height + 15, Colors.WHITE.color);
            Renderer.drawLine(x - 15, y + height + 15, x + width + 15, y + height + 15, Colors.WHITE.color);
            Renderer.drawLine(x + width + 15, y + height + 15, x + width + 15, y - 15, Colors.WHITE.color);
            Renderer.drawLine(x - 15, y - 15, x + width + 15, y - 15, Colors.WHITE.color);
        }
    }

}
