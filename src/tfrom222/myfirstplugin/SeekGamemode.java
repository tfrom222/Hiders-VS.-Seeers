package tfrom222.myfirstplugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import net.milkbowl.vault.economy.Economy;



public class SeekGamemode {
	
	
	
	public static HashMap<String, Boolean> isPlayerHidingInBlock = new HashMap<String, Boolean>();
	
	// Random number generator
	public static Random randomgen = new Random();
	
	static Location seekerSpawnLoc;
	static boolean seekerSpawnIsSet;
	
	static Location hiderSpawnLoc;
	static boolean hiderSpawnIsSet;
	
	static int rewardMoney;
	

	
	static MyFirstPlugin plugin = MyFirstPlugin.plugin;
	static boolean useVault = plugin.useVault;
	private static Economy economy = plugin.econ;	
	// How many players?
	static int playerCount;

	// Scoreboard Manager
	public static ScoreboardManager manager = Bukkit.getScoreboardManager();
	
	// Scoreboard
	public static Scoreboard board = manager.getNewScoreboard();
	
	static Team seekerTeam;
	static Team hiderTeam;
	static Objective objective;
	static Objective timeLeftObjective;
	public static Score hiderCount;
	public static Score timeLeft;
	
	
	// The countdown timer
	static int taskID1;
	
	// How long a game should last
	static int gameTimeSeconds= MyFirstPlugin.plugin.config.getInt("GameTime");
	
	
	// The world the game is held in
	public static World world = Bukkit.getServer().getWorld(MyFirstPlugin.plugin.config.getString("GameWorld"));
	
	// has the game started?
	static boolean gameHasStarted;
	
	// The plugins config file
	public static FileConfiguration config = MyFirstPlugin.plugin.getConfig();
	
	// The seeker
	static Player seekerPlayer;
	
	// Gets the spawn location for the seeker
	public static Location getSeekerSpawnLoc()
	{	
		
		return seekerSpawnLoc;
	}
	
	public static Location getSeekerSpawnLocFromConfig()
	{	
		
		Location SeekerSpawnConfigLoc = new Location(Bukkit.getWorld("world"), config.getDouble("SeekerSpawnX"),config.getDouble("SeekerSpawnY"),config.getDouble("SeekerSpawnZ"));
		return SeekerSpawnConfigLoc;
	}
	
	public static void setSeekerSpawnLoc(Location SpawnLoc, Player host)
	{
		
		seekerSpawnLoc = SpawnLoc;
		
		config.set("SeekerSpawnX", seekerSpawnLoc.getX());
		config.set("SeekerSpawnY", seekerSpawnLoc.getY());
		config.set("SeekerSpawnZ", seekerSpawnLoc.getZ());
		
		
		
		MyFirstPlugin.plugin.saveConfig();
		
		
		if(seekerSpawnLoc != null)
		{
		seekerSpawnIsSet = true;
		config.set("SeekerSpawnSet", true);
		MyFirstPlugin.plugin.saveConfig();
		
		host.sendMessage("Succesfully set seeker spawn location!");
		}
		else
		{
			host.sendMessage("Could not set seeker spawn location!");
		}
	}
	
	public static Location getHiderSpawnLoc()
	{
		return hiderSpawnLoc;
	}
	
	public static Location getHiderSpawnLocFromConfig()
	{	
		
		Location HiderSpawnConfigLoc = new Location(Bukkit.getWorld(MyFirstPlugin.plugin.config.getString("GameWorld")), config.getDouble("HiderSpawnX"),config.getDouble("HiderSpawnY"),config.getDouble("HiderSpawnZ"));
		return HiderSpawnConfigLoc;
	}
	
	
	
