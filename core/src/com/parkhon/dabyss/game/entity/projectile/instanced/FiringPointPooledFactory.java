package com.parkhon.dabyss.game.entity.projectile.instanced;

import com.parkhon.dabyss.game.entity.projectile.FiringPoint;
import com.parkhon.dabyss.game.entity.projectile.instanced.generics.SingletonTestProjectilePooledFactory;
import com.parkhon.dabyss.game.entity.projectile.pool.FiringPointPool;
import com.parkhon.dabyss.game.system.global.TextureManager;

public class FiringPointPooledFactory
{
    //Singleton

    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Pool
    FiringPointPool firingPointPool;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public FiringPointPooledFactory()
    {
        firingPointPool = new FiringPointPool();
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Building
    public FiringPoint buildTestFiringPoint(long parentId)
    {
        FiringPoint fp = firingPointPool.lendFiringPoint();
        //Configuring fire point
        fp.setParentId(parentId);
        fp.setFireRate(0.5f);
        fp.setTexture(TextureManager.getTextureManager().getTestProjectileTex());
        fp.setTextureRegion(TextureManager.getTextureManager().getTestProjectileTR());
        fp.setProjectileFactory(SingletonTestProjectilePooledFactory.getFactory());
        //Alive
        fp.setAlive(true);
        return fp;
    }


    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
