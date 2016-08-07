/*
  Created by Davide Cossu (gjkf), 6/11/2016
 */
package com.gjkf.seriousEngine.core.render;

import com.gjkf.seriousEngine.core.math.*;
import com.gjkf.seriousEngine.core.util.FileUtil;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBEasyFont.stb_easy_font_print;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.stb.STBTruetype.stbtt_BakeFontBitmap;
import static org.lwjgl.stb.STBTruetype.stbtt_GetBakedQuad;

/**
 *	Misc renderer class.
 *	<p>
 *	Helps to render shapes and text.
 */

public class Renderer{

    private static String font;

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

    public static void renderFont(float x, float y, String text, float scale, Color3f color){
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
     *  Draws a text with the font specified at {@link #font}
     *
     * 	@param x 		The x coordinate
     * 	@param y 		The y coordinate
     * 	@param color	The color
     * 	@param size	    The size
     * 	@param text 	The text to display
     */

    public static void drawText(float x, float y, String text, int size, Color3f color){
        glPushMatrix();

        int BITMAP_W = 512;
        int BITMAP_H = 512;

        int texID = glGenTextures();
        ByteBuffer cdata = BufferUtils.createByteBuffer(96 * STBTTBakedChar.SIZEOF);

        try {
            ByteBuffer ttf = FileUtil.ioResourceToByteBuffer(font, 160 * 1024);

            ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H);
            stbtt_BakeFontBitmap(ttf, size, bitmap, BITMAP_W, BITMAP_H, 32, cdata);

            glBindTexture(GL_TEXTURE_2D, texID);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, BITMAP_W, BITMAP_H, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        glColor3f(color.r, color.g, color.b); // Text color

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        FloatBuffer xF = BufferUtils.createFloatBuffer(1);
        FloatBuffer yF = BufferUtils.createFloatBuffer(1);
        STBTTAlignedQuad q = new STBTTAlignedQuad();

        glPushMatrix();
        xF.put(0, x);
        yF.put(0, y);
        glBegin(GL_QUADS);
        for ( int i = 0; i < text.length(); i++ ) {
            char c = text.charAt(i);
            if ( c == '\n' ) {
                yF.put(0, yF.get(0) + size);
                xF.put(0, 0.0f);
                continue;
            } else if ( c < 32 || 128 <= c )
                continue;

            stbtt_GetBakedQuad(cdata, BITMAP_W, BITMAP_H, c - 32, xF, yF, q.buffer(), 1);

            glTexCoord2f(q.getS0(), q.getT0());
            glVertex2f(q.getX0(), q.getY0());

            glTexCoord2f(q.getS1(), q.getT0());
            glVertex2f(q.getX1(), q.getY0());

            glTexCoord2f(q.getS1(), q.getT1());
            glVertex2f(q.getX1(), q.getY1());

            glTexCoord2f(q.getS0(), q.getT1());
            glVertex2f(q.getX0(), q.getY1());
        }
        glEnd();

        glPopMatrix();

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

        if(color != null) // In case the color is equals to Colors.NULL (used for shaders and other stuff
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
     *  <p>
     *  YOU DO NEED TO USE THIS WITH {@link FileUtil#loadResource(String)}
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
        glUseProgram(0);
        glPopMatrix();
    }

    public static void loadShader(String vertPath, String fragPath, HashMap<String, Object> map, Runnable runnable){
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

        ShaderProgram finalProgram = program;

        map.forEach((key, value) -> {
            int target = finalProgram.getUniformLocation(key);
            if(value instanceof Matrix2f)
                finalProgram.setUniform(target, (Matrix2f) value);
            else if(value instanceof Matrix3f)
                finalProgram.setUniform(target, (Matrix3f) value);
            else if(value instanceof Matrix4f)
                finalProgram.setUniform(target, (Matrix4f) value);
            else if(value instanceof Vector2f)
                finalProgram.setUniform(target, (Vector2f) value);
            else if(value instanceof Vector3f)
                finalProgram.setUniform(target, (Vector3f) value);
            else if(value instanceof Vector4f)
                finalProgram.setUniform(target, (Vector4f) value);
            else
                finalProgram.setUniform(target, (int) value);
        });

        program = finalProgram;

        runnable.run();

        v.delete();
        f.delete();
        program.delete();
        glUseProgram(0);
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

    /**
     *  Renders an image from the given path with default width and height
     *  <p>
     *  YOU DO NOT NEED TO USE THIS WITH {@link FileUtil#loadResource(String)}
     *  @param x The x coordinate of where to render the image
     *  @param y The y coordinate of where to render the image
     *  @param path The path of the file
     */

    public static void renderImage(float x, float y, String path){
        ByteBuffer imageBuffer = null;
        try{
            imageBuffer = FileUtil.ioResourceToByteBuffer(path, 8 * 1024);
        }catch(IOException e){
            e.printStackTrace();
        }

        IntBuffer ww = BufferUtils.createIntBuffer(1);
        IntBuffer hh = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);

        stbi_info_from_memory(imageBuffer, ww, hh, comp);

        renderImage(x, y, ww.get(0), hh.get(0), path);
    }

    /**
     *  Renders an image from the given path
     *  <p>
     *  YOU DO NOT NEED TO USE THIS WITH {@link FileUtil#loadResource(String)}
     *
     *  @param x The x coordinate of where to render the image
     *  @param y The y coordinate of where to render the image
     *  @param width The width of the rendered image
     *  @param height The height of the rendered image
     *  @param path The path of the file
     */

    public static void renderImage(float x, float y, float width, float height, String path){
        glPushMatrix();

        ByteBuffer image;
        ByteBuffer imageBuffer = null;
        try{
            imageBuffer = FileUtil.ioResourceToByteBuffer(path, 8 * 1024);
        }catch(IOException e){
            e.printStackTrace();
        }

        IntBuffer ww = BufferUtils.createIntBuffer(1);
        IntBuffer hh = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);

        stbi_info_from_memory(imageBuffer, ww, hh, comp);

        image = stbi_load_from_memory(imageBuffer, ww, hh, comp, 0);

        if(image == null)
            throw new RuntimeException("Failed to load image: " + stbi_failure_reason());

        int texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        int w = ww.get(0);
        int h = hh.get(0);

        if(comp.get(0) == 3){
            if ( (w & 3) != 0 )
                glPixelStorei(GL_UNPACK_ALIGNMENT, 2 - (w & 1));
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w, h, 0, GL_RGB, GL_UNSIGNED_BYTE, image);
        }else{
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        }

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glEnable(GL_TEXTURE_2D);

        glTranslatef(x, y, 0);

        glBegin(GL_QUADS);

        glTexCoord2f(0.0f, 0.0f);
        glVertex2f(0.0f, 0.0f);

        glTexCoord2f(1.0f, 0.0f);
        glVertex2f(width, 0.0f);

        glTexCoord2f(1.0f, 1.0f);
        glVertex2f(width, height);

        glTexCoord2f(0.0f, 1.0f);
        glVertex2f(0.0f, height);

        glEnd();

        glDisable(GL_TEXTURE_2D);

        stbi_image_free(image);

        glPopMatrix();
    }

    public static void renderImage(float x, float y, float width, float height, Image image){
        glPushMatrix();
        int texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        if(image.comp == 3){
            if ( (image.width & 3) != 0 )
                glPixelStorei(GL_UNPACK_ALIGNMENT, 2 - (image.width & 1));
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, image.width, image.height, 0, GL_RGB, GL_UNSIGNED_BYTE, image.image);
        }else{
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.width, image.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image.image);

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        }

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glEnable(GL_TEXTURE_2D);

        glBegin(GL_QUADS);

        glTexCoord2f(0.0f, 0.0f);
        glVertex2f(x, y);

        glTexCoord2f(1.0f, 0.0f);
        glVertex2f(x+width, y);

        glTexCoord2f(1.0f, 1.0f);
        glVertex2f(x+width, y+height);

        glTexCoord2f(0.0f, 1.0f);
        glVertex2f(x, y+height);

        glEnd();

        glDisable(GL_TEXTURE_2D);

        stbi_image_free(image.image);

        glPopMatrix();
    }

    public static String getFont(){
        return font;
    }

    public static void setFont(String font){
        Renderer.font = font;
    }

}