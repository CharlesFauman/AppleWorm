package org.fauman.appleworm.storage;

import java.util.HashMap;
import java.util.Map;

import org.fauman.appleworm.states.*;
import processing.core.PApplet;
import org.fauman.appleworm.util.IntPair;

public final class Model {
	private static final Model instance = new Model();

	public static Model getInstance(PApplet _p_app) {
		p_app = _p_app;
		sound_controller = SoundController.getInstance();
		return instance;
	}
	
	public static enum StateEnum {
		MAIN_MENU, LEVEL_SELECT, GAME, CUSTOM_LEVEL_SELECT, LEVEL_MAKER;
		
		private static Map<StateEnum, State> stateMap = new HashMap<>();
	    
	    static {
	    	stateMap.put(MAIN_MENU, MainMenuState.getInstance());
	    	stateMap.put(LEVEL_SELECT, LevelSelectState.getInstance());
	    	stateMap.put(GAME, GameState.getInstance());
	    	stateMap.put(CUSTOM_LEVEL_SELECT, CustomLevelSelectState.getInstance());
	    	stateMap.put(LEVEL_MAKER, LevelMakerState.getInstance());
	    }
	    
	    public State getState() {
	    	return stateMap.get(this);
	    }
	    
	};
	
	public Model() {
		reset();
	}
	
	// hardcoded settings _____________________________________:
	public final static IntPair size = new IntPair(1000, 750);
	public final static int num_levels = 30;
	
	// hardcoded settings --------------------------------------:
	
	// general useful
	public static State active_state;
	public static PApplet p_app;
	public static  SoundController sound_controller;
	
	public static int current_level;
	public static int current_custom_num;
	public static boolean current_map_is_custom;
	public static String current_map_path;
	
	public static int unlocked_level;
	
	public static boolean[] finished_levels;
	
	
	public void reset() {
		current_level = 1;
		current_custom_num = 1;
		current_map_is_custom = false;
		unlocked_level = 3;
		finished_levels = new boolean[num_levels];
	}
	
	public static void switchToState(StateEnum state_enum) {
		State to_state = state_enum.getState();
		
		if(active_state != null) active_state.exit(to_state);
		to_state.enter(active_state);
		
		active_state = to_state;
	}
}
