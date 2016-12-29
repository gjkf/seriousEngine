/*
 * Created by Davide Cossu (gjkf), 11/6/2016
 */

package io.github.gjkf.seriousEngine.render.lights;

import org.joml.Vector3f;

/**
 * A spot light.
 * <p>Very similar to {@link PointLight} but it only works on a certain angle.</p>
 */

public class SpotLight{

    /**
     * The point light.
     */
    private PointLight pointLight;
    /**
     * The direction of the cone of light.
     */
    private Vector3f coneDirection;
    /**
     * The cut off.
     */
    private float cutOff;

    /**
     * Constructs a new Spot Light.
     *
     * @param pointLight    The point light to originate from.
     * @param coneDirection The direction of the cone.
     * @param cutOffAngle   The cutoff angle.
     */

    public SpotLight(PointLight pointLight, Vector3f coneDirection, float cutOffAngle){
        this.pointLight = pointLight;
        this.coneDirection = coneDirection;
        setCutOffAngle(cutOffAngle);
    }

    /**
     * Copies a Spot Light.
     *
     * @param spotLight The spotlight.
     */

    public SpotLight(SpotLight spotLight){
        this(new PointLight(spotLight.getPointLight()),
                new Vector3f(spotLight.getConeDirection()),
                spotLight.getCutOff());
    }

    /**
     * Getter for property 'pointLight'.
     *
     * @return Value for property 'pointLight'.
     */

    public PointLight getPointLight(){
        return pointLight;
    }

    /**
     * Setter for property 'pointLight'.
     *
     * @param pointLight Value to set for property 'pointLight'.
     */

    public void setPointLight(PointLight pointLight){
        this.pointLight = pointLight;
    }

    /**
     * Getter for property 'coneDirection'.
     *
     * @return Value for property 'coneDirection'.
     */

    public Vector3f getConeDirection(){
        return coneDirection;
    }

    /**
     * Setter for property 'coneDirection'.
     *
     * @param coneDirection Value to set for property 'coneDirection'.
     */

    public void setConeDirection(Vector3f coneDirection){
        this.coneDirection = coneDirection;
    }

    /**
     * Getter for property 'cutOff'.
     *
     * @return Value for property 'cutOff'.
     */

    public float getCutOff(){
        return cutOff;
    }

    /**
     * Setter for property 'cutOff'.
     *
     * @param cutOff Value to set for property 'cutOff'.
     */

    public void setCutOff(float cutOff){
        this.cutOff = cutOff;
    }

    /**
     * Setter for property 'cutOffAngle'.
     *
     * @param cutOffAngle Value to set for property 'cutOffAngle'.
     */

    public final void setCutOffAngle(float cutOffAngle){
        this.setCutOff((float) Math.cos(Math.toRadians(cutOffAngle)));
    }

}