/*
  Created by Davide Cossu (gjkf), 6/10/2016
 */
package com.gjkf.seriousEngine.core.controls;

import com.gjkf.seriousEngine.SeriousEngine;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.system.Retainable;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class Keys{

    private static ArrayList<GLFWKeyCallback> array = new ArrayList<>();

    /**
     * Use this with a Lambda expression:
     * <p>
     * (long w,int key, int code, int action, int mods) -> {//CODE HERE} }
     *
     * @param window
     * @param keyCallback
     */

	public static void registerKeys(long window, KeyCallback keyCallback){
	    array.add(new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                keyCallback.invoke(window, key, action, scancode, mods);
            }
        });
	    GLFW.glfwSetKeyCallback(window, array.get(array.size()-1));
	}

	public static boolean isPressed(int key){
		return glfwGetKey(SeriousEngine.window.window, key) == GLFW_PRESS;
	}

	public static void destroyCallback(){
        array.forEach(Retainable.Default::release);
    }

	@FunctionalInterface
	public interface KeyCallback{

	    void  invoke(long window, int key, int code, int action, int mods);

    }

}
