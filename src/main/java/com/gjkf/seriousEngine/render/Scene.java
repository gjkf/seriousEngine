/*
 * Created by Davide Cossu (gjkf), 11/15/2016
 */

package com.gjkf.seriousEngine.render;

import com.gjkf.seriousEngine.items.Item;
import com.gjkf.seriousEngine.items.SkyBox;
import com.gjkf.seriousEngine.render.lights.SceneLight;
import com.gjkf.seriousEngine.render.weather.Fog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing a typical scene.
 * <p>Contains a {@link SceneLight}, some items and a {@link SkyBox}.</p>
 */

public class Scene{

    /**
     * The map containing all the meshes and the correspondent items.
     */
    private Map<Mesh, List<Item>> meshMap;
    /**
     * The sky box.
     */
    private SkyBox skyBox;
    /**
     * The light.
     */
    private SceneLight sceneLight;
    /**
     * The fog.
     */
    private Fog fog;

    /**
     * Constructs a new Scene.
     */
    public Scene(){
        meshMap = new HashMap<>();
        fog = Fog.NOFOG;
    }

    /**
     * Getter for property 'gameMeshes'.
     *
     * @return Value for property 'gameMeshes'.
     */

    public Map<Mesh, List<Item>> getMeshes(){
        return meshMap;
    }

    /**
     * Setter for property 'items'.
     *
     * @param items Value to set for property 'items'.
     */

    public void setItems(Item[] items){
        int numItems = items != null ? items.length : 0;
        for(int i = 0; i < numItems; i++){
            Item item = items[i];
            Mesh mesh = item.getMesh();
            List<Item> list = meshMap.get(mesh);
            if(list == null){
                list = new ArrayList<>();
                meshMap.put(mesh, list);
            }
            list.add(item);
        }
    }

    /**
     * Getter for property 'skyBox'.
     *
     * @return Value for property 'skyBox'.
     */

    public SkyBox getSkyBox(){
        return skyBox;
    }

    /**
     * Setter for property 'skyBox'.
     *
     * @param skyBox Value to set for property 'skyBox'.
     */

    public void setSkyBox(SkyBox skyBox){
        this.skyBox = skyBox;
    }

    /**
     * Getter for property 'sceneLight'.
     *
     * @return Value for property 'sceneLight'.
     */

    public SceneLight getSceneLight(){
        return sceneLight;
    }

    /**
     * Setter for property 'sceneLight'.
     *
     * @param sceneLight Value to set for property 'sceneLight'.
     */

    public void setSceneLight(SceneLight sceneLight){
        this.sceneLight = sceneLight;
    }

    /**
     * Getter for property 'fog'.
     *
     * @return Value for property 'fog'.
     */

    public Fog getFog(){
        return fog;
    }

    /**
     * Setter for property 'fog'.
     *
     * @param fog Value to set for property 'fog'.
     */

    public void setFog(Fog fog){
        this.fog = fog;
    }

    /**
     * Cleans up the meshes.
     */

    public void cleanup(){
        meshMap.keySet().forEach(Mesh::cleanUp);
    }

}