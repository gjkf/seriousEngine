/*
 * Created by Davide Cossu (gjkf), 11/27/2016
 */

package io.github.gjkf.seriousEngine.loaders.md5;

import java.util.List;

/**
 * The model header.
 */

public class MD5ModelHeader{

    /**
     * The header.
     */
    private String version;
    /**
     * The command line.
     */
    private String commandLine;
    /**
     * The number of joints.
     */
    private int numJoints;
    /**
     * The number of meshes.
     */
    private int numMeshes;

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
     * Getter for property 'numMeshes'.
     *
     * @return Value for property 'numMeshes'.
     */

    public int getNumMeshes(){
        return numMeshes;
    }

    /**
     * Setter for property 'numMeshes'.
     *
     * @param numMeshes Value to set for property 'numMeshes'.
     */

    public void setNumMeshes(int numMeshes){
        this.numMeshes = numMeshes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(){
        return "[version: " + version + ", commandLine: " + commandLine +
                ", numJoints: " + numJoints + ", numMeshes: " + numMeshes + "]";
    }

    /**
     * Parses the lines to get a header.
     *
     * @param lines The lines.
     *
     * @return The model header.
     *
     * @throws Exception The exception.
     */

    public static MD5ModelHeader parse(List<String> lines) throws Exception{
        MD5ModelHeader header = new MD5ModelHeader();
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
                    case "numJoints":
                        header.setNumJoints(Integer.parseInt(paramValue));
                        break;
                    case "numMeshes":
                        header.setNumMeshes(Integer.parseInt(paramValue));
                        break;
                    case "joints":
                        finishHeader = true;
                        break;
                }
            }
        }

        return header;
    }
}