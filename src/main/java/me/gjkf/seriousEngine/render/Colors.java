/*
  Created by Davide Cossu (gjkf), 7/9/2016
 */
package me.gjkf.seriousEngine.render;

import org.joml.Vector3f;

/**
 * Enum to store some of the most used colors.
 */

public enum Colors{

    WHITE(new Color4f(1f, 1f, 1f, 1f)),
    BLACK(new Color4f(0f, 0f, 0f, 1f)),
    RED(new Color4f(1f, 0f, 0f, 1f)),
    GREEN(new Color4f(0f, 1f, 0f, 1f)),
    BLUE(new Color4f(0f, 0f, 1f, 1f)),
    PURPLE(new Color4f(1f, 0f, 1f, 1f)),
    NULL(null);

    public final Color4f color;

    Colors(Color4f Color4f){
        this.color = Color4f;
    }

    public Vector3f toVector(){
        return new Vector3f(this.color.r, this.color.g, this.color.b);
    }

}
