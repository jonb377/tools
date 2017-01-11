package com.bolin.ui;

import java.util.BitSet;

/**
 * Created by jonb3_000 on 7/19/2016.
 */
public class KeyManager {

    private BitSet keys;

    public KeyManager() {
        keys = new BitSet(256);
    }

    /**
     * Tells if the key with char c is pressed.
     * @param c The key char.
     * @return True if the key is pressed, false otherwise.
     */
    public boolean isPressed(char c) {
        return keys.get(c);
    }

    /**
     * Sets the key with char c to be true.
     * @param c The key char to be set.
     */
    public void press(char c) {
        keys.set(c);
    }

    /**
     * Sets the key with char c to be false.
     * @param c The key char to be reset.
     */
    public void release(char c) {
        keys.clear(c);
    }

}
