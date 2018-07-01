package util;

import java.util.HashSet;
import java.util.Objects;

public class IntPair {
	
	private int x, y;
	public int getX() {return x;}
	public int getY() {return y;}
	public void setX(int x) {this.x = x;}
	public void setY(int y) {this.y = y;}
	
	public IntPair(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public IntPair(IntPair pos) {
		x = pos.x;
		y = pos.y;
	}
	
	@Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof IntPair)) {
            return false;
        }
        IntPair p = (IntPair) o;
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
	
	public static float squaredDistance(IntPair p1, IntPair p2) {
		return (float) (Math.pow(p1.x-p2.x, 2) + Math.pow(p1.y, p2.y));
	}
	
	public static float distance(IntPair p1, IntPair p2) {
		return (float) Math.pow(squaredDistance(p1, p2), .5);
	}
	
	public static IntPair plus(IntPair l, IntPair r) {
    	return(new IntPair(l.x + r.x, l.y + r.y));
    }
}