package main;

import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import storage.Model;

public final class Main extends PApplet{
	
	private static final Main instance = new Main();
	
	public void settings() {
		Model.getInstance(this);
		size(Model.size.getX(), Model.size.getY());
	}
	
	public void setup() {
		frameRate(60);
		Model.switchToState(Model.StateEnum.MAIN_MENU);
	}
	
	public void update() {
		Model.active_state.update();
	}
	
	public void draw() {
		update();
		Model.active_state.draw();
	}
	
	public void keyPressed(KeyEvent event) {
		Model.active_state.keyPressed(event);
	}
	
	public void mousePressed(MouseEvent event) {
		Model.active_state.mousePressed(event);
	}
	
	public void mouseReleased(MouseEvent event) {
		Model.active_state.mouseReleased(event);
	}
	
	public void mouseMoved(MouseEvent event) {
		Model.active_state.mouseMoved(event);
	}
	
	public void mouseDragged(MouseEvent event) {
		Model.active_state.mouseMoved(event);
	}
	
	public static void main(String _args[]) {
		PApplet.runSketch(new String[] { Main.class.getName() }, instance);
	}
}
