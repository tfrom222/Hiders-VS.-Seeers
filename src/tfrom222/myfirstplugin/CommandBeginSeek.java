package tfrom222.myfirstplugin;

import java.util.Random;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tfrom222.myfirstplugin.SeekGamemode;



public class CommandBeginSeek implements CommandExecutor {

	 Random randomGenerator = new Random();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		   if (sender instanceof Player) {
	            Player player = (Player) sender;
	           
	           
	           
	            if(sender.isOp())
	            {
	            
	            if(args.length == 0) 
	            { 
	            	sender.sendMessage("Starting Hiders VS. Seeker");
	            	SeekGamemode.beginSeek(player);	
	            	
	            }
	            else{
	            
	            	
	            	player.sendMessage("Too many arguments!");
	
	            		
	            }
	            	
	            }
	            	
	         }
		   else{
			 
           	
           		
           	SeekGamemode.beginSeekFromConsole(sender);	
			   
			   
		   }
		
		   
		return true;
	}
	
}
		   


