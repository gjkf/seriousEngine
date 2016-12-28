/*
 * Created by Davide Cossu (gjkf), 11/27/2016
 */

package me.gjkf.seriousEngine.loaders.md5;

import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Useful class for utils.
 */

public class MD5Utils{

    /**
     * RegEx for floats.
     */
    public static final String FLOAT_REGEXP = "[+-]?\\d*\\.?\\d*";
    /**
     * RegEx for vectors.
     */
    public static final String VECTOR3_REGEXP = "\\(\\s*(" + FLOAT_REGEXP + ")\\s*(" + FLOAT_REGEXP + ")\\s*(" + FLOAT_REGEXP + ")\\s*\\)";

    /**
     * Do not instantiate MD5Utils.
     */
    private MD5Utils(){
    }

    /**
     * Calculates a quaternion.
     *
     * @param vec The vector.
     *
     * @return The quaternion.
     */

    public static Quaternionf calculateQuaternion(Vector3f vec){
        return calculateQuaternion(vec.x, vec.y, vec.z);
    }

    /**
     * Calculates a quaternion.
     *
     * @param x The x coordinate.
     * @param y The y coordinate
     * @param z The z coordinate.
     *
     * @return The quaternion.
     */

    public static Quaternionf calculateQuaternion(float x, float y, float z){
        Quaternionf orientation = new Quaternionf(x, y, z, 0);
        float temp = 1.0f - (orientation.x * orientation.x) - (orientation.y * orientation.y) - (orientation.z * orientation.z);
        if(temp < 0.0f){
            orientation.w = 0.0f;
        }else{
            orientation.w = -(float) (Math.sqrt(temp));
        }
        return orientation;
    }

}