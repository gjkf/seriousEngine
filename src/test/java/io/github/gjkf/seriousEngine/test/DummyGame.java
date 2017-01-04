/*
 * Created by Davide Cossu (gjkf), 11/1/2016
 */
package io.github.gjkf.seriousEngine.test;

import io.github.gjkf.seriousEngine.ILogic;
import io.github.gjkf.seriousEngine.MouseInput;
import io.github.gjkf.seriousEngine.Window;
import io.github.gjkf.seriousEngine.items.Item;
import io.github.gjkf.seriousEngine.items.Terrain;
import io.github.gjkf.seriousEngine.loaders.obj.OBJLoader;
import io.github.gjkf.seriousEngine.render.*;
import io.github.gjkf.seriousEngine.render.lights.DirectionalLight;
import io.github.gjkf.seriousEngine.render.lights.SceneLight;
import io.github.gjkf.seriousEngine.render.particles.FlowParticleEmitter;
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

    private static final float CAMERA_POS_STEP = 0.10f;

    private Terrain terrain;

    private float angleInc;

    private float lightAngle;

    private FlowParticleEmitter particleEmitter;

    private Item block, block1;

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

        float reflectance = 1.0f;
        Mesh cube = OBJLoader.loadMesh("/engineModels/cube.obj");
        cube.setMaterial(new Material(new Texture("/textures/grassblock.png"), reflectance));
        block = new Item(cube);
//        block.setScale(0.5f);
        block.setPosition(0, 0, -5);
        block1 = new Item(cube);
//        block1.setScale(0.5f);
        block1.setPosition(-1, 0, -5);
//        l.setScale(2f);
        scene.setItems(new Item[]{
                block,block1
        });

        setupLights();

        hud = new Hud("DEMO \\ s");
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
        if(window.isKeyPressed(GLFW_KEY_L)){
            angleInc -= 0.05f;
        }else if(window.isKeyPressed(GLFW_KEY_K)){
            angleInc += 0.05f;
        }
        if(window.isKeyPressed(GLFW_KEY_LEFT)){
            block1.setPosition(block1.getPosition().x-0.02f, block1.getPosition().y, block1.getPosition().z);
        }else if(window.isKeyPressed(GLFW_KEY_RIGHT)){
            block1.setPosition(block1.getPosition().x+0.02f, block1.getPosition().y, block1.getPosition().z);
        }
        if(window.isKeyPressed(GLFW_KEY_UP)){
            block1.setPosition(block1.getPosition().x, block1.getPosition().y+0.02f, block1.getPosition().z);
        }else if(window.isKeyPressed(GLFW_KEY_DOWN)){
            block1.setPosition(block1.getPosition().x, block1.getPosition().y-0.02f, block1.getPosition().z);
        }
        if(window.isKeyPressed(GLFW_KEY_X)){
            block1.setPosition(block1.getPosition().x, block1.getPosition().y, block1.getPosition().z+0.02f);
        }else if(window.isKeyPressed(GLFW_KEY_Z)){
            block1.setPosition(block1.getPosition().x, block1.getPosition().y, block1.getPosition().z-0.02f);
        }

    }

    @Override
    public void update(float interval, MouseInput mouseInput){
        System.err.println(block.checkCollisionWith(block1));
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

//        particleEmitter.update((long) (interval * 1000));
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