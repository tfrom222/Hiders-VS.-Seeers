package tfrom222.myfirstplugin;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import tfrom222.myfirstplugin.SeekGamemode;


public class MyFirstPlugin extends JavaPlugin {

	 public static MyFirstPlugin plugin;
	 
	public static Economy econ;
	 public static boolean useVault;
	
	
	public FileConfiguration config = getConfig();
	
	
	public void onEnable() {
		
		plugin = this;
	getServer().getPluginManager().registerEvents(new MyEventListener(), this);
		
	
	    config.addDefault("SeekerSpawnX", 0);
	    config.addDefault("SeekerSpawnY", 0);
	    config.addDefault("SeekerSpawnZ", 0);
	    
	    config.addDefault("SeekerSpawnSet", false);
	    
	    config.addDefault("HiderSpawnX", 0);
	    config.addDefault("HiderSpawnY", 0);
	    config.addDefault("HiderSpawnZ", 0);
	    
	    config.addDefault("HiderSpawnSet", false);
	    
	    config.addDefault("GameWorld", "world");
	    config.addDefault("GameTime", 180);
	    
	    

	    config.addDefault("Use-Vault", true);
	    config.addDefault("Reward-Money", 50);

	    config.addDefault("Auto-Update", false);
	    
	    
	    if(config.getBoolean("Auto-Update") == true)
	    {
	   	 Updater updater = new Updater(this, 93443	, this.getFile(), Updater.UpdateType.DEFAULT, true);
	    }
	  
	    
	    
	    
	    config.options().copyDefaults(true);
	    saveConfig();
	    
	    
	    useVault = config.getBoolean("Use-Vault");
	    
	    if(Bukkit.getPluginManager().getPlugin("Vault") != null && useVault == true && setupEconomy() == true)
	    {
	    	Bukkit.getConsoleSender().sendMessage("["+plugin.getName()+"]:"+ChatColor.WHITE+"Vault Integration Enabled!");
	    	
	    	
	    }
	    else
	    {
	    	useVault = false;
	    	Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD+"["+plugin.getName()+"]:"+ChatColor.WHITE+"Vault Integration Was Not Enabled!");
	    }
	    
	    
	   SeekGamemode.PluginStartGamemode();

	    
	    this.getCommand("beginseek").setExecutor(new CommandBeginSeek());
	    this.getCommand("stopseek").setExecutor(new CommandStopSeek());
	    this.getCommand("setupseek").setExecutor(new CommandSetupSeek());
	    
	    
	}

	public void onPlayerJoin(PlayerJoinEvent event) {
	    Player player = event.getPlayer();

	    player.setCustomName("Bacon_Master_2000");
	    player.setCustomNameVisible(true);
	    player.setCanPickupItems(false);
	    
	    
	    
	   /* if (config.getBoolean("youAreAwesome")) {
	        player.sendMessage("You are awesome!");
	    } else {
	        player.sendMessage("You are not awesome...");
	        player.setCustomName("IDIOT");
	    }*/
	}
	
    // Fired when plugin is disabled
    @Override
    public void onDisable() {

    	this.getServer().getScheduler().cancelAllTasks();
    	
    }
    
    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            econ = economyProvider.getProvider();
        }

        return (econ != null);
    }
	
}
