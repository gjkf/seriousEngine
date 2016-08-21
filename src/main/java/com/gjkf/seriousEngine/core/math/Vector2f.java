/*
 * The MIT License (MIT)
 *
 * Copyright © 2015, Heiko Brumme
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gjkf.seriousEngine.core.math;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

/**
 * This class represents a (x,y)-Vector. GLSL equivalent to vec2.
 *
 * @author Heiko Brumme
 */
public class Vector2f {

    public float x;
    public float y;
    private float limit;

    /**
     * Creates a default 2-tuple vector with all values set to 0.
     */
    public Vector2f() {
        this.x = 0f;
        this.y = 0f;
    }

    /**
     * Creates a 2-tuple vector with specified values.
     *
     * @param x x value
     * @param y y value
     */
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates the squared length of the vector.
     *
     * @return Squared length of this vector
     */
    public float lengthSquared() {
        return x * x + y * y;
    }

    /**
     * Calculates the length of the vector.
     *
     * @return Length of this vector
     */
    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    /**
     * Limits the vector length
     *
     * @param limit The limit
     */

    public void limit(float limit){
        this.limit = limit;
    }

    /**
     * Normalizes the vector.
     *
     * @return Normalized vector
     */
    public Vector2f normalize() {
        float length = length();
        return divide(length);
    }

    /**
     * Adds this vector to another vector.
     *
     * @param other The other vector
     */
    public void add(Vector2f other) {
        if(other.x > 0){
            if(x+other.x < limit){
                this.x += other.x;
            }
        }else{
            if(x+other.x > -limit){
                this.x += other.x;
            }
        }

        if(other.y > 0){
            if(y+other.y < limit){
                this.y += other.y;
            }
        }else{
            if(y+other.y > -limit){
                this.y += other.y;
            }
        }
    }

    /**
     * Negates this vector.
     *
     * @return Negated vector
     */
    public Vector2f negate() {
        return scale(-1f);
    }

    /**
     * Subtracts this vector from another vector.
     *
     * @param other The other vector
     */
    public void subtract(Vector2f other) {
        this.add(other.negate());
    }

    /**
     * Multiplies a vector by a scalar.
     *
     * @param scalar Scalar to multiply
     *
     * @return Scalar product of this * scalar
     */
    public Vector2f scale(float scalar) {
        float x = this.x * scalar;
        float y = this.y * scalar;
        return new Vector2f(x, y);
    }

    /**
     * Divides a vector by a scalar.
     *
     * @param scalar Scalar to multiply
     *
     * @return Scalar quotient of this / scalar
     */
    public Vector2f divide(float scalar) {
        return scale(1f / scalar);
    }

    /**
     * Calculates the dot product of this vector with another vector.
     *
     * @param other The other vector
     *
     * @return Dot product of this * other
     */
    public float dot(Vector2f other) {
        return this.x * other.x + this.y * other.y;
    }

    /**
     * Calculates a linear interpolation between this vector with another
     * vector.
     *
     * @param other The other vector
     * @param alpha The alpha value, must be between 0.0 and 1.0
     */
    public void lerp(Vector2f other, float alpha) {
        this.scale(1f - alpha).add(other.scale(alpha));
    }

    /**
     * Returns the Buffer representation of this vector.
     *
     * @return Vector as FloatBuffer
     */
    public FloatBuffer getBuffer() {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(2);
        buffer.put(x).put(y);
        buffer.flip();
        return buffer;
    }

}
