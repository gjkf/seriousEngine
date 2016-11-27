/*
 * Created by Davide Cossu (gjkf), 11/2/2016
 */
package com.gjkf.seriousEngine.render;

import com.gjkf.seriousEngine.items.Item;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Utility class that provides methods to create matrices used in the rendering process.
 */

public class Transformation{

    /**
     * The projection matrix.
     */
    private final Matrix4f projectionMatrix;
    /**
     * The model matrix.
     */
    private final Matrix4f modelMatrix;
    /**
     * The model view matrix.
     */
    private final Matrix4f modelViewMatrix;
    /**
     * The model light matrix.
     */
    private final Matrix4f modelLightMatrix;
    /**
     * The model light view matrix.
     */
    private final Matrix4f modelLightViewMatrix;
    /**
     * The view matrix.
     */
    private final Matrix4f viewMatrix;
    /**
     * The light view matrix.
     */
    private final Matrix4f lightViewMatrix;
    /**
     * The ortographic projection matrix.
     */
    private final Matrix4f orthoProjMatrix;
    /**
     * The orto matrix.
     */
    private final Matrix4f ortho2DMatrix;

    /**
     * The orthomodel matrix.
     */
    private final Matrix4f orthoModelMatrix;


    /**
     * Creates the new empty matrices.
     */

    public Transformation(){
        projectionMatrix = new Matrix4f();
        modelMatrix = new Matrix4f();
        modelViewMatrix = new Matrix4f();
        modelLightMatrix = new Matrix4f();
        modelLightViewMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        orthoProjMatrix = new Matrix4f();
        ortho2DMatrix = new Matrix4f();
        orthoModelMatrix = new Matrix4f();
        lightViewMatrix = new Matrix4f();
    }

    /**
     * Getter for property 'projectionMatrix'.
     *
     * @return Value for property 'projectionMatrix'.
     */

    public Matrix4f getProjectionMatrix(){
        return projectionMatrix;
    }

    /**
     * Updates the {@link #projectionMatrix}.
     *
     * @param fov    The Field Of View in radians.
     * @param width  The width of the frustum.
     * @param height The height of the frustum.
     * @param zNear  The near coordinate of the frustum.
     * @param zFar   The far coordinate of the frustum.
     *
     * @return The updated matrix.
     */

    public Matrix4f updateProjectionMatrix(float fov, float width, float height, float zNear, float zFar){
        float aspectRatio = width / height;
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }

    /**
     * Getter for property 'orthoProjectionMatrix'.
     *
     * @return Value for property 'orthoProjectionMatrix'.
     */

    public final Matrix4f getOrthoProjectionMatrix(){
        return orthoProjMatrix;
    }

    /**
     * Updates the orthographic projection matrix.
     *
     * @param left   The left coordinate.
     * @param right  The right coordinate.
     * @param bottom The bottom coordinate.
     * @param top    The top coordinate.
     * @param zNear  The near Z coordinate.
     * @param zFar   The far Z coordinate.
     *
     * @return The updated matrix.
     */

    public Matrix4f updateOrthoProjectionMatrix(float left, float right, float bottom, float top, float zNear, float zFar){
        orthoProjMatrix.identity();
        orthoProjMatrix.setOrtho(left, right, bottom, top, zNear, zFar);
        return orthoProjMatrix;
    }

    /**
     * Getter for property 'viewMatrix'.
     *
     * @return Value for property 'viewMatrix'.
     */

    public Matrix4f getViewMatrix(){
        return viewMatrix;
    }

    /**
     * Updates the {@link #viewMatrix}.
     *
     * @param camera The camera.
     *
     * @return The updated matrix.
     */

    public Matrix4f updateViewMatrix(Camera camera){
        return updateGenericViewMatrix(camera.getPosition(), camera.getRotation(), viewMatrix);
    }

    /**
     * Updates the generic view matrix.
     *
     * @param position The position.
     * @param rotation The rotation.
     * @param matrix   The matrix.
     *
     * @return The generic view matrix.
     */

    private Matrix4f updateGenericViewMatrix(Vector3f position, Vector3f rotation, Matrix4f matrix){
        matrix.identity();
        // First do the rotation so camera rotates over its position
        matrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        // Then do the translation
        matrix.translate(-position.x, -position.y, -position.z);
        return matrix;
    }

    /**
     * Getter for property 'lightViewMatrix'.
     *
     * @return Value for property 'lightViewMatrix'.
     */

    public Matrix4f getLightViewMatrix(){
        return lightViewMatrix;
    }

    /**
     * Setter for property 'lightViewMatrix'.
     *
     * @param lightViewMatrix Value to set for property 'lightViewMatrix'.
     */

    public void setLightViewMatrix(Matrix4f lightViewMatrix){
        this.lightViewMatrix.set(lightViewMatrix);
    }

    /**
     * Update the light view matrix.
     *
     * @param position The position.
     * @param rotation The rotation.
     *
     * @return The updated matrix.
     */

    public Matrix4f updateLightViewMatrix(Vector3f position, Vector3f rotation){
        return updateGenericViewMatrix(position, rotation, lightViewMatrix);
    }

    /**
     * Returns a orthographic matrix.
     *
     * @param left   The left coordinate.
     * @param right  The right coordinate.
     * @param bottom The bottom coordinate.
     * @param top    The top coordinate.
     *
     * @return The new matrix.
     */

    public final Matrix4f getOrtho2DProjectionMatrix(float left, float right, float bottom, float top){
        ortho2DMatrix.identity();
        ortho2DMatrix.setOrtho2D(left, right, bottom, top);
        return ortho2DMatrix;
    }

    /**
     * Builds a new model matrix multiplied by a view matrix.
     *
     * @param item       The item.
     * @param viewMatrix The view matrix.
     *
     * @return The new matrix.
     */

    public Matrix4f buildModelViewMatrix(Item item, Matrix4f viewMatrix){
        Vector3f rotation = item.getRotation();
        modelMatrix.identity().translate(item.getPosition()).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(item.getScale());
        modelViewMatrix.set(viewMatrix);
        return modelViewMatrix.mul(modelMatrix);
    }

    /**
     * Builds the model light view matrix.
     *
     * @param item   The item.
     * @param matrix The matrix.
     *
     * @return The newly built matrix.
     */

    public Matrix4f buildModelLightViewMatrix(Item item, Matrix4f matrix){
        Vector3f rotation = item.getRotation();
        modelLightMatrix.identity().translate(item.getPosition()).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(item.getScale());
        modelLightViewMatrix.set(matrix);
        return modelLightViewMatrix.mul(modelLightMatrix);
    }

    /**
     * Builds a orthographic matrix multiplied by the model matrix.
     *
     * @param item        The item.
     * @param orthoMatrix The orthographic matrix.
     *
     * @return The new matrix.
     */

    public Matrix4f buildOrtoProjModelMatrix(Item item, Matrix4f orthoMatrix){
        Vector3f rotation = item.getRotation();
        modelMatrix.identity().translate(item.getPosition()).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(item.getScale());
        orthoModelMatrix.set(orthoMatrix);
        orthoModelMatrix.mul(modelMatrix);
        return orthoModelMatrix;
    }

}