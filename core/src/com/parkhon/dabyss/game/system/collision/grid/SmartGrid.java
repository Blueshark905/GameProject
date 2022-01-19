package com.parkhon.dabyss.game.system.collision.grid;

import com.badlogic.gdx.math.Vector2;
import com.parkhon.dabyss.game.system.collision.CollisionUtils;
import com.parkhon.dabyss.game.system.collision.effect.CollisionEffect;
import com.parkhon.dabyss.game.system.collision.shape.CircleCollisionDetector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SmartGrid
{
    /*
    SmartGrid places all objects in the game world on a dynamically generated grid, and can then service object data
    to various services to empower the gameplay.

    IDEA: when grid objects are not alive, they have a grace period before they are deleted from the game, that way
    we avoid the various threads from using them when they have been deleted, and thus avoid null pointer exceptions.
    Something like 5 seconds.
     */
    //Variables----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Map Data: Subclass or instance
    private float mapWidth;
    private float mapHeight;
    private int width;
    private int height;
    private float tileLength; //Note that tiles are squares and must be larger than the largest ship.
    private Vector2 mapStartingCoord; //Not 0,0

    //Grid
    private TileCollection[][] grid;

    //Collision detector
    CircleCollisionDetector collisionDetector;

    //Mechanical
    private int collisionObjectX;
    private int collisionObjectY;
    private List<GridObject> pendingIntroduction;
    private HashMap<String, Boolean> object2Object; //Hash for not hit testing collisions that were previously computed.
    private int adjacentX;
    private int adjacentY;
    private int subjectPriority;
    private int objectPriority;
    private ArrayList<TileCollection> neighborTiles;

    //Constructor--------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    public SmartGrid()
    {
        width = 0;
        height = 0;
        collisionObjectX = 0;
        collisionObjectY = 0;
        collisionDetector = new CircleCollisionDetector();
        pendingIntroduction = Collections.synchronizedList(new ArrayList<GridObject>());
        object2Object = new HashMap<>();
        adjacentX = 0;
        adjacentY = 0;
        subjectPriority = 0;
        objectPriority = 0;
        neighborTiles = new ArrayList<>();
    }

    //Overrides----------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    //Methods------------------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    public void update()
    {
        //Add objects pending introduction
        addNewGridObjects();
        /*
        for(GridObject subject : pendingIntroduction)
        {
            addGridObjects(subject);
        }
        gridObjectAddedCleanup(); //Clean up the pending addition list
         */
        //Update tiles
        for(int i = 0; i < grid.length; i++)
        {
            for(int j = 0; j < grid[0].length; j++)
            {
                TileCollection tile = grid[i][j];
                removeDeadObjects(tile);
                updateGridObjectsPosition(tile);
                updateCollisionShapes(tile);
                deliverTileCollisionEffects(tile);
            }
        }
        //Update the position of all GridObjects
        //Check for collisions of GridObjects
    }

    //Grid configuration methods
    public void start()
    {
        generateGrid(); //Generating the grid from the specified dimensions
        //Testing
        System.out.println(grid.length + " and " + grid[0].length);
    }

    public float getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(float mapWidth) {
        this.mapWidth = mapWidth;
    }

    public float getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(float mapHeight) {
        this.mapHeight = mapHeight;
    }

    public Vector2 getMapStartingCoord() {
        return mapStartingCoord;
    }

    public void setMapStartingCoord(Vector2 mapStartingCoord) {
        this.mapStartingCoord = mapStartingCoord;
    }

    public float getTileLength() {
        return tileLength;
    }

    public void setTileLength(float tileLength) {
        this.tileLength = tileLength;
    }

    public void introduceGridObject(GridObject gridObject)
    {
        pendingIntroduction.add(gridObject);
    }



    //Technical Methods--------------------------------------------------
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------

    private boolean checkObjectInBounds(int boundsX, int boundsY)
    {
        if(boundsX < 0 || boundsY < 0 || boundsX >= grid.length || boundsY >= grid[0].length)
        {
            //object drifted out of the map
            return false;
        } else {
            //object is inside map
            return true;
        }
    }

    private void generateGrid()
    {
        System.out.println("Generating grid");
        //Generate a grid with the correct dimensions based on the map size and tile length.
        //Grid uses [x,y] matrix notation
        grid = new TileCollection[(int)Math.ceil(mapWidth / tileLength)][(int)Math.ceil(mapHeight / tileLength)];
        for(int i = 0; i < grid.length; i++)
        {
            for(int j = 0; j < grid[0].length; j++)
            {
                grid[i][j] = new TileCollection(i, j);
            }
        }
        //Registering width and height in tiles
        width = grid.length;
        height = grid[0].length;
    }

    //Services
    private void deliverTileCollisionEffects(TileCollection tile)
    {
        /*
        This method defers to the collision algorithm to detect if a collision is in effect.

        If a collision is present, it processes the collition and queues up the proper collision event for the
        game objects to effect at their convenience.
         */
        //Getting the adjacent tiles. Current tile is tile22. The matrix is a 3x3.
        //System.out.println("own tile " + tile);
        //11
        adjacentX = tile.getX() - 1;
        adjacentY = tile.getY() + 1;
        TileCollection tile11 = (adjacentX >= 0 && adjacentY < height) ? grid[adjacentX][adjacentY]: null;
        //System.out.println("a11 " + tile11);
        //21
        adjacentX = tile.getX();
        adjacentY = tile.getY() + 1;
        TileCollection tile21 = (adjacentY < height) ? grid[adjacentX][adjacentY]: null;
        //System.out.println("a21 " + tile21);
        //31
        adjacentX = tile.getX() + 1;
        adjacentY = tile.getY() + 1;
        TileCollection tile31 = (adjacentX < width && adjacentY < height) ? grid[adjacentX][adjacentY]: null;
        //System.out.println("a31 " + tile31);

        //12
        adjacentX = tile.getX() - 1;
        adjacentY = tile.getY();
        TileCollection tile12 = (adjacentX >= 0) ? grid[adjacentX][adjacentY]: null;
        //System.out.println("a12 " + tile12);
        //32
        adjacentX = tile.getX() + 1;
        adjacentY = tile.getY();
        TileCollection tile32 = (adjacentX < width) ? grid[adjacentX][adjacentY]: null;
        //System.out.println("a32 " + tile32);

        //13
        adjacentX = tile.getX() - 1;
        adjacentY = tile.getY() - 1;
        TileCollection tile13 = (adjacentX >= 0 && adjacentY >= 0) ? grid[adjacentX][adjacentY]: null;
        //System.out.println("a13 " + tile13);
        //23
        adjacentX = tile.getX();
        adjacentY = tile.getY() - 1;
        TileCollection tile23 = (adjacentY >= 0) ? grid[adjacentX][adjacentY]: null;
        //System.out.println("a23 " + tile23);
        //33
        adjacentX = tile.getX() + 1;
        adjacentY = tile.getY() - 1;
        TileCollection tile33 = (adjacentX < width && adjacentY >= 0) ? grid[adjacentX][adjacentY]: null;
        //System.out.println("a33 " + tile33);

        //Adding neighbor tiles
        neighborTiles.clear();
        neighborTiles.add(tile11);
        neighborTiles.add(tile21);
        neighborTiles.add(tile31);

        neighborTiles.add(tile12);
        neighborTiles.add(tile32);

        neighborTiles.add(tile13);
        neighborTiles.add(tile23);
        neighborTiles.add(tile33);


        //Processing collisions for all grid objects in the tile.
        for(GridObject subject : tile.getGridObjects())
        {
            //Processing collisions for the current tile
            for(GridObject gridObject: tile.getGridObjects())
            {
                long minId = Math.min(subject.getGameId(), gridObject.getGameId());
                long maxId = Math.max(subject.getGameId(), gridObject.getGameId());
                if(subject.getGameId() != gridObject.getGameId() && subject.canCollide() && gridObject.canCollide()
                        && CollisionUtils.isValidCollision(subject, gridObject)
                        && !object2Object.containsKey(minId + "+" + maxId))
                {
                    //If its not the same object and this combination of game id's has not been tried before
                    object2Object.put(minId + "+" + maxId, null); //Adding it to the memo as we are now testing it
                    //Testing for collision
                    if(collisionDetector.hit(subject, gridObject))
                    {
                        deliverEffects(subject, gridObject);
                    }
                }
            }
            //Flushing the memo
            object2Object.clear();
            //Processing collisions for neighbors
            for(TileCollection neighborTile : neighborTiles)
            {
                if(neighborTile != null)
                {
                    //If there is a neighbor tile
                    //For every grid object in the neighbor tile
                    for(GridObject gridObject : neighborTile.getGridObjects())
                    {
                        long minId = Math.min(subject.getGameId(), gridObject.getGameId());
                        long maxId = Math.max(subject.getGameId(), gridObject.getGameId());
                        if(subject.canCollide() && gridObject.canCollide() && CollisionUtils.isValidCollision(subject, gridObject)
                                && !object2Object.containsKey(minId + "+" + maxId))
                        {
                            //This collision has not been computed before
                            object2Object.put(minId + "+" + maxId, null); //Adding this collision pair to the memo
                            //Testing for collision
                            if(collisionDetector.hit(subject, gridObject))
                            {
                                deliverEffects(subject, gridObject);
                            }
                        }
                    }
                    //All collisions for the subject and this neighbor tile processed, flushing memo
                    object2Object.clear();
                }
            }
        }
        //OLD VERSION
        /*
        object2Object.clear(); //Clearing the memo for collision hit testing
        //
        collisionObjectX = (int)Math.ceil(subject.getCollisionX() / tileLength);
        collisionObjectY = (int)Math.ceil(subject.getCollisionY() / tileLength);
        for(GridObject gridObject : grid[collisionObjectX][collisionObjectY].getGridObjects())
        {
            //Remove dead objects from the grid
            if(!gridObject.isAlive()) {
                grid[collisionObjectX][collisionObjectY].getGridObjects().remove(gridObject);
                continue;
            }
            //Delegate collision detection
            if(collisionDetector.hit(subject, gridObject))
            {
                //Object hit case
                return gridObject;
            }
        }

         */
    }

    private void removeDeadObjects(TileCollection tile)
    {
        for(int i = 0; i < tile.getGridObjects().size(); i++)
        {
            GridObject subject = tile.getGridObjects().get(i);
            if(!subject.isAlive())
            {
                tile.getGridObjects().remove(i);
                i--; //Repeat this index
            }
        }
    }

    private void updateGridObjectsPosition(TileCollection tile)
    {
        for(int i = 0; i < tile.getGridObjects().size(); i++)
        {
            GridObject subject = tile.getGridObjects().get(i);
            collisionObjectX = (int)Math.floor(subject.getCollisionX() / tileLength);
            collisionObjectY = (int)Math.floor(subject.getCollisionY() / tileLength);
            //If the subject should be placed on a different tile
            if(collisionObjectX != tile.getX() || collisionObjectY != tile.getY()) {
                //Sanitization
                if (!checkObjectInBounds(collisionObjectX, collisionObjectY)) {
                    //Object drifted out of the map
                    //TODO: teleport back to the map or do something else
                } else {
                    //Legal change
                    //Add to add grid object
                    grid[collisionObjectX][collisionObjectY].getGridObjects().add(subject);
                    /*
                    System.out.println("Grid X: " + collisionObjectX + " Grid Y: " + collisionObjectY + " Object: "
                            + grid[collisionObjectX][collisionObjectY]);
                     */
                    //Remove from current grid
                    tile.getGridObjects().remove(subject);
                    i--; //Repeat the iteration
                }
            }
        }
        //
    }

    private void updateCollisionShapes(TileCollection tile)
    {
        //Processing all grid objects in the tile
        for(GridObject subject : tile.getGridObjects())
        {
            //Updating the position of the collision shape, and thus all its collision circles
            subject.getCollisionShape().updateShipPosition(subject.getCollisionX(), subject.getCollisionY(), subject.getCollisionAngle());
        }
    }

    /*
    private void addGridObjects(GridObject gridObject)
    {
        collisionObjectX = (int)Math.floor(gridObject.getCollisionX() / tileLength);
        collisionObjectY = (int)Math.floor(gridObject.getCollisionY() / tileLength);
        //Sanitization
        if(!checkObjectInBounds(collisionObjectX, collisionObjectY))
        {
            //object drifted out of the map
            //TODO: teleport back to the map or do something else
            System.out.println("Object out of bounds "  + collisionObjectX + " and " + collisionObjectY);
        } else {
            //legal to add grid object
            //Adding object to grid
            grid[collisionObjectX][collisionObjectY].getGridObjects().add(gridObject);
            //Object is added, so removing it from list of objects that need introduction to the grid
            gridObject.setPendingAddition(false);
            System.out.println("Object added to grid");
        }
    }

    private void gridObjectAddedCleanup()
    {
        //Clean the pending additions list so that it no longer contains objects already added.
        for(int i = 0; i < pendingIntroduction.size(); i++)
        {
            GridObject gridObject = pendingIntroduction.get(i);
            if(!gridObject.isPendingAddition())
            {
                //Object was already added
                pendingIntroduction.remove(gridObject); //Multi thread safety handled
                i--; //Repeat loop with next object on the list
            }
        }
    }

     */

    private void addNewGridObjects()
    {
        //While there are grid objects pending introduction
        while(pendingIntroduction.size() > 0)
        {
            //Getting the object to add
            GridObject gridObject = pendingIntroduction.get(0);
            //Adding the object to the grid
            collisionObjectX = (int)Math.floor(gridObject.getCollisionX() / tileLength);
            collisionObjectY = (int)Math.floor(gridObject.getCollisionY() / tileLength);
            //Sanitization
            if(!checkObjectInBounds(collisionObjectX, collisionObjectY))
            {
                //object drifted out of the map
                //TODO: teleport back to the map or do something else
                //System.out.println("Object out of bounds "  + collisionObjectX + " and " + collisionObjectY);
                //Adding to 0,0 DEBUG
                grid[0][0].getGridObjects().add(gridObject);
            } else {
                //legal to add grid object
                //Adding object to grid
                grid[collisionObjectX][collisionObjectY].getGridObjects().add(gridObject);
                //System.out.println("Object added to grid");
            }
            //Add collision manager ghost
            //
            //...
            //Removing the object from the pending introduction list
            pendingIntroduction.remove(0);
        }
    }

    private void deliverEffects(GridObject subject, GridObject gridObject)
    {
        subjectPriority = CollisionUtils.getCollisionEventPriorityWeight(subject.getCollisionEventPriority());
        objectPriority = CollisionUtils.getCollisionEventPriorityWeight(gridObject.getCollisionEventPriority());
        //There is a hit. Apply effector
        GridObject greatestEffector;
        GridObject equalEffector = null;
        GridObject affectedObject = null;
        if(subjectPriority > objectPriority) {
            greatestEffector = subject;
            affectedObject = gridObject;
        } else if(subjectPriority < objectPriority) {
            greatestEffector = gridObject;
            affectedObject = subject;
        } else {
            //Equality so both will be affected
            greatestEffector = subject;
            equalEffector = gridObject;
        }
        //Applying the greatest effector on the lowest effector
        if(equalEffector == null)
        {
            CollisionEffect effect = greatestEffector.deliverEffect();
            if(effect != null) {
                //CASE: One object has higher priority
                affectedObject.receiveEffect(effect);
            }
        } else {
            CollisionEffect equalEffect = equalEffector.deliverEffect();
            CollisionEffect greatestEffect = greatestEffector.deliverEffect();
            //CASE: Both elements have the same priority
            if(greatestEffect != null) {
                equalEffector.receiveEffect(greatestEffect);
            }
            if(equalEffect != null) {
                greatestEffector.receiveEffect(equalEffect);
            }
        }
    }

}
