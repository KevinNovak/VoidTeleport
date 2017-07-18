package me.kevinnovak.voidteleport;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

public class LanguageManager {
	public VoidTeleport plugin;
	private ColorConverter colorConv = new ColorConverter();
	
	public String consolePrefix = "[VoidTeleport] ";
	public String noPermission;
	public String commandMenuHeader, commandMenuCommandTp, commandMenuMorePages, commandMenuNoCommands, commandMenuFooter;
	
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
        
    	this.commandMenuHeader = colorConv.convert(languageData.getString("commandMenu.header"));
    	this.commandMenuCommandTp = colorConv.convert(languageData.getString("commandMenu.command.tp"));
    	this.commandMenuMorePages = colorConv.convert(languageData.getString("commandMenu.morePages"));
    	this.commandMenuNoCommands = colorConv.convert(languageData.getString("commandMenu.noCommands"));
    	this.commandMenuFooter = colorConv.convert(languageData.getString("commandMenu.footer"));
	}
}