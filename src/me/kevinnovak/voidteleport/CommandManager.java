package me.kevinnovak.voidteleport;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

public class CommandManager {
	public VoidTeleport plugin;
	
	public List<String> spawnCommands, randomCommands;
	
	public CommandManager(VoidTeleport plugin) {
		this.plugin = plugin;
	}
	
	void load() {
        File commandsFile = new File(plugin.getDataFolder() + "/commands.yml");
        if (!commandsFile.exists()) {
        	this.plugin.log("Copying default commands file.");
        	this.plugin.saveResource("commands.yml", false);
        }
		this.plugin.log("Loading commands file.");
        YamlConfiguration commandsData = YamlConfiguration.loadConfiguration(commandsFile);
        
    	this.spawnCommands = commandsData.getStringList("spawn");
    	this.randomCommands = commandsData.getStringList("random");
	}
}