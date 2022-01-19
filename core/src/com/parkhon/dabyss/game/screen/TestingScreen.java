package com.parkhon.dabyss.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.parkhon.dabyss.game.screen.stages.StaticBackgroundLayer;
import com.parkhon.dabyss.game.map.layer.instanced.Level1Background;
import com.parkhon.dabyss.game.screen.stages.GameWorldStage;
import com.parkhon.dabyss.game.system.camera.BackgroundCamera;
import com.parkhon.dabyss.game.system.camera.ShipCamera;
import com.parkhon.dabyss.game.system.global.GlobalServices;

public class TestingScreen implements Screen
{
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    GameWorldStage gameWorldStage;
    StaticBackgroundLayer staticBackgroundLayer;
    //
    private SpriteBatch spriteBatch;
    private InputMultiplexer inputMultiplexer;
    //
    private ShipCamera shipCamera;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    public TestingScreen(SpriteBatch spriteBatch)
    {
        //Creating the game world
        this.spriteBatch = spriteBatch;
        //Input handling
        inputMultiplexer = new InputMultiplexer();
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    @Override
    public void show() {
        //Firing up this screen
        GlobalServices.getGlobalServices().setGamescreenShutdownIssued(false);
        //Starting the background
        //staticBackgroundLayer = new StaticBackgroundLayer(new BackgroundCamera(4000, 4000), spriteBatch);
        staticBackgroundLayer = new Level1Background(new BackgroundCamera(), spriteBatch);
        //Games starting stages
        shipCamera = new ShipCamera();
        gameWorldStage = new GameWorldStage(shipCamera, spriteBatch);
        staticBackgroundLayer.show();
        gameWorldStage.show();
        //Input handling
        inputMultiplexer.addProcessor(staticBackgroundLayer);
        inputMultiplexer.addProcessor(gameWorldStage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        //Act
        staticBackgroundLayer.act();
        gameWorldStage.act();
        //
        staticBackgroundLayer.updateGameCamPosition(gameWorldStage.getCamX(), gameWorldStage.getCamY());
        //Draw
        staticBackgroundLayer.draw();
        gameWorldStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        //Resizing all the stages
        staticBackgroundLayer.resize(width, height);
        gameWorldStage.resize(width, height);
    }

    @Override
    public void pause() {
        GlobalServices.getGlobalServices().setGamescreenShutdownIssued(true);
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        inputMultiplexer.removeProcessor(gameWorldStage);
        inputMultiplexer.removeProcessor(staticBackgroundLayer);
        inputMultiplexer.clear();
        staticBackgroundLayer.dispose();
        gameWorldStage.dispose();
    }

    @Override
    public void dispose() {
        GlobalServices.getGlobalServices().setGamescreenShutdownIssued(true);
        inputMultiplexer.removeProcessor(gameWorldStage);
        inputMultiplexer.removeProcessor(staticBackgroundLayer);
        inputMultiplexer.clear();
        staticBackgroundLayer.dispose();
        gameWorldStage.dispose();
    }

    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
