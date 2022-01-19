package com.parkhon.dabyss.game.entity.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Duke extends Actor
{
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    private Vector3 velocity;
    private Texture texture;
    private TextureRegion textureRegion;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    public Duke()
    {
        setPosition(0,0,0);
        velocity = new Vector3(0, 0, 0);
        texture = new Texture("java-duke-small.png");
        textureRegion = new TextureRegion(texture);
        setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
    }

    public Duke(float x, float y, int z)
    {
        setPosition(x, y, z);
        velocity = new Vector3(0,0,0);
        texture = new Texture("java-duke-small.png");
        setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------


    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        batch.draw(
                textureRegion, this.getX(), this.getY(),
                texture.getWidth()/2, texture.getHeight()/2,
                texture.getWidth(), texture.getHeight(),1, 1, this.getRotation());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return super.hit(x, y, touchable);
    }

    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    public Vector3 getVelocity() {
        return velocity;
    }
    public Texture getTexture() {
        return texture;
    }


    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
