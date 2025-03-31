package com.inventory.UI;

import javax.swing.*;
import java.awt.*;

public class QRCodePage extends JPanel {

    public QRCodePage() {
        setLayout(new BorderLayout());

        // Верхний заголовок
        JLabel label = new JLabel("Сканируйте QR-код для покупки товара", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.LIGHT_GRAY);
        add(label, BorderLayout.NORTH);

        // Загрузка QR-картинки из ресурсов
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/com/inventory/UI/Icons/qr.png"));
            JLabel qrLabel = new JLabel(icon);
            qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(qrLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Ошибка загрузки QR-кода", SwingConstants.CENTER);
            errorLabel.setForeground(Color.RED);
            add(errorLabel, BorderLayout.CENTER);
        }
    }
}
