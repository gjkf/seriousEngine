/*
 * Created by Davide Cossu (gjkf), 7/9/2016
 */
package com.gjkf.seriousEngine;

import com.gjkf.seriousEngine.core.controls.Keys;
import com.gjkf.seriousEngine.core.controls.MouseInput;
import com.gjkf.seriousEngine.core.gui.Window;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;

/**
 *	Entry point for the engine.
 */

public class SeriousEngine{

	private GLFWMouseButtonCallback mouseButtonCallback;

	public static ClassLoader runningDirectory;

	/**
	 * 	The current window
	 */

	public static Window window;

	public SeriousEngine(){
		SharedLibraryLoader.load();
        String os = System.getProperty("os.name").toLowerCase();

        /* Mac OS X needs headless mode for AWT */
        if (os.contains("mac")) {
            System.setProperty("java.awt.headless", "true");
        }

        runningDirectory = Thread.currentThread().getContextClassLoader();

    }

	/**
	 * 	Sets the window to the parameter
	 *
	 * 	@param window1 	The window to be set
	 */

	public void setWindow(Window window1){
		window = window1;
		window.init();
	}

	/**
	 * 	Here happens all the action.
	 *
	 * 	@see Window
	 * 	@see com.gjkf.seriousEngine.core.gui.GuiScreenWidget
	 */

	public void run(){
		try{
			init();
			while(glfwWindowShouldClose(window.window) == GL_FALSE){
				window.preLoop();
				window.getScreen().update();
				window.postLoop();
			}
			glfwDestroyWindow(window.window);
		}finally{
			glfwTerminate();
			window.getErrorCallback().release();
			mouseButtonCallback.release();
            Keys.destroyCallback();
        }
	}

	public void init(){
		glfwSetMouseButtonCallback(window.window, mouseButtonCallback  = MouseInput.initMouseButton());
	}

}
