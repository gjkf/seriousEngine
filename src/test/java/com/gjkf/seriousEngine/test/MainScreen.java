package com.gjkf.seriousEngine.test;

/*
  Created by Davide Cossu (gjkf), 7/10/2016
 */

import com.gjkf.seriousEngine.core.gui.Colors;
import com.gjkf.seriousEngine.core.gui.GuiScreenWidget;
import com.gjkf.seriousEngine.core.gui.shapes.Point2i;
import com.gjkf.seriousEngine.core.gui.shapes.Rectangle;
import com.gjkf.seriousEngine.core.gui.shapes.Triangle;
import com.gjkf.seriousEngine.core.render.Renderer;

public class MainScreen extends GuiScreenWidget{

	public MainScreen(int width, int height){
		super(width, height);
		add(new Triangle(new Point2i[]{
				new Point2i(200, 100),
				new Point2i(140, 200),
				new Point2i(500, 400)
		}, Colors.WHITE.color, () -> System.out.println("TEST!")));
		add(new Rectangle(100, 20, 50, 50, Colors.BLUE.color, () -> System.err.println("Test1")));
	}

	@Override
	public void drawBackground(){

	}

	@Override
	public void drawForeground(){
		Renderer.renderFont(350, 350, Colors.RED.color, 4f, "This is some test text");
	}
}
