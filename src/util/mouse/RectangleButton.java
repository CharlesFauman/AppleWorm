package util.mouse;

import processing.core.PApplet;
import storage.Model;
import util.FloatPair;
import util.gui.Button;

public class RectangleButton extends Button{
	
	public RectangleButton(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	@Override
	protected void draw_shape() {
		Model.p_app.rect(pos.getX(), pos.getY(), width_height.getX(), width_height.getY());
	}
	
	@Override
	protected void draw_text() {
		Model.p_app.text(text, pos.getX() + width_height.getX()/2, pos.getY() + width_height.getY()/2 + text_size/4);
	}

	@Override
	public boolean contains(FloatPair p) {
		if (pos.getX() <= p.getX() && pos.getY() <= p.getY() &&
				pos.getX() + width_height.getX() > p.getX() && pos.getY() + width_height.getY() > p.getY()){
			return true;
		}
		return false;
	}

}
