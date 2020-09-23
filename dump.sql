-- --------------------------------------------------------
-- Host:                         database-1.cakmnnvnot39.us-east-2.rds.amazonaws.com
-- Versione server:              8.0.17 - Source distribution
-- S.O. server:                  Linux
-- HeidiSQL Versione:            11.0.0.5919
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dump della struttura del database consiglia_viaggi_19
CREATE DATABASE IF NOT EXISTS `consiglia_viaggi_19` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `consiglia_viaggi_19`;

-- Dump della struttura di tabella consiglia_viaggi_19.recensione
CREATE TABLE IF NOT EXISTS `recensione` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `valutazione` smallint(6) NOT NULL,
  `testo` varchar(1000) NOT NULL,
  `dataCreazione` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `idUtente` int(11) NOT NULL,
  `idStruttura` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `SingolaRecensionePerStruttura` (`idUtente`,`idStruttura`),
  KEY `idStruttura` (`idStruttura`),
  CONSTRAINT `recensione_ibfk_1` FOREIGN KEY (`idUtente`) REFERENCES `utente` (`id`),
  CONSTRAINT `recensione_ibfk_2` FOREIGN KEY (`idStruttura`) REFERENCES `struttura` (`id`),
  CONSTRAINT `RangeValutazione` CHECK ((`valutazione` between 1 and 5))
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dump dei dati della tabella consiglia_viaggi_19.recensione: ~19 rows (circa)
/*!40000 ALTER TABLE `recensione` DISABLE KEYS */;
INSERT INTO `recensione` (`id`, `valutazione`, `testo`, `dataCreazione`, `idUtente`, `idStruttura`) VALUES
	(1, 3, 'Buona sosta prima/dopo una partenza                          ', '2020-08-26 14:23:35', 3, 15),
	(2, 4, 'Abbiamo prenotato due giorni per vedere bene la reggia, la vista dall\'alto è sensazionale.       ', '2020-08-26 14:26:24', 4, 12),
	(3, 4, 'Hotel molto lussioso per un\'allegro weekend di famiglia (?)                                         ', '2020-08-26 14:30:27', 4, 11),
	(4, 5, 'bell\'hotel ma qui gli alberi sono tutti verdi                                                        ', '2020-08-26 14:31:19', 6, 13),
	(5, 5, 'Visita obbligatoria prima di un\'esame se si vuole prendere un 30 politico                               ', '2020-08-26 14:32:28', 7, 4),
	(6, 5, 'Tutto molto buono ma i frequentatori del locale sono tutti psicologi/avvocati hipster                           ', '2020-08-26 14:33:47', 6, 2),
	(7, 4, 'Tranne che per uno strano odore di petrolio sul mio panino, tutto buono direi!', '2020-08-26 14:35:47', 3, 1),
	(8, 5, 'Luogo di ritrovo per tutti gli studenti universitari il giorno prima di un\'esame; all\'ordine del giorno: rituali voodoo sul professore e ubriacate tra colleghi', '2020-08-26 12:39:23', 1, 5),
	(9, 4, 'Locale napoletano famoso tra i ragazzi', '2020-08-26 14:45:42', 5, 5),
	(10, 1, 'Se domani passo l\'esame metto 5 stelle.                                                                              ', '2020-08-26 14:48:43', 2, 5),
	(11, 4, 'Dopo una lunga giornata di lezione non c\'è niente di meglio di una sushata di gruppo tra colleghi', '2020-08-26 14:49:26', 7, 3),
	(12, 4, 'Il loro algoritmo di servizio dei clienti è molto efficiente, voto DIESHII                                                       ', '2020-08-26 14:52:15', 6, 1),
	(13, 5, 'Teatro famoso in tutto il mondo, visita obbligatoria per i turisti                      ', '2020-08-26 14:53:25', 2, 6),
	(14, 5, 'Proprio come in Jojo! ZA WAAARUUUDOOOOOO                                                                    ', '2020-08-26 14:54:51', 6, 7),
	(15, 4, 'A Napoli conosciuto come il maschio angioino,ospita spesso mostre e esposizioni di varia natura, merita di essere visitato.', '2020-08-26 14:59:03', 4, 8),
	(16, 2, 'Ci manca solo che ti fanno pagare per entrare per quanto è tenuto male.                                                                ', '2020-08-26 14:59:19', 5, 9),
	(17, 4, 'Ospita collezioni d\' arte e ceramiche di lusso e pregio                                                        ', '2020-08-26 13:02:14', 4, 9),
	(18, 5, 'La migliore università di Napoli! professori bravissimi e gentilissimi! e non lo sto dicendo perchè domani ho un\'esame nono', '2020-08-26 15:02:36', 2, 10),
	(19, 1, 'In attesa di un 615 per poterla visitare.                                                                             ', '2020-08-26 13:04:50', 3, 10);
/*!40000 ALTER TABLE `recensione` ENABLE KEYS */;

