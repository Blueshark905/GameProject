package com.parkhon.dabyss.game.entity.character.turret;

import com.parkhon.dabyss.game.entity.character.turret.pool.RotationRestrictionPool;

public class RotationRestriction {
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    private int beginAngle;
    private int endAngle;
    private float minDistanceBorders;

    //Pool
    RotationRestrictionPool rotationRestrictionPool;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    public RotationRestriction(RotationRestrictionPool rotationRestrictionPool)
    {
        //Pool
        this.rotationRestrictionPool = rotationRestrictionPool;
    }

    public RotationRestriction(int beginAngle, int endAngle)
    {
        this.beginAngle = beginAngle;
        this.endAngle = endAngle;
        minDistanceBorders = 180 - Math.abs(Math.abs(beginAngle - endAngle) - 180);
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public void recycle()
    {
        rotationRestrictionPool.returnToPool(this);
    }

    public void start()
    {
        minDistanceBorders = 180 - Math.abs(Math.abs(beginAngle - endAngle) - 180);
    }

    //Access
    public int getBeginAngle() {
        return beginAngle;
    }

    public void setBeginAngle(int beginAngle) {
        this.beginAngle = beginAngle;
    }

    public int getEndAngle() {
        return endAngle;
    }

    public void setEndAngle(int endAngle) {
        this.endAngle = endAngle;
    }

    public float getMinDistanceBorders() {
        return minDistanceBorders;
    }

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
