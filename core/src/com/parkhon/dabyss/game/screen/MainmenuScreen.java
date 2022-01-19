package com.parkhon.dabyss.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.parkhon.dabyss.game.screen.stages.MainmenuStage;
import com.parkhon.dabyss.game.screen.stages.StaticBackgroundLayer;
import com.parkhon.dabyss.game.map.layer.instanced.Level1Background;
import com.parkhon.dabyss.game.system.camera.BackgroundCamera;
import com.parkhon.dabyss.game.system.camera.MenuCamera;

public class MainmenuScreen implements Screen
{
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Screen generic
    private SpriteBatch spriteBatch;

    //Main menu characteristics
    private StaticBackgroundLayer staticBackgroundLayer;
    private MainmenuStage mainmenuStage;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public MainmenuScreen(SpriteBatch spriteBatch)
    {
        //Rendering
        this.spriteBatch = spriteBatch;
        //Input handling
        //...
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    @Override
    public void show() {
        //Starting the background
        staticBackgroundLayer = new Level1Background(new BackgroundCamera(), spriteBatch);
        mainmenuStage = new MainmenuStage(new MenuCamera(100, 100), spriteBatch);

        //Prepare rendering start
        staticBackgroundLayer.show();
        mainmenuStage.show();
    }

    @Override
    public void render(float v) {
        //Act
        staticBackgroundLayer.act();
        mainmenuStage.act();
        //Draw
        staticBackgroundLayer.draw();
        mainmenuStage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        staticBackgroundLayer.resize(width, height);
        mainmenuStage.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        //Disposing
    }

    @Override
    public void dispose() {
        //Disposing
        staticBackgroundLayer.dispose();
        mainmenuStage.dispose();
    }


    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
