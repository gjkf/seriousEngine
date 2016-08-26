/*
 * Created by Davide Cossu (gjkf), 8/7/2016
 */
package com.gjkf.seriousEngine.core.render;

import com.gjkf.seriousEngine.core.util.FileUtil;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;

/**
 * An image object.
 * <p>Load new images with {@link #loadImage(String)}</p>
 */

public class Image{

    /**
     * The buffer for the image
     */
    private ByteBuffer image;
    /**
     * Dimensions and texture ID
     */
    private int width, height, id;

    /**
     * The image constructor
     *
     * @param width The width
     * @param height The height
     * @param data The data of the image
     */

    private Image(int width, int height, ByteBuffer data){
        this.id = glGenTextures();
        this.width = width;
        this.height = height;
        this.image = data;

        glBindTexture(GL_TEXTURE_2D, id);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
    }

    /**
     * Returns the texture ID
     *
     * @return The ID
     */

    public int getID(){
        return id;
    }

    /**
     * Returns the width
     *
     * @return The width
     */

    public int getWidth(){
        return width;
    }

    /**
     * Returns the height
     *
     * @return The height
     */

    public int getHeight(){
        return height;
    }

    /**
     * Returns the image buffer
     *
     * @return The buffer
     */

    public ByteBuffer getImage(){
        return image;
    }

    /**
     * Loads an image and returns a new object.
     * <p>The path is relative to the current resources folder</p>
     *
     * @param path The path from where to load the image
     *
     * @return The newly loaded image
     */

    public static Image loadImage(String path){
        InputStream in;
        BufferedImage image = null;

        try{
            in = FileUtil.loadResource(path);
            image = ImageIO.read(in);
        }catch(IOException e){
            e.printStackTrace();
        }

        assert image != null;
        int width = image.getWidth();
        int height = image.getHeight();

        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                     /* Pixel as RGBA: 0xAARRGGBB */
                int pixel = pixels[y * width + x];

                    /* Red component 0xAARRGGBB >> (4 * 4) = 0x0000AARR */
                buffer.put((byte) ((pixel >> 16) & 0xFF));

                    /* Green component 0xAARRGGBB >> (2 * 4) = 0x00AARRGG */
                buffer.put((byte) ((pixel >> 8) & 0xFF));

                    /* Blue component 0xAARRGGBB >> 0 = 0xAARRGGBB */
                buffer.put((byte) (pixel & 0xFF));

                    /* Alpha component 0xAARRGGBB >> (6 * 4) = 0x000000AA */
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

            /* Do not forget to flip the buffer! */
        buffer.flip();

        return new Image(width, height, buffer);
    }

}
