/*
  Created by Davide Cossu (gjkf), 7/10/2016
 */

package com.gjkf.seriousEngine.test;

import com.gjkf.seriousEngine.core.controls.MouseInput;
import com.gjkf.seriousEngine.core.gui.GuiScreenWidget;
import com.gjkf.seriousEngine.core.math.Vector2f;
import com.gjkf.seriousEngine.core.math.Vector4f;
import com.gjkf.seriousEngine.core.render.Colors;
import com.gjkf.seriousEngine.core.render.Renderer;
import com.gjkf.seriousEngine.core.util.FileUtil;

import java.util.HashMap;

public class MainScreen extends GuiScreenWidget{

    HashMap<String, Object> image;

	public MainScreen(int width, int height){
		super(width, height);
//		add(new Triangle(new Point2i[]{
//				new Point2i(500, 500),
//				new Point2i(440, 420),
//				new Point2i(600, 500)
//		}, Colors.WHITE.color, () -> System.out.println("TEST!")));
        image = Renderer.loadImage("textures/Globe.png");
    }


	@Override
	public void drawBackground(){
		HashMap<String, Object> m = new HashMap<>();
		m.put("uResolution", new Vector2f(590, 490));
		m.put("uStartingColor", new Vector4f(1f, .6f, .0f, 1f));
        m.put("uEndingColor", new Vector4f(0f, .6f, .6f, 1f));
        m.put("uDirection", 0);
        Renderer.loadShader(// TODO: figure out why has to be like so (the path) and why it works for everything, not just the Runnable
                FileUtil.loadResource("shaders/defaultVertex.glsl"), // Sets the vertex position
                FileUtil.loadResource("shaders/gradient.glsl"), // Sets the red fragment color, the problem is that it sets it for all things
				m,
                () -> {
                    Renderer.drawRect(0, 0, 100, 100, Colors.NULL.color);
//                    Renderer.drawArray(new float[]{10,10, 150,420, 600,500}, Colors.GREEN.color, GL11.GL_TRIANGLES);
                }
                );
		Renderer.renderImage(200, 300, 150, 150, image);
        Renderer.setFont("fonts/ASO.ttf"); // Sets the font
        Renderer.drawText(100, 200, "This is some sample text with font", 30, Colors.RED.color); // Draws text with the font
//        Renderer.renderFont(100, 500, "Test", 5f, Colors.BLUE.color); // Draws debug text

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
