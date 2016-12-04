/*
 * Created by Davide Cossu (gjkf), 11/15/2016
 */

package com.gjkf.seriousEngine.items;

import com.gjkf.seriousEngine.loaders.obj.OBJLoader;
import com.gjkf.seriousEngine.render.Material;
import com.gjkf.seriousEngine.render.Mesh;
import com.gjkf.seriousEngine.render.Texture;
import org.joml.Vector3f;

/**
 * Object representing a sky box.
 * <p>Extends {@link Item} so it's easy to access utility methods.</p>
 */

public class SkyBox extends Item{

    public SkyBox(String objModel, String textureFile) throws Exception {
        super();
        Mesh skyBoxMesh = OBJLoader.loadMesh(objModel);
        Texture skyBoxtexture = new Texture(textureFile);
        skyBoxMesh.setMaterial(new Material(skyBoxtexture, 0.0f));
        setMesh(skyBoxMesh);
        setPosition(0, 0, 0);
    }

    public SkyBox(String objModel, Vector3f colour) throws Exception {
        super();
        Mesh skyBoxMesh = OBJLoader.loadMesh(objModel);
        Material material = new Material(colour, 0);
        skyBoxMesh.setMaterial(material);
        setMesh(skyBoxMesh);
        setPosition(0, 0, 0);
    }

}