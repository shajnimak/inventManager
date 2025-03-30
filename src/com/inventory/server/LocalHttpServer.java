package com.inventory.server;

import com.inventory.DAO.CustomerDAO;
import com.inventory.DAO.ProductDAO;
import com.inventory.DTO.CustomerDTO;
import com.inventory.DTO.ProductDTO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;

public class LocalHttpServer {
    public static void startServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(1234)) {
                System.out.println("Локальный сервер запущен на http://localhost:1234");

                while (true) {
                    try (Socket client = serverSocket.accept()) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        OutputStream out = client.getOutputStream();

                        // Читаем первую строку запроса
                        String line = in.readLine();
                        if (line == null) continue;

                        if (line.startsWith("GET /buy?product=")) {
                            handleBuyRequest(line, out);
                        } else {
                            serveProductSelection(out);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void sellRealProduct(String productCode) {
        try {
            ProductDAO productDAO = new ProductDAO();
            CustomerDAO customerDAO = new CustomerDAO();

            // Проверка цены
            Double price = productDAO.getProdSell(productCode);
            if (price == null) {
                System.out.println("Товар с кодом " + productCode + " не найден.");
                return;
            }

            // Используем существующего клиента
            String custCode = "24";

            // Продажа
            ProductDTO dto = new ProductDTO();
            dto.setProdCode(productCode);
            dto.setCustCode(custCode);
            dto.setQuantity(1);
            dto.setDate(java.time.LocalDate.now().toString());
            dto.setTotalRevenue(price);
            dto.setCustName("QR");

            productDAO.sellProductDAO(dto, "qruser");

            System.out.println("✔ Покупка товара " + productCode + " выполнена успешно.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static void serveProductSelection(OutputStream out) throws IOException {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Покупка товара</title></head><body>");
        html.append("<h2>Выберите товар для покупки:</h2>");
        html.append("<ul>");

        try {
            ProductDAO dao = new ProductDAO();
            ResultSet rs = dao.getProdInfo();
            while (rs.next()) {
                String code = rs.getString("productcode");
                String name = rs.getString("productname");
                html.append("<li>")
                        .append(name)
                        .append(" — <a href='/buy?product=")
                        .append(code)
                        .append("'>Купить</a></li>");
            }
        } catch (Exception e) {
            html.append("<li>Ошибка загрузки товаров.</li>");
        }

        html.append("</ul></body></html>");

        writeHttpResponse(out, html.toString());
    }

    private static void handleBuyRequest(String line, OutputStream out) throws IOException {
        String productCode = line.split("product=")[1].split(" ")[0];

        // Выполняем реальную продажу
        sellRealProduct(productCode);

        String html = "<html><body><h2>Покупка завершена!</h2><p>Товар " + productCode + " успешно продан.</p><br><a href='/'>Вернуться назад</a></body></html>";
        writeHttpResponse(out, html);
    }


    private static void writeHttpResponse(OutputStream out, String html) throws IOException {
        PrintWriter writer = new PrintWriter(out);
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: text/html; charset=UTF-8");
        writer.println("Content-Length: " + html.getBytes().length);
        writer.println();
        writer.print(html);
        writer.flush();
    }
}
