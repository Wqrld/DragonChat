package ga.imluc.dragonchat;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;

import java.awt.*;
import java.io.File;


public class Main extends JavaPlugin implements Listener {

    public static Main plugin;


    public FileConfiguration config = this.getConfig();
    public FileConfiguration fileConfiguration;
    public static boolean isPluginAvailable(String pluginName) {
        PluginManager pm = Bukkit.getPluginManager();
        Plugin plugin = pm.getPlugin(pluginName.trim());
        return ((plugin != null) && (plugin.isEnabled()));
    }


    public Main() {
        plugin = this;
    }


public void rlconf(){
    reloadConfig();

}

public FileConfiguration getconfig(){
    return config;
}

    @Override
    public void onEnable() {
        //Fired when the server enables the plugin
        getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getConsoleSender().sendMessage("&8[&eDragonchat&8]&e - loaded");
        createConfig();
        this.getCommand("dchat").setExecutor(new Dchat());
    }

    @Override
    public void onDisable() {
        //Fired when the server stops and disables all plugins
    }




    private void createConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");

            if (!file.exists()) {
                getLogger().info("Config.yml not found, creating!");
                saveDefaultConfig();
                YamlConfiguration.loadConfiguration(file);
            } else {
                getLogger().info("Config.yml found, loading!");
                fileConfiguration = YamlConfiguration.loadConfiguration(file);
                Bukkit.getConsoleSender().sendMessage("&8[&eDragonchat8]&e - " + this.getConfig().getString("test"));

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }



    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();

       // String oldformat = "&8[[Faction]&8] &8[&7%vault_prefix%&r&8] &7%player_name%: &f" + event.getMessage();





        Bukkit.getConsoleSender().sendMessage("&8[&eDragonchat8]&e - " + config.getString("format"));

        String oldformat = config.getString("format").replace("%", "%") + event.getMessage();
        String format = PlaceholderAPI.setPlaceholders(event.getPlayer(), ChatColor.translateAlternateColorCodes('&', oldformat));
        Bukkit.getConsoleSender().sendMessage("&8[&eDragonchat8]&e - " + format + "\n" + config.getString("format") + "\nThatwasit");
        format = format.replace("%§", "%%§");

        //.replace("&", "§");
        event.setFormat(format);



//format = format.replace("[holding]", "§e§l" + event.getPlayer().getItemInHand().getI18NDisplayName() + " (" + event.getPlayer().getItemInHand().getAmount() + ")§r");

//TextComponent it = new TextComponent( "§e§l" + event.getPlayer().getItemInHand().getI18NDisplayName() + " (" + event.getPlayer().getItemInHand().getAmount() + ")" );
//it.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( event.getPlayer().getItemInHand().getItemMeta().getDisplayName() ).create()));
//TextComponent form = new TextComponent(oldformat)
        format = format.replace("[holding]", "§e§l" + event.getPlayer().getItemInHand().getI18NDisplayName() + " (" + event.getPlayer().getItemInHand().getAmount() + ")");

        if(isPluginAvailable("Factions")) {
        event.setCancelled(true);
            FPlayer me = FPlayers.getInstance().getByPlayer(event.getPlayer());
        for (Player listeningPlayer : event.getRecipients()) {
            FPlayer you = FPlayers.getInstance().getByPlayer(listeningPlayer);
            //    event.setFormat(format.replace("[Faction]", me.getChatTag(you).trim()));
            listeningPlayer.sendMessage(format.replace("[Faction]", me.getChatTag(you).trim()));


        }


    }


    }
}


   /* @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            Bukkit.getConsoleSender().sendMessage("&8[&eDragonchat8]&e - cancelled");
            return;
        }
        Bukkit.getConsoleSender().sendMessage("&8[&eDragonchat8]&e - " + event.getMessage());
event.setMessage("pizza:" + event.getMessage());

    }(/
}*/
