package com.parkhon.dabyss.game.map.layer.instanced;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.parkhon.dabyss.game.screen.stages.StaticBackgroundLayer;
import com.parkhon.dabyss.game.map.layer.objects.ParallaxObject;
import com.parkhon.dabyss.game.system.camera.BackgroundCamera;

public class Level1Background extends StaticBackgroundLayer
{
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    private BackgroundCamera camera;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public Level1Background(BackgroundCamera camera, SpriteBatch spriteBatch) {
        super(camera, spriteBatch);
        this.camera = camera;
    }


    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    @Override
    public void show() {
        Texture bgImage = new Texture("lv_1_bg_1.png");
        setImageTexture(bgImage);
        addParllaxObject(new ParallaxObject(new Texture("lv_1_pla_1.png"), 0.04f,500,1000, 1, 0.25f));
        addParllaxObject(new ParallaxObject(new Texture("lv_1_star_1.png"), 0.0005f, 1500, 1500, 0, 0.025f));
        camera.setBgImage(bgImage);
        super.show();
    }

    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
