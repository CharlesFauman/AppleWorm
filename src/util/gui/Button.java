package util.gui;


import java.awt.Color;

import processing.core.PApplet;
import storage.Model;
import util.FloatPair;
import util.Procedure;
import util.mouse.Clickable;

public abstract class Button extends StateObject implements Clickable{
	protected FloatPair pos;
	protected FloatPair width_height;
	protected Color color, hovered_color, clicked_color, text_color;
	protected boolean clicked, hovered;
	protected Procedure on_click_procedure;
	protected String text;
	protected float text_size;
	
	public Button(float x, float y, float width, float height) {
		pos = new FloatPair(x,y);
		width_height = new FloatPair(width, height);
		clicked = false;
		hovered = false;
		color = new Color(0, 0, 0);
		hovered_color = new Color(100, 100, 100);
		clicked_color = new Color(200, 200, 200);
		
		on_click_procedure = () -> {};
		
		text = "";
		text_color = new Color(255, 255, 255);
		text_size = 1;
	}
	
	public void setColors(Color _color, Color _hovered_color, Color _clicked_color) {
		color = _color;
		hovered_color = _hovered_color;
		clicked_color = _clicked_color;
	}
	
	public void setText(String _text, Color col, float size) {
		text = _text;
		text_color = col;
		text_size = size;
	}
	
	public void setOnClick(Procedure procedure) {
		on_click_procedure = procedure;
	}
	
	protected abstract void draw_shape();
	protected abstract void draw_text();
	
	@Override
	public void draw() {
		Model.p_app.stroke(0,0,0);
		Model.p_app.strokeWeight(5);
		Model.p_app.fill(color.getRGB());
		if(hovered) Model.p_app.fill(hovered_color.getRGB());
		if(clicked) Model.p_app.fill(clicked_color.getRGB());
		draw_shape();
		
		Model.p_app.fill(text_color.getRGB());
		Model.p_app.textSize(text_size);
		Model.p_app.textAlign(PApplet.CENTER);
		draw_text();
	}

	@Override
	public void mouseEnter() {
		hovered = true;
	}

	@Override
	public void mouseExit() {
		hovered = false;
		clicked = false;
	}

	@Override
	public void mousePressed() {
		clicked = true;
	}

	@Override
	public void mouseReleased() {
		if(clicked) {
			clicked = false;
			on_click_procedure.invoke();
		}
	}
	@Override
	public Integer drawPriority() {
		return 0;
	}
	@Override
	public Integer updatePriority() {
		return 0;
	}
	
	@Override
	public void update() {
		//
	}

}

