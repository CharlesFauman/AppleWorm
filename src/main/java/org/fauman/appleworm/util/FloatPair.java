package org.fauman.appleworm.util;

import java.util.Objects;

public class FloatPair {
	
	private float x, y;
	public float getX() {return x;}
	public float getY() {return y;}
	public void setX(int x) {this.x = x;}
	public void setY(int y) {this.y = y;}
	
	public FloatPair(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public FloatPair(FloatPair pos) {
		x = pos.x;
		y = pos.y;
	}
	
	@Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof FloatPair)) {
            return false;
        }
        FloatPair p = (FloatPair) o;
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
	
	
	@Override
	public String toString() {
		return new String("(" + x + ", " + y + ")");
	}
	
	public static float squaredDistance(FloatPair p1, FloatPair p2) {
		return (float) (Math.pow(p1.x-p2.x, 2) + Math.pow(p1.y, p2.y));
	}
	
	public static float distance(FloatPair p1, FloatPair p2) {
		return (float) Math.pow(squaredDistance(p1, p2), .5);
	}
	
	public static FloatPair plus(FloatPair l, FloatPair r) {
    	return(new FloatPair(l.x + r.x, l.y + r.y));
    }
}