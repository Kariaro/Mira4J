package com.sekwah.mira4j.config;

public class Vector2 {
    public float x;
    public float y;
    
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector2 clone() {
        return new Vector2(x, y);
    }
    
    public String toString() {
        return String.format("{ x=%.5f, y=%.5f }", x, y);
    }
}