/*
 * Created by Davide Cossu (gjkf), 11/27/2016
 */

package me.gjkf.seriousEngine.loaders.md5;


import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the information of a joint in a MD5 model.
 */

public class MD5JointInfo{

    /**
     * The joint data.
     */
    private List<MD5JointData> joints;

    /**
     * Getter for property 'joints'.
     *
     * @return Value for property 'joints'.
     */

    public List<MD5JointData> getJoints(){
        return joints;
    }

    /**
     * Setter for property 'joints'.
     *
     * @param joints Value to set for property 'joints'.
     */

    public void setJoints(List<MD5JointData> joints){
        this.joints = joints;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder("joints [" + System.lineSeparator());
        for(MD5JointData joint : joints){
            str.append(joint).append(System.lineSeparator());
        }
        str.append("]").append(System.lineSeparator());
        return str.toString();
    }

    /**
     * Parses the lines and gets the info.
     *
     * @param blockBody The lines.
     *
     * @return The info.
     */

    public static MD5JointInfo parse(List<String> blockBody){
        MD5JointInfo result = new MD5JointInfo();
        List<MD5JointData> joints = new ArrayList<>();
        for(String line : blockBody){
            MD5JointData jointData = MD5JointData.parseLine(line);
            if(jointData != null){
                joints.add(jointData);
            }
        }
        result.setJoints(joints);
        return result;
    }

    /**
     * The joint data.
     */

    public static class MD5JointData{

        /**
         * The RegEx of the parent index.
         */
        private static final String PARENT_INDEX_REGEXP = "([-]?\\d+)";
        /**
         * The RegEx of the name.
         */
        private static final String NAME_REGEXP = "\\\"([^\\\"]+)\\\"";
        /**
         * The joint RegEx.
         */
        private static final String JOINT_REGEXP = "\\s*" + NAME_REGEXP + "\\s*" + PARENT_INDEX_REGEXP + "\\s*"
                + MD5Utils.VECTOR3_REGEXP + "\\s*" + MD5Utils.VECTOR3_REGEXP + ".*";
        /**
         * The pattern of the {@link #JOINT_REGEXP}.
         */
        private static final Pattern PATTERN_JOINT = Pattern.compile(JOINT_REGEXP);
        /**
         * The name.
         */
        private String name;
        /**
         * The parent index.
         */
        private int parentIndex;
        /**
         * The position.
         */
        private Vector3f position;
        /**
         * The orientation.
         */
        private Quaternionf orientation;

        /**
         * Getter for property 'name'.
         *
         * @return Value for property 'name'.
         */

        public String getName(){
            return name;
        }

        /**
         * Setter for property 'name'.
         *
         * @param name Value to set for property 'name'.
         */

        public void setName(String name){
            this.name = name;
        }

        /**
         * Getter for property 'parentIndex'.
         *
         * @return Value for property 'parentIndex'.
         */

        public int getParentIndex(){
            return parentIndex;
        }

        /**
         * Setter for property 'parentIndex'.
         *
         * @param parentIndex Value to set for property 'parentIndex'.
         */

        public void setParentIndex(int parentIndex){
            this.parentIndex = parentIndex;
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
            return "[name: " + name + ", parentIndex: " + parentIndex + ", position: " + position + ", orientation: " + orientation + "]";
        }

        /**
         * Parses a line and gets the joint data.
         *
         * @param line The line.
         *
         * @return The joint data.
         */

        public static MD5JointData parseLine(String line){
            MD5JointData result = null;
            Matcher matcher = PATTERN_JOINT.matcher(line);
            if(matcher.matches()){
                result = new MD5JointData();
                result.setName(matcher.group(1));
                result.setParentIndex(Integer.parseInt(matcher.group(2)));
                float x = Float.parseFloat(matcher.group(3));
                float y = Float.parseFloat(matcher.group(4));
                float z = Float.parseFloat(matcher.group(5));
                result.setPosition(new Vector3f(x, y, z));

                x = Float.parseFloat(matcher.group(6));
                y = Float.parseFloat(matcher.group(7));
                z = Float.parseFloat(matcher.group(8));
                result.setOrientation(new Vector3f(x, y, z));
            }
            return result;
        }
    }
}