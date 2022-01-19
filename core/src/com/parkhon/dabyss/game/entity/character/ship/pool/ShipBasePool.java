package com.parkhon.dabyss.game.entity.character.ship.pool;

import com.parkhon.dabyss.game.entity.character.ship.ShipBase;

import java.util.ArrayList;

public class ShipBasePool
{
    //Singleton

    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Pool
    private ArrayList<ShipBase> availablePool;
    private ArrayList<ShipBase> totalPool;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public ShipBasePool()
    {
        //Pools
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
    public ShipBase lendShipBase()
    {
        System.out.println("Ships handled by pool: " + totalPool.size() + " available: " + availablePool.size());
        if(availablePool.size() == 0)
        {
            //No collision shapes in pool, creating a new one
            ShipBase shipBase = new ShipBase(this);
            totalPool.add(shipBase);
            return shipBase;
        } else {
            //There are collision shapes available to lend, lending these
            ShipBase shipBase = availablePool.get(0);
            availablePool.remove(0); //No longer available
            return shipBase;
        }
    }

    public void returnToPool(ShipBase shipBase)
    {
        availablePool.add(shipBase);
        System.out.println("Ships handled by pool: " + totalPool.size() + " available: " + availablePool.size());
    }


    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
