package com.parkhon.dabyss.game.entity.projectile.instanced;

import com.parkhon.dabyss.game.entity.projectile.Projectile;

public interface AbstractProjectilePooledFactory
{
    public Projectile buildProjectile(float x, float y, float angle, long parentId);
}
