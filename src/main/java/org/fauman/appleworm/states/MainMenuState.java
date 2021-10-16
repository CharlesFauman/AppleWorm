package org.fauman.appleworm.states;

import java.awt.Color;

import processing.event.MouseEvent;
import org.fauman.appleworm.storage.Model;
import org.fauman.appleworm.util.gui.Button;
import org.fauman.appleworm.util.gui.StateObjectManager;
import org.fauman.appleworm.util.mouse.ClickManager;
import org.fauman.appleworm.util.mouse.EllipseButton;

public final class MainMenuState extends State {
	private static final MainMenuState instance = new MainMenuState();
	
	public static MainMenuState getInstance(){ return instance; }

	private ClickManager click_manager;
	private StateObjectManager state_object_manager;
	
	private MainMenuState() {
		click_manager = new ClickManager();
		state_object_manager = new StateObjectManager();
		
		Button level_select_button = new  EllipseButton((float)(Model.size.getX())/2, 2*(float)(Model.size.getY())/3, 200, 200);
		level_select_button.setOnClick(() -> {
			Model.switchToState(Model.StateEnum.LEVEL_SELECT);
		});
		level_select_button.setText("Levels", new Color(0, 100, 0), 30);
		click_manager.addObject(level_select_button);
		state_object_manager.addObject(level_select_button);
		
		Button custom_level_select_button = new  EllipseButton((float)(Model.size.getX())/2, 1*(float)(Model.size.getY())/3, 200, 200);
		custom_level_select_button.setOnClick(() -> {
			Model.switchToState(Model.StateEnum.CUSTOM_LEVEL_SELECT);
		});
		custom_level_select_button.setText("Custom", new Color(0, 100, 0), 30);
		click_manager.addObject(custom_level_select_button);
		state_object_manager.addObject(custom_level_select_button);
	}
	
	@Override
	public void update() {
		state_object_manager.update();
	}

	@Override
	public void draw() {
		Model.p_app.background(50);
		
		Model.p_app.fill(50, 150, 0);
		Model.p_app.text("Apple Worm", (float)(Model.size.getX())/2, (float)(Model.size.getY())/16);
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

}
