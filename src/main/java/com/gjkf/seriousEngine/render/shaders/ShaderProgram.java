/*
 * Created by Davide Cossu (gjkf), 11/1/2016
 */
package com.gjkf.seriousEngine.render.shaders;

import com.gjkf.seriousEngine.render.Material;
import com.gjkf.seriousEngine.render.lights.DirectionalLight;
import com.gjkf.seriousEngine.render.lights.PointLight;
import com.gjkf.seriousEngine.render.lights.SpotLight;
import com.gjkf.seriousEngine.render.weather.Fog;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

/**
 * Object representing a <tt>Shader program</tt>.
 * <p>Provides useful methods to create, set and load shaders.</p>
 */

public class ShaderProgram{

    /**
     * The program ID
     */
    private final int programId;
    /**
     * The ID of the vertex shader.
     */
    private int vertexShaderId;
    /**
     * The ID of the fragment shader.
     */
    private int fragmentShaderId;
    /**
     * The ID of the geometry shader.
     */
    private int geometryShaderId;
    /**
     * A map containing all the uniforms that will be passed to the shaders.
     */
    private final Map<String, UniformData> uniforms;

    /**
     * Creates an new program.
     *
     * @throws Exception If the {@link #programId} is 0.
     */
    public ShaderProgram() throws Exception{
        programId = glCreateProgram();
        if(programId == 0){
            throw new Exception("Could not create Shader");
        }
        uniforms = new HashMap<>();
    }

    /**
     * Adds the <tt>uniformName</tt> to {@link #uniforms}.
     *
     * @param uniformName The name to add.
     *
     * @throws Exception If the uniform could not be found in the shader.
     */

    public void createUniform(String uniformName) throws Exception{
        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if(uniformLocation < 0){
            throw new Exception("Could not find uniform:" + uniformName);
        }
        uniforms.put(uniformName, new UniformData(uniformLocation));
    }

    /**
     * Sets the value of the given uniform.
     *
     * @param uniformName The uniform to reference.
     * @param value       The value.
     */

    public void setUniform(String uniformName, Vector3f value){
        UniformData uniformData = uniforms.get(uniformName);
        if(uniformData == null){
            throw new RuntimeException("Uniform [" + uniformName + "] has nor been created");
        }
        glUniform3f(uniformData.getUniformLocation(), value.x, value.y, value.z);
    }


    /**
     * Sets the value of the given uniform.
     *
     * @param uniformName The uniform to reference.
     * @param value       The value.
     */

    public void setUniform(String uniformName, Matrix4f value){
        UniformData uniformData = uniforms.get(uniformName);
        if(uniformData == null){
            throw new RuntimeException("Uniform [" + uniformName + "] has nor been created");
        }
        // Check if float buffer has been created
        FloatBuffer fb = uniformData.getFloatBuffer();
        if(fb == null){
            fb = BufferUtils.createFloatBuffer(16);
            uniformData.setFloatBuffer(fb);
        }
        // Dump the matrix into a float buffer
        value.get(fb);
        glUniformMatrix4fv(uniformData.getUniformLocation(), false, fb);
    }

    /**
     * Sets the value of the given uniform.
     *
     * @param uniformName The uniform to reference.
     * @param value       The value.
     */

    public void setUniform(String uniformName, int value){
        UniformData uniformData = uniforms.get(uniformName);
        if(uniformData == null){
            throw new RuntimeException("Uniform [" + uniformName + "] has nor been created");
        }
        glUniform1i(uniformData.getUniformLocation(), value);
    }

    /**
     * Sets the value of the given uniform.
     *
     * @param uniformName The uniform to reference.
     * @param value       The value.
     */

    public void setUniform(String uniformName, float value){
        UniformData uniformData = uniforms.get(uniformName);
        if(uniformData == null){
            throw new RuntimeException("Uniform [" + uniformName + "] has nor been created");
        }
        glUniform1f(uniformData.getUniformLocation(), value);
    }

    /**
     * Sets the value of the given uniform.
     *
     * @param uniformName The uniform to reference.
     * @param dirLight    The value.
     */

    public void setUniform(String uniformName, DirectionalLight dirLight){
        setUniform(uniformName + ".colour", dirLight.getColor());
        setUniform(uniformName + ".direction", dirLight.getDirection());
        setUniform(uniformName + ".intensity", dirLight.getIntensity());
    }

