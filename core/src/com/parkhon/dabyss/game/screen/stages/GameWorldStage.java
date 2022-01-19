package com.parkhon.dabyss.game.screen.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.parkhon.dabyss.game.entity.EntityFactory;
import com.parkhon.dabyss.game.entity.character.ship.ShipBase;
import com.parkhon.dabyss.game.entity.character.ship.instance.ShipPooledFactory;
import com.parkhon.dabyss.game.entity.character.turret.TurretBase;
import com.parkhon.dabyss.game.entity.character.turret.instance.TurretBasePooledFactory;
import com.parkhon.dabyss.game.entity.projectile.instanced.FiringPointPooledFactory;
import com.parkhon.dabyss.game.system.collision.CollisionWorker;
import com.parkhon.dabyss.game.system.collision.grid.SmartGrid;
import com.parkhon.dabyss.game.system.collision.shape.CollisionCircle;
import com.parkhon.dabyss.game.system.controller.HumanController;
import com.parkhon.dabyss.game.entity.projectile.FiringPoint;
import com.parkhon.dabyss.game.system.camera.ShipCamera;
import com.parkhon.dabyss.game.system.controller.primitive.TurretControllerWorker;
import com.parkhon.dabyss.game.system.cursor.GameplayCursor;
import com.parkhon.dabyss.game.system.global.GlobalServices;

import java.util.List;

public class GameWorldStage extends Stage
{
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    private SpriteBatch spriteBatch;
    //private FreeCamera freeCamera;
    private ShipCamera shipCamera;
    private HumanController humanController;
    private GameplayCursor gameplayCursor;

    //Collision System
    private SmartGrid collisionGrid;
    private CollisionWorker collisionWorker;
    private float timeSinceAct, timeSinceAct2;

    //Turret AI Control System
    private TurretControllerWorker turretControllerWorker;

    //Debug shapes
    private boolean debugEnabled;
    private ShapeRenderer shapeRenderer;

