/*
  Created by Davide Cossu (gjkf), 6/10/2016
 */
package com.gjkf.seriousEngine.core.gui;

import com.gjkf.seriousEngine.core.controls.MouseInput;
import com.gjkf.seriousEngine.core.render.Colors;
import com.gjkf.seriousEngine.core.render.Renderer;
import org.lwjgl.glfw.GLFW;

/**
 * 	Abstract class defining a {@code GuiWidget}.
 * 	It provides methods for drawing, updating and managing mouse inputs.
 *
 * 	@see GuiScreenWidget
 */

public abstract class GuiWidget{

	public float x, y, width, height;
	public MouseListener listener;
    public boolean debug = false;

	public GuiWidget(float x, float y, float width, float height, MouseListener mouseListener){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.listener = mouseListener;
	}

	/**
	 * 	Checks if the given coordinates are inside the GuiWidget
	 *
	 * 	@param px The x coordinate of the point to check
	 * 	@param py The y coordinate of the point to check
	 *
	 * 	@return TRUE if the point is inside, FALSE otherwise
	 */

	public boolean pointInside(double px, double py) {
		return px >= x && px < x + width && py >= y && py < y + height;
	}

	/**
	 * 	Draw method for the widget
	 */

	public void draw(){
	    if(debug){
            for(int i = 0; i < width; i += width / 10){
                Renderer.drawLine(i, 0, i, height, Colors.GREEN.color);
            }
            for(int i = 0; i < width; i += height / 10){
                Renderer.drawLine(0, i, width, i, Colors.GREEN.color);
            }
        }
    }

	/**
	 * 	Update method for the widget
	 */

	public void update(){
		this.draw();
	}

	public void mouseClicked(){
		if(MouseInput.isPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT) && pointInside(MouseInput.getMouseX(), MouseInput.getMouseY())){
			if(listener != null){
				listener.mouseClicked();
			}
		}
	}

}
