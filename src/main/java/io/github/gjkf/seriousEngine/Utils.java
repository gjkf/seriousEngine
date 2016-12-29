/*
 * Created by Davide Cossu (gjkf), 11/1/2016
 */
package io.github.gjkf.seriousEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Misc util class
 */

public class Utils{

    /**
     * Loads and returns a resource at the given path.
     *
     * @param fileName The path of the resource.
     *
     * @return The read resource.
     */

    public static String loadResource(String fileName){
        StringBuilder builder = new StringBuilder();
        try(InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in))){
            String line;
            while((line = reader.readLine()) != null){
                builder.append(line).append("\n");
            }
        }catch(IOException ex){
            throw new RuntimeException("Failed to load a file" + System.lineSeparator() + ex.getMessage());
        }

        return builder.toString();
    }

    /**
     * Reads all the lines of a file and returns them as a list.
     *
     * @param fileName The path of the file.
     *
     * @return The list containing each line.
     *
     * @throws Exception If anything went wrong.
     */

    public static List<String> readAllLines(String fileName) throws Exception{
        List<String> list = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(Utils.class.getClass().getResourceAsStream(fileName)))){
            String line;
            while((line = br.readLine()) != null){
                list.add(line);
            }
        }
        return list;
    }

    /**
     * Transforms a list into an array.
     *
     * @param list The original list.
     *
     * @return The new array.
     */

    public static float[] listToArray(List<Float> list){
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for(int i = 0; i < size; i++){
            floatArr[i] = list.get(i);
        }
        return floatArr;
    }

    /**
     * Gets an Integer list and returns an array.
     *
     * @param list The list.
     *
     * @return The array.
     */

    public static int[] listIntToArray(List<Integer> list){
        int[] result = list.stream().mapToInt((Integer v) -> v).toArray();
        return result;
    }

    /**
     * Checks if a resource exists.
     *
     * @param fileName The path.
     *
     * @return Whether it exists.
     */

    public static boolean existsResourceFile(String fileName){
        boolean result;
        try(InputStream is = Utils.class.getResourceAsStream(fileName)){
            result = is != null;
        }catch(Exception excp){
            result = false;
        }
        return result;
    }

}