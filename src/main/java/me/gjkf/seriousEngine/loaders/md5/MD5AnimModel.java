/*
 * Created by Davide Cossu (gjkf), 11/27/2016
 */

package me.gjkf.seriousEngine.loaders.md5;

import me.gjkf.seriousEngine.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a MD5 animated model.
 */

public class MD5AnimModel{

    /**
     * The header.
     */
    private MD5AnimHeader header;
    /**
     * The hierarchy.
     */
    private MD5Hierarchy hierarchy;
    /**
     * The info.
     */
    private MD5BoundInfo boundInfo;
    /**
     * The base frame.
     */
    private MD5BaseFrame baseFrame;
    /**
     * A list of frames.
     */
    private List<MD5Frame> frames;

    /**
     * Constructs a new MD5AnimModel.
     */
    public MD5AnimModel(){
        frames = new ArrayList<>();
    }

    /**
     * Getter for property 'header'.
     *
     * @return Value for property 'header'.
     */

    public MD5AnimHeader getHeader(){
        return header;
    }

    /**
     * Setter for property 'header'.
     *
     * @param header Value to set for property 'header'.
     */

    public void setHeader(MD5AnimHeader header){
        this.header = header;
    }

    /**
     * Getter for property 'hierarchy'.
     *
     * @return Value for property 'hierarchy'.
     */

    public MD5Hierarchy getHierarchy(){
        return hierarchy;
    }

    /**
     * Setter for property 'hierarchy'.
     *
     * @param hierarchy Value to set for property 'hierarchy'.
     */

    public void setHierarchy(MD5Hierarchy hierarchy){
        this.hierarchy = hierarchy;
    }

    /**
     * Getter for property 'boundInfo'.
     *
     * @return Value for property 'boundInfo'.
     */

    public MD5BoundInfo getBoundInfo(){
        return boundInfo;
    }

    /**
     * Setter for property 'boundInfo'.
     *
     * @param boundInfo Value to set for property 'boundInfo'.
     */

    public void setBoundInfo(MD5BoundInfo boundInfo){
        this.boundInfo = boundInfo;
    }

    /**
     * Getter for property 'baseFrame'.
     *
     * @return Value for property 'baseFrame'.
     */

    public MD5BaseFrame getBaseFrame(){
        return baseFrame;
    }

    /**
     * Setter for property 'baseFrame'.
     *
     * @param baseFrame Value to set for property 'baseFrame'.
     */

    public void setBaseFrame(MD5BaseFrame baseFrame){
        this.baseFrame = baseFrame;
    }

    /**
     * Getter for property 'frames'.
     *
     * @return Value for property 'frames'.
     */

    public List<MD5Frame> getFrames(){
        return frames;
    }

    /**
     * Setter for property 'frames'.
     *
     * @param frames Value to set for property 'frames'.
     */

    public void setFrames(List<MD5Frame> frames){
        this.frames = frames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder("MD5AnimModel: " + System.lineSeparator());
        str.append(getHeader()).append(System.lineSeparator());
        str.append(getHierarchy()).append(System.lineSeparator());
        str.append(getBoundInfo()).append(System.lineSeparator());
        str.append(getBaseFrame()).append(System.lineSeparator());

        for(MD5Frame frame : frames){
            str.append(frame).append(System.lineSeparator());
        }
        return str.toString();
    }

    public static MD5AnimModel parse(String animFile) throws Exception{
        List<String> lines = Utils.readAllLines(animFile);

        MD5AnimModel result = new MD5AnimModel();

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
        MD5AnimHeader header = MD5AnimHeader.parse(headerBlock);
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
     * Parses a block and sets the needed things.
     *
     * @param model The model.
     * @param blockId The ID (<tt>hierarchy</tt>, <tt>bounds</tt> or <tt>baseFrame</tt>)
     * @param blockBody
     *
     * @throws Exception
     */

    private static void parseBlock(MD5AnimModel model, String blockId, List<String> blockBody) throws Exception{
        switch(blockId){
            case "hierarchy":
                MD5Hierarchy hierarchy = MD5Hierarchy.parse(blockBody);
                model.setHierarchy(hierarchy);
                break;
            case "bounds":
                MD5BoundInfo boundInfo = MD5BoundInfo.parse(blockBody);
                model.setBoundInfo(boundInfo);
                break;
            case "baseframe":
                MD5BaseFrame baseFrame = MD5BaseFrame.parse(blockBody);
                model.setBaseFrame(baseFrame);
                break;
            default:
                if(blockId.startsWith("frame ")){
                    MD5Frame frame = MD5Frame.parse(blockId, blockBody);
                    model.getFrames().add(frame);
                }
                break;
        }
    }
}