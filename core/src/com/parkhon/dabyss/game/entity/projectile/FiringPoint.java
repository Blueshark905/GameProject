package com.parkhon.dabyss.game.entity.projectile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.parkhon.dabyss.game.entity.projectile.instanced.AbstractProjectilePooledFactory;
import com.parkhon.dabyss.game.entity.projectile.pool.FiringPointPool;
import com.parkhon.dabyss.game.system.global.GlobalServices;

public class FiringPoint extends Actor
{
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Data polling
    GlobalServices globalServices;

    //DEBUG
    private Texture texture;
    private TextureRegion textureRegion;
    //END DEBUG

    //Position
    private float placementX;
    private float placementY;

    private float projectedX;
    private float projectedY;

    //Firing
    private boolean firingPrimed; //Is the fire command given by a controller
    private float fireRate; //Per second

    //Projectile

    //Mechanical
    private float fireRateEnforcer;

    //Collision
    private long parentId;

    //Projectile factory
    AbstractProjectilePooledFactory projectileFactory;

    //Pool
    FiringPointPool firingPointPool;

    //Recycling
    private boolean alive;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    public FiringPoint(FiringPointPool firingPointPool)
    {
        //Pool
        this.firingPointPool = firingPointPool;

        //Initiation
        globalServices = GlobalServices.getGlobalServices();
        //Fallback values
        placementX = 0.0f;
        placementY = 0.0f;

        projectedX = 0.0f;
        projectedY = 0.0f;
        firingPrimed = false;
        fireRateEnforcer = 0.0f;
        alive = true;
        //DEBUG
        texture = new Texture("aim_assist.png");
        textureRegion = new TextureRegion(texture);
        //END DEBUG
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------


    @Override
    public void act(float delta) {
        if(alive) {
            //Handling weapons
            fireWeapons(delta, firingPrimed);
            //Super
            super.act(delta);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(alive) {
            if (globalServices.isTurretFirePointVisible() && globalServices.isDebugEnabled()) {
                batch.draw(
                        textureRegion, this.getX(), this.getY(),
                        texture.getWidth() / 2, texture.getHeight() / 2,
                        texture.getWidth(), texture.getHeight(), 1, 1, 0);
            }
        }
    }


    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public void updateTurretPosition(float turretX, float turretY, float turretAngle)
    {
        //Firing point position relative to the x pivot
        projectedX = placementX * MathUtils.cos( (turretAngle - 90) * MathUtils.degreesToRadians) +
                placementY * MathUtils.sin((turretAngle + 90) * MathUtils.degreesToRadians) + turretX;
        projectedY = -placementY * MathUtils.cos((turretAngle + 90) * MathUtils.degreesToRadians) +
                placementX * MathUtils.sin((turretAngle - 90) * MathUtils.degreesToRadians) + turretY;
        //Updating position of the firing point
        setPosition(projectedX - texture.getWidth() / 2, projectedY - texture.getHeight() / 2);
        //Updating rotation of the firing point to influence projectile launch angle
        setRotation(turretAngle);
    }

    public void recycle()
    {
        //Exiting stage
        remove();
        //Returning to pool
        firingPointPool.returnToPool(this);
    }

    public void primeRecycle()
    {
        alive = false;
    }

    public void setPlacement(float px, float py) {
        placementX = px;
        placementY = py;
    }

    public float getFireRate() {
        return fireRate;
    }

    public void setFireRate(float fireRate) {
        this.fireRate = fireRate;
        fireRateEnforcer = fireRate;
    }

    public boolean isFiringPrimed() {
        return firingPrimed;
    }

    public void setFiringPrimed(boolean firingPrimed) {
        this.firingPrimed = firingPrimed;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    public void setProjectileFactory(AbstractProjectilePooledFactory projectileFactory) {
        this.projectileFactory = projectileFactory;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    private void fireWeapons(float delta, boolean fire)
    {
        //Refreshing fire rate controls
        if(fireRateEnforcer > 0) {
            fireRateEnforcer -= delta;
        }
        //Advancing fire process if requested and is legal
        if(fire && fireRateEnforcer <= 0) {
            //Resetting fire rate controls
            fireRateEnforcer = fireRate;
            //Firing
            Projectile projectile = projectileFactory.buildProjectile(getX(), getY(), getRotation(), parentId);
            /*
            if(getStage() == null){
                System.out.println("ERROR");
            }
             */
            getStage().addActor(projectile);
            projectile.start();
            //OLD VER
            /*
            ShapeTestProjectile shapeTestProjectile = new ShapeTestProjectile(parentId);
            Projectile projectile = ProjectilePooledFactory.getFactory().buildTestProjectile(getX(), getY(),getRotation());
            Projectile projectile = new Projectile(getX(),getY(),"aim_assist.png", projectileVelocity, getRotation(), projectileLifetime,
                    shapeTestProjectile);
            //Adding the projectile to the game
            getStage().addActor(projectile);
            //Adding the shape to the collision layer smart grid
            globalServices.getSmartGrid().introduceGridObject(projectile);
            //Adding all the collision circles of the shape test projectile for debug display
            shapeTestProjectile.addCollisionCirclesToStage(getStage());
            */
        }
    }
}
