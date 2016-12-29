/*
 * Created by Davide Cossu (gjkf), 11/6/2016
 */
package io.github.gjkf.seriousEngine.render.lights;

import org.joml.Vector3f;

/**
 * Defines a directional light, like the sun.
 */

public class DirectionalLight{

    /**
     * The color.
     */
    private Vector3f color;
    /**
     * The direction.
     */
    private Vector3f direction;
    /**
     * The intensity.
     */
    private float intensity;
    /**
     * The orthographic coordinates,
     */
    private OrthoCoords orthoCords;
    /**
     * The factor.
     */
    private float shadowPosMult;

    /**
     * Creates a new directional light.
     *
     * @param color     The color.
     * @param direction The direction.
     * @param intensity The intensity.
     */

    public DirectionalLight(Vector3f color, Vector3f direction, float intensity){
        this.orthoCords = new OrthoCoords();
        this.shadowPosMult = 1;
        this.color = color;
        this.direction = direction;
        this.intensity = intensity;
    }

    /**
     * Copies a directional light.
     *
     * @param light The light.
     */

    public DirectionalLight(DirectionalLight light){
        this(new Vector3f(light.getColor()), new Vector3f(light.getDirection()), light.getIntensity());
    }


    /**
     * Getter for property 'shadowPosMult'.
     *
     * @return Value for property 'shadowPosMult'.
     */

    public float getShadowPosMult(){
        return shadowPosMult;
    }

    /**
     * Setter for property 'shadowPosMult'.
     *
     * @param shadowPosMult Value to set for property 'shadowPosMult'.
     */

    public void setShadowPosMult(float shadowPosMult){
        this.shadowPosMult = shadowPosMult;
    }

    /**
     * Getter for property 'orthoCoords'.
     *
     * @return Value for property 'orthoCoords'.
     */

    public OrthoCoords getOrthoCoords(){
        return orthoCords;
    }

    public void setOrthoCords(float left, float right, float bottom, float top, float near, float far){
        orthoCords.left = left;
        orthoCords.right = right;
        orthoCords.bottom = bottom;
        orthoCords.top = top;
        orthoCords.near = near;
        orthoCords.far = far;
    }

    /**
     * Getter for property 'color'.
     *
     * @return Value for property 'color'.
     */

    public Vector3f getColor(){
        return color;
    }

    /**
     * Setter for property 'color'.
     *
     * @param color Value to set for property 'color'.
     */

    public void setColor(Vector3f color){
        this.color = color;
    }

    /**
     * Getter for property 'direction'.
     *
     * @return Value for property 'direction'.
     */

    public Vector3f getDirection(){
        return direction;
    }

    /**
     * Setter for property 'direction'.
     *
     * @param direction Value to set for property 'direction'.
     */

    public void setDirection(Vector3f direction){
        this.direction = direction;
    }

    /**
     * Getter for property 'intensity'.
     *
     * @return Value for property 'intensity'.
     */

    public float getIntensity(){
        return intensity;
    }

    /**
     * Setter for property 'intensity'.
     *
     * @param intensity Value to set for property 'intensity'.
     */

    public void setIntensity(float intensity){
        this.intensity = intensity;
    }

    public static class OrthoCoords{

        public float left;

        public float right;

        public float bottom;

        public float top;

        public float near;

        public float far;
    }

}