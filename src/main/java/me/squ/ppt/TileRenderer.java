package me.squ.ppt;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;

public class TileRenderer extends MapRenderer {
    private final BufferedImage tile;
    private boolean rendered = false;

    public TileRenderer(BufferedImage tile) {
        super(true); // true 表示移除其他渲染器
        this.tile = tile;
    }

    @Override
    public void render(MapView view, MapCanvas canvas, Player player) {
        if (rendered) return;
        canvas.drawImage(0, 0, tile);
        rendered = true; // 只绘制一次，提高效率
    }
}