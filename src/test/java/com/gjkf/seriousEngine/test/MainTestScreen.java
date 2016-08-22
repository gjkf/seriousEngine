/*
  Created by Davide Cossu (gjkf), 7/10/2016
 */

package com.gjkf.seriousEngine.test;

import com.gjkf.seriousEngine.SeriousEngine;
import com.gjkf.seriousEngine.core.controls.Keys;
import com.gjkf.seriousEngine.core.controls.MouseInput;
import com.gjkf.seriousEngine.core.gui.GuiButton;
import com.gjkf.seriousEngine.core.gui.GuiScreenWidget;
import com.gjkf.seriousEngine.core.render.Image;
import com.gjkf.seriousEngine.core.render.Renderer;

public class MainTestScreen extends GuiScreenWidget{

    Image image;

    GuiButton b;

    int a = 0;

	public MainTestScreen(int width, int height){
		super(width, height);
//		add(new Triangle(new Point2i[]{
//				new Point2i(500, 500),
//				new Point2i(440, 420),
//				new Point2i(600, 500)
//		}, Colors.WHITE.color, () -> System.out.println("TEST!")));
//        Renderer.setFont("fonts/ASO.ttf");
//        add(b = new GuiButton(150, 150, "Test", 50f, () -> {
//            System.out.println(b.x + " " + b.y + " " + b.width + " " + b.height);
//        }));
    }


	@Override
	public void drawBackground(){
//        // Map to set the variables for the shader
//		HashMap<String, Ogbject> m = new HashMap<>();
//		m.put("uResolution", new Vector2f(590, 490));
//		m.put("uStartingColor", new Vector4f(1f, .6f, .0f, 1f));
//        m.put("uEndingColor", new Vector4f(0f, .6f, .6f, 1f));
//        m.put("uDirection", 0);
//        Renderer.loadShader(
//                "shaders/defaultVertex.glsl", // Sets the vertex position
//                "shaders/gradient.glsl", // Sets the red fragment color, the problem is that it sets it for all things
//				m,
//                () -> {
////                    Renderer.drawRect(0, 0, 100, 100, Colors.NULL.color);
//                    Renderer.drawArray(new float[]{150,150, 250,420, 600,500}, Colors.GREEN.color, GL11.GL_TRIANGLES);
//                }
//                );
        // Draws the top-left region of the image with an overlay color
        image = Image.loadImage("textures/multiTest.png");
        Renderer.drawImageRegion(image, 100, 500, 0, 0, 32, 32);
        image = Image.loadImage("textures/multiTest.png");
        Renderer.drawImageRegion(image, 200, 500, 32, 0, 32, 32);
        image = Image.loadImage("textures/multiTest.png");
        Renderer.drawImageRegion(image, 300, 500, 64, 0, 32, 32);
        image = Image.loadImage("textures/multiTest.png");
        Renderer.drawImageRegion(image, 400, 500, 98, 0, 32, 32);
        image = Image.loadImage("textures/Globe.png");
        Renderer.drawImageRegion(image, 500, 100, 400, 0, 500, 500);
        // Sets the font
        // Draws text with the font
//        Renderer.drawText(100, 200, "This is some sample text with font", 30, Colors.RED.color);
//        Renderer.renderFont(100, 500, "Test", 5f, Colors.BLUE.color);
	}

	@Override
	public void drawForeground(){
        Keys.registerKeys(SeriousEngine.window.window, (long w,int key, int code, int action, int mods) -> System.out.println(key));
    }

    @Override
    public void mouseClicked(){
        super.mouseClicked();
        System.out.println(MouseInput.getMouseX() + " " + MouseInput.getMouseY());
    }
}
