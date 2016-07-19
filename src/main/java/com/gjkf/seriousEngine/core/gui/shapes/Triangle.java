/*
  Created by Davide Cossu (gjkf), 7/10/2016
 */
package com.gjkf.seriousEngine.core.gui.shapes;

import com.gjkf.seriousEngine.core.gui.GuiWidget;
import com.gjkf.seriousEngine.core.gui.MouseListener;
import com.gjkf.seriousEngine.core.render.Color3f;
import com.gjkf.seriousEngine.core.render.Renderer;
import org.lwjgl.opengl.GL11;

public class Triangle extends GuiWidget{

	private int x, y, width, height;
	private Point2i[] vertices;
	private Color3f color;

	/**
	 * 	The Triangle constructor
	 *
	 * 	@param vertices	An array of floats to identify the coordinates of the vertices
	 * 	                <pre>{<br>
	 * 	                	p1,<br>
	 * 	                	p2,<br>
	 * 	                	p3 <br>}</pre>
	 * 	@param color	The fill color
	 */

	public Triangle(Point2i[] vertices, Color3f color){
		super(
				Math.min(vertices[0].x, Math.min(vertices[1].x, vertices[2].x)),
				Math.min(vertices[0].y, Math.min(vertices[1].y, vertices[2].y)),
				Math.max(vertices[0].x, (Math.max(vertices[1].x, vertices[2].x))) - (Math.min(vertices[0].x, Math.min(vertices[1].x, vertices[2].x))),
				Math.max(vertices[0].y, (Math.max(vertices[1].y, vertices[2].y))) - (Math.min(vertices[0].y, Math.min(vertices[1].y, vertices[2].y))),
				null
		);

		this.x = Math.min(vertices[0].x, Math.min(vertices[1].x, vertices[2].x));
		this.y = Math.min(vertices[0].y, Math.min(vertices[1].y, vertices[2].y));
		this.width = Math.max(vertices[0].x, (Math.max(vertices[1].x, vertices[2].x))) - x;
		this.height = Math.max(vertices[0].y, (Math.max(vertices[1].y, vertices[2].y))) - y;
		this.vertices = vertices;
		this.color = color;
	}

	/**
	 * 	The Triangle constructor
	 *
	 * 	@param vertices	An array of floats to identify the coordinates of the vertices
	 * 	                <pre>{<br>
	 * 	                	p1,<br>
	 * 	                	p2,<br>
	 * 	                	p3 <br>}</pre>
	 * 	@param color	The fill color
	 *	@param mouseListener The callback function for the triangle
	 */

	public Triangle(Point2i[] vertices, Color3f color, MouseListener mouseListener){
		super(
				Math.min(vertices[0].x, Math.min(vertices[1].x, vertices[2].x)),
				Math.min(vertices[0].y, Math.min(vertices[1].y, vertices[2].y)),
				Math.max(vertices[0].x, (Math.max(vertices[1].x, vertices[2].x))) - (Math.min(vertices[0].x, Math.min(vertices[1].x, vertices[2].x))),
				Math.max(vertices[0].y, (Math.max(vertices[1].y, vertices[2].y))) - (Math.min(vertices[0].y, Math.min(vertices[1].y, vertices[2].y))),
				mouseListener
		);
		this.x = Math.min(vertices[0].x, Math.min(vertices[1].x, vertices[2].x));
		this.y = Math.min(vertices[0].y, Math.min(vertices[1].y, vertices[2].y));
		this.width = Math.max(vertices[0].x, (Math.max(vertices[1].x, vertices[2].x))) - x;
		this.height = Math.max(vertices[0].y, (Math.max(vertices[1].y, vertices[2].y))) - y;
		this.vertices = vertices;
		this.color = color;
	}

	@Override
	public void draw(){
		super.draw();
		Renderer.drawArray(new float[]{vertices[0].x, vertices[0].y, vertices[1].x, vertices[1].y, vertices[2].x, vertices[2].y}, color, GL11.GL_TRIANGLES);
	}

	@Override
	public boolean pointInside(double px, double py){
		boolean b1, b2, b3;

		b1 = sign(new Point2i((int)px, (int)py), vertices[0], vertices[1]) < 0.0f;
		b2 = sign(new Point2i((int)px, (int)py), vertices[1], vertices[2]) < 0.0f;
		b3 = sign(new Point2i((int)px, (int)py), vertices[2], vertices[0] )< 0.0f;

		return ((b1 == b2) && (b2 == b3));
	}

	private double sign(Point2i p1, Point2i p2, Point2i p3){
		return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
	}

}
