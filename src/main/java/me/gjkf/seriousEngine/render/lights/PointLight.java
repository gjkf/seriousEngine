/*
 * Created by Davide Cossu (gjkf), 11/6/2016
 */
package me.gjkf.seriousEngine.render.lights;

import org.joml.Vector3f;

/**
 * Defines a point light.
 */

public class PointLight{

    /**
     * The color.
     */
    private Vector3f color;
    /**
     * The position.
     */
    private Vector3f position;
    /**
     * The intensity.
     */
    protected float intensity;
    /**
     * The attenuation. See {@link Attenuation}.
     */
    private Attenuation attenuation;

    /**
     * Defines a new point light.
     *
     * @param color     The color.
     * @param position  The position.
     * @param intensity The intensity.
     */

    public PointLight(Vector3f color, Vector3f position, float intensity){
        attenuation = new Attenuation(1, 0, 0);
        this.color = color;
        this.position = position;
        this.intensity = intensity;
    }

    /**
     * Defines a new point light.
     *
     * @param color       The color.
     * @param position    The position.
     * @param intensity   The intensity.
     * @param attenuation The attenuation.
     */

    public PointLight(Vector3f color, Vector3f position, float intensity, Attenuation attenuation){
        this(color, position, intensity);
        this.attenuation = attenuation;
    }

    /**
     * Copies a point light.
     *
     * @param pointLight The point light.
     */

    public PointLight(PointLight pointLight){
        this(new Vector3f(pointLight.getColor()), new Vector3f(pointLight.getPosition()),
                pointLight.getIntensity(), pointLight.getAttenuation());
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
     * Getter for property 'position'.
     *
     * @return Value for property 'position'.
     */
    public Vector3f getPosition(){
        return position;
    }

    /**
     * Setter for property 'position'.
     *
     * @param position Value to set for property 'position'.
     */
    public void setPosition(Vector3f position){
        this.position = position;
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

    /**
     * Getter for property 'attenuation'.
     *
     * @return Value for property 'attenuation'.
     */
    public Attenuation getAttenuation(){
        return attenuation;
    }

    /**
     * Setter for property 'attenuation'.
     *
     * @param attenuation Value to set for property 'attenuation'.
     */
    public void setAttenuation(Attenuation attenuation){
        this.attenuation = attenuation;
    }

    /**
     * Used to find the intensity of the light based on the distance.
     */

    public static class Attenuation{

        /**
         * The constant.
         */
        private float constant;
        /**
         * The linear component.
         */
        private float linear;
        /**
         * The exponent.
         */
        private float exponent;

        /**
         * Creates a new attenuation value.
         *
         * @param constant The constant.
         * @param linear   The linear component.
         * @param exponent The exponent.
         */

        public Attenuation(float constant, float linear, float exponent){
            this.constant = constant;
            this.linear = linear;
            this.exponent = exponent;
        }

        /**
         * Getter for property 'constant'.
         *
         * @return Value for property 'constant'.
         */
        public float getConstant(){
            return constant;
        }

        /**
         * Setter for property 'constant'.
         *
         * @param constant Value to set for property 'constant'.
         */
        public void setConstant(float constant){
            this.constant = constant;
        }

        /**
         * Getter for property 'linear'.
         *
         * @return Value for property 'linear'.
         */
        public float getLinear(){
            return linear;
        }

        /**
         * Setter for property 'linear'.
         *
         * @param linear Value to set for property 'linear'.
         */
        public void setLinear(float linear){
            this.linear = linear;
        }

        /**
         * Getter for property 'exponent'.
         *
         * @return Value for property 'exponent'.
         */
        public float getExponent(){
            return exponent;
        }

        /**
         * Setter for property 'exponent'.
         *
         * @param exponent Value to set for property 'exponent'.
         */
        public void setExponent(float exponent){
            this.exponent = exponent;
        }
    }
}