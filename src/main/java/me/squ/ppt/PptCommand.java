package me.squ.ppt;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PptCommand implements CommandExecutor, TabCompleter {


    private final PPT plugin;


    public PptCommand(PPT plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("ppt.use")) {
            sender.sendMessage(ChatColor.RED + "你没有权限使用此命令。");
            return true;
        }


        if (args.length != 1) {
            sender.sendMessage(ChatColor.YELLOW + "用法: /ppt <begin|up|down>");
            return true;
        }


        String action = args[0].toLowerCase();
        int current = plugin.getCurrentSlide();


        if (action.equals("begin")) {
            if (showSlide(1, sender)) {
                plugin.setCurrentSlide(1);
            }
            return true;
        }


        if (action.equals("up")) {
            int prev = current - 1;
            if (prev >= 1 && showSlide(prev, sender)) {
                plugin.setCurrentSlide(prev);
            }
            return true;
        }


        if (action.equals("down")) {
            int next = current + 1;
            if (showSlide(next, sender)) {
                plugin.setCurrentSlide(next);
            }
            return true;
        }
        if (action.matches("\\d+")) {
            int index = Integer.parseInt(action);
            if (index >= 1 && showSlide(index, sender)) {
                plugin.setCurrentSlide(index);
            }
            return true;
        }

        if (action.equals("host")) {
            if (sender instanceof org.bukkit.entity.Player player) {
                org.bukkit.inventory.ItemStack begin = new org.bukkit.inventory.ItemStack(org.bukkit.Material.NETHER_STAR);
                org.bukkit.inventory.meta.ItemMeta m1 = begin.getItemMeta();
                m1.setDisplayName(ChatColor.GREEN + "[PPT Begin]");
                begin.setItemMeta(m1);


                org.bukkit.inventory.ItemStack up = new org.bukkit.inventory.ItemStack(org.bukkit.Material.ARROW);
                org.bukkit.inventory.meta.ItemMeta m2 = up.getItemMeta();
                m2.setDisplayName(ChatColor.YELLOW + "[PPT Up]");
                up.setItemMeta(m2);


                org.bukkit.inventory.ItemStack down = new org.bukkit.inventory.ItemStack(org.bukkit.Material.BLAZE_ROD);
                org.bukkit.inventory.meta.ItemMeta m3 = down.getItemMeta();
                m3.setDisplayName(ChatColor.RED + "[PPT Down]");
                down.setItemMeta(m3);


                player.getInventory().addItem(begin, up, down);
                player.sendMessage(ChatColor.AQUA + "已获得控制物品：Begin/Up/Down。");
            } else {
                sender.sendMessage(ChatColor.RED + "该指令仅玩家可用。");
            }
            return true;
        }


        sender.sendMessage(ChatColor.RED + "未知参数: " + action);
        return true;
    }
    private boolean showSlide(int index, CommandSender sender) {
        File file = new File(plugin.getImagesFolder(), index + ".png");
        if (!file.exists()) {
            sender.sendMessage(ChatColor.RED + "没有找到图片: " + index + ".png");
            return false;
        }


// 复用 /placeimage 的逻辑
        String[] fakeArgs = {index + ".png"};
        new PlaceImageCommand(plugin).onCommand(sender, null, "placeimage", fakeArgs);
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> options = new ArrayList<>();
            options.add("begin");
            options.add("up");
            options.add("down");
            options.add("host");

            return options.stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
