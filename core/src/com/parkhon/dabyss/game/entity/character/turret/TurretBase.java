package com.parkhon.dabyss.game.entity.character.turret;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.parkhon.dabyss.game.entity.character.turret.pool.TurretBasePool;
import com.parkhon.dabyss.game.entity.projectile.FiringPoint;

import java.util.ArrayList;
import java.util.List;

public class TurretBase extends Actor
{
    //DEVELOPMENT NOTES:
    //First Proto: rotate with keyboard controls for now, then do the aim to coordinates on the global map.
    //Have the controller give the coordinates of the current cursor position.

    //Goals: for turret motion
    //Static turret rotates with ship through grouping
    //Rotating turret, and static base rotating with ship.
    //Turret can aim to given coordinates at all times, turret target.
    //Turret respects rotation capacity
    //Turret has rotation constraints

    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    //Graphics
    private Texture texture;
    private TextureRegion textureRegion;
    private float graphicAngleOffset;
    private float xPivot;
    private float yPivot;

    //IDEAL SYSTEMS
    //Placement
    private Vector2 placement;
    private Vector2 globalPosition;

    //Movement
    private Vector2 target; //Where the turret aims for
    private Vector2 toTarget;
    protected float rotationCapacity; //Max degrees per second

    //WORLD SYSTEMS
    private Vector2 shipPosition;
    private float shipAngle;
    private float shipCenterX;
    private float shipCenterY;

    //Movement Mechanisms
    private float targetAngling;
    private float sinDifference;
    private float offsetX;
    private float offsetY;
    private float previousShipAngle;
    private float current2PreviousShipAngleOffset;

    //Primitive AI Controller
    private float nextRotationValue; // 0 means no rotation, positive values mean counter clockwise and negative mean clockwise.

    //Restrictions
    private ArrayList<RotationRestriction> rotationRestrictions;
    private boolean clockwisePossible; //If clockwise rotation is rendered impossible by a rotation restriction.
    private boolean counterClockwisePossible;
    private float directionSin;
    private float minDistanceRestrictionBegin;
    private float minDistanceRestrictionEnd;
    private enum Direction {CLOCKWISE, CENTERED, COUNTER_CLOCKWISE}

    //Recycling
    private boolean alive;

    //Firing Capabilities
    private boolean firingPrimed;
    private ArrayList<FiringPoint> firingPoints; //Relative to pivot x,y

    //Pool
    TurretBasePool turretBasePool;



    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    public TurretBase(TurretBasePool turretBasePool)
    {
        super();
        //Pool
        this.turretBasePool = turretBasePool;

        //Initiation
        shipPosition = new Vector2(0,0);
        globalPosition = new Vector2(0,0);
        targetAngling = 0.0f;
        target = new Vector2(100,100);
        toTarget = new Vector2(0,0);
        placement = new Vector2(0,0);
        rotationRestrictions = new ArrayList<>();
        clockwisePossible = true;
        counterClockwisePossible = true;
        directionSin = 0;
        minDistanceRestrictionBegin = 0.0f;
        minDistanceRestrictionEnd = 0.0f;
        firingPoints = new ArrayList<>();
        offsetX = 0.0f;
        offsetY = 0.0f;
        alive = true;
        firingPrimed = false;
        previousShipAngle = 0.0f;
        current2PreviousShipAngleOffset = 0.0f;
        nextRotationValue = 0.0f;
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(alive){
        batch.draw(
                textureRegion, this.getX(), this.getY(),
                xPivot, yPivot,
                texture.getWidth(), texture.getHeight(),1, 1, getRotation() + graphicAngleOffset);
        }
    }

    @Override
    public void act(float delta) {
        if(alive){
            rotateTurret2Ship();
            fixRotation();
            moveTurretToShipPosition();
            rotateTurretToTarget();
            updateFiringPoints();
            //DEBUG
            fireWeapons(firingPrimed);
            //END DEBUG
            super.act(delta);
        }
    }

    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    public void start(){
        this.setBounds(getX(), getY() , texture.getWidth(), texture.getHeight());
        this.moveBy(-texture.getWidth() / 2, -texture.getHeight() / 2);
    }
    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    public void updateShipAngle(float angle)
    {
        if(angle < 0) {
            this.shipAngle = 360 + (angle % 360);
        } else {
            this.shipAngle = angle % 360;
        }
    }

