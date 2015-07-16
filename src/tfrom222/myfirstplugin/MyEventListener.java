package tfrom222.myfirstplugin;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

import tfrom222.myfirstplugin.MyFirstPlugin;

public class MyEventListener implements Listener {

	
	Random randomGen = new Random();
	
	MyFirstPlugin plugin = MyFirstPlugin.plugin;
	
	
	
	
	 @EventHandler
	 public void onKill(PlayerDeathEvent e)
	 {
		 
	if(SeekGamemode.gameHasStarted == true){
	 String killed = e.getEntity().getDisplayName();
	 
	 String killer = e.getEntity().getKiller().getDisplayName();
	 
	 e.getEntity().setHealth(20);
	 
	 
	 
	 if(killer != null)
	 {
	 e.setDeathMessage(ChatColor.RED + killed + " Was found by " + killer);
	 }
	 else if(e.getEntity().getName().equals(SeekGamemode.getSeeker().getName()))
	 {
		 Bukkit.broadcastMessage(ChatColor.GOLD+"The hiders won!!!");
		 SeekGamemode.stopSeekWin(false, false);
		 
	 }
	 else
	 {
		 e.setDeathMessage(ChatColor.RED + killed + " committed suicide");
	 }
	 
	 SeekGamemode.decreaseHiderCount();
	 
	 if(SeekGamemode.playerCount <= 0)
	 {
		 Bukkit.broadcastMessage(ChatColor.GOLD+"The seeker has won!!!");
		 SeekGamemode.stopSeekWin(true, false);
	 }
	 
	}
	}
	 
	 @EventHandler
	 public void onPlayerDamage(EntityDamageEvent e){
		 if(e.getEntity() instanceof Player)
		 {
		 Player player = (Player) e.getEntity();
		 
		 
		 player.getWorld().playEffect(player.getLocation(), Effect.COLOURED_DUST, 1);
		 player.getWorld().playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 100, randomGen.nextFloat());
		 
		 
		 if(SeekGamemode.gameHasStarted == true){
			 		 
		 if(player.getName().equals(SeekGamemode.getSeeker().getName()))
		 {
			 if(player.getHealth() < 1)
			 {
				 Bukkit.broadcastMessage(ChatColor.GOLD+"The Hiders Won!!!");
				 SeekGamemode.stopSeekWin(false, false);
			 }
			 
			 
		 }
		 else
		 {
			 if(player.getHealth() < 1)
			 {
				 player.setHealth(20);
				 player.getInventory().clear();
				 player.teleport(SeekGamemode.world.getSpawnLocation());
				 
				 SeekGamemode.decreaseHiderCount();
				 
				 
				 if(SeekGamemode.playerCount <= 0)
				 {
					 Bukkit.broadcastMessage(ChatColor.GOLD+"The seeker has won");
					 SeekGamemode.stopSeekWin(true, false);
				 }
				 
			 }
		 }
		 
	}
		 else
		 {
			 player.setHealth(20);
		 }
}
		 
		 
	
		 
  }
	 
	 @EventHandler
	 public void onJoin(PlayerJoinEvent event)
	 {
		Player player = event.getPlayer();
		player.teleport(player.getWorld().getSpawnLocation());
		
		if(SeekGamemode.gameHasStarted == true)
		{
		 player.teleport(player.getWorld().getSpawnLocation());
		}
		 
	 }
	 
	 @EventHandler
	 public void onQuit(PlayerQuitEvent event)
	 {
		 if(SeekGamemode.gameHasStarted == true)
		 {
			 SeekGamemode.decreaseHiderCount();
			 
			 if(event.getPlayer() == SeekGamemode.seekerPlayer)
			 {
				 Bukkit.broadcastMessage(ChatColor.RED+"Seeker quit the game.");
				 SeekGamemode.stopSeekWin(false, false);
			 }
		 }
	 }
	 
	 
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
		Block clickedBlock = e.getClickedBlock();
		
		Player player = e.getPlayer();	
		
		
		
		if(SeekGamemode.gameHasStarted == true && clickedBlock != null && player != null){
		/*if(clickedBlock.getType() == Material.CHEST )
		{
			player.sendMessage("Clicked on chest: " + e.getClickedBlock().getX()+ ", " + e.getClickedBlock().getY() + ", " + e.getClickedBlock().getZ());
			
			if(SeekGamemode.isPlayerHidingInBlock.containsKey(player.getName()) == true)
					{
				
				player.sendMessage("Bacon");
					if(SeekGamemode.isPlayerHidingInBlock.get(player.getName()) == false)
					{
						SeekGamemode.isPlayerHidingInBlock.put(player.getName(), true);
					player.setFallDistance(0);
					player.teleport(clickedBlock.getLocation(), TeleportCause.PLUGIN);
					player.setFallDistance(0);
					player.addPotionEffect(PotionEffectType.JUMP.createEffect(Integer.MAX_VALUE, -100));
					player.addPotionEffect(PotionEffectType.SLOW.createEffect(Integer.MAX_VALUE, 100));
					}
					else
					{
						SeekGamemode.isPlayerHidingInBlock.put(player.getName(), false);
						player.removePotionEffect(PotionEffectType.JUMP);
						player.removePotionEffect(PotionEffectType.SLOW);
					}
				}
			}*/
		}
		
	}
	
}
	
	ItemStack Helmet = null;
	ItemStack ChestPlate = null;
	ItemStack Leggings =  null;
	 ItemStack boots =  null;
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
	Material longGrass = Material.LONG_GRASS;
	Material longFlower = Material.DOUBLE_PLANT;
	Block block = event.getPlayer().getLocation().getBlock().getRelative(BlockFace.SELF);
	Player player = event.getPlayer();
	PlayerInventory inv = event.getPlayer().getInventory();
	
	 
	
	 
	 if(SeekGamemode.gameHasStarted == true && player != SeekGamemode.getSeeker())
	 {
	   if (block.getType() == longGrass || block.getType() == longFlower){
	                     
		   player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(Integer.MAX_VALUE, 100));
		   
		   /*Helmet = inv.getHelmet();
		   ChestPlate =  inv.getChestplate();
		   Leggings =  inv.getLeggings();
		   boots =  inv.getBoots();*/
		   
		   if(Helmet == null)
		   {
			   Helmet = inv.getHelmet();
		   }
		   if(ChestPlate == null)
		   {
			   ChestPlate =  inv.getChestplate();
		   }
		   if(Leggings == null)
		   {
			   Leggings =  inv.getLeggings();
		   }
		   if(boots == null)
		   {
			   boots =  inv.getBoots();
		   }  
		   
		   
		   inv.setHelmet(null);
		   inv.setChestplate(null);
		   inv.setLeggings(null);
		   inv.setBoots(null);
		   
		   
		 
		   
		   
	   }
	   else if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
	   {
	
		   player.removePotionEffect(PotionEffectType.INVISIBILITY);
		   
		   
			   inv.setHelmet(Helmet);
		   
		   
		   
			   inv.setChestplate(ChestPlate);
		   
		  
		   
			   inv.setLeggings(Leggings);
		
			   inv.setBoots(boots);
		   
		   
		
		   
		   
	   }
	 }
	   
	}
	
}


