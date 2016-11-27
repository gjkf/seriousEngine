/*
 * Created by Davide Cossu (gjkf), 11/27/2016
 */

package com.gjkf.seriousEngine.loaders.md5;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a model mesh.
 */

public class MD5Mesh{

    /**
     * The pattern for the shader.
     */
    private static final Pattern PATTERN_SHADER = Pattern.compile("\\s*shader\\s*\\\"([^\\\"]+)\\\"");
    /**
     * The pattern for the vertex.
     */
    private static final Pattern PATTERN_VERTEX = Pattern.compile("\\s*vert\\s*(\\d+)\\s*\\(\\s*("
            + MD5Utils.FLOAT_REGEXP + ")\\s*(" + MD5Utils.FLOAT_REGEXP + ")\\s*\\)\\s*(\\d+)\\s*(\\d+)");
    /**
     * The pattern for the triangles.
     */
    private static final Pattern PATTERN_TRI = Pattern.compile("\\s*tri\\s*(\\d+)\\s*(\\d+)\\s*(\\d+)\\s*(\\d+)");
    /**
     * The pattern for the weights.
     */
    private static final Pattern PATTERN_WEIGHT = Pattern.compile("\\s*weight\\s*(\\d+)\\s*(\\d+)\\s*" +
            "(" + MD5Utils.FLOAT_REGEXP + ")\\s*" + MD5Utils.VECTOR3_REGEXP);
    /**
     * The texture.
     */
    private String texture;
    /**
     * The vertices.
     */
    private List<MD5Vertex> vertices;
    /**
     * The triangles.
     */
    private List<MD5Triangle> triangles;
    /**
     * The weights.
     */
    private List<MD5Weight> weights;

    /**
     * Constructs a new MD5Mesh.
     */
    public MD5Mesh(){
        this.vertices = new ArrayList<>();
        this.triangles = new ArrayList<>();
        this.weights = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder("mesh [" + System.lineSeparator());
        str.append("texture: ").append(texture).append(System.lineSeparator());

        str.append("vertices [").append(System.lineSeparator());
        for(MD5Vertex vertex : vertices){
            str.append(vertex).append(System.lineSeparator());
        }
        str.append("]").append(System.lineSeparator());

        str.append("triangles [").append(System.lineSeparator());
        for(MD5Triangle triangle : triangles){
            str.append(triangle).append(System.lineSeparator());
        }
        str.append("]").append(System.lineSeparator());

        str.append("weights [").append(System.lineSeparator());
        for(MD5Weight weight : weights){
            str.append(weight).append(System.lineSeparator());
        }
        str.append("]").append(System.lineSeparator());

        return str.toString();
    }

    /**
     * Parses a mesh block to get a model mesh.
     *
     * @param meshBlock The block.
     *
     * @return The model mesh.
     */

    public static MD5Mesh parse(List<String> meshBlock){
        MD5Mesh mesh = new MD5Mesh();
        List<MD5Vertex> vertices = mesh.getVertices();
        List<MD5Triangle> triangles = mesh.getTriangles();
        List<MD5Weight> weights = mesh.getWeights();

        for(String line : meshBlock){
            if(line.contains("shader")){
                Matcher textureMatcher = PATTERN_SHADER.matcher(line);
                if(textureMatcher.matches()){
                    mesh.setTexture(textureMatcher.group(1));

                }
            }else if(line.contains("vert")){
                Matcher vertexMatcher = PATTERN_VERTEX.matcher(line);
                if(vertexMatcher.matches()){
                    MD5Vertex vertex = new MD5Vertex();
                    vertex.setIndex(Integer.parseInt(vertexMatcher.group(1)));
                    float x = Float.parseFloat(vertexMatcher.group(2));
                    float y = Float.parseFloat(vertexMatcher.group(3));
                    vertex.setTextCoords(new Vector2f(x, y));
                    vertex.setStartWeight(Integer.parseInt(vertexMatcher.group(4)));
                    vertex.setWeightCount(Integer.parseInt(vertexMatcher.group(5)));
                    vertices.add(vertex);
                }
            }else if(line.contains("tri")){
                Matcher triMatcher = PATTERN_TRI.matcher(line);
                if(triMatcher.matches()){
                    MD5Triangle triangle = new MD5Triangle();
                    triangle.setIndex(Integer.parseInt(triMatcher.group(1)));
                    triangle.setVertex0(Integer.parseInt(triMatcher.group(2)));
                    triangle.setVertex1(Integer.parseInt(triMatcher.group(3)));
                    triangle.setVertex2(Integer.parseInt(triMatcher.group(4)));
                    triangles.add(triangle);
                }
            }else if(line.contains("weight")){
                Matcher weightMatcher = PATTERN_WEIGHT.matcher(line);
                if(weightMatcher.matches()){
                    MD5Weight weight = new MD5Weight();
                    weight.setIndex(Integer.parseInt(weightMatcher.group(1)));
                    weight.setJointIndex(Integer.parseInt(weightMatcher.group(2)));
                    weight.setBias(Float.parseFloat(weightMatcher.group(3)));
                    float x = Float.parseFloat(weightMatcher.group(4));
                    float y = Float.parseFloat(weightMatcher.group(5));
                    float z = Float.parseFloat(weightMatcher.group(6));
                    weight.setPosition(new Vector3f(x, y, z));
                    weights.add(weight);
                }
            }
        }
        return mesh;
    }

