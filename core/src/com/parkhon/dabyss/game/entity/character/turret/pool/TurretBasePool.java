package com.parkhon.dabyss.game.entity.character.turret.pool;


import com.parkhon.dabyss.game.entity.character.turret.TurretBase;
import com.parkhon.dabyss.game.system.controller.primitive.TurretControllerWorker;

import java.util.ArrayList;

public class TurretBasePool
{
    //Singleton

    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Pool
    private ArrayList<TurretBase> availablePool;
    private ArrayList<TurretBase> totalPool;

    //Primitive AI Control
    private TurretControllerWorker turretControllerWorker;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public TurretBasePool()
    {
        availablePool = new ArrayList<>();
        totalPool = new ArrayList<>();
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Pool
    public TurretBase lendTurretBase()
    {
        //System.out.println("Restrictions handled by pool: " + totalPool.size() + " available: " + availablePool.size());
        if(availablePool.size() == 0)
        {
            //No restrictions available for reuse, creating a new one
            TurretBase turret = new TurretBase(this);
            totalPool.add(turret);
            turretControllerWorker.registerTurret(turret); //Enabling the primitive AI to control this turret
            return turret; //Returning the configured restriction
        } else {
            //There are elements available for use in the pool, using these
            TurretBase turret = availablePool.get(0);
            availablePool.remove(0);
            return turret;
        }
    }

    public void returnToPool(TurretBase turret)
    {
        availablePool.add(turret);
    }

    //Set primitive turret AI controller for all turrets
    public void setTurretControllerWorker(TurretControllerWorker turretControllerWorker) {
        this.turretControllerWorker = turretControllerWorker;
    }


    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

}
