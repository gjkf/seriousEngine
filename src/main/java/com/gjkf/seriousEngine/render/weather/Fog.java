/*
 * Created by Davide Cossu (gjkf), 11/20/2016
 */

package com.gjkf.seriousEngine.render.weather;

import org.joml.Vector3f;

/**
 * A class representing fog.
 */

public class Fog{

    /**
     * Whether the fog effect is active or not.
     */
    private boolean active;
    /**
     * The colour.
     */
    private Vector3f colour;
    /**
     * The density.
     */
    private float density;

    public static Fog NOFOG = new Fog();

    /**
     * Constructs a new Fog.
     */
    public Fog(){
        active = false;
        this.colour = new Vector3f(0, 0, 0);
        this.density = 0;
    }

    /**
     * Constructs a new Fog.
     *
     * @param active  Whether or not it's active.
     * @param colour  The colour.
     * @param density The density
     */

    public Fog(boolean active, Vector3f colour, float density){
        this.colour = colour;
        this.density = density;
        this.active = active;
    }

    /**
     * Getter for property 'active'.
     *
     * @return Value for property 'active'.
     */

    public boolean isActive(){
        return active;
    }

    /**
     * Setter for property 'active'.
     *
     * @param active Value to set for property 'active'.
     */

    public void setActive(boolean active){
        this.active = active;
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
     * Getter for property 'density'.
     *
     * @return Value for property 'density'.
     */

    public float getDensity(){
        return density;
    }

    /**
     * Setter for property 'density'.
     *
     * @param density Value to set for property 'density'.
     */

    public void setDensity(float density){
        this.density = density;
    }


}