package states;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import storage.Model;
import util.Direction;
import util.IntPair;
import util.gui.Button;
import util.gui.StateObjectManager;
import util.mouse.ClickManager;
import util.mouse.EllipseButton;

public final class GameState extends State{
	private static final GameState instance = new GameState();
	
	public static GameState getInstance(){ return instance; }
	
	public GameState() {
		background = Model.p_app.loadImage("game_background.png");
	}
	
	private void initialize_buttons() {
		click_manager = new ClickManager();
		state_object_manager = new StateObjectManager();
		
		float reset_button_size = ((float)Model.size.getX())/14;
		float reset_button_x = ((float)Model.size.getX())/16;
		float reset_button_y = ((float)Model.size.getX())/16;
		Button reset_button = new EllipseButton(reset_button_x, reset_button_y, reset_button_size, reset_button_size);
		reset_button.setOnClick(() -> {
			reset();
		});
		reset_button.setText("Reset", new Color(0, 0, 0), 20);
		reset_button.setColors(new Color(200, 0, 0), new Color(200, 50, 50), new Color(220, 60, 60));
		click_manager.addObject(reset_button);
		state_object_manager.addObject(reset_button);
		
		float level_select_button_size = ((float)Model.size.getX())/16;
		float level_select_button_x = (float)Model.size.getX() - ((float)Model.size.getX())/16;
		float level_select_button_y = ((float)Model.size.getX())/16;
		Button level_select_button = new EllipseButton(level_select_button_x, level_select_button_y, level_select_button_size, level_select_button_size);
		level_select_button.setOnClick(() -> {
			if(Model.current_map_is_custom) {
				Model.switchToState(Model.StateEnum.CUSTOM_LEVEL_SELECT);
			}else {
				Model.switchToState(Model.StateEnum.LEVEL_SELECT);
			}
		});
		level_select_button.setText("lvls", new Color(0, 0, 0), 20);
		level_select_button.setColors(new Color(200, 0, 0), new Color(200, 50, 50), new Color(220, 60, 60));
		
		click_manager.addObject(level_select_button);
		state_object_manager.addObject(level_select_button);
	}
	
	private PImage background;
	
	private ClickManager click_manager;
	private StateObjectManager state_object_manager;
	
	private LinkedList<IntPair> snake_list;
	private HashSet<IntPair> snake_tiles;
	private HashSet<IntPair> food_tiles;
	private HashSet<IntPair> ground_tiles;
	private HashSet<IntPair> movable_tiles;
	private HashSet<IntPair> spike_tiles;
	private IntPair portal_tile;
	
	public float tile_size;
	public IntPair world_size;
	
	boolean will_reset, move_selected;
	Direction selected_direction; 
	
	
	private void reset() {
		will_reset = true;
	}
	
