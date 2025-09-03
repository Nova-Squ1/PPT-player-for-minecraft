package me.squ.ppt;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class PptToolListener implements Listener {


    private final PPT plugin;


    public PptToolListener(PPT plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        ItemStack item = e.getItem();
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        String name = ChatColor.stripColor(meta.getDisplayName());
        Player player = e.getPlayer();


        switch (name) {
            case "[PPT Begin]":
                player.performCommand("ppt begin");
                break;
            case "[PPT Up]":
                player.performCommand("ppt up");
                break;
            case "[PPT Down]":
                player.performCommand("ppt down");
                break;
            default:
                return;
        }


        e.setCancelled(true); // 防止原始交互动作
    }
}