    /**
     * Creates the uniforms for the array of lights.
     *
     * @param uniformName The name.
     * @param size        The size of the array.
     *
     * @throws Exception If anything went wrong.
     */

    public void createPointLightListUniform(String uniformName, int size) throws Exception{
        for(int i = 0; i < size; i++){
            createPointLightUniform(uniformName + "[" + i + "]");
        }
    }

    /**
     * Sets the uniforms of the array.
     *
     * @param uniformName The name.
     * @param pointLights The lights.
     */

    public void setUniform(String uniformName, PointLight[] pointLights){
        int numLights = pointLights != null ? pointLights.length : 0;
        for(int i = 0; i < numLights; i++){
            setUniform(uniformName, pointLights[i], i);
        }
    }

    /**
     * Sets the uniform for the given spot light.
     *
     * @param uniformName The name.
     * @param pointLight  The light.
     * @param pos         The position in the array.
     */

    public void setUniform(String uniformName, PointLight pointLight, int pos){
        setUniform(uniformName + "[" + pos + "]", pointLight);
    }

    /**
     * Sets the value of the given uniform.
     *
     * @param uniformName The uniform to reference.
     * @param pointLight  The value.
     */

    public void setUniform(String uniformName, PointLight pointLight){
        setUniform(uniformName + ".colour", pointLight.getColor());
        setUniform(uniformName + ".position", pointLight.getPosition());
        setUniform(uniformName + ".intensity", pointLight.getIntensity());
        PointLight.Attenuation att = pointLight.getAttenuation();
        setUniform(uniformName + ".att.constant", att.getConstant());
        setUniform(uniformName + ".att.linear", att.getLinear());
        setUniform(uniformName + ".att.exponent", att.getExponent());
    }

    /**
     * Creates the uniforms for the array of lights.
     *
     * @param uniformName The name.
     * @param size        The size of the array.
     *
     * @throws Exception If anything went wrong.
     */

    public void createSpotLightListUniform(String uniformName, int size) throws Exception{
        for(int i = 0; i < size; i++){
            createSpotLightUniform(uniformName + "[" + i + "]");
        }
    }

    /**
     * Sets the value of the given uniform.
     *
     * @param uniformName The uniform to reference.
     * @param spotLight   The value.
     */

    public void setUniform(String uniformName, SpotLight spotLight){
        setUniform(uniformName + ".pl", spotLight.getPointLight());
        setUniform(uniformName + ".conedir", spotLight.getConeDirection());
        setUniform(uniformName + ".cutoff", spotLight.getCutOff());
    }

    /**
     * Sets the uniform for the given spot light.
     *
     * @param uniformName The name.
     * @param spotLight   The light.
     * @param pos         The position in the array.
     */

    public void setUniform(String uniformName, SpotLight spotLight, int pos){
        setUniform(uniformName + "[" + pos + "]", spotLight);
    }

    /**
     * Sets the uniforms of the array.
     *
     * @param uniformName The name.
     * @param spotLights  The lights.
     */

    public void setUniform(String uniformName, SpotLight[] spotLights){
        int numLights = spotLights != null ? spotLights.length : 0;
        for(int i = 0; i < numLights; i++){
            setUniform(uniformName, spotLights[i], i);
        }
    }

    /**
     * Sets the value of the given uniform.
     *
     * @param uniformName The uniform to reference.
     * @param material    The value.
     */

    public void setUniform(String uniformName, Material material){
        setUniform(uniformName + ".colour", material.getColour());
        setUniform(uniformName + ".hasTexture", material.isTextured() ? 1 : 0);
        setUniform(uniformName + ".hasNormalMap", material.hasNormalMap() ? 1 : 0);
        setUniform(uniformName + ".reflectance", material.getReflectance());
    }

    /**
     * Sets the value of the given uniform.
     *
     * @param uniformName The uniform to reference.
     * @param fog         The value.
     */

    public void setUniform(String uniformName, Fog fog){
        setUniform(uniformName + ".activeFog", fog.isActive() ? 1 : 0);
        setUniform(uniformName + ".colour", fog.getColour());
        setUniform(uniformName + ".density", fog.getDensity());
    }

    /**
     * Sets the value of the given uniform.
     *
     * @param uniformName The uniform to reference.
     * @param matrices    The values.
     */

