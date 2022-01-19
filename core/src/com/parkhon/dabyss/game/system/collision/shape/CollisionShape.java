package com.parkhon.dabyss.game.system.collision.shape;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.parkhon.dabyss.game.system.collision.shape.pool.CollisionShapePool;
import org.ejml.data.DMatrix2x2;
import org.ejml.dense.fixed.CommonOps_DDF2;

import java.util.ArrayList;

public class CollisionShape
{
    /*
    This class represents the shape of collision circles that define a ship's hitbox. It is meant to be instanced and
    customized in the child constructor for each ship type.
     */
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Position
    //it ghosts the position of the game ship, but its position processing is done by the collision worker.
    private float x;
    private float y;
    private float angle;
    private float shipCenterX;
    private float shipCenterY;

    //Collision circles
    private ArrayList<CollisionCircle> circles;

    //Immune parent (to prevent projectiles from hitting their own source)
    private long parentId;

    //Transformation matrix
    private DMatrix2x2 rotationMatrix;
    private float a11;
    private float a12;
    private float a21;
    private float a22;

    //Pool
    CollisionShapePool collisionShapePool;


    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public CollisionShape(CollisionShapePool collisionShapePool)
    {
        //Pool
        this.collisionShapePool = collisionShapePool;

        //Simple initializations
        circles = new ArrayList<>();
        rotationMatrix = new DMatrix2x2(0,0,0,0);
        a11 = 0.0f;
        a12 = 0.0f;
        a21 = 0.0f;
        a22 = 0.0f;
        this.shipCenterX = 0.0f;
        this.shipCenterY = 0.0f;
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Update method to alter positions of circles
    public void updateShipPosition(float x, float y, float angle)
    {
        //Called by the worker thread
        this.x = x + shipCenterX;
        this.y = y + shipCenterY;
        this.angle = angle;
        //Computing the transformation matrix for shape basis to game basis
        //Basis change: get the transformation matrix for converting from game basis to shape basis
        /*
        float sXx = MathUtils.cos(shapeAngle * MathUtils.degreesToRadians);
        float sXy = MathUtils.sin(shapeAngle * MathUtils.degreesToRadians);

        float sYx = MathUtils.cos((shapeAngle + 90) * MathUtils.degreesToRadians);
        float sYy = MathUtils.sin((shapeAngle + 90) * MathUtils.degreesToRadians);
         */
        //Preparing rotation matrix CAN FACTOR TO SHAPE ITSELF FOR OPTIMIZATION
        rotationMatrix.set(-MathUtils.cos(angle * MathUtils.degreesToRadians),
                -MathUtils.sin(angle * MathUtils.degreesToRadians),
                MathUtils.cos((angle + 90) * MathUtils.degreesToRadians),
                MathUtils.sin((angle + 90) * MathUtils.degreesToRadians));
        CommonOps_DDF2.invert(rotationMatrix, rotationMatrix); //Inverting matrix, now we can convert shape basis to game basis
        //Storing the matrix values on floats for manual matrix multiplication inside the CollisionCircles
        a11 = (float)rotationMatrix.a11;
        a12 = (float)rotationMatrix.a12;
        a21 = (float)rotationMatrix.a21;
        a22 = (float)rotationMatrix.a22;
        //Transform ideal placement to game x and y (Matrix multiplication)
        /*
        float gameX = (float)(idealX * rotationMatrix.a11 + idealY * rotationMatrix.a21);
        float gameY = (float)(idealX * rotationMatrix.a12 + idealY * rotationMatrix.a22);
         */

        //Update all circles data
        for(CollisionCircle circle : circles)
        {
            //Order of operations here matters after specified
            circle.updateRotationMatrix(a11, a12, a21, a22);
            circle.updateShapePosition(this.x, this.y, this.angle);
            //Order sensitive with updateRotationMatrix and updateShapePosition:
            circle.transformCirclePositionToShapePosition();
        }
    }

    //Creating the hitbox
    public void addCircle(CollisionCircle circle)
    {
        circles.add(circle);
    }

    public ArrayList<CollisionCircle> getCollisionCircles()
    {
        return circles;
    }

    //Add circles to stage for debug view
    public void addCollisionCirclesToStage(Stage stage)
    {
        for(CollisionCircle circle : circles)
        {
            stage.addActor(circle);
        }
    }

    public void recycle()
    {
        while(circles.size() > 0)
        {
            CollisionCircle circle = circles.get(0);
            circle.recycle();
            circles.remove(0);
        }
        collisionShapePool.returnToPool(this);
    }

    //Access
    public float getShipCenterX() {
        return shipCenterX;
    }

    public void setShipCenterX(float shipCenterX) {
        this.shipCenterX = shipCenterX;
    }

    public float getShipCenterY() {
        return shipCenterY;
    }

    public void setShipCenterY(float shipCenterY) {
        this.shipCenterY = shipCenterY;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
