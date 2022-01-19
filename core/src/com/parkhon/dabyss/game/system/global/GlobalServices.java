package com.parkhon.dabyss.game.system.global;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.parkhon.dabyss.game.system.collision.grid.SmartGrid;

public class GlobalServices
{
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Shutdown safeties
    private boolean gamescreenShutdownIssued;

    //Debug options
    private boolean debugEnabled;
    private boolean collisionVisible;
    private boolean turretFirePointVisible;

    //Global services
    private static GlobalServices globalServices = new GlobalServices();

    //Rendering
    private ShapeRenderer shapeRenderer;

    //Game elements time until death (after they are removed from the game)
    private float elementTimeTillDeath;

    //The smart grid of the collision layer
    private SmartGrid smartGrid;

    //Background and foreground coordination
    private float playerRotationCapacity;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    private GlobalServices()
    {
        //Singleton constructor
        gamescreenShutdownIssued = false;
        shapeRenderer = new ShapeRenderer();
        collisionVisible = false;
        turretFirePointVisible = false;
        debugEnabled = false;
        elementTimeTillDeath = 0.5f;
        smartGrid = new SmartGrid();
        playerRotationCapacity = 0.0f;
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------


    public boolean isGamescreenShutdownIssued() {
        return gamescreenShutdownIssued;
    }

    public void setGamescreenShutdownIssued(boolean gamescreenShutdownIssued) {
        this.gamescreenShutdownIssued = gamescreenShutdownIssued;
    }

    public static GlobalServices getGlobalServices()
    {
        return globalServices;
    }

    public ShapeRenderer getShapeRenderer()
    {
        return shapeRenderer;
    }

    public boolean isCollisionVisible() {
        return collisionVisible;
    }

    public void setCollisionVisible(boolean collisionVisible) {
        this.collisionVisible = collisionVisible;
    }

    public boolean isTurretFirePointVisible() {
        return turretFirePointVisible;
    }

    public void setTurretFirePointVisible(boolean turretFirePointVisible) {
        this.turretFirePointVisible = turretFirePointVisible;
    }

    public float getElementTimeTillDeath() {
        return elementTimeTillDeath;
    }

    public SmartGrid getSmartGrid() {
        return smartGrid;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    public float getPlayerRotationCapacity() {
        return playerRotationCapacity;
    }

    public void setPlayerRotationCapacity(float playerRotationCapacity) {
        this.playerRotationCapacity = playerRotationCapacity;
    }

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
