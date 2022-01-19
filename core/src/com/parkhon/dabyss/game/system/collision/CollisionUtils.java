package com.parkhon.dabyss.game.system.collision;

import com.parkhon.dabyss.game.system.collision.grid.GridObject;

public class CollisionUtils
{
    public static enum CollisionType {
        SHIP, PROJECTILE, OBSTACLE
    }

    public static enum CollisionEventPriority {
        LOW, MEDIUM, HIGH
    }

    public static int getCollisionEventPriorityWeight(CollisionEventPriority priority)
    {
        if(priority == CollisionEventPriority.HIGH)
        {
            return 3;
        } else if(priority == CollisionEventPriority.MEDIUM)
        {
            return 2;
        } else {
            return 1;
        }
    }

    public static boolean isValidCollision(GridObject a, GridObject b)
    {
        if(a.getCollisionType() == CollisionType.PROJECTILE && b.getCollisionType() == CollisionType.PROJECTILE) {
            //Projectiles cannot influence other projectiles on collision
            return false;
        }
        if(a.getCollisionType() == CollisionType.OBSTACLE && b.getCollisionType() == CollisionType.OBSTACLE) {
            return false;
        }
        if(a.getCollisionShape().getParentId() == b.getGameId() || b.getCollisionShape().getParentId() == a.getGameId())
        {
            return false;
        }
        //Base case: there is no immunity, the collision should be computed
        return true;
    }
}
