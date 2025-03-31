-- Создание базы данных, если не существует
CREATE DATABASE inventory;

\c inventory;

-- Таблица currentstock
DROP TABLE IF EXISTS currentstock;
CREATE TABLE currentstock (
                              productcode VARCHAR(45) PRIMARY KEY,
                              quantity INT NOT NULL
);

-- Вставка данных в таблицу currentstock
INSERT INTO currentstock (productcode, quantity)
VALUES
    ('prod1', 146),
    ('prod2', 100),
    ('prod3', 202),
    ('prod4', 172),
    ('prod5', 500),
    ('prod6', 500),
    ('prod7', 10),
    ('prod8', 20);

-- Таблица customers
DROP TABLE IF EXISTS customers;
CREATE TABLE customers (
                           cid SERIAL PRIMARY KEY,
                           customercode VARCHAR(45) NOT NULL,
                           fullname VARCHAR(45) NOT NULL,
                           location VARCHAR(45) NOT NULL,
                           phone VARCHAR(45) NOT NULL
);

-- Вставка данных в таблицу customers
INSERT INTO customers (customercode, fullname, location, phone)
VALUES
    ('vip1', 'John Seed', 'New York', '9818562354'),
    ('vip2', 'Jacob Seed', 'Texas', '9650245489'),
    ('std1', 'Ajay Kumar', 'Mumbai', '9236215622'),
    ('std2', 'Astha Walia', 'Chandigarh', '8854612478'),
    ('vip3', 'Madhu Chitkara', 'Chandigarh', '9826546182');

-- Таблица products
DROP TABLE IF EXISTS products;
CREATE TABLE products (
                          pid SERIAL PRIMARY KEY,
                          productcode VARCHAR(45) UNIQUE NOT NULL,
                          productname VARCHAR(45) NOT NULL,
                          costprice DOUBLE PRECISION NOT NULL,
                          sellprice DOUBLE PRECISION NOT NULL,
                          brand VARCHAR(45) NOT NULL
);

-- Вставка данных в таблицу products
INSERT INTO products (productcode, productname, costprice, sellprice, brand)
VALUES
    ('prod1', 'Laptop', 85000, 90000, 'Dell'),
    ('prod2', 'Laptop', 70000, 72000, 'HP'),
    ('prod3', 'Mobile', 60000, 64000, 'Apple'),
    ('prod4', 'Mobile', 50000, 51000, 'Samsung'),
    ('prod5', 'Charger', 2000, 2100, 'Apple'),
    ('prod6', 'Mouse', 1700, 1900, 'Dell'),
    ('prod7', 'Power Adapter', 3000, 3500, 'Dell'),
    ('prod8', 'Smart Watch', 15000, 17000, 'Apple');

-- Таблица purchaseinfo
DROP TABLE IF EXISTS purchaseinfo;
CREATE TABLE purchaseinfo (
                              purchaseID SERIAL PRIMARY KEY,
                              suppliercode VARCHAR(45) NOT NULL,
                              productcode VARCHAR(45) NOT NULL,
                              date VARCHAR(45) NOT NULL,
                              quantity INT NOT NULL,
                              totalcost DOUBLE PRECISION NOT NULL
);

-- Вставка данных в таблицу purchaseinfo
INSERT INTO purchaseinfo (suppliercode, productcode, date, quantity, totalcost)
VALUES
    ('sup1', 'prod1', 'Wed Jan 14 00:15:19 IST 2021', 10, 850000),
    ('sup1', 'prod6', 'Wed Jan 14 00:15:19 IST 2021', 20, 34000),
    ('sup2', 'prod3', 'Wed Jan 14 00:15:19 IST 2021', 5, 300000),
    ('sup2', 'prod5', 'Wed Jan 14 00:15:19 IST 2021', 5, 10000),
    ('sup3', 'prod2', 'Wed Jan 14 00:15:19 IST 2021', 2, 140000),
    ('sup4', 'prod4', 'Wed Jan 14 00:15:19 IST 2021', 2, 100000),
    ('sup2', 'prod3', 'Wed Sep 01 04:11:13 IST 2021', 2, 120000),
    ('sup1', 'prod7', 'Wed Sep 01 04:25:06 IST 2021', 10, 30000),
    ('sup2', 'prod8', 'Fri Sep 03 00:00:00 IST 2021', 20, 300000);

-- Таблица salesinfo
DROP TABLE IF EXISTS salesinfo;
CREATE TABLE salesinfo (
                           salesid SERIAL PRIMARY KEY,
                           date VARCHAR(45) NOT NULL,
                           productcode VARCHAR(45) NOT NULL,
                           customercode VARCHAR(45) NOT NULL,
                           quantity INT NOT NULL,
                           revenue DOUBLE PRECISION NOT NULL,
                           soldby VARCHAR(45) NOT NULL
);

