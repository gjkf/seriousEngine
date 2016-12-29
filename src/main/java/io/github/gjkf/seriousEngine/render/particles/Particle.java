/*
 * Created by Davide Cossu (gjkf), 12/3/2016
 */

package io.github.gjkf.seriousEngine.render.particles;

import io.github.gjkf.seriousEngine.items.Item;
import io.github.gjkf.seriousEngine.render.Mesh;
import io.github.gjkf.seriousEngine.render.Texture;
import org.joml.Vector3f;

/**
 * A particle. It is a object that is always oriented towards the camera.
 * <p>Has a set time to live and speed, can use a texture atlas.</p>
 */

public class Particle extends Item{

    /**
     * How many milliseconds from each texture update.
     */
    private long updateTextureMillis;
    /**
     * How many milliseconds has this particle had the current texture.
     */
    private long currentAnimTimeMillis;
    /**
     * The speed of the particle.
     */
    private Vector3f speed;
    /**
     * Time to live for particle in milliseconds.
     */
    private long ttl;
    /**
     * The number of frames in the texture atlas.
     */
    private int animFrames;

    /**
     * Constructs a new Particle.
     *
     * @param mesh                The mesh to use.
     * @param speed               The speed of the particle.
     * @param ttl                 The TimeToLive
     * @param updateTextureMillis How often the texture should be updated.
     */

    public Particle(Mesh mesh, Vector3f speed, long ttl, long updateTextureMillis){
        super(mesh);
        this.speed = new Vector3f(speed);
        this.ttl = ttl;
        this.updateTextureMillis = updateTextureMillis;
        this.currentAnimTimeMillis = 0;
        Texture texture = this.getMesh().getMaterial().getTexture();
        this.animFrames = texture.getNumCols() * texture.getNumRows();
    }

    /**
     * Copies a given particle.
     *
     * @param baseParticle The base particle
     */

    public Particle(Particle baseParticle){
        super(baseParticle.getMesh());
        Vector3f aux = baseParticle.getPosition();
        setPosition(aux.x, aux.y, aux.z);
        setRotation(baseParticle.getRotation());
        setScale(baseParticle.getScale());
        this.speed = new Vector3f(baseParticle.speed);
        this.ttl = baseParticle.geTtl();
        this.updateTextureMillis = baseParticle.getUpdateTextureMillis();
        this.currentAnimTimeMillis = 0;
        this.animFrames = baseParticle.getAnimFrames();
    }

    /**
     * Getter for property 'animFrames'.
     *
     * @return Value for property 'animFrames'.
     */

    public int getAnimFrames(){
        return animFrames;
    }

    /**
     * Getter for property 'speed'.
     *
     * @return Value for property 'speed'.
     */

    public Vector3f getSpeed(){
        return speed;
    }

    /**
     * Getter for property 'updateTextureMillis'.
     *
     * @return Value for property 'updateTextureMillis'.
     */

    public long getUpdateTextureMillis(){
        return updateTextureMillis;
    }

    /**
     * Setter for property 'speed'.
     *
     * @param speed Value to set for property 'speed'.
     */

    public void setSpeed(Vector3f speed){
        this.speed = speed;
    }

    /**
     * Getter for property 'ttl'.
     *
     * @return Value for property 'ttl'.
     */

    public long geTtl(){
        return ttl;
    }

    /**
     * Setter for property 'ttl'.
     *
     * @param ttl Value to set for property 'ttl'.
     */

    public void setTtl(long ttl){
        this.ttl = ttl;
    }

    /**
     * Setter for property 'updateTextureMills'.
     *
     * @param updateTextureMillis Value to set for property 'updateTextureMills'.
     */

    public void setUpdateTextureMills(long updateTextureMillis){
        this.updateTextureMillis = updateTextureMillis;
    }

    /**
     * Updates the Particle's TTL
     *
     * @param elapsedTime Elapsed Time in milliseconds
     *
     * @return The Particle's TTL
     */

    public long updateTtl(long elapsedTime){
        this.ttl -= elapsedTime;
        this.currentAnimTimeMillis += elapsedTime;
        if(this.currentAnimTimeMillis >= this.getUpdateTextureMillis() && this.animFrames > 0){
            this.currentAnimTimeMillis = 0;
            int pos = this.getTextPos();
            pos++;
            if(pos < this.animFrames){
                this.setTextPos(pos);
            }else{
                this.setTextPos(0);
            }
        }
        return this.ttl;
    }


}