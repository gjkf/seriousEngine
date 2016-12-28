/*
 * Created by Davide Cossu (gjkf), 11/1/2016
 */
package me.gjkf.seriousEngine.test;

import me.gjkf.seriousEngine.Engine;
import me.gjkf.seriousEngine.ILogic;
import me.gjkf.seriousEngine.SharedLibraryLoader;
import me.gjkf.seriousEngine.Window;

public class Main{

    public static void main(String[] args){
        SharedLibraryLoader.load();
        String os = System.getProperty("os.name").toLowerCase();
        /* Mac OS X needs headless mode for AWT */
        if(os.contains("mac")){
            System.setProperty("java.awt.headless", "true");
        }

        try{
            boolean vSync = true;
            ILogic gameLogic = new DummyGame();
            Window.WindowOptions opts = new Window.WindowOptions();
            opts.cullFace = true;
            opts.showFps = true;
            Engine gameEng = new Engine("Game", 800, 800, vSync, opts, gameLogic);
            gameEng.start();
        }catch(Exception excp){
            excp.printStackTrace();
            System.exit(-1);
        }
    }

}
