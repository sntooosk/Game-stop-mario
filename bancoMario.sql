-- --------------------------------------------------------
-- Servidor:                     localhost
-- Versão do servidor:           5.5.20 - MySQL Community Server (GPL)
-- OS do Servidor:               Win32
-- HeidiSQL Versão:              12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Copiando estrutura do banco de dados para db_stopmario
CREATE DATABASE IF NOT EXISTS `db_stopmario` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `db_stopmario`;

-- Copiando estrutura para tabela db_stopmario.tb01_partida
CREATE TABLE IF NOT EXISTS `tb01_partida` (
  `tb01_id` int(11) NOT NULL AUTO_INCREMENT,
  `tb01_nome_jogador` varchar(50) DEFAULT NULL,
  `tb01_pontos` int(11) DEFAULT NULL,
  `tb01_tempo` varchar(50) DEFAULT NULL,
  `tb01_dificuldade` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`tb01_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

-- Copiando dados para a tabela db_stopmario.tb01_partida: ~2 rows (aproximadamente)
INSERT INTO `tb01_partida` (`tb01_id`, `tb01_nome_jogador`, `tb01_pontos`, `tb01_tempo`, `tb01_dificuldade`) VALUES
	(1, 'Juliano', 7000, '12:15', 'Medio'),
	(6, 'Rafaela', 3000, '00:00', 'Facil');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
