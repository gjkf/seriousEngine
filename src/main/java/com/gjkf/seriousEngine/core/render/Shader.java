/*
 * Created by Davide Cossu (gjkf), 7/17/2016
 */
package com.gjkf.seriousEngine.core.render;

import com.gjkf.seriousEngine.core.util.FileUtil;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;

/**
 * This class represents a shader.
 *
 * @author Heiko Brumme
 */
public class Shader {

    /**
     * Stores the handle of the shader.
     */
    private final int id;

    /**
     * Creates a shader with specified type and source and compiles it. The type
     * in the tutorial should be either <code>GL_VERTEX_SHADER</code> or
     * <code>GL_FRAGMENT_SHADER</code>.
     *
     * @param type   Type of the shader
     * @param source Source of the shader
     */
    public Shader(int type, CharSequence source) {
        id = glCreateShader(type);
        glShaderSource(id, source);
        glCompileShader(id);

        checkStatus();
    }

    /**
     * Checks if the shader was compiled successfully.
     */
    private void checkStatus() {
        int status = glGetShaderi(id, GL_COMPILE_STATUS);
        if (status != GL_TRUE) {
            throw new RuntimeException(glGetShaderInfoLog(id));
        }
    }

    /**
     * Deletes the shader.
     */
    public void delete() {
        glDeleteShader(id);
    }

    /**
     * Getter for the shader ID.
     *
     * @return Handle of this shader
     */
    public int getID() {
        return id;
    }

    /**
     * Load shader from file.
     *
     * @param type Type of the shader
     * @param path File path of the shader
     *
     * @return Shader from specified file
     */
    public static Shader loadShader(int type, String path) {
        return new Shader(type, FileUtil.readFile(path));
    }

}
