/*
  Created by Davide Cossu (gjkf), 6/11/2016
 */
package com.gjkf.seriousEngine.core.render;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBEasyFont.stb_easy_font_print;

/**
 *	Misc renderer class.
 *	<p>
 *	Helps to render shapes and text.
 */

public class Renderer{

	/**
	 * 	Renders a {@link org.lwjgl.stb.STBEasyFont} at the given position
	 *
	 * 	@param x 		The x coordinate
	 * 	@param y 		The y coordinate
	 * 	@param color	The color
	 * 	@param scale	The scale.
	 *              	Note that this is used inside a {@link org.lwjgl.opengl.GL11#glScalef(float, float, float)} call.
	 *              	The default size is like Arial 12
	 * 	@param text 	The text to display
	 */

	public static void renderFont(float x, float y, Color3f color, float scale, String text){
		ByteBuffer charBuffer = BufferUtils.createByteBuffer(text.length() * 270);
		int quads = stb_easy_font_print(0, 0, text, null, charBuffer);

		glPushMatrix();

		glEnableClientState(GL_VERTEX_ARRAY);
		glVertexPointer(2, GL_FLOAT, 16, charBuffer);

		glColor3f(color.r, color.g, color.b); // Text color
		glTranslatef(x, y, 0);
		glScalef(scale, scale, 1f);
		glDrawArrays(GL_QUADS, 0, quads * 4);

		glDisableClientState(GL_VERTEX_ARRAY);

		glPopMatrix();
	}

	/**
	 * 	Draws a line between 2 points with the given color.
	 *
	 * 	@param x1 		The first x coordinate
	 * 	@param y1 		The first y coordinate
	 * 	@param x2 		The second x coordinate
	 * 	@param y2 		The second y coordinate
	 * 	@param color 	The color
	 */

	public static void drawLine(float x1, float y1, float x2, float y2, Color3f color){
		float[] verts = new float[]{
			x1,y1, x2,y2
		};
		drawArray(verts, color, GL_LINES);
	}

	/**
	 * 	Draws a filled rectangle at the given coordinates.
	 *
	 * 	@param x 		The x coordinate
	 * 	@param y 		The y coordinate
	 * 	@param width 	The width
	 * 	@param height 	The height
	 * 	@param color 	The fill color
	 */

	public static void drawRect(float x, float y, float width, float height, Color3f color){
		float[] verts = new float[]{
			x,y,  x+width,y,  x+width,y+height,  x,y+height
		};
		drawArray(verts, color, GL_QUADS);
	}

	/**
	 * 	Using VAOs and VBOs it draws the given float array in the given color with the specified mode
	 *
	 * 	@param vertices The array of vertices
	 * 	@param color 	The fill color
	 * 	@param mode 	The GL mode
	 *
	 * 	@see org.lwjgl.opengl.GL11
	 */

	public static void drawArray(float [] vertices, Color3f color, int mode){
		int vaoID= glGenVertexArrays();
		glBindVertexArray(vaoID);

		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
		verticesBuffer.put(vertices).flip();

		int vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

		glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(0);

		glColor3f(color.r, color.g, color.b);
		glDrawArrays(mode, 0, vertices.length);

		glBindVertexArray(0);
		glDeleteVertexArrays(vaoID);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(vboID);
	}

    /**
     *  Loads and runs the shader
     *
     *  @param vertexSource      Path to the vertex shader
     *  @param fragmentSource    Path to the fragment shader
     *
     *  @throws RuntimeException If the shader is not compiled or the program crashes
     */

	public static void useShader(String vertexSource, String fragmentSource, Runnable things){
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexSource);
        glCompileShader(vertexShader);

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentSource);
        glCompileShader(fragmentShader);

        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) != GL_TRUE) {
            throw new RuntimeException(glGetShaderInfoLog(vertexShader));
        }

        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) != GL_TRUE) {
            throw new RuntimeException(glGetShaderInfoLog(fragmentShader));
        }

        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glBindFragDataLocation(shaderProgram, 0, "fragColor");
        glLinkProgram(shaderProgram);

        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) != GL_TRUE) {
            throw new RuntimeException(glGetProgramInfoLog(shaderProgram));
        }

        glUseProgram(shaderProgram);

        things.run();

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        glDeleteProgram(shaderProgram);
    }

}
