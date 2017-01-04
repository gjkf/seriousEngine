/*
 * Created by Davide Cossu (gjkf), 11/2/2016
 */
package io.github.gjkf.seriousEngine.items;

import io.github.gjkf.seriousEngine.render.Mesh;
import org.joml.Quaternionf;
import org.joml.Vector2f;
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

    /**
     * Setter for property 'textPos'.
     *
     * @param textPos Value to set for property 'textPos'.
     */

    public void setTextPos(int textPos){
        this.textPos = textPos;
    }

    /**
     * Getter for property 'textPos'.
     *
     * @return Value for property 'textPos'.
     */

    public int getTextPos(){
        return textPos;
    }

    /**
     * Checks whether the given item collides with this one.
     *
     * @param item The item to check.
     *
     * @return TRUE if there's a collision, FALSE otherwise.
     */

    public boolean checkCollisionWith(Item item){
        if(item.equals(this)){
            return true;
        }
        boolean collidedX = false, collidedY = false, collidedZ = false;

        Vector3f thisPosition = new Vector3f(
                getPosition().x * getScale(),
                getPosition().y * getScale(),
                getPosition().z * getScale()
        );

        Vector3f itemPosition = new Vector3f(
                item.getPosition().x * item.getScale(),
                item.getPosition().y * item.getScale(),
                item.getPosition().z * item.getScale()
        );

        Vector2f thisXBounds = new Vector2f(thisPosition.x - getScale(), thisPosition.x + getScale());
        Vector2f thisYBounds = new Vector2f(thisPosition.y - getScale(), thisPosition.y + getScale());
        Vector2f thisZBounds = new Vector2f(thisPosition.z - getScale(), thisPosition.z + getScale());

        Vector2f itemXBounds = new Vector2f(itemPosition.x - item.getScale(), itemPosition.x + item.getScale());
        Vector2f itemYBounds = new Vector2f(itemPosition.y - item.getScale(), itemPosition.y + item.getScale());
        Vector2f itemZBounds = new Vector2f(itemPosition.z - item.getScale(), itemPosition.z + item.getScale());

        if(itemPosition.x < thisPosition.x){
            if(itemXBounds.y >= thisXBounds.x){
                collidedX = true;
            }
        }else{
            if(itemXBounds.x <= thisXBounds.y){
                collidedX = true;
            }
        }

        if(itemPosition.y < thisPosition.y){
            if(itemYBounds.y >= thisYBounds.x){
                collidedY = true;
            }
        }else{
            if(itemYBounds.x <= thisYBounds.y){
                collidedY = true;
            }
        }

        if(itemPosition.z < thisPosition.z){
            if(itemZBounds.y >= thisZBounds.x){
                collidedZ = true;
            }
        }else{
            if(itemZBounds.x <= thisZBounds.y){
                collidedZ = true;
            }
        }

        return collidedX && collidedY && collidedZ;
    }
}
