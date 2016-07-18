/*
 * Created by Davide Cossu (gjkf), 7/17/2016
 */

package com.gjkf.seriousEngine.core.render;

import com.gjkf.seriousEngine.core.math.Matrix4f;
import com.gjkf.seriousEngine.core.render.font.Font;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBEasyFont.stb_easy_font_print;

/**
 * This class is performing the rendering process.
 *
 * @author Heiko Brumme
 */
public class Render {

    private VertexArrayObject vao;
    private VertexBufferObject vbo;
    private Shader vertexShader;
    private Shader fragmentShader;
    private ShaderProgram program;
    private Font font;
    private Font debugFont;

    private boolean defaultContext;

    private FloatBuffer vertices;
    private int numVertices;
    private boolean drawing;
    private boolean hasShaders = true;

    /**
     * Initializes the renderer.
     *
     * @param defaultContext Specifies if the OpenGL context is 3.2 compatible
     */
    public void init(boolean defaultContext, String[] vertShader, String[] fragShader, String fontPath) {

        if(vertShader == null && fragShader == null){
            hasShaders = false;
        }else{
            if(vertShader.length < 2 || fragShader.length < 2)
                throw new RuntimeException("Argument fragShader or vertShader is too short.");
        }
        this.defaultContext = defaultContext;

        if (defaultContext) {
            /* Generate Vertex Array Object */
            vao = new VertexArrayObject();
            vao.bind();
        } else {
            vao = null;
        }

        /* Generate Vertex Buffer Object */
        vbo = new VertexBufferObject();
        vbo.bind(GL_ARRAY_BUFFER);

        /* Create FloatBuffer */
        vertices = BufferUtils.createFloatBuffer(4096);

        /* Upload null data to allocate storage for the VBO */
        long size = vertices.capacity() * Float.BYTES;
        vbo.uploadData(GL_ARRAY_BUFFER, size, GL_DYNAMIC_DRAW);

        /* Initialize variables */
        numVertices = 0;
        drawing = false;

        if(hasShaders){
        /* Load shaders */
            if(defaultContext){
                vertexShader = Shader.loadShader(GL_VERTEX_SHADER, vertShader[0]);
                fragmentShader = Shader.loadShader(GL_FRAGMENT_SHADER, fragShader[0]);
            }else{
                vertexShader = Shader.loadShader(GL_VERTEX_SHADER, vertShader[1]);
                fragmentShader = Shader.loadShader(GL_FRAGMENT_SHADER, fragShader[1]);
            }
            /* Create shader program */
            program = new ShaderProgram();
            program.attachShader(vertexShader);
            program.attachShader(fragmentShader);
            if (defaultContext) {
                program.bindFragmentDataLocation(0, "fragColor3f");
            }
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
            specifyVertexAttributes();

            /* Set texture uniform */
            int uniTex = program.getUniformLocation("texImage");
            program.setUniform(uniTex, 0);

            /* Set model matrix to identity matrix */
            Matrix4f model = new Matrix4f();
            int uniModel = program.getUniformLocation("model");
            program.setUniform(uniModel, model);

            /* Set view matrix to identity matrix */
            Matrix4f view = new Matrix4f();
            int uniView = program.getUniformLocation("view");
            program.setUniform(uniView, view);

            /* Set projection matrix to an orthographic projection */
            Matrix4f projection = Matrix4f.orthographic(0f, width, 0f, height, -1f, 1f);
            int uniProjection = program.getUniformLocation("projection");
            program.setUniform(uniProjection, projection);
        }

        /* Enable blending */
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        /* Create fonts */
        try {
            font = new Font(new FileInputStream(fontPath), 16);
        } catch (FontFormatException | IOException | NullPointerException ex) {
            font = new Font();
            ex.printStackTrace();
        }
        debugFont = new Font(12, false);
    }

    /**
     * Clears the drawing area.
     */
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Begin rendering.
     */
    public void begin() {
        if (drawing) {
            throw new IllegalStateException("Renderer is already drawing!");
        }
        drawing = true;
        numVertices = 0;
    }

    /**
     * End rendering.
     */
    public void end() {
        if (!drawing) {
            throw new IllegalStateException("Renderer isn't drawing!");
        }
        drawing = false;
        flush();
    }

