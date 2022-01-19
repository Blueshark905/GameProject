package com.parkhon.dabyss.game.screen.stages;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.parkhon.dabyss.game.map.layer.objects.ParallaxObject;
import com.parkhon.dabyss.game.system.camera.BackgroundCamera;

import java.util.ArrayList;

public class StaticBackgroundLayer extends Stage
{
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Background
    private Image image;
    private Texture imageTexture;
    private BackgroundCamera camera;
    private SpriteBatch spriteBatch;

    //Parallax objects
    private ArrayList<ParallaxObject> parllaxObjects;
    //Gamecam data
    private float gameCamX;
    private float gameCamY;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    public StaticBackgroundLayer(BackgroundCamera camera, SpriteBatch spriteBatch)
    {
        super(camera.getViewport(), spriteBatch);
        //Setting variables
        this.camera = camera;
        this.spriteBatch = spriteBatch;
        //Initializing parallaxObjects
        parllaxObjects = new ArrayList<>();
        //Safety fallbacks
        gameCamX = 0.0f;
        gameCamY = 0.0f;
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------


    @Override
    public void act(float delta) {
        for(ParallaxObject parallax : parllaxObjects)
        {
            parallax.updateGameCamLocation(gameCamX, gameCamY);
        }
        super.act(delta);
    }

    @Override
    public void draw() {
        spriteBatch.setProjectionMatrix(camera.getCombinedMatrices());
        super.draw();
    }

    @Override
    public boolean keyDown(int keyCode) {
        boolean handled = false;
        return handled;
    }

    @Override
    public boolean keyUp(int keyCode) {
        boolean handled = false;
        return handled;
    }



    @Override
    public void dispose() {
        for(ParallaxObject parallax : parllaxObjects)
        {
            parallax.dispose();
        }
        imageTexture.dispose();
        super.dispose();
    }

    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public void show()
    {
        //Creating the image
        //image = new Image(new Texture("lv_1_bg_1.png"));
        //Game start
        camera.start();
        addActor(camera);
        this.setKeyboardFocus(camera);
        //Parallax objects
        //dukeParallax = new ParallaxObject(new Texture("java-duke-small.png"),1500,1500, 1);
        //parllaxObjects.add(dukeParallax);
        //addActor(dukeParallax);
    }

    public void resize(int width, int height)
    {
        camera.resize(width, height);
    }

    public void updateGameCamPosition(float x, float y)
    {
        gameCamX = x;
        gameCamY = y;
    }

    //For subclassing
    public void addParllaxObject(ParallaxObject parallaxObject)
    {
        parllaxObjects.add(parallaxObject);
        addActor(parallaxObject);
    }

    public void setImageTexture(Texture texture) {
        //For creating different levels with OOP
        this.imageTexture = texture;
        this.image = new Image(texture);
        this.image.setZIndex(1);
        addActor(image);
    }

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
