package me.kevinnovak.voidteleport;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.java.JavaPlugin;

public class VoidTeleport extends JavaPlugin implements Listener{
	// Plugin
	private PermissionManager perm = new PermissionManager();
	private LanguageManager langMan = new LanguageManager(this);
	private CommandManager cmdMan = new CommandManager(this);
	private CommandMenu commandMenu = new CommandMenu(this.perm, this.langMan);
	
	private List<VoidWorld> voidWorlds = new ArrayList<VoidWorld>();
	
	// Config
	private int maxSpawnAttempts;
	
    // ======================
    // Enable
    // ======================
    public void onEnable() {
        this.saveDefaultConfig();
        this.loadConfig();
        this.loadLanguageFile();
        this.loadCommandsFile();
        this.loadWorlds();
        this.registerEvents();
        this.log("Plugin enabled!");
    }
    
    // ======================
    // Disable
    // ======================
    public void onDisable() {
    	this.log("Plugin disabled!");
    }
    
    void log(String info) {
    	Bukkit.getServer().getLogger().info(langMan.consolePrefix + ChatColor.stripColor(info));
    }
    
    void registerEvents() {
        this.log("Registering events.");
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }
    
    void loadLanguageFile() {
    	langMan.load();
    }
    
    void loadCommandsFile() {
    	cmdMan.load();
    }
    
	void loadConfig() {
        this.log("Loading main config.");
        this.maxSpawnAttempts = getConfig().getInt("maxSpawnAttempts");
    }
	
