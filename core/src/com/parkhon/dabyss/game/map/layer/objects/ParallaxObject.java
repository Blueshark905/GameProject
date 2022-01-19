package com.parkhon.dabyss.game.map.layer.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ParallaxObject extends Actor
{
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    //Object details
    private Texture texture;
    private TextureRegion textureRegion;
    //Parallax precise movement
    private float movementRatioToCam;
    private float offsetX;
    private float offsetY;
    private float scale;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public ParallaxObject(Texture texture, float movementRatioToCam, float x, float y, int z, float scale)
    {
        //Setting values
        this.texture = texture;
        this.textureRegion = new TextureRegion(this.texture);
        this.setPosition(x, y);
        this.setZIndex(z);
        this.scale = scale;

        //Parallax precise movement
        this.movementRatioToCam = movementRatioToCam;
        offsetX = x;
        offsetY = y;
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(
                textureRegion, this.getX(), this.getY(),
                texture.getWidth()/2, texture.getHeight()/2,
                texture.getWidth(), texture.getHeight(),scale, scale, this.getRotation());
    }


    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    public void updateGameCamLocation(float camX, float camY)
    {
        this.setPosition(offsetX - (camX * movementRatioToCam), offsetY - (camY * movementRatioToCam));
    }

    public void dispose()
    {
        texture.dispose();
    }

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
