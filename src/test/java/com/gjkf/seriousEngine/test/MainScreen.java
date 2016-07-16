/*
  Created by Davide Cossu (gjkf), 7/10/2016
 */

package com.gjkf.seriousEngine.test;

import com.gjkf.seriousEngine.core.gui.GuiScreenWidget;
import com.gjkf.seriousEngine.core.gui.shapes.Rectangle;
import com.gjkf.seriousEngine.core.render.Colors;
import com.gjkf.seriousEngine.core.render.Renderer;

public class MainScreen extends GuiScreenWidget{

	public MainScreen(int width, int height){
		super(width, height);
//		add(new Triangle(new Point2i[]{
//				new Point2i(200, 100),
//				new Point2i(140, 200),
//				new Point2i(500, 400)
//		}, Colors.WHITE.color, () -> System.out.println("TEST!")));
		add(new Rectangle(100, 20, 50, 50, Colors.BLUE.color, () -> System.err.println("Test1")));
	}

	@Override
	public void drawBackground(){

	}

	@Override
	public void drawForeground(){
		Renderer.renderFont(350, 350, Colors.RED.color, 4f, "This is some test text");
//		try{
//			Renderer.useShader("shaders/vertex.glsl", "shaders/fragment.glsl", () -> {
//				GL11.glBegin(GL11.GL_QUADS);
//				GL11.glVertex3f(10f, 10f, 0.0f);
//				GL11.glVertex3f(100f, 10f, 0.0f);
//				GL11.glVertex3f(100f, 100f, 0.0f);
//				GL11.glVertex3f(10f, 100f, 0.0f);
//				GL11.glEnd();
//			});
//		}catch(Exception e){
//			e.printStackTrace();
//		}
	}
}
