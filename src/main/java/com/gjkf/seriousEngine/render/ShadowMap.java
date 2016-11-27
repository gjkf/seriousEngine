/*
 * Created by Davide Cossu (gjkf), 11/26/2016
 */

package com.gjkf.seriousEngine.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Class representing a Shadow Map.
 */

public class ShadowMap{

    /**
     * The map width.
     */
    public static final int SHADOW_MAP_WIDTH = 1024;
    /**
     * The map height.
     */
    public static final int SHADOW_MAP_HEIGHT = 1024;
    /**
     * The FrameBufferObject (FBO)
     */
    private final int depthMapFBO;
    /**
     * The texture that is created.
     */
    private final Texture depthMap;

    /**
     * Constructs a new ShadowMap.
     */
    public ShadowMap() throws Exception{
        // Create a FBO to render the depth map
        depthMapFBO = glGenFramebuffers();

        // Create the depth map texture
        depthMap = new Texture(SHADOW_MAP_WIDTH, SHADOW_MAP_HEIGHT, GL_DEPTH_COMPONENT);

        // Attach the the depth map texture to the FBO
        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMap.getId(), 0);
        // Set only depth
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE){
            throw new Exception("Could not create FrameBuffer");
        }

        // Unbind
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    /**
     * Getter for property 'depthMapTexture'.
     *
     * @return Value for property 'depthMapTexture'.
     */

    public Texture getDepthMapTexture(){
        return depthMap;
    }

    /**
     * Getter for property 'depthMapFBO'.
     *
     * @return Value for property 'depthMapFBO'.
     */

    public int getDepthMapFBO(){
        return depthMapFBO;
    }

    /**
     * Cleans up resources.
     */

    public void cleanup(){
        glDeleteFramebuffers(depthMapFBO);
        depthMap.cleanup();
    }

}