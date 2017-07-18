package me.kevinnovak.voidteleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
            	for (String tpCommand : cmdMan.tpCommands) {
            		if (args[0].equalsIgnoreCase(tpCommand)) {
            			if (player.hasPermission(perm.tp)) {
            				
            				// TO-DO: teleport player
            				player.sendMessage("TELEPORTED");
            				
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
}