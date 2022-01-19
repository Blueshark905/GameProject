package com.parkhon.dabyss.game.system.controller;

import com.badlogic.gdx.Input;
import com.parkhon.dabyss.game.entity.character.turret.TurretBase;
import com.parkhon.dabyss.game.system.cursor.GameplayCursor;

public class HumanController extends DAController
{
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Interaction
    private GameplayCursor cursor;

    //Mechanical
    private boolean keyDownHandled;
    private boolean keyUpHandled;
    private boolean mouseMovedHandled;

    //Development
    private boolean gameplayCursorActivated;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    public HumanController(GameplayCursor cursor){
        this.cursor = cursor;
        keyDownHandled = false;
        keyUpHandled = false;
        mouseMovedHandled = false;
        //Development
        gameplayCursorActivated = true;
        //END Development
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    public void update()
    {
        if(ship != null) {
            //Updating player turrets to point to new coordinates
            for (TurretBase turret : ship.getTurrets()) {
                turret.aimTo(cursor.getAimX(), cursor.getAimY());
            }
        }
    }

    public void start()
    {
        cursor.setCursorActivated(gameplayCursorActivated);
        cursor.start();
    }

    public boolean keyDown(int keycode) {
        keyDownHandled = false;
        if(ship != null)
        {
            if(keycode == Input.Keys.valueOf(",")) {
                ship.setMovingForward(true);
                keyDownHandled = true;
            } if(keycode ==Input.Keys.valueOf("O")) {
                ship.setMovingBackwards(true);
                keyDownHandled = true;
            } if(keycode == Input.Keys.valueOf("A")) {
                ship.setMovingLeft(true);
                keyDownHandled = true;
            } if(keycode == Input.Keys.valueOf("E")) {
                ship.setMovingRight(true);
                keyDownHandled = true;
            } if(keycode == Input.Keys.valueOf(";")) {
                ship.setRotatingCounterClockwise(true);
                keyDownHandled = true;
            } if(keycode == Input.Keys.valueOf(".")) {
                ship.setRotatingClockwise(true);
                keyDownHandled = true;
            } if(keycode == Input.Keys.ALT_LEFT) {
                gameplayCursorActivated = !gameplayCursorActivated;
                cursor.setCursorActivated(gameplayCursorActivated);
            }
        }
        return keyDownHandled;
    }

    public boolean keyUp(int keycode) {
        keyUpHandled = false;
        if(ship != null){
            if(keycode == Input.Keys.valueOf(",")) {
                ship.setMovingForward(false);
                keyUpHandled = true;
            } else if(keycode == Input.Keys.valueOf("O")) {
                ship.setMovingBackwards(false);
                keyUpHandled = true;
            } if(keycode == Input.Keys.valueOf("A")) {
                ship.setMovingLeft(false);
                keyUpHandled = true;
            } else if(keycode == Input.Keys.valueOf("E")) {
                ship.setMovingRight(false);
                keyUpHandled = true;
            } if(keycode == Input.Keys.valueOf(";")) {
                ship.setRotatingCounterClockwise(false);
                keyUpHandled = true;
            } else if(keycode == Input.Keys.valueOf(".")) {
                ship.setRotatingClockwise(false);
                keyUpHandled = true;
            }
        }
        return keyUpHandled;
    }

    public boolean mouseMoved(int screenX, int screenY) {

        if(gameplayCursorActivated) {
            mouseMovedHandled = true;
            cursor.moveScreenCursor(screenX, screenY);
        } else {
            mouseMovedHandled = false;
        }
        return mouseMovedHandled;
    }

    public boolean isControllingShip()
    {
        return (ship == null) ? false : true;
    }

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