	void loadWorlds() {
		for (World world : Bukkit.getWorlds()) {
			String worldName = world.getName();
			File worldFile = new File(getDataFolder() + "/worlds/" + worldName + ".yml");
			
			// copy default file if doesnt exist
			if (!worldFile.exists()) {
				this.log("Creating default world file for \"" + worldName + "\".");
				
				FileConfiguration worldData = this.getResourceConfig("worlds/default.yml");
				Location worldSpawn = world.getSpawnLocation();
				worldData.set("teleportOutOfVoid.toWorld", worldSpawn.getWorld().getName());
				worldData.set("worldSpawn.x-Pos", worldSpawn.getBlockX());
				worldData.set("worldSpawn.y-Pos", worldSpawn.getBlockY());
				worldData.set("worldSpawn.z-Pos", worldSpawn.getBlockZ());
				worldData.set("worldSpawn.yaw", worldSpawn.getYaw());
				worldData.set("worldSpawn.pitch", worldSpawn.getPitch());
				
				if (world.getEnvironment() == Environment.NETHER) {
					worldData.set("worldRandomSpawn.y-Range.min", 5);
					worldData.set("worldRandomSpawn.y-Range.max", 120);
				} else if (world.getEnvironment() == Environment.THE_END) {
					worldData.set("worldRandomSpawn.x-Range.min", -100);
					worldData.set("worldRandomSpawn.x-Range.max", 100);
					worldData.set("worldRandomSpawn.y-Range.min", 10);
					worldData.set("worldRandomSpawn.y-Range.max", 100);
					worldData.set("worldRandomSpawn.z-Range.min", -100);
					worldData.set("worldRandomSpawn.z-Range.max", 100);
				}
				
				try {
					worldData.save(worldFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (worldFile.exists()) {
				FileConfiguration worldData = YamlConfiguration.loadConfiguration(worldFile);
				this.log("Loading world file for \"" + worldName + "\".");
				VoidWorld voidWorld = new VoidWorld(world, worldData, this.maxSpawnAttempts);
				voidWorlds.add(voidWorld);
			}
		}		
	}
    
    // ======================
    // Commands
    // ======================
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        // ======================
        // Console
        // ======================
        // if command sender is the console, let them know, cancel command
        if (!(sender instanceof Player)) {
            // TO-DO: send message to console
            return true;
        }
        Player player = (Player) sender;
        
        // ======================
        // /mt
        // ======================
        if(cmd.getName().equalsIgnoreCase("vtp")) {
            // th
        	if (args.length == 0) {
        		commandMenu.print(player, 1);
            	return true;
            } else if (args.length > 0) {
            	for (String setSpawnCommand : cmdMan.setSpawnCommands) {
            		if (args[0].equalsIgnoreCase(setSpawnCommand)) {
            			if (player.hasPermission(perm.setSpawn)) {
            				this.setVoidSpawn(player);
                    		return true;
            			} else {
            				player.sendMessage(langMan.noPermission);
            				return true;
            			}
                	}
            	}
            	
            	for (String spawnCommand : cmdMan.spawnCommands) {
            		if (args[0].equalsIgnoreCase(spawnCommand)) {
            			if (player.hasPermission(perm.spawn)) {
            				this.teleportPlayerSpawn(player, this.getVoidWorld(player.getWorld()));
                    		return true;
            			} else {
            				player.sendMessage(langMan.noPermission);
            				return true;
            			}
                	}
            	}
            	
            	for (String randomCommand : cmdMan.randomCommands) {
            		if (args[0].equalsIgnoreCase(randomCommand)) {
            			if (player.hasPermission(perm.random)) {
            				this.teleportPlayerRandom(player, this.getVoidWorld(player.getWorld()));
                    		return true;
            			} else {
            				player.sendMessage(langMan.noPermission);
            				return true;
            			}
                	}
            	}
            	
        		commandMenu.print(player, 1);
        		return true;
            }
        }
        
		return false;
    }
    
    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent e) {
    	if (e.getCause().equals(DamageCause.VOID)) {
        	Entity damagedEntity = e.getEntity();
    	    if (damagedEntity instanceof Player) {
    		    Player player = (Player) damagedEntity;
    		    World playerWorld = player.getLocation().getWorld();
		    	for (VoidWorld voidWorld : this.voidWorlds) {
		    		if (voidWorld.getWorld().equals(playerWorld)) {
		    			if (voidWorld.getEnabled()) {
		    				World toWorld = voidWorld.getToWorld();
		    				VoidWorld toVoidWorld = this.getVoidWorld(toWorld);
    		    			// cancel damage
    		    			e.setCancelled(true);
    						if (voidWorld.getUseRandom()) {
    							this.teleportPlayerRandom(player, toVoidWorld);
    						} else {
    							this.teleportPlayerSpawn(player, toVoidWorld);
    						}
		    			}
		    		}
		    	}
    	    }
    	}
    }
    
    void setVoidSpawn(Player player) {
    	Location playerLocation = player.getLocation();
    	World playerWorld = playerLocation.getWorld();
    	String playerWorldName = playerWorld.getName();

    	File worldFile = new File(getDataFolder() + "/worlds/" + playerWorldName + ".yml");
    	if (worldFile.exists()) {
    		int playerXPos = playerLocation.getBlockX();
    		int playerYPos = playerLocation.getBlockY();
    		int playerZPos = playerLocation.getBlockZ();
    		float playerYaw = playerLocation.getYaw();
    		float playerPitch = playerLocation.getPitch();
    		
    		// save new spawn to file
    		FileConfiguration worldData = YamlConfiguration.loadConfiguration(worldFile);
			worldData.set("worldSpawn.x-Pos", playerXPos);
			worldData.set("worldSpawn.y-Pos", playerYPos);
			worldData.set("worldSpawn.z-Pos", playerZPos);
			worldData.set("worldSpawn.yaw", playerYaw);
			worldData.set("worldSpawn.pitch", playerPitch);
			
			try {
				worldData.save(worldFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// set already loaded spawn to new spawn
	    	VoidWorld voidWorld = this.getVoidWorld(playerWorld);
	    	Location spawnLocation = new Location(playerWorld, playerXPos, playerYPos, playerZPos, playerYaw, playerPitch);
	    	voidWorld.setSpawn(spawnLocation);
	    	
	    	player.sendMessage("World Spawn Set");
    	}
    }
    
    void teleportPlayerSpawn(Player player, VoidWorld voidWorld) {
    	String currentWorld = player.getWorld().getName();
    	
		player.setFallDistance(0);
		player.sendMessage(langMan.teleporting);
		player.teleport(voidWorld.getSpawn());
		
		this.log(langMan.consoleSpawn
				.replace("{PLAYER}", player.getName())
				.replace("{VOID-WORLD}", currentWorld)
				.replace("{WORLD}", voidWorld.getWorld().getName()));
		
		if (langMan.spawnEnabled) {
			player.sendMessage(langMan.spawnMessage);
		}
    }
    
    void teleportPlayerRandom(Player player, VoidWorld voidWorld) {
    	String currentWorld = player.getWorld().getName();
    	
		Location tpLocation = voidWorld.getRandomVoidLocation();
		// add in pitch and yaw of player
		tpLocation = new Location(tpLocation.getWorld(), tpLocation.getX(), tpLocation.getY(), tpLocation.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
		
		player.setFallDistance(0);
		player.sendMessage(langMan.teleporting);
		player.teleport(tpLocation);
		
		if (tpLocation.equals(voidWorld.getSpawn())) {
			this.log(langMan.consoleSpawn
					.replace("{PLAYER}", player.getName())
					.replace("{VOID-WORLD}", currentWorld)
					.replace("{WORLD}", voidWorld.getWorld().getName()));
			
			if (langMan.spawnEnabled) {
				player.sendMessage(langMan.spawnMessage);
			}
		} else {
			String xPos = Integer.toString(tpLocation.getBlockX());
			String yPos = Integer.toString(tpLocation.getBlockY());
			String zPos = Integer.toString(tpLocation.getBlockZ());
			
			this.log(langMan.consoleRandom
					.replace("{PLAYER}", player.getName())
					.replace("{VOID-WORLD}", currentWorld)
					.replace("{X-POS}", xPos)
					.replace("{Y-POS}", yPos)
					.replace("{Z-POS}", zPos)
					.replace("{WORLD}", voidWorld.getWorld().getName()));
			
			player.sendMessage(langMan.random
					.replace("{X-POS}", xPos)
					.replace("{Y-POS}", yPos)
					.replace("{Z-POS}", zPos));
		}
    }
    
    VoidWorld getVoidWorld(World world) {
    	for (VoidWorld voidWorld : this.voidWorlds) {
    		if (voidWorld.getWorld().equals(world)) {
    			return voidWorld;
    		}
    	}
    	return null;
    }
    
    FileConfiguration getResourceConfig(String filename) {
		InputStream stream = this.getResource(filename);
		Reader reader = new InputStreamReader(stream);
		FileConfiguration config = YamlConfiguration.loadConfiguration(reader);
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return config;
    }
}