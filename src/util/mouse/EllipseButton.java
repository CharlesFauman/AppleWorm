package util.mouse;

import processing.core.PApplet;
import storage.Model;
import util.FloatPair;
import util.gui.Button;

public class EllipseButton extends Button{

	public EllipseButton(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	@Override
	protected void draw_shape() {
		Model.p_app.ellipse(pos.getX(), pos.getY(), width_height.getX(), width_height.getY());
	}
	
	@Override
	protected void draw_text() {
		Model.p_app.text(text, pos.getX(), pos.getY() + text_size/4);
	}

	@Override
	public boolean contains(FloatPair p) {
		return (1 > (Math.pow(p.getX()-pos.getX(), 2)/Math.pow(width_height.getX()/2, 2)) + (Math.pow(p.getY()-pos.getY(), 2)/Math.pow(width_height.getY()/2, 2)));
	}
	
	

}
