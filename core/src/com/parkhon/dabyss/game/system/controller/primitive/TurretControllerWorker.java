package com.parkhon.dabyss.game.system.controller.primitive;

import com.badlogic.gdx.Gdx;
import com.parkhon.dabyss.game.entity.character.turret.TurretBase;
import com.parkhon.dabyss.game.screen.stages.GameWorldStage;
import com.parkhon.dabyss.game.system.global.GlobalServices;

import java.util.ArrayList;

public class TurretControllerWorker extends Thread
{
    /*
    This worker computes the turrets calculations to decide where to turn to and registers the next rotation on the
    turret base itself.
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
    private long extraOffset; // The extra offset to wait to not run on perfect synchronization with the main loop.

    //Control
    ArrayList<TurretBase> turrets; //The turrets this worker will process

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public TurretControllerWorker(GameWorldStage stage)
    {
        this.stage = stage;
        turrets = new ArrayList<>();
        mainOffset = 0;
        loop = true;
        extraOffset = (long)(1.0 / 60 / 4 * 1000); //One quarter of an fps
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
        //TODO: Do a stage directed stage alive flag, that will be toggled by shutdown issued.
        while(loop && !GlobalServices.getGlobalServices().isGamescreenShutdownIssued())
        {
            //Game loop begin
            controlTurrets();
            //Game loop end
            fps = Gdx.graphics.getFramesPerSecond();
            //Getting the time to wait for the main loop
            mainOffset = (fps > 0) ? (long) ((1.0 / fps * 1000) - stage.getTimeSinceAct()) : (long) (1.0 / 60 * 1000);
            if (mainOffset > 0) {
                //There is time left on the main loop, sleep
                try {
                    sleep(mainOffset + extraOffset); //Sleeping at a slightly phased rhythm.
                } catch (InterruptedException e) {
                }
            } else {
                //There is no time left on the main loop, continue immediately
            }
        }
    }


    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public void registerTurret(TurretBase turret)
    {
        /*
        This method allows this worker to be control the turret. It will only take control if the turret is actually alive.
         */
        turrets.add(turret);
    }

    public void dispose()
    {
        loop = false;
    }

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    private void controlTurrets()
    {
        for(TurretBase turret: turrets)
        {
            turret.computeNextTurretRotation();
        }
    }
}
