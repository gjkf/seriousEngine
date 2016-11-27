/*
 * Created by Davide Cossu (gjkf), 11/19/2016
 */

package com.gjkf.seriousEngine.render;

import com.gjkf.seriousEngine.Utils;
import org.joml.Vector3f;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a mesh for a HeightMap.
 * <p>It provides useful methods to easily create one from an image.</p>
 */

public class HeightMapMesh{

    /**
     * The maximum value of a pixel's colour.
     * <p>It could have been <tt>255</tt> but using <tt>255^3<tt/> gives us more flexibility.   </p>
     */
    private static final int MAX_COLOUR = 255 * 255 * 255;
    /**
     * The start of the X coordinate on the image.
     */
    public static final float STARTX = -0.5f;
    /**
     * The start on the Y coordinate on the image.
     */
    public static final float STARTZ = -0.5f;
    /**
     * The minimum Y value of the vertex.
     */
    private final float minY;
    /**
     * The maximum Y value of the vertex.
     */
    private final float maxY;
    /**
     * The array containing each point's height.
     */
    private final float[][] heightArray;
    /**
     * The created mesh.
     */
    private final Mesh mesh;

    /**
     * Constructs a new object reading an image and creating a mesh from it.
     *
     * @param minY           The minimum value the Y coordinate of the terrain can get.
     * @param maxY           The maximum value the Y coordinate of the terrain can get.
     * @param heightMapImage The file from which read the height map.
     * @param textureFile    The file from which read the texture.
     * @param textInc        The amount of texture between vertices.
     *
     * @throws Exception If anything went wrong.
     */

    public HeightMapMesh(float minY, float maxY, BufferedImage heightMapImage, String textureFile, int textInc) throws Exception{
        this.minY = minY;
        this.maxY = maxY;

        int height = heightMapImage.getHeight();
        int width = heightMapImage.getWidth();

        heightArray = new float[height][width];

        Texture texture = new Texture(textureFile);

        float incx = getXLength() / (width - 1);
        float incz = getZLength() / (height - 1);

        List<Float> positions = new ArrayList<>();
        List<Float> textCoords = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        for(int row = 0; row < height; row++){
            for(int col = 0; col < width; col++){
                // Create vertex for current position
                positions.add(STARTX + col * incx); // x
                float currentHeight = getHeight(col, row, heightMapImage);
                heightArray[row][col] = currentHeight;
                positions.add(currentHeight); // y
                positions.add(STARTZ + row * incz); // z

                // Set texture coordinates
                textCoords.add((float) textInc * (float) col / (float) width);
                textCoords.add((float) textInc * (float) row / (float) height);

                // Create indices
                if(col < width - 1 && row < height - 1){
                    int leftTop = row * width + col;
                    int leftBottom = (row + 1) * width + col;
                    int rightBottom = (row + 1) * width + col + 1;
                    int rightTop = row * width + col + 1;

                    indices.add(leftTop);
                    indices.add(leftBottom);
                    indices.add(rightTop);

                    indices.add(rightTop);
                    indices.add(leftBottom);
                    indices.add(rightBottom);
                }
            }
        }
        float[] posArr = Utils.listToArray(positions);
        int[] indicesArr = indices.stream().mapToInt(i -> i).toArray();
        float[] textCoordsArr = Utils.listToArray(textCoords);
        float[] normalsArr = calcNormals(posArr, width, height);
        this.mesh = new Mesh(posArr, textCoordsArr, normalsArr, indicesArr);
        Material material = new Material(texture, 0.0f);
        mesh.setMaterial(material);
    }

    /**
     * Gets the height of the given point.
     *
     * @param row The row.
     * @param col The column.
     *
     * @return The height.
     */

    public float getHeight(int row, int col){
        float result = 0;
        if(row >= 0 && row < heightArray.length){
            if(col >= 0 && col < heightArray[row].length){
                result = heightArray[row][col];
            }
        }
        return result;
    }

