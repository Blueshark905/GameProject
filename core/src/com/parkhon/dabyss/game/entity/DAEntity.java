package com.parkhon.dabyss.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public interface DAEntity
{
    /*
    All elements in a Dreadnaught Abyss game map are essentially entities. They keep track of their own logic, AI and
    position data. Game entities contain components, which are general systems that alter their behavior within a game
    system.
     */

    public Vector3 getPosition();
    public void setPosition(Vector3 position);
    public Vector3 getVelocity();
    public int getRotation();
    public void setRotation(int rotation);
    public Texture getTexture();
}
