/*
 * Created by Davide Cossu (gjkf), 11/2/2016
 */
package io.github.gjkf.seriousEngine.render;

import io.github.gjkf.seriousEngine.items.Item;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
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
        return projectionMatrix.setPerspective(fov, aspectRatio, zNear, zFar);
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
        return orthoProjMatrix.setOrtho(left, right, bottom, top, zNear, zFar);
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
        // First do the rotation so camera rotates over its position
        return matrix.rotationX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .translate(-position.x, -position.y, -position.z);
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
        return ortho2DMatrix.setOrtho2D(left, right, bottom, top);
    }

    /**
     * Builds a new model matrix multiplied by a view matrix.
     *
     * @param item The item.
     *
     * @return The new matrix.
     */

    public Matrix4f buildModelMatrix(Item item){
        Quaternionf rotation = item.getRotation();
        return modelMatrix.translationRotateScale(
                item.getPosition().x, item.getPosition().y, item.getPosition().z,
                rotation.x, rotation.y, rotation.z, rotation.w,
                item.getScale(), item.getScale(), item.getScale());
    }

    /**
     * Builds a model view matrix.
     *
     * @param item       The item.
     * @param viewMatrix The view matrix.
     *
     * @return The new matrix.
     */

    public Matrix4f buildModelViewMatrix(Item item, Matrix4f viewMatrix){
        return buildModelViewMatrix(buildModelMatrix(item), viewMatrix);
    }

    /**
     * Builds a model view matrix.
     *
     * @param modelMatrix The model matrix.
     * @param viewMatrix  The view matrix.
     *
     * @return The new matrix.
     */

    public Matrix4f buildModelViewMatrix(Matrix4f modelMatrix, Matrix4f viewMatrix){
        return viewMatrix.mulAffine(modelMatrix, modelViewMatrix);
    }

    /**
     * Builds a model light view matrix.
     *
     * @param item            The item.
     * @param lightViewMatrix The light view matrix.
     *
     * @return The new matrix.
     */

    public Matrix4f buildModelLightViewMatrix(Item item, Matrix4f lightViewMatrix){
        return buildModelViewMatrix(buildModelMatrix(item), lightViewMatrix);
    }

    /**
     * Builds a model light view matrix.
     *
     * @param modelMatrix     The model matrix.
     * @param lightViewMatrix The light view matrix.
     *
     * @return The new matrix.
     */

    public Matrix4f buildModelLightViewMatrix(Matrix4f modelMatrix, Matrix4f lightViewMatrix){
        return lightViewMatrix.mulAffine(modelMatrix, modelLightViewMatrix);
    }

    /**
     * Builds a orthographic projection model matrix.
     *
     * @param item        The item.
     * @param orthoMatrix The orthographic matrix.
     *
     * @return The new matrix.
     */

    public Matrix4f buildOrthoProjModelMatrix(Item item, Matrix4f orthoMatrix){
        return orthoMatrix.mulOrthoAffine(buildModelMatrix(item), orthoModelMatrix);
    }
}