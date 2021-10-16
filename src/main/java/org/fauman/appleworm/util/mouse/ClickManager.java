package org.fauman.appleworm.util.mouse;

import java.util.HashSet;

import processing.event.MouseEvent;
import org.fauman.appleworm.util.FloatPair;

public class ClickManager {
	private HashSet<Clickable> clickables;
	private HashSet<Clickable> inside;
	
	public ClickManager() {
		clickables = new HashSet<Clickable>();
		inside = new HashSet<Clickable>();
	}
	
	public void clear() {
		clickables.clear();
		inside.clear();
	}
	
	public void addObject(Clickable clickable) {
		clickables.add(clickable);
	}
	
	public void removeObject(Clickable clickable) {
		clickables.remove(clickable);
	}
	
	public void mousePressed(MouseEvent event) {
		for(Clickable clickable : inside) {
			clickable.mousePressed();
		}
	}

	public void mouseReleased(MouseEvent event) {
		for(Clickable clickable : inside) {
			clickable.mouseReleased();
		}
	}
	
	public void mouseMoved(MouseEvent event) {
		changeIntersectors(event);
	}
	
	public void changeIntersectors(MouseEvent event) {
		HashSet<Clickable> new_inside = new HashSet<>();
		for(Clickable clickable : clickables) {
			if(clickable.contains(new FloatPair(event.getX(), event.getY()))) {
				new_inside.add(clickable);
			}
		}
		
		for(Clickable clickable : new_inside) {
			if(inside.contains(clickable)) {
				inside.remove(clickable);
			}else {
				clickable.mouseEnter();
			}
		}
		
		for(Clickable clickable : inside) {
			clickable.mouseExit();
		}
		inside = new_inside;
	}
}
