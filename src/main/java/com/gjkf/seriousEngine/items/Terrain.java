/*
 * Created by Davide Cossu (gjkf), 11/19/2016
 */

package com.gjkf.seriousEngine.items;

import com.gjkf.seriousEngine.render.HeightMapMesh;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Object that constructs items for each <i>block</i> in the terrain.
 */

public class Terrain{

    /**
     * The items.
     */
    private final Item[] items;
    /**
     * It will hold the bounding box for each terrain block
     */
    private final Rectangle2D.Float[][] boundingBoxes;
    /**
     * The size of the terrain.
     */
    private final int terrainSize;
    /**
     * The amount of vertices per column.
     */
    private final int verticesPerCol;
    /**
     * The amount of vertices per row.
     */
    private final int verticesPerRow;
    /**
     * The height map mesh.
     */
    private final HeightMapMesh heightMapMesh;

    /**
     * Constructs the items starting from the image given.
     *
     * @param terrainSize   The size of the terrain. The size of {@link #items} is determined by <tt>blocksPerRow ^
     *                      2</tt>.
     * @param scale         The texture scale.
     * @param minY          The minimum value the Y coordinate of the terrain can get.
     * @param maxY          The maximum value the Y coordinate of the terrain can get.
     * @param heightMapFile The file from which read the height map.
     * @param textureFile   The file from which read the texture.
     * @param textInc       The amount of texture between vertices.
     *
     * @throws Exception If anything went wrong.
     */

    public Terrain(int terrainSize, float scale, float minY, float maxY, String heightMapFile, String textureFile, int textInc) throws Exception{
        this.terrainSize = terrainSize;
        items = new Item[terrainSize * terrainSize];

        BufferedImage heightMapImage = ImageIO.read(getClass().getResourceAsStream(heightMapFile));
        // The number of vertices per column and row
        verticesPerCol = heightMapImage.getWidth() - 1;
        verticesPerRow = heightMapImage.getHeight() - 1;

        heightMapMesh = new HeightMapMesh(minY, maxY, heightMapImage, textureFile, textInc);
        boundingBoxes = new Rectangle2D.Float[terrainSize][terrainSize];

        for(int row = 0; row < terrainSize; row++){
            for(int col = 0; col < terrainSize; col++){
                float xDisplacement = (col - ((float) terrainSize - 1) / (float) 2) * scale * HeightMapMesh.getXLength();
                float zDisplacement = (row - ((float) terrainSize - 1) / (float) 2) * scale * HeightMapMesh.getZLength();

                Item terrainBlock = new Item(heightMapMesh.getMesh());
                terrainBlock.setScale(scale);
                terrainBlock.setPosition(xDisplacement, 0, zDisplacement);
                items[row * terrainSize + col] = terrainBlock;

                boundingBoxes[row][col] = getBoundingBox(terrainBlock);
            }
        }
    }

    /**
     * Gets the height of the given position.
     *
     * @param position The position to check.
     *
     * @return The height.
     */

    public float getHeight(Vector3f position){
        float result = Float.MIN_VALUE;
        // For each terrain block we get the bounding box, translate it to view coodinates
        // and check if the position is contained in that bounding box
        Rectangle2D.Float boundingBox = null;
        boolean found = false;
        Item terrainBlock = null;
        for(int row = 0; row < terrainSize && !found; row++){
            for(int col = 0; col < terrainSize && !found; col++){
                terrainBlock = items[row * terrainSize + col];
                boundingBox = boundingBoxes[row][col];
                found = boundingBox.contains(position.x, position.z);
            }
        }

        // If we have found a terrain block that contains the position we need
        // to calculate the height of the terrain on that position
        if(found){
            Vector3f[] triangle = getTriangle(position, boundingBox, terrainBlock);
            result = interpolateHeight(triangle[0], triangle[1], triangle[2], position.x, position.z);
        }

        return result;
    }

    /**
     * Gets which triangle of the terrain block the position is in.
     *
     * @param position     The position to check.
     * @param boundingBox  The bounding box of the terrain block.
     * @param terrainBlock The terrain block.
     *
     * @return An array containing all the vertices of the triangle.
     */

