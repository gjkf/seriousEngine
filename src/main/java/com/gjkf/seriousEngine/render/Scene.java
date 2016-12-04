/*
 * Created by Davide Cossu (gjkf), 11/15/2016
 */

package com.gjkf.seriousEngine.render;

import com.gjkf.seriousEngine.items.Item;
import com.gjkf.seriousEngine.items.SkyBox;
import com.gjkf.seriousEngine.render.lights.SceneLight;
import com.gjkf.seriousEngine.render.particles.IParticleEmitter;
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
     * The map containing all the instanced meshes and the corresponding items.
     */
    private final Map<InstancedMesh, List<Item>> instancedMeshMap;
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
     * The particle emitters.
     */
    private IParticleEmitter[] particleEmitters;
    /**
     * Whether or not render shadows.
     */
    private boolean renderShadows;

    /**
     * Constructs a new Scene.
     */
    public Scene(){
        meshMap = new HashMap<>();
        instancedMeshMap = new HashMap<>();
        fog = Fog.NOFOG;
        renderShadows = true;
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
        // Create a map of meshes to speed up rendering
        int numItems = items != null ? items.length : 0;
        for(int i = 0; i < numItems; i++){
            Item item = items[i];
            Mesh[] meshes = item.getMeshes();
            for(Mesh mesh : meshes){
                boolean instancedMesh = mesh instanceof InstancedMesh;
                List<Item> list = instancedMesh ? instancedMeshMap.get(mesh) : meshMap.get(mesh);
                if(list == null){
                    list = new ArrayList<>();
                    if(instancedMesh){
                        instancedMeshMap.put((InstancedMesh) mesh, list);
                    }else{
                        meshMap.put(mesh, list);
                    }
                }
                list.add(item);
            }
        }
    }

    /**
     * Getter for property 'meshMap'.
     *
     * @return Value for property 'meshMap'.
     */

    public Map<Mesh, List<Item>> getMeshMap(){
        return meshMap;
    }


    /**
     * Setter for property 'meshMap'.
     *
     * @param meshMap Value to set for property 'meshMap'.
     */

    public void setMeshMap(Map<Mesh, List<Item>> meshMap){
        this.meshMap = meshMap;
    }

    /**
     * Getter for property 'instancedMeshMap'.
     *
     * @return Value for property 'instancedMeshMap'.
     */

    public Map<InstancedMesh, List<Item>> getInstancedMeshes(){
        return instancedMeshMap;
    }

    /**
     * Getter for property 'renderShadows'.
     *
     * @return Value for property 'renderShadows'.
     */

    public boolean isRenderShadows(){
        return renderShadows;
    }

    /**
     * Setter for property 'renderShadows'.
     *
     * @param renderShadows Value to set for property 'renderShadows'.
     */

    public void setRenderShadows(boolean renderShadows){
        this.renderShadows = renderShadows;
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
     * Getter for property 'particleEmitters'.
     *
     * @return Value for property 'particleEmitters'.
     */

    public IParticleEmitter[] getParticleEmitters(){
        return particleEmitters;
    }

    /**
     * Setter for property 'particleEmitters'.
     *
     * @param particleEmitters Value to set for property 'particleEmitters'.
     */

    public void setParticleEmitters(IParticleEmitter[] particleEmitters){
        this.particleEmitters = particleEmitters;
    }

    /**
     * Cleans up the meshes.
     */

    public void cleanup(){
        for(Mesh mesh : meshMap.keySet()){
            mesh.cleanUp();
        }
        for(Mesh mesh : instancedMeshMap.keySet()){
            mesh.cleanUp();
        }
        if(particleEmitters != null){
            for(IParticleEmitter particleEmitter : particleEmitters){
                particleEmitter.cleanup();
            }
        }
    }

}