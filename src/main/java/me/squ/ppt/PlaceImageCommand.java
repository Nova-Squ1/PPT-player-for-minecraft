package me.squ.ppt;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;


import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PlaceImageCommand implements CommandExecutor, TabCompleter {


    private final PPT plugin;


    public PlaceImageCommand(PPT plugin) {
        this.plugin = plugin;
    }


    @Override

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("ppt.use")) {
            sender.sendMessage(ChatColor.RED + "你没有权限使用此命令。");
            return true;
        }


        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "配置已重载。");
            return true;
        }


        if (args.length == 1 && args[0].equalsIgnoreCase("clear")) {
            int removed = clearArea();
            sender.sendMessage(ChatColor.YELLOW + "已清理区域内物品展示框数量：" + removed);
            return true;
        }


        String fileName = (args.length >= 1) ? args[0] : plugin.cfg().getString("default-image", "example.png");
        File file = new File(plugin.getImagesFolder(), fileName);
        if (!file.exists()) {
            sender.sendMessage(ChatColor.RED + "图片不存在：" + file.getAbsolutePath());
            return true;
        }


        World world = Bukkit.getWorld(plugin.cfg().getString("world", "world"));
        if (world == null) {
            sender.sendMessage(ChatColor.RED + "世界不存在：" + plugin.cfg().getString("world", "world"));
            return true;
        }
        BlockFace face = BlockFace.valueOf(plugin.cfg().getString("face", "NORTH").toUpperCase());
        boolean clearFirst = plugin.cfg().getBoolean("clear-before-place", true);
        boolean visible = plugin.cfg().getBoolean("itemframe-visible", false);
        boolean fixed = plugin.cfg().getBoolean("itemframe-fixed", true);


        int x1 = plugin.cfg().getInt("from.x");
        int y1 = plugin.cfg().getInt("from.y");
        int z1 = plugin.cfg().getInt("from.z");
        int x2 = plugin.cfg().getInt("to.x");
        int y2 = plugin.cfg().getInt("to.y");
        int z2 = plugin.cfg().getInt("to.z");


        if (z1 != z2) {
            sender.sendMessage(ChatColor.RED + "当前实现仅支持 Z 固定的墙面（你的 Z: " + z1 + " vs " + z2 + ")。");
            return true;
        }
        // 计算网格尺寸
        int width = Math.abs(x1 - x2) + 1; // 预期 21
        int height = Math.abs(y1 - y2) + 1; // 预期 16


        try {
            if (clearFirst) clearArea();

            BufferedImage big = ImageTiler.loadAndScale(file, width, height);

            int startX = Math.max(x1, x2);
            int startY = Math.max(y1, y2);
            int endX = Math.min(x1, x2);
            int endY = Math.min(y1, y2);
            int z = z1;

            int placed = 0;

            for (int ty = 0; ty < height; ty++) {
                int y = startY - ty;
                for (int tx = 0; tx < width; tx++) {
                    int x = startX - tx;

                    BufferedImage tile = ImageTiler.tile(big, tx, ty);
                    // 1) 创建 MapView
                    MapView view = Bukkit.createMap(world);
                    view.getRenderers().forEach(view::removeRenderer);
                    view.setTrackingPosition(false);
                    view.setScale(MapView.Scale.FARTHEST); // 缩放无关紧要，这里关闭跟踪即可
                    view.addRenderer(new TileRenderer(tile));

                    // 2) 地图物品
                    ItemStack mapItem = new ItemStack(Material.FILLED_MAP, 1);
                    MapMeta meta = (MapMeta) mapItem.getItemMeta();
                    meta.setMapView(view);
                    mapItem.setItemMeta(meta);

                    // 3) 在指定方块的指定朝向挂一个物品展示框
                    Block wallBlock = world.getBlockAt(x, y, z);
                    Location spawnLoc = wallBlock.getLocation().add(0.5, 0.5, 0.5);

                    // 依赖朝向将展示框吸附到对应表面（NORTH 表面朝向 -Z）
                    ItemFrame frame = world.spawn(spawnLoc, ItemFrame.class, ent -> {
                        ent.setFacingDirection(face, true);
                        ent.setVisible(visible);
                        ent.setFixed(fixed);
                    });

                    frame.setItem(mapItem, false);
                    placed++;
                }
            }

            if (sender instanceof Player p) {
                p.playSound(p.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1f, 1.2f);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            sender.sendMessage(ChatColor.RED + "放置失败：" + ex.getMessage());
        }


        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) {
            File[] files = plugin.getImagesFolder().listFiles((dir, name) ->
                    name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg"));
            if (files == null) return Collections.emptyList();

            return Arrays.stream(files)
                    .map(File::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private int clearArea() {
        World world = Bukkit.getWorld(plugin.cfg().getString("world", "world"));
        if (world == null) return 0;
        int x1 = plugin.cfg().getInt("from.x");
        int y1 = plugin.cfg().getInt("from.y");
        int z1 = plugin.cfg().getInt("from.z");
        int x2 = plugin.cfg().getInt("to.x");
        int y2 = plugin.cfg().getInt("to.y");
        int z2 = plugin.cfg().getInt("to.z");


        int minX = Math.min(x1, x2), maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2), maxY = Math.max(y1, y2);
        int minZ = Math.min(z1, z2), maxZ = Math.max(z1, z2);


        int removed = 0;
        for (ItemFrame frame : world.getEntitiesByClass(ItemFrame.class)) {
            Location l = frame.getLocation();
            int fx = l.getBlockX();
            int fy = l.getBlockY();
            int fz = l.getBlockZ();
            if (fx >= minX && fx <= maxX && fy >= minY && fy <= maxY && fz >= minZ && fz <= maxZ) {
                frame.remove();
                removed++;
            }
        }
        return removed;
    }

}
