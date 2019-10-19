package ga.imluc.dragonchat;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
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
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;

import java.io.File;


public class Main extends JavaPlugin implements Listener {
    private static Permission perms = null;
    public static Main plugin;
    public static Chat chat = null;


    public FileConfiguration fileConfiguration;

    public static boolean isPluginAvailable(String pluginName) {
        PluginManager pm = Bukkit.getPluginManager();
        Plugin plugin = pm.getPlugin(pluginName.trim());
        return ((plugin != null) && (plugin.isEnabled()));
    }


    public Main() {
        plugin = this;
    }


    @Override
    public void onEnable() {


        //Fired when the server enables the plugin
        getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getConsoleSender().sendMessage("&8[&eDragonchat&8]&e - loaded");
        createConfig();
        this.getCommand("dchat").setExecutor(new Dchat());

        if (isPluginAvailable("Vault")) {
            setupChat();
            setupPermissions();
        } else {
            Bukkit.getPluginManager().disablePlugin(this);
            Bukkit.getConsoleSender().sendMessage("&8[&eDragonchat&8]&e - this plugin requires vault to detect roles and chatcolors");
        }

    }

    @Override
    public void onDisable() {
        //Fired when the server stops and disables all plugins
        Bukkit.getConsoleSender().sendMessage("&8[&eDragonchat&8]&e - disabling");
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
                //   Bukkit.getConsoleSender().sendMessage("&8[&eDragonchat8]&e - " + this.getConfig().getString("test.test"));

            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {

        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();

        // String oldformat = "&8[[Faction]&8] &8[&7%vault_prefix%&r&8] &7%player_name%: &f" + event.getMessage();


        //  Bukkit.getConsoleSender().sendMessage("&8[&eDragonchat8]&e - " + config.getString("format"));

        String format = getConfig().getString("format").replace("%", "%");
        format = format.replace("[message]", event.getMessage());
        format = PlaceholderAPI.setPlaceholders(event.getPlayer(), ChatColor.translateAlternateColorCodes('&', format));


        String chatColor = getConfig().getString("color." + perms.getPrimaryGroup(player));
        if (chatColor == null) {
            chatColor = getConfig().getString("defaultcolor");
        }
        format = format.replace("[chatcolor]", ChatColor.translateAlternateColorCodes('&', chatColor));
        format = format.replace("[prefix]", ChatColor.translateAlternateColorCodes('&', chat.getPlayerPrefix(player)));


        format = format.replace("[username]", player.getName());
        format = format.replace("%§", "%%§");
//        if (format.contains("[item]")) {
            format = format.replace("[item]", "§e§l" + event.getPlayer().getItemInHand().getType().name() + " (" + event.getPlayer().getItemInHand().getAmount() + ")");
//        }


        Bukkit.getConsoleSender().sendMessage("§8[§eDragonchat§8]§e - " + format);
        event.setFormat(format);


//format = format.replace("[holding]", "§e§l" + event.getPlayer().getItemInHand().getI18NDisplayName() + " (" + event.getPlayer().getItemInHand().getAmount() + ")§r");

//TextComponent it = new TextComponent( "§e§l" + event.getPlayer().getItemInHand().getI18NDisplayName() + " (" + event.getPlayer().getItemInHand().getAmount() + ")" );
//it.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( event.getPlayer().getItemInHand().getItemMeta().getDisplayName() ).create()));
//TextComponent form = new TextComponent(oldformat)

        if (isPluginAvailable("Factions")) {
            event.setCancelled(true);
            FPlayer me = FPlayers.getInstance().getByPlayer(event.getPlayer());
            for (Player listeningPlayer : event.getRecipients()) {
                FPlayer you = FPlayers.getInstance().getByPlayer(listeningPlayer);
                //    event.setFormat(format.replace("[Faction]", me.getChatTag(you).trim()));
                if (me.getChatTag(you).trim().length() > 2) {
                    format = format.replace("[faction]", me.getChatTag(you).trim() + " ");
                } else {
                    format = format.replace("[faction]", "§2Factionless");
                }
                listeningPlayer.sendMessage(format);

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
