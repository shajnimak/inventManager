package com.inventory.UI;

import com.inventory.DAO.ProductDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class LowStockPage extends JPanel {

    private JTable lowStockTable;

    public LowStockPage() {
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Low Stock Products");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

        add(titleLabel, BorderLayout.NORTH);

        lowStockTable = new JTable();
        styleTable(lowStockTable);

        loadLowStockData();

        JScrollPane scrollPane = new JScrollPane(lowStockTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70)));

        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadLowStockData() {
        ProductDAO dao = new ProductDAO();
        int threshold = 5;

        try {
            ResultSet rs = dao.getLowStockProducts(threshold);

            Vector<String> columnNames = new Vector<>();
            columnNames.add("Product");
            columnNames.add("Quantity");
            columnNames.add("Brand");
            columnNames.add("Supplier");

            Vector<Vector<Object>> data = new Vector<>();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("productname"));
                row.add(rs.getInt("quantity"));
                row.add(rs.getString("brand"));
                row.add(rs.getString("supplier") != null ? rs.getString("supplier") : "N/A");
                data.add(row);
            }

            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            lowStockTable.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void styleTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setForeground(Color.LIGHT_GRAY);
        table.setBackground(new Color(40, 40, 40));
        table.setGridColor(new Color(80, 80, 80));
        table.setSelectionBackground(new Color(60, 60, 60));
        table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setForeground(Color.BLACK);
        header.setBackground(new Color(200, 200, 200));
    }
}
