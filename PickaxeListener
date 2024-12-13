package pl.bartix.goldbrush;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class PickaxeListener implements Listener {

    private final JavaPlugin plugin;

    public PickaxeListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    //System niszczenia bloków
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (item.getType() != Material.BLAZE_ROD) return;

        String size = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, "brush_size"), PersistentDataType.STRING);
        if (size == null) return;

        int radius = Integer.parseInt(size.split("x")[0]) / 2;
        Block block = event.getBlock();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block relative = block.getRelative(x, y, z);
                    if (relative.getType() != Material.AIR && !relative.equals(block)) {
                        relative.breakNaturally(item);
                    }
                }
            }
        }
    }
            //Czy po śmierci mamy tracić brusha
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!plugin.getConfig().getBoolean("keep_brush_on_death")) {
            Player player = event.getEntity();
            List<ItemStack> itemsToRemove = new ArrayList<>();
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null && item.getType() == Material.BLAZE_ROD) {
                    if (item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "brush_size"), PersistentDataType.STRING)) {
                        itemsToRemove.add(item);
                    }
                }
            }
            player.getInventory().removeItem(itemsToRemove.toArray(new ItemStack[0]));
        }
    }
        //Brak możliwości wyrzucenie brusha z eq
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item != null && item.getType() == Material.BLAZE_ROD) {
            if (item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "brush_size"), PersistentDataType.STRING)) {
                if (event.getAction().toString().contains("DROP")) {
                    event.setCancelled(true);
                    Player player = (Player) event.getWhoClicked();
                    player.sendMessage("Nie możesz wyrzucić pędzla z ekwipunku!");
                }
            }
        }
    }
}
