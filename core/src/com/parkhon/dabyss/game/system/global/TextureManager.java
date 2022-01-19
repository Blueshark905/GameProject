package com.parkhon.dabyss.game.system.global;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureManager
{
    //Singleton

    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Singleton
    private static TextureManager textureManager = new TextureManager();

    //Textures

    //Test Projectile
    private Texture testProjectileTex;
    private TextureRegion testProjectileTR;

    //Test Ship
    private Texture testShipTex;
    private TextureRegion testShipTR;

    //Test Turret
    private Texture testTurretTex;
    private TextureRegion testTurretTR;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    private TextureManager()
    {
        //Textures
        //Test Projectile
        testProjectileTex = new Texture("aim_assist.png");
        testProjectileTR = new TextureRegion(testProjectileTex);

        //Test Ship
        testShipTex = new Texture("ship7.png");
        testShipTR = new TextureRegion(testShipTex);

        //Test Turret
        testTurretTex = new Texture("test_turret_body.png");
        testTurretTR = new TextureRegion(testTurretTex);
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Singleton
    public static TextureManager getTextureManager() {
        return textureManager;
    }

    //Access to textures

    //Debug aim assist


    //Test Projectile
    public Texture getTestProjectileTex() {
        return testProjectileTex;
    }
    public TextureRegion getTestProjectileTR() {
        return testProjectileTR;
    }

    //Test Ship
    public Texture getTestShipTex() {
        return testShipTex;
    }
    public TextureRegion getTestShipTR() {
        return testShipTR;
    }

    //Test Turret
    public Texture getTestTurretTex() {
        return testTurretTex;
    }
    public TextureRegion getTestTurretTR() {
        return testTurretTR;
    }


    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
