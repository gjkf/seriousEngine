/*
 * Created by Davide Cossu (gjkf), 11/27/2016
 */

package me.gjkf.seriousEngine.loaders.md5;

import me.gjkf.seriousEngine.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a frame of a MD5 model.
 */

public class MD5Frame{

    /**
     * The ID.
     */
    private int id;
    /**
     * The data.
     */
    private float[] frameData;

    /**
     * Getter for property 'id'.
     *
     * @return Value for property 'id'.
     */

    public int getId(){
        return id;
    }

    /**
     * Setter for property 'id'.
     *
     * @param id Value to set for property 'id'.
     */

    public void setId(int id){
        this.id = id;
    }

    /**
     * Getter for property 'frameData'.
     *
     * @return Value for property 'frameData'.
     */

    public float[] getFrameData(){
        return frameData;
    }

    /**
     * Setter for property 'frameData'.
     *
     * @param frameData Value to set for property 'frameData'.
     */

    public void setFrameData(float[] frameData){
        this.frameData = frameData;
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder("frame " + id + " [data: " + System.lineSeparator());
        for(float frameData : frameData){
            str.append(frameData).append(System.lineSeparator());
        }
        str.append("]").append(System.lineSeparator());
        return str.toString();
    }

    /**
     * Parses the block to get the frame.
     *
     * @param blockId The ID of the block.
     * @param blockBody The body.
     *
     * @return The frame.
     *
     * @throws Exception If the frame was bad defined.
     */

    public static MD5Frame parse(String blockId, List<String> blockBody) throws Exception{
        MD5Frame result = new MD5Frame();
        String[] tokens = blockId.trim().split("\\s+");
        if(tokens.length >= 2){
            result.setId(Integer.parseInt(tokens[1]));
        }else{
            throw new Exception("Wrong frame definition: " + blockId);
        }

        List<Float> data = new ArrayList<>();
        for(String line : blockBody){
            List<Float> lineData = parseLine(line);
            if(lineData != null){
                data.addAll(lineData);
            }
        }
        float[] dataArr = Utils.listToArray(data);
        result.setFrameData(dataArr);

        return result;
    }

    /**
     * Parses a line and gets a list of floats.
     *
     * @param line The line.
     *
     * @return The list.
     */

    private static List<Float> parseLine(String line){
        String[] tokens = line.trim().split("\\s+");
        List<Float> data = new ArrayList<>();
        for(String token : tokens){
            data.add(Float.parseFloat(token));
        }
        return data;
    }
}