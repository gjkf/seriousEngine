/*
 * Created by Davide Cossu (gjkf), 11/13/2016
 */

package io.github.gjkf.seriousEngine;

import io.github.gjkf.seriousEngine.items.Item;

/**
 * An interface that should be implemented when creating a new HUD.
 */

public interface IHud{

    /**
     * Getter for property 'items'.
     *
     * @return Value for property 'items'.
     */

    Item[] getItems();

    /**
     * Cleans up the item's resources.
     */

    default void cleanup(){
        Item[] items = getItems();
        for(Item item : items){
            item.getMesh().cleanUp();
        }
    }

}