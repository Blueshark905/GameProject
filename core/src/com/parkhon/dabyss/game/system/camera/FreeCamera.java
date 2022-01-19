package com.parkhon.dabyss.game.system.camera;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class FreeCamera extends Actor
{
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    private Viewport viewport;
    private OrthographicCamera camera;
    private Vector3 direction;
    private Vector3 newDirection; //Used to move in a given direction
    private float rotation;
    private float rotationSpeed;
    //Velocity
    private float minVelocity = 750;
    private float maxVelocity = 1500;
    private float timeMoving = 0.0f;
    private float timeTillMaxVelocity = 2.0f;
    //
    private boolean toggledUp;
    private boolean toggledDown;
    private boolean toggledLeft;
    private boolean toggledRight;
    private boolean toggledCounterClockwise;
    private boolean toggledClockwise;
    private float dt; //delta time
    private float angle; //Camera angle
    //Ideal game screen proportions for the viewport to approximate
    private int idealWidth;
    private int idealHeight;
    private float zoomingFactor;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    public FreeCamera()
    {
        setPosition(0, 0);
        direction = new Vector3(0,0,0);
        newDirection = new Vector3(0,0,0);
        rotation = 0;
        angle = 0;
        //
        rotationSpeed = 90.0f;
        //
        dt = 1;
        //Camera Preferences
        zoomingFactor = 150;
        idealWidth = (int)(16 * zoomingFactor);
        idealHeight = (int)(9 * zoomingFactor);
        //Creating the camera and viewports
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(idealWidth, idealHeight, camera);
        //Actor settings
        setVisible(false);
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                boolean handled = false;
                if(keycode == Input.Keys.valueOf(",")) {
                    toggledUp = true;
                    handled = true;
                } if(keycode ==Input.Keys.valueOf("O")) {
                    toggledDown = true;
                    handled = true;
                } if(keycode == Input.Keys.valueOf("A")) {
                    toggledLeft = true;
                    handled = true;
                } if(keycode == Input.Keys.valueOf("E")) {
                    toggledRight = true;
                    handled = true;
                } if(keycode == Input.Keys.valueOf(";")) {
                    toggledCounterClockwise = true;
                    handled = true;
                } if(keycode == Input.Keys.valueOf(".")) {
                    toggledClockwise = true;
                    handled = true;
                }
                return handled;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                boolean handled = false;
                if(keycode == Input.Keys.valueOf(",")) {
                    toggledUp = false;
                    handled = true;
                } else if(keycode == Input.Keys.valueOf("O")) {
                    toggledDown = false;
                    handled = true;
                } if(keycode == Input.Keys.valueOf("A")) {
                    toggledLeft = false;
                    handled = true;
                } else if(keycode == Input.Keys.valueOf("E")) {
                    toggledRight = false;
                    handled = true;
                } if(keycode == Input.Keys.valueOf(";")) {
                    toggledCounterClockwise = false;
                    handled = true;
                } else if(keycode == Input.Keys.valueOf(".")) {
                    toggledClockwise = false;
                    handled = true;
                }
                return handled;
            }
        });
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    @Override
    public void act(float delta) {
        this.dt = delta;
        boolean moved = false;
        if(toggledUp) {
            moveUp();
            moved = true;
        } else if (toggledDown) {
            moveDown();
            moved = true;
        } if (toggledLeft) {
            moveLeft();
            moved = true;
        } else if (toggledRight) {
            moveRight();
            moved = true;
        }
        if(moved)  {
            timeMoving += delta;
            completeMovement(); //Calculate where the camera should be now
            camera.position.set(getX(), getY(), 0); //Move the camera to the target location
        } else {
            timeMoving = 0.0f;
        }
        boolean rotated= false;
        if(toggledCounterClockwise) {
            rotateCounterClockwise();
            rotated = true;
        } else if(toggledClockwise) {
            rotateClockwise();
            rotated = true;
        }
        if(rotated) {
            angle += rotation;
            angle = angle % 360;
            camera.rotate(rotation);
            this.rotation = 0; //Rotation value already delivered.
        }
        super.act(delta);
    }



    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    public void moveRight()
    {
        //Adding the newly selected direction to our final direction.
        newDirection.set(MathUtils.cos(angle * MathUtils.PI / 180), -MathUtils.sin(angle * MathUtils.PI / 180), 0);
        direction.add(newDirection);
        direction.nor();
        //Reseting the newDirection to allow for a later calculation
        newDirection.set(0,0,0);
    }
    public void moveLeft()
    {
        //Adding the newly selected direction to our final direction.
        newDirection.set(-MathUtils.cos(angle * MathUtils.PI / 180), MathUtils.sin(angle * MathUtils.PI / 180), 0);
        direction.add(newDirection);
        direction.nor();
        //Reseting the newDirection to allow for a later calculation
        newDirection.set(0,0,0);
    }
    public void moveUp()
    {
        //Adding the newly selected direction to our final direction.
        newDirection.set(MathUtils.sin(angle * MathUtils.PI / 180), MathUtils.cos(angle * MathUtils.PI / 180), 0);
        direction.add(newDirection);
        direction.nor();
        //Reseting the newDirection to allow for a later calculation
        newDirection.set(0,0,0);
    }
    public void moveDown()
    {
        //Adding the newly selected direction to our final direction.
        newDirection.set(-MathUtils.sin(angle * MathUtils.PI / 180), -MathUtils.cos(angle * MathUtils.PI / 180), 0);
        direction.add(newDirection);
        direction.nor();
        //Reseting the newDirection to allow for a later calculation
        newDirection.set(0,0,0);
    }
    public void completeMovement()
    {
        //Scaling the direction with the movement speed for our delta time prediction.
        double targetVelocity;
        if(timeMoving < timeTillMaxVelocity) {
            targetVelocity = minVelocity + (maxVelocity - minVelocity) / Math.pow(timeTillMaxVelocity, 2) * Math.pow(timeMoving, 2);
        } else {
            targetVelocity = maxVelocity;
        }
        direction.scl((float)(targetVelocity) * dt);
        //Moving in the given direction
        moveBy(direction.x, direction.y);
        //Reset the direction
        direction.set(0,0,0);
    }
    public void rotateCounterClockwise()
    {
        this.rotation = (-rotationSpeed * dt);
    }
    public void rotateClockwise()
    {
        this.rotation = (rotationSpeed * dt);
    }

    public Viewport getViewport() {
        return viewport;
    }

    public Matrix4 getCombinedMatrices()
    {
        return viewport.getCamera().combined;
    }

    public void start()
    {
        //
        viewport.apply();
    }

    public void resize(int width, int height)
    {
        viewport.update(width, height);
    }

    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
}
