/**
 * Created by Davide Cossu (gjkf), 6/10/2016
 */
package com.gjkf.seriousEngine.core.controls;

import com.gjkf.seriousEngine.SeriousEngine;
import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class Keys{

	public static GLFWKeyCallback registerKeys(){
		return new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
			}
		};
	}

	public static boolean isPressed(int key){
		return glfwGetKey(SeriousEngine.window.window, key) == GLFW_PRESS;
	}

}
