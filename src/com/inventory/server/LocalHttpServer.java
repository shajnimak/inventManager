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

                        if (line.startsWith("GET /images/")) {
                            sendImage(line, out);
                        } else if (line.startsWith("GET /buy?product=")) {
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
        html.append("<html><head><title>Покупка товара</title>");
        html.append("<style>");
        html.append("body { font-family: Arial; background-color: #1e1e1e; color: #ddd; padding: 30px; }");
        html.append(".card { background: #2e2e2e; padding: 20px; margin: 10px; border-radius: 8px; display: flex; align-items: center; }");
        html.append(".card img { width: 64px; height: 64px; margin-right: 15px; }");
        html.append(".card a { padding: 8px 16px; background: #4caf50; color: white; text-decoration: none; border-radius: 4px; margin-left: auto; }");
        html.append("</style></head><body>");
        html.append("<h2>Выберите товар для покупки:</h2>");

        try {
            ProductDAO dao = new ProductDAO();
            ResultSet rs = dao.getProdInfo();
            while (rs.next()) {
                String code = rs.getString("productcode");
                String name = rs.getString("productname");
                String iconName = name.toLowerCase().replaceAll("\\s+", "") + ".png"; // пример: "Ноутбук Pro" → "ноутбукpro.png"

                html.append("<div class='card'>")
                        .append("<img src='/images/").append(iconName).append("' alt='icon'>")
                        .append("<div>").append(name).append("</div>")
                        .append("<a href='/buy?product=").append(code).append("'>Купить</a>")
                        .append("</div>");
            }
        } catch (Exception e) {
            html.append("<p style='color: red;'>Ошибка загрузки товаров.</p>");
        }

        html.append("</body></html>");
        writeHttpResponse(out, html.toString());
    }


    private static void handleBuyRequest(String line, OutputStream out) throws IOException {
        String productCode = line.split("product=")[1].split(" ")[0];

        // Выполняем реальную продажу
        sellRealProduct(productCode);

        String html = "<html><head><title>Покупка завершена</title>" +
                "<style>" +
                "body { background-color: #1e1e1e; color: #ddd; font-family: Arial; padding: 30px; }" +
                ".container { background: #2e2e2e; padding: 30px; border-radius: 10px; max-width: 600px; margin: auto; text-align: center; }" +
                "a.button { display: inline-block; margin-top: 20px; background: #4caf50; color: white; padding: 10px 20px; border-radius: 5px; text-decoration: none; font-weight: bold; }" +
                "</style>" +
                "<meta http-equiv='refresh' content='5;URL=/' />" +
                "</head><body>" +
                "<div class='container'>" +
                "<h2>✅ Покупка завершена!</h2>" +
                "<p>Товар с кодом <strong>" + productCode + "</strong> успешно продан.</p>" +
                "<p>Вы будете перенаправлены через 5 секунд...</p>" +
                "<a class='button' href='/'>Вернуться назад</a>" +
                "</div>" +
                "</body></html>";

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

    private static void sendImage(String line, OutputStream out) throws IOException {
        String fileName = line.split("GET /images/")[1].split(" ")[0];
        File imageFile = new File("src/com/inventory/UI/Icons/" + fileName); // путь к ресурсам

        if (!imageFile.exists()) {
            writeHttpResponse(out, "<html><body><h3>Изображение не найдено</h3></body></html>");
            return;
        }

        byte[] imageBytes = java.nio.file.Files.readAllBytes(imageFile.toPath());

        PrintWriter writer = new PrintWriter(out);
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: image/png");
        writer.println("Content-Length: " + imageBytes.length);
        writer.println();
        writer.flush();

        out.write(imageBytes);
        out.flush();
    }
}
