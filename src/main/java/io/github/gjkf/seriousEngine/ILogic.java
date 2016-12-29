/*
 * Created by Davide Cossu (gjkf), 11/1/2016
 */
package io.github.gjkf.seriousEngine;

/**
 * Interface representing the game logic.
 * <p>Implement this to create your own game.</p>
 */

public interface ILogic{

    /**
     * Initializes the game logic
     *
     * @param window The window
     *
     * @throws Exception If anything went wrong.
     */

    void init(Window window) throws Exception;

    /**
     * Retrieves the inputs from the window.
     *
     * @param window     The current window.
     * @param mouseInput The mouse input.
     */

    void input(Window window, MouseInput mouseInput);

    /**
     * Updates the game logic. Used for calculations.
     *
     * @param interval   The frames passed.
     * @param mouseInput The mouse input.
     */

    void update(float interval, MouseInput mouseInput);

    /**
     * Renders the game logic.
     *
     * @param window The window to update.
     */

    void render(Window window);

    /**
     * Cleans up the resources.
     */

    void cleanup();
}