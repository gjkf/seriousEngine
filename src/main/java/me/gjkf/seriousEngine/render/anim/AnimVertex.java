/*
 * Created by Davide Cossu (gjkf), 11/27/2016
 */

package me.gjkf.seriousEngine.render.anim;

import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * A POJO.
 */

public class AnimVertex {

    /**
     * The positions.
     */
    public Vector3f position;
    /**
     * The texture coordinates.
     */
    public Vector2f textCoords;
    /**
     * The normal.
     */
    public Vector3f normal;
    /**
     * The weights.
     */
    public float[] weights;
    /**
     * The joint indices.
     */
    public int[] jointIndices;

    /** Constructs a new AnimVertex. */
    public AnimVertex() {
        super();
        normal = new Vector3f(0, 0, 0);
    }
}