    /**
     * Flushes the data to the GPU to let it get rendered.
     */
    public void flush() {
        if (numVertices > 0) {
            vertices.flip();

            if (vao != null) {
                vao.bind();
            } else {
                vbo.bind(GL_ARRAY_BUFFER);
                specifyVertexAttributes();
            }
            if(hasShaders)
                program.use();

            /* Upload the new vertex data */
            vbo.bind(GL_ARRAY_BUFFER);
            vbo.uploadSubData(GL_ARRAY_BUFFER, 0, vertices);

            /* Draw batch */
            glDrawArrays(GL_TRIANGLES, 0, numVertices);

            /* Clear vertex data for next batch */
            vertices.clear();
            numVertices = 0;
        }
    }

    /**
     * Calculates total width of a text.
     *
     * @param text The text
     *
     * @return Total width of the text
     */
    public int getTextWidth(CharSequence text) {
        return font.getWidth(text);
    }

    /**
     * Calculates total height of a text.
     *
     * @param text The text
     *
     * @return Total width of the text
     */
    public int getTextHeight(CharSequence text) {
        return font.getHeight(text);
    }

    /**
     * Calculates total width of a debug text.
     *
     * @param text The text
     *
     * @return Total width of the text
     */
    public int getDebugTextWidth(CharSequence text) {
        return debugFont.getWidth(text);
    }

    /**
     * Calculates total height of a debug text.
     *
     * @param text The text
     *
     * @return Total width of the text
     */
    public int getDebugTextHeight(CharSequence text) {
        return debugFont.getHeight(text);
    }

    /**
     * Draw text at the specified position.
     *
     * @param text Text to draw
     * @param x    X coordinate of the text position
     * @param y    Y coordinate of the text position
     */
    public void drawText(CharSequence text, float x, float y) {
        font.drawText(this, text, x, y);
    }

    /**
     * Draw debug text at the specified position.
     *
     * @param text Text to draw
     * @param x    X coordinate of the text position
     * @param y    Y coordinate of the text position
     */
    public void drawDebugText(CharSequence text, float x, float y) {
        debugFont.drawText(this, text, x, y);
    }

    /**
     * Draw text at the specified position and Color3f.
     *
     * @param text Text to draw
     * @param x    X coordinate of the text position
     * @param y    Y coordinate of the text position
     * @param c    Color3f to use
     */
    public void drawText(CharSequence text, float x, float y, Color3f c) {
        font.drawText(this, text, x, y, c);
    }

    /**
     * Draw debug text at the specified position and Color3f.
     *
     * @param text Text to draw
     * @param x    X coordinate of the text position
     * @param y    Y coordinate of the text position
     * @param c    Color3f to use
     */
    public void drawDebugText(CharSequence text, float x, float y, Color3f c) {
        debugFont.drawText(this, text, x, y, c);
    }

    /**
     * Draws the currently bound texture on specified coordinates.
     *
     * @param texture Used for getting width and height of the texture
     * @param x       X position of the texture
     * @param y       Y position of the texture
     */
    public void drawTexture(Texture texture, float x, float y) {
        drawTexture(texture, x, y, Colors.WHITE.color);
    }

    /**
     * Draws the currently bound texture on specified coordinates and with
     * specified Color3f.
     *
     * @param texture Used for getting width and height of the texture
     * @param x       X position of the texture
     * @param y       Y position of the texture
     * @param c       The Color3f to use
     */
    public void drawTexture(Texture texture, float x, float y, Color3f c) {
        /* Vertex positions */
        float x1 = x;
        float y1 = y;
        float x2 = x1 + texture.getWidth();
        float y2 = y1 + texture.getHeight();

        /* Texture coordinates */
        float s1 = 0f;
        float t1 = 0f;
        float s2 = 1f;
        float t2 = 1f;

        drawTextureRegion(x1, y1, x2, y2, s1, t1, s2, t2, c);
    }

    /**
     * Draws a texture region with the currently bound texture on specified
     * coordinates.
     *
     * @param texture   Used for getting width and height of the texture
     * @param x         X position of the texture
     * @param y         Y position of the texture
     * @param regX      X position of the texture region
     * @param regY      Y position of the texture region
     * @param regWidth  Width of the texture region
     * @param regHeight Height of the texture region
     */
    public void drawTextureRegion(Texture texture, float x, float y, float regX, float regY, float regWidth, float regHeight) {
        drawTextureRegion(texture, x, y, regX, regY, regWidth, regHeight, Colors.WHITE.color);
    }