    public void rotateTurret2Ship()
    {
        //Computing offset
        current2PreviousShipAngleOffset = shipAngle - previousShipAngle; //for moving turrets with ship rotation
        //Rotating turret
        rotateBy(current2PreviousShipAngleOffset);
        //Updating angle
        previousShipAngle = shipAngle; //Updating angles for next offset computation
    }

    public void updateShipPosition(float x, float y)
    {
        shipPosition.set(x, y);
    }

    public void recycle()
    {
        //TODO WIP
        //Recycling rotation restrictions
        for(RotationRestriction restriction : rotationRestrictions)
        {
            restriction.recycle();
        }
        rotationRestrictions.clear();
        //Recycling firing points
        for(FiringPoint firingPoint : firingPoints)
        {
            firingPoint.recycle();
        }
        firingPoints.clear();
        //Leaving stage
        remove();
        //Returning to pool
        turretBasePool.returnToPool(this);
    }

    public void primeRecycle()
    {
        alive = false;
        //Priming firing points
        for(FiringPoint firingPoint : firingPoints)
        {
            firingPoint.primeRecycle();
        }
    }

    public Vector2 getPlacement() {
        return placement;
    }

    public void setPlacement(float x ,float y) {
        this.placement.set(x, y);
    }

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

    public float getGraphicAngleOffset() {
        return graphicAngleOffset;
    }

    public void setGraphicAngleOffset(float graphicAngleOffset) {
        this.graphicAngleOffset = graphicAngleOffset;
    }

    public float getxPivot() {
        return xPivot;
    }

    public void setxPivot(float xPivot) {
        this.xPivot = xPivot;
    }

    public float getyPivot() {
        return yPivot;
    }

    public void setyPivot(float yPivot) {
        this.yPivot = yPivot;
    }

    public void pushRotationRestriction(RotationRestriction rotationRestriction)
    {
        rotationRestrictions.add(rotationRestriction);
    }

    public void pushFiringPoint(FiringPoint firingPoint)
    {
        this.firingPoints.add(firingPoint);
    }

    public List<FiringPoint> getFiringPoints()
    {
        return firingPoints;
    }

    public void aimTo(float targetX, float targetY)
    {
        target.set(targetX, targetY);
    }

