// QRCodePage.java
package com.inventory.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class QRCodePage extends JPanel {
    public QRCodePage() {
        setLayout(new BorderLayout());

        String url = "http://localhost:1234";
        JLabel label = new JLabel("Сканируйте QR-код для покупки товара", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        add(label, BorderLayout.NORTH);

        try {
            BufferedImage qrImage = generateDummyQRCode(url);
            JLabel imageLabel = new JLabel(new ImageIcon(qrImage));
            add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Ошибка генерации QR-кода", SwingConstants.CENTER);
            add(errorLabel, BorderLayout.CENTER);
        }
    }

    private BufferedImage generateDummyQRCode(String text) {
        int width = 300;
        int height = 300;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);

        g2.setColor(Color.BLACK);
        g2.drawRect(0, 0, width - 1, height - 1);

        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        Rectangle2D rect = g2.getFontMetrics().getStringBounds(text, g2);
        int centerX = (width - (int) rect.getWidth()) / 2;
        int centerY = height / 2;
        g2.drawString(text, centerX, centerY);

        g2.dispose();
        return image;
    }
}
