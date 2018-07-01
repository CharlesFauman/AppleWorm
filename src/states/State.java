package states;

import processing.event.KeyEvent;
import processing.event.MouseEvent;

public abstract class State {
	public abstract void update();
	public abstract void draw();
	public void enter(State from) {}
	public void exit(State to) {}
	public void keyPressed(KeyEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
}
