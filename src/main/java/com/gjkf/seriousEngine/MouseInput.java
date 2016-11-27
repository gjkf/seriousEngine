/*
 * Created by Davide Cossu (gjkf), 11/3/2016
 */
package com.gjkf.seriousEngine;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Utility class to handle the mouse inputs.
 */

public class MouseInput{

    /**
     * The last position of the mouse.
     */
    private final Vector2d previousPos;
    /**
     * The current position of the mouse.
     */
    private final Vector2d currentPos;
    /**
     * The difference between {@link #previousPos} and {@link #currentPos}.
     */
    private final Vector2f displVec;
    /**
     * Whether or not the mouse is in the window.
     */
    private boolean inWindow = false;
    /**
     * Whether or not the left mouse button is pressed.
     */
    private boolean leftButtonPressed = false;
    /**
     * Whether or not the right mouse button is pressed.
     */
    private boolean rightButtonPressed = false;
    /**
     * The position callback. Even if never used, we need to keep it.
     */
    private GLFWCursorPosCallback cursorPosCallback;
    /**
     * The cursor enter callback. Even if never used, we need to keep it.
     */
    private GLFWCursorEnterCallback cursorEnterCallback;
    /**
     * The mouse button callback. Even if never used, we need to keep it.
     */
    private GLFWMouseButtonCallback mouseButtonCallback;

    /**
     * Sets the vectors to the default values.
     */

    public MouseInput(){
        previousPos = new Vector2d(-1, -1);
        currentPos = new Vector2d(0, 0);
        displVec = new Vector2f();
    }

    /**
     * Initializes the calbacks.
     *
     * @param window The window to refer to.
     */

    public void init(Window window){
        glfwSetCursorPosCallback(window.getWindowHandle(), cursorPosCallback = new GLFWCursorPosCallback(){
            @Override
            public void invoke(long window, double xpos, double ypos){
                currentPos.x = xpos;
                currentPos.y = ypos;
            }
        });
        glfwSetCursorEnterCallback(window.getWindowHandle(), cursorEnterCallback = new GLFWCursorEnterCallback(){
            @Override
            public void invoke(long window, boolean entered){
                inWindow = entered;
            }
        });
        glfwSetMouseButtonCallback(window.getWindowHandle(), mouseButtonCallback = new GLFWMouseButtonCallback(){
            @Override
            public void invoke(long window, int button, int action, int mods){
                leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
                rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
            }
        });
    }

    /**
     * Returns the display vector.
     *
     * @return The vector.
     */

    public Vector2f getDisplVec(){
        return displVec;
    }

    /**
     * Updates all the mouse vectors.
     *
     * @param window The window.
     */

    public void input(Window window){
        displVec.x = 0;
        displVec.y = 0;
        if(previousPos.x > 0 && previousPos.y > 0 && inWindow){
            double deltax = currentPos.x - previousPos.x;
            double deltay = currentPos.y - previousPos.y;
            boolean rotateX = deltax != 0;
            boolean rotateY = deltay != 0;
            if(rotateX){
                displVec.y = (float) deltax;
            }
            if(rotateY){
                displVec.x = (float) deltay;
            }
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }

    /**
     * Whether or not the left mouse button is pressed.
     *
     * @return The left mouse press.
     */

    public boolean isLeftButtonPressed(){
        return leftButtonPressed;
    }

    /**
     * Whether or not the right mouse button is pressed.
     *
     * @return The right mouse press.
     */

    public boolean isRightButtonPressed(){
        return rightButtonPressed;
    }
}