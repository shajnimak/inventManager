package com.inventory.UI;

import com.inventory.DAO.ProductDAO;
import com.inventory.DAO.UserDAO;
import com.inventory.DTO.UserDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class HomePage extends JPanel {

    private JLabel welcomeLabel;
    private JLabel incomeLabel;
    private JLabel expenseLabel;
    private JTable topProductsTable;

    public HomePage(String username) {
        initComponents();
        UserDTO userDTO = new UserDTO();
        new UserDAO().getFullName(userDTO, username);
        welcomeLabel.setText("Welcome, " + userDTO.getFullName());

        displayIncomeAndExpense();
        loadTopSellingProducts();
    }

    private void initComponents() {
        // Layout
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Верхняя панель
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        headerPanel.setOpaque(false);

        welcomeLabel = new JLabel("Welcome");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(33, 33, 33));

        JLabel subtitle = new JLabel("Your business overview");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(100, 100, 100));

        headerPanel.add(welcomeLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitle);

        // Аналитическая панель
        JPanel analyticsPanel = new JPanel();
        analyticsPanel.setLayout(new GridLayout(1, 2, 20, 0));
        analyticsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        analyticsPanel.setOpaque(false);

        incomeLabel = createInfoLabel("Total Income: $0");
        expenseLabel = createInfoLabel("Total Expense: $0");

        analyticsPanel.add(incomeLabel);
        analyticsPanel.add(expenseLabel);

        // Панель таблицы
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        tablePanel.setOpaque(false);

        JLabel tableTitle = new JLabel("Top 5 Selling Products");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(new Color(60, 60, 60));
        tableTitle.setBorder(new EmptyBorder(0, 0, 10, 0));

        topProductsTable = new JTable();
        styleTable(topProductsTable);

        JScrollPane scrollPane = new JScrollPane(topProductsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Добавление компонентов
        add(headerPanel, BorderLayout.NORTH);
        add(analyticsPanel, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.SOUTH);
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        label.setForeground(new Color(34, 139, 34));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    private void displayIncomeAndExpense() {
        ProductDAO dao = new ProductDAO();
        double totalIncome = 0;
        double totalExpense = 0;

        try {
            ResultSet sales = dao.getSalesInfo();
            while (sales.next()) {
                totalIncome += sales.getDouble("revenue");
            }

            ResultSet purchases = dao.getPurchaseInfo();
            while (purchases.next()) {
                totalExpense += purchases.getDouble("totalcost");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        incomeLabel.setText("Total Income: $" + totalIncome);
        expenseLabel.setText("Total Expense: $" + totalExpense);
    }

    private void loadTopSellingProducts() {
        ProductDAO dao = new ProductDAO();
        Map<String, Integer> productSales = new HashMap<>();

        try {
            ResultSet sales = dao.getSalesInfo();
            while (sales.next()) {
                String productName = sales.getString("productname");
                int quantity = sales.getInt("quantity");
                productSales.put(productName, productSales.getOrDefault(productName, 0) + quantity);
            }

            Object[][] topData = productSales.entrySet().stream()
                    .sorted((a, b) -> b.getValue() - a.getValue())
                    .limit(5)
                    .map(e -> new Object[]{e.getKey(), e.getValue()})
                    .toArray(Object[][]::new);

            String[] columnNames = {"Product", "Quantity Sold"};
            DefaultTableModel model = new DefaultTableModel(topData, columnNames);
            topProductsTable.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void styleTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        table.setShowGrid(false);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(220, 230, 241));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(220, 220, 220));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setForeground(Color.DARK_GRAY);
        header.setBackground(new Color(230, 230, 230));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.LEFT);
        table.setDefaultRenderer(Object.class, centerRenderer);
    }
}
