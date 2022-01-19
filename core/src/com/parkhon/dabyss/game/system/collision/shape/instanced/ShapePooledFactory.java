package com.parkhon.dabyss.game.system.collision.shape.instanced;

import com.parkhon.dabyss.game.system.collision.shape.CollisionShape;
import com.parkhon.dabyss.game.system.collision.shape.pool.CollisionCirclePool;
import com.parkhon.dabyss.game.system.collision.shape.pool.CollisionShapePool;

public class ShapePooledFactory
{
    //Singleton

    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Singleton
    private static ShapePooledFactory shapePooledFactory = new ShapePooledFactory();

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Singleton
    private ShapePooledFactory()
    {

    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Singleton
    public static ShapePooledFactory getShapePooledFactory() {
        return shapePooledFactory;
    }

    //Shape construction
    //Test Projectile
    public CollisionShape buildShapeTestProjectile(long parentId)
    {
        //Creating the shape
        CollisionShape shape = CollisionShapePool.getCollisionShapePool().lendCollisionShape(parentId);
        //Configuring the shape for the test projectile
        //Adding circles
        shape.addCircle(CollisionCirclePool.getCollisionCirclePool().lendCollisionCircle(0,0,5));
        return shape;
    }

    //Test Ship
    public CollisionShape buildShapeTestShip()
    {
        //Creating the shape
        CollisionShape shape = CollisionShapePool.getCollisionShapePool().lendCollisionShape(-1); //No parent for ship
        //Configuring the shape
        //Adding circles
        //TOP
        shape.addCircle(CollisionCirclePool.getCollisionCirclePool().lendCollisionCircle(0,50,50));
        shape.addCircle(CollisionCirclePool.getCollisionCirclePool().lendCollisionCircle(0,120,20));

        //MIDDLE
        shape.addCircle(CollisionCirclePool.getCollisionCirclePool().lendCollisionCircle(50,0,50));
        shape.addCircle(CollisionCirclePool.getCollisionCirclePool().lendCollisionCircle(-50,0,50));

        //BOTTOM
        shape.addCircle(CollisionCirclePool.getCollisionCirclePool().lendCollisionCircle(40,-85,50));
        shape.addCircle(CollisionCirclePool.getCollisionCirclePool().lendCollisionCircle(-40,-85,50));

        return shape;
    }


    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
