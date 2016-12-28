/*
 * Created by Davide Cossu (gjkf), 11/13/2016
 */

package me.gjkf.seriousEngine.render.lights;

import org.joml.Vector3f;

public class SceneLight{

    private Vector3f ambientLight;

    private Vector3f skyBoxLight;

    private PointLight[] pointLightList;

    private SpotLight[] spotLightList;

    private DirectionalLight directionalLight;

    /**
     * Getter for property 'ambientLight'.
     *
     * @return Value for property 'ambientLight'.
     */

    public Vector3f getAmbientLight(){
        return ambientLight;
    }

    /**
     * Setter for property 'ambientLight'.
     *
     * @param ambientLight Value to set for property 'ambientLight'.
     */

    public void setAmbientLight(Vector3f ambientLight){
        this.ambientLight = ambientLight;
    }

    /**
     * Getter for property 'pointLightList'.
     *
     * @return Value for property 'pointLightList'.
     */

    public PointLight[] getPointLightList(){
        return pointLightList;
    }

    /**
     * Setter for property 'pointLightList'.
     *
     * @param pointLightList Value to set for property 'pointLightList'.
     */

    public void setPointLightList(PointLight[] pointLightList){
        this.pointLightList = pointLightList;
    }

    /**
     * Getter for property 'spotLightList'.
     *
     * @return Value for property 'spotLightList'.
     */

    public SpotLight[] getSpotLightList(){
        return spotLightList;
    }

    /**
     * Setter for property 'spotLightList'.
     *
     * @param spotLightList Value to set for property 'spotLightList'.
     */

    public void setSpotLightList(SpotLight[] spotLightList){
        this.spotLightList = spotLightList;
    }

    /**
     * Getter for property 'directionalLight'.
     *
     * @return Value for property 'directionalLight'.
     */

    public DirectionalLight getDirectionalLight(){
        return directionalLight;
    }

    /**
     * Setter for property 'directionalLight'.
     *
     * @param directionalLight Value to set for property 'directionalLight'.
     */

    public void setDirectionalLight(DirectionalLight directionalLight){
        this.directionalLight = directionalLight;
    }

    /**
     * Getter for property 'skyBoxLight'.
     *
     * @return Value for property 'skyBoxLight'.
     */

    public Vector3f getSkyBoxLight(){
        return skyBoxLight;
    }

    /**
     * Setter for property 'skyBoxLight'.
     *
     * @param skyBoxLight Value to set for property 'skyBoxLight'.
     */

    public void setSkyBoxLight(Vector3f skyBoxLight){
        this.skyBoxLight = skyBoxLight;
    }

}