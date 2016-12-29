/*
 * Created by Davide Cossu (gjkf), 11/2/2016
 */
package io.github.gjkf.seriousEngine.render;

import de.matthiasmann.twl.utils.PNGDecoder;

import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

/**
 * Object representing a texture.
 */

public class Texture{

    /**
     * The ID of the texture
     */
    private final int id;
    /**
     * The width.
     */
    private final int width;
    /**
     * The height.
     */
    private final int height;
    /**
     * The number of rows.
     */
    private int numRows = 1;
    /**
     * The number of columns.
     */
    private int numCols = 1;

    /**
     * Creates an empty texture.
     *
     * @param width       Width of the texture
     * @param height      Height of the texture
     * @param pixelFormat Specifies the format of the pixel data (GL_RGBA, etc.)
     *
     * @throws Exception If the image could not be bound.
     */

    public Texture(int width, int height, int pixelFormat) throws Exception{
        this.id = glGenTextures();
        this.width = width;
        this.height = height;
        glBindTexture(GL_TEXTURE_2D, this.id);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, this.width, this.height, 0, pixelFormat, GL_FLOAT, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    }

    public Texture(String fileName) throws Exception{
        this(Texture.class.getResourceAsStream(fileName));
    }

    public Texture(String fileName, int numCols, int numRows) throws Exception{
        this(fileName);
        this.numCols = numCols;
        this.numRows = numRows;
    }

    public Texture(InputStream is) throws Exception{
        try{
            // Load Texture file
            PNGDecoder decoder = new PNGDecoder(is);

            this.width = decoder.getWidth();
            this.height = decoder.getHeight();

            // Load texture contents into a byte buffer
            ByteBuffer buf = ByteBuffer.allocateDirect(
                    4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buf.flip();

            // Create a new OpenGL texture
            this.id = glGenTextures();
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, this.id);

            // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            // Upload the texture data
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
            // Generate Mip Map
            glGenerateMipmap(GL_TEXTURE_2D);

            is.close();
        }finally{
            if(is != null){
                is.close();
            }
        }
    }

    /**
     * Getter for property 'numCols'.
     *
     * @return Value for property 'numCols'.
     */

    public int getNumCols(){
        return numCols;
    }

    /**
     * Getter for property 'numRows'.
     *
     * @return Value for property 'numRows'.
     */

    public int getNumRows(){
        return numRows;
    }

    /**
     * Getter for property 'width'.
     *
     * @return Value for property 'width'.
     */

    public int getWidth(){
        return this.width;
    }

    /**
     * Getter for property 'height'.
     *
     * @return Value for property 'height'.
     */

    public int getHeight(){
        return this.height;
    }

    /**
     * Binds the texture.
     */

    public void bind(){
        glBindTexture(GL_TEXTURE_2D, id);
    }

    /**
     * Getter for property 'id'.
     *
     * @return Value for property 'id'.
     */

    public int getId(){
        return id;
    }

    /**
     * Cleans up the texture.
     */

    public void cleanup(){
        glDeleteTextures(id);
    }

}
