/*
 * Created by Davide Cossu (gjkf), 12/3/2016
 */

package io.github.gjkf.seriousEngine.render.particles;

import io.github.gjkf.seriousEngine.items.Item;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is the most basic {@link IParticleEmitter}. It's just a flow of particles.
 * <p>Can define a direction, a speed and a scale of each particle.</p>
 */

public class FlowParticleEmitter implements IParticleEmitter{

    /**
     * The maximum amount of active particles.
     */
    private int maxParticles;
    /**
     * If this emitter is alive.
     */
    private boolean active;
    /**
     * The list of particles.
     */
    private final List<Item> particles;
    /**
     * The base particle.
     */
    private final Particle baseParticle;
    /**
     * How often a particle should be created.
     */
    private long creationPeriodMillis;
    /**
     * The last time a particle was created.
     */
    private long lastCreationTime;
    /**
     * The speed of the particle. Also affects the emitter range.
     */
    private float speedRndRange;
    /**
     * The position of the particle. Also affects the emitter range.
     */
    private float positionRndRange;
    /**
     * The scale of the particle. Also affects the emitter range.
     */
    private float scaleRndRange;
    /**
     * The animation range.
     */
    private long animRange;

    /**
     * Constructs a new FlowParticleEmitter
     *
     * @param baseParticle         The base particle from which the other origin.
     * @param maxParticles         The maximum amount of particles.
     * @param creationPeriodMillis The time in milliseconds from each creation.
     */

    public FlowParticleEmitter(Particle baseParticle, int maxParticles, long creationPeriodMillis){
        particles = new ArrayList<>();
        this.baseParticle = baseParticle;
        this.maxParticles = maxParticles;
        this.active = false;
        this.lastCreationTime = 0;
        this.creationPeriodMillis = creationPeriodMillis;
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public Particle getBaseParticle(){
        return baseParticle;
    }

    /**
     * Getter for property 'creationPeriodMillis'.
     *
     * @return Value for property 'creationPeriodMillis'.
     */

    public long getCreationPeriodMillis(){
        return creationPeriodMillis;
    }

    /**
     * Getter for property 'maxParticles'.
     *
     * @return Value for property 'maxParticles'.
     */

    public int getMaxParticles(){
        return maxParticles;
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public List<Item> getParticles(){
        return particles;
    }

    /**
     * Getter for property 'positionRndRange'.
     *
     * @return Value for property 'positionRndRange'.
     */

    public float getPositionRndRange(){
        return positionRndRange;
    }

    /**
     * Getter for property 'scaleRndRange'.
     *
     * @return Value for property 'scaleRndRange'.
     */

    public float getScaleRndRange(){
        return scaleRndRange;
    }

    /**
     * Getter for property 'speedRndRange'.
     *
     * @return Value for property 'speedRndRange'.
     */

    public float getSpeedRndRange(){
        return speedRndRange;
    }

    /**
     * Setter for property 'animRange'.
     *
     * @param animRange Value to set for property 'animRange'.
     */

    public void setAnimRange(long animRange){
        this.animRange = animRange;
    }

    /**
     * Setter for property 'creationPeriodMillis'.
     *
     * @param creationPeriodMillis Value to set for property 'creationPeriodMillis'.
     */

    public void setCreationPeriodMillis(long creationPeriodMillis){
        this.creationPeriodMillis = creationPeriodMillis;
    }

    /**
     * Setter for property 'maxParticles'.
     *
     * @param maxParticles Value to set for property 'maxParticles'.
     */

    public void setMaxParticles(int maxParticles){
        this.maxParticles = maxParticles;
    }

    /**
     * Setter for property 'positionRndRange'.
     *
     * @param positionRndRange Value to set for property 'positionRndRange'.
     */

    public void setPositionRndRange(float positionRndRange){
        this.positionRndRange = positionRndRange;
    }

    /**
     * Setter for property 'scaleRndRange'.
     *
     * @param scaleRndRange Value to set for property 'scaleRndRange'.
     */

    public void setScaleRndRange(float scaleRndRange){
        this.scaleRndRange = scaleRndRange;
    }

    /**
     * Getter for property 'active'.
     *
     * @return Value for property 'active'.
     */

    public boolean isActive(){
        return active;
    }

    /**
     * Setter for property 'active'.
     *
     * @param active Value to set for property 'active'.
     */

    public void setActive(boolean active){
        this.active = active;
    }

    /**
     * Setter for property 'speedRndRange'.
     *
     * @param speedRndRange Value to set for property 'speedRndRange'.
     */

    public void setSpeedRndRange(float speedRndRange){
        this.speedRndRange = speedRndRange;
    }

    /**
     * Updates the emitter with the given time delta.
     *
     * @param elapsedTime The time delta.
     */

    public void update(long elapsedTime){
        long now = System.currentTimeMillis();
        if(lastCreationTime == 0){
            lastCreationTime = now;
        }
        Iterator<? extends Item> it = particles.iterator();
        while(it.hasNext()){
            Particle particle = (Particle) it.next();
            if(particle.updateTtl(elapsedTime) < 0){
                it.remove();
            }else{
                updatePosition(particle, elapsedTime);
            }
        }

        int length = this.getParticles().size();
        if(now - lastCreationTime >= this.creationPeriodMillis && length < maxParticles){
            createParticle();
            this.lastCreationTime = now;
        }
    }

    /**
     * Creates a new particle.
     * <p>It assigns random speed and position.</p>
     */

    private void createParticle(){
        Particle particle = new Particle(this.getBaseParticle());
        // Add a little bit of randomness of the particle
        float sign = Math.random() > 0.5d ? -1.0f : 1.0f;
        float speedInc = sign * (float) Math.random() * this.speedRndRange;
        float posInc = sign * (float) Math.random() * this.positionRndRange;
        float scaleInc = sign * (float) Math.random() * this.scaleRndRange;
        long updateAnimInc = (long) sign * (long) (Math.random() * (float) this.animRange);
        particle.getPosition().add(posInc, posInc, posInc);
        particle.getSpeed().add(speedInc, speedInc, speedInc);
        particle.setScale(particle.getScale() + scaleInc);
        particle.setUpdateTextureMills(particle.getUpdateTextureMillis() + updateAnimInc);
        particles.add(particle);
    }

    /**
     * Updates a particle position
     *
     * @param particle    The particle to update
     * @param elapsedTime Elapsed time in milliseconds
     */

    public void updatePosition(Particle particle, long elapsedTime){
        Vector3f speed = particle.getSpeed();
        float delta = elapsedTime / 1000.0f;
        float dx = speed.x * delta;
        float dy = speed.y * delta;
        float dz = speed.z * delta;
        Vector3f pos = particle.getPosition();
        particle.setPosition(pos.x + dx, pos.y + dy, pos.z + dz);
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public void cleanup(){
        for(Item particle : getParticles()){
            particle.cleanup();
        }
    }

}