    public void setRotationCapacity(float rotationCapacity) {
        this.rotationCapacity = rotationCapacity;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setFiringPrimed(boolean firingPrimed) {
        this.firingPrimed = firingPrimed;
    }

    public void computeNextTurretRotation()
    {
        /*
        This method will be handled by the TurretControllerWorker. It specifies the next rotation the turret should turn
         */
        if(nextRotationValue == 0)
        {
            //Then the previous rotation value was consumed or there was no need to rotate. Attempting to issue new orders
            //
            float delta = Gdx.graphics.getDeltaTime();
            //Note: errors out problematically from 0 vector, might want to consider removing the zero vector by removing 0,0 from game world space.
            //Resetting flags DEV
            clockwisePossible = true;
            counterClockwisePossible = true;
            //Continue
            toTarget.set(target);
            toTarget.sub(globalPosition);
            targetAngling = MathUtils.atan2(toTarget.y, toTarget.x) * MathUtils.radiansToDegrees;
            if(targetAngling < 0) {
                targetAngling = 360 + targetAngling;
            }
            //Honoring restrictions
            for(RotationRestriction restriction : rotationRestrictions) {
                //Target Unreachable
                minDistanceRestrictionBegin = 180 - Math.abs(Math.abs(targetAngling - (restriction.getBeginAngle() + shipAngle) % 360) - 180);
                minDistanceRestrictionEnd = 180 - Math.abs(Math.abs(targetAngling - (restriction.getEndAngle() + shipAngle) % 360) - 180);
                if(minDistanceRestrictionBegin < restriction.getMinDistanceBorders() && minDistanceRestrictionEnd < restriction.getMinDistanceBorders())
                {
                    clockwisePossible = false;
                    counterClockwisePossible = false;
                } else {
                    //Cannot cross restriction clockwise
                    if(getDirectionTo(getRotation(), restriction.getEndAngle() + shipAngle) == Direction.CLOCKWISE
                            && 180 - Math.abs(Math.abs(getRotation() - (restriction.getEndAngle() + shipAngle) % 360) - 180)
                            < 180 - Math.abs(Math.abs(getRotation() - targetAngling) - 180)) {
                        clockwisePossible = false;
                    }
                    //Cannot cross restriction counter clockwise
                    if(getDirectionTo(getRotation(), restriction.getBeginAngle() + shipAngle) == Direction.COUNTER_CLOCKWISE &&
                            180 - Math.abs(Math.abs(getRotation() - (restriction.getBeginAngle() + shipAngle) % 360) - 180)
                                    < 180 - Math.abs(Math.abs(getRotation() - targetAngling) - 180)) {
                        counterClockwisePossible = false;
                    }
                }
            }
            sinDifference = MathUtils.sin((getRotation() - targetAngling) * MathUtils.degreesToRadians);
            if(clockwisePossible && (sinDifference > 0 || !counterClockwisePossible)) {
                //Have to spin clockwise to make the turret angle smaller
                //Target angle is larger special case
                if(targetAngling > getRotation()) {
                    //Rotating clockwise with special case adjustment
                    if(360 + getRotation() - targetAngling > rotationCapacity * delta) {
                        nextRotationValue = -rotationCapacity * delta;
                    } else {
                        nextRotationValue = targetAngling - 360 -getRotation(); //Locking on on to target
                    }
                }
                //Normal case: turret angle is larger
                else {
                    if(getRotation() - targetAngling > rotationCapacity * delta) {
                        nextRotationValue = -rotationCapacity * delta;
                    } else {
                        nextRotationValue = targetAngling - getRotation();
                    }
                }

            } else if(counterClockwisePossible && (sinDifference < 0 || !clockwisePossible)) {
                //Have to spin counter clockwise to make turret angle larger
                //Turret angle is larger special case
                if(getRotation() > targetAngling) {
                    if(360 + targetAngling - getRotation() > rotationCapacity * delta) {
                        nextRotationValue = rotationCapacity * delta;
                    }else {
                        nextRotationValue = targetAngling + 360 - getRotation();
                    }
                }
                //Normal case: target angle is larger
                else {
                    if(targetAngling - getRotation() > rotationCapacity * delta) {
                        nextRotationValue = rotationCapacity * delta;
                    } else {
                        nextRotationValue = targetAngling - getRotation();
                    }
                }
            } else{
                //Turret is on target. sin(0) equals 0.
            }
        }
    }

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    private void fixRotation()
    {
        if(getRotation() < 0) {
            setRotation(360 + (getRotation() % 360));
        } else {
            setRotation(getRotation() % 360);
        }
    }

    private void moveTurretToShipPosition()
    {
        //Move x and y according to ship rotation TRANSFORM
        offsetX = placement.x * MathUtils.cos(shipAngle * MathUtils.degreesToRadians) - placement.y * MathUtils.sin(shipAngle * MathUtils.degreesToRadians);
        offsetY = placement.y * MathUtils.cos(shipAngle * MathUtils.degreesToRadians) + placement.x * MathUtils.sin(shipAngle * MathUtils.degreesToRadians);
        setPosition(shipPosition.x + shipCenterX - xPivot + offsetX, shipPosition.y + shipCenterY - yPivot + offsetY);
        globalPosition.set(getX() + xPivot, getY() + yPivot);
    }

    /*
    private void rotateTurretToTarget(float delta)
    {
        //Note: errors out problematically from 0 vector, might want to consider removing the zero vector by removing 0,0 from game world space.
        //Resetting flags DEV
        clockwisePossible = true;
        counterClockwisePossible = true;
        //Continue
        toTarget.set(target);
        toTarget.sub(globalPosition);
        targetAngling = MathUtils.atan2(toTarget.y, toTarget.x) * MathUtils.radiansToDegrees;
        if(targetAngling < 0) {
            targetAngling = 360 + targetAngling;
        }
        //Honoring restrictions
        for(RotationRestriction restriction : rotationRestrictions) {
            //Target Unreachable
            minDistanceRestrictionBegin = 180 - Math.abs(Math.abs(targetAngling - (restriction.getBeginAngle() + shipAngle) % 360) - 180);
            minDistanceRestrictionEnd = 180 - Math.abs(Math.abs(targetAngling - (restriction.getEndAngle() + shipAngle) % 360) - 180);
            if(minDistanceRestrictionBegin < restriction.getMinDistanceBorders() && minDistanceRestrictionEnd < restriction.getMinDistanceBorders())
            {
                clockwisePossible = false;
                counterClockwisePossible = false;
            } else {
                //Cannot cross restriction clockwise
                if(getDirectionTo(getRotation(), restriction.getEndAngle() + shipAngle) == Direction.CLOCKWISE
                && 180 - Math.abs(Math.abs(getRotation() - (restriction.getEndAngle() + shipAngle) % 360) - 180)
                        < 180 - Math.abs(Math.abs(getRotation() - targetAngling) - 180)) {
                    clockwisePossible = false;
                }
                //Cannot cross restriction counter clockwise
                if(getDirectionTo(getRotation(), restriction.getBeginAngle() + shipAngle) == Direction.COUNTER_CLOCKWISE &&
                        180 - Math.abs(Math.abs(getRotation() - (restriction.getBeginAngle() + shipAngle) % 360) - 180)
                                < 180 - Math.abs(Math.abs(getRotation() - targetAngling) - 180)) {
                    counterClockwisePossible = false;
                }
            }
        }
        sinDifference = MathUtils.sin((getRotation() - targetAngling) * MathUtils.degreesToRadians);
        if(clockwisePossible && (sinDifference > 0 || !counterClockwisePossible)) {
            //Have to spin clockwise to make the turret angle smaller
            //Target angle is larger special case
            if(targetAngling > getRotation()) {
                //Rotating clockwise with special case adjustment
                if(360 + getRotation() - targetAngling > rotationCapacity * delta) {
                    rotateBy(-rotationCapacity * delta);
                } else {
                    setRotation(targetAngling);
                }
            }
            //Normal case: turret angle is larger
            else {
                if(getRotation() - targetAngling > rotationCapacity * delta) {
                    rotateBy(-rotationCapacity * delta);
                } else {
                    setRotation(targetAngling);
                }
            }

        } else if(counterClockwisePossible && (sinDifference < 0 || !clockwisePossible)) {
            //Have to spin counter clockwise to make turret angle larger
            //Turret angle is larger special case
            if(getRotation() > targetAngling) {
                if(360 + targetAngling - getRotation() > rotationCapacity * delta) {
                    rotateBy(rotationCapacity * delta);
                }else {
                    setRotation(targetAngling);
                }
            }
            //Normal case: target angle is larger
            else {
                if(targetAngling - getRotation() > rotationCapacity * delta) {
                    rotateBy(rotationCapacity * delta);
                } else {
                    setRotation(targetAngling);
                }
            }
        } else{
            //Turret is on target. sin(0) equals 0.
        }
    }
     */
    private void rotateTurretToTarget()
    {
        if(nextRotationValue != 0)
        {
            this.rotateBy(nextRotationValue); //Rotating
            nextRotationValue = 0; //To queue more rotation orders
        }
    }


    private Direction getDirectionTo(float origin, float destination)
    {
        directionSin = MathUtils.sin((origin - destination) * MathUtils.degreesToRadians);
        if(directionSin < 0) {
            return Direction.COUNTER_CLOCKWISE;
        } else if(directionSin > 0) {
            return Direction.CLOCKWISE;
        } else {
            return Direction.CENTERED;
        }
    }

    private void updateFiringPoints()
    {
        for(FiringPoint firingPoint : firingPoints)
        {
            firingPoint.updateTurretPosition(getX() + xPivot, getY() + yPivot, getRotation());
        }
    }

    private void fireWeapons(boolean fire)
    {
        for(FiringPoint firingPoint : firingPoints)
        {
            firingPoint.setFiringPrimed(fire);
        }
    }

}
