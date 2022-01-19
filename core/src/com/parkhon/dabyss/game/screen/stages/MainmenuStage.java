package com.parkhon.dabyss.game.screen.stages;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.parkhon.dabyss.game.system.camera.MenuCamera;
import com.parkhon.dabyss.game.ui.mainmenu.MainMenuPanel;

public class MainmenuStage extends Stage
{
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Rendering
    private SpriteBatch spriteBatch;

    //Camera
    private MenuCamera camera;

    //Main Menu elements
    private MainMenuPanel mainMenuPanel;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public MainmenuStage(MenuCamera camera, SpriteBatch spriteBatch)
    {
        super(camera.getViewport(), spriteBatch);
        //Initializing necessities
        this.spriteBatch = spriteBatch;
        this.camera = camera;

        //Initializing menu elements
        this.mainMenuPanel = new MainMenuPanel();

        //Adding to the actor hierarchy
        addActor(mainMenuPanel);
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    @Override
    public void draw() {
        //Camara not controlling rendering for some reason
        spriteBatch.setProjectionMatrix(camera.getCombinedMatrices()); //Binding camera isometric scene to the sprite-batch
        super.draw();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }


    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public void show()
    {
        //Start the stage.
        //Starting the camera
        camera.start();
        addActor(camera);
        //Positioning elements on the menu world
        positionElements();
    }

    public void resize(int width, int height)
    {
        //Pass resizing instructions to the camera.
        camera.resize(width, height);
    }

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    private void positionElements()
    {
        //The menu panel that contains the main menu icons
        mainMenuPanel.setPosition(100, -mainMenuPanel.getHeight() / 2);
    }
}
