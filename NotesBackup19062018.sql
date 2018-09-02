-- phpMyAdmin SQL Dump
-- version 4.0.10.14
-- http://www.phpmyadmin.net
--
-- Servidor: localhost:3306
-- Tiempo de generación: 19-06-2018 a las 16:24:36
-- Versión del servidor: 10.0.31-MariaDB-cll-lve
-- Versión de PHP: 5.4.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `miguela6_notas`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `notes`
--

CREATE TABLE IF NOT EXISTS `notes` (
  `note_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `content` mediumtext,
  `creation_date` bigint(11) DEFAULT NULL,
  `modification_date` bigint(11) DEFAULT NULL,
  `deleted` int(11) NOT NULL,
  `uploaded` int(11) NOT NULL,
  `pending_changes` int(11) NOT NULL,
  PRIMARY KEY (`note_id`,`user_id`),
  KEY `llave_usuarios` (`user_id`),
  KEY `user_id` (`user_id`),
  KEY `user_id_2` (`user_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=162 ;

--
-- Volcado de datos para la tabla `notes`
--

INSERT INTO `notes` (`note_id`, `user_id`, `title`, `content`, `creation_date`, `modification_date`, `deleted`, `uploaded`, `pending_changes`) VALUES
(2, 17, 'Que Lo Mio Con Sel', '', 1499785320, 1499832585, 0, 1, 0),
(5, 17, 'Fer', '', 1499785740, 1499832546, 0, 1, 0),
(6, 17, 'Dianne', '', 1499785740, 1499832552, 0, 1, 0),
(128, 5, 'Las quiero alla', '', 1506500940, 1506572984, 1, 1, 0),
(1, 19, 'Ueue', 'Udud', 1499836740, 1499840389, 0, 1, 0),
(2, 19, '&-$'''' Djjd', '', 1499836740, 1499840396, 0, 1, 0),
(123, 5, 'Danna', '', 1506351540, 1506573376, 0, 1, 0),
(126, 5, 'Cuando', '', 1506500940, 1506572985, 1, 1, 0),
(127, 5, 'Llegue', '', 1506500940, 1506572984, 1, 1, 0),
(122, 5, 'Denni', '', 1506351540, 1506573374, 0, 1, 0),
(121, 5, 'Pooo', '', 1506351420, 1506572987, 1, 1, 0),
(118, 5, 'Den', '', 1506233940, 1506572989, 1, 1, 0),
(117, 5, 'Dan', '', 1506233940, 1506572989, 1, 1, 0),
(116, 5, 'Seli', '', 1506233940, 1520221685, 1, 1, 0),
(115, 5, 'Fer', '', 1506233940, 1506572990, 1, 1, 0),
(113, 5, 'Sam', '', 1506233940, 1506572991, 1, 1, 0),
(100, 5, 'Dan', '', 1504971720, 1506051608, 1, 1, 0),
(1, 106, 'Hola', 'Kejsjdisbqjd', 1504614060, 1504617680, 0, 1, 0),
(129, 5, 'Hillian', '', 1506503700, 1506572983, 1, 1, 0),
(101, 5, 'A', '', 1506271920, 1506232333, 0, 1, 0),
(106, 5, 'F', '', 1506233880, 1506280685, 0, 1, 0),
(102, 5, 'B', '', 1506233820, 1506280675, 0, 1, 0),
(103, 5, 'Cocosi', '', 1506233820, 1520297680, 0, 1, 0),
(104, 5, 'D', '', 1506233880, 1506280680, 0, 1, 0),
(105, 5, 'E', '', 1506233880, 1506280683, 0, 1, 0),
(1, 110, 'Inpüúùūût', '', 1506001740, 1506048554, 0, 1, 0),
(1, 102, 'yy', '', 1501219560, 1501223175, 0, 1, 0),
(2, 102, 'asssdffg', '', 1501219920, 1501223685, 0, 1, 0),
(3, 102, 'assd', '', 1501220040, 1501223652, 0, 1, 0),
(1, 103, 'Mamadas :)', '', 1501221900, 1501225533, 0, 1, 0),
(2, 103, 'Pendejadas :/', '', 1501221900, 1501225526, 0, 1, 0),
(1, 104, 'Otro Pedo', 'Verga hasta tiene paea notas xD', 1501256880, 1501261487, 0, 1, 0),
(2, 104, 'kk', 'Ijfdbfbfnf', 1501257120, 1501261493, 0, 1, 0),
(3, 104, 'Nota sin título', 'QqFjfhhxhff', 1501257180, 1501261803, 1, 1, 0),
(4, 104, 'vtvtv', 'vvfvfvfvffvfvfvfvfvf', 1501257540, 1501261205, 0, 1, 0),
(2, 105, '2', 'Guay guay guay\\r\\nDhdkaldjdkskdkd', 1502021220, 1502068112, 0, 1, 0),
(1, 105, 'Notitukis', 'Hola Migue, esto esta muy guay.! :D', 1502020560, 1502067923, 1, 1, 0),
(109, 5, 'Tan', '', 1506233880, 1506572058, 1, 1, 0),
(108, 5, 'Cos', '', 1506233880, 1506572059, 1, 1, 0),
(1, 111, 'Poo', '', 1506145500, 1506192300, 0, 1, 0),
(124, 5, 'Si me', '', 1506421740, 1506572985, 1, 1, 0),
(125, 5, 'Falta el ruido', '', 1506421740, 1506572985, 1, 1, 0),
(1, 108, 'A', '', 1505918220, 1505965047, 0, 1, 0),
(2, 108, 'B', '', 1505918220, 1505965052, 0, 1, 0),
(3, 108, 'C', '', 1505918220, 1505965055, 0, 1, 0),
(1, 112, 'Hola', 'El correo ', 1506601200, 1506605104, 0, 1, 0),
(110, 5, 'Sec', '', 1506233940, 1506572058, 1, 1, 0),
(130, 5, 'Sami', 'df', 1520221671, 1520221693, 0, 1, 0),
(131, 5, 'dsdsd', '', 1520298061, 1520298061, 0, 1, 0),
(83, 1, 'Youtube iYTB', 'https://androidfilehost.com/?w=files&flid=170196\n\n\n', 1518344040000, 1522116483590, 0, 1, 0),
(1, 115, 'sl1 P', '', 1520458199000, 1520523009839, 1, 1, 0),
(2, 115, 'sl2 r', '', 1520458204000, 1520523007998, 1, 1, 0),
(3, 115, 'Perrito', '', 1520458207000, 1520523683000, 0, 1, 0),
(4, 115, 'Ls1 Op', '', 1520458130354, 1520523016607, 1, 1, 0),
(5, 115, 'Sinh(t)', '', 1520458133428, 1520522969147, 0, 1, 0),
(6, 115, 'Cosh(t)', '', 1520458136917, 1520492708731, 0, 1, 0),
(7, 115, 'Pato', '', 1520458300000, 1520492631000, 0, 1, 0),
(8, 115, 'polla', '', 1520458245300, 1520492540627, 0, 1, 0),
(9, 115, '" \\ ''', '', 1520485092000, 1520523012506, 1, 1, 0),
(10, 115, 'Gatote', '4/', 1520485079128, 1520492608314, 0, 1, 0),
(11, 115, 'Qwerty', '', 1520524343000, 1520524343000, 0, 1, 0),
(12, 115, 'Azerty', '', 1520524355000, 1520524355000, 0, 1, 0),
(13, 115, 'Victor', '', 1520524362000, 1520527949810, 0, 1, 0),
(14, 115, 'Pipes', '', 1520524321865, 1520524321865, 0, 1, 0),
(15, 115, 'Piscina', '', 1520524325347, 1520629499000, 0, 1, 0),
(16, 115, 'Alpha wolf', '', 1520524333996, 1520527968000, 0, 1, 0),
(17, 115, 'Deja que salga la luna', '', 1520629488000, 1520629488000, 0, 1, 0),
(84, 1, 'Dual Boot (W10 & Linux) Indications', '- First disable hibernation and Fast Boot on Windows\r\n- Disable the secure mode on BIOS\r\n- Install Linux normally \r\n\r\n*** This website shows how to fix the incorrect time in Windows with dual boot\r\nhttps://www.howtogeek.com/323390/how-to-fix-windows-and-linux-showing-different-times-when-dual-booting/\n\n\n\n', 1519222200000, 1522116501743, 0, 1, 0),
(74, 1, 'contra2@@', '', 1508148840000, 1518758123000, 1, 1, 0),
(76, 1, 'Fav PSs', 'Stella Cox\r\nAlex Grey\r\nNicole Aniston\r\nSybian\r\nTiffany Watson fight\nAlli Rae hotel maid', 1513265760000, 1524722911257, 1, 1, 0),
(77, 1, 'SSD M2 Info', 'https://www.kingston.com/us/ssd/system-builder/m2_faq', 1514638980000, 1514682207000, 0, 1, 0),
(78, 1, 'Pro Android Dev MVP', 'proandroiddev.com/clean-your-activity-using-delegation-pattern-fcaafd82336d', 1515664200000, 1515707455000, 0, 1, 0),
(79, 1, 'Oggie Base Raxton', 'use custom keyboard only on my app android\r\n\r\nhttps://stackoverflow.com/questions/15174606/disable-soft-keyboard-from-edittext-but-still-allow-copy-paste\r\n\r\nhttps://stackoverflow.com/questions/3988316/android-tie-a-softkeyboard-inputmethodservice-to-an-edittext\r\n*** (Old but good) -> http://tutorials-android.blogspot.mx/2011/06/create-your-own-custom-keyboard-for.html\r\nhttp://www.fampennings.nl/maarten/android/09keyboard/index.htm\r\nhttps://stackoverflow.com/questions/9577304/how-to-make-an-android-custom-keyboard/45005691#45005691\r\n', 1516109880000, 1516835329000, 0, 1, 0),
(80, 1, 'My Own Phrases', 'Recuerda que la fidelidad es el arte de practicar la monogamia en todos los sentidos y lograr eso es practicamente imposible porque asi no es nuestra verdadera naturaleza.\nSe dice que si te enamoras de verdad es posible lograrlo casi al 100% pues tu pareja es la persona con la cual te ves proyectado hasta el final de tus dias y no harias nada que pudiera poner en peligro el vinculo', 1516469280000, 1516512506000, 0, 1, 0),
(81, 1, 'Keepa Amazon Price Tracker', 'La s ', 1516814280000, 1523069381027, 0, 1, 0),
(82, 1, 'Check ViewModel Android', '', 1517424480000, 1518758149000, 1, 1, 0),
(73, 1, 'Swift Help', 'import Foundation\r\n\r\nfunc getGoogleMapsAPIUrl(address: String) -> String{\r\n	/*Example: = https://maps.googleapis.com/maps/api/geocode/json?address=Tecnológico%20de%20Morelia+Morelia+Michoacán+México&key=AIzaSyB_NHavuVVw0WwMt4Oo8iOThVnMau3ONsc*/\r\n	let MAPS_API_KEY : String = "AIzaSyB_NHavuVVw0WwMt4Oo8iOThVnMau3ONsc"\r\n	let GoogleMapsAPIURL: String = "https://maps.googleapis.com/maps/api/geocode/json?"\r\n	/*The method folding is used to remove diacritics (í, ó , ú)of String*/\r\n	return GoogleMapsAPIURL+"address="+address.replacingOccurrences(of: ", " , with: "+").replacingOccurrences(of: " ", with: "%20").folding(options: .diacriticInsensitive, locale: .current)+"&key="+MAPS_API_KEY\r\n}\r\n\r\nfunc getFoursquareAPIUrl(latitude: String, longitude: String) -> String{\r\n	/*Example: = https://api.foursquare.com/v2/venues/search?v=20161016&ll=41.878114%2C%20-87.629798&client_id=SWP0JTQFJEUNYVQEJZFGYF0A0FRAB5CYGEJ52VW3RRAL0GRZ&client_secret=YGFBL0O00ATAZV424NGNFCXG13LQAEOHXKMU1LEKQQMUDHRK*/\r\n	let date = Date()\r\n	let formatter = DateFormatter()\r\n	formatter.dateFormat = "yyyyMMdd"\r\n	let FOURSQUARE_CLIENT_ID: String = "SWP0JTQFJEUNYVQEJZFGYF0A0FRAB5CYGEJ52VW3RRAL0GRZ"\r\n	let FOURSQUARE_CLIENT_SECRET: String = "YGFBL0O00ATAZV424NGNFCXG13LQAEOHXKMU1LEKQQMUDHRK"\r\n	let FOURSQUARE_API_URL: String = "https://api.foursquare.com/v2/venues/search?"\r\n	let DATE_FORMATTED: String = formatter.string(from: date)\r\n	return FOURSQUARE_API_URL+"v="+DATE_FORMATTED+"&ll="+latitude+","+longitude+"&client_id="+FOURSQUARE_CLIENT_ID+"&client_secret="+FOURSQUARE_CLIENT_SECRET+"&limit=3"\r\n}\r\n\r\nlet var1: String = "papu", var2: String = "Prrrrrrro"\r\nprint("Hello world!")\r\nprint(var1+"-"+var2)\r\nprint(getGoogleMapsAPIUrl(address: "Tecnológico de Morelia, Morelia, Michoacán, México"))\r\nprint(getFoursquareAPIUrl(latitude: "19.7229386", longitude: "-101.1858201"))\r\n\n\n', 1507789020000, 1508039476000, 1, 1, 0),
(72, 1, 'My Google''s APIs Keys', 'Geolocation: AIzaSyB_NHavuVVw0WwMt4Oo8iOThVnMau3ONsc\r\nGeolocation: AIzaSyBMQHAMJwNj3gDeyw0ixRSw_BaliyZNtZo\r\nPlaces: AIzaSyDzQGWnaKRf5oEKUVWCyZKCgq_X2NaU7ko\r\nAndroid: AIzaSyAyviam9-KGMhNY3k8d6ojHCMaaSuiKisE\r\nDirections: AIzaSyA0AY22NCjty0gkFi9xSHz-LE_iHtoAyrg', 1507127040000, 1514909081000, 0, 1, 0),
(71, 1, 'Idea Ahorrar Datos En Sincronización', '- Crear columna cambios_pendientes (S/N)\r\n- El JSON NotasSync solo deberá mandar notas con estatus cambios_pendientes = ''S''\r\n- Si se elimina una nota se deben guardar los IDs de notas eliminadas que ademas ya hayan sido sincronizadas (Las no sincronizadas no es importante llevar un registro de si se han eliminado o no)\r\n- Mandar otro parámetro que contenga un array con los IDs de notas que debe eliminar el servidor\r\n- Todas las notas que envié de vuelta el JSON del servidor deberan tener el estatus de cambios_pendientes = ''N''\r\n- Usar replace con todo lo que envie el servidor\r\n- Cuando el servidor responda se deberá eliminar el arreglo de IDs de notas eliminadas\r\n\r\n¿Porqué se eliminan las notas en el login?\r\nEsto se debe a que si se tienen notas locales por ejemplo (1, 2, 3) estas se enviaran al servidor, y si el usuario ya tiene notas (Suponiendo que empiezan sus IDs en 7), las notas (1, 2, 3) ocuparan los lugares (8, 9, 10) pero las notas (1, 2, 3) seguiran existiendo, por lo que habra registros duplicados y por eso se eliminan solo en el login. Despues ya no es necesario pues en futuras sincronizaciones ya se tendran los IDs correctos que estan en la base de datos remota', 1506960360000, 1507003657000, 0, 1, 0),
(69, 1, 'Proyecto iOS', 'Ordenar lugares por cercania\n', 1506785040000, 1506785046000, 0, 1, 0),
(65, 1, 'My Own Ideas', 'corundas figuras\nestacionamientis reservados\nspinner carga celular ', 1505201940000, 1526266345824, 0, 1, 0),
(66, 1, 'Improvements For This App', 'Use replace instead of delete table and reinsert all rows\nOn Login, after create NoSyncNotes JSON delete all notes (Only on Login and Sign Up)\nOn Close Session delete only synced notes\n-Try to use Swipe Refresh Layout\n-Add search function in nav bar\n-Try to add multi select on Notes Fragment\n\nTwo possibilities to explain why losing notes sometimes:\n1.- Local notes JSONs don''t go out complete (Only 1 sync is neccesary to lose notes completely)\n2.- Remote JSON doesn''t arrive complete (2 syncs are needed to delete notes completely)', 1506189180000, 1506567492000, 0, 1, 0),
(61, 1, 'Frase', 'No se sale adelante celebrando exitos, sino superando fracasos', 1504347060000, 1504390261000, 0, 1, 0),
(60, 1, 'MVP with Orientation Changes', 'code.tutsplus.com/tutorials/how-to-adopt-model-view-presenter-on-android--cms-26206', 1504458660000, 1504415487000, 0, 1, 0),
(59, 1, 'Residencias Conceptos', 'REST\nPrincipios Solid\nMVP Android', 1504250040000, 1504293264000, 0, 1, 0),
(54, 1, 'Jorge Jacobo Rendon', 'Jorge Macias Peraza\nEs el Coco\n\n', 1502873640000, 1503289747000, 0, 1, 0),
(144, 1, 'B', '', 1524083176389, 1524083202644, 1, 1, 0),
(143, 1, 'A', '', 1524083173800, 1524083203451, 1, 1, 0),
(51, 1, 'Mensajero De La Oscuridad', 'http://mega.nz/#!EhVmAZYT!3An3HaxUJlTTEYFVDDGoDKqc2otk6eUKHGitzJE5U8s\n', 1502354400000, 1523487092469, 0, 1, 0),
(145, 1, 'C', '', 1524083179459, 1524083201052, 1, 1, 0),
(146, 1, 'D', '', 1524083183107, 1524083201939, 1, 1, 0),
(53, 1, 'Volley Docs', 'www.smashingmagazine.com/2017/03/simplify-android-networking-volley-http-library\n', 1502817060000, 1504293302000, 0, 1, 0),
(50, 1, 'Tio Serafin', 'collections@interdatesa.com\ncomsar_@hotmail.com\nElba5075\n\ncofesar@hotmail.com\nelba5077\navisofactura@hcmcollect.com\n\nApple ID\ncomsar_@hotmail.com && DonSera5075 \n\n\n', 1501571400000, 1523486932823, 0, 1, 0),
(49, 1, 'cdz', 'http:/www.facebook.com/112494728824787/videos/1549265325147713/', 1501430160000, 1501475744000, 0, 1, 0),
(149, 1, 'G', '', 1524083190709, 1524083198540, 1, 1, 0),
(148, 1, 'F', '', 1524083188248, 1524083198101, 1, 1, 0),
(147, 1, 'E', '', 1524083185693, 1524083197653, 1, 1, 0),
(48, 1, 'Test ''\\\\ " \\ \\"  "  ^\\ \\', '', 1500877080000, 1523486856460, 0, 1, 0),
(150, 1, 'H', '', 1524083193106, 1524083198975, 1, 1, 0),
(151, 1, 'I', '', 1524083195371, 1524083199409, 1, 1, 0),
(42, 1, 'Teddy', '__________,-~-. _.–._.-~-,\n_________/ .- ,’_______`-. \\\n_________\\ /`__________\\’/\n_________ /___’a___a`___\\\n_________|____,’(_)`.____ |\n_________\\___( ._|_. )___ /\n__________\\___ .__,’___ /\n__________.-`._______,’-.__\n________,’__,’___`-’___`.__`.\n_______/___/____V_____\\___\\_\n_____,’____/_____o______\\___`.__\n___,’_____|______o_______|_____`.\n__|_____,’|______o_______|`._____|\n___`.__,’_.-\\_____o______/-._`.__,’\n_________/_`.___o____,’__\\_\n__.”"-._,’______`._:_,’_______`.,-”"._\n_/_,-._`_______)___(________’_,-.__\\\n(_(___`._____,’_____`.______,’___)_)\n_\\_\\____\\__,’________`.____/.___/_/\n', 1500816900000, 1523486828158, 1, 1, 0),
(41, 1, 'Guitarra', '_____________#####\n_____________####\n_____________###\n_____________###\n_____________###\n_____________###\n_____________###\n_____________###\n_____________###__##\n_____________###__#__#\n_____________###___#__#\n_____________###___#___#\n_____________###___#____#\n_______##____###__#____#\n______#__#__######____#\n______#___##_____#____###\n_______#____#####____##\n________#___________####\n_________#_________###\n_________##_______###\n________##_________###\n_______##___________###\n______##______##_____##\n_____##_____#_____#___##\n_____##______####_____##\n_____###________________##\n______##_______________###\n_______###____________###\n_________####________###\n___________#########', 1500816600000, 1500859876000, 1, 1, 0),
(39, 1, 'Dolphin', '__________________ ##\n_________________###*\n______________.*#####\n_____________*######\n___________*#######\n__________*########.\n_________*#########.\n_________*#######*##*\n________*#########*###\n_______*##########*__*##\n_____*###########_____*\n____############\n___*##*#########\n___*_____########\n__________#######\n___________*######\n____________*#####*\n______________*####*\n________________*####\n__________________*##*\n____________________*##\n_____________________*##.\n____________________.#####.\n_________________.##########\n________________.####*__*####\n', 1500816360000, 1500859872000, 1, 1, 0),
(40, 1, 'Flower', ':::: , : – -:- – : , :::::\n:::: ) ` – : : – `(::::::\n::: / : : : :`\\: : :\\ ::::\n:::( : : : : : |: : :) ::\n::::\\ :: : : :/ : : / ::::\n:::::: `=(:/:)=` :::::\n:::::::: `-;`;-’ ::::::::\n::::::::::: || :: , ::::::\n::::::::::: ||_-’| ::::::\n::::::: |’-: || : / ::::::\n::::::::\\ : || :’ / ::::::\n::::::: \\ : || : /:::::::\n::::::::: \\:|| : /::::::\n::::::::: \\:||:/ :::::::\n:::::::::::\\||/:::::::::\n::::::::::::|| :::::::::\n::::::::::::|| :::::::::\n::::::::::::|| :::::::::\n::::::::::::|| :::::::::\n::::::::::::||:::::::::: ', 1500816480000, 1500859873000, 1, 1, 0),
(38, 1, 'Ranas', 'vvv rrr\nvv vrrr\nvvrv rr\nvvrvr r\nvvr rvr\nv rvrvr\n vrvrvr\nrv vrvr\nrvrv vr\nrvrvrv \nrvrvr v\nrvr rvv\nr rvrvv\nrr vrvv\nrrrv vv\nrrr vvv', 1500704520000, 1500751021000, 1, 1, 0),
(152, 1, 'MVP With Good Naming Convention', 'https://medium.com/@kailash09dabhi/mvp-with-better-naming-of-implementation-classes-dry-principle-e8b6130bbd02', 1524520860703, 1524529600428, 0, 1, 0),
(153, 1, 'MVP With Adapters XD', 'https://android.jlelse.eu/recyclerview-in-mvp-passive-views-approach-8dd74633158\n', 1524635418350, 1525244131552, 0, 1, 0),
(154, 1, 'Lic Cinthya Sánchez', 'cinthya.s@samsung.com\n\n\n\n\n', 1524758418182, 1525245513725, 0, 1, 0),
(155, 1, 'Gema Harman ', 'gema.bravo@harman.com\n\n\n\n\n\n', 1524759669226, 1525245801027, 0, 1, 0),
(156, 1, 'New Classes', 'OnboardingSupportFragment (To make intro activities)\nBottomSheet (Not properly a class is more like a convention of material design)\nBottomSheetDialogFragment(Disappeared from Official Docs)\n', 1524792448402, 1524928394200, 0, 1, 0),
(1, 113, 'Ff', '', 1524848527372, 1524848527372, 0, 1, 0),
(2, 113, 'Pooo', '', 1524848534802, 1524848538006, 1, 1, 0),
(3, 113, 'Vhvh', '', 1524851936450, 1524852216633, 1, 1, 0),
(4, 113, 'Hhh', '', 1524851942220, 1524851942220, 0, 1, 0),
(158, 1, 'Papulino', 'Prro', 1524939986180, 1525245716184, 0, 1, 0),
(32, 1, 'Buena Info Amazon', 'https://www.amazon.com.mx/gp/help/customer/display.html?nodeId=201812830\nhttps://www.amazon.com.mx/gp/aw/d/B01M2CM8CA/ref=mp_s_a_1_3?ie=UTF8&qid=1500084317&sr=8-3&pi=AC_SX118_SY170_QL70&keywords=bicicleta\n\n', 1495470240000, 1500876226000, 0, 1, 0),
(1, 114, 'Cuc', '', 1525536820369, 1525536830666, 1, 1, 0),
(2, 114, 'Txyc', '', 1525536825181, 1525536825181, 0, 1, 0),
(3, 114, 'Santader Contraseña', '46RcYU270163', 1525633505060, 1525633607307, 0, 1, 0),
(159, 1, 'Domicilio Tio Arcos', 'Calle Guamuchil, colonia el Fresno', 1527457841080, 1527457841080, 0, 1, 0),
(27, 1, '25/Feb/2017', 'Depilacion vello pubico', 1488132360000, 1488089181000, 1, 1, 0),
(160, 1, 'Papi', 'ric', 1527627152000, 1527627181000, 0, 1, 0),
(161, 1, 'Inversion', '8002.79 -> 15/06/2018\r\n8005.83 -> 16/06/2018\r\n8006.82 -> 18/06/2018', 1529115710166, 1529397010000, 0, 1, 0),
(18, 1, 'YuGiOh GX Full Japones Buena Calidd', 'https://mega.nz/#F!fUNmjCiC!VdeSqZgKzcJphFZEpo7ZyA', 1481214480000, 1482050494000, 0, 1, 0),
(19, 1, 'Ver Esto', '\\o/\n  l         Take my energy Goku!\n/ \\\n4451259437', 1482390840000, 1506960899000, 1, 1, 0),
(8, 1, 'Faltantes App 2', '-Iconos\n-Tratar de usar OnActivityResult\n-Titulos de nota largos\n- Alarmas en el pasado\n- Metodo de conversión a 12 horas\n- Activar receiver de booteo si y solo si se hace desde codigo\n', 1469542320000, 1469588797000, 0, 1, 0),
(13, 1, 'Conricyt', 'Pagina para ver cosas guays pero necesitas cuenta institucional xD', 1471513980000, 1471557227000, 0, 1, 0),
(14, 1, 'Dunlop Tines Xd', '5/sep/2016 -> Compra\n13/Abr/2018 -> Unboxing tines', 1473083880000, 1528047261650, 0, 1, 0),
(15, 1, 'Linux', '/boot (aprox 250MB)\n/home (archivos [la mas grande])\n/swap (doble de la ram)\n/         (aprox 30GB)\nTipo extendido en W7 con boot legacy.\nTipo primario en W8⬆ con boot UEFI', 1473502380000, 1473545631000, 0, 1, 0),
(16, 1, 'Grabacion En RA', 'Realidad aumentada y luego mostrarla a los demas.', 1474363740000, 1474406982000, 0, 1, 0),
(6, 1, 'Cuentas Sin Importancia :D', 'Gearbest && PayPal && QQMusic && EdModo && Samsung && Cisco && LinkedIn && Postman && 4shared\n\r\nmigue300995@gmail.com && Cont.Mast\r\n\r\nBanorte\r\nuser: Miguelopez123\r\ntip: Remember that password and PIN have something in common\r\n\r\nGitHub\r\nuser: MigueArcos\r\nmail: migue300995@gmail.com\r\npass: Cont.Mast\r\n\r\nSkype && Facebook && Instagram\r\nmla-66@hotmail.com && Cont.Mast\r\n\r\nApple, Oracle, BlackberryID, Amazon Mx\r\nmigue300995@gmail.com && Miguelopez123\r\n\r\nOverleaf && Promodescuentos && x10hosting \r\nmigue300995@gmail.com && migue300995\r\n\r\nMega:\r\nmigue300995@gmail.com && migue300995 && ClaveRec=dyhp1pzUxyffGetpVZc_rw\r\n\r\nClaroVideo:\r\n77596341\r\n\r\nMiTelcel\r\n4432007778 && migue0995\r\n\r\nSpotify\r\nmigue0995 && Cont.Mast\r\n\r\nBIOS Gateway Ne522 && BIOS Acer E5-475-52XJ\r\nmigue300995\r\n\n\nCuenta Tec Morelia\n13121475@tecmor.mx  && Cont.Mast\r\nFrase:\r\n\r\nIf you have knowledge, let other lights their candles in it\r\n\n\n\n\n\n', 1468760700000, 1529175165873, 0, 1, 0),
(5, 1, 'Link Flash Player', 'https://play.google.com/store/apps/details?id=com.adobe.flashplayer\nJejeje', 1468760640000, 1471579959000, 0, 1, 0),
(3, 1, 'Sobresuscripción', 'Buscarla en Internet', 1468161780000, 1469834834000, 0, 1, 0),
(2, 1, 'Estudios Inglés', '-Gerundios (Modos de uso) mas allá de las formas continuas de los verbos\n-Condicionales ingles\n-Verbos modales\n-Tratar de ver películas en ingles de vez en cuando y entenderlas.\n\n\nChecar\n- www.curso-ingles.com\n- www.inglesnaturalmente.com \n\nFarolazo:\n"Owning an Android device requires intellect"\nToday is gonna be the day\n', 1467959340000, 1500101665000, 0, 1, 0),
(18, 115, 'Dhjd', '', 1520911040189, 1520911040189, 0, 1, 0),
(136, 1, 'Hair Cuts', '03/Apr/2018 ', 1522809890874, 1522809890874, 0, 1, 0),
(139, 1, 'Last Border Test', '', 1523044776481, 1523044788475, 0, 1, 0),
(140, 1, 'App Improvements', 'Splash screen\nAction mode\nSwipe refresh layout\nCustom toolbars', 1523310929332, 1523473889180, 0, 1, 0),
(141, 1, 'Tratar De Traducir', 'De tu forma de hacer eso a lo que llamas amor\nOf your way to do that (that you call love)?', 1523481977694, 1523482207045, 0, 1, 0),
(113, 1, 'Fecha Titulo', 'Jueves 19 Abril, 10:00 AM-11:00 AM en Sala de titulación 1\n\nRecoger oficio titulacion el miercoles 11 de Abril', 1521743788781, 1521745269205, 0, 1, 0),
(142, 1, 'Estimación Gastos', 'Super semanal:\r\n{{pan bimbo,30}, {jugos,30},{sabritas,50},{sodas,40},{yakult,30},{galletas,30},{jamon,40},{miscelaneo,50}} \r\n\r\n$300 semanal aprox en super\r\n\r\nNota: Miscelaneo incluye: Papel baño, servilletas, quesadillas ingredientes, jabon, shampoo, fabuloso, cloro...etc\r\n\r\nQuerétaro (Gastos mensuales):\r\nSuper:                          $1,200\r\nPasaje:                           1,600\r\nComida:                         1,500\r\nRenta:                             3,000\r\nCenas:                            600\r\nTransporte:                   400\r\n\r\nTotal.                               $8,300\r\n\r\n\r\nMorelia (Gastos mensuales):\r\nSuper:                          $1,200\r\nPasaje:                            300\r\nComida:                         1,200\r\nRenta:                              2,000\r\nCenas:                             600\r\nTransporte:                    400\r\n\r\nTotal.                             $5,700', 1524079084416, 1524144966829, 1, 1, 0);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
