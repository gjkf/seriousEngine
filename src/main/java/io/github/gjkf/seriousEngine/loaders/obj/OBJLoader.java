/*
 * Created by Davide Cossu (gjkf), 11/5/2016
 */
package io.github.gjkf.seriousEngine.loaders.obj;


import io.github.gjkf.seriousEngine.Utils;
import io.github.gjkf.seriousEngine.render.InstancedMesh;
import io.github.gjkf.seriousEngine.render.Mesh;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Loads and reads a basic <tt>.obj</tt> file.
 */

public class OBJLoader{

    /**
     * Represents a face that can and cannot have the texture vertices.
     * <p>Each face is represented by 3 vertices for the triangle.</p>
     */

    private static class Face{

        /**
         * The groups for each vertex.
         */
        private VertexGroup[] groups = new VertexGroup[3];

        /**
         * Constructs a new Face.
         *
         * @param v0 The first vertex.
         * @param v1 The second vertex.
         * @param v2 The third vertex.
         */

        public Face(String v0, String v1, String v2){
            groups[0] = parseLine(v0);
            groups[1] = parseLine(v1);
            groups[2] = parseLine(v2);
        }

        /**
         * Parses and gets the information from the line.
         *
         * @param line The line to parse.
         *
         * @return The {@link VertexGroup} object with the information.
         */

        private VertexGroup parseLine(String line){
            VertexGroup group = new VertexGroup();

            String[] tokens = line.split("/");
            if(tokens.length < 3){
                throw new IllegalStateException("The following line has less than 3 attributes to it. Please use (v/vn/vt) or (v//vn)");
            }

            group.v = Integer.parseInt(tokens[0]);
            group.vt = Objects.equals(tokens[1], "") ? VertexGroup.NULL : Integer.parseInt(tokens[1]);
            group.vn = Integer.parseInt(tokens[2]);

            return group;
        }

        VertexGroup[] getVertexGroups(){
            return groups;
        }

    }

    /**
     * Represents the vertex group for a face.
     * <p>It can have <tt>v/vt/vn</tt> and <tt>v//vn</tt> as formats.</p>
     */

    private static class VertexGroup{
        /**
         * The vertex information.
         */
        int v, vt, vn;
        /**
         * The value for the not valid information.
         */
        static int NULL = -2;

        VertexGroup(){
            v = NULL;
            vt = NULL;
            vn = NULL;
        }

    }

    /**
     * Loads a mesh from the given path.
     *
     * @param objPath   The relative path.
     * @param instances The number of instances.
     *
     * @return The new mesh.
     *
     * @throws Exception If anything went wrong.
     */

    public static Mesh loadMesh(String objPath, int instances) throws Exception{
        return loadMesh(Thread.currentThread().getContextClassLoader().getResourceAsStream(objPath), instances);
    }

    /**
     * Loads a mesh from the given path.
     *
     * @param objPath The relative path.
     *
     * @return The new mesh.
     *
     * @throws Exception If anything went wrong.
     */

    public static Mesh loadMesh(String objPath) throws Exception{
        return loadMesh(Thread.currentThread().getContextClassLoader().getResourceAsStream(objPath));
    }

    /**
     * Loads a single instanced mesh from the given {@link InputStream}.
     *
     * @param obj The inputStream.
     *
     * @return The new mesh.
     *
     * @throws Exception If anything went wrong.
     */

    public static Mesh loadMesh(InputStream obj) throws Exception{
        return loadMesh(obj, 1);
    }

    /**
     * Loads a mesh from a file (with an absolute path).
     *
     * @param obj       The InputStream of the <tt>.obj</tt> file.
     * @param instances The number of instances.
     *
     * @return The new mesh.
     *
     * @throws Exception If anything went wrong.
     */

    public static Mesh loadMesh(InputStream obj, int instances) throws Exception{
        List<String> objLines = new BufferedReader(new InputStreamReader(obj)).lines().collect(Collectors.toList());

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        for(String line : objLines){
            String[] tokens = line.split("\\s+");
            switch(tokens[0]){
                // Vertex
                case "v":
                    Vector3f vec3f = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    vertices.add(vec3f);
                    break;
                // Texture
                case "vt":
                    Vector2f vec2f = new Vector2f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2])
                    );
                    textures.add(vec2f);
                    break;
                // Normal
                case "vn":
                    Vector3f vec3fNorm = new Vector3f(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    );
                    normals.add(vec3fNorm);
                    break;
                // Face
                case "f":
                    Face face = new Face(tokens[0], tokens[1], tokens[2]);
                    faces.add(face);
                    break;
                // All other lines we ignore them
                default:
                    break;
            }
        }
        return reorderLists(vertices, textures, normals, faces, instances);
    }

    /**
     * Reorders the list. When adding them in {@link #loadMesh(InputStream, int)} they are backwards.
     *
     * @param posList       The list of the vertices.
     * @param textCoordList The list of the textures.
     * @param normList      The list of the normals.
     * @param facesList     The list of the faces.
     * @param instances     The instances.
     *
     * @return The mesh.
     */

    private static Mesh reorderLists(List<Vector3f> posList, List<Vector2f> textCoordList, List<Vector3f> normList, List<Face> facesList, int instances){

        List<Integer> indices = new ArrayList<>();
        // Create position array in the order it has been declared
        float[] posArr = new float[posList.size() * 3];
        int i = 0;
        for(Vector3f pos : posList){
            posArr[i * 3] = pos.x;
            posArr[i * 3 + 1] = pos.y;
            posArr[i * 3 + 2] = pos.z;
            i++;
        }
        float[] textCoordArr = new float[posList.size() * 2];
        float[] normArr = new float[posList.size() * 3];

        for(Face face : facesList){
            VertexGroup[] faceVertexIndices = face.getVertexGroups();
            for(VertexGroup indValue : faceVertexIndices){
                processFaceVertex(indValue, textCoordList, normList, indices, textCoordArr, normArr);
            }
        }
        int[] indicesArr = Utils.listIntToArray(indices);
        Mesh mesh;
        if(instances > 1){
            mesh = new InstancedMesh(posArr, textCoordArr, normArr, indicesArr, instances);
        }else{
            mesh = new Mesh(posArr, textCoordArr, normArr, indicesArr);
        }
        return mesh;
    }

    /**
     * Processes the lists and sets their correct values.
     *
     * @param indices       The indices.
     * @param textCoordList The list of the textures.
     * @param normList      The list of the normals.
     * @param indicesList   The list of the indices.
     * @param texCoordArr   The array of the texture coordinates.
     * @param normArr       The normals array.
     */

    private static void processFaceVertex(VertexGroup indices, List<Vector2f> textCoordList, List<Vector3f> normList, List<Integer> indicesList, float[] texCoordArr, float[] normArr){
        // Set index for vertex coordinates
        int posIndex = indices.v;
        indicesList.add(posIndex);

        // Reorder texture coordinates
        if(indices.vt >= 0){
            Vector2f textCoord = textCoordList.get(indices.vt);
            texCoordArr[posIndex * 2] = textCoord.x;
            texCoordArr[posIndex * 2 + 1] = 1 - textCoord.y;
        }
        if(indices.vn >= 0){
            // Reorder vector normals
            Vector3f vecNorm = normList.get(indices.vn);
            normArr[posIndex * 3] = vecNorm.x;
            normArr[posIndex * 3 + 1] = vecNorm.y;
            normArr[posIndex * 3 + 2] = vecNorm.z;
        }
    }

}