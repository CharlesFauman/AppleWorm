package util.gui;

import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import processing.core.PApplet;
import storage.Model;

public class StateObjectManager {
	private TreeMap<Integer, HashSet<StateObject>> draw_objects;
	private TreeMap<Integer, HashSet<StateObject>> update_objects;
	private HashSet<StateObject> to_remove_objects;
	private HashSet<StateObject> to_add_objects;
	
	public StateObjectManager() {
		draw_objects = new TreeMap<>();
		update_objects = new TreeMap<>();
		to_remove_objects = new HashSet<>();
		to_add_objects = new HashSet<>();
	}
	
	public void clear() {
		for(Map.Entry<Integer,HashSet<StateObject>> entry : update_objects.entrySet()) {
			HashSet<StateObject> bin = entry.getValue();
			for(StateObject o : bin) {
				to_remove_objects.add(o);
			}
		}
	}
	
	public void update() {
		clean();
		for(Map.Entry<Integer,HashSet<StateObject>> entry : update_objects.entrySet()) {
			HashSet<StateObject> bin = entry.getValue();
			for(StateObject o : bin) {
				o.update();
			}
		}
		clean();
	}
	
	public void draw() {
		for(Map.Entry<Integer,HashSet<StateObject>> entry : draw_objects.entrySet()) {
			HashSet<StateObject> bin = entry.getValue();
			for(StateObject o : bin) {
				o.draw();
			}
		}
	}
	
	private void clean() {
		for(StateObject o : to_remove_objects) {
			actualRemoveObject(o);
		}
		to_remove_objects.clear();
		for(StateObject o : to_add_objects) {
			actualAddObject(o);
		}
		to_add_objects.clear();
	}
	
	private boolean addToPriorityMap(TreeMap<Integer, HashSet<StateObject>> p_m, Integer priority, StateObject o) {
		HashSet<StateObject> bin;
		if(p_m.containsKey(priority)) {
			bin = p_m.get(priority);
		}else {
			bin = new HashSet<StateObject>();
			p_m.put(priority, bin);
		}
		if(bin.contains(o)) {
			return false;
		}
		bin.add(o);
		return true;
	}
	
	private boolean removeFromPriorityMap(TreeMap<Integer, HashSet<StateObject>> p_m, Integer priority, StateObject o) {
		if(p_m.containsKey(priority)){
			HashSet<StateObject> bin = p_m.get(priority);
			if(!bin.contains(o)) {
				return false;
			}
			bin.remove(o);
		}else {
			return false;
		}
		return true;
	}
	
	public void addObject(StateObject o) {
		to_add_objects.add(o);
	}
	
	public void removeObject(StateObject o) {
		to_remove_objects.add(o);
	}
	
	private void actualAddObject(StateObject o) {
		addToPriorityMap(draw_objects, o.drawPriority(), o);
		addToPriorityMap(update_objects, o.updatePriority(), o);
	}
	
	private void actualRemoveObject(StateObject o) {
		removeFromPriorityMap(draw_objects, o.drawPriority(), o);
		removeFromPriorityMap(update_objects, o.updatePriority(), o);
	}
}
