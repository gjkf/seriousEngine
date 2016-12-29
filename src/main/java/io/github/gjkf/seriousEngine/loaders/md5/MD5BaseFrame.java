/*
 * Created by Davide Cossu (gjkf), 11/27/2016
 */

package io.github.gjkf.seriousEngine.loaders.md5;


import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class representing the base frame of a MD5 model.
 */

public class MD5BaseFrame{

    /**
     * The frame data list.
     */
    private List<MD5BaseFrameData> frameDataList;

    /**
     * Getter for property 'frameDataList'.
     *
     * @return Value for property 'frameDataList'.
     */

    public List<MD5BaseFrameData> getFrameDataList(){
        return frameDataList;
    }

    /**
     * Setter for property 'frameDataList'.
     *
     * @param frameDataList Value to set for property 'frameDataList'.
     */

    public void setFrameDataList(List<MD5BaseFrameData> frameDataList){
        this.frameDataList = frameDataList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder("base frame [" + System.lineSeparator());
        for(MD5BaseFrameData frameData : frameDataList){
            str.append(frameData).append(System.lineSeparator());
        }
        str.append("]").append(System.lineSeparator());
        return str.toString();
    }

    /**
     * Parses a block and gets the base frame.
     *
     * @param blockBody The body.
     *
     * @return The base frame.
     */

    public static MD5BaseFrame parse(List<String> blockBody){
        MD5BaseFrame result = new MD5BaseFrame();

        List<MD5BaseFrameData> frameInfoList = new ArrayList<>();
        result.setFrameDataList(frameInfoList);

        for(String line : blockBody){
            MD5BaseFrameData frameInfo = MD5BaseFrameData.parseLine(line);
            if(frameInfo != null){
                frameInfoList.add(frameInfo);
            }
        }

        return result;
    }

    /**
     * Inner class for basic data.
     */

    public static class MD5BaseFrameData{

        /**
         * A patter to find the base frame.
         */
        private static final Pattern PATTERN_BASEFRAME = Pattern.compile("\\s*" + MD5Utils.VECTOR3_REGEXP + "\\s*" + MD5Utils.VECTOR3_REGEXP + ".*");
        /**
         * The position.
         */
        private Vector3f position;
        /**
         * The orientation.
         */
        private Quaternionf orientation;

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
         * Getter for property 'orientation'.
         *
         * @return Value for property 'orientation'.
         */

        public Quaternionf getOrientation(){
            return orientation;
        }

        /**
         * Setter for property 'orientation'.
         *
         * @param vec Value to set for property 'orientation'.
         */

        public void setOrientation(Vector3f vec){
            this.orientation = MD5Utils.calculateQuaternion(vec);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString(){
            return "[position: " + position + ", orientation: " + orientation + "]";
        }

        /**
         * Parses a line and gets the data.
         *
         * @param line The line.
         *
         * @return The data.
         */

        public static MD5BaseFrameData parseLine(String line){
            Matcher matcher = PATTERN_BASEFRAME.matcher(line);
            MD5BaseFrameData result = null;
            if(matcher.matches()){
                result = new MD5BaseFrameData();
                float x = Float.parseFloat(matcher.group(1));
                float y = Float.parseFloat(matcher.group(2));
                float z = Float.parseFloat(matcher.group(3));
                result.setPosition(new Vector3f(x, y, z));

                x = Float.parseFloat(matcher.group(4));
                y = Float.parseFloat(matcher.group(5));
                z = Float.parseFloat(matcher.group(6));
                result.setOrientation(new Vector3f(x, y, z));
            }

            return result;
        }
    }
}