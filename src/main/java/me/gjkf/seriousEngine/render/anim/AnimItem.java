/*
 * Created by Davide Cossu (gjkf), 11/27/2016
 */

package me.gjkf.seriousEngine.render.anim;

import me.gjkf.seriousEngine.items.Item;
import me.gjkf.seriousEngine.render.Mesh;
import org.joml.Matrix4f;

import java.util.List;

/**
 * Represents a {@link Item} that is animated.
 */

public class AnimItem extends Item{

    /**
     * The current frame.
     */
    private int currentFrame;
    /**
     * The list of frames.
     */
    private List<AnimatedFrame> frames;
    /**
     * The joint matrices.
     */
    private List<Matrix4f> invJointMatrices;

    public AnimItem(Mesh[] meshes, List<AnimatedFrame> frames, List<Matrix4f> invJointMatrices){
        super(meshes);
        this.frames = frames;
        this.invJointMatrices = invJointMatrices;
        currentFrame = 0;
    }

    /**
     * Getter for property 'frames'.
     *
     * @return Value for property 'frames'.
     */

    public List<AnimatedFrame> getFrames(){
        return frames;
    }

    /**
     * Setter for property 'frames'.
     *
     * @param frames Value to set for property 'frames'.
     */

    public void setFrames(List<AnimatedFrame> frames){
        this.frames = frames;
    }

    /**
     * Getter for property 'currentFrame'.
     *
     * @return Value for property 'currentFrame'.
     */

    public AnimatedFrame getCurrentFrame(){
        return this.frames.get(currentFrame);
    }

    /**
     * Getter for property 'nextFrame'.
     *
     * @return Value for property 'nextFrame'.
     */

    public AnimatedFrame getNextFrame(){
        int nextFrame = currentFrame + 1;
        if(nextFrame > frames.size() - 1){
            nextFrame = 0;
        }
        return this.frames.get(nextFrame);
    }

    /**
     * Goes to the next frame.
     */

    public void nextFrame(){
        int nextFrame = currentFrame + 1;
        if(nextFrame > frames.size() - 1){
            currentFrame = 0;
        }else{
            currentFrame = nextFrame;
        }
    }

    /**
     * Getter for property 'invJointMatrices'.
     *
     * @return Value for property 'invJointMatrices'.
     */

    public List<Matrix4f> getInvJointMatrices(){
        return invJointMatrices;
    }


}