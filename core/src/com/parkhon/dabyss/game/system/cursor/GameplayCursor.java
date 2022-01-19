package com.parkhon.dabyss.game.system.cursor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.parkhon.dabyss.game.entity.character.ship.ShipBase;
import com.parkhon.dabyss.game.system.camera.ShipCamera;

public class GameplayCursor extends Actor
{
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Graphic
    private Texture texture;
    private TextureRegion textureRegion;

    //Controls
    private float sensitivity;

    //Position
    private Vector2 relativePosition;
    private Vector2 shipPosition;

    //Movement
    private float screenX;
    private float screenY;

    //Settings
    private boolean cursorActivated;

    //Data Polling
    private ShipBase ship;
    private ShipCamera camera;

    //Data
    private float maxCursorRange;

    //Screen displacement
    private float minScreenDimension;
    private float maxSightDistance;
    private float displacementRatioX;
    private float displacementRatioY;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public GameplayCursor(ShipCamera camera)
    {
        //this.ship = ship;
        this.camera = camera;
        setPosition(100,100);
        texture = new Texture("aim_assist.png");
        textureRegion = new TextureRegion(texture);
        screenX = 0.0f;
        screenY = 0.0f;
        cursorActivated = true;
        shipPosition = new Vector2(0,0);
        relativePosition = new Vector2(0,0);
        minScreenDimension = 0.0f;
        //DEBUG
        sensitivity = 10.0f;
        maxCursorRange = 2000;
        //END DEBUG
        displacementRatioX = 0.0f;
        displacementRatioY = 0.0f;
        maxSightDistance = 0.0f;
        //Getting max ranges
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(cursorActivated && ship != null) {
            batch.draw(
                    textureRegion, this.getX(), this.getY(),
                    texture.getWidth() / 2, texture.getHeight() / 2,
                    texture.getWidth(), texture.getHeight(), 1, 1, getRotation());
        }
    }

    @Override
    public void act(float delta) {
        if(ship != null) {
            updateShipPosition();
            if (cursorActivated) {
                offsetCursorToRangeRatio();
                moveCursor(delta);
                Gdx.input.setCursorPosition(0, 0);
                super.act(delta);
            }
            updateCursor();
        }
    }


    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public void start()
    {
        //Capturing the system cursor
        Gdx.input.setCursorPosition(0,0);
    }

    public float getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(float sensitivity) {
        this.sensitivity = sensitivity;
    }

    public void moveScreenCursor(float screenX, float screenY)
    {
        this.screenX = -screenX;
        this.screenY = screenY;
    }

    public boolean isCursorActivated() {
        return cursorActivated;
    }

    public void setCursorActivated(boolean cursorActivated) {
        Gdx.input.setCursorCatched(cursorActivated);
        this.cursorActivated = cursorActivated;
    }

    public void updateShipPosition()
    {
        shipPosition.set(ship.getX(), ship.getY());
    }

    public float getAimX()
    {
        return getX() + texture.getWidth() / 2;
    }

    public float getAimY()
    {
        return getY() + texture.getHeight() / 2;
    }

    public void offsetCursorToRangeRatio()
    {
        /*
        //Preparing the matrix to transform from default basis to ship basis
        shipBasisTransformMatrix.set(
                MathUtils.cos(getRotation() * MathUtils.degreesToRadians), MathUtils.cos((getRotation() + 90) * MathUtils.degreesToRadians),
                MathUtils.sin(getRotation() * MathUtils.degreesToRadians), MathUtils.sin((getRotation() + 90) * MathUtils.degreesToRadians)
                );
        //Preparing the matrix to transform from ship basis to default basis
        CommonOps_DDF2.invert(shipBasisTransformMatrix, shipBasisTransformMatrix);
        //Getting the ship basis coordinates for our cursor's position. (Basis change)
        shipXs = (float)(relativePosition.x * shipBasisTransformMatrix.a11 + relativePosition.y * shipBasisTransformMatrix.a12);
        shipYs = (float)(relativePosition.x * shipBasisTransformMatrix.a21 + relativePosition.y * shipBasisTransformMatrix.a22);
        //Getting the ratio of screen displacement that should be effected based on cursor position
        displacementRatioX = -(shipXs / maxSightDistance) * MathUtils.cos((getRotation() + 180) * MathUtils.degreesToRadians) -
                (shipYs / maxSightDistance) * MathUtils.sin(getRotation() * MathUtils.degreesToRadians);
        displacementRatioY = (shipYs / maxSightDistance) * MathUtils.cos(getRotation() * MathUtils.degreesToRadians) -
                (shipXs / maxSightDistance) * MathUtils.sin((getRotation() + 180) * MathUtils.degreesToRadians);
        //The modifier percentage to the maxSightDistance mult at the end is the variable we have to compute. If none is present we center at cursor.
        camera.setDisplacementX(displacementRatioX * maxSightDistance - (displacementRatioX * minScreenDimension / 2 * 0.9f)); //Screen Dimensions not working here, need world units
        camera.setDisplacementY(displacementRatioY * maxSightDistance - (displacementRatioY * minScreenDimension / 2 * 0.9f));
         */
        //Getting the ratio of screen displacement that should be effected based on cursor position
        displacementRatioX = relativePosition.x / maxSightDistance;
        displacementRatioY = relativePosition.y / maxSightDistance;
        //The modifier percentage to the maxSightDistance mult at the end is the variable we have to compute. If none is present we center at cursor.
        camera.setDisplacementX(displacementRatioX * maxSightDistance - (displacementRatioX * minScreenDimension / 2 * 0.9f)); //Screen Dimensions not working here, need world units
        camera.setDisplacementY(displacementRatioY * maxSightDistance - (displacementRatioY * minScreenDimension / 2 * 0.9f));
    }

    public void resize()
    {
        float width = getStage().getCamera().viewportWidth;
        float height = getStage().getCamera().viewportHeight;
        //Get the minimum screen dimension
        if(width < height) {
            minScreenDimension = width;
        } else {
            minScreenDimension = height;
        }
        if(maxCursorRange > minScreenDimension) {
            maxSightDistance = maxCursorRange;
        }
    }

    public ShipBase getShip() {
        return ship;
    }

    public void setShip(ShipBase ship) {
        this.ship = ship;
    }

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    private void moveCursor(float delta)
    {
        relativePosition.add(
                -screenX * sensitivity * delta
                ,
                -screenY * sensitivity * delta
        );
        setZIndex(999999);
        //We already moved, resetting movement data
        screenX = 0.0f;
        screenY = 0.0f;
    }

    private void updateCursor()
    {
        if(relativePosition.len() > maxCursorRange) {
            //We move the cursor to the furthest legal range in that direction
            relativePosition.nor();
            relativePosition.scl(maxCursorRange);
        }
        setPosition(
                shipPosition.x + ship.getWidth() / 2 - texture.getWidth() / 2 + relativePosition.x
                ,
                shipPosition.y + ship.getHeight() / 2 - texture.getHeight() / 2 + relativePosition.y
        );
    }
}
