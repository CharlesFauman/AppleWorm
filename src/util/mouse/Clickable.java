package util.mouse;
import processing.event.MouseEvent;
import util.FloatPair;

public interface Clickable {
	public void mouseEnter();
	public void mouseExit();
	public void mousePressed();
	public void mouseReleased();
	public boolean contains(FloatPair p);
}
