package tfrom222.myfirstplugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tfrom222.myfirstplugin.SeekGamemode;



public class CommandSetupSeek implements CommandExecutor {

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		   if (sender instanceof Player) {
	            Player player = (Player) sender;
	           
	           
	           
	            if(sender.isOp())
	            {
	            
	            	if(args.length == 0) 
		            { 
	            		sender.sendMessage(ChatColor.GOLD+"Hiders VS. Seeker");
		            	sender.sendMessage("setseekerspawn, sethiderspawn");
		            }
	            	
	            	else if(args.length > 0)
	            	{
	            		
	            		if(args[0].equals("setseekerspawn"))
	            		{
	            			SeekGamemode.setSeekerSpawnLoc(player.getLocation(), player);
	            		}
	            		else if(args[0].equals("sethiderspawn"))
	            		{
	            			SeekGamemode.setHiderSpawnLoc(player.getLocation(), player);
	            		}
	            		
	            		else
	            		{
	            			player.sendMessage(ChatColor.RED+"Unknown argument: " + ChatColor.GRAY+ args[0]);
			            	player.sendMessage("setseekerspawn, sethiderspawn");
	            		}
	            		
	            	}
	            	else
	            	{
	            		player.sendMessage("Something Went Wrong!");
	            	}
	            	
	            	
	            	
	            	
	            }
	            else{
	            	
	            	sender.sendMessage(ChatColor.GOLD+"Hiders VS. Seeker");
	            	sender.sendMessage(ChatColor.RED+"You are are not an operator");
	            	
	            }
	            
       
		   
	            
		

		   }
		return true;
	}
}
		   


