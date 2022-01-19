package com.parkhon.dabyss.game.system.collision.grid;

import com.parkhon.dabyss.game.system.collision.effect.CollisionEffect;
import com.parkhon.dabyss.game.system.collision.CollisionUtils;
import com.parkhon.dabyss.game.system.collision.shape.CollisionShape;

public interface GridObject
{
    //This interface allows any class to become a grid object in the smart grid
    public boolean isAlive();
    public float getCollisionX();
    public float getCollisionY();
    public float getCollisionAngle();
    public boolean canCollide();
    public CollisionUtils.CollisionType getCollisionType();
    public CollisionUtils.CollisionEventPriority getCollisionEventPriority();
    public long getGameId();
    public void receiveEffect(CollisionEffect collisionEffect);
    public CollisionEffect deliverEffect();
    public CollisionShape getCollisionShape();
}
