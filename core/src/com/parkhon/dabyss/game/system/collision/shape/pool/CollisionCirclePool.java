package com.parkhon.dabyss.game.system.collision.shape.pool;

import com.parkhon.dabyss.game.system.collision.shape.CollisionCircle;

import java.util.ArrayList;

public class CollisionCirclePool
{
    //Singleton

    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Singleton
    private static CollisionCirclePool collisionCirclePool = new CollisionCirclePool();

    //Pools
    private ArrayList<CollisionCircle> availablePool;
    private ArrayList<CollisionCircle> totalPool;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Singleton
    private CollisionCirclePool()
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

    //Singleton
    public static CollisionCirclePool getCollisionCirclePool() {
        return collisionCirclePool;
    }

    //Pool
    public CollisionCircle lendCollisionCircle(float centerX, float centerY, float radius)
    {
        //System.out.println("Circles handled by pool: " + totalPool.size() + " available: " + availablePool.size());
        if(availablePool.size() == 0)
        {
            //No circles in pool, create a new one
            CollisionCircle circle = new CollisionCircle(this);
            configureCircle(circle, centerX, centerY, radius);
            totalPool.add(circle);
            return circle;
        } else {
            //There are circles available in the pool, lend these
            CollisionCircle circle = availablePool.get(0);
            configureCircle(circle, centerX, centerY, radius);
            availablePool.remove(0); //Circle no longer available
            return circle;
        }
    }

    public void returnToPool(CollisionCircle circle)
    {
        availablePool.add(circle);
    }


    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    private void configureCircle(CollisionCircle circle, float centerX, float centerY, float radius)
    {
        circle.setIdealX(centerX);
        circle.setIdealY(centerY);
        circle.setRadius(radius);
    }
}