    /**
     * Draws a texture region with the currently bound texture on specified
     * coordinates.
     *
     * @param texture   Used for getting width and height of the texture
     * @param x         X position of the texture
     * @param y         Y position of the texture
     * @param regX      X position of the texture region
     * @param regY      Y position of the texture region
     * @param regWidth  Width of the texture region
     * @param regHeight Height of the texture region
     * @param c         The Color3f to use
     */
    public void drawTextureRegion(Texture texture, float x, float y, float regX, float regY, float regWidth, float regHeight, Color3f c) {
        /* Vertex positions */
        float x1 = x;
        float y1 = y;
        float x2 = x + regWidth;
        float y2 = y + regHeight;

        /* Texture coordinates */
        float s1 = regX / texture.getWidth();
        float t1 = regY / texture.getHeight();
        float s2 = (regX + regWidth) / texture.getWidth();
        float t2 = (regY + regHeight) / texture.getHeight();

        drawTextureRegion(x1, y1, x2, y2, s1, t1, s2, t2, c);
    }

    /**
     * Draws a texture region with the currently bound texture on specified
     * coordinates.
     *
     * @param x1 Bottom left x position
     * @param y1 Bottom left y position
     * @param x2 Top right x position
     * @param y2 Top right y position
     * @param s1 Bottom left s coordinate
     * @param t1 Bottom left t coordinate
     * @param s2 Top right s coordinate
     * @param t2 Top right t coordinate
     */
    public void drawTextureRegion(float x1, float y1, float x2, float y2, float s1, float t1, float s2, float t2) {
        drawTextureRegion(x1, y1, x2, y2, s1, t1, s2, t2, Colors.WHITE.color);
    }

    /**
     * Draws a texture region with the currently bound texture on specified
     * coordinates.
     *
     * @param x1 Bottom left x position
     * @param y1 Bottom left y position
     * @param x2 Top right x position
     * @param y2 Top right y position
     * @param s1 Bottom left s coordinate
     * @param t1 Bottom left t coordinate
     * @param s2 Top right s coordinate
     * @param t2 Top right t coordinate
     * @param c  The Color3f to use
     */
    public void drawTextureRegion(float x1, float y1, float x2, float y2, float s1, float t1, float s2, float t2, Color3f c) {
        if (vertices.remaining() < 7 * 6) {
            /* We need more space in the buffer, so flush it */
            flush();
        }

        float r = c.r;
        float g = c.g;
        float b = c.b;

        vertices.put(x1).put(y1).put(r).put(g).put(b).put(s1).put(t1);
        vertices.put(x1).put(y2).put(r).put(g).put(b).put(s1).put(t2);
        vertices.put(x2).put(y2).put(r).put(g).put(b).put(s2).put(t2);

        vertices.put(x1).put(y1).put(r).put(g).put(b).put(s1).put(t1);
        vertices.put(x2).put(y2).put(r).put(g).put(b).put(s2).put(t2);
        vertices.put(x2).put(y1).put(r).put(g).put(b).put(s2).put(t1);

        numVertices += 6;
    }

    /**
     * Dispose renderer and clean up its used data.
     */
    public void dispose() {
        if (vao != null) {
            vao.delete();
        }
        vbo.delete();
        vertexShader.delete();
        fragmentShader.delete();
        program.delete();
        font.dispose();
    }

    /**
     * Shows if the OpenGL context supports version 3.2.
     *
     * @return true, if OpenGL context supports version 3.2, else false
     */
    public boolean hasDefaultContext() {
        return defaultContext;
    }

    /**
     * Specifies the vertex pointers.
     */
    private void specifyVertexAttributes() {
        /* Specify Vertex Pointer */
        int posAttrib = program.getAttributeLocation("position");
        program.enableVertexAttribute(posAttrib);
        program.pointVertexAttribute(posAttrib, 2, 7 * Float.BYTES, 0);

        /* Specify Color3f Pointer */
        int colAttrib = program.getAttributeLocation("color");
        program.enableVertexAttribute(colAttrib);
        program.pointVertexAttribute(colAttrib, 3, 7 * Float.BYTES, 2 * Float.BYTES);

        /* Specify Texture Pointer */
        int texAttrib = program.getAttributeLocation("texcoord");
        program.enableVertexAttribute(texAttrib);
        program.pointVertexAttribute(texAttrib, 2, 7 * Float.BYTES, 5 * Float.BYTES);
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

}
