package com.parkhon.dabyss.game.system.collision.effect;

public class CollisionEffect
{
    /*
    This class defines the collision effects that can be passed to a lower or equal priority entity.
    It conveys the intention of the collision.
     */
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Effect attributes
    private float damage;

    //Pool


    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    public CollisionEffect()
    {
        reset(); //Setting the initial values
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public void reset()
    {
        //Zeroed values
        damage = 0.0f;
    }

    //Customization
    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }
    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