    //Pooled factories
    private ShipPooledFactory shipPooledFactory;
    private TurretBasePooledFactory turretBasePooledFactory;
    private FiringPointPooledFactory firingPointPooledFactory;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    public GameWorldStage(ShipCamera shipCamera, SpriteBatch spriteBatch)
    {
        super(shipCamera.getViewport(), spriteBatch);

        //Debug shapes
        shapeRenderer = GlobalServices.getGlobalServices().getShapeRenderer();

        //Simple initializations
        timeSinceAct = 0.0f;
        timeSinceAct2 = 0.0f;

        //Debug enabled
        debugEnabled = GlobalServices.getGlobalServices().isDebugEnabled();

        //Initializing simple preferences
        //this.freeCamera = freeCamera;
        this.shipCamera = shipCamera;
        //Creating the stage
        this.spriteBatch = spriteBatch;
        //Creating the collision worker
        collisionGrid = GlobalServices.getGlobalServices().getSmartGrid();
        collisionGrid.setMapHeight(10000);
        collisionGrid.setMapWidth(10000);
        collisionGrid.setMapStartingCoord(new Vector2(1,1));
        collisionGrid.setTileLength(500);
        collisionWorker = new CollisionWorker(this, collisionGrid);

        //Creating the factories
        shipPooledFactory = new ShipPooledFactory();
        turretBasePooledFactory = new TurretBasePooledFactory();
        firingPointPooledFactory = new FiringPointPooledFactory();

        //Wiring up the factories for inter use
        shipPooledFactory.setTurretBasePooledFactory(turretBasePooledFactory);
        turretBasePooledFactory.setFiringPointPooledFactory(firingPointPooledFactory);

        //Creating the primitive turret AI controller
        turretControllerWorker = new TurretControllerWorker(this);
        turretBasePooledFactory.setTurretControllerWorker(turretControllerWorker);

        //Creating player controls
        //Creating the player cursor
        gameplayCursor = new GameplayCursor(shipCamera);
        addActor(gameplayCursor);
        //Creating the human controller
        humanController = new HumanController(gameplayCursor);

        //TEST
        addActor(EntityFactory.createDuke(100,100,0));
        addActor(EntityFactory.createDuke(590, 100, 90));
        addActor(EntityFactory.createDuke(800, 50, 180));
        addActor(EntityFactory.createDuke(1000, 100, 270));
        addActor(EntityFactory.createDuke(1500, 100, 0));
        //
        addActor(EntityFactory.createDuke(100,100));
        addActor(EntityFactory.createDuke(100, 522, 90));
        addActor(EntityFactory.createDuke(100, 862, 180));
        addActor(EntityFactory.createDuke(50, 1000,  270));
        addActor(EntityFactory.createDuke(100, 1500, 0));
        addActor(new CollisionCircle(500,500,100));

        //Human controlled ship
        //spawnUserShip(EntityFactory.createTestShip(500,500,0));
        spawnUserShip(shipPooledFactory.buildTestShip(500,500,0));

        //Spawning an AI Ship
        spawnAiShip(shipPooledFactory.buildTestShip(1000,500,90));
        spawnAiShip(shipPooledFactory.buildTestShip(1000,1000,60));
        spawnAiShip(shipPooledFactory.buildTestShip(1000,1700,30));
        spawnAiShip(shipPooledFactory.buildTestShip(2000,500,90));
        spawnAiShip(shipPooledFactory.buildTestShip(2000,1000,60));
        spawnAiShip(shipPooledFactory.buildTestShip(2000,1700,30));
        //spawnAiShip(EntityFactory.createTestShip(1000,500, 90));

    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------


    @Override
    public void dispose() {
        collisionWorker.dispose();
        turretControllerWorker.dispose();
        super.dispose();
    }

    @Override
    public void draw() {
        spriteBatch.setProjectionMatrix(shipCamera.getCombinedMatrices());

        if(debugEnabled) {
            shapeRenderer.setProjectionMatrix(shipCamera.getCombinedMatrices());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        }
        super.draw();
        if(debugEnabled){shapeRenderer.end();}
    }

    @Override
    public void act(float delta) {
        humanController.update();
        Gdx.graphics.setTitle("FPS: "+ Gdx.graphics.getFramesPerSecond());
        //Updating the time since the last act
        timeSinceAct += delta;
        super.act(delta);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        boolean handled = false;
        handled = handled | humanController.mouseMoved(screenX, screenY);
        return handled;
    }

    @Override
    public boolean scrolled(int amount) {
        return super.scrolled(amount);
    }

    @Override
    public boolean keyDown(int keyCode) {
        boolean handled = false;
        handled = handled | humanController.keyDown(keyCode);
        return handled;
    }

    @Override
    public boolean keyUp(int keyCode) {
        boolean handled = false;
        handled = handled | humanController.keyUp(keyCode);
        return handled;
    }

    @Override
    public void addActor(Actor actor) {
        super.addActor(actor);
    }

    @Override
    public void addAction(Action action) {
        super.addAction(action);
    }

    @Override
    public Array<Actor> getActors() {
        return super.getActors();
    }


    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    public void resize(int width, int height)
    {
        shipCamera.resize(width, height);
        gameplayCursor.resize();
    }

    public void show()
    {
        //Game start
        collisionGrid.start();
        collisionWorker.start();
        turretControllerWorker.start();
        shipCamera.start();
        addActor(shipCamera);
        humanController.start();
    }

    public float getCamX()
    {
        return shipCamera.getX();
    }

    public float getCamY()
    {
        return shipCamera.getY();
    }

    public float getTimeSinceAct() {
        timeSinceAct2 = timeSinceAct;
        timeSinceAct = 0.0f;
        return timeSinceAct2;
    }

    public HumanController getHumanController() {
        return humanController;
    }

    public void spawnUserShip(ShipBase ship)
    {
        //Adding the ship proper to the game
        ship.setFiringPrimed(true); //DEBUG
        addActor(ship);
        //Adding the turrets to the game
        List<TurretBase> turrets = ship.getTurrets();
        for(TurretBase turret : turrets)
        {
            addActor(turret);
            //Add firing points
            List<FiringPoint> firingPoints = turret.getFiringPoints();
            for(FiringPoint firingPoint : firingPoints)
            {
                addActor(firingPoint);
            }
        }
        //Adding the collision circles to the game for debug rendering
        ship.getCollisionShape().addCollisionCirclesToStage(this);
        //Adding to the collision grid for collision tile based processing
        collisionGrid.introduceGridObject(ship);
        setUserControlledShip(ship);
        //Coordinating the background layer to the new ship rotation speed
        GlobalServices.getGlobalServices().setPlayerRotationCapacity(ship.getRotationCapacity());
    }

    public void spawnAiShip(ShipBase ship)
    {
        //Adding the ship proper to the game
        addActor(ship);
        //Adding the turrets to the game
        List<TurretBase> turrets = ship.getTurrets();
        for(TurretBase turret : turrets)
        {
            addActor(turret);
            //Adding the firing points for the turret
            List <FiringPoint> firingPoints = turret.getFiringPoints();
            for(FiringPoint firingPoint : firingPoints)
            {
                addActor(firingPoint);
            }
            //Adding the collision circles for debug rendering
            ship.getCollisionShape().addCollisionCirclesToStage(this);
            //Adding to the collision grid for tile based processing
            collisionGrid.introduceGridObject(ship);
        }
    }

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    private void setUserControlledShip(ShipBase ship)
    {
        shipCamera.setShip(ship);
        gameplayCursor.setShip(ship);
        humanController.setControlledShip(ship);
    }
}