	private void load_level(String map_path) {
		InputStream map_stream = getClass().getResourceAsStream("/data/maps/" + map_path + ".txt");
		BufferedReader map_reader = new BufferedReader(new InputStreamReader(map_stream));
		try {
			
			String line = map_reader.readLine();
			String[] width_height = line.split("\\s+");
			int world_width = Integer.parseInt(width_height[0]);
			int world_height = Integer.parseInt(width_height[1]);
			
			world_size = new IntPair(world_width, world_height);
			tile_size = ((float)Model.size.getX())/world_size.getX();
			
			snake_list = new LinkedList<>();
			snake_tiles = new HashSet<>();
			food_tiles = new HashSet<>();
			ground_tiles = new HashSet<>();
			movable_tiles = new HashSet<>();
			spike_tiles = new HashSet<>();
			
			PriorityQueue<Entry<IntPair, Integer>> snake_queue = new PriorityQueue<>(
			new Comparator<Entry<IntPair, Integer>>() {

				@Override
				public int compare(Entry<IntPair, Integer> o1, Entry<IntPair, Integer> o2) {
					return o1.getValue().compareTo(o2.getValue());
				}

			});
			
			for(int row=0; row < world_height; row++){
				line = map_reader.readLine();
				String[] tile_type = line.split("\\s+");
				for(int col=0; col < world_width; col++){
					int tile_val = Integer.valueOf(tile_type[col]);
					switch(tile_val) {
					case 0: break;
					case 1: ground_tiles.add(new IntPair(col, row)); break;
					case 2: portal_tile = new IntPair(col, row); break;
					case 3: food_tiles.add(new IntPair(col, row)); break;
					case 4: movable_tiles.add(new IntPair(col, row)); break;
					case 5: spike_tiles.add(new IntPair(col, row)); break;
					default:
						snake_queue.add(new AbstractMap.SimpleEntry<IntPair, Integer>(new IntPair(col, row), tile_val));
					}
				}
			}
			
			Entry<IntPair, Integer> current_snake_tile;
			while(!snake_queue.isEmpty()) {
				current_snake_tile = snake_queue.poll();
				snake_list.add(current_snake_tile.getKey());
				snake_tiles.add(current_snake_tile.getKey());
			}
			
			
			map_reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public void update() {
		if(will_reset) {
			load_level(Model.current_map_path);
			will_reset = false;
			return;
		}
		if(movableTilesAreFloating()) {
			fallMovingTiles();
			move_selected = false;
		}
		if(snakeIsFloating() ) {
			snake_tiles.clear();
			LinkedList<IntPair> new_snake_list = new LinkedList<>();
			for(IntPair snake_tile : snake_list) {
				IntPair new_pos = IntPair.plus(snake_tile, Direction.DOWN.getChange());
				if(new_pos.getY() >= world_size.getY()) {
					reset();
					break;
				}
				new_snake_list.add(new_pos);
			}
			snake_tiles.addAll(new_snake_list);
			/*
			for(IntPair pos : snake_list) {
				fallMovingTile(IntPair.plus(pos, Direction.UP.getChange()));
			}
			*/
			snake_list = new_snake_list;
			if(snakeTouchingSpikes()) reset();
			move_selected = false;
		}
		if(move_selected) {
			moveSnake(selected_direction);
			move_selected = false;
		}
		state_object_manager.update();
	}

	@Override
	public void draw() {
		Model.p_app.image(background, 0, 0, Model.size.getX(), Model.size.getY());
		
		Model.p_app.fill(0,0,0,0);
		Model.p_app.stroke(0);
		Model.p_app.strokeWeight(1);
		
		for(int col = 0; col < world_size.getX(); ++col) {
			for(int row = 0; row < world_size.getY(); ++row) {
				Model.p_app.rect(col*tile_size, row*tile_size, tile_size, tile_size);
			}	
		}
		
		for(IntPair pos : ground_tiles) {
			Model.p_app.fill(150, 70, 24);
			Model.p_app.rect(pos.getX()*tile_size, pos.getY()*tile_size, tile_size, tile_size);
		}
		
		for(IntPair pos : movable_tiles) {
			Model.p_app.fill(154, 155, 147);
			Model.p_app.rect(pos.getX()*tile_size, pos.getY()*tile_size, tile_size, tile_size);
		}
		
		for(IntPair pos : spike_tiles) {
			Model.p_app.fill(152, 173, 13);
			Model.p_app.rect((float)(pos.getX()+.25)*tile_size, (float)(pos.getY()+.25)*tile_size, tile_size/2, tile_size/2);
		}
		
		Model.p_app.fill(25, 25, 25);
		Model.p_app.rect(portal_tile.getX()*tile_size, portal_tile.getY()*tile_size, tile_size, tile_size);
		
		for(IntPair pos : food_tiles) {
			Model.p_app.fill(219, 19, 43);
			Model.p_app.rect(pos.getX()*tile_size, pos.getY()*tile_size, tile_size, tile_size);
		}
		
		Iterator<IntPair> snake_itr = snake_list.iterator();
		IntPair next_snake_pos;
		if(snake_itr.hasNext()) {
			next_snake_pos = snake_itr.next();
			Model.p_app.fill(1, 51, 16);
			Model.p_app.rect(next_snake_pos.getX()*tile_size, next_snake_pos.getY()*tile_size, tile_size, tile_size);
		}
		while(snake_itr.hasNext()) {
			next_snake_pos = snake_itr.next();
			if(snake_itr.hasNext()) {
				Model.p_app.fill(9, 130, 25);
				Model.p_app.rect(next_snake_pos.getX()*tile_size, next_snake_pos.getY()*tile_size, tile_size, tile_size);
			
			}else {
				Model.p_app.fill(232, 255, 232);
				Model.p_app.rect(next_snake_pos.getX()*tile_size, next_snake_pos.getY()*tile_size, tile_size, tile_size);
			
			}
		}
		
		
		
		state_object_manager.draw();
	}
	
	private boolean snakeIsFloating() {
		for(IntPair snake_tile : snake_tiles) {
			IntPair down_pos = IntPair.plus(snake_tile, Direction.DOWN.getChange());
			while(movable_tiles.contains(down_pos)) down_pos = IntPair.plus(down_pos, Direction.DOWN.getChange());
			if(portal_tile.equals(down_pos) || ground_tiles.contains(down_pos) || food_tiles.contains(down_pos)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean movableTilesAreFloating() {
		for(IntPair movable_tile : movable_tiles) {
			IntPair down_pos = IntPair.plus(movable_tile, Direction.DOWN.getChange());
			if(!(snake_tiles.contains(down_pos) || ground_tiles.contains(down_pos) || food_tiles.contains(down_pos) || movable_tiles.contains(down_pos))) {
				return true;
			}
		}
		return false;
	}
	
	private void fallMovingTiles() {
		Iterator<IntPair> itr = movable_tiles.iterator();
		HashSet<IntPair> new_positions = new HashSet<>();
		while(itr.hasNext()) {
			IntPair movable_tile = itr.next();
			IntPair down_pos = IntPair.plus(movable_tile, Direction.DOWN.getChange());
			if(down_pos.getY() >= world_size.getY()) {
				itr.remove();
			}else {
				if(!(snake_tiles.contains(down_pos) || ground_tiles.contains(down_pos) || food_tiles.contains(down_pos) || movable_tiles.contains(down_pos))) {
					new_positions.add(IntPair.plus(movable_tile,Direction.DOWN.getChange()));
					itr.remove();
				}
			}
		}
		movable_tiles.addAll(new_positions);
	}
	
	private boolean snakeTouchingSpikes() {
		for(IntPair snake_tile : snake_tiles) {
			if(spike_tiles.contains(snake_tile)) return true;
		}
		return false;
	}
	
	/*
	private void fallMovingTile(IntPair pos) {
		if(!movable_tiles.contains(pos)) return;
		IntPair down_pos = IntPair.plus(pos, Direction.DOWN.getChange());
		movable_tiles.remove(pos);
		while(!(snake_tiles.contains(down_pos) || ground_tiles.contains(down_pos) || food_tiles.contains(down_pos) || movable_tiles.contains(down_pos))) {
			if(down_pos.getY() >= world_size.getY()) break;
			down_pos = IntPair.plus(down_pos, Direction.DOWN.getChange());
		}
		if(down_pos.getY() < world_size.getY()) movable_tiles.add(IntPair.plus(down_pos, Direction.UP.getChange()));
		fallMovingTile(IntPair.plus(pos, Direction.UP.getChange()));
	}
	*/
	
	
	private void moveSnake(Direction to) {
		if(will_reset == true) return;
		if(to == Direction.UP) {
			boolean straight_up = true;
			int snake_x = snake_list.peek().getX();
			for(IntPair s_pos : snake_tiles) {
				if(s_pos.getX() != snake_x) {
					straight_up = false;
				}
			}
			if(straight_up) return;
		}
		IntPair head_pos = snake_list.peek();
		IntPair new_head_pos = IntPair.plus(head_pos, to.getChange());
		if(snake_tiles.contains(new_head_pos) || ground_tiles.contains(new_head_pos)) {
			//
		}else {
			if(food_tiles.contains(new_head_pos)) {
				snake_list.addFirst(new_head_pos);
				snake_tiles.add(new_head_pos);
				food_tiles.remove(new_head_pos);
			}else if(new_head_pos.equals(portal_tile)) {
				if(!Model.current_map_is_custom) {
					if(!Model.finished_levels[Model.current_level-1]) {
						Model.unlocked_level += 1;
						Model.finished_levels[Model.current_level-1] = true;
					}
					Model.switchToState(Model.StateEnum.LEVEL_SELECT);
				}else {
					Model.switchToState(Model.StateEnum.CUSTOM_LEVEL_SELECT);
				}
			}else {
				IntPair check_pos = new IntPair(new_head_pos);
				while(movable_tiles.contains(check_pos)) check_pos = IntPair.plus(check_pos, to.getChange());
				if(snake_tiles.contains(check_pos) || ground_tiles.contains(check_pos) || food_tiles.contains(check_pos)) {
					//
				}else {
					
					snake_list.addFirst(new_head_pos);
					snake_tiles.add(new_head_pos);
					IntPair tail_pos = snake_list.pollLast();
					snake_tiles.remove(tail_pos);
					
					if(movable_tiles.contains(new_head_pos)) {
						movable_tiles.remove(new_head_pos);
						movable_tiles.add(check_pos);
						//fallMovingTile(check_pos);
					}
					//fallMovingTile(IntPair.plus(tail_pos, Direction.UP.getChange()));
					
					if(snakeTouchingSpikes()) reset();
				}
			}
			/*
			boolean breakout = false;
			while(snakeIsFloating() && !breakout) {
				snake_tiles.clear();
				LinkedList<IntPair> new_snake_list = new LinkedList<>();
				for(IntPair snake_tile : snake_list) {
					IntPair new_pos = IntPair.plus(snake_tile, Direction.DOWN.getChange());
					if(new_pos.getY() >= world_size.getY()) {
						reset();
						breakout = true;
						break;
					}
					new_snake_list.add(new_pos);
				}
				snake_tiles.addAll(new_snake_list);
				for(IntPair pos : snake_list) {
					fallMovingTile(IntPair.plus(pos, Direction.UP.getChange()));
				}
				snake_list = new_snake_list;
				if(snakeTouchingSpikes()) reset();
			}
			*/
		}
	}
	
	private void moveSelected(Direction to) {
		selected_direction = to;
		move_selected = true;
	}
	
	@Override
	public void keyPressed(KeyEvent event){
		switch(event.getKeyCode()) {
		case PApplet.UP: moveSelected(Direction.UP); break;
		case PApplet.DOWN: moveSelected(Direction.DOWN); break;
		case PApplet.LEFT: moveSelected(Direction.LEFT); break;
		case PApplet.RIGHT: moveSelected(Direction.RIGHT); break;
		}
	}
	
	@Override
	public void mousePressed(MouseEvent event) {
		click_manager.mousePressed(event);
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		click_manager.mouseReleased(event);
	}
	
	@Override
	public void mouseMoved(MouseEvent event) {
		click_manager.mouseMoved(event);
	}
	
	@Override
	public void enter(State from) {
		Model.p_app.frameRate(10);
		initialize_buttons();
		load_level(Model.current_map_path);
	}
	
	@Override
	public void exit(State to) {
		Model.p_app.frameRate(60);
	}
	
	
}
