package com.parkhon.dabyss.game.entity.character.ship.instance;

import com.parkhon.dabyss.game.entity.character.ship.ShipBase;
import com.parkhon.dabyss.game.entity.character.ship.pool.ShipBasePool;
import com.parkhon.dabyss.game.entity.character.turret.TurretBase;
import com.parkhon.dabyss.game.entity.character.turret.instance.TurretBasePooledFactory;
import com.parkhon.dabyss.game.entity.character.turret.pool.RotationRestrictionPool;
import com.parkhon.dabyss.game.system.collision.shape.instanced.ShapePooledFactory;
import com.parkhon.dabyss.game.system.global.TextureManager;


public class ShipPooledFactory
{
    //Singleton

    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Pool
    private ShipBasePool shipBasePool;
    private RotationRestrictionPool rotationRestrictionPool;

    //Subordinate Factories
    private TurretBasePooledFactory turretBasePooledFactory;


    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public ShipPooledFactory()
    {
        shipBasePool = new ShipBasePool();
        rotationRestrictionPool = new RotationRestrictionPool();
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Production
    //Test Turret
    public ShipBase buildTestShip(float x, float y, float angle)
    {
        ShipBase ship = shipBasePool.lendShipBase();

        //Stats
        ship.setTexture(TextureManager.getTextureManager().getTestShipTex());
        ship.setTextureRegion(TextureManager.getTextureManager().getTestShipTR());
        ship.setMaxForwardVelocity(1200);
        ship.setMinForwardVelocity(400);
        ship.setMaxBackwardVelocity(800);
        ship.setMinBackwardVelocity(200);
        ship.setMaxLeftVelocity(1000);
        ship.setMinLeftVelocity(300);
        ship.setMaxRightVelocity(1000);
        ship.setMinRightVelocity(300);
        ship.setTimeTillMaxVelocity(0.5f);
        ship.setRotationCapacity(60);
        ship.setFriction(2000);
        ship.setPosition(x, y);
        ship.setRotation(angle); //???
        ship.setHealth(100.0f);
        //Turrets
        TurretBase forwardTurret = turretBasePooledFactory.buildTestTurret(ship.getGameId(),ship.getTexture().getWidth() / 2,
                ship.getTexture().getHeight()/2,0,75);
        //TestTurret forwardTurret = new TestTurret(shipTexture.getWidth() / 2, shipTexture.getHeight() / 2, rotationCapacity, getGameId());
        forwardTurret.pushRotationRestriction(rotationRestrictionPool.lendRotationRestriction(225 ,315));
        TurretBase backwardsTurret = turretBasePooledFactory.buildTestTurret(ship.getGameId(),ship.getTexture().getWidth() / 2,
                ship.getTexture().getHeight()/2,0,-25);
        //TestTurret backwardsTurret = new TestTurret(shipTexture.getWidth() / 2, shipTexture.getHeight() / 2, rotationCapacity, getGameId());
        backwardsTurret.pushRotationRestriction(rotationRestrictionPool.lendRotationRestriction(45, 135));
        ship.addTurret(forwardTurret);
        ship.addTurret(backwardsTurret);

        //Collision
        ship.setCollisionShape(ShapePooledFactory.getShapePooledFactory().buildShapeTestShip());

        //Starting
        ship.start();
        //Ready
        return ship;
    }

    public void setTurretBasePooledFactory(TurretBasePooledFactory turretBasePooledFactory) {
        this.turretBasePooledFactory = turretBasePooledFactory;
    }

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
