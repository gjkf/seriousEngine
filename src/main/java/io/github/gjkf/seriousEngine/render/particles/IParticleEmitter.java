/*
 * Created by Davide Cossu (gjkf), 12/3/2016
 */

package io.github.gjkf.seriousEngine.render.particles;

import io.github.gjkf.seriousEngine.items.Item;

import java.util.List;

/**
 * Implement this interface to create your own <tt>Particle Emitter</tt>.
 * <p>An example can be found at {@link FlowParticleEmitter}.</p>
 */

public interface IParticleEmitter{

    /**
     * Clean up the resources.
     */
    void cleanup();

    /**
     * Gets the base particle. From this particle originate all the others.
     *
     * @return The base particle.
     */

    Particle getBaseParticle();

    /**
     * Gets the list of particles.
     *
     * @return The particles.
     */

    List<Item> getParticles();

}