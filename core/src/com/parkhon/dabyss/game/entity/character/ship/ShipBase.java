package com.parkhon.dabyss.game.entity.character.ship;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.parkhon.dabyss.game.entity.character.ship.pool.ShipBasePool;
import com.parkhon.dabyss.game.entity.character.turret.TurretBase;
import com.parkhon.dabyss.game.system.collision.effect.CollisionEffect;
import com.parkhon.dabyss.game.system.collision.CollisionUtils;
import com.parkhon.dabyss.game.system.collision.grid.GridObject;
import com.parkhon.dabyss.game.system.collision.shape.CollisionShape;
import com.parkhon.dabyss.game.system.global.GameIdentities;
import com.parkhon.dabyss.game.system.global.GlobalServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShipBase extends Actor implements GridObject
{
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    //Game Id
    private long gameId;

    //Graphics
    private Texture texture;
    private TextureRegion textureRegion;

    //Combat
    private float health;

    //Logic
    private float delta;
    private boolean alive;
    private float timeTillDeath;

    //Movement Stats
    protected float maxForwardVelocity; //Max GU distance per second
    protected float minForwardVelocity;
    protected float minBackwardVelocity;
    protected float maxBackwardVelocity;
    protected float minLeftVelocity;
    protected float maxLeftVelocity;
    protected float minRightVelocity;
    protected float maxRightVelocity;
    protected float timeTillMaxVelocity; //How long will the ship accelerate to max speed.
    protected float rotationCapacity; //Degrees per second
    protected float friction; //Friction that forces deceleration

    //Movement Mechanisms
    private Vector2 direction; //The direction currently facing.
    private Vector2 motion; //Working vector to calculate motion
    private float velocity; //The current ship velocity
    private float timeMovingForward; //The time the ship has been moving
    private float timeMovingBackward;
    private float timeMovingLeft;
    private float timeMovingRight;
    private boolean movingForward;
    private boolean movingBackwards;
    private boolean movingLeft;
    private boolean movingRight;
    private boolean rotatingCounterClockwise;
    private boolean rotatingClockwise;

    //Turrets
    private ArrayList<TurretBase> turrets;

    //Collision
    private float collisionX;   //Clone of ship x for collision worker thread
    private float collisionY;   //Clone of ship y for collision worker thread
    private float collisionAngle; //Clone of ship angle for collision worker thread
    private List<CollisionEffect> pendingCollisionEffects;
    private CollisionEffect collisionEffect;
    private CollisionShape collisionShape;

    //Pool
    ShipBasePool shipBasePool;

    //DEBUG DEMO
    private float demoCounter = 10.0f;


    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public ShipBase(ShipBasePool shipBasePool) {
        super();
        this.shipBasePool = shipBasePool;
        gameId = GameIdentities.serveId();
        alive = true;
        direction = new Vector2(0,0);
        motion = new Vector2(0,0);
        timeMovingForward = 0.0f;
        timeMovingBackward = 0.0f;
        timeMovingLeft = 0.0f;
        timeMovingRight = 0.0f;
        movingForward = false;
        movingBackwards = false;
        movingLeft = false;
        movingRight = false;
        health = 1.0f;
        turrets = new ArrayList<>();
        pendingCollisionEffects = Collections.synchronizedList(new ArrayList<CollisionEffect>());
        collisionEffect = prepareCollisionEffect(); //What happens when the ship collides. What it does to the other element.
        timeTillDeath = GlobalServices.getGlobalServices().getElementTimeTillDeath();
    }


    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(alive) {
            batch.draw(
                    textureRegion, this.getX(), this.getY(),
                    texture.getWidth() / 2, texture.getHeight() / 2,
                    texture.getWidth(), texture.getHeight(), 1, 1, this.getRotation());
            super.draw(batch, parentAlpha);
        }
    }

    @Override
    public void act(float delta) {
        this.delta = delta;
        if(alive){
            boolean moved = false;
            if(movingForward) {
                timeMovingForward += delta;
                timeMovingBackward = 0.0f;
                moved = true;
            } else if (movingBackwards) {
                timeMovingForward = 0.0f;
                timeMovingBackward += delta;
                moved = true;
            } if (movingLeft) {
                timeMovingLeft += delta;
                timeMovingRight = 0.0f;
                moved = true;
            } else if (movingRight) {
                timeMovingLeft = 0.0f;
                timeMovingRight += delta;
                moved = true;
            }
            if(!moved)  {
                timeMovingForward = 0.0f;
                timeMovingBackward = 0.0f;
                timeMovingLeft = 0.0f;
                timeMovingRight = 0.0f;

            }
            updatePosition(); //Calculate where the camera should be now
            if(rotatingCounterClockwise) {
                rotateCounterClockwise();
            } else if(rotatingClockwise) {
                rotateClockwise();
            }
            //Updating turrets and collision worker thread data
            collisionAngle = getRotation();
            for(TurretBase turret : turrets)
            {
                turret.updateShipAngle(getRotation());
            }
            //Collision effects
            processCollisionEffects();
            //Death
            if(health <= 0)
            {
                alive = false;
                primeRecycle();
            }
            //DEBUG DEMO
            demoCounter -= delta;
            if(demoCounter < 0)
            {
                setFiringPrimed(true);
            }
            //DEBUG DEMO
            super.act(delta);
        } else {
            //Ship has been destroyed
            if(timeTillDeath > 0)
            {
                timeTillDeath -= delta;
            } else {
                recycle();
            }
        }
    }

    //DEBUG
    public void setFiringPrimed(boolean firingPrimed)
    {
        for(TurretBase turret : turrets)
        {
            turret.setFiringPrimed(firingPrimed);
        }
    }
    //DEBUG

    @Override
    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public boolean canCollide() {
        return true;
    }

    @Override
    public CollisionUtils.CollisionType getCollisionType() {
        return CollisionUtils.CollisionType.SHIP;
    }

    @Override
    public float getCollisionX() {
        return collisionX;
    }

    @Override
    public float getCollisionY() {
        return collisionY;
    }

    @Override
    public float getCollisionAngle() {
        return collisionAngle;
    }

    @Override
    public long getGameId() {
        return gameId;
    }

    @Override
    public CollisionUtils.CollisionEventPriority getCollisionEventPriority() {
        return CollisionUtils.CollisionEventPriority.LOW;
    }

    @Override
    public void receiveEffect(CollisionEffect collisionEffect) {
        pendingCollisionEffects.add(collisionEffect);
    }

    @Override
    public CollisionEffect deliverEffect() {
        return collisionEffect;
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
        setOrigin(0,0);
        setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
        this.moveBy(-texture.getWidth() / 2, -texture.getHeight() / 2);
        textureRegion = new TextureRegion(texture);
        //Preparing collision shape
        collisionShape.setShipCenterX(texture.getWidth() / 2);
        collisionShape.setShipCenterY(texture.getHeight() / 2);
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public boolean isMovingForward() {
        return movingForward;
    }

    public void setMovingForward(boolean movingForward) {
        this.movingForward = movingForward;
    }

    public boolean isMovingBackwards() {
        return movingBackwards;
    }

    public void setMovingBackwards(boolean movingBackwards) {
        this.movingBackwards = movingBackwards;
    }

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public boolean isRotatingCounterClockwise() {
        return rotatingCounterClockwise;
    }

    public void setRotatingCounterClockwise(boolean rotatingCounterClockwise) {
        this.rotatingCounterClockwise = rotatingCounterClockwise;
    }

    public boolean isRotatingClockwise() {
        return rotatingClockwise;
    }

    public void setRotatingClockwise(boolean rotatingClockwise) {
        this.rotatingClockwise = rotatingClockwise;
    }

    public void addTurret(TurretBase turret)
    {
        turrets.add(turret);
    }

    public void recycle()
    {
        //TODO WIP
        //Leaving stage
        remove();
        //Recycling turrets
        for(TurretBase turret : turrets)
        {
            turret.recycle();
        }
        turrets.clear();
        //Resetting collision effects
        pendingCollisionEffects.clear(); //Forgetting pending collision effects
        //Resetting alive stats
        timeTillDeath = GlobalServices.getGlobalServices().getElementTimeTillDeath();
        motion.set(0,0);
        direction.set(0,0);
        movingForward = false;
        movingBackwards = false;
        movingLeft = false;
        movingRight = false;
        timeMovingForward = 0.0f;
        timeMovingBackward = 0.0f;
        timeMovingLeft = 0.0f;
        timeMovingRight = 0.0f;
        //Returning to pool
        shipBasePool.returnToPool(this);
    }

    public void primeRecycle()
    {
        for(TurretBase turret : turrets)
        {
            turret.primeRecycle();
        }
    }

    public ArrayList<TurretBase> getTurrets()
    {
        return turrets;
    }

    public float getShipWidth()
    {
        return texture.getWidth();
    }

    public float getShipHeight()
    {
        return texture.getHeight();
    }

    public void setCollisionShape(CollisionShape collisionShape) {
        this.collisionShape = collisionShape;
    }

    public float getRotationCapacity() {
        return rotationCapacity;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    public void setMaxForwardVelocity(float maxForwardVelocity) {
        this.maxForwardVelocity = maxForwardVelocity;
    }

    public void setMinForwardVelocity(float minForwardVelocity) {
        this.minForwardVelocity = minForwardVelocity;
    }

    public void setMinBackwardVelocity(float minBackwardVelocity) {
        this.minBackwardVelocity = minBackwardVelocity;
    }

    public void setMaxBackwardVelocity(float maxBackwardVelocity) {
        this.maxBackwardVelocity = maxBackwardVelocity;
    }

    public void setMinLeftVelocity(float minLeftVelocity) {
        this.minLeftVelocity = minLeftVelocity;
    }

    public void setMaxLeftVelocity(float maxLeftVelocity) {
        this.maxLeftVelocity = maxLeftVelocity;
    }

    public void setMinRightVelocity(float minRightVelocity) {
        this.minRightVelocity = minRightVelocity;
    }

    public void setMaxRightVelocity(float maxRightVelocity) {
        this.maxRightVelocity = maxRightVelocity;
    }

    public void setTimeTillMaxVelocity(float timeTillMaxVelocity) {
        this.timeTillMaxVelocity = timeTillMaxVelocity;
    }

    public void setRotationCapacity(float rotationCapacity) {
        this.rotationCapacity = rotationCapacity;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------


    private void updatePosition()
    {
        //We are meant to move.
        //Computing the velocity the ship should have
        if(movingForward) {
            if(movingLeft)
            {
                velocity = getForwardVelocity() / 2 + getLeftVelocity() / 2;
                redirectionForward();
                redirectionLeft();
            } else if(movingRight) {
                velocity = getForwardVelocity() / 2 + getRightVelocity() / 2;
                redirectionForward();
                redirectionRight();
            } else {
                velocity = getForwardVelocity();
                redirectionForward();
            }
        } else if(movingBackwards) {
            if(movingLeft)
            {
                velocity = getBackwardVelocity() / 2 + getLeftVelocity() / 2;
                redirectionBackward();
                redirectionLeft();
            } else if(movingRight) {
                velocity = getBackwardVelocity() / 2 + getRightVelocity() / 2;
                redirectionBackward();
                redirectionRight();
            } else {
                velocity = getBackwardVelocity();
                redirectionBackward();
            }
        } else if(movingLeft)
        {
            velocity = getLeftVelocity();
            redirectionLeft();
        } else if(movingRight) {
            velocity = getRightVelocity();
            redirectionRight();
        } else {
            //Decceleration should act
            if(velocity > 0) {
                velocity -= friction / 2 * delta;
            } else {
                velocity = 0;
            }
        }
        //Limiting velocity
        if(velocity > maxForwardVelocity) {
            velocity = maxForwardVelocity;
        }
        direction.scl(velocity * delta);
        //Finalizing movement
        this.moveBy(direction.x, direction.y); //Moving
        //Notify turrets of position change
        for(TurretBase turret : turrets)
        {
            turret.updateShipPosition(getX(), getY());
        }
        //Update collision thread ship position data
        collisionX = getX();
        collisionY = getY();
        direction.nor(); //Reset the motion working vector
        direction.scl(0.5f);
    }
    private void updateRotation()
    {
    }

    private float getForwardVelocity()
    {
        if(timeMovingForward < timeTillMaxVelocity) {
            return (float)(minForwardVelocity + (maxForwardVelocity - minForwardVelocity) / timeTillMaxVelocity * timeMovingForward);
        } else {
            return maxForwardVelocity;
        }
    }

    private float getBackwardVelocity()
    {
        if(timeMovingBackward < timeTillMaxVelocity) {
            return (float)(minBackwardVelocity + (maxBackwardVelocity - minBackwardVelocity) / timeTillMaxVelocity * timeMovingBackward);
        } else {
            return maxBackwardVelocity;
        }
    }

    private float getLeftVelocity()
    {
        if(timeMovingLeft < timeTillMaxVelocity) {
            return (float)(minLeftVelocity + (maxLeftVelocity - minLeftVelocity) / timeTillMaxVelocity * timeMovingLeft);
        } else {
            return maxLeftVelocity;
        }
    }

    private float getRightVelocity()
    {
        if(timeMovingRight < timeTillMaxVelocity) {
            return (float)(minRightVelocity + (maxRightVelocity - minRightVelocity) / timeTillMaxVelocity * timeMovingRight);
        } else {
            return maxRightVelocity;
        }
    }

    private void redirectionRight()
    {
        //Adding the newly selected direction to our final direction.
        motion.set(MathUtils.cos(getRotation() * MathUtils.PI / 180), MathUtils.sin(getRotation() * MathUtils.PI / 180));
        direction.add(motion);
        direction.nor();
        //Reseting the newDirection to allow for a later calculation
        motion.set(0,0);
    }
    private void redirectionLeft()
    {
        //Adding the newly selected direction to our final direction.
        motion.set(-MathUtils.cos(getRotation() * MathUtils.PI / 180), -MathUtils.sin(getRotation() * MathUtils.PI / 180));
        direction.add(motion);
        direction.nor();
        //Reseting the newDirection to allow for a later calculation
        motion.set(0,0);
    }
    private void redirectionForward()
    {
        //Adding the newly selected direction to our final direction.
        motion.set(-MathUtils.sin(getRotation() * MathUtils.PI / 180), MathUtils.cos(getRotation() * MathUtils.PI / 180));
        direction.add(motion);
        direction.nor();
        //Reseting the newDirection to allow for a later calculation
        motion.set(0,0);
    }
    private void redirectionBackward()
    {
        //Adding the newly selected direction to our final direction.
        motion.set(MathUtils.sin(getRotation() * MathUtils.PI / 180), -MathUtils.cos(getRotation() * MathUtils.PI / 180));
        direction.add(motion);
        direction.nor();
        //Reseting the newDirection to allow for a later calculation
        motion.set(0,0);
    }
    private void rotateCounterClockwise()
    {
        rotateBy(rotationCapacity * delta);
    }
    private void rotateClockwise()
    {
        rotateBy(-rotationCapacity * delta);
    }

    private CollisionEffect prepareCollisionEffect()
    {
        CollisionEffect collisionEffect = new CollisionEffect();
        return collisionEffect;
    }

    private void processCollisionEffects()
    {
        while(pendingCollisionEffects.size() > 0)
        {
            //Applying the effect
            CollisionEffect effect = pendingCollisionEffects.get(0);
            pendingCollisionEffects.remove(0);
            //Damage
            if(effect.getDamage() > 0){
                health -= effect.getDamage();
            }
        }
    }
    //END OF CLASS
}
