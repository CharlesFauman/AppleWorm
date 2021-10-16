package org.fauman.appleworm.util.gui;

public abstract class StateObject {
	public abstract Integer drawPriority();
	public abstract Integer updatePriority();
	
	public abstract void draw();
	public abstract void update();
}
