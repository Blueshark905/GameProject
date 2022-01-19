package com.parkhon.dabyss;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.parkhon.dabyss.game.screen.TestingScreen;
import com.parkhon.dabyss.game.screen.MainmenuScreen;
import com.parkhon.dabyss.game.system.global.GlobalServices;

public class DabyssGame extends Game {
	/*
	TODO: Firing point pool remains untested, test when firing points and ships can die and be respawned
	 */
	//Variables----------------------------------------------------------
	//-------------------------------------------------------------------
	//-------------------------------------------------------------------
	//Dev
	private TestingScreen testingScreen;
	private MainmenuScreen mainmenuScreen;
	//Gameplay
	private Screen activeScreen;
	//App
	private final static int HEIGHT = 1080;
	private final static int WIDTH = 1920;
	private final static String TITLE = "Dreadnought Abyss";
	//Vanilla
	SpriteBatch batch;

	//Overrides----------------------------------------------------------
	//-------------------------------------------------------------------
	//-------------------------------------------------------------------
	
	@Override
	public void create () {
		//Print development notes
		System.out.println("Firing point pool remains untested. Test when ships can be destroyed and created!");
		System.out.println("Firing point pending recycle(). Also, recycle in firing point remains untested.");
		System.out.println("Next TODO: Implement pooling, recycling and pooled factories for the turrets and the ships.");
		System.out.println("Rotation Restriciton pool remains untested.");
		System.out.println("Ship pool remains untested.");
		//Configuring game
		GlobalServices.getGlobalServices().setCollisionVisible(false);
		GlobalServices.getGlobalServices().setTurretFirePointVisible(false);
		GlobalServices.getGlobalServices().setDebugEnabled(false);
		//Starting game

		//
		batch = new SpriteBatch();
		//Testing Screen
		testingScreen = new TestingScreen(batch);
		activeScreen = testingScreen;
		//Main menu screen
		mainmenuScreen = new MainmenuScreen(batch);
		setScreen(activeScreen);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		super.dispose();
		Gdx.app.exit();
	}

	@Override
	public void pause() {

	}

	//Methods------------------------------------------------------------
	//-------------------------------------------------------------------
	//-------------------------------------------------------------------

	public static int getHEIGHT() {
		return HEIGHT;
	}

	public static int getWIDTH() {
		return WIDTH;
	}

	public static String getTITLE() {
		return TITLE;
	}


	//Technical Methods--------------------------------------------------
	//-------------------------------------------------------------------
	//-------------------------------------------------------------------

}
