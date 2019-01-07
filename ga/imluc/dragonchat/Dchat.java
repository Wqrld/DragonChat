package ga.imluc.dragonchat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ga.imluc.dragonchat.Main;

public class Dchat implements CommandExecutor {





    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(args.length < 1) {
                player.sendMessage("/dchat reload");
                return true;
            }

            if(args[0].equalsIgnoreCase("reload")){
Main.
sender.sendMessage(Main.plugin.getConfig().getString("test"));
                player.sendMessage("config reloaded");



            }



            // Here we need to give items to our player
        }

        // If the player (or console) uses our command correct, we can return true
        return true;
    }

}
