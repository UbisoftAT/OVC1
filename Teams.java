package at.ubisoft.odv.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Teams implements CommandExecutor {
	
	int teams;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player)sender;
		
		if (args.length == 0) {
	        
			p.sendMessage("§8§m-----------");
	        p.sendMessage("§e/team [Spieler]");
	        p.sendMessage("§e/team accept");
	        p.sendMessage("§e/team deny");
			p.sendMessage("§8§m-----------");
	      } else if (args.length == 1 && args[0].equalsIgnoreCase("accept")) {
	        
	        if (getConfig().contains("invite." + p.getName().toLowerCase())) {
	          
	          String inviter = getConfig().getString("invite." + p.getName().toLowerCase());
	          if (Bukkit.getPlayer(inviter) != null) {

	         p.sendMessage("§eItsLoru §8| §aDu und §e" + Bukkit.getPlayer(inviter).getName() + " §aseid nun im Team");

	          } else {
	           
	            p.sendMessage("§eItsLoru §8| §cDieser Spieler ist Offline.");
	            return true;
	          } 
	          
	          List<String> list = getConfig().getStringList("team." + p.getName().toLowerCase());
	          list.add(inviter.toLowerCase());
	          
	          List<String> list2 = getConfig().getStringList("team." + inviter.toLowerCase());
	          list2.add(p.getName().toLowerCase());
	          getConfig().set("invite." + inviter.toLowerCase(), null);
	          getConfig().set("invite." + p.getName().toLowerCase(), null);
	          getConfig().set("team." + p.getName().toLowerCase(), list);
	          getConfig().set("team." + inviter.toLowerCase(), list2);
	          saveConfig();
	        } else {
	          
	            p.sendMessage("§eItsLoru §8| §cEs wurde dir keine Anfrage geschickt.");
	        }
	      
	      } else if (args.length == 1 && args[0].equalsIgnoreCase("deny")) {
	        
	        if (getConfig().contains("invite." + p.getName().toLowerCase())) {
	          
	          String inviter = getConfig().getString("invite." + p.getName().toLowerCase());
	          if (Bukkit.getPlayer(inviter) != null) {
	            
	            p.sendMessage("§aDu hast erfolgreich die Anfrage von §e" + Bukkit.getPlayer(inviter).getName() + " §aabgelehnt!");
	          }
	          else {
	            
		            p.sendMessage("§eItsLoru §8| §cDieser Spieler ist Offline.");

		            return true;
	          } 
	          getConfig().set("invite." + inviter.toLowerCase(), null);
	          getConfig().set("invite." + p.getName().toLowerCase(), null);
	        } else {
	          
	            p.sendMessage("§eItsLoru §8| §cEs wurde dir keine Anfrage geschickt.");

	        }
	      
	      } else if (args.length == 1 && !args[0].equalsIgnoreCase("accept") && !args[0].equalsIgnoreCase("list")) {
	        
	        if (!args[0].toLowerCase().equals(p.getName().toLowerCase())) {
	          
	          if (Bukkit.getPlayerExact(args[0]) != null) {
	            
	            if (!getConfig().getStringList("team." + p.getName().toLowerCase()).contains(args[0].toLowerCase())) {
	              
	              getConfig().set("invite." + args[0].toLowerCase(), p.getName().toLowerCase());
	              saveConfig();
	              Player invited = Bukkit.getPlayer(args[0]);
	              invited.sendMessage("§a" + p.getName() + " §amöchte mit dir im Team sein!");
	              invited.sendMessage("§aAnnehmen: §e/team accept");
	              invited.sendMessage("§cAblehnen: §e/team deny");
	              p.sendMessage("§eItsLoru §8| §aDu hast dem Spieler §e" + Bukkit.getPlayer(args[0]).getName() + " §aeine Anfrage geschickt.");
	            } else {
	              
	              p.sendMessage("§eItsLoru §8| §cDu hast das Team von §e" + Bukkit.getPlayer(args[0]).getName() + " §cverlassen!");
	              Player invited = Bukkit.getPlayer(args[0]);
	              invited.sendMessage("§7" + p.getName() + " §7hat den Team mit dir §3aufgel§st!");
	              
	              List<?> list = getConfig().getStringList("team." + p.getName().toLowerCase());
	              list.remove(args[0].toLowerCase());
	              
	              List<?> list2 = getConfig().getStringList("team." + args[0].toLowerCase());
	              list2.remove(p.getName().toLowerCase());
	              getConfig().set("team." + p.getName().toLowerCase(), list);
	              getConfig().set("team." + args[0].toLowerCase(), list2);
	              saveConfig();
	            } 
	          } else {
	            
		            p.sendMessage("§eItsLoru §8| §cDieser Spieler ist Offline.");
	          } 
	        } else {
	          
	            p.sendMessage("§eItsLoru §8| §cDu kannst kein Team mit dir selber machen.");
	        } 
	    }
	    return true;
	  }
	
	public File getFile() {
		return new File("plugins/ODV/teams.yml");
	}
	
	public YamlConfiguration getConfig() {
		return YamlConfiguration.loadConfiguration(getFile());
	}
	
	public void saveConfig() {
		try {
			getConfig().save(getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
