package interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagenPanel extends JPanel {
    private BufferedImage mapa;

    public ImagenPanel(BufferedImage mapa){
     this.mapa = mapa;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        if (mapa == null) return;
        // Calculate dimensions for drawing
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int imgWidth = mapa.getWidth();
        int imgHeight = mapa.getHeight();

        // Calculate scaling while preserving aspect ratio
        double scale = 1.0;
        int drawWidth = imgWidth;
        int drawHeight = imgHeight;
        int drawX = (panelWidth - imgWidth) / 2;
        int drawY = (panelHeight - imgHeight) / 2;

        double widthRatio = (double) panelWidth / imgWidth;
        double heightRatio = (double) panelHeight / imgHeight;
        scale = Math.min(widthRatio, heightRatio) * 0.9; // 90% of available space

        drawWidth = (int) (imgWidth * scale);
        drawHeight = (int) (imgHeight * scale);
        drawX = (panelWidth - drawWidth) / 2;
        drawY = (panelHeight - drawHeight) / 2;

        // Draw the image
        g2d.drawImage(mapa, drawX, drawY, drawWidth, drawHeight, this);
    }
}
