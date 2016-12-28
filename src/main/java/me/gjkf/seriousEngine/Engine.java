/*
 * Created by Davide Cossu (gjkf), 11/1/2016
 */
package me.gjkf.seriousEngine;

/**
 * This is the main class. <p>
 * Here happens the game loop ({@link #gameLoop()}) and all the relevant updates.
 */

public class Engine implements Runnable{

    /**
     * The FPSs we are trying to get
     */
    public static final int TARGET_FPS = 75;
    /**
     * The UPSs we are trying to get.
     */
    public static final int TARGET_UPS = 30;
    /**
     * The {@link Window} object.
     */
    private final Window window;
    /**
     * A new thread where all the game loop happens.
     */
    private final Thread gameLoopThread;
    /**
     * The timer to regulate the thread.
     */
    private final Timer timer;
    /**
     * An interface for the game logic.
     */
    private final ILogic logic;
    /**
     * The mouse input handle.
     */
    private final MouseInput mouseInput;

    /**
     * Constructs the Engine object.
     *
     * @param windowTitle The title of the window
     * @param width       The width of the window
     * @param height      The height of the window
     * @param vSync       Whether or not use vSync
     * @param logic       A class extending {@link ILogic}
     */

    public Engine(String windowTitle, int width, int height, boolean vSync, Window.WindowOptions opts, ILogic logic){
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(windowTitle, width, height, vSync, opts);
        mouseInput = new MouseInput();
        this.logic = logic;
        timer = new Timer();
    }

    /**
     * Starts the game thread.
     */

    public void start(){
        String osName = System.getProperty("os.name");
        if(osName.contains("Mac")){
            gameLoopThread.run();
        }else{
            gameLoopThread.start();
        }
    }

    /**
     * Runs the game loop.
     */

    @Override
    public void run(){
        try{
            init();
            gameLoop();
        }catch(Exception excp){
            excp.printStackTrace();
        }finally{
            logic.cleanup();
        }
    }

    /**
     * Initializes the {@link #window}, the {@link #timer}, the {@link #mouseInput} and the {@link #logic}.
     *
     * @throws Exception If any of the operations fail.
     */

    protected void init() throws Exception{
        window.init();
        timer.init();
        mouseInput.init(window);
        logic.init(window);
    }

    /**
     * The game loop method.
     * <p>Checks for input, renders the screen and updates FPSs</p>
     */

    protected void gameLoop(){
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        boolean running = true;
        while(running && !window.windowShouldClose()){
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while(accumulator >= interval){
                update(interval);
                accumulator -= interval;
            }

            render();

            if(!window.isvSync()){
                sync();
            }
        }
    }

    /**
     * Syncs the timer with the actual time needed to process the operations.
     */

    private void sync(){
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while(timer.getTime() < endTime){
            try{
                Thread.sleep(1);
            }catch(InterruptedException ie){
            }
        }
    }

    /**
     * Retrieves the inputs from {@link #logic}.
     */

    protected void input(){
        mouseInput.input(window);
        logic.input(window, mouseInput);
    }

    /**
     * Updates the {@link #logic}.
     *
     * @param interval The frames passed.
     */

    protected void update(float interval){
        logic.update(interval, mouseInput);
    }

    /**
     * Renders and updates the window.
     */

    protected void render(){
        logic.render(window);
        window.update();
    }

}
