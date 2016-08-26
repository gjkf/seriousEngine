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

    /**
     * Array containing all the key callbacks that need to be registered
     */
    private static ArrayList<GLFWKeyCallback> array = new ArrayList<>();

    /**
     * Registers a new key callback.
     * <p>Use this with a Lambda expression:<p>
     * (long w,int key, int code, int action, int mods)
     *
     * @param window The window that needs focus
     * @param keyCallback The callback
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

    /**
     * Checks if the given key is pressed
     *
     * @param key The key to check
     *
     * @return TRUE if it is pressed
     */

	public static boolean isPressed(int key){
		return glfwGetKey(SeriousEngine.window.window, key) == GLFW_PRESS;
	}

    /**
     * Destroys the callbacks stored in {@link #array}
     */

	public static void destroyCallback(){
        array.forEach(Retainable.Default::release);
    }

    /**
     * Interface representing the key callback.
     * <p>Has only 1 method that is invoked in {@link #registerKeys(long, KeyCallback)}</p>
     */

	@FunctionalInterface
	public interface KeyCallback{

	    void  invoke(long window, int key, int code, int action, int mods);

    }

}
