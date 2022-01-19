package com.parkhon.dabyss.game.entity.character.turret.instance;

import com.parkhon.dabyss.game.entity.character.turret.TurretBase;
import com.parkhon.dabyss.game.entity.character.turret.pool.TurretBasePool;
import com.parkhon.dabyss.game.entity.projectile.FiringPoint;
import com.parkhon.dabyss.game.entity.projectile.instanced.FiringPointPooledFactory;
import com.parkhon.dabyss.game.system.controller.primitive.TurretControllerWorker;
import com.parkhon.dabyss.game.system.global.TextureManager;

public class TurretBasePooledFactory
{
    //Singleton
    //TODO: Singleton pattern makes no sense here. This must be owned by the stage. Change this in all pooled factories and their pools.

    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Pool
    TurretBasePool turretBasePool;

    //Subordinate Factories
    FiringPointPooledFactory firingPointPooledFactory;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public TurretBasePooledFactory()
    {
        turretBasePool = new TurretBasePool();
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Production
    //Test Turret
    public TurretBase buildTestTurret(long parentId, float shipCenterX, float shipCenterY, float placementX, float placementY)
    {
        TurretBase turret = turretBasePool.lendTurretBase();

        //Base turret stats
        turret.setRotationCapacity(100);
        turret.setTexture(TextureManager.getTextureManager().getTestTurretTex());
        turret.setTextureRegion(TextureManager.getTextureManager().getTestTurretTR());
        turret.setShipCenterX(shipCenterX);
        turret.setShipCenterY(shipCenterY);
        turret.setPlacement(placementX, placementY);
        turret.setGraphicAngleOffset(-90);
        turret.setxPivot(45);
        turret.setyPivot(30);

        //Firing points
        FiringPoint firing1 = firingPointPooledFactory.buildTestFiringPoint(parentId);
        firing1.setPlacement(25,59);
        firing1.setFireRate(0.5f);
        FiringPoint firing2 = firingPointPooledFactory.buildTestFiringPoint(parentId);
        firing2.setPlacement(13,55);
        firing2.setFireRate(0.5f);

        FiringPoint firing3 = firingPointPooledFactory.buildTestFiringPoint(parentId);
        firing3.setPlacement(-13, 55);
        firing3.setFireRate(0.5f);
        FiringPoint firing4 = firingPointPooledFactory.buildTestFiringPoint(parentId);
        firing4.setPlacement(-25, 59);
        firing4.setFireRate(0.5f);

        //Adding firing points
        turret.pushFiringPoint(firing1);
        turret.pushFiringPoint(firing2);
        turret.pushFiringPoint(firing3);
        turret.pushFiringPoint(firing4);

        //Starting turret
        turret.start();

        //Alive
        turret.setAlive(true);

        //Turret ready
        return turret;
    }

    public void setTurretControllerWorker(TurretControllerWorker turretControllerWorker)
    {
        turretBasePool.setTurretControllerWorker(turretControllerWorker);
    }

    public void setFiringPointPooledFactory(FiringPointPooledFactory firingPointPooledFactory) {
        this.firingPointPooledFactory = firingPointPooledFactory;
    }

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
