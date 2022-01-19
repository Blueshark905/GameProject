package com.parkhon.dabyss.game.system.controller;

import com.parkhon.dabyss.game.entity.character.ship.ShipBase;

public abstract class DAController
{
    protected ShipBase ship;
    public DAController(){};
    public DAController(ShipBase shipControlled){
        this.ship = shipControlled;
    }

    public void setControlledShip(ShipBase ship)
    {
        this.ship = ship;
    }
}
