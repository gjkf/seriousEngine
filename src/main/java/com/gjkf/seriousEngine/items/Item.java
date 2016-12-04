/*
 * Created by Davide Cossu (gjkf), 11/2/2016
 */
package com.gjkf.seriousEngine.items;

import com.gjkf.seriousEngine.render.Mesh;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Object representing an item in the world.
 */

public class Item{

    /**
     * The mesh of this item.
     */
    private Mesh[] meshes;
    /**
     * The position in the world.
     */
    private final Vector3f position;
    /**
     * The scale.
     */
    private float scale;
    /**
     * The rotation.
     */
    private final Quaternionf rotation;
    /**
     * The texture position.
     */
    private int textPos;

    /**
     * Constructs a new Item.
     */

    public Item(){
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Quaternionf();
        textPos = 0;
    }

    /**
     * Creates a new item with position and rotation like <tt>new Vector3f(0,0,0)</tt>
     *
     * @param mesh The mesh of the item.
     */

    public Item(Mesh mesh){
        this();
        this.meshes = new Mesh[] {mesh};
    }

    public Item(Mesh[] meshes){
        this();
        this.meshes = meshes;
    }

    /**
     * Returns the position.
     *
     * @return The position vector.
     */

    public Vector3f getPosition(){
        return position;
    }

    /**
     * Sets the position of this item.
     *
     * @param x The position in the X axis.
     * @param y The position in the Y axis.
     * @param z The position in the Z axis.
     */

    public void setPosition(float x, float y, float z){
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    /**
     * Gets the scale of this item.
     *
     * @return The scale.
     */

    public float getScale(){
        return scale;
    }

    /**
     * Sets the scale of the item.
     *
     * @param scale The new scale.
     */

    public void setScale(float scale){
        this.scale = scale;
    }

    /**
     * Gets the rotation vector.
     *
     * @return The rotation.
     */

    public Quaternionf getRotation(){
        return rotation;
    }

    /**
     * Sets the rotation.
     *
     * @param q The new rotation.
     */

    public final void setRotation(Quaternionf q){
        this.rotation.set(q);
    }

    /**
     * Getter for property 'mesh'.
     *
     * @return Value for property 'mesh'.
     */

    public Mesh getMesh(){
        return meshes[0];
    }

    /**
     * Getter for property 'meshes'.
     *
     * @return Value for property 'meshes'.
     */

    public Mesh[] getMeshes(){
        return meshes;
    }

    /**
     * Setter for property 'meshes'.
     *
     * @param meshes Value to set for property 'meshes'.
     */

    public void setMeshes(Mesh[] meshes){
        this.meshes = meshes;
    }

    /**
     * Setter for property 'mesh'.
     *
     * @param mesh Value to set for property 'mesh'.
     */

    public void setMesh(Mesh mesh){
        this.meshes = new Mesh[] {mesh};
    }

    /**
     * Cleans up the meshes.
     */

    public void cleanup(){
        int numMeshes = this.meshes != null ? this.meshes.length : 0;
        for(int i = 0; i < numMeshes; i++){
            this.meshes[i].cleanUp();
        }
    }

    public void setTextPos(int textPos){
        this.textPos = textPos;
    }

    public int getTextPos(){
        return textPos;
    }


}