-- Вставка данных в таблицу salesinfo
INSERT INTO salesinfo (date, productcode, customercode, quantity, revenue, soldby)
VALUES
    ('Fri Jan 16 23:12:40 IST 2021', 'prod1', 'vip1', 3, 270000, 'stduser1'),
    ('Fri Jan 16 23:12:40 IST 2021', 'prod2', 'vip2', 2, 144000, 'stduser1'),
    ('Fri Jan 16 23:12:40 IST 2021', 'prod3', 'std1', 1, 64000, 'aduser1'),
    ('Fri Jan 16 23:12:40 IST 2021', 'prod4', 'std2', 5, 255000, 'aduser1'),
    ('Thu Aug 05 17:29:36 IST 2021', 'prod1', 'vip1', 2, 180000, 'root'),
    ('Fri Aug 06 00:00:00 IST 2021', 'prod4', 'std1', 1, 51000, 'aduser1'),
    ('Fri Aug 06 02:41:28 IST 2021', 'prod7', 'std1', 1, 3500, 'aduser1'),
    ('Sat Aug 07 00:00:00 IST 2021', 'prod7', 'std1', 5, 17500, 'aduser1'),
    ('Thu Aug 12 00:00:00 IST 2021', 'prod4', 'vip3', 2, 102000, 'root'),
    ('Sun Aug 15 23:08:51 IST 2021', 'prod7', 'vip2', 10, 35000, 'root'),
    ('Thu Aug 26 15:17:48 IST 2021', 'prod4', 'vip3', 5, 255000, 'aduser1');

-- Таблица suppliers
DROP TABLE IF EXISTS suppliers;
CREATE TABLE suppliers (
                           sid SERIAL PRIMARY KEY,
                           suppliercode VARCHAR(45) NOT NULL,
                           fullname VARCHAR(45) NOT NULL,
                           location VARCHAR(45) NOT NULL,
                           mobile VARCHAR(10) NOT NULL
);

-- Вставка данных в таблицу suppliers
INSERT INTO suppliers (suppliercode, fullname, location, mobile)
VALUES
    ('sup1', 'Dell Inc.', 'Gurugram', '1800560001'),
    ('sup2', 'iWorld Stores', 'New Delhi', '1800560041'),
    ('sup3', 'Samsung Appliances', 'New Delhi', '6546521234'),
    ('sup4', 'Hewlett-Packard', 'Mumbai', '8555202215'),
    ('sup5', 'Hewlett-Packard Ltd.', 'Mumbai', '8555203300'),
    ('sup6', 'Shelby Company Ltd.', 'Birmingham', '9696969696');

-- Таблица userlogs
DROP TABLE IF EXISTS userlogs;
CREATE TABLE userlogs (
                          username VARCHAR(45) NOT NULL,
                          in_time VARCHAR(45) NOT NULL,
                          out_time VARCHAR(45) NOT NULL
);

-- Блокировка таблиц в PostgreSQL
BEGIN;

-- Вставка данных в таблицу userlogs
INSERT INTO userlogs (username, start_time, end_time)
VALUES
    ('aduser1', '2021-09-01T04:46:55.125709800', '2021-09-01T04:47:01.801381'),
    ('root', '2021-09-01T05:02:43.010014', '2021-09-01T05:02:50.224787400'),
    ('stduser1', '2021-09-01T05:04:57.690182100', '2021-09-01T05:05:04.294274300'),
    ('root', '2021-09-01T05:05:12.269897600', '2021-09-01T05:05:16.866792500'),
    ('root', '2021-09-01T05:10:08.728527600', '2021-09-01T05:10:16.926883100'),
    ('root', '2021-09-01T06:19:09.326477200', '2021-09-01T06:19:21.641620900'),
    ('emp1', '2021-09-01T06:19:34.536411800', '2021-09-01T06:19:43.517392100'),
    ('root', '2021-09-01T06:19:46.811400900', '2021-09-01T06:20:10.879660700'),
    ('root', '2021-09-01T14:59:48.661066400', '2021-09-01T15:02:09.761864900'),
    ('root', '2021-09-01T15:09:02.964317400', '2021-09-01T15:09:14.141324800'),
    ('root', '2021-09-01T15:09:27.889908500', '2021-09-01T15:09:48.262387'),
    ('root', '2021-09-01T15:38:48.557639300', '2021-09-01T15:40:00.527183800'),
    ('root', '2021-09-01T15:40:22.622326', '2021-09-01T15:41:06.461438500'),
    ('root', '2021-09-01T15:44:26.195028100', '2021-09-01T15:44:33.071448800'),
    ('root', '2021-09-02T01:42:52.417989900', '2021-09-02T01:42:55.226675900'),
    ('root', '2021-09-02T01:43:12.226339400', '2021-09-02T01:43:15.818776'),
    ('aduser1', '2021-09-03T02:12:41.021781900', '2021-09-03T02:19:11.829873500');

-- Вставка данных в таблицу users
INSERT INTO users (id, name, location, phone, username, password, usertype)
VALUES
    (17, 'Asjad Iqbal', 'Chandigarh', '9650786717', 'aduser1', 'dbadpass', 'ADMINISTRATOR'),
    (18, 'Ahan Jaiswal', 'Delhi', '9660654785', 'stduser1', 'dbstdpass', 'EMPLOYEE'),
    (20, 'Trial Admin', 'Local', '9876543210', 'root', 'root', 'ADMINISTRATOR'),
    (29, 'Trial Employee', 'Local', '1122334455', 'emp1', 'emp1', 'EMPLOYEE');

CREATE TABLE faq (
                     id SERIAL PRIMARY KEY,
                     question TEXT NOT NULL,
                     answer TEXT NOT NULL
);

-- Завершение транзакции
COMMIT;
