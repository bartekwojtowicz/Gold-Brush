package pl.bartix.goldbrush;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Gold_Brush extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.getCommand("brush").setExecutor(new BrushCommand(this));
        this.getCommand("testowybrush").setExecutor(new TestowyBrushCommand(this));
        getServer().getPluginManager().registerEvents(new PickaxeListener(this), this);

        sendConsoleMessage("plugin_enabled");
        sendConsoleMessage("plugin_download");
    }

    @Override
    public void onDisable() {
        sendConsoleMessage("plugin_disabled");
        sendConsoleMessage("plugin_download");
    }

    public void sendConsoleMessage(String path) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages." + path)));
    }
}
