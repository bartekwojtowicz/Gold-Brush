package pl.bartix.goldbrush;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class TestowyBrushCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final HashMap<UUID, BukkitTask> activeBrushTasks = new HashMap<>();

    public TestowyBrushCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.player_only")));
            return true;
        }

        Player player = (Player) sender;
        UUID playerId = player.getUniqueId();

        if (activeBrushTasks.containsKey(playerId)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.brush_already_active")));
            return true;
        }
        // Materiał, lore oraz nazwa Brusha
        ItemStack brush = new ItemStack(Material.valueOf(plugin.getConfig().getString("brush.material")));
        ItemMeta meta = brush.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("brush.name")));
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "brush_size"), PersistentDataType.STRING, plugin.getConfig().getString("brush.size"));
            meta.setLore(plugin.getConfig().getStringList("brush.lore"));
            brush.setItemMeta(meta);
        }

        player.getInventory().addItem(brush);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.brush_received")));

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.brush_expired")));
                activeBrushTasks.remove(playerId);
                player.getInventory().remove(brush);
            }
        }.runTaskLater(plugin, plugin.getConfig().getInt("brush.duration") * 20L);

        activeBrushTasks.put(playerId, task);

        new BukkitRunnable() {
            int timeLeft = plugin.getConfig().getInt("brush.duration");

            @Override
            public void run() {
                if (!player.isOnline() || !activeBrushTasks.containsKey(playerId)) {
                    this.cancel();
                    return;
                }

                timeLeft--;
                sendActionBar(player, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.brush_actionbar")));

                if (timeLeft <= 0) {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20);

        return true;
    }
        //Wiadomość na acionbar
    private void sendActionBar(Player player, String message) {
        try {
            if (player.spigot() != null) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                return;
            }
        } catch (NoClassDefFoundError | NoSuchMethodError e) {
        }

        try {
            Object chatComponentText = Class.forName("net.minecraft.network.chat.ChatComponentText").getConstructor(String.class).newInstance(message);
            Object packetPlayOutChat = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutChat")
                    .getConstructor(Class.forName("net.minecraft.network.chat.IChatBaseComponent"), byte.class)
                    .newInstance(chatComponentText, (byte) 2);
            Object playerConnection = player.getClass().getMethod("getHandle").invoke(player)
                    .getClass().getField("b").get(player.getClass().getMethod("getHandle").invoke(player));
            playerConnection.getClass().getMethod("a", packetPlayOutChat.getClass()).invoke(playerConnection, packetPlayOutChat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
