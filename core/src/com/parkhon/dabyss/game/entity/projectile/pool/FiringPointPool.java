package com.parkhon.dabyss.game.entity.projectile.pool;

import com.parkhon.dabyss.game.entity.projectile.FiringPoint;

import java.util.ArrayList;

public class FiringPointPool
{
    //Singleton

    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Pool
    private ArrayList<FiringPoint> availablePool;
    private ArrayList<FiringPoint> totalPool;


    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public FiringPointPool()
    {
        //Pool initiation
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
    public FiringPoint lendFiringPoint()
    {
        //System.out.println("Firing points handled by pool: " + totalPool.size() + " available: " + availablePool.size());
        if(availablePool.size() == 0)
        {
            //No available firing points in pool, creating a new one
            FiringPoint fp = new FiringPoint(this);
            totalPool.add(fp);
            return fp;
        } else {
            //There are available firing points in the pool, using these instead
            FiringPoint fp = availablePool.get(0);
            availablePool.remove(0); //No longer available
            return fp;
        }
    }

    public void returnToPool(FiringPoint firingPoint)
    {
        availablePool.add(firingPoint);
    }


    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    private void configureFiringPoint(FiringPoint fp, long parentId, float fireRate)
    {
        fp.setParentId(parentId);
        fp.setFireRate(fireRate);
    }
}
