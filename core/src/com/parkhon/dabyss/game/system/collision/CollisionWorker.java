package com.parkhon.dabyss.game.system.collision;

import com.badlogic.gdx.Gdx;
import com.parkhon.dabyss.game.screen.stages.GameWorldStage;
import com.parkhon.dabyss.game.system.collision.grid.SmartGrid;
import com.parkhon.dabyss.game.system.global.GlobalServices;

public class CollisionWorker extends Thread{
    /*
    This worker will allow a dedicated thread to process collisions without affecting the rendering framerate
     */
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Data polling
    GameWorldStage stage;

    //Thread
    private boolean loop;
    private int fps;

    //Synchronization
    private long mainOffset; //The offset time to be synchronized with the main loop, if the worker finishes first.

    //Collision handling
    private SmartGrid grid;


    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    public CollisionWorker(GameWorldStage stage, SmartGrid grid)
    {
        this.stage = stage;
        this.grid = grid;
        mainOffset = 0;
        loop = true;
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------


    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void run() {
        while(loop && !GlobalServices.getGlobalServices().isGamescreenShutdownIssued()) {
                //Game loop begin
                grid.update();
                //Game loop end
                //Getting the fps
                fps = Gdx.graphics.getFramesPerSecond();
                //Getting the time to wait for the main loop
                mainOffset = (fps > 0) ? (long) ((1.0 / fps * 1000) - stage.getTimeSinceAct()) : (long) (1.0 / 60 * 1000);
                if (mainOffset > 0) {
                    //There is time left on the main loop, sleep
                    try {
                        sleep(mainOffset);
                    } catch (InterruptedException e) {
                    }
                } else {
                    //There is no time left on the main loop, continue immediately
                }
        }
        //Shutdown issued, disposing...
    }


    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public void dispose()
    {
        loop = false;
    }

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    private void tick()
    {

    }
}
