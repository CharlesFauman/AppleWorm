package states;

import java.awt.Color;
import java.io.File;

import processing.event.MouseEvent;
import storage.Model;
import util.gui.Button;
import util.gui.StateObjectManager;
import util.mouse.ClickManager;
import util.mouse.EllipseButton;
import util.mouse.RectangleButton;

public final class CustomLevelSelectState extends State {
	private static final CustomLevelSelectState instance = new CustomLevelSelectState();
	
	public static CustomLevelSelectState getInstance(){ return instance; }

	private static ClickManager click_manager;
	private static StateObjectManager state_object_manager;
	
	// hardcoded settings _____________________________________:
	public final static int num_cols = 5;
	public final static int num_rows = 3;
	
	private static int num_custom_levels;
	private static String[] level_names;
	
	private static int start_level = 1;
	
	// hardcoded settings --------------------------------------:
	
	private CustomLevelSelectState() {		
		File folder = new java.io.File(Model.p_app.sketchPath("") + "src\\data\\maps\\custom\\");
		level_names = folder.list();
		num_custom_levels = level_names.length;
		for(int i = 0; i < num_custom_levels; ++i) {
			level_names[i] = level_names[i].replace(".txt", "");
		}
	}
	
	@Override
	public void update() {
		state_object_manager.update();
	}

	@Override
	public void draw() {
		Model.p_app.background(50);
		
		state_object_manager.draw();
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
	public void exit(State to) {
		click_manager.clear();
		state_object_manager.clear();
	}
	

	private void initialize_buttons() {
		click_manager = new ClickManager();
		state_object_manager = new StateObjectManager();
		
		float main_menu_button_size = ((float)Model.size.getX())/14;
		float main_menu_button_x = ((float)Model.size.getX())/16;
		float main_menu_button_y = ((float)Model.size.getX())/16;
		Button main_menu_button = new EllipseButton(main_menu_button_x, main_menu_button_y, main_menu_button_size, main_menu_button_size);
		main_menu_button.setOnClick(() -> {
			Model.switchToState(Model.StateEnum.MAIN_MENU);
		});
		main_menu_button.setText("Main", new Color(0, 0, 0), 20);
		main_menu_button.setColors(new Color(200, 0, 0), new Color(200, 50, 50), new Color(220, 60, 60));
		click_manager.addObject(main_menu_button);
		state_object_manager.addObject(main_menu_button);
		
		float new_level_button_size = ((float)Model.size.getX())/14;
		float new_level_button_x = (float)Model.size.getX() - ((float)Model.size.getX())/16;
		float new_level_button_y = ((float)Model.size.getX())/16;
		Button new_level_button = new EllipseButton(new_level_button_x, new_level_button_y, new_level_button_size, new_level_button_size);
		new_level_button.setOnClick(() -> {
			Model.switchToState(Model.StateEnum.LEVEL_MAKER);
		});
		new_level_button.setText("New", new Color(0, 0, 0), 20);
		new_level_button.setColors(new Color(200, 0, 0), new Color(200, 50, 50), new Color(220, 60, 60));
		click_manager.addObject(new_level_button);
		state_object_manager.addObject(new_level_button);
		
		if(start_level > 1) {
			Button left_button = new EllipseButton(Model.size.getX()/8, Model.size.getY()/2, Model.size.getX()/8, Model.size.getX()/8);
			
			left_button.setOnClick(() -> {
				start_level -= (num_rows*num_cols);
				initialize_buttons();
			});
			left_button.setText("Left", new Color(0, 0, 0), 20);
			left_button.setColors(new Color(200, 0, 0), new Color(200, 50, 50), new Color(220, 60, 60));
			click_manager.addObject(left_button);
			state_object_manager.addObject(left_button);
		}
		if(start_level + num_rows*num_cols < num_custom_levels) {
			Button right_button = new EllipseButton(Model.size.getX() - Model.size.getX()/8, Model.size.getY()/2, Model.size.getX()/8, Model.size.getX()/8);
			right_button.setOnClick(() -> {
				start_level += (num_rows*num_cols);
				initialize_buttons();
			});
			right_button.setText("Right", new Color(0, 0, 0), 20);
			right_button.setColors(new Color(200, 0, 0), new Color(200, 50, 50), new Color(220, 60, 60));
			click_manager.addObject(right_button);
			state_object_manager.addObject(right_button);
		}

		float offset_x = Model.size.getX()/5;
		float offset_y = Model.size.getY()/5;
		int button_size = Model.size.getX()/10;
		for(int col = 0; col < num_cols; ++col) {
			for(int row = 0; row < num_rows; ++row) {
				float x = (float) (offset_x + ((float)col*1.2)*button_size);
				float y = (float) (offset_y + ((float)row*1.2)*button_size);
				int button_level_val = col + row*num_cols + start_level - 1;
				if(button_level_val >= num_custom_levels) continue;
				System.out.println(button_level_val);
				Button level_button = new RectangleButton(x, y, button_size, button_size);
				level_button.setOnClick(() -> {
					Model.current_custom_num = button_level_val+1;
					Model.current_map_is_custom = true;
					Model.current_map_path = "custom/" + level_names[button_level_val];
					Model.switchToState(Model.StateEnum.GAME);
				});
				System.out.println(button_level_val);
				level_button.setText(level_names[button_level_val], new Color(255, 255, 255), 20);
				level_button.setColors(new Color(65, 155, 9), new Color(114, 198, 61), new Color(179, 255, 132));
				
				click_manager.addObject(level_button);
				state_object_manager.addObject(level_button);
			}
		}
	}
	
	@Override
	public void enter(State from) {
		start_level = (Math.floorDiv(Model.current_custom_num-1, (num_rows*num_cols))*(num_rows*num_cols)) + 1;
		initialize_buttons();
	}

}
