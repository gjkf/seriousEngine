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

public class MainScreen extends GuiScreenWidget{

	public MainScreen(int width, int height){
		super(width, height);
		add(new Triangle(new Point2i[]{
				new Point2i(500, 500),
				new Point2i(440, 420),
				new Point2i(600, 500)
		}, Colors.WHITE.color, () -> System.out.println("TEST!")));
		add(new Rectangle(100, 100, 100, 100, Colors.BLUE.color, () -> System.err.println("Test1")));
	}


	@Override
	public void drawBackground(){
        Renderer.drawRect(300, 300, 100, 100, Colors.GREEN.color);
		Renderer.loadShader(
		        "C:/Users/cossu/IdeaProjects/SeriousEngine/src/test/resources/shaders/defaultVertex.glsl",
				"C:/Users/cossu/IdeaProjects/SeriousEngine/src/test/resources/shaders/fragment.glsl",
                () -> Renderer.drawRect(0, 0, 100, 100, Colors.WHITE.color)
                );
        Renderer.drawRect(200, 200, 100, 100, Colors.WHITE.color);
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
