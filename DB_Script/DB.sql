-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               11.1.2-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.3.0.6589
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping structure for table productmanagement.orders
CREATE TABLE IF NOT EXISTS `orders` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ORDER_DATE` datetime NOT NULL,
  `CUSTOMER_NAME` varchar(255) NOT NULL,
  `ADDRESS` text NOT NULL,
  `EMAIL` varchar(255) NOT NULL,
  `PHONE_NUMBER` varchar(20) NOT NULL,
  `STATUS` varchar(50) NOT NULL,
  `TOTAL_AMOUNT` decimal(20,6) NOT NULL DEFAULT 0.000000,
  `QUANTITY` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table productmanagement.orders: ~18 rows (approximately)
INSERT INTO `orders` (`ID`, `ORDER_DATE`, `CUSTOMER_NAME`, `ADDRESS`, `EMAIL`, `PHONE_NUMBER`, `STATUS`, `TOTAL_AMOUNT`, `QUANTITY`) VALUES
	(1, '2024-04-01 00:00:00', 'John Doe', '123 Main St, Anytown, USA', 'john.doe@example.com', '123-456-7890', '1', 2199.970000, 1),
	(2, '2024-04-05 00:00:00', 'Jane Smith', '456 Elm St, Anytown, USA', 'jane.smith@example.com', '987-654-3210', '1', 1349.980000, 2),
	(3, '2024-06-01 00:00:00', 'Johfn Doe', '123 Main St, Anytofwn, USA', 'jofhn.doe@exfample.com', '0234567890', '1', 599.980000, 2),
	(4, '2024-04-15 00:00:00', 'Alice Brown', '321 Cedar St, Anytown, USA', 'alice.brown@example.com', '789-012-3456', '1', 1649.970000, 3),
	(5, '2024-04-20 00:00:00', 'Michael Wilson', '654 Pine St, Anytown, USA', 'michael.wilson@example.com', '012-345-6789', '1', 2549.950000, 4),
	(6, '2024-06-01 00:00:00', 'John Doe', '123 Main St, Anytown, USA', 'jofhn.doe@example.com', '0234567890', '1', 3399.970000, 3),
	(7, '2024-06-01 00:00:00', 'John Doe', '123 Main St, Anytown, USA', 'jofhn.doe@example.com', '0234567890', '1', 3399.970000, 3),
	(8, '2024-06-01 00:00:00', 'John Doe', '123 Main St, Anytown, USA', 'jofhn.doe@example.com', '0234567890', '1', 3399.970000, 3),
	(9, '2024-06-01 00:00:00', 'John Doe', '123 Main St, Anytown, USA', 'jofhn.doe@example.com', '0234567890', '1', 3399.970000, 3),
	(10, '2024-06-01 00:00:00', 'John Doe', '123 Main St, Anytown, USA', 'jofhn.doe@example.com', '0234567890', '1', 3399.970000, 3),
	(11, '2024-06-01 00:00:00', 'John Doe', '123 Main St, Anytown, USA', 'jofhn.doe@example.com', '0234567890', '1', 3399.970000, 3),
	(12, '2024-06-01 00:00:00', 'John Doe', '123 Main St, Anytown, USA', 'jofhn.doe@example.com', '0234567890', '1', 3399.970000, 3),
	(13, '2024-06-01 00:00:00', 'John Doe', '123 Main St, Anytown, USA', 'jofhn.doe@example.com', '0234567890', '1', 3399.970000, 3),
	(14, '2024-06-01 00:00:00', 'John Doe', '123 Main St, Anytown, USA', 'jofhn.doe@example.com', '0234567890', '1', 3399.970000, 3),
	(15, '2024-06-01 00:00:00', 'John Doe', '123 Main St, Anytown, USA', 'jofhn.doe@example.com', '0234567890', '1', 3399.970000, 3),
	(16, '2024-06-01 00:00:00', 'John Doe', '123 Main St, Anytown, USA', 'jofhn.doe@example.com', '0234567890', '1', 3399.970000, 3),
	(17, '2024-04-21 16:37:15', 'John Doe', '123 Main St', 'john.doe@example.com', '0123456789', '1', 2999.980000, 2),
	(18, '2024-04-21 22:04:48', 'John Doe', '123 Main St', 'john.doe@example.com', '0123456789', '1', 2999.980000, NULL);

-- Dumping structure for table productmanagement.order_detail
CREATE TABLE IF NOT EXISTS `order_detail` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ORDER_ID` int(11) DEFAULT NULL,
  `PRODUCT_ID` int(11) DEFAULT NULL,
  `QUANTITY` int(11) NOT NULL,
  `UNIT_PRICE` decimal(20,6) NOT NULL DEFAULT 0.000000,
  `STATUS` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `OrderID` (`ORDER_ID`) USING BTREE,
  KEY `ProductID` (`PRODUCT_ID`) USING BTREE,
  CONSTRAINT `order_detail_ibfk_1` FOREIGN KEY (`ORDER_ID`) REFERENCES `orders` (`ID`),
  CONSTRAINT `order_detail_ibfk_2` FOREIGN KEY (`PRODUCT_ID`) REFERENCES `product` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table productmanagement.order_detail: ~6 rows (approximately)
INSERT INTO `order_detail` (`ID`, `ORDER_ID`, `PRODUCT_ID`, `QUANTITY`, `UNIT_PRICE`, `STATUS`) VALUES
	(1, 1, 1, 1, 1499.990000, '1'),
	(2, 1, 4, 2, 349.990000, '1'),
	(3, 2, 3, 1, 1499.000000, '1'),
	(4, 3, 5, 2, 299.990000, '-1'),
	(5, 4, 2, 1, 999.990000, '1'),
	(6, 3, 1, 2, 1499.990000, '-1');

-- Dumping structure for table productmanagement.product
CREATE TABLE IF NOT EXISTS `product` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PRODUCT_NAME` varchar(255) NOT NULL,
  `DESCRIPTION` text DEFAULT NULL,
  `PRICE` decimal(20,6) NOT NULL DEFAULT 0.000000,
  `STOCK_QUANTITY` int(11) NOT NULL,
  `STATUS` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table productmanagement.product: ~7 rows (approximately)
INSERT INTO `product` (`ID`, `PRODUCT_NAME`, `DESCRIPTION`, `PRICE`, `STOCK_QUANTITY`, `STATUS`) VALUES
	(1, 'Laptop Dell XPS 13', 'Powerful and sleek laptop with Intel Core i7 processor', 1499.990000, 10, '1'),
	(2, 'iPhone 13 Pro', 'Latest smartphone from Apple with ProMotion display', 999.990000, 20, '1'),
	(3, 'Samsung 4K Smart TV', '55-inch QLED TV with stunning picture quality', 1499.000000, 5, '1'),
	(4, 'Sony WH-1000XM4', 'Wireless noise-canceling headphones with industry-leading sound quality', 349.990000, 15, '1'),
	(5, 'Samsung 8K Smart TV', '75-inch QLED TV with stunning picture quality', 5499.000000, 10, '1'),
	(6, 'Samsung 8K Smart TV', '65-inch QLED TV with stunning picture quality', 2499.000000, 3, '-1'),
	(7, 'Samsung 8K Smart TV', '65-inch QLED TV with stunning picture quality', 2499.000000, 3, '1'),
	(9, 'Laptop Dell XPS 14', 'Powerful and sleek laptop with Intel Core i7 processor', 149.990000, 3, '1');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