	public static void setHiderSpawnLoc(Location SpawnLoc, Player host)
	{
		hiderSpawnLoc = SpawnLoc;
		
		config.set("HiderSpawnX", hiderSpawnLoc.getX());
		config.set("HiderSpawnY", hiderSpawnLoc.getY());
		config.set("HiderSpawnZ", hiderSpawnLoc.getZ());
		MyFirstPlugin.plugin.saveConfig();
		
		if(hiderSpawnLoc != null)
		{
		hiderSpawnIsSet = true;
		config.set("HiderSpawnSet", true);
		MyFirstPlugin.plugin.saveConfig();
		
		
		
		
		host.sendMessage("Succesfully set hider spawn location!");
		
		}
		else
		{
			host.sendMessage("Could not set hider spawn location!");
		}
	}
	
	
	public static Player getSeeker()
	{
		return seekerPlayer;
		
	}
	
	
	
	
	public static void PluginStartGamemode()
	{
		seekerSpawnLoc = getSeekerSpawnLocFromConfig();
		hiderSpawnLoc = getHiderSpawnLocFromConfig();
		
		hiderSpawnIsSet = config.getBoolean("HiderSpawnSet");
		seekerSpawnIsSet = config.getBoolean("SeekerSpawnSet");
		
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		
		useVault = MyFirstPlugin.useVault;
		
		if(useVault == true)
		{
			rewardMoney = config.getInt("Reward-Money");
		}
		
		
	}
	
	
	// Begin the gamemode
	public static void beginSeek(Player host)
	{
		
		if(Bukkit.getServer().getOnlinePlayers().size() > 1)
		{
		if(seekerSpawnIsSet == true && hiderSpawnIsSet == true && gameHasStarted == false && Bukkit.getServer().getOnlinePlayers().size() > 0 )
		{
			
		
		playerCount = Bukkit.getServer().getOnlinePlayers().size() - 1;
		
	
		
			objective = board.registerNewObjective("hidersleft", "dummy");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			objective.setDisplayName(ChatColor.DARK_GREEN+"Hiders VS. Seeker");
			hiderCount = objective.getScore("Hiders Left:");
			
			Bukkit.broadcastMessage(ChatColor.GOLD+"Hiders VS. Seeker Initializing");
			
		
			
			hiderCount.setScore(playerCount);
		
			startGameCountDown();
			
			Bukkit.broadcastMessage(ChatColor.GOLD+"Timer Started");
			
			Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getServer().getOnlinePlayers().size()]);
			
			seekerPlayer = players[randomgen.nextInt(players.length)];
			
			
			
			
	Bukkit.broadcastMessage(ChatColor.GOLD+seekerPlayer.getName() + " was chosen as the seeker!");
			
			seekerPlayer.setDisplayName(ChatColor.RED+"Seeker: " + ChatColor.GOLD+seekerPlayer.getName());
			
			
			seekerPlayer.playEffect(EntityEffect.VILLAGER_HEART);
			seekerPlayer.playSound(seekerPlayer.getLocation(), Sound.FIREWORK_TWINKLE, 10, 1.0f);
			seekerPlayer.addPotionEffect(PotionType.SPEED.getEffectType().createEffect(Integer.MAX_VALUE, 2));
			seekerPlayer.addPotionEffect(PotionType.REGEN.getEffectType().createEffect(Integer.MAX_VALUE, 400));
			seekerPlayer.addPotionEffect(PotionType.STRENGTH.getEffectType().createEffect(Integer.MAX_VALUE, 100));
			seekerPlayer.addPotionEffect(PotionType.NIGHT_VISION.getEffectType().createEffect(Integer.MAX_VALUE, 100));
			
			seekerTeam = board.registerNewTeam("Seekers");
			seekerTeam.addPlayer((OfflinePlayer) seekerPlayer);
			seekerTeam.setAllowFriendlyFire(false);
			seekerTeam.setDisplayName("Seeker");
			
			seekerPlayer.setScoreboard(board);
			// Add Seeker Items
			
			
	    	PlayerInventory inventory = seekerPlayer.getInventory();
	    	inventory.clear();
	    	
	    	
	    	// Seeker Sword
	    	ItemStack item = new ItemStack(Material.GOLD_SWORD);
	    	ItemMeta meta = item.getItemMeta();
	    	meta.setDisplayName( seekerPlayer.getDisplayName() + "'s "  + "Hider Killer 2000"); 
	    	meta.addEnchant(Enchantment.DURABILITY, 100, true);
	    	meta.addEnchant(Enchantment.DAMAGE_ALL, 100, true);
	    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
	       ArrayList<String> lore = new ArrayList<String>();
	       lore.add(ChatColor.GREEN + "Owner: " + ChatColor.GOLD +seekerPlayer.getDisplayName());
	       lore.add(ChatColor.RED + "Used To Catch & Kill Hiding Peeps.");
	       meta.setLore(lore);
	    	item.setItemMeta(meta);
	    	inventory.setItemInHand(item);
			
	    	seekerPlayer.setBedSpawnLocation(seekerSpawnLoc);
	    	seekerPlayer.teleport(seekerSpawnLoc);
	    	
	/////////////////////////////
	    	
			
			hiderTeam = board.registerNewTeam("Hiders");
			
			hiderTeam.setDisplayName("Hiders");
			
			hiderTeam.setAllowFriendlyFire(false);
			hiderTeam.setNameTagVisibility(NameTagVisibility.NEVER);
			
			
		 	for(Player all: Bukkit.getServer().getOnlinePlayers())
	    	{
	    		
			
				all.setGameMode(GameMode.ADVENTURE);
				all.setHealth(20);
			
	    		if(all.getName() != seekerPlayer.getName())
	    		{
	    		
	    			
	    			isPlayerHidingInBlock.put(all.getName(), false);
	    			
	    		all.getInventory().clear();
	    		all.setBedSpawnLocation(hiderSpawnLoc);
	    	
	    		all.teleport(hiderSpawnLoc);	
	    		all.sendMessage(ChatColor.GOLD+"Hiders VS. Seeker");
	    		all.sendMessage(ChatColor.GOLD+"Your objective is to hide from the seeker!");
	    		hiderTeam.addPlayer(all);
	    			all.setScoreboard(board);
	    		}
	    		else
	    		{
	    			all.sendMessage(ChatColor.GOLD+"Hiders VS. Seeker");
	        		all.sendMessage(ChatColor.GOLD+"Your objective is to kill all of the hiders!");
	    		}
	    		
		}
	    	
    	
    	gameHasStarted = true;	
    	
    	
	}
		
