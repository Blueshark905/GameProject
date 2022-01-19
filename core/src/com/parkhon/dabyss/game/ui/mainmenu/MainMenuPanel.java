package com.parkhon.dabyss.game.ui.mainmenu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MainMenuPanel extends Table
{
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Graphic
    Texture texture;
    TextureRegion textureRegion;

    //Object


    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public MainMenuPanel()
    {
        //Initializing the actor
        setPosition(0,0,0);
        texture = new Texture("ui-mainmenu-panel.png");
        textureRegion = new TextureRegion(texture);
        setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(
                textureRegion, this.getX(), this.getY(),
                texture.getWidth()/2, texture.getHeight()/2,
                texture.getWidth(), texture.getHeight(),1, 1, this.getRotation());
        super.draw(batch, parentAlpha);
    }

    @Override
    public float getPrefWidth() {
        return super.getPrefWidth();
    }

    @Override
    public float getPrefHeight() {
        return super.getPrefHeight();
    }

    @Override
    public float getMinWidth() {
        return super.getMinWidth();
    }

    @Override
    public float getMinHeight() {
        return super.getMinHeight();
    }

    @Override
    public float getMaxWidth() {
        return super.getMaxWidth();
    }

    @Override
    public float getMaxHeight() {
        return super.getMaxHeight();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }


    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
