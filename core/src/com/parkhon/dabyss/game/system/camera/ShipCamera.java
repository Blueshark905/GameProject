package com.parkhon.dabyss.game.system.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.parkhon.dabyss.game.entity.character.ship.ShipBase;

public class ShipCamera extends Actor
{
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    private Viewport viewport;
    private OrthographicCamera camera;
    private float dt; //delta time
    //Ideal game screen proportions for the viewport to approximate
    private int idealWidth;
    private int idealHeight;
    private float zoomingFactor;
    //Ship tracking
    ShipBase ship;
    //Target reticle displacement
    private float displacementX;
    private float displacementY;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    public ShipCamera()
    {
        setPosition(0,0);
        //
        dt = 1;
        //Camera Preferences
        zoomingFactor = 150;
        idealWidth = (int)(16 * zoomingFactor);
        idealHeight = (int)(9 * zoomingFactor);
        displacementX = 0.0f;
        displacementY = 0.0f;
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
        if(ship != null){
            this.dt = delta;
            this.setPosition(ship.getX() + ship.getWidth() / 2 + (displacementX),
                    ship.getY() + ship.getHeight() / 2 + (displacementY));
            camera.position.set(this.getX(), this.getY(), 0);
        }
        super.act(delta);
    }



    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------


    public ShipBase getShip() {
        return ship;
    }

    public void setShip(ShipBase ship) {
        this.ship = ship;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public Matrix4 getCombinedMatrices()
    {
        return viewport.getCamera().combined;
    }

    public void start()
    {
        //
        viewport.apply();
    }

    public void resize(int width, int height)
    {
        viewport.update(width, height);
    }

    public void setDisplacementX(float displacementX) {
        this.displacementX = displacementX;
    }

    public void setDisplacementY(float displacementY) {
        this.displacementY = displacementY;
    }

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
