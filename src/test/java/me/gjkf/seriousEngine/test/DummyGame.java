/*
 * Created by Davide Cossu (gjkf), 11/1/2016
 */
package me.gjkf.seriousEngine.test;

import me.gjkf.seriousEngine.ILogic;
import me.gjkf.seriousEngine.MouseInput;
import me.gjkf.seriousEngine.Window;
import me.gjkf.seriousEngine.items.Item;
import me.gjkf.seriousEngine.items.Terrain;
import me.gjkf.seriousEngine.loaders.obj.OBJLoader;
import me.gjkf.seriousEngine.render.*;
import me.gjkf.seriousEngine.render.lights.DirectionalLight;
import me.gjkf.seriousEngine.render.lights.SceneLight;
import me.gjkf.seriousEngine.render.particles.FlowParticleEmitter;
import org.joml.Quaternionf;
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
        Mesh lander = OBJLoader.loadMesh("/engineModels/quad.obj");
        lander.setMaterial(new Material(new Texture("/textures/lander0.png"), reflectance));
        Item l = new Item(lander);
        l.setPosition(0, 0, -5);
        l.setRotation(new Quaternionf(0, 0, 0));
        l.setScale(2f);

        scene.setItems(new Item[]{
                l
        });

        setupLights();

//        camera.getPosition().y = 5f;
//        camera.getRotation().x = 90;

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
        if(window.isKeyPressed(GLFW_KEY_LEFT)){
            angleInc -= 0.05f;
        }else if(window.isKeyPressed(GLFW_KEY_RIGHT)){
            angleInc += 0.05f;
        }else{
            angleInc = 0;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput){
        System.out.println(camera.getPosition());
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