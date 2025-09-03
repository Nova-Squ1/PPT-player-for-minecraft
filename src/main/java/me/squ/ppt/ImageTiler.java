package me.squ.ppt;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ImageTiler {
    public static final int TILE_SIZE = 128;

    public static BufferedImage loadAndScale(File file, int widthTiles, int heightTiles) throws IOException {
        int targetW = widthTiles * TILE_SIZE;
        int targetH = heightTiles * TILE_SIZE;
        BufferedImage src = ImageIO.read(file);
        if (src == null) throw new IOException("Unsupported or unreadable image: " + file);
        BufferedImage dst = new BufferedImage(targetW, targetH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dst.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(src, 0, 0, targetW, targetH, null);
        g.dispose();
        return dst;
    }


    public static BufferedImage tile(BufferedImage big, int tx, int ty) {
// tx:[0..20], ty:[0..15]
        int x = tx * TILE_SIZE;
        int y = ty * TILE_SIZE;
        return big.getSubimage(x, y, TILE_SIZE, TILE_SIZE);
    }
}
