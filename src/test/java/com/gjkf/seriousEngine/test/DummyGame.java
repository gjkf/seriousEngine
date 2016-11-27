/*
 * Created by Davide Cossu (gjkf), 11/1/2016
 */
package com.gjkf.seriousEngine.test;

import com.gjkf.seriousEngine.ILogic;
import com.gjkf.seriousEngine.MouseInput;
import com.gjkf.seriousEngine.Window;
import com.gjkf.seriousEngine.items.Item;
import com.gjkf.seriousEngine.items.Terrain;
import com.gjkf.seriousEngine.loaders.md5.MD5AnimModel;
import com.gjkf.seriousEngine.loaders.md5.MD5Loader;
import com.gjkf.seriousEngine.loaders.md5.MD5Model;
import com.gjkf.seriousEngine.loaders.obj.OBJLoader;
import com.gjkf.seriousEngine.render.*;
import com.gjkf.seriousEngine.render.anim.AnimItem;
import com.gjkf.seriousEngine.render.lights.DirectionalLight;
import com.gjkf.seriousEngine.render.lights.SceneLight;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class DummyGame implements ILogic{

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private Scene scene;

    private Hud hud;

    private Terrain terrain;

    private float angleInc;

    private float lightAngle;

    private AnimItem bob;

    private static final float CAMERA_POS_STEP = 0.05f;

    public DummyGame(){
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        angleInc = 0;
        lightAngle = 45;
    }

    @Override
    public void init(Window window) throws Exception{
        renderer.init(window);

        scene = new Scene();

        float reflectance = 1f;

        Mesh quadMesh = OBJLoader.loadMesh("/models/plane.obj");
        Material quadMaterial = new Material(Colors.BLUE.toVector(), reflectance);
        quadMesh.setMaterial(quadMaterial);
        Item quadItem = new Item(quadMesh);
        quadItem.setPosition(0, 0, 0);
        quadItem.setScale(2.5f);

        // Setup  GameItems
        MD5Model md5Meshodel = MD5Model.parse("/models/monster/monster.md5mesh");
        MD5AnimModel md5AnimModel = MD5AnimModel.parse("/models/monster/monster.md5anim");
//        MD5Model md5Meshodel = MD5Model.parse("/models/bob/boblamp.md5mesh");
//        MD5AnimModel md5AnimModel = MD5AnimModel.parse("/models/bob/boblamp.md5anim");

        bob = MD5Loader.process(md5Meshodel, md5AnimModel, Colors.WHITE.toVector());
        bob.setScale(0.05f);
        bob.setRotation(90, 0, 90);
        //monster.setRotation(90, 0, 0);

        scene.setItems(new Item[] {quadItem, bob});

        // Setup Lights
        setupLights();

        camera.getPosition().x = 0.25f;
        camera.getPosition().y = 6.5f;
        camera.getPosition().z = 6.5f;
        camera.getRotation().x = 25;
        camera.getRotation().y = -1;
        hud = new Hud("");
    }

    private void setupLights(){
        SceneLight sceneLight = new SceneLight();
        scene.setSceneLight(sceneLight);

        // Ambient Light
        sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));
        sceneLight.setSkyBoxLight(new Vector3f(1.0f, 1.0f, 1.0f));

        // Directional Light
        float lightIntensity = 1.0f;
        Vector3f lightDirection = new Vector3f(0, 1, 1);
        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), lightDirection, lightIntensity);
        directionalLight.setShadowPosMult(10);
        directionalLight.setOrthoCords(-10.0f, 10.0f, -10.0f, 10.0f, -1.0f, 20.0f);
        sceneLight.setDirectionalLight(directionalLight);
    }

    @Override
    public void input(Window window, MouseInput mouseInput){
        cameraInc.set(0, 0, 0);
        if(window.isKeyPressed(GLFW_KEY_W)){
            cameraInc.z = -1;
        }else if(window.isKeyPressed(GLFW_KEY_S)){
            cameraInc.z = 1;
        }
        if(window.isKeyPressed(GLFW_KEY_A)){
            cameraInc.x = -1;
        }else if(window.isKeyPressed(GLFW_KEY_D)){
            cameraInc.x = 1;
        }
        if(window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)){
            cameraInc.y = -1;
        }else if(window.isKeyPressed(GLFW_KEY_SPACE)){
            cameraInc.y = 1;
        }
        if(window.isKeyPressed(GLFW_KEY_LEFT)){
            angleInc -= 0.05f;
        }else if(window.isKeyPressed(GLFW_KEY_RIGHT)){
            angleInc += 0.05f;
        }else{
            angleInc = 0;
        }
        if(window.isKeyPressed(GLFW_KEY_L)){
            bob.nextFrame();
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput){
        // Update camera based on mouse
        if(mouseInput.isLeftButtonPressed()){
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        // Update camera position
        Vector3f prevPos = new Vector3f(camera.getPosition());
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);
        // Check if there has been a collision. If true, set the y position to
        // the maximum height
        float height = terrain != null ? terrain.getHeight(camera.getPosition()) : -Float.MAX_VALUE;
        if(camera.getPosition().y <= height){
            camera.setPosition(prevPos.x, prevPos.y, prevPos.z);
        }

        lightAngle += angleInc;
        if(lightAngle < 0){
            lightAngle = 0;
        }else if(lightAngle > 180){
            lightAngle = 180;
        }
        float zValue = (float) Math.cos(Math.toRadians(lightAngle));
        float yValue = (float) Math.sin(Math.toRadians(lightAngle));
        Vector3f lightDirection = this.scene.getSceneLight().getDirectionalLight().getDirection();
        lightDirection.x = 0;
        lightDirection.y = yValue;
        lightDirection.z = zValue;
        lightDirection.normalize();
    }

    @Override
    public void render(Window window){
        if(hud != null){
            hud.updateSize(window);
        }
        renderer.render(window, camera, scene, hud);
    }

    @Override
    public void cleanup(){
        renderer.cleanup();
        scene.cleanup();
        if(hud != null){
            hud.cleanup();
        }
    }
}