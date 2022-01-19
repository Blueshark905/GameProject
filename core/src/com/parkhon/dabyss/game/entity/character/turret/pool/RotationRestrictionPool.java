package com.parkhon.dabyss.game.entity.character.turret.pool;

import com.parkhon.dabyss.game.entity.character.turret.RotationRestriction;

import java.util.ArrayList;

public class RotationRestrictionPool
{
    //Singleton

    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Pool
    private ArrayList<RotationRestriction> availablePool;
    private ArrayList<RotationRestriction> totalPool;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public RotationRestrictionPool()
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
    public RotationRestriction lendRotationRestriction(int beginAngle, int endAngle)
    {
        //System.out.println("Restrictions handled by pool: " + totalPool.size() + " available: " + availablePool.size());
        if(availablePool.size() == 0)
        {
            //No restrictions available for reuse, creating a new one
            RotationRestriction restriction = new RotationRestriction(this);
            totalPool.add(restriction);
            configureRotationRestriction(restriction, beginAngle, endAngle);
            return restriction; //Returning the configured restriction
        } else {
            //There are elements available for use in the pool, using these
            RotationRestriction restriction = availablePool.get(0);
            availablePool.remove(0);
            configureRotationRestriction(restriction, beginAngle, endAngle);
            return restriction;
        }
    }

    public void returnToPool(RotationRestriction restriction)
    {
        availablePool.add(restriction);
    }


    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    private void configureRotationRestriction(RotationRestriction restriction, int beginAngle, int endAngle)
    {
        restriction.setBeginAngle(beginAngle);
        restriction.setEndAngle(endAngle);
        restriction.start();
    }
}
