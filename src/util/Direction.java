package util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public enum Direction {

    UP, DOWN, LEFT, RIGHT;

    private static Map<Direction, IntPair> changeMap = new HashMap<>();
    
    static {
    	changeMap.put(UP, new IntPair(0, -1));
    	changeMap.put(DOWN, new IntPair(0, 1));
    	changeMap.put(LEFT, new IntPair(-1, 0));
    	changeMap.put(RIGHT, new IntPair(1, 0));
    }
    
    private static Map<Direction, Direction> oppositeMap = new HashMap<>();
    
    static {
    	oppositeMap.put(UP, DOWN);
    	oppositeMap.put(DOWN, UP);
    	oppositeMap.put(LEFT, RIGHT);
    	oppositeMap.put(RIGHT, LEFT);
    }
    
    private static final List<Direction> VALUES = Collections.unmodifiableList(Arrays.asList(values()));

    public static Direction random(Random rnd)  {
    	return VALUES.get(rnd.nextInt(VALUES.size()));
	}
    
    public IntPair getChange() {
    	return changeMap.get(this);
    }
    
    public Direction getOpposite() {
    	return oppositeMap.get(this);
    }
    
};
