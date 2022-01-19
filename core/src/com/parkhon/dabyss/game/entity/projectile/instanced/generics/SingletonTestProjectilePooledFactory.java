package com.parkhon.dabyss.game.entity.projectile.instanced.generics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.parkhon.dabyss.game.entity.projectile.Projectile;
import com.parkhon.dabyss.game.entity.projectile.instanced.AbstractProjectilePooledFactory;
import com.parkhon.dabyss.game.entity.projectile.pool.SingletonProjectilePool;
import com.parkhon.dabyss.game.system.collision.shape.CollisionShape;
import com.parkhon.dabyss.game.system.collision.shape.instanced.ShapePooledFactory;
import com.parkhon.dabyss.game.system.global.GlobalServices;
import com.parkhon.dabyss.game.system.global.TextureManager;

public class SingletonTestProjectilePooledFactory implements AbstractProjectilePooledFactory
{
    //Singleton

    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Singleton
    private static SingletonTestProjectilePooledFactory projectilePooledFactory = new SingletonTestProjectilePooledFactory();

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Singleton
    private SingletonTestProjectilePooledFactory()
    {

    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    @Override
    public Projectile buildProjectile(float x, float y, float angle, long parentId)
    {
        Projectile projectile = SingletonProjectilePool.getProjectilePool().lendProjectile();
        projectile.setParentId(parentId);
        projectile.setLifetime(2.0f);
        projectile.setVelocity(800);
        Texture texture = TextureManager.getTextureManager().getTestProjectileTex();
        projectile.setTexture(texture);
        projectile.setTextureRegion(TextureManager.getTextureManager().getTestProjectileTR());
        projectile.setPosition(x, y);
        projectile.setRotation(angle);
        projectile.getDirection().set(MathUtils.cos(angle * MathUtils.degreesToRadians), MathUtils.sin(angle * MathUtils.degreesToRadians));
        projectile.setAlive(true);
        projectile.setTimeTillDeath(GlobalServices.getGlobalServices().getElementTimeTillDeath());
        //Collision
        CollisionShape shape = ShapePooledFactory.getShapePooledFactory().buildShapeTestProjectile(projectile.getParentId());
        shape.setShipCenterX(texture.getWidth() / 2);
        shape.setShipCenterY(texture.getHeight() / 2);
        projectile.setCollisionShape(shape);
        GlobalServices.getGlobalServices().getSmartGrid().introduceGridObject(projectile);
        //Collision effect
        projectile.getCollisionEffect().reset();
        projectile.getCollisionEffect().setDamage(10.0f);
        //Alive
        projectile.setAlive(true);
        return projectile;
    }

    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Singleton
    public static SingletonTestProjectilePooledFactory getFactory() {
        return projectilePooledFactory;
    }



    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
