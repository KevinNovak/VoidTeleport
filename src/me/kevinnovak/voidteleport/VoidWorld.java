package me.kevinnovak.voidteleport;

import java.util.List;

import org.bukkit.Location;

public class VoidWorld {
	String name;
	Location spawnLocation;
	boolean voidRandomSpawnEnabled;
	String voidRandomSpawnWorld;
	int voidRandomSpawnXRangeMin, voidRandomSpawnXRangeMax, voidRandomSpawnZRangeMin, voidRandomSpawnZRangeMax;
	
	List<String> dontSpawnOn;
	
	VoidWorld() {
		
    }
	
	String getName() {
		return this.name;
	}
	
	void setName(String name) {
		this.name = name;
	}
	
	Location getSpawnLocation() {
		return this.spawnLocation;
	}
	
	void setSpawnLocation(Location spawnLocation) {
		this.spawnLocation = spawnLocation;
	}
	
	boolean getVoidRandomSpawnEnabled() {
		return this.voidRandomSpawnEnabled;
	}
	
	void setVoidRandomSpawnEnabled(boolean voidRandomSpawnEnabled) {
		this.voidRandomSpawnEnabled = voidRandomSpawnEnabled;
	}
	
	String getVoidRandomSpawnWorld() {
		return this.voidRandomSpawnWorld;
	}
	
	void setRandomSpawnWorld(String voidRandomSpawnWorld) {
		this.voidRandomSpawnWorld = voidRandomSpawnWorld;
	}
	
	int getVoidRandomSpawnXRangeMin() {
		return this.voidRandomSpawnXRangeMin;
	}
	
	void setVoidRandomSpawnXRangeMin(int voidRandomSpawnXRangeMin) {
		this.voidRandomSpawnXRangeMin = voidRandomSpawnXRangeMin;
	}
	
	int getVoidRandomSpawnXRangeMax() {
		return this.voidRandomSpawnXRangeMax;
	}
	
	void setVoidRandomSpawnXRangeMax(int voidRandomSpawnXRangeMax) {
		this.voidRandomSpawnXRangeMax = voidRandomSpawnXRangeMax;
	}
	
	int getVoidRandomSpawnZRangeMin() {
		return this.voidRandomSpawnZRangeMin;
	}
	
	void setVoidRandomSpawnZRangeMin(int voidRandomSpawnZRangeMin) {
		this.voidRandomSpawnZRangeMin = voidRandomSpawnZRangeMin;
	}
	
	int getVoidRandomSpawnZRangeMax() {
		return this.voidRandomSpawnZRangeMax;
	}
	
	void setVoidRandomSpawnZRangeMax(int voidRandomSpawnZRangeMax) {
		this.voidRandomSpawnZRangeMax = voidRandomSpawnZRangeMax;
	}
}