-- Dump della struttura di tabella consiglia_viaggi_19.struttura
CREATE TABLE IF NOT EXISTS `struttura` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nomeStruttura` varchar(40) NOT NULL,
  `indirizzo` varchar(100) NOT NULL,
  `latitudine` decimal(10,8) NOT NULL,
  `longitudine` decimal(11,8) NOT NULL,
  `tipoStruttura` enum('Hotel','Ristorante','AttrazioneTuristica') NOT NULL,
  `descrizione` varchar(100) DEFAULT NULL,
  `emailStruttura` varchar(320) DEFAULT NULL,
  `sitoWeb` varchar(2080) DEFAULT NULL,
  `numeroTelefono` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `SingolaStrutturaSuCoordinate` (`nomeStruttura`,`latitudine`,`longitudine`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dump dei dati della tabella consiglia_viaggi_19.struttura: ~15 rows (circa)
/*!40000 ALTER TABLE `struttura` DISABLE KEYS */;
INSERT INTO `struttura` (`id`, `nomeStruttura`, `indirizzo`, `latitudine`, `longitudine`, `tipoStruttura`, `descrizione`, `emailStruttura`, `sitoWeb`, `numeroTelefono`) VALUES
	(1, 'McDonald\'s Napoli Stadio Fuorigrotta', 'Via Giulia Gonzaga, 31/35, 80125 Napoli NA', 40.83040600, 14.19495900, 'Ristorante', 'La famosa catena di ristoranti a Fuorigrotta! apprezzato da  studenti universitarei obesi', NULL, 'mcdonalds.it', '0816107007'),
	(2, 'Locanda del Cerriglio', 'Via del Cerriglio, 3, 80134 Napoli NA', 40.84394000, 14.25404000, 'Ristorante', 'Rinomato ristorante di Napoli,apprezzato da studenti universitari della sede di Porte di massa', NULL, 'locandadelcerriglio.it', '0815526406'),
	(3, 'Tokyo 2', 'Via Diocleziano, 107, 80124 Napoli NA', 40.82010600, 14.18522400, 'Ristorante', 'Ristorante giapponese, apprezzato da studenti universitari particolarmente otaku', NULL, 'ristorantetokyo2.it', '0815703108'),
	(4, 'La Facoltà', 'Via Cintia, 3, 80126 Napoli NA', 40.83895300, 14.18827900, 'Ristorante', 'Ristorante/Pizzeria situata a pochi passi dalla sede di Monte Sant\'angelo', NULL, NULL, NULL),
	(5, 'Cammarota Spritz', 'Vico Lungo Teatro Nuovo, 31, 80134 Napoli NA', 40.84240700, 14.24765100, 'Ristorante', 'Pub apprezzato da giovani studenti universitari particolarmente disperati', NULL, 'cammarotasprintz.com', '3202775687'),
	(6, 'Teatro di San Carlo', 'Via San Carlo, 98, 80132 Napoli NA', 40.83761500, 14.24961600, 'AttrazioneTuristica', 'Antichissimo teatro di fama storica famoso in tutto il mondo', NULL, 'teatrosancarlo.it', '0817972331'),
	(7, 'Castel dell\'Ovo', 'Via Eldorado, 3, 80132 Napoli NA', 40.82846200, 14.24756100, 'AttrazioneTuristica', 'Imponente fortezza ed antica residenza reale, con 2 torri panoramiche e sale per conferenze.', NULL, 'comune.napoli.it', '0817956180'),
	(8, 'Castel Nuovo', 'Via Vittorio Emanuele III, 80133 Napoli NA', 40.83872300, 14.25271900, 'AttrazioneTuristica', 'Fortezza medievale con 5 torri e arco trionfale rinascimentale, museo d\'arte civico.', NULL, 'comune.napoli.it', '0817957722'),
	(9, 'Villa Floridiana', 'Via Domenico Cimarosa, 77, 80127 Napoli NA', 40.84127800, 14.23051700, 'AttrazioneTuristica', 'Luogo di interesse storico ed artistico di Napoli, situato nel quartiere del Vomero', NULL, 'comune.napoli.it', '0815788418'),
	(10, 'Università Federico 2 Complesso Monte Sa', 'Strada Vicinale Cupa Cintia, 21, 80126 Napoli NA', 40.83971900, 14.18772000, 'AttrazioneTuristica', 'Patrimonio architetturale della Federico 2, Ben gestita e facile da raggiungere1!!1!', NULL, NULL, NULL),
	(11, 'Hotel Royal Continental', 'Via Partenope, 38, 80121 Napoli NA', 40.83008700, 14.24690700, 'Hotel', 'Situato di fronte al Golfo di Napoli, vicino al Teatro San Carlo', NULL, 'royalgroup.it', '0812452068'),
	(12, 'Hotel dei Cavalieri Caserta', 'Piazza Luigi Vanvitelli, 12, 81100 Caserta CE', 41.07621500, 14.33219600, 'Hotel', 'Hotel di lusso situato vicino la Reggia di Caserta', NULL, 'deicavaliericaserta.com', '0823355520'),
	(13, 'Hotel San Francesco al monte', 'Corso Vittorio Emanuele, 328, 80135 Napoli NA', 40.84348300, 14.24453700, 'Hotel', 'Hotel raffinato, situato in un antico monastero del XVI secolo.', NULL, 'sanfrancescoalmonte.it', '0814239111'),
	(14, 'Hotel NH Napoli Panorama', 'Via Medina, 70, 80133 Napoli NA', 40.84242500, 14.25189300, 'Hotel', 'Hotel in zona centro storico, molto iconico per via della statura dell\'edificio.', NULL, NULL, NULL),
	(15, 'Capodichino International', 'Viale Comandante Umberto Maddalena, 35, 80144 Napoli NA', 40.87327100, 14.28167700, 'Hotel', 'Hotel modesto vicino all\'Aereoporto di Napoli', NULL, 'hotelcapodichino.it', '0817518786');
/*!40000 ALTER TABLE `struttura` ENABLE KEYS */;

-- Dump della struttura di tabella consiglia_viaggi_19.utente
CREATE TABLE IF NOT EXISTS `utente` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nomeUtente` varchar(16) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nomeReale` varchar(60) DEFAULT NULL,
  `cognomeReale` varchar(60) DEFAULT NULL,
  `preferisceNomeReale` tinyint(1) DEFAULT NULL,
  `emailUtente` varchar(320) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nomeUtente` (`nomeUtente`),
  UNIQUE KEY `emailUtente` (`emailUtente`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='id 1 importante per il server metti sempre admin';

-- Dump dei dati della tabella consiglia_viaggi_19.utente: ~7 rows (circa)
/*!40000 ALTER TABLE `utente` DISABLE KEYS */;
INSERT INTO `utente` (`id`, `nomeUtente`, `password`, `nomeReale`, `cognomeReale`, `preferisceNomeReale`, `emailUtente`) VALUES
	(1, 'noctino52', '$2a$10$pS9SynJUkjEWmNgrz7lEO.f./lCB/xBh25a.YBs8rLkA.zovFry2e', 'Ivan', 'Capasso', 1, 'ivan.capasso.5252@gmail.com'),
	(2, 'lory22', '$2a$10$K/jQ4gg3at/SJF9wd6ma6.hmZ/IPGzHxos74X55knjA5ijavnyAuq', 'Lorenzo', 'Ricci', 0, 'lorenzoricci@gmail.com'),
	(3, 'lion99', '$2a$10$andyOe4v6r1qw0ffQ4tBweKctddaKB2DInT482D9EKLk9/OFLlYHG', 'Leonardo', 'Santoro', 1, 'leonardosantoro@gmail.com'),
	(4, 'orseta01', '$2a$10$pZcudDkDU8wq7iOGwIr02O41oqCAzkKP8o79Fvw4Clclgw881Zd2O', 'Sofia', 'D\'angelo', 0, 'sofy@live.it'),
	(5, 'marty67', '$2a$10$01xcfoM0OMAPP4VO7tqIc.kvGZcLCi2Vs9n9lw676Blw/hcjtLmYq', 'Giovanni', 'Martinelli', 0, 'marty67@ops.it'),
	(6, 'redandblacklover', '$2a$10$ie4HaCGwPsl33if3sevK4utk7.xN9Zf9l/gAoMQZk5O0v72z9I0sC', 'Red', 'Black', 0, 'binarytree@leaf.com'),
	(7, 'winner52', '$2a$10$8mxi/Zdp5DNJvE1AGkC.juN6zxmATHmw524FPLpmedI2f9LSyGn4q', 'Ciro', 'Gallo', 1, 'cirogallo@ciro.it');
/*!40000 ALTER TABLE `utente` ENABLE KEYS */;

-- Dump della struttura di trigger consiglia_viaggi_19.NomeUtenteMinuscoloEConLunghezzaValida
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `NomeUtenteMinuscoloEConLunghezzaValida` BEFORE INSERT ON `utente` FOR EACH ROW BEGIN
		DECLARE lunghezzaNomeUtente INT;
		SET lunghezzaNomeUtente = (SELECT LENGTH(new.nomeUtente));

        IF(INSTR(new.nomeUtente, ' ') > 0) THEN
            SIGNAL SQLSTATE '45002'
		    SET MESSAGE_TEXT = 'Errore: Un nome utente non può contenere spazi';
        END IF;

		IF(lunghezzaNomeUtente < 4) THEN
            SIGNAL SQLSTATE '45005'
		    SET MESSAGE_TEXT = 'Errore: Un nome utente non può essere minore di 4 caratteri';
        END IF;

    SET new.nomeUtente = LOWER(new.nomeUtente);
    SET new.emailUtente = LOWER(new.emailUtente);
    END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Dump della struttura di trigger consiglia_viaggi_19.RecensioneTroppoBreve
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `RecensioneTroppoBreve` BEFORE INSERT ON `recensione` FOR EACH ROW BEGIN
        IF(LENGTH(new.testo) < 100) THEN
            SIGNAL SQLSTATE '45001'
		    SET MESSAGE_TEXT = 'Errore: Una recensione deve contenere almeno 100 caratteri';
        END IF;
    END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