    /**
     * Getter for property 'texture'.
     *
     * @return Value for property 'texture'.
     */

    public String getTexture(){
        return texture;
    }

    /**
     * Setter for property 'texture'.
     *
     * @param texture Value to set for property 'texture'.
     */

    public void setTexture(String texture){
        this.texture = texture;
    }

    /**
     * Getter for property 'vertices'.
     *
     * @return Value for property 'vertices'.
     */

    public List<MD5Vertex> getVertices(){
        return vertices;
    }

    /**
     * Setter for property 'vertices'.
     *
     * @param vertices Value to set for property 'vertices'.
     */

    public void setVertices(List<MD5Vertex> vertices){
        this.vertices = vertices;
    }

    /**
     * Getter for property 'triangles'.
     *
     * @return Value for property 'triangles'.
     */

    public List<MD5Triangle> getTriangles(){
        return triangles;
    }

    /**
     * Setter for property 'triangles'.
     *
     * @param triangles Value to set for property 'triangles'.
     */

    public void setTriangles(List<MD5Triangle> triangles){
        this.triangles = triangles;
    }

    /**
     * Getter for property 'weights'.
     *
     * @return Value for property 'weights'.
     */

    public List<MD5Weight> getWeights(){
        return weights;
    }

    /**
     * Setter for property 'weights'.
     *
     * @param weights Value to set for property 'weights'.
     */

    public void setWeights(List<MD5Weight> weights){
        this.weights = weights;
    }

    /**
     * A model vertex.
     */

    public static class MD5Vertex{
        /**
         * The index.
         */
        private int index;
        /**
         * The texture coordinate.
         */
        private Vector2f textCoords;
        /**
         * The start weight.
         */
        private int startWeight;
        /**
         * The weight count.
         */
        private int weightCount;

        /**
         * Getter for property 'index'.
         *
         * @return Value for property 'index'.
         */

        public int getIndex(){
            return index;
        }

        /**
         * Setter for property 'index'.
         *
         * @param index Value to set for property 'index'.
         */

        public void setIndex(int index){
            this.index = index;
        }

        /**
         * Getter for property 'textCoords'.
         *
         * @return Value for property 'textCoords'.
         */

        public Vector2f getTextCoords(){
            return textCoords;
        }

        /**
         * Setter for property 'textCoords'.
         *
         * @param textCoords Value to set for property 'textCoords'.
         */

        public void setTextCoords(Vector2f textCoords){
            this.textCoords = textCoords;
        }

        /**
         * Getter for property 'startWeight'.
         *
         * @return Value for property 'startWeight'.
         */

        public int getStartWeight(){
            return startWeight;
        }

        /**
         * Setter for property 'startWeight'.
         *
         * @param startWeight Value to set for property 'startWeight'.
         */

        public void setStartWeight(int startWeight){
            this.startWeight = startWeight;
        }

        /**
         * Getter for property 'weightCount'.
         *
         * @return Value for property 'weightCount'.
         */

