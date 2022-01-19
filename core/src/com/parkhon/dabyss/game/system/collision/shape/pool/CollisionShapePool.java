package com.parkhon.dabyss.game.system.collision.shape.pool;

import com.parkhon.dabyss.game.system.collision.shape.CollisionShape;

import java.util.ArrayList;

public class CollisionShapePool
{
    //Singleton

    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Singleton
    private static CollisionShapePool collisionShapePool = new CollisionShapePool();

    //Pool
    private ArrayList<CollisionShape> availablePool;
    private ArrayList<CollisionShape> totalPool;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Singleton
    private CollisionShapePool()
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

    //Singleton
    public static CollisionShapePool getCollisionShapePool() {
        return collisionShapePool;
    }

    //Pool
    public CollisionShape lendCollisionShape(long parentId)
    {
        //System.out.println("Shapes handled by pool: " + totalPool.size() + " available: " + availablePool.size());
        if(availablePool.size() == 0)
        {
            //No collision shapes in pool, creating a new one
            CollisionShape collisionShape = new CollisionShape(this);
            collisionShape.setParentId(parentId);
            totalPool.add(collisionShape);
            return collisionShape;
        } else {
            //There are collision shapes available to lend, lending these
            CollisionShape collisionShape = availablePool.get(0);
            availablePool.remove(0); //No longer available
            collisionShape.setParentId(parentId);
            return collisionShape;
        }
    }

    public void returnToPool(CollisionShape collisionShape)
    {
        availablePool.add(collisionShape);
    }


    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
