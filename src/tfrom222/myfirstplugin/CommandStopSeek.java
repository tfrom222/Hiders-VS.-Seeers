package tfrom222.myfirstplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tfrom222.myfirstplugin.SeekGamemode;

public class CommandStopSeek implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args	) {
         	
	            	SeekGamemode.stopSeekWin(false, true);
		  
	            
		
		
		  
		  return true;

	}
}

