/*
  Created by Davide Cossu (gjkf), 7/9/2016
 */
package com.gjkf.seriousEngine.core.gui;

import com.gjkf.seriousEngine.core.render.Color3f;

public enum Colors{

	WHITE(new Color3f(1f, 1f, 1f)),
	BLACK(new Color3f(0f, 0f, 0f)),
	RED(new Color3f(1f, 0f, 0f)),
	GREEN(new Color3f(0f, 1f, 0f)),
	BLUE(new Color3f(0f, 0f, 1f));

	public final Color3f color;

	Colors(Color3f color3f){
		this.color = color3f;
	}

}
