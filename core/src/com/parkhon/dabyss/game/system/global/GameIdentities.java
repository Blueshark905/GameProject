package com.parkhon.dabyss.game.system.global;

public class GameIdentities
{
    private static long idCounter = 0;

    public static long serveId()
    {
        return idCounter++;
    }
}