    /**
     * Getter for property 'mesh'.
     *
     * @return Value for property 'mesh'.
     */

    public Mesh getMesh(){
        return mesh;
    }

    /**
     * Getter for property 'XLength'.
     *
     * @return Value for property 'XLength'.
     */

    public static float getXLength(){
        return Math.abs(-STARTX * 2);
    }

    /**
     * Getter for property 'ZLength'.
     *
     * @return Value for property 'ZLength'.
     */

    public static float getZLength(){
        return Math.abs(-STARTZ * 2);
    }

    /**
     * Calculate the normals for the array.
     *
     * @param posArr The array.
     * @param width  The width of the rectangle.
     * @param height The height of the rectangle.
     *
     * @return The array of normals.
     */

    private float[] calcNormals(float[] posArr, int width, int height){
        Vector3f v0 = new Vector3f();
        Vector3f v1 = new Vector3f();
        Vector3f v2 = new Vector3f();
        Vector3f v3 = new Vector3f();
        Vector3f v4 = new Vector3f();
        Vector3f v12 = new Vector3f();
        Vector3f v23 = new Vector3f();
        Vector3f v34 = new Vector3f();
        Vector3f v41 = new Vector3f();
        List<Float> normals = new ArrayList<>();
        Vector3f normal = new Vector3f();
        for(int row = 0; row < height; row++){
            for(int col = 0; col < width; col++){
                if(row > 0 && row < height - 1 && col > 0 && col < width - 1){
                    int i0 = row * width * 3 + col * 3;
                    v0.x = posArr[i0];
                    v0.y = posArr[i0 + 1];
                    v0.z = posArr[i0 + 2];

                    int i1 = row * width * 3 + (col - 1) * 3;
                    v1.x = posArr[i1];
                    v1.y = posArr[i1 + 1];
                    v1.z = posArr[i1 + 2];
                    v1 = v1.sub(v0);

                    int i2 = (row + 1) * width * 3 + col * 3;
                    v2.x = posArr[i2];
                    v2.y = posArr[i2 + 1];
                    v2.z = posArr[i2 + 2];
                    v2 = v2.sub(v0);

                    int i3 = (row) * width * 3 + (col + 1) * 3;
                    v3.x = posArr[i3];
                    v3.y = posArr[i3 + 1];
                    v3.z = posArr[i3 + 2];
                    v3 = v3.sub(v0);

                    int i4 = (row - 1) * width * 3 + col * 3;
                    v4.x = posArr[i4];
                    v4.y = posArr[i4 + 1];
                    v4.z = posArr[i4 + 2];
                    v4 = v4.sub(v0);

                    v1.cross(v2, v12);
                    v12.normalize();

                    v2.cross(v3, v23);
                    v23.normalize();

                    v3.cross(v4, v34);
                    v34.normalize();

                    v4.cross(v1, v41);
                    v41.normalize();

                    normal = v12.add(v23).add(v34).add(v41);
                    normal.normalize();
                }else{
                    normal.x = 0;
                    normal.y = 1;
                    normal.z = 0;
                }
                normal.normalize();
                normals.add(normal.x);
                normals.add(normal.y);
                normals.add(normal.z);
            }
        }
        return Utils.listToArray(normals);
    }

    /**
     * Returns the height, read from the image, of the given coordinate.
     *
     * @param x         The X coordinate.
     * @param z         The Y coordinate.
     * @param buffImage The image.
     *
     * @return The height.
     */

    private float getHeight(int x, int z, BufferedImage buffImage){
        float result = 0;
        if(x >= 0 && x < buffImage.getWidth() && z >= 0 && z < buffImage.getHeight()){
            int rgb = buffImage.getRGB(x, z);
            result = this.minY + Math.abs(this.maxY - this.minY) * ((float) rgb / (float) MAX_COLOUR);
        }
        return result;
    }

}