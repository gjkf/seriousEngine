/*
 * Created by Davide Cossu (gjkf), 11/15/2016
 */

package io.github.gjkf.seriousEngine.render.shaders;

import java.nio.FloatBuffer;

/**
 * Simple place holder class used with engineShaders.
 */

public class UniformData{

    /**
     * The location of the uniform, its index.
     */
    private final int uniformLocation;
    /**
     * The buffer that holds the information.
     */
    private FloatBuffer floatBuffer;

    /**
     * Constructs a new object.
     *
     * @param uniformLocation The location.
     */
    public UniformData(int uniformLocation){
        this.uniformLocation = uniformLocation;
    }

    /**
     * Getter for property 'uniformLocation'.
     *
     * @return Value for property 'uniformLocation'.
     */

    public int getUniformLocation(){
        return uniformLocation;
    }

    /**
     * Getter for property 'floatBuffer'.
     *
     * @return Value for property 'floatBuffer'.
     */

    public FloatBuffer getFloatBuffer(){
        return floatBuffer;
    }

    /**
     * Setter for property 'floatBuffer'.
     *
     * @param floatBuffer Value to set for property 'floatBuffer'.
     */

    public void setFloatBuffer(FloatBuffer floatBuffer){
        this.floatBuffer = floatBuffer;
    }
}