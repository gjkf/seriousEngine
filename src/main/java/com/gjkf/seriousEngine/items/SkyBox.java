/*
 * Created by Davide Cossu (gjkf), 11/15/2016
 */

package com.gjkf.seriousEngine.items;

import com.gjkf.seriousEngine.loaders.obj.OBJLoader;
import com.gjkf.seriousEngine.render.Material;
import com.gjkf.seriousEngine.render.Mesh;
import com.gjkf.seriousEngine.render.Texture;

/**
 * Object representing a sky box.
 * <p>Extends {@link Item} so it's easy to access utility methods.</p>
 */

public class SkyBox extends Item{

    public SkyBox(String objModel, String textureFile) throws Exception{
        super();
        Mesh skyBoxMesh = OBJLoader.loadMesh(objModel);
        Texture skyBoxTexture = new Texture(textureFile);
        skyBoxMesh.setMaterial(new Material(skyBoxTexture, 0.0f));
        setMesh(skyBoxMesh);
        setPosition(0, 0, 0);
    }

}