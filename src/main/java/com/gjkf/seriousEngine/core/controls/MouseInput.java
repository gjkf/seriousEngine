/*
  Created by Davide Cossu (gjkf), 6/12/2016
 */
package com.gjkf.seriousEngine.core.controls;

import com.gjkf.seriousEngine.SeriousEngine;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Class that gives a general util for the mouse inputs.
 */

public class MouseInput{

	/**
	 *	Returns the mouseX coordinate
	 *
	 * 	@return	The X coordinate
	 */

	public static double getMouseX(){
		DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(SeriousEngine.window.window, xpos, null);
        return xpos.get();
	}

	/**
	 *	Returns the mouseY coordinate
	 *
	 * 	@return	The Y coordinate
	 */

	public static double getMouseY(){
		DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(SeriousEngine.window.window, null, ypos);
        return ypos.get();
	}

	/**
	 * Initialize the button callback.
	 *
	 * @return The initialized callback
	 */

	public static GLFWMouseButtonCallback initMouseButton(){
		return new GLFWMouseButtonCallback(){
			@Override
			public void invoke(long window, int button, int action, int mods){
				SeriousEngine.window.getScreen().mouseClicked();
			}
		};
	}

    /**
     * Checks if the given mouse button is pressed
     *
     * @param btn The button
     *
     * @return TRUE if it is pressed
     */

	public static boolean isPressed(int btn){
		return glfwGetMouseButton(SeriousEngine.window.window, btn) == GLFW_PRESS;
	}

}
