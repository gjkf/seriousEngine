/*
 * Created by Davide Cossu (gjkf), 11/15/2016
 */

package me.gjkf.seriousEngine.items;

import me.gjkf.seriousEngine.loaders.obj.OBJLoader;
import me.gjkf.seriousEngine.render.Material;
import me.gjkf.seriousEngine.render.Mesh;
import me.gjkf.seriousEngine.render.Texture;
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