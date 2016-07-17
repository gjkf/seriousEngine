/*
  Created by Davide Cossu (gjkf), 6/11/2016
 */
package com.gjkf.seriousEngine.core.render;

import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        glPushMatrix();

	    ByteBuffer charBuffer = BufferUtils.createByteBuffer(text.length() * 270);
		int quads = stb_easy_font_print(0, 0, text, null, charBuffer);

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
	    glPushMatrix();
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
        glPopMatrix();
	}

    /**
     *  Loads and runs the shader
     *
     *  @param vertPath Path to the vertex shader
     *  @param fragPath Path to the fragment shader
     *  @param runnable The things that should be affected by the shader
     */

	public static void loadShader(String vertPath, String fragPath, Runnable runnable){
		// These lines of code first take in the file path
		// for both our vertex shader and our fragment shader
		// and then create a string containing all
		// of the source code of both shaders and put them
		// into vert and frag. These will later be passed into
		// our created shader objects in our create() function
		String vert = loadAsString(vertPath);
		String frag = loadAsString(fragPath);

        // creates a program object and assigns it to the
        // variable program.
        int program = glCreateProgram();
        // glCreateShader specificies the type of shader
        // that we want created. For the vertex shader
        // we define it as GL_VERTEX_SHADER
        int vertID = glCreateShader(GL_VERTEX_SHADER);
        // Specificies that we want to create a
        // GL_FRAGMENT_SHADER
        int fragID = glCreateShader(GL_FRAGMENT_SHADER);
        // glShaderSource replaces the source code in a shader
        // object.
        // We've defined our vertex shader object and now
        // we want to pass in our vertex shader that we
        // managed to build as a string in our load
        // function.
        //
        glShaderSource(vertID, vert);
        // does the same for our fragment shader
        glShaderSource(fragID, frag);

        // This group of code tries to compile our shader object
        // it then gets the status of that compiled shader and
        // if it proves to be false then it prints an error to
        // the command line.
        glCompileShader(vertID);
        if(glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE){
            System.err.println("Failed to compile vertexd shader!");
            System.err.println(glGetShaderInfoLog(vertID));
        }

        // This group of code tries to compile our shader object
        // it then gets the status of that compiled shader and
        // if it proves to be false then it prints an error to
        // the command line.
        glCompileShader(fragID);
        if(glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE){
            System.err.println("Failed to compile fragment shader!");
            System.err.println(glGetShaderInfoLog(fragID));
        }

        // This attaches our vertex and fragment shaders
        // to the program object
        glAttachShader(program, vertID);
        glAttachShader(program, fragID);
        // this links our program object
        glLinkProgram(program);
        //
        glValidateProgram(program);

		runnable.run();

		glDeleteShader(vertID);
        glDeleteShader(fragID);
        glDeleteProgram(program);
	}

	private static String loadAsString(String location){
		StringBuilder result = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(location));
			String buffer = "";
			while((buffer = reader.readLine())!= null){
				result.append(buffer);
				result.append("\n");
			}
			reader.close();

		} catch(IOException e){
			e.printStackTrace();
		}
		return result.toString();
	}


}
