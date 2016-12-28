/*
 * Created by Davide Cossu (gjkf), 11/27/2016
 */

package me.gjkf.seriousEngine.render.anim;

import org.joml.Matrix4f;

import java.util.Arrays;

/**
 * An animated frame.
 */

public class AnimatedFrame{

    /**
     * The max number of joints.
     */
    public static final int MAX_JOINTS = 150;
    /**
     * The identity matrix.
     */
    private static final Matrix4f IDENTITY_MATRIX = new Matrix4f();
    /**
     * The local joint matrices.
     */
    private final Matrix4f[] localJointMatrices;
    /**
     * The joint matrices.
     */
    private final Matrix4f[] jointMatrices;

    /** Constructs a new AnimatedFrame. */
    public AnimatedFrame() {
        localJointMatrices = new Matrix4f[MAX_JOINTS];
        Arrays.fill(localJointMatrices, IDENTITY_MATRIX);

        jointMatrices = new Matrix4f[MAX_JOINTS];
        Arrays.fill(jointMatrices, IDENTITY_MATRIX);
    }

    /**
     * Getter for property 'localJointMatrices'.
     *
     * @return Value for property 'localJointMatrices'.
     */

    public Matrix4f[] getLocalJointMatrices() {
        return localJointMatrices;
    }

    /**
     * Getter for property 'jointMatrices'.
     *
     * @return Value for property 'jointMatrices'.
     */

    public Matrix4f[] getJointMatrices() {
        return jointMatrices;
    }

    /**
     * Sets the matrices.
     *
     * @param pos The position.
     * @param localJointMatrix The local matrix.
     * @param invJointMatrix The joint matrix.
     */

    public void setMatrix(int pos, Matrix4f localJointMatrix, Matrix4f invJointMatrix) {
        localJointMatrices[pos] = localJointMatrix;
        Matrix4f mat = new Matrix4f(localJointMatrix);
        mat.mul(invJointMatrix);
        jointMatrices[pos] = mat;
    }

}