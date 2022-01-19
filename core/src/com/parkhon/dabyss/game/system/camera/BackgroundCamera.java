package com.parkhon.dabyss.game.system.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BackgroundCamera extends Actor
{
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    private Viewport viewport;
    private OrthographicCamera camera;
    //Position
    //Game engine
    float dt; //Delta time
    //Ideal game screen proportions for the viewport to approximate
    private int idealWidth;
    private int idealHeight;
    private float zoomingFactor;
    //Background positioning
    Texture bgImage;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    public BackgroundCamera()
    {
        //Setting attributes
        //Camera Preferences
        zoomingFactor = 175;
        idealWidth = (int)(16 * zoomingFactor);
        idealHeight = (int)(9 * zoomingFactor);
        //Initializing
        //Creating the camera and viewports
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(idealWidth, idealHeight, camera);
        //Actor settings
        setVisible(false);
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------


    @Override
    public void act(float delta) {
        super.act(delta);
    }

    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public Viewport getViewport() {
        return viewport;
    }

    public Matrix4 getCombinedMatrices()
    {
        return viewport.getCamera().combined;
    }

    public void start()
    {
        viewport.apply();
    }

    public void resize(int width, int height)
    {
        viewport.update(width, height);
        viewport.getCamera().position.set(bgImage.getWidth()/2, bgImage.getHeight()/2, 0);
    }

    public void setBgImage(Texture bgImage) {
        this.bgImage = bgImage;
    }

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
