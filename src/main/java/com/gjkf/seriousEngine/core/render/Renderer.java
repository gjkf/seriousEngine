/*
  Created by Davide Cossu (gjkf), 6/11/2016
 */
package com.gjkf.seriousEngine.core.render;

import com.gjkf.seriousEngine.core.math.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

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
        glPushMatrix();

        ShaderProgram program = new ShaderProgram();
        Shader v = Shader.loadShader(GL_VERTEX_SHADER, vertPath);
        program.attachShader(v);
        Shader f = Shader.loadShader(GL_FRAGMENT_SHADER, fragPath);
        program.attachShader(f);
        program.link();
        program.use();

        /* Get width and height of framebuffer */
        long window = GLFW.glfwGetCurrentContext();
        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetFramebufferSize(window, widthBuffer, heightBuffer);
        int width = widthBuffer.get();
        int height = heightBuffer.get();

        /* Specify Vertex Pointers */
        program = specifyVertexAttributes(program);

        /* Set model matrix to identity matrix */
        Matrix4f model = new Matrix4f();
        int uniModel = program.getUniformLocation("model");
        program.setUniform(uniModel, model);

        /* Set view matrix to identity matrix */
        Matrix4f view = new Matrix4f();
        int uniView = program.getUniformLocation("view");
        program.setUniform(uniView, view);

        /* Set projection matrix to an orthographic projection */
        Matrix4f projection = Matrix4f.orthographic(0f, width, height, 0, -1f, 1f);
        int uniProjection = program.getUniformLocation("projection");
        program.setUniform(uniProjection, projection);

        runnable.run();

        v.delete();
        f.delete();
        program.delete();
        glPopMatrix();
    }

    /**
     * Specifies the vertex pointers.
     */
    private static ShaderProgram specifyVertexAttributes(ShaderProgram program) {
        /* Specify Vertex Pointer */
        int posAttrib = program.getAttributeLocation("position");
        program.enableVertexAttribute(posAttrib);
        program.pointVertexAttribute(posAttrib, 2, 7 * Float.BYTES, 0);

        /* Specify Color3f Pointer */
        int colAttrib = program.getAttributeLocation("color");
        program.enableVertexAttribute(colAttrib);
        program.pointVertexAttribute(colAttrib, 3, 7 * Float.BYTES, 2 * Float.BYTES);

        return program;
    }

}