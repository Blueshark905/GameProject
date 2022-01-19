package com.parkhon.dabyss.game.entity.projectile.pool;

import com.parkhon.dabyss.game.entity.projectile.Projectile;

import java.util.ArrayList;

public class SingletonProjectilePool
{
    /*
    Singleton pool that handles and reuses all projectiles created and recycled by the game
     */
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Singleton
    private static SingletonProjectilePool projectilePool = new SingletonProjectilePool();

    //The pool proper
    private ArrayList<Projectile> availablePool; //The projectiles that are ready to use
    private ArrayList<Projectile> totalPool; //All the projectiles this pool has ever created and lended

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    private SingletonProjectilePool()
    {
        //Simple initialization
        availablePool = new ArrayList<Projectile>();
        totalPool = new ArrayList<Projectile>();
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------


    //Singleton
    public static SingletonProjectilePool getProjectilePool() {
        return projectilePool;
    }

    //Lending of projectiles
    public Projectile lendProjectile()
    {
        //System.out.println("Projectiles handled by pool: " + totalPool.size() + " available: " + availablePool.size());
        if(availablePool.size() == 0)
        {
            //No projectiles in pool, creating a new one
            Projectile newProjectile = new Projectile(this);
            totalPool.add(newProjectile);
            return newProjectile;
        } else {
            //There are projectiles available, use those
            Projectile lentProjectile = availablePool.get(0); //Getting an available projectile
            availablePool.remove(0); //No longer available
            return lentProjectile;
        }
    }

    public void returnToPool(Projectile projectile)
    {
        availablePool.add(projectile);
    }


    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