        public int getWeightCount(){
            return weightCount;
        }

        /**
         * Setter for property 'weightCount'.
         *
         * @param weightCount Value to set for property 'weightCount'.
         */

        public void setWeightCount(int weightCount){
            this.weightCount = weightCount;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString(){
            return "[index: " + index + ", textCoods: " + textCoords
                    + ", startWeight: " + startWeight + ", weightCount: " + weightCount + "]";
        }
    }

    /**
     * A model triangle.
     */

    public static class MD5Triangle{
        /**
         * The index.
         */
        private int index;
        /**
         * The first vertex.
         */
        private int vertex0;
        /**
         * The second vertex.
         */
        private int vertex1;
        /**
         * The third vertex.
         */
        private int vertex2;

        /**
         * Getter for property 'index'.
         *
         * @return Value for property 'index'.
         */

        public int getIndex(){
            return index;
        }

        /**
         * Setter for property 'index'.
         *
         * @param index Value to set for property 'index'.
         */

        public void setIndex(int index){
            this.index = index;
        }

        /**
         * Getter for property 'vertex0'.
         *
         * @return Value for property 'vertex0'.
         */

        public int getVertex0(){
            return vertex0;
        }

        /**
         * Setter for property 'vertex0'.
         *
         * @param vertex0 Value to set for property 'vertex0'.
         */

        public void setVertex0(int vertex0){
            this.vertex0 = vertex0;
        }

        /**
         * Getter for property 'vertex1'.
         *
         * @return Value for property 'vertex1'.
         */

        public int getVertex1(){
            return vertex1;
        }

        /**
         * Setter for property 'vertex1'.
         *
         * @param vertex1 Value to set for property 'vertex1'.
         */

        public void setVertex1(int vertex1){
            this.vertex1 = vertex1;
        }

        /**
         * Getter for property 'vertex2'.
         *
         * @return Value for property 'vertex2'.
         */

        public int getVertex2(){
            return vertex2;
        }

        /**
         * Setter for property 'vertex2'.
         *
         * @param vertex2 Value to set for property 'vertex2'.
         */

        public void setVertex2(int vertex2){
            this.vertex2 = vertex2;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString(){
            return "[index: " + index + ", vertex0: " + vertex0
                    + ", vertex1: " + vertex1 + ", vertex2: " + vertex2 + "]";
        }
    }

    /**
     * Represents a weight.
     */

    public static class MD5Weight{
        /**
         * The index.
         */
        private int index;
        /**
         * The joint index.
         */
        private int jointIndex;
        /**
         * The bias.
         */
        private float bias;
        /**
         * The position.
         */
        private Vector3f position;

        /**
         * Getter for property 'index'.
         *
         * @return Value for property 'index'.
         */

        public int getIndex(){
            return index;
        }

        /**
         * Setter for property 'index'.
         *
         * @param index Value to set for property 'index'.
         */

        public void setIndex(int index){
            this.index = index;
        }

        /**
         * Getter for property 'jointIndex'.
         *
         * @return Value for property 'jointIndex'.
         */

        public int getJointIndex(){
            return jointIndex;
        }

        /**
         * Setter for property 'jointIndex'.
         *
         * @param jointIndex Value to set for property 'jointIndex'.
         */

        public void setJointIndex(int jointIndex){
            this.jointIndex = jointIndex;
        }

        /**
         * Getter for property 'bias'.
         *
         * @return Value for property 'bias'.
         */

        public float getBias(){
            return bias;
        }

        /**
         * Setter for property 'bias'.
         *
         * @param bias Value to set for property 'bias'.
         */

        public void setBias(float bias){
            this.bias = bias;
        }

        /**
         * Getter for property 'position'.
         *
         * @return Value for property 'position'.
         */

        public Vector3f getPosition(){
            return position;
        }

        /**
         * Setter for property 'position'.
         *
         * @param position Value to set for property 'position'.
         */

        public void setPosition(Vector3f position){
            this.position = position;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString(){
            return "[index: " + index + ", jointIndex: " + jointIndex
                    + ", bias: " + bias + ", position: " + position + "]";
        }
    }
}