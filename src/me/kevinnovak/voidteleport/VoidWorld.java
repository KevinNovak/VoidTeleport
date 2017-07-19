package me.kevinnovak.voidteleport;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class VoidWorld {
	public static int MAX_HEIGHT = 127;
	public static int MIN_HEIGHT = 0;
	
	private World world;
	private Location spawn;
	private boolean randomSpawnEnabled;
	private World randomSpawnWorld;
	private int minX, maxX, minZ, maxZ;
	private List<Integer> dontSpawnOn;
	private int maxSpawnAttempts;
	
	VoidWorld(World world, FileConfiguration worldData, int maxSpawnAttempts) {
		this.world = world;
		
		String spawnWorldName = worldData.getString("voidSpawn.world");
		World spawnWorld = Bukkit.getWorld(spawnWorldName);
		int spawnXPos = worldData.getInt("voidSpawn.x-Pos");
		int spawnYPos = worldData.getInt("voidSpawn.y-Pos");
		int spawnZPos = worldData.getInt("voidSpawn.z-Pos");
		this.spawn = new Location(spawnWorld, spawnXPos, spawnYPos, spawnZPos);
		
		this.randomSpawnEnabled = worldData.getBoolean("voidRandomSpawn.enabled");
		String randomSpawnWorldName = worldData.getString("voidRandomSpawn.world");
		this.randomSpawnWorld = Bukkit.getWorld(randomSpawnWorldName);
		this.minX = worldData.getInt("voidRandomSpawn.x-Range.min");
		this.maxX = worldData.getInt("voidRandomSpawn.x-Range.max");
		this.minZ = worldData.getInt("voidRandomSpawn.z-Range.min");
		this.maxZ = worldData.getInt("voidRandomSpawn.z-Range.max");
		
		this.dontSpawnOn = worldData.getIntegerList("dontSpawnOn");
		
		this.maxSpawnAttempts = maxSpawnAttempts;
    }
	
	@SuppressWarnings("deprecation")
	Location getVoidLocation() {
		if (this.randomSpawnEnabled) {
		   	Location randLocation;
	    	
	    	int attempt = 1;
	    	while (attempt <= maxSpawnAttempts) {
	    		randLocation = getRandomLocation();

	    		while (attempt <= maxSpawnAttempts && randLocation.getBlockY() >= MIN_HEIGHT) {
	        		int randX = randLocation.getBlockX();
	        		int randY = randLocation.getBlockY();
	        		int randZ = randLocation.getBlockZ();
	        		
	        		Material blockAboveMaterial = new Location(this.randomSpawnWorld, randX, randY+1, randZ).getBlock().getType();
	        		Material blockBelowMaterial = new Location(this.randomSpawnWorld, randX, randY-1, randZ).getBlock().getType();
	        		
	    			if (randLocation.getBlock().getType() == Material.AIR) {
	    				if (blockAboveMaterial == Material.AIR) {
	    					if (blockBelowMaterial != Material.AIR) {
	    						boolean forbidden = false;
	    						for (Integer itemID : this.dontSpawnOn) {
	    							if (blockBelowMaterial == Material.getMaterial(itemID)) {
	    								forbidden = true;
	    							}
	    						}
	    						if (!forbidden) {
	        						return randLocation;
	    						}
	    					}
	    				}
	    			}
	    			
	    			randLocation.subtract(0, 1, 0);
	    			attempt++;
	    		}
	    	}
		}
    	
    	return this.spawn;
    }
	
    Location getRandomLocation() {
    	int randX = ThreadLocalRandom.current().nextInt(this.minX, this.maxX + 1);
    	int randY = MAX_HEIGHT;
    	int randZ = ThreadLocalRandom.current().nextInt(this.minZ, this.maxZ + 1);
  
    	Location randLocation = new Location(this.randomSpawnWorld, randX, randY, randZ);
    	
    	return randLocation;
    }
	
	World getWorld() {
		return this.world;
	}
	
	void setName(World world) {
		this.world = world;
	}
	
	Location getSpawn() {
		return this.spawn;
	}
	
	void setSpawn(Location spawn) {
		this.spawn = spawn;
	}
	
	boolean getRandomSpawnEnabled() {
		return this.randomSpawnEnabled;
	}
	
	void setRandomSpawnEnabled(boolean randomSpawnEnabled) {
		this.randomSpawnEnabled = randomSpawnEnabled;
	}
	
	World getRandomSpawnWorld() {
		return this.randomSpawnWorld;
	}
	
	void setRandomSpawnWorld(World randomSpawnWorld) {
		this.randomSpawnWorld = randomSpawnWorld;
	}
	
	int getMinX() {
		return this.minX;
	}
	
	void setMinX(int minX) {
		this.minX = minX;
	}
	
	int getMaxX() {
		return this.maxX;
	}
	
	void setMaxX(int maxX) {
		this.maxX = maxX;
	}
	
	int getMinZ() {
		return this.minZ;
	}
	
	void setMinZ(int minZ) {
		this.minZ = minZ;
	}
	
	int getMaxZ() {
		return this.maxZ;
	}
	
	void setMaxZ(int maxZ) {
		this.maxZ = maxZ;
	}
	
	List<Integer> getDontSpawnOn() {
		return this.dontSpawnOn;
	}
	
	void setDontSpawnOn(List<Integer> dontSpawnOn) {
		this.dontSpawnOn = dontSpawnOn;
	}
}