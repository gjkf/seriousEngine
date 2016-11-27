/*
 * Created by Davide Cossu (gjkf), 11/3/2016
 */
package com.gjkf.seriousEngine.render;

import org.joml.Vector3f;

/**
 * Object representing the camera of the viewer/player.
 * <p>Takes in account the position and rotation</p>
 */

public class Camera{

    /**
     * The vector representing the camera position.
     */
    private final Vector3f position;
    /**
     * The vector representing the camera rotation.
     */
    private final Vector3f rotation;

    /**
     * Camera constructor equivalent to <tt>new Camera(new Vector3f(0,0,0), new Vector3f(0,0,0));</tt>
     */

    public Camera(){
        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
    }

    /**
     * Camera constructor that accepts the position and the rotation.
     *
     * @param position The camera position.
     * @param rotation The camera rotation.
     */

    public Camera(Vector3f position, Vector3f rotation){
        this.position = position;
        this.rotation = rotation;
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
     * Sets the position of the camera.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z coordinate.
     */

    public void setPosition(float x, float y, float z){
        position.x = x;
        position.y = y;
        position.z = z;
    }

    /**
     * Moves the position of the camera of the given offsets.
     *
     * @param offsetX The offset in the X axis.
     * @param offsetY The offset in the Y axis.
     * @param offsetZ The offset in the Z axis.
     */

    public void movePosition(float offsetX, float offsetY, float offsetZ){
        if(offsetZ != 0){
            position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float) Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }
        if(offsetX != 0){
            position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        position.y += offsetY;
    }

    /**
     * Returns the {@link #rotation} vector.
     *
     * @return The rotation.
     */

    public Vector3f getRotation(){
        return rotation;
    }

    /**
     * Sets the rotation of the camera.
     *
     * @param x The rotation on the X axis.
     * @param y The rotation on the Y axis.
     * @param z The rotation on the Z axis.
     */

    public void setRotation(float x, float y, float z){
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    /**
     * Rotates the camera.
     *
     * @param offsetX The offset on the X axis.
     * @param offsetY The offset on the Y axis.
     * @param offsetZ The offset on the Z axis.
     */

    public void moveRotation(float offsetX, float offsetY, float offsetZ){
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
    }
}