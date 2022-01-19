package com.parkhon.dabyss.game.system.collision.shape;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.parkhon.dabyss.game.system.collision.shape.pool.CollisionCirclePool;
import com.parkhon.dabyss.game.system.global.GameIdentities;
import com.parkhon.dabyss.game.system.global.GlobalServices;

public class CollisionCircle extends Actor {
    /*
    The ideal position of the collision circle is relative to 0,0 of the CollisionShape.
     */
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Data polling
    private GlobalServices globalServices;
    private long gameId;

    //Position
    private float idealX;
    private  float idealY;
    private float radius;

    //Rendering
    private ShapeRenderer shapeRenderer;
    private boolean hit;
    private float hitRecoverTimer;
    private float hitMeter;

    //Shape position
    private float shapeAngle;
    private float shapeX;
    private float shapeY;

    //Rotation matrix values
    private float a11;
    private float a12;
    private float a21;
    private float a22;

    //Pool
    CollisionCirclePool collisionCirclePool;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public CollisionCircle(CollisionCirclePool collisionCirclePool)
    {
        //Pool
        this.collisionCirclePool = collisionCirclePool;

        //Id
        gameId = GameIdentities.serveId();

        //Basic initializations
        a11 = 0.0f;
        a12 = 0.0f;
        a21 = 0.0f;
        a22 = 0.0f;

        //Global services
        this.globalServices = GlobalServices.getGlobalServices();
        shapeRenderer = globalServices.getShapeRenderer();

        //Visualization
        hit = false;
        hitRecoverTimer = 3.0f;
        hitMeter = hitRecoverTimer;
    }

    public CollisionCircle(float x, float y, float radius)
    {
        //Id
        gameId = GameIdentities.serveId();

        //Basic initializations
        idealX = -x;
        idealY = y;
        this.setPosition(x, y);
        this.radius = radius;
        a11 = 0.0f;
        a12 = 0.0f;
        a21 = 0.0f;
        a22 = 0.0f;

        //Global services
        this.globalServices = GlobalServices.getGlobalServices();
        shapeRenderer = globalServices.getShapeRenderer();

        //Visualization
        hit = false;
        hitRecoverTimer = 3.0f;
        hitMeter = hitRecoverTimer;
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        if(globalServices.isCollisionVisible() && globalServices.isDebugEnabled())
        {
            if(!hit) {
                shapeRenderer.setColor(Color.GREEN);
            } else {
                shapeRenderer.setColor(Color.RED);
            }
            shapeRenderer.circle(getX(), getY(), radius);
        }
    }

    @Override
    public void act(float delta) {
        if(hit)
        {
            if(hitMeter > 0)
            {
                hitMeter -= delta;
            } else {
                hit = false;
                hitMeter = hitRecoverTimer;
            }
        }
    }


    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public void updateRotationMatrix(float a11, float a12, float a21, float a22)
    {
        this.a11 = a11;
        this.a12 = a12;
        this.a21 = a21;
        this.a22 = a22;
    }

    public void updateShapePosition(float x, float y, float angle)
    {
        shapeX = x;
        shapeY = y;
        shapeAngle = angle;
    }



    public void transformCirclePositionToShapePosition()
    {
        setPosition(shapeX + idealX * a11 + idealY * a21,  shapeY + idealX * a12 + idealY * a22);
    }

    public void recycle()
    {
        remove(); //Exiting the game stage
        //reset values
        hit = false;
        hitMeter = hitRecoverTimer;
        //return to the pool for further reassignment
        collisionCirclePool.returnToPool(this);
    }

    //Access
    public long getGameId()
    {
        return gameId;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public void setIdealX(float idealX) {
        this.idealX = idealX;
    }

    public void setIdealY(float idealY) {
        this.idealY = idealY;
    }

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
