/*
  Created by Davide Cossu (gjkf), 7/9/2016
 */
package com.gjkf.seriousEngine.core.gui;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 *	Window object
 */

public class Window{

	/**
	 * 	The error callback
	 */

	private GLFWErrorCallback errorCallback;

	/**
	 * 	The windowID
	 */

	public long window;

	/**
	 * 	The height and the width of the screen
	 */

	public int width, height;

	/**
	 *	The window name
	 */

	public String name;

	/**
	 * 	The loaded screen
	 */

	private GuiScreenWidget screen;

	/**
	 * 	The window constructor, implement this if you want to have the window created
	 *
	 * 	@param width	The window width
	 * 	@param height	The window height
	 * 	@param name		The window name
	 */

	public Window(int width, int height, String name){
		this.width = width;
		this.height = height;
		this.name = name;
	}

	/**
	 *	Initializes the context for the GL
	 */

	public void init(){
		// Setup an error callback.
		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( glfwInit() != GL11.GL_TRUE )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure our window
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(width, height, name, NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		GLContext.createFromCurrent();

		{//For normal use
			glMatrixMode(GL_PROJECTION);
			glOrtho(0, width, height, 0, 1, -1);
			glMatrixMode(GL_MODELVIEW);
		}

		// Get the resolution of the primary monitor
		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window
		glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - width) / 2, (GLFWvidmode.height(vidmode) - height) / 2);

		// Make the window visible
		glfwShowWindow(window);
	}

	/**
	 *	Pre-loop
	 */

	public void preLoop(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	/**
	 *	Post-loop
	 */

	public void postLoop(){
		glfwSwapBuffers(window);
		glfwPollEvents();
	}

	/**
	 * 	Sets the screen of the current window
	 *
	 * 	@param screen	The screen
	 */

	public void setScreen(GuiScreenWidget screen){
		this.screen = screen;
	}

	/**
	 * 	Gets the screen
	 *
	 * 	@return The current screen
	 */

	public GuiScreenWidget getScreen(){
		return screen;
	}

	public GLFWErrorCallback getErrorCallback(){
		return errorCallback;
	}
}
