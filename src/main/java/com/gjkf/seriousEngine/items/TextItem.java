/*
 * Created by Davide Cossu (gjkf), 11/13/2016
 */

package com.gjkf.seriousEngine.items;

import com.gjkf.seriousEngine.Utils;
import com.gjkf.seriousEngine.render.FontTexture;
import com.gjkf.seriousEngine.render.Material;
import com.gjkf.seriousEngine.render.Mesh;

import java.util.ArrayList;
import java.util.List;

/**
 * Extends {@link Item}. Represents a piece of text.
 */

public class TextItem extends Item{

    /**
     * The Z position in the buffer.
     */
    private static final float ZPOS = 0.0f;
    /**
     * The number of vertices per quad.
     */
    private static final int VERTICES_PER_QUAD = 4;
    /**
     * The text.
     */
    private String text;
    /**
     * The font texture.
     */
    private final FontTexture fontTexture;

    /**
     * Constructs a new object.
     *
     * @param text        The text.
     * @param fontTexture The font texture.
     *
     * @throws Exception If anything went wrong.
     */

    public TextItem(String text, FontTexture fontTexture) throws Exception{
        super();
        this.text = text;
        this.fontTexture = fontTexture;
        setMesh(buildMesh());
    }

    /**
     * Builds a mesh from the texture.
     *
     * @return The newly created mesh.
     */

    private Mesh buildMesh(){
        List<Float> positions = new ArrayList<>();
        List<Float> textCoords = new ArrayList<>();
        float[] normals = new float[0];
        List<Integer> indices = new ArrayList<>();
        char[] characters = text.toCharArray();
        int numChars = characters.length;

        float startx = 0;
        for(int i = 0; i < numChars; i++){
            FontTexture.CharInfo charInfo = fontTexture.getCharInfo(characters[i]);

            // Build a character tile composed by two triangles

            // Left Top vertex
            positions.add(startx); // x
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textCoords.add((float) charInfo.getStartX() / (float) fontTexture.getWidth());
            textCoords.add(0.0f);
            indices.add(i * VERTICES_PER_QUAD);

            // Left Bottom vertex
            positions.add(startx); // x
            positions.add((float) fontTexture.getHeight()); //y
            positions.add(ZPOS); //z
            textCoords.add((float) charInfo.getStartX() / (float) fontTexture.getWidth());
            textCoords.add(1.0f);
            indices.add(i * VERTICES_PER_QUAD + 1);

            // Right Bottom vertex
            positions.add(startx + charInfo.getWidth()); // x
            positions.add((float) fontTexture.getHeight()); //y
            positions.add(ZPOS); //z
            textCoords.add((float) (charInfo.getStartX() + charInfo.getWidth()) / (float) fontTexture.getWidth());
            textCoords.add(1.0f);
            indices.add(i * VERTICES_PER_QUAD + 2);

            // Right Top vertex
            positions.add(startx + charInfo.getWidth()); // x
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textCoords.add((float) (charInfo.getStartX() + charInfo.getWidth()) / (float) fontTexture.getWidth());
            textCoords.add(0.0f);
            indices.add(i * VERTICES_PER_QUAD + 3);

            // Add indices por left top and bottom right vertices
            indices.add(i * VERTICES_PER_QUAD);
            indices.add(i * VERTICES_PER_QUAD + 2);

            startx += charInfo.getWidth();
        }

        float[] posArr = Utils.listToArray(positions);
        float[] textCoordsArr = Utils.listToArray(textCoords);
        int[] indicesArr = indices.stream().mapToInt(i -> i).toArray();
        Mesh mesh = new Mesh(posArr, textCoordsArr, normals, indicesArr);
        mesh.setMaterial(new Material(fontTexture.getTexture()));
        return mesh;
    }

    /**
     * Getter for property 'text'.
     *
     * @return Value for property 'text'.
     */

    public String getText(){
        return text;
    }

    /**
     * Setter for property 'text'.
     *
     * @param text Value to set for property 'text'.
     */

    public void setText(String text){
        this.text = text;
        this.getMesh().deleteBuffers();
        this.setMesh(buildMesh());
    }
}