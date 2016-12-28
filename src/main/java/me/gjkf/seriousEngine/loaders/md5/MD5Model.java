/*
 * Created by Davide Cossu (gjkf), 11/27/2016
 */

package me.gjkf.seriousEngine.loaders.md5;

import me.gjkf.seriousEngine.Utils;

import java.util.ArrayList;
import java.util.List;

public class MD5Model{

    /**
     * The joint info.
     */
    private MD5JointInfo jointInfo;
    /**
     * The header.
     */
    private MD5ModelHeader header;
    /**
     * The meshes.
     */
    private List<MD5Mesh> meshes;

    /**
     * Constructs a new MD5Model.
     */

    public MD5Model(){
        meshes = new ArrayList<>();
    }

    /**
     * Getter for property 'jointInfo'.
     *
     * @return Value for property 'jointInfo'.
     */

    public MD5JointInfo getJointInfo(){
        return jointInfo;
    }

    /**
     * Setter for property 'jointInfo'.
     *
     * @param jointInfo Value to set for property 'jointInfo'.
     */

    public void setJointInfo(MD5JointInfo jointInfo){
        this.jointInfo = jointInfo;
    }

    /**
     * Getter for property 'header'.
     *
     * @return Value for property 'header'.
     */

    public MD5ModelHeader getHeader(){
        return header;
    }

    /**
     * Setter for property 'header'.
     *
     * @param header Value to set for property 'header'.
     */

    public void setHeader(MD5ModelHeader header){
        this.header = header;
    }

    /**
     * Getter for property 'meshes'.
     *
     * @return Value for property 'meshes'.
     */

    public List<MD5Mesh> getMeshes(){
        return meshes;
    }

    /**
     * Setter for property 'meshes'.
     *
     * @param meshes Value to set for property 'meshes'.
     */

    public void setMeshes(List<MD5Mesh> meshes){
        this.meshes = meshes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder("MD5MeshModel: " + System.lineSeparator());
        str.append(getHeader()).append(System.lineSeparator());
        str.append(getJointInfo()).append(System.lineSeparator());

        for(MD5Mesh mesh : meshes){
            str.append(mesh).append(System.lineSeparator());
        }
        return str.toString();
    }

    /**
     * Parses the model file.
     *
     * @param meshModelFile The file.
     *
     * @return The model.
     *
     * @throws Exception If the file is non existent.
     */

    public static MD5Model parse(String meshModelFile) throws Exception{
        List<String> lines = Utils.readAllLines(meshModelFile);

        MD5Model result = new MD5Model();

        int numLines = lines != null ? lines.size() : 0;
        if(numLines == 0){
            throw new Exception("Cannot parse empty file");
        }

        // Parse Header
        boolean headerEnd = false;
        int start = 0;
        for(int i = 0; i < numLines && !headerEnd; i++){
            String line = lines.get(i);
            headerEnd = line.trim().endsWith("{");
            start = i;
        }
        if(!headerEnd){
            throw new Exception("Cannot find header");
        }
        List<String> headerBlock = lines.subList(0, start);
        MD5ModelHeader header = MD5ModelHeader.parse(headerBlock);
        result.setHeader(header);

        // Parse the rest of block
        int blockStart = 0;
        boolean inBlock = false;
        String blockId = "";
        for(int i = start; i < numLines; i++){
            String line = lines.get(i);
            if(line.endsWith("{")){
                blockStart = i;
                blockId = line.substring(0, line.lastIndexOf(" "));
                inBlock = true;
            }else if(inBlock && line.endsWith("}")){
                List<String> blockBody = lines.subList(blockStart + 1, i);
                parseBlock(result, blockId, blockBody);
                inBlock = false;
            }
        }

        return result;
    }

    /**
     * Parses a block.
     *
     * @param model     The model.
     * @param blockId   The block ID.
     * @param blockBody The block vody.
     *
     * @throws Exception If anything went wrong.
     */

    private static void parseBlock(MD5Model model, String blockId, List<String> blockBody) throws Exception{
        switch(blockId){
            case "joints":
                MD5JointInfo jointInfo = MD5JointInfo.parse(blockBody);
                model.setJointInfo(jointInfo);
                break;
            case "mesh":
                MD5Mesh md5Mesh = MD5Mesh.parse(blockBody);
                model.getMeshes().add(md5Mesh);
                break;
            default:
                break;
        }
    }

}