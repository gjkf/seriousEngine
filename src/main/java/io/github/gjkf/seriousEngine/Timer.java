/*
 * Created by Davide Cossu (gjkf), 11/1/2016
 */
package io.github.gjkf.seriousEngine;

public class Timer{

    private double lastLoopTime;

    public void init(){
        lastLoopTime = getTime();
    }

    public double getTime(){
        return System.nanoTime() / 1000_000_000.0;
    }

    public float getElapsedTime(){
        double time = getTime();
        float elapsedTime = (float) (time - lastLoopTime);
        lastLoopTime = time;
        return elapsedTime;
    }

    public double getLastLoopTime(){
        return lastLoopTime;
    }
}