    public void setUniform(String uniformName, Matrix4f[] matrices){
        int length = matrices != null ? matrices.length : 0;
        UniformData uniformData = uniforms.get(uniformName);
        if(uniformData == null){
            throw new RuntimeException("Uniform [" + uniformName + "] has nor been created");
        }
        // Check if float buffer has been created
        FloatBuffer fb = uniformData.getFloatBuffer();
        if(fb == null){
            fb = BufferUtils.createFloatBuffer(16 * length);
            uniformData.setFloatBuffer(fb);
        }
        for(int i = 0; i < length; i++){
            matrices[i].get(16 * i, fb);
        }
        glUniformMatrix4fv(uniformData.getUniformLocation(), false, fb);
    }

    /**
     * Creates the uniform for a fog.
     *
     * @param uniformName The name.
     *
     * @throws Exception If anything went wrong.
     */

    public void createFogUniform(String uniformName) throws Exception{
        createUniform(uniformName + ".activeFog");
        createUniform(uniformName + ".colour");
        createUniform(uniformName + ".density");
    }

    /**
     * Creates the uniform for a point light.
     *
     * @param uniformName The name.
     *
     * @throws Exception If anything went wrong.
     */

    public void createPointLightUniform(String uniformName) throws Exception{
        createUniform(uniformName + ".colour");
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".att.constant");
        createUniform(uniformName + ".att.linear");
        createUniform(uniformName + ".att.exponent");
    }

    /**
     * Creates the uniform for a material.
     *
     * @param uniformName The name.
     *
     * @throws Exception If anything went wrong.
     */

    public void createMaterialUniform(String uniformName) throws Exception{
        createUniform(uniformName + ".colour");
        createUniform(uniformName + ".hasTexture");
        createUniform(uniformName + ".hasNormalMap");
        createUniform(uniformName + ".reflectance");

    }

    /**
     * Creates the uniform for a light.
     *
     * @param uniformName The name.
     *
     * @throws Exception If anything went wrong.
     */

    public void createDirectionalLightUniform(String uniformName) throws Exception{
        createUniform(uniformName + ".colour");
        createUniform(uniformName + ".direction");
        createUniform(uniformName + ".intensity");
    }

    /**
     * Creates the uniform for a light.
     *
     * @param uniformName The name.
     *
     * @throws Exception If anything went wrong.
     */

    public void createSpotLightUniform(String uniformName) throws Exception{
        createPointLightUniform(uniformName + ".pl");
        createUniform(uniformName + ".conedir");
        createUniform(uniformName + ".cutoff");
    }

    /**
     * Creates a vertex shader from a string of text.
     *
     * @param shaderCode The shader's code.
     *
     * @throws Exception If the {@link #vertexShaderId} is 0.
     */

    public void createVertexShader(String shaderCode) throws Exception{
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    /**
     * Creates a fragment shader from a string of text.
     *
     * @param shaderCode The shader's code.
     *
     * @throws Exception If the {@link #fragmentShaderId} is 0.
     */

    public void createFragmentShader(String shaderCode) throws Exception{
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    /**
     * Creates a shader of the given type with the given code.
     *
     * @param shaderCode The code of the shader.
     * @param shaderType The type of the shader.
     *
     * @return The ID of the new shader.
     *
     * @throws Exception If the ID is 0 or there has been a problem.
     */

    protected int createShader(String shaderCode, int shaderType) throws Exception{
        int shaderId = glCreateShader(shaderType);
        if(shaderId == 0){
            throw new Exception("Error creating shader. Code: " + shaderId);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if(glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0){
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    /**
     * Links the shaders to the program.
     *
     * @throws Exception If there has been any problems.
     */

    public void link() throws Exception{
        glLinkProgram(programId);
        if(glGetProgrami(programId, GL_LINK_STATUS) == 0){
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        glValidateProgram(programId);
        if(glGetProgrami(programId, GL_VALIDATE_STATUS) == 0){
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

    }

    /**
     * Binds the current program.
     */

    public void bind(){
        glUseProgram(programId);
    }

    /**
     * Unbinds the current program.
     */

    public void unbind(){
        glUseProgram(0);
    }

    /**
     * Cleans up the resources used.
     */

    public void cleanup(){
        unbind();
        if(programId != 0){
            if(vertexShaderId != 0){
                glDetachShader(programId, vertexShaderId);
            }
            if(fragmentShaderId != 0){
                glDetachShader(programId, fragmentShaderId);
            }
            if(geometryShaderId != 0){
                glDetachShader(programId, geometryShaderId);
            }
            glDeleteProgram(programId);
        }
    }

}