		else if(gameHasStarted == true)	
		{
			host.sendMessage(ChatColor.RED+"There is already a game running");
		}
	else if(Bukkit.getServer().getOnlinePlayers().size() < 0)
	{
		host.sendMessage("Not enough Players!!");
	}
	else
		{
			host.sendMessage(ChatColor.GOLD+"Hiders VS. Seeker");
			host.sendMessage(ChatColor.RED+"You need to set the spawnpoints for the hiders and seeker!");
		}
		}
		else
		{
			host.sendMessage(ChatColor.RED+"You need at least 2 players to play!");
		}
		
		
		
	}
	
	
	public static void beginSeekFromConsole(CommandSender sender)
	{
		if(Bukkit.getServer().getOnlinePlayers().size() > 1)
		{
		if(seekerSpawnIsSet == true && hiderSpawnIsSet == true && gameHasStarted == false && Bukkit.getServer().getOnlinePlayers().size() > 0 )
		{
	
		
		playerCount = Bukkit.getServer().getOnlinePlayers().size() - 1;

			
			
			
		
			objective = board.registerNewObjective("hidersleft", "dummy");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			objective.setDisplayName(ChatColor.DARK_GREEN+"Hiders VS. Seeker");
			hiderCount = objective.getScore("Hiders Left:");
			
			
			hiderCount.setScore(playerCount);
			
			
			startGameCountDown();
			
			
			Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getServer().getOnlinePlayers().size()]);
			
			seekerPlayer = players[randomgen.nextInt(players.length)];
			
			
			
			
	Bukkit.broadcastMessage(ChatColor.GOLD+seekerPlayer.getName() + " was chosen as the seeker!");
			
			seekerPlayer.setDisplayName(ChatColor.RED+"Seeker: " + ChatColor.GOLD+seekerPlayer.getName());
			
			
			seekerPlayer.playEffect(EntityEffect.VILLAGER_HEART);
			seekerPlayer.playSound(seekerPlayer.getLocation(), Sound.FIREWORK_TWINKLE, 10, 1.0f);
			seekerPlayer.addPotionEffect(PotionType.SPEED.getEffectType().createEffect(Integer.MAX_VALUE, 2));
			seekerPlayer.addPotionEffect(PotionType.REGEN.getEffectType().createEffect(Integer.MAX_VALUE, 400));
			seekerPlayer.addPotionEffect(PotionType.STRENGTH.getEffectType().createEffect(Integer.MAX_VALUE, 100));
			seekerPlayer.addPotionEffect(PotionType.NIGHT_VISION.getEffectType().createEffect(Integer.MAX_VALUE, 100));
			
			seekerTeam = board.registerNewTeam("Seekers");
			seekerTeam.addPlayer((OfflinePlayer) seekerPlayer);
			seekerTeam.setAllowFriendlyFire(false);
			seekerTeam.setDisplayName("Seeker");
			
			seekerPlayer.setScoreboard(board);
			// Add Seeker Items
			
			
	    	PlayerInventory inventory = seekerPlayer.getInventory();
	    	inventory.clear();
	    	
	    	
	    	// Seeker Sword
	    	ItemStack item = new ItemStack(Material.GOLD_SWORD);
	    	ItemMeta meta = item.getItemMeta();
	    	meta.setDisplayName( seekerPlayer.getDisplayName() + "'s "  + "Hider Killer 2000"); 
	    	meta.addEnchant(Enchantment.DURABILITY, 100, true);
	    	meta.addEnchant(Enchantment.DAMAGE_ALL, 100, true);
	    	meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
	       ArrayList<String> lore = new ArrayList<String>();
	       lore.add(ChatColor.GREEN + "Owner: " + ChatColor.GOLD +seekerPlayer.getDisplayName());
	       lore.add(ChatColor.RED + "Used To Catch & Kill Hiding Peeps.");
	       meta.setLore(lore);
	    	item.setItemMeta(meta);
	    	inventory.setItemInHand(item);
			
	    	seekerPlayer.setBedSpawnLocation(seekerSpawnLoc);
	    	seekerPlayer.teleport(seekerSpawnLoc);
	    	
	/////////////////////////////
	    	
			
			hiderTeam = board.registerNewTeam("Hiders");
			
			hiderTeam.setDisplayName("Hiders");
			
			hiderTeam.setAllowFriendlyFire(false);
			hiderTeam.setNameTagVisibility(NameTagVisibility.NEVER);
			
			
		 	for(Player all: Bukkit.getServer().getOnlinePlayers())
	    	{
	    		
			
				all.setGameMode(GameMode.ADVENTURE);
				all.setHealth(20);
			
	    		if(all.getName() != seekerPlayer.getName())
	    		{
	    		
	    		isPlayerHidingInBlock.put(all.getName(), false);	
	    		all.getInventory().clear();
	    		all.setBedSpawnLocation(hiderSpawnLoc);
	    		equipPlayers(all);
	    		all.teleport(hiderSpawnLoc);	
	    		all.sendMessage(ChatColor.GOLD+"Hiders VS. Seeker");
	    		all.sendMessage(ChatColor.GOLD+"Your objective is to hide from the seeker!");
	    		hiderTeam.addPlayer(all);
	    			all.setScoreboard(board);
	    		}
	    		else
	    		{
	    			all.sendMessage(ChatColor.GOLD+"Hiders VS. Seeker");
	        		all.sendMessage(ChatColor.GOLD+"Your objective is to kill all of the hiders!");
	    		}
	    		
		}
	    	
			
		

    	gameHasStarted = true;	
    	

    	
	}
		
		else if(gameHasStarted == true)	
		{
			sender.sendMessage(ChatColor.RED+"There is already a game running");
		}
	else
		{
		sender.sendMessage(ChatColor.GOLD+"Hiders VS. Seeker");
		sender.sendMessage(ChatColor.RED+"You need to set the spawnpoints for the hiders and seeker!");
		}
	}
		else
		{
			System.out.println("["+plugin.getName()+"]"+ " You need at least 2 players to play.");
		}
		
		
		
	}
	
	
	// Used to equip hiders.
	public static void equipPlayers(Player player)
	{
		
		// Check if player exists.
		if(player != null)
		{
			
			
			// Player Inventory
			PlayerInventory inv = player.getInventory();
			
			// Player Armor
			ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
			ItemStack ChestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);
			ItemStack Leggings = new ItemStack(Material.LEATHER_LEGGINGS);
			ItemStack Feet = new ItemStack(Material.LEATHER_BOOTS);
			
			
			
			// Armor Metadata
			ItemMeta helmetmeta = helmet.getItemMeta();
			ItemMeta chestMeta = ChestPlate.getItemMeta();
			ItemMeta LegMeta = Leggings.getItemMeta();
			ItemMeta FeetMeta = Feet.getItemMeta();
			
			
			// Armor Display Names
			helmetmeta.setDisplayName(ChatColor.GREEN+"Hider Hat");
			chestMeta.setDisplayName(ChatColor.GREEN+"Hider Shirt");
			LegMeta.setDisplayName(ChatColor.GREEN+"Hider Trousers");
			FeetMeta.setDisplayName(ChatColor.GREEN+"Hider Boots");
			
			
			// Hide Enchants
			helmetmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
			chestMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
			LegMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
			FeetMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
			
			// Add Enchants
			helmetmeta.addEnchant(Enchantment.WATER_WORKER, 100, true);
			FeetMeta.addEnchant(Enchantment.PROTECTION_FALL, Integer.MAX_VALUE, true);
			
			
			// Lore variables for armor
			 ArrayList<String> helmetLore = new ArrayList<String>();
			 ArrayList<String> chestLore = new ArrayList<String>();
			 ArrayList<String> LeggingsLore = new ArrayList<String>();
			 ArrayList<String> feetLore = new ArrayList<String>(); 
			 
			 // Set in armor lore
			 helmetLore.add(ChatColor.MAGIC+"Helmet worn by hiders.");
			 chestLore.add(ChatColor.MAGIC+"Shirt worn by hiders.");
			 LeggingsLore.add(ChatColor.MAGIC+"Trousers worn by hiders.");
			 feetLore.add(ChatColor.MAGIC+"Boots worn by hiders.");
			 
			 // Add lore to metadata
			 helmetmeta.setLore(helmetLore);
			 chestMeta.setLore(chestLore);
			 LegMeta.setLore(LeggingsLore);
			 FeetMeta.setLore(feetLore);
			 
			 // Set metadata for items
			helmet.setItemMeta(helmetmeta);
			ChestPlate.setItemMeta(chestMeta);
			Leggings.setItemMeta(LegMeta);
			Feet.setItemMeta(FeetMeta);
			
			// Add & equip items to players
			inv.setHelmet(helmet);
			inv.setChestplate(ChestPlate);
			inv.setLeggings(Leggings);
			inv.setBoots(Feet);
			
			
			
		}
	 

	}
	
	public static void stopSeekWin(boolean didSeekerWin, boolean manualStop)
	{
		if(gameHasStarted == true)
		{
			
			
			
		gameHasStarted = false;
		for(Player all: Bukkit.getServer().getOnlinePlayers()){
        	
				all.setHealth(20);
        		all.removePotionEffect(PotionEffectType.NIGHT_VISION);
        		all.removePotionEffect(PotionType.SPEED.getEffectType());
        		all.removePotionEffect(PotionType.REGEN.getEffectType());
        		all.removePotionEffect(PotionType.STRENGTH.getEffectType());
        		all.teleport(world.getSpawnLocation());
        		all.getInventory().clear();
        		all.setDisplayName(all.getName());
        		
        		   
        		all.getInventory().setHelmet(null);
        		all.getInventory().setChestplate(null);
        		all.getInventory().setLeggings(null);
        		all.getInventory().setBoots(null);
        		
        		if(manualStop == false)
        		{
        		if(didSeekerWin == false && all != seekerPlayer)
        		{
        			
        			if(useVault == true && economy != null)
        			{
        			
        				if(economy.hasAccount(all))
        				{
        					economy.depositPlayer(all, rewardMoney);
            				
        					if(rewardMoney > 1)
        					{		
        						all.sendMessage(ChatColor.GREEN+"You have been awarded " + rewardMoney + economy.currencyNamePlural());
        					}
        					else
        					{
        						all.sendMessage(ChatColor.GREEN+"You have been awarded " + rewardMoney + economy.currencyNameSingular());
        					}
        				}
        				else
        				{
        					economy.createPlayerAccount(all);
        					economy.depositPlayer(all, rewardMoney);
        				
        					if(rewardMoney > 1)
        					{		
        						all.sendMessage(ChatColor.GREEN+"You have been awarded " + rewardMoney + economy.currencyNamePlural());
        					}
        					else
        					{
        						all.sendMessage(ChatColor.GREEN+"You have been awarded " + rewardMoney + economy.currencyNameSingular());
        					}
        				}
        			}
        		}
        		else
        		{
        			
        			if(useVault == true && economy != null)
        			{
        				if(economy.hasAccount(all))
        				{
        					economy.depositPlayer(all, rewardMoney);
            				
        					if(rewardMoney > 1)
        					{		
        						all.sendMessage(ChatColor.GREEN+"You have been awarded " + rewardMoney + economy.currencyNamePlural());
        					}
        					else
        					{
        						all.sendMessage(ChatColor.GREEN+"You have been awarded " + rewardMoney + economy.currencyNameSingular());
        					}
        				}
        				else
        				{
        					economy.createPlayerAccount(all);
        					economy.depositPlayer(all, rewardMoney);
        				
        					if(rewardMoney > 1)
        					{		
        						all.sendMessage(ChatColor.GREEN+"You have been awarded " + rewardMoney + economy.currencyNamePlural());
        					}
        					else
        					{
        						all.sendMessage(ChatColor.GREEN+"You have been awarded " + rewardMoney + economy.currencyNameSingular());
        					}
        				}
     		   
        			}
        		}
        	}
        		
		}
		
		seekerPlayer = null;
		hiderTeam.unregister();
		seekerTeam.unregister();
		objective.unregister();
		isPlayerHidingInBlock.clear();
		
        	
        	Bukkit.broadcastMessage(  ChatColor.GOLD + "Hiders VS. Seeker: " + ChatColor.WHITE+"Cleared all player inventories");
        	Bukkit.broadcastMessage(   ChatColor.GOLD+ "Hiders VS. Seeker: " + ChatColor.WHITE+"Stopped Gamemode");
        	
        	

        	
        	 gameTimeSeconds=180;
        	}
	}
 
		
	
	
	
	public static Runnable startGameCountDown()
	{
		// REPEATING TASKS COUNTDOWN DEMO
		taskID1 = MyFirstPlugin.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(MyFirstPlugin.plugin, new Runnable() {
			  public void run() {
				  
				  gameTimeSeconds--;
				  
				  
				  for(Player p : Bukkit.getOnlinePlayers()){
	                    p.setLevel(gameTimeSeconds);
	                    p.setExp(gameTimeSeconds / 100);
	                    
	                }
				  
				  if(playerCount <= 0)
				  {
					  Bukkit.broadcastMessage(ChatColor.GOLD+"The Seeker Won!!!");
					  stopSeekWin(true, false);
					  MyFirstPlugin.plugin.getServer().getScheduler().cancelTask(taskID1);
				  }
				
				  
				  
				  if (gameTimeSeconds == 60 || gameTimeSeconds == 10 || gameTimeSeconds == 9 || gameTimeSeconds == 8 || gameTimeSeconds == 7 || gameTimeSeconds == 6 || gameTimeSeconds == 5 || gameTimeSeconds == 4 || gameTimeSeconds == 3 || gameTimeSeconds == 2 || gameTimeSeconds == 1) {
					  Bukkit.broadcastMessage(gameTimeSeconds + " second(s) left until game ends!");
				  }
				  if (gameTimeSeconds==0) {
				     
				      gameTimeSeconds=180;
				      Bukkit.broadcastMessage(ChatColor.GOLD+"The Hiders Won!!!");
				      stopSeekWin(false, false);
				       MyFirstPlugin.plugin.getServer().getScheduler().cancelTask(taskID1);
				  }
				  if(gameHasStarted == false)
				  {
					  MyFirstPlugin.plugin.getServer().getScheduler().cancelTask(taskID1);
				  }

			  }
			}, 20L, 20L); // once each second
		return null;
	}
	
	public static void updateHiderCount(int count)
	{
		playerCount = count;
		hiderCount.setScore(count);
		System.out.println("Updated Hider Count");
	}
	
	public static void decreaseHiderCount()
	{
		
		int hiders = playerCount;
		hiders--;
		updateHiderCount(hiders);
	}
	
}
	
	
	
	
	
	
	
	
	

