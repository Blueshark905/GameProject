package com.parkhon.dabyss.game.system.collision.shape;

import com.parkhon.dabyss.game.system.collision.grid.GridObject;

import java.util.HashMap;

public class CircleCollisionDetector
{
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Memo for hit testing
    private HashMap<String, Boolean> circle2Circle;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public CircleCollisionDetector()
    {
        circle2Circle = new HashMap<>();
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    public boolean hit(GridObject subject, GridObject gridObject)
    {
        //Reset the memo
        circle2Circle.clear();
        //Test for every possible collision combination
        for(CollisionCircle circle : subject.getCollisionShape().getCollisionCircles())
        {
            for(CollisionCircle otherCircle : gridObject.getCollisionShape().getCollisionCircles())
            {
                //Checking to see if this computation was already in the memo
                long minId = Math.min(circle.getGameId(), otherCircle.getGameId());
                long maxId = Math.max(circle.getGameId(), otherCircle.getGameId());
                if(!circle2Circle.containsKey(minId + "+" + maxId))
                {
                    //Adding to memo as it is being computed
                    circle2Circle.put(minId + "+" + maxId, null);
                    //Compute to see if the distance between the two centers are smaller than the largest radius
                    float touchDistance = circle.getRadius() + otherCircle.getRadius();
                    //Point to point computation
                    float distance = (float)Math.sqrt(Math.pow(circle.getX() - otherCircle.getX(), 2) + Math.pow(circle.getY() - otherCircle.getY(), 2));
                    if(distance <= touchDistance)
                    {
                        //There is a collision
                        circle.setHit(true);
                        otherCircle.setHit(true);
                        return true;
                    }
                }
            }
        }
        //PLACEHOLDER
        return false;
    }

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
