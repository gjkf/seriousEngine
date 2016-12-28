/*
 * Created by Davide Cossu (gjkf), 12/4/2016
 */

package me.gjkf.seriousEngine.render;

import me.gjkf.seriousEngine.items.Item;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

/**
 * A more efficient {@link Mesh} that does not support animations.
 * <p>Takes advantage of the OpenGL <tt>glDraw*Instanced</tt> methods to have better performance.</p>
 */

public class InstancedMesh extends Mesh{

    /**
     * The size of a float in bytes.
     */
    private static final int FLOAT_SIZE_BYTES = 4;
    /**
     * The size of a {@link org.joml.Vector4f} in bytes.
     */
    private static final int VECTOR4F_SIZE_BYTES = 4 * FLOAT_SIZE_BYTES;
    /**
     * The matrix's size.
     */
    private static final int MATRIX_SIZE_FLOATS = 4 * 4;
    /**
     * The matrix's size in bytes.
     */
    private static final int MATRIX_SIZE_BYTES = MATRIX_SIZE_FLOATS * FLOAT_SIZE_BYTES;
    /**
     * The size of an instance (bytes) in bytes.
     */
    private static final int INSTANCE_SIZE_BYTES = MATRIX_SIZE_BYTES * 2 + FLOAT_SIZE_BYTES * 2;
    /**
     * The size of an instance (floats) in bytes.
     */
    private static final int INSTANCE_SIZE_FLOATS = MATRIX_SIZE_FLOATS * 2 + 2;
    /**
     * The number of instances.
     */
    private final int numInstances;
    /**
     * The instance data of the VBO.
     */
    private final int instanceDataVBO;
    /**
     * The data buffer.
     */
    private final FloatBuffer instanceDataBuffer;

    /**
     * Creates a new InstancedMesh
     *
     * @param positions    The position's array.
     * @param textCoords   The texture coordinate's array.
     * @param normals      The normal's array.
     * @param indices      The indice's array.
     * @param numInstances The number of instances
     */

    public InstancedMesh(float[] positions, float[] textCoords, float[] normals, int[] indices, int numInstances){
        super(positions, textCoords, normals, indices, createEmptyIntArray(MAX_WEIGHTS * positions.length / 3, 0), createEmptyFloatArray(MAX_WEIGHTS * positions.length / 3, 0));

        this.numInstances = numInstances;

        glBindVertexArray(vaoId);

        // Model View Matrix
        instanceDataVBO = glGenBuffers();
        vboIdList.add(instanceDataVBO);
        this.instanceDataBuffer = BufferUtils.createFloatBuffer(numInstances * INSTANCE_SIZE_FLOATS);
        glBindBuffer(GL_ARRAY_BUFFER, instanceDataVBO);
        int start = 5;
        int strideStart = 0;
        for(int i = 0; i < 4; i++){
            glVertexAttribPointer(start, 4, GL_FLOAT, false, INSTANCE_SIZE_BYTES, strideStart);
            glVertexAttribDivisor(start, 1);
            start++;
            strideStart += VECTOR4F_SIZE_BYTES;
        }

        // Light view matrix
        for(int i = 0; i < 4; i++){
            glVertexAttribPointer(start, 4, GL_FLOAT, false, INSTANCE_SIZE_BYTES, strideStart);
            glVertexAttribDivisor(start, 1);
            start++;
            strideStart += VECTOR4F_SIZE_BYTES;
        }

        // Texture offsets
        glVertexAttribPointer(start, 2, GL_FLOAT, false, INSTANCE_SIZE_BYTES, strideStart);
        glVertexAttribDivisor(start, 1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    /**
     * {@inheritDoc}
     */

    @Override
    protected void initRender(){
        super.initRender();

        int start = 5;
        int numElements = 4 * 2 + 1;
        for(int i = 0; i < numElements; i++){
            glEnableVertexAttribArray(start + i);
        }
    }

    /**
     * {@inheritDoc}
     */

    @Override
    protected void endRender(){
        int start = 5;
        int numElements = 4 * 2 + 1;
        for(int i = 0; i < numElements; i++){
            glDisableVertexAttribArray(start + i);
        }

        super.endRender();
    }

    /**
     * Renders a list of instanced items.
     *
     * @param items           The items.
     * @param transformation  The transformation.
     * @param viewMatrix      The view matrix.
     * @param lightViewMatrix The light view matrix.
     */

    public void renderListInstanced(List<Item> items, Transformation transformation, Matrix4f viewMatrix, Matrix4f lightViewMatrix){
        renderListInstanced(items, false, transformation, viewMatrix, lightViewMatrix);
    }

    /**
     * Renders a list of instanced items.
     *
     * @param items           The items.
     * @param billBoard       If the items are particles.
     * @param transformation  The transformation.
     * @param viewMatrix      The view matrix.
     * @param lightViewMatrix The light view matrix.
     */

    public void renderListInstanced(List<Item> items, boolean billBoard, Transformation transformation, Matrix4f viewMatrix, Matrix4f lightViewMatrix){
        initRender();

        int chunkSize = numInstances;
        int length = items.size();
        for(int i = 0; i < length; i += chunkSize){
            int end = Math.min(length, i + chunkSize);
            List<Item> subList = items.subList(i, end);
            renderChunkInstanced(subList, billBoard, transformation, viewMatrix, lightViewMatrix);
        }

        endRender();
    }

    /**
     * Renders a chunk of items.
     *
     * @param items           The items.
     * @param billBoard       If the items are particles.
     * @param transformation  The transformation.
     * @param viewMatrix      The view matrix.
     * @param lightViewMatrix The light view matrix.
     */

    private void renderChunkInstanced(List<Item> items, boolean billBoard, Transformation transformation, Matrix4f viewMatrix, Matrix4f lightViewMatrix){
        this.instanceDataBuffer.clear();

        int i = 0;

        Texture text = getMaterial().getTexture();
        for(Item gameItem : items){
            Matrix4f modelMatrix = transformation.buildModelMatrix(gameItem);
            if(viewMatrix != null){
                if(billBoard){
                    viewMatrix.transpose3x3(modelMatrix);
                }
                Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(modelMatrix, viewMatrix);
                modelViewMatrix.get(INSTANCE_SIZE_FLOATS * i, instanceDataBuffer);
            }
            if(lightViewMatrix != null){
                Matrix4f modelLightViewMatrix = transformation.buildModelLightViewMatrix(modelMatrix, lightViewMatrix);
                modelLightViewMatrix.get(INSTANCE_SIZE_FLOATS * i + MATRIX_SIZE_FLOATS, this.instanceDataBuffer);
            }
            if(text != null){
                int col = gameItem.getTextPos() % text.getNumCols();
                int row = gameItem.getTextPos() / text.getNumCols();
                float textXOffset = (float) col / text.getNumCols();
                float textYOffset = (float) row / text.getNumRows();
                int buffPos = INSTANCE_SIZE_FLOATS * i + MATRIX_SIZE_FLOATS * 2;
                this.instanceDataBuffer.put(buffPos, textXOffset);
                this.instanceDataBuffer.put(buffPos + 1, textYOffset);
            }

            i++;
        }

        glBindBuffer(GL_ARRAY_BUFFER, instanceDataVBO);
        glBufferData(GL_ARRAY_BUFFER, instanceDataBuffer, GL_DYNAMIC_DRAW);

        glDrawElementsInstanced(
                GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0, items.size());

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

}