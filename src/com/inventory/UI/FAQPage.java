package com.inventory.UI;

import com.inventory.DAO.FAQDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FAQPage extends JPanel {

    public FAQPage() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Часто задаваемые вопросы", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        JPanel faqListPanel = new JPanel();
        faqListPanel.setLayout(new BoxLayout(faqListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(faqListPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        loadFAQs(faqListPanel);
    }

    private void loadFAQs(JPanel panel) {
        try {
            FAQDAO dao = new FAQDAO();
            ResultSet rs = dao.getAllFAQs();

            while (rs.next()) {
                String question = rs.getString("question");
                String answer = rs.getString("answer");

                JPanel container = new JPanel();
                container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
                container.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                container.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel questionLabel = new JLabel("<html><b>" + question + "</b></html>");
                questionLabel.setFont(new Font("Arial", Font.PLAIN, 13));
                questionLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel answerLabel = new JLabel("<html>" + answer + "</html>");
                answerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                answerLabel.setVisible(false);
                answerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                questionLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        answerLabel.setVisible(!answerLabel.isVisible());
                        container.revalidate();
                        container.repaint();
                        panel.revalidate();
                        panel.repaint();
                    }
                });

                container.add(questionLabel);
                container.add(answerLabel);
                panel.add(container);
            }

            panel.revalidate();
            panel.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при загрузке FAQ.");
        }
    }
}
