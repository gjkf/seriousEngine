/*
 * Created by Davide Cossu (gjkf), 11/6/2016
 */
package com.gjkf.seriousEngine.render;

import org.joml.Vector3f;

/**
 * Holds the information of a material.
 */

public class Material{

    /**
     * The default color (white).
     */
    private static final Vector3f DEFAULT_COLOUR = new Vector3f(1.0f, 1.0f, 1.0f);
    /**
     * The color.
     */
    private Vector3f colour;
    /**
     * The reflectance of the material.
     */
    private float reflectance;
    /**
     * The texture.
     */
    private Texture texture;
    /**
     * The texture of the normals.
     */
    private Texture normalMap;

    /**
     * Constructs a new Material.
     */
    public Material(){
        colour = DEFAULT_COLOUR;
        reflectance = 0;
    }

    public Material(Vector3f colour, float reflectance){
        this();
        this.colour = colour;
        this.reflectance = reflectance;
    }

    public Material(Texture texture){
        this();
        this.texture = texture;
    }

    public Material(Texture texture, float reflectance){
        this();
        this.texture = texture;
        this.reflectance = reflectance;
    }

    /**
     * Getter for property 'colour'.
     *
     * @return Value for property 'colour'.
     */

    public Vector3f getColour(){
        return colour;
    }

    /**
     * Setter for property 'colour'.
     *
     * @param colour Value to set for property 'colour'.
     */

    public void setColour(Vector3f colour){
        this.colour = colour;
    }

    /**
     * Getter for property 'reflectance'.
     *
     * @return Value for property 'reflectance'.
     */

    public float getReflectance(){
        return reflectance;
    }

    /**
     * Setter for property 'reflectance'.
     *
     * @param reflectance Value to set for property 'reflectance'.
     */

    public void setReflectance(float reflectance){
        this.reflectance = reflectance;
    }

    /**
     * Getter for property 'textured'.
     *
     * @return Value for property 'textured'.
     */

    public boolean isTextured(){
        return this.texture != null;
    }

    /**
     * Getter for property 'texture'.
     *
     * @return Value for property 'texture'.
     */

    public Texture getTexture(){
        return texture;
    }

    /**
     * Setter for property 'texture'.
     *
     * @param texture Value to set for property 'texture'.
     */

    public void setTexture(Texture texture){
        this.texture = texture;
    }

    /**
     * Returns if it has a normal map.
     *
     * @return TRUE if it has a normal map.
     */

    public boolean hasNormalMap(){
        return this.normalMap != null;
    }

    /**
     * Getter for property 'normalMap'.
     *
     * @return Value for property 'normalMap'.
     */

    public Texture getNormalMap(){
        return normalMap;
    }

    /**
     * Setter for property 'normalMap'.
     *
     * @param normalMap Value to set for property 'normalMap'.
     */

    public void setNormalMap(Texture normalMap){
        this.normalMap = normalMap;
    }

}