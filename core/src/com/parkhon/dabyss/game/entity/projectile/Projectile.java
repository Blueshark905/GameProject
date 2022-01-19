package com.parkhon.dabyss.game.entity.projectile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.parkhon.dabyss.game.entity.projectile.pool.SingletonProjectilePool;
import com.parkhon.dabyss.game.system.collision.effect.CollisionEffect;
import com.parkhon.dabyss.game.system.collision.CollisionUtils;
import com.parkhon.dabyss.game.system.collision.grid.GridObject;
import com.parkhon.dabyss.game.system.collision.shape.CollisionShape;
import com.parkhon.dabyss.game.system.global.GameIdentities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Projectile extends Actor implements GridObject
{
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Stats
    private Texture texture;
    private TextureRegion textureRegion;
    private float velocity;
    private float lifetime;

    //Mechanical
    private Vector2 direction;
    private Vector2 movement;

    //Collision
    private boolean alive;
    private long gameId;
    private CollisionEffect collisionEffect;
    private CollisionShape collisionShape;
    private List<CollisionEffect> pendingEffects;
    private long parentId;

    //Death
    private float timeTillDeath;

    //Pooling
    private SingletonProjectilePool projectilePool;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public Projectile(SingletonProjectilePool projectilePool)
    {
        //Setting the pool
        this.projectilePool = projectilePool;

        //Initializing objects
        direction = new Vector2(0,0); //Unit vector
        movement = new Vector2(0,0);
        pendingEffects = Collections.synchronizedList(new ArrayList<CollisionEffect>());

        //Collision
        gameId = GameIdentities.serveId();
        collisionEffect = prepareCollisionEffect();
        parentId = 0;
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(alive)
        {
            batch.draw(
                    textureRegion, this.getX(), this.getY(),
                    texture.getWidth() / 2, texture.getHeight() / 2,
                    texture.getWidth(), texture.getHeight(),1, 1, getRotation());
        }
    }

    @Override
    public void act(float delta) {
        //Update
        if(shouldProjectileDie(delta) || !alive) {
            alive = false;
            timeTillDeath -= delta;
            if(timeTillDeath < 0)
            {
                //Projectile has expired its time, should be destroyed
                recycle();
            }
        } else {
            //Projectile still has time to affect the world
            updateProjectilePosition(delta);
        }
        //Super
        super.act(delta);
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public float getCollisionX() {
        return getX();
    }

    @Override
    public float getCollisionY() {
        return getY();
    }

    @Override
    public float getCollisionAngle() {
        return getRotation();
    }

    @Override
    public boolean canCollide() {
        return true;
    }

    @Override
    public CollisionUtils.CollisionType getCollisionType() {
        return CollisionUtils.CollisionType.PROJECTILE;
    }

    @Override
    public CollisionUtils.CollisionEventPriority getCollisionEventPriority() {
        return CollisionUtils.CollisionEventPriority.HIGH;
    }

    @Override
    public long getGameId() {
        return gameId;
    }

    @Override
    public void receiveEffect(CollisionEffect collisionEffect) {
        //pendingEffects.add(collisionEffect);
    }

    @Override
    public CollisionEffect deliverEffect() {
        if(alive)
        {
            alive = false;
            return collisionEffect;
        } else {
            return null;
        }
    }

    @Override
    public CollisionShape getCollisionShape() {
        return collisionShape;
    }

    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public void start()
    {
        //Adding the collision circles of the collision shape to the stage for debug rendering
        collisionShape.addCollisionCirclesToStage(getStage());
    }

    //Access
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public void setLifetime(float lifetime) {
        this.lifetime = lifetime;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setCollisionEffect(CollisionEffect collisionEffect) {
        this.collisionEffect = collisionEffect;
    }

    public void setCollisionShape(CollisionShape collisionShape) {
        this.collisionShape = collisionShape;
    }

    public void setTimeTillDeath(float timeTillDeath) {
        this.timeTillDeath = timeTillDeath;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getParentId() {
        return parentId;
    }

    public CollisionEffect getCollisionEffect() {
        return collisionEffect;
    }


    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    private void updateProjectilePosition(float delta)
    {
        movement.set(direction); //Cloning direction vector
        movement.scl(velocity); //Expanding by the velocity
        moveBy(movement.x * delta, movement.y * delta); //Moving the projectile
    }

    private boolean shouldProjectileDie(float delta) {
        lifetime -= delta;
        if(lifetime <= 0) {
            return true;
        } else {
            return false;
        }
    }

    private CollisionEffect prepareCollisionEffect()
    {
        CollisionEffect collisionEffect = new CollisionEffect();
        return collisionEffect;
    }

    private void recycle()
    {
        //Recycle the collision shape
        collisionShape.recycle();
        //Return the projectile to its pool
        pendingEffects.clear();
        movement.set(0,0); //Resetting objects
        projectilePool.returnToPool(this); //Returning to the available projectiles pool
        remove(); //Remove itself from the stage
    }


}
