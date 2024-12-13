package pl.bartix.goldbrush;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BrushCommand implements TabExecutor {

    private final JavaPlugin plugin;

    public BrushCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2 || !args[0].equalsIgnoreCase("nadaj")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.usage")));
            return true;
        }

        if (args.length == 2) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.player_only")));
                return true;
            }

            Player player = (Player) sender;
            String size = args[1];
            if (!size.matches("2x2|3x3|4x4|5x5|6x6")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.invalid_size")));
                return true;
            }

            ItemStack brush = createBrush(size);
            player.getInventory().addItem(brush);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.brush_received").replace("{size}", size)));

            // Dodanie komunikatu o ustawieniach konfiguracji
            if (!plugin.getConfig().getBoolean("allow_brush_removal")) {
                player.sendMessage(ChatColor.RED + "Uwaga! Nie możesz wyrzucić tego brusha z ekwipunku.");
            }
            return true;
        }

        if (args.length == 3) {
            String targetName = args[1];
            String size = args[2];
            if (!size.matches("2x2|3x3|4x4|5x5|6x6")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.invalid_size")));
                return true;
            }

            Player target = Bukkit.getPlayer(targetName);
            if (target == null) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.player_not_online").replace("{player}", targetName)));
                return true;
            }

            ItemStack brush = createBrush(size);
            target.getInventory().addItem(brush);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.brush_given_sender").replace("{size}", size).replace("{player}", targetName)));
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages.brush_given_target").replace("{size}", size).replace("{sender}", sender.getName())));

            // Dodanie komunikatu o ustawieniach konfiguracji
            if (!plugin.getConfig().getBoolean("allow_brush_removal")) {
                target.sendMessage(ChatColor.RED + "Uwaga! Nie możesz wyrzucić tego brusha z ekwipunku.");
            }
            return true;
        }

        return false;
    }

    private ItemStack createBrush(String size) {
        ItemStack brush = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = brush.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Brush " + size);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "brush_size"), PersistentDataType.STRING, size);
            brush.setItemMeta(meta);
        }
        return brush;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("nadaj");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("nadaj")) {
            return Bukkit.getOnlinePlayers().stream().map(player -> player.getName()).collect(Collectors.toList());
        } else if (args.length == 3 && args[0].equalsIgnoreCase("nadaj")) {
            return Arrays.asList("2x2", "3x3", "4x4", "5x5", "6x6");
        }
        return new ArrayList<>();
    }
}
