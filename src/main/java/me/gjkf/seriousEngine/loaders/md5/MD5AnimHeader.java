/*
 * Created by Davide Cossu (gjkf), 11/27/2016
 */

package me.gjkf.seriousEngine.loaders.md5;

import java.util.List;

/**
 * Represents the header of a MD5 file.
 */

public class MD5AnimHeader{

    /**
     * The version of MD5.
     */
    private String version;
    /**
     * Any command line arguments.
     */
    private String commandLine;
    /**
     * The number of frames.
     */
    private int numFrames;
    /**
     * The number of joints.
     */
    private int numJoints;
    /**
     * The frame rate.
     */
    private int frameRate;
    /**
     * The number of animated components.
     */
    private int numAnimatedComponents;

    /**
     * Getter for property 'version'.
     *
     * @return Value for property 'version'.
     */

    public String getVersion(){
        return version;
    }

    /**
     * Setter for property 'version'.
     *
     * @param version Value to set for property 'version'.
     */

    public void setVersion(String version){
        this.version = version;
    }

    /**
     * Getter for property 'commandLine'.
     *
     * @return Value for property 'commandLine'.
     */

    public String getCommandLine(){
        return commandLine;
    }

    /**
     * Setter for property 'commandLine'.
     *
     * @param commandLine Value to set for property 'commandLine'.
     */

    public void setCommandLine(String commandLine){
        this.commandLine = commandLine;
    }

    /**
     * Getter for property 'numFrames'.
     *
     * @return Value for property 'numFrames'.
     */

    public int getNumFrames(){
        return numFrames;
    }

    /**
     * Setter for property 'numFrames'.
     *
     * @param numFrames Value to set for property 'numFrames'.
     */

    public void setNumFrames(int numFrames){
        this.numFrames = numFrames;
    }

    /**
     * Getter for property 'numJoints'.
     *
     * @return Value for property 'numJoints'.
     */

    public int getNumJoints(){
        return numJoints;
    }

    /**
     * Setter for property 'numJoints'.
     *
     * @param numJoints Value to set for property 'numJoints'.
     */

    public void setNumJoints(int numJoints){
        this.numJoints = numJoints;
    }

    /**
     * Getter for property 'frameRate'.
     *
     * @return Value for property 'frameRate'.
     */

    public int getFrameRate(){
        return frameRate;
    }

    /**
     * Setter for property 'frameRate'.
     *
     * @param frameRate Value to set for property 'frameRate'.
     */

    public void setFrameRate(int frameRate){
        this.frameRate = frameRate;
    }

    /**
     * Getter for property 'numAnimatedComponents'.
     *
     * @return Value for property 'numAnimatedComponents'.
     */

    public int getNumAnimatedComponents(){
        return numAnimatedComponents;
    }

    /**
     * Setter for property 'numAnimatedComponents'.
     *
     * @param numAnimatedComponents Value to set for property 'numAnimatedComponents'.
     */

    public void setNumAnimatedComponents(int numAnimatedComponents){
        this.numAnimatedComponents = numAnimatedComponents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(){
        return "animHeader: [version: " + version + ", commandLine: " + commandLine +
                ", numFrames: " + numFrames + ", numJoints: " + numJoints +
                ", frameRate: " + frameRate + ", numAnimatedComponents:" + numAnimatedComponents + "]";
    }

    /**
     * Parses the lines to return a new {@link MD5AnimHeader} object.
     *
     * @param lines The lines.
     *
     * @return The new object.
     *
     * @throws Exception If anythings went wrong.
     */

    public static MD5AnimHeader parse(List<String> lines) throws Exception{
        MD5AnimHeader header = new MD5AnimHeader();

        int numLines = lines != null ? lines.size() : 0;
        if(numLines == 0){
            throw new Exception("Cannot parse empty file");
        }

        boolean finishHeader = false;
        for(int i = 0; i < numLines && !finishHeader; i++){
            String line = lines.get(i);
            String[] tokens = line.split("\\s+");
            int numTokens = tokens != null ? tokens.length : 0;
            if(numTokens > 1){
                String paramName = tokens[0];
                String paramValue = tokens[1];

                switch(paramName){
                    case "MD5Version":
                        header.setVersion(paramValue);
                        break;
                    case "commandline":
                        header.setCommandLine(paramValue);
                        break;
                    case "numFrames":
                        header.setNumFrames(Integer.parseInt(paramValue));
                        break;
                    case "numJoints":
                        header.setNumJoints(Integer.parseInt(paramValue));
                        break;
                    case "frameRate":
                        header.setFrameRate(Integer.parseInt(paramValue));
                        break;
                    case "numAnimatedComponents":
                        header.setNumAnimatedComponents(Integer.parseInt(paramValue));
                        break;
                    case "hierarchy":
                        finishHeader = true;
                        break;
                }
            }
        }
        return header;
    }
}