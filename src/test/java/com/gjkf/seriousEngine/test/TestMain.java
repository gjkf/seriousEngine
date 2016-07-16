/*
  Created by Davide Cossu (gjkf), 7/10/2016
 */

package com.gjkf.seriousEngine.test;

import com.gjkf.seriousEngine.SeriousEngine;
import com.gjkf.seriousEngine.core.gui.Window;

public class TestMain{

	public static void main(String... args){
		SeriousEngine engine = new SeriousEngine();
		Window w = new Window(1000, 1000, "Test window");
		w.setScreen(new MainScreen(1000, 1000));
		engine.setWindow(w);
		engine.run();
	}

}
