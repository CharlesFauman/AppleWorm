package org.fauman.appleworm.util.mouse;
import org.fauman.appleworm.util.FloatPair;

public interface Clickable {
	public void mouseEnter();
	public void mouseExit();
	public void mousePressed();
	public void mouseReleased();
	public boolean contains(FloatPair p);
}
