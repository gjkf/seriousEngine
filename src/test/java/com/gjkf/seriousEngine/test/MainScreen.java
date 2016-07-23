/*
  Created by Davide Cossu (gjkf), 7/10/2016
 */

package com.gjkf.seriousEngine.test;

import com.gjkf.seriousEngine.core.controls.MouseInput;
import com.gjkf.seriousEngine.core.gui.GuiScreenWidget;
import com.gjkf.seriousEngine.core.gui.shapes.Point2i;
import com.gjkf.seriousEngine.core.gui.shapes.Rectangle;
import com.gjkf.seriousEngine.core.gui.shapes.Triangle;
import com.gjkf.seriousEngine.core.render.Colors;
import com.gjkf.seriousEngine.core.render.Renderer;
import com.gjkf.seriousEngine.core.util.FileUtil;

public class MainScreen extends GuiScreenWidget{

	public MainScreen(int width, int height){
		super(width, height);
		add(new Triangle(new Point2i[]{
				new Point2i(500, 500),
				new Point2i(440, 420),
				new Point2i(600, 500)
		}, Colors.WHITE.color, () -> System.out.println("TEST!")));
		add(new Rectangle(100, 100, 100, 100, Colors.BLUE.color, () -> System.err.println("Test1"))); // Yet another rectangle
	}


	@Override
	public void drawBackground(){
        Renderer.drawRect(300, 300, 100, 100, Colors.GREEN.color);
        Renderer.loadShader(// TODO: figure out why has to be like so (the path) and why it works for everything, not just the Runnable
                FileUtil.loadResource("shaders/defaultVertex.glsl"), // Sets the vertex position
				FileUtil.loadResource("shaders/fragment.glsl"), // Sets the red fragment color, the problem is that it sets it for all things
                () -> Renderer.drawRect(0, 0, 100, 100, Colors.WHITE.color) // White default color in case fails
                );
        Renderer.drawRect(200, 200, 100, 100, Colors.WHITE.color); // Draws a white rectangle
        Renderer.setFont(FileUtil.loadResource("fonts/ASO.ttf")); // Sets the font
        Renderer.drawText(500, 500, "This is some sample text with font", 30, Colors.WHITE.color); // Draws text with the font
        Renderer.renderFont(500, 200, "Test", 2f, Colors.BLUE.color); // Draws debug text
	}

	@Override
	public void drawForeground(){

	}

    @Override
    public void mouseClicked(){
        super.mouseClicked();
        System.out.println(MouseInput.getMouseX() + " " + MouseInput.getMouseY());
    }
}
