/*
 * Created by Davide Cossu (gjkf), 8/7/2016
 */
package com.gjkf.seriousEngine.core.render;

import com.gjkf.seriousEngine.core.util.FileUtil;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.*;

public class Image{

    private String path;

    public ByteBuffer image;
    public int width, height, comp;

    public Image(String path){
        this.path = path;
        this.loadImage();
    }

    public Image(String path, ByteBuffer image, int width, int height, int comp){
        this.path = path;
        this.image = image;
        this.width = width;
        this.height = height;
        this.comp = comp;
    }

    public Image loadImage(){
        ByteBuffer imageBuffer = BufferUtils.createByteBuffer(8*1024);
        try{
            imageBuffer = FileUtil.ioResourceToByteBuffer(path, 8 * 1024);
        }catch(IOException e){
            e.printStackTrace();
        }

        IntBuffer ww = BufferUtils.createIntBuffer(1);
        IntBuffer hh = BufferUtils.createIntBuffer(1);
        IntBuffer compc = BufferUtils.createIntBuffer(1);

        stbi_info_from_memory(imageBuffer, ww, hh, compc);

        width = ww.get(0);
        height = hh.get(0);
        comp = compc.get(0);

        image = stbi_load_from_memory(imageBuffer, ww, hh, compc, 0);

        if(image == null)
            throw new RuntimeException("Failed to load image: " + stbi_failure_reason());

        return new Image(path, image, width, height, comp);
    }

}
