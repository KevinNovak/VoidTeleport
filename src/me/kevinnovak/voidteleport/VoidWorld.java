package me.kevinnovak.voidteleport;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class VoidWorld {
	
	private boolean enabled;
	private World toWorld;
	private boolean useRandom;
	private World world;
	private Location spawn;
	private int minX, maxX, minY, maxY, minZ, maxZ;
	private List<Integer> dontSpawnOn;
	private int maxSpawnAttempts;
	
	VoidWorld(World world, FileConfiguration worldData, int maxSpawnAttempts) {
		this.world = world;
		
		this.enabled = worldData.getBoolean("teleportOutOfVoid.enabled");
		
		String toWorldName = worldData.getString("teleportOutOfVoid.toWorld");
		this.toWorld = Bukkit.getWorld(toWorldName);
		
		this.useRandom = worldData.getBoolean("teleportOutOfVoid.useRandom");
		
		int spawnXPos = worldData.getInt("worldSpawn.x-Pos");
		int spawnYPos = worldData.getInt("worldSpawn.y-Pos");
		int spawnZPos = worldData.getInt("worldSpawn.z-Pos");
		this.spawn = new Location(this.world, spawnXPos, spawnYPos, spawnZPos);
		this.spawn.add(0.5, 0, 0.5);
		
		this.minX = worldData.getInt("worldRandomSpawn.x-Range.min");
		this.maxX = worldData.getInt("worldRandomSpawn.x-Range.max");
		this.minY = worldData.getInt("worldRandomSpawn.y-Range.min");
		this.maxY = worldData.getInt("worldRandomSpawn.y-Range.max");
		this.minZ = worldData.getInt("worldRandomSpawn.z-Range.min");
		this.maxZ = worldData.getInt("worldRandomSpawn.z-Range.max");
		
		this.dontSpawnOn = worldData.getIntegerList("worldSpawn.dontSpawnOn");
		
		this.maxSpawnAttempts = maxSpawnAttempts;
    }
	
	@SuppressWarnings("deprecation")
	Location getRandomVoidLocation() {
	   	Location randLocation;
    	
    	int attempt = 1;
    	while (attempt <= maxSpawnAttempts) {
    		randLocation = getRandomLocation();

    		while (attempt <= maxSpawnAttempts && randLocation.getBlockY() >= this.minY) {
        		int randX = randLocation.getBlockX();
        		int randY = randLocation.getBlockY();
        		int randZ = randLocation.getBlockZ();
        		
        		Material blockAboveMaterial = new Location(this.world, randX, randY+1, randZ).getBlock().getType();
        		Material blockBelowMaterial = new Location(this.world, randX, randY-1, randZ).getBlock().getType();
        		
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
    							randLocation.add(0.5, 0, 0.5);
    							Bukkit.getLogger().info("attempts: " + attempt);
        						return randLocation;
    						}
    					}
    				}
    			}
    			
    			randLocation.subtract(0, 1, 0);
    			attempt++;
    		}
    	}
    	Bukkit.getLogger().info("SPAWNING AT SPAWN");
    	return this.spawn;
    }
	
    Location getRandomLocation() {
    	//Bukkit.getLogger().info("GETTING RANDOM LOCATION");
    	int randX = ThreadLocalRandom.current().nextInt(this.minX, this.maxX + 1);
    	int randY = ThreadLocalRandom.current().nextInt(this.minY, this.maxY + 1);
    	int randZ = ThreadLocalRandom.current().nextInt(this.minZ, this.maxZ + 1);
  
    	Location randLocation = new Location(this.world, randX, randY, randZ);
    	
    	return randLocation;
    }
    
    boolean getEnabled() {
    	return this.enabled;
    }
    
    void setEnable(boolean enabled) {
    	this.enabled = enabled;
    }
    
    World getToWorld() {
    	return this.toWorld;
    }
    
    void setToWorld(World toWorld) {
    	this.toWorld = toWorld;
    }
    
    boolean getUseRandom() {
    	return this.useRandom;
    }
    
    void setUseRandom(boolean useRandom) {
    	this.useRandom = useRandom;
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