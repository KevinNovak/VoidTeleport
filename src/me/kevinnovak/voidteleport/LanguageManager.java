package me.kevinnovak.voidteleport;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

public class LanguageManager {
	public VoidTeleport plugin;
	private ColorConverter colorConv = new ColorConverter();
	
	public String consolePrefix = "[VoidTeleport] ";
	public String noPermission;
	public String teleporting;
	public Boolean spawnEnabled;
	public String spawnMessage;
	public String random;
	public String commandMenuHeader, commandMenuCommandSetSpawn, commandMenuCommandSpawn, commandMenuCommandRandom, commandMenuMorePages, commandMenuNoCommands, commandMenuFooter;
	public String consoleSpawn, consoleRandom;
	
	public LanguageManager(VoidTeleport plugin) {
		this.plugin = plugin;
	}
	
	void load() {
        File languageFile = new File(plugin.getDataFolder() + "/language.yml");
        if (!languageFile.exists()) {
        	this.plugin.log("Copying default language file.");
        	this.plugin.saveResource("language.yml", false);
        }
		this.plugin.log("Loading language file.");
        YamlConfiguration languageData = YamlConfiguration.loadConfiguration(languageFile);
        
        this.noPermission = colorConv.convert(languageData.getString("noPermission"));
        
        this.teleporting = colorConv.convert(languageData.getString("teleporting"));
        this.spawnEnabled = languageData.getBoolean("spawn.enabled");
        this.spawnMessage = colorConv.convert(languageData.getString("spawn.message"));
        this.random = colorConv.convert(languageData.getString("random"));
        
    	this.commandMenuHeader = colorConv.convert(languageData.getString("commandMenu.header"));
    	this.commandMenuCommandSetSpawn = colorConv.convert(languageData.getString("commandMenu.command.setSpawn"));
    	this.commandMenuCommandSpawn = colorConv.convert(languageData.getString("commandMenu.command.spawn"));
    	this.commandMenuCommandRandom = colorConv.convert(languageData.getString("commandMenu.command.random"));
    	this.commandMenuMorePages = colorConv.convert(languageData.getString("commandMenu.morePages"));
    	this.commandMenuNoCommands = colorConv.convert(languageData.getString("commandMenu.noCommands"));
    	this.commandMenuFooter = colorConv.convert(languageData.getString("commandMenu.footer"));
    	
    	this.consoleSpawn = languageData.getString("console.spawn");
    	this.consoleRandom = languageData.getString("console.random");
	}
}