package me.kevinnovak.voidteleport;

import java.util.List;

public class VoidWorld {
	boolean enabled;
	String voidSpawnWorld;
	int voidSpawnXPos, voidSpawnYPos, voidSpawnZPos;
	
	boolean voidRandomSpawnEnabled;
	String voidRandomSpawnWorld;
	int voidRandomSpawnXRangeMin, voidRandomSpawnXRangeMax, voidRandomSpawnZRangeMin, voidRandomSpawnZRangeMax;
	
	List<String> dontSpawnOn;
	
	VoidWorld() {
		
    }
	
}