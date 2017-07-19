package me.kevinnovak.voidteleport;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class VoidTeleport extends JavaPlugin implements Listener{
	private PermissionManager perm = new PermissionManager();
	private LanguageManager langMan = new LanguageManager(this);
	private CommandManager cmdMan = new CommandManager(this);
	private CommandMenu commandMenu = new CommandMenu(this.perm, this.langMan);
	
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
				worldData.set("voidSpawn.world", worldSpawn.getWorld().getName());
				worldData.set("voidSpawn.x-Pos", worldSpawn.getBlockX());
				worldData.set("voidSpawn.y-Pos", worldSpawn.getBlockY());
				worldData.set("voidSpawn.z-Pos", worldSpawn.getBlockZ());
				worldData.set("voidRandomSpawn.world", worldName);
				
				try {
					worldData.save(worldFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			this.log("Loading world file for \"" + worldName + "\".");
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
            	for (String spawnCommand : cmdMan.spawnCommands) {
            		if (args[0].equalsIgnoreCase(spawnCommand)) {
            			if (player.hasPermission(perm.spawn)) {
            				
            				// TO-DO: teleport player
            				player.sendMessage("Teleported to Spawn");
            				
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
            				
            				// TO-DO: teleport player
            				player.sendMessage("Teleported to Random");
            				
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