    protected Vector3f[] getTriangle(Vector3f position, Rectangle2D.Float boundingBox, Item terrainBlock){
        // Get the column and row of the heightmap associated to the current position
        float cellWidth = boundingBox.width / (float) verticesPerCol;
        float cellHeight = boundingBox.height / (float) verticesPerRow;
        int col = (int) ((position.x - boundingBox.x) / cellWidth);
        int row = (int) ((position.z - boundingBox.y) / cellHeight);

        Vector3f[] triangle = new Vector3f[3];
        triangle[1] = new Vector3f(
                boundingBox.x + col * cellWidth,
                getWorldHeight(row + 1, col, terrainBlock),
                boundingBox.y + (row + 1) * cellHeight);
        triangle[2] = new Vector3f(
                boundingBox.x + (col + 1) * cellWidth,
                getWorldHeight(row, col + 1, terrainBlock),
                boundingBox.y + row * cellHeight);
        if(position.z < getDiagonalZCoord(triangle[1].x, triangle[1].z, triangle[2].x, triangle[2].z, position.x)){
            triangle[0] = new Vector3f(
                    boundingBox.x + col * cellWidth,
                    getWorldHeight(row, col, terrainBlock),
                    boundingBox.y + row * cellHeight);
        }else{
            triangle[0] = new Vector3f(
                    boundingBox.x + (col + 1) * cellWidth,
                    getWorldHeight(row + 2, col + 1, terrainBlock),
                    boundingBox.y + (row + 1) * cellHeight);
        }

        return triangle;
    }

    /**
     * Gets the coordinate on the z axis of any point. Used to determine the triangle.
     *
     * @param x1 The first x coordinate.
     * @param z1 The first z coordinate.
     * @param x2 The second x coordinate.
     * @param z2 The second z coordinate.
     * @param x  The position on the x axis.
     *
     * @return The z coordinate.
     */

    protected float getDiagonalZCoord(float x1, float z1, float x2, float z2, float x){
        float z = ((z1 - z2) / (x1 - x2)) * (x - x1) + z1;
        return z;
    }

    /**
     * Gets the scaled height of the given row and column.
     *
     * @param row  The row.
     * @param col  The column.
     * @param item The item
     *
     * @return The height.
     */

    protected float getWorldHeight(int row, int col, Item item){
        float y = heightMapMesh.getHeight(row, col);
        return y * item.getScale() + item.getPosition().y;
    }

    /**
     * Interpolates the heights of the points to get an height that is not on a vertex.
     *
     * @param pA The first vector.
     * @param pB The second vector.
     * @param pC The third vector.
     * @param x  The x coordinate.
     * @param z  The z coordinate.
     *
     * @return The interpolated height.
     */

    protected float interpolateHeight(Vector3f pA, Vector3f pB, Vector3f pC, float x, float z){
        // Plane equation ax+by+cz+d=0
        float a = (pB.y - pA.y) * (pC.z - pA.z) - (pC.y - pA.y) * (pB.z - pA.z);
        float b = (pB.z - pA.z) * (pC.x - pA.x) - (pC.z - pA.z) * (pB.x - pA.x);
        float c = (pB.x - pA.x) * (pC.y - pA.y) - (pC.x - pA.x) * (pB.y - pA.y);
        float d = -(a * pA.x + b * pA.y + c * pA.z);
        // y = (-d -ax -cz) / b
        float y = (-d - a * x - c * z) / b;
        return y;
    }

    /**
     * Gets the bounding box of a terrain block
     *
     * @param terrainBlock An Item instance that defines the terrain block
     *
     * @return The bounding box of the terrain block
     */

    private Rectangle2D.Float getBoundingBox(Item terrainBlock){
        float scale = terrainBlock.getScale();
        Vector3f position = terrainBlock.getPosition();

        float topLeftX = HeightMapMesh.STARTX * scale + position.x;
        float topLeftZ = HeightMapMesh.STARTZ * scale + position.z;
        float width = Math.abs(HeightMapMesh.STARTX * 2) * scale;
        float height = Math.abs(HeightMapMesh.STARTZ * 2) * scale;
        Rectangle2D.Float boundingBox = new Rectangle2D.Float(topLeftX, topLeftZ, width, height);
        return boundingBox;
    }


    /**
     * Getter for property 'items'.
     *
     * @return Value for property 'items'.
     */

    public Item[] getItems(){
        return items;
    }


}