-- phpMyAdmin SQL Dump
-- version 4.0.10.14
-- http://www.phpmyadmin.net
--
-- Servidor: localhost:3306
-- Tiempo de generación: 24-11-2017 a las 02:02:15
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
-- Estructura de tabla para la tabla `notas`
--

CREATE TABLE IF NOT EXISTS `notas` (
  `id_nota` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `titulo` varchar(100) DEFAULT NULL,
  `contenido` mediumtext,
  `fecha_creacion` varchar(25) DEFAULT NULL,
  `fecha_modificacion` varchar(25) DEFAULT NULL,
  `fecha_modificacion_orden` varchar(20) DEFAULT NULL,
  `eliminado` char(1) DEFAULT NULL,
  `subida` char(1) DEFAULT NULL,
  PRIMARY KEY (`id_nota`,`id_usuario`),
  KEY `llave_usuarios` (`id_usuario`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `notas`
--

INSERT INTO `notas` (`id_nota`, `id_usuario`, `titulo`, `contenido`, `fecha_creacion`, `fecha_modificacion`, `fecha_modificacion_orden`, `eliminado`, `subida`) VALUES
(2, 17, 'Que Lo Mio Con Sel', '', '11/07/2017 a las 11:02 PM', '11/07/2017 a las 11:09 PM', '1499832585', 'N', 'S'),
(4, 1, 'Sobresuscripción', 'Buscarla en Internet', '10/07/2016 a las 9:43 p.m', '29/07/2016 a las 6:27 p.m', '1469834834', 'N', 'S'),
(5, 17, 'Fer', '', '11/07/2017 a las 11:09 PM', '11/07/2017 a las 11:09 PM', '1499832546', 'N', 'S'),
(6, 17, 'Dianne', '', '11/07/2017 a las 11:09 PM', '11/07/2017 a las 11:09 PM', '1499832552', 'N', 'S'),
(2, 1, 'Estudios Inglés', '-Gerundios (Modos de uso) mas allá de las formas continuas de los verbos\n-Condicionales ingles\n-Verbos modales\n-Tratar de ver películas en ingles de vez en cuando y entenderlas.\n\n\nChecar\n- www.curso-ingles.com\n- www.inglesnaturalmente.com \n\nFarolazo:\n"Owning an Android device requires intellect"\nToday is gonna be the day\n', '08/07/2016 a las 1:29 p.m', '15/07/2017 a las 01:54 AM', '1500101665', 'N', 'S'),
(128, 5, 'Las quiero alla', '', '27/09/2017 a las 04:29 PM', '27/09/2017 a las 11:29 PM', '1506572984', 'S', 'S'),
(1, 19, 'Ueue', 'Udud', '12/07/2017 a las 01:19 AM', '12/07/2017 a las 01:19 AM', '1499840389', 'N', 'S'),
(2, 19, '&-$'''' Djjd', '', '12/07/2017 a las 01:19 AM', '12/07/2017 a las 01:19 AM', '1499840396', 'N', 'S'),
(1, 1, 'Faltantes De App', '-Checar boton deshacer que si elimina de la BD\n-Metodo OnBackPressed\n-Checar el orden de las notas\n-Añadir columna de eliminado para no eliminar las notas\n-Permiso de write external storage en tiempo de ejecucion.\n-Poner SetHasFixedSize(True)\n-Icono de las notificaciones\n-Poner esta lista de tamaño completo (Ya hay boton flotante)\n-Ordenar bien por fecha de modificación\n-Quitar que editor sea hijo de notas\n-Checar que se actualice bien el timepicker despues de una vez.\n-Error al borrar\n-Iconos\n-Tratar de usar OnActivityResult\n-Titulos de nota largos\n', '08/07/2016 a las 1:29 p.m', '25/07/2016 a las 10:04 a.', '1469459049', 'N', 'S'),
(19, 1, 'Code', '#include <stdio.h>\nint pot();\nint main(){\nint n=0, p=0,a=0;\nprintf("Escribe la base de la potencia muchacho\\n");\nscanf("%d", &n);\nprintf("Escribe el exponente al cual la quieres elevar muchacho\\n");\nscanf("%d", &p);\n//En este caso n es la base, p la potencia, y en la entrada de la funcion van 3 variables auxiliares con el mismo valor quee la base//\na=pot(n, p, n, n, n);\nprintf("El resultado de %d^%d= %d\\n",n,p, a);\nreturn 1;\n}\nint pot(int b, int e1, int b2, int b3, int b4){\nif (e1==0) {return 1;}\nif (e1==1) {return b;}\nif (b2>=1) {return b+pot(b,e1, b2-1, b3, b4);}\nif (b2==0){\n	int k=0;\n	b2=b3;\n	b4=b;\n	for (k=0; k<b3-1; k++){\n		b=b+b4;\n	}\n	return pot(b, e1-1, b2-1, b3, b4);\n}\n	\n}', '16/07/2016 a las 1:27 p.m', '16/07/2016 a las 1:27 p.m', '1468693656', 'N', 'S'),
(20, 1, 'Link Flash Player', 'https://play.google.com/store/apps/details?id=com.adobe.flashplayer\nJejeje', '17/07/2016 a las 8:04 p.m', '18/08/2016 a las 11:12 p.', '1471579959', 'N', 'S'),
(21, 1, 'Cuentas Sin Importancia', 'Gearbest && PayPal && QQMusic && EdModo && Samsung && Cisco && LinkedIn && Postman\n\r\nmigue300995@gmail.com && Cont.Mast\r\n\r\nBanorte\r\nuser: Miguelopez123\r\ntip: Remember that password and PIN have something in common\r\n\r\nGitHub\r\nuser: MigueArcos\r\nmail: migue300995@gmail.com\r\npass: Cont.Mast\r\n\r\nSkype && Facebook && Instagram\r\nmla-66@hotmail.com && Cont.Mast\r\n\r\nApple, Oracle, BlackberryID, Amazon Mx\r\nmigue300995@gmail.com && Miguelopez123\r\n\r\nOverleaf && Promodescuentos && x10hosting \r\nmigue300995@gmail.com && migue300995\r\n\r\nMega:\r\nmigue300995@gmail.com && migue300995 && ClaveRec=dyhp1pzUxyffGetpVZc_rw\r\n\r\nClaroVideo:\r\n77596341\r\n\r\nMiTelcel\r\n4432007778 && migue0995\r\n\r\nSpotify\r\nmigue0995 && Cont.Mast\r\n\r\nBIOS Gateway Ne522\r\nmigue300995\r\n\r\nFrase:\r\n\r\nIf you have knowledge, let other lights their candles in it\r\n\n', '17/07/2016 a las 8:05 p.m', '05/10/2017 a las 08:42 AM', '1507210960', 'N', 'S'),
(22, 1, 'Android', 'Componentes nuevos.\nUndobar.\nSnackbar.\nRecyclerView.\nCardview.', '17/07/2016 a las 8:08 p.m', '17/07/2016 a las 8:08 p.m', '1468804137', 'N', 'S'),
(24, 1, 'Faltantes App 2', '-Iconos\n-Tratar de usar OnActivityResult\n-Titulos de nota largos\n- Alarmas en el pasado\n- Metodo de conversión a 12 horas\n- Activar receiver de booteo si y solo si se hace desde codigo\n', '26/07/2016 a las 9:12 p.m', '26/07/2016 a las 10:06 p.', '1469588797', 'N', 'S'),
(25, 1, 'Tec', 'Concepto: Reinscripcion\nNo. Referencia: 01E131214750710214406\nNo. Cuenta: 4008637019\nNombre: SEP-Instituto Tecnologico de Morelia\nClave de servicio: 9630\nMonto: $ 2400.00\n', '28/07/2016 a las 8:10 a.m', '07/09/2017 a las 03:42 PM', '1504816971', 'N', 'S'),
(29, 1, 'Si Me Tenias Porque Cambiaste La Frontera De Otros', '', '29/07/2016 a las 6:44 p.m', '29/07/2016 a las 6:45 p.m', '1469835932', 'S', 'S'),
(30, 1, 'Examen Ingles Unit 5-6', 'Page 52\nPage 54\nPage 66\nPage 64*\nPage 56\nPage 53*\nPage 68', '02/08/2016 a las 10:57 p.', '03/08/2016 a las 10:41 a.', '1470238908', 'S', 'S'),
(31, 1, 'Tarea Int. Web', 'Mandar correo a:\nrogelio@itmorelia.edu.mx\nDesde mla-66\nMandar nombre y grupo\nPortafolio de los trabajos que he hecho con ligas a github\n', '15/08/2016 a las 1:19 p.m', '15/08/2016 a las 1:26 p.m', '1471285600', 'N', 'S'),
(34, 1, 'Conricyt', 'Pagina para ver cosas guays pero necesitas cuenta institucional xD', '18/08/2016 a las 4:53 p.m', '18/08/2016 a las 4:53 p.m', '1471557227', 'N', 'S'),
(37, 1, 'Dunlop Compra Tines', '5/sep/2016', '05/09/2016 a las 8:58 p.m', '05/09/2016 a las 8:58 p.m', '1473127084', 'N', 'S'),
(38, 1, 'Linux', '/boot (aprox 250MB)\n/home (archivos [la mas grande])\n/swap (doble de la ram)\n/         (aprox 30GB)\nTipo extendido en W7 con boot legacy.\nTipo primario en W8⬆ con boot UEFI', '10/09/2016 a las 5:13 p.m', '10/09/2016 a las 5:13 p.m', '1473545631', 'N', 'S'),
(39, 1, 'Grabacion En RA', 'Realidad aumentada y luego mostrarla a los demas.', '20/09/2016 a las 4:29 p.m', '20/09/2016 a las 4:29 p.m', '1474406982', 'N', 'S'),
(40, 1, 'Faltantes Web', 'If (disponibles==0)\nGenerar div deshabilitado\nPonerle un background\nIf (carrito.estatus=disponibles)\nMarcar como no disponible en carrito\n\n* En todas las consultas de suma  hay que ignorar los de estatus no disponible\n\nEn el catalogo ya esta programado para que no se muestren items si cant=0. Ahora falta deshabilitar el boton de comprar si no esta loggeado\n\n*Al comprar hay que afectar 3 tablas (carrito, articulo, venta)\n\n*En ajax si se vendio el articulo antes de que un usuario lo haya comprado cuando da clic en comprar y en añadir maso menor se puede mostar un alert que diga " Te lo ganaron" ', '13/11/2016 a las 10:27 p.', '13/11/2016 a las 10:27 p.', '1479097665', 'N', 'S'),
(41, 1, 'YuGiOh GX Full Japones Buena Calidd', 'https://mega.nz/#F!fUNmjCiC!VdeSqZgKzcJphFZEpo7ZyA', '08/12/2016 a las 10:28 a.', '18/12/2016 a las 2:41 a.m', '1482050494', 'N', 'S'),
(45, 1, 'Ver Esto', '\\o/\n  l         Take my energy Goku!\n/ \\\n4451259437', '22/12/2016 a las 1:14 a.m', '02/10/2017 a las 11:14 AM', '1506960899', 'S', 'S'),
(46, 1, 'Tio Serafin', 'Damian Alcaraz\n\nLuis Felipe Tovar\n\nJoaquin Cosa\n\nCecilia Suarez\n\nManuel Ojeda\n\nLeticia Huijara', '31/12/2016 a las 12:38 p.', '31/12/2016 a las 12:54 p.', '1483210466', 'N', 'S'),
(47, 1, 'Horario Del Servicio', 'L,M=2:30-6:30\nM,J=3:30-7:30\nV=10-12,4:30-6:30', '20/01/2017 a las 2:45 p.m', '20/01/2017 a las 2:45 p.m', '1484945123', 'N', 'S'),
(48, 1, 'Tarea Guapo', 'Instalar CentOS no muy reciente 6.8 (Preferencia en maquina virtual, vmware)\nInvestigar comandos basicos de CentOS Linux\nInvestigar demonios y levantar servicios', '23/01/2017 a las 1:34 p.m', '23/01/2017 a las 1:35 p.m', '1485200138', 'N', 'S'),
(49, 1, 'Fichitas', 'Puede haber ficha 0.1 (No colocada)\r\nEn el random hay que usar cualficha con los nuevos valores\r\nInicializar en -1\r\n''Xx\\\\\\ ''x ', '24/01/2017 a las 1:35 a.m', '26/07/2017 a las 12:24 AM', '1501046677', 'S', 'S'),
(50, 1, 'Chiquilla', 'vv rr\nv vrr\nvrv r\nvrvr\nvr rv\n rvrv\nr vrv\nrrv v\nrr vv\nRelacion de anteproyecto e informe final (Corina Schmelkes)\nPasos segun Sanpieri para realizar protocolo de investigacion\nPara el martes leer el material que Peñuñuri nos debe pasar\n\nElaborar diagnostico del analisis del protocolo del semestre anterior\n\n', '25/01/2017 a las 11:48 a.', '05/10/2017 a las 08:42 AM', '1507210927', 'S', 'S'),
(51, 1, 'Chavin', 'Sing street\nEl contador\nHasta el ultimo hombre (pendiente)', '29/01/2017 a las 12:35 p.', '29/01/2017 a las 12:35 p.', '1485714908', 'N', 'S'),
(53, 1, '10 Ideas Para Eventos De Atraccion Talen', 'Unam\nMit \nStanford', '24/02/2017 a las 11:53 a.', '21/07/2017 a las 10:44 AM', '1500651865', 'S', 'S'),
(54, 1, '25/Feb/2017', 'Depilacion vello pubico', '26/02/2017 a las 12:06 a.', '26/02/2017 a las 12:06 a.', '1488089181', 'S', 'S'),
(55, 1, 'Feria De Atraccion De Talento 2017', '', '17/03/2017 a las 12:55 p.', '21/07/2017 a las 10:18 AM', '1500650324', 'S', 'S'),
(56, 1, 'Tarea Monica', 'Empezar a taratar de haxer una sintesis\n\nBuscar 4 exordios\nEj. Luther King\n\nCrear mi propio exordio', '25/04/2017 a las 9:37 a.m', '25/04/2017 a las 9:41 a.m', '1493131264', 'N', 'S'),
(57, 1, 'Nota sin título', '8 may 1-3\n9 May 1-3\n16 may 9-11 && 11-1\nRequisito:CURP y lapicero y libreta\n', '04/05/2017 a las 9:02 a.m', '04/05/2017 a las 9:02 a.m', '1493906537', 'S', 'S'),
(58, 1, 'Hacer Horoscopos', 'Preguntando fecha, regresar caballeros del zodiaco lost canvas jej\n\nAir alert\nVerificar si esta vacio\nContar lista\nVer si 2 listas son iguales', '11/05/2017 a las 10:27 a.', '21/06/2017 a las 11:19 p.', '1498105187', 'N', 'S'),
(59, 1, 'Buena Info Amazon', 'https://www.amazon.com.mx/gp/help/customer/display.html?nodeId=201812830\nhttps://www.amazon.com.mx/gp/aw/d/B01M2CM8CA/ref=mp_s_a_1_3?ie=UTF8&qid=1500084317&sr=8-3&pi=AC_SX118_SY170_QL70&keywords=bicicleta\n\n', '22/05/2017 a las 11:24 p.', '24/07/2017 a las 01:03 AM', '1500876226', 'N', 'S'),
(60, 1, 'Nota sin título', 'Conclusion:\nDefinitivamente es posible predecir el comportamiento de un incendio forestal en la mayoria de los casos, si se logra implementar este sistema en la mayoria de los  bosques de la republica mexicana se puede ahorrar mucho tiempo para acabar con los incendios, asi como tambien mucho dinero y la vida de muchas especies animales y vegetales, ya que el daño que causaria cada incendio se veria muy reducido pues si se predice su comportamiento, el incendio se podra controlar a la brevedad.', '31/05/2017 a las 1:47 a.m', '31/05/2017 a las 1:47 a.m', '1496213258', 'S', 'S'),
(61, 1, 'Villalcaba', 'Target robotics (2016/8) no se le puso id porque no se sabe que tipo de pail es\n', '01/06/2017 a las 12:55 p.', '19/06/2017 a las 10:54 p.', '1497930877', 'S', 'S'),
(123, 5, 'Danna', '', '25/09/2017 a las 10:59 PM', '27/09/2017 a las 11:36 PM', '1506573376', 'N', 'S'),
(134, 1, 'Pendientes Compras', 'Pilas de Mouse\r\nDesodorante\r\nGel\r\nAsiento Bicicleta\r\nCamara Bicicleta?\r\nCrema?\r\nObjeto para guardar cosméticos\r\n', '10/09/2017 a las 09:25 PM', '10/09/2017 a las 09:26 PM', '1505096784', 'N', 'S'),
(135, 1, 'ideas', 'corundas figuras\nestacionamientis reservados\nspinner carga celular', '12/09/2017 a las 02:39 PM', '12/09/2017 a las 02:39 PM', '1505245142', 'N', 'S'),
(130, 1, 'Frase', 'No se sale adelante celebrando exitos, sino superando fracasos', '02/09/2017 a las 05:11 PM', '02/09/2017 a las 05:11 PM', '1504390261', 'N', 'S'),
(126, 5, 'Cuando', '', '27/09/2017 a las 04:29 PM', '27/09/2017 a las 11:29 PM', '1506572985', 'S', 'S'),
(127, 5, 'Llegue', '', '27/09/2017 a las 04:29 PM', '27/09/2017 a las 11:29 PM', '1506572984', 'S', 'S'),
(124, 1, 'Poo', '', '25/08/2017 a las 10:44 PM', '25/08/2017 a las 10:44 PM', '1503719074', 'N', 'S'),
(122, 5, 'Denni', '', '25/09/2017 a las 10:59 PM', '27/09/2017 a las 11:36 PM', '1506573374', 'N', 'S'),
(121, 5, 'Pooo', '', '25/09/2017 a las 10:57 PM', '27/09/2017 a las 11:29 PM', '1506572987', 'S', 'S'),
(145, 1, 'Proyecto iOS', 'Ordenar lugares por cercania\n', '30/09/2017 a las 10:24 AM', '30/09/2017 a las 10:24 AM', '1506785046', 'N', 'S'),
(118, 5, 'Den', '', '24/09/2017 a las 02:19 PM', '27/09/2017 a las 11:29 PM', '1506572989', 'S', 'S'),
(117, 5, 'Dan', '', '24/09/2017 a las 02:19 PM', '27/09/2017 a las 11:29 PM', '1506572989', 'S', 'S'),
(116, 5, 'Sel', '', '24/09/2017 a las 02:19 PM', '27/09/2017 a las 11:29 PM', '1506572990', 'S', 'S'),
(115, 5, 'Fer', '', '24/09/2017 a las 02:19 PM', '27/09/2017 a las 11:29 PM', '1506572990', 'S', 'S'),
(113, 5, 'Sam', '', '24/09/2017 a las 02:19 PM', '27/09/2017 a las 11:29 PM', '1506572991', 'S', 'S'),
(133, 1, 'Cosmo', '', '07/09/2017 a las 09:12 AM', '07/09/2017 a las 09:12 AM', '1504793539', 'N', 'S'),
(100, 5, 'Dan', '', '09/09/2017 a las 11:42 AM', '21/09/2017 a las 10:40 PM', '1506051608', 'S', 'S'),
(1, 106, 'Hola', 'Kejsjdisbqjd', '05/09/2017 a las 08:21 AM', '05/09/2017 a las 08:21 AM', '1504617680', 'N', 'S'),
(146, 1, 'Mames', 'CD', '02/10/2017 a las 11:15 AM', '05/10/2017 a las 08:41 AM', '1507210911', 'S', 'S'),
(132, 1, 'Dan', 'github.com/MezcalDev/LaFactura_Android/pull/24', '05/09/2017 a las 10:39 AM', '05/09/2017 a las 02:28 PM', '1504639704', 'N', 'S'),
(104, 1, 'Teddy', '__________,-~-. _.–._.-~-,\n_________/ .- ,’_______`-. \\\n_________\\ /`__________\\’/\n_________ /___’a___a`___\\\n_________|____,’(_)`.____ |\n_________\\___( ._|_. )___ /\n__________\\___ .__,’___ /\n__________.-`._______,’-.__\n________,’__,’___`-’___`.__`.\n_______/___/____V_____\\___\\_\n_____,’____/_____o______\\___`.__\n___,’_____|______o_______|_____`.\n__|_____,’|______o_______|`._____|\n___`.__,’_.-\\_____o______/-._`.__,’\n_________/_`.___o____,’__\\_\n__.”"-._,’______`._:_,’_______`.,-”"._\n_/_,-._`_______)___(________’_,-.__\\\n(_(___`._____,’_____`.______,’___)_)\n_\\_\\____\\__,’________`.____/.___/_/\n', '23/07/2017 a las 08:35 PM', '23/07/2017 a las 08:35 PM', '1500860136', 'N', 'S'),
(103, 1, 'Guitarra', '_____________#####\n_____________####\n_____________###\n_____________###\n_____________###\n_____________###\n_____________###\n_____________###\n_____________###__##\n_____________###__#__#\n_____________###___#__#\n_____________###___#___#\n_____________###___#____#\n_______##____###__#____#\n______#__#__######____#\n______#___##_____#____###\n_______#____#####____##\n________#___________####\n_________#_________###\n_________##_______###\n________##_________###\n_______##___________###\n______##______##_____##\n_____##_____#_____#___##\n_____##______####_____##\n_____###________________##\n______##_______________###\n_______###____________###\n_________####________###\n___________#########', '23/07/2017 a las 08:30 PM', '23/07/2017 a las 08:31 PM', '1500859876', 'S', 'S'),
(86, 1, 'Eres Lo Mas Importante De Mi Vida', '3bebujeej', '20/06/2017 a las 3:21 a.m', '21/06/2017 a las 7:00 p.m', '1498089606', 'S', 'S'),
(102, 1, 'Flower', ':::: , : – -:- – : , :::::\n:::: ) ` – : : – `(::::::\n::: / : : : :`\\: : :\\ ::::\n:::( : : : : : |: : :) ::\n::::\\ :: : : :/ : : / ::::\n:::::: `=(:/:)=` :::::\n:::::::: `-;`;-’ ::::::::\n::::::::::: || :: , ::::::\n::::::::::: ||_-’| ::::::\n::::::: |’-: || : / ::::::\n::::::::\\ : || :’ / ::::::\n::::::: \\ : || : /:::::::\n::::::::: \\:|| : /::::::\n::::::::: \\:||:/ :::::::\n:::::::::::\\||/:::::::::\n::::::::::::|| :::::::::\n::::::::::::|| :::::::::\n::::::::::::|| :::::::::\n::::::::::::|| :::::::::\n::::::::::::||:::::::::: ', '23/07/2017 a las 08:28 PM', '23/07/2017 a las 08:31 PM', '1500859873', 'S', 'S'),
(97, 1, 'Notas', 'Verificar cual es el maximo ID de nota sincronizada y usarlo como tope (permitiendo asi a la sync añadir nuevas notas)\r\nDesde tope hasta fin construimos otro JSON con todas las notas\r\n''Riatota''\r\nPerrazo\r\nServidor\nP', '28/06/2017 a las 01:31 AM', '15/07/2017 a las 01:41 AM', '1500100863', 'N', 'S'),
(100, 1, 'Ranas', 'vvv rrr\nvv vrrr\nvvrv rr\nvvrvr r\nvvr rvr\nv rvrvr\n vrvrvr\nrv vrvr\nrvrv vr\nrvrvrv \nrvrvr v\nrvr rvv\nr rvrvv\nrr vrvv\nrrrv vv\nrrr vvv', '22/07/2017 a las 01:22 PM', '22/07/2017 a las 02:17 PM', '1500751021', 'S', 'S'),
(99, 1, 'Nosesi', 'Por eso yo la amo', '15/07/2017 a las 01:03 AM', '26/07/2017 a las 12:10 AM', '1501045821', 'N', 'S'),
(101, 1, 'Dolphin', '__________________ ##\n_________________###*\n______________.*#####\n_____________*######\n___________*#######\n__________*########.\n_________*#########.\n_________*#######*##*\n________*#########*###\n_______*##########*__*##\n_____*###########_____*\n____############\n___*##*#########\n___*_____########\n__________#######\n___________*######\n____________*#####*\n______________*####*\n________________*####\n__________________*##*\n____________________*##\n_____________________*##.\n____________________.#####.\n_________________.##########\n________________.####*__*####\n', '23/07/2017 a las 08:26 PM', '23/07/2017 a las 08:31 PM', '1500859872', 'S', 'S'),
(129, 5, 'Hillian', '', '27/09/2017 a las 05:15 PM', '27/09/2017 a las 11:29 PM', '1506572983', 'S', 'S'),
(101, 5, 'A', '', '24/09/2017 a las 12:52 AM', '24/09/2017 a las 12:52 AM', '1506232333', 'N', 'S'),
(106, 5, 'F', '', '24/09/2017 a las 02:18 PM', '24/09/2017 a las 02:18 PM', '1506280685', 'N', 'S'),
(102, 5, 'B', '', '24/09/2017 a las 02:17 PM', '24/09/2017 a las 02:17 PM', '1506280675', 'N', 'S'),
(103, 5, 'C', '', '24/09/2017 a las 02:17 PM', '24/09/2017 a las 02:17 PM', '1506280677', 'N', 'S'),
(104, 5, 'D', '', '24/09/2017 a las 02:18 PM', '24/09/2017 a las 02:18 PM', '1506280680', 'N', 'S'),
(105, 5, 'E', '', '24/09/2017 a las 02:18 PM', '24/09/2017 a las 02:18 PM', '1506280683', 'N', 'S'),
(125, 1, 'Doctos', 'Propuesta propia\nNecesito residentes (carta de la empresa)', '29/08/2017 a las 10:44 AM', '29/08/2017 a las 10:44 AM', '1504021494', 'N', 'S'),
(126, 1, 'Compra Amazon', 'Rastrillo\nCrema lubiderm\nMouse alambrico\n*Keyboard\n', '30/08/2017 a las 10:07 PM', '30/08/2017 a las 10:07 PM', '1504148834', 'N', 'S'),
(127, 1, 'Walmart', 'Acondicionador\nTapete\nEspejos\nPapel baño', '31/08/2017 a las 07:50 PM', '31/08/2017 a las 07:50 PM', '1504227049', 'N', 'S'),
(128, 1, 'Residencias Conceptos', 'REST\nPrincipios Solid\nMVP Android', '01/09/2017 a las 02:14 PM', '01/09/2017 a las 02:14 PM', '1504293264', 'N', 'S'),
(129, 1, 'MVP with Orientation Changes', 'code.tutsplus.com/tutorials/how-to-adopt-model-view-presenter-on-android--cms-26206', '03/09/2017 a las 12:11 AM', '03/09/2017 a las 12:11 AM', '1504415487', 'N', 'S'),
(136, 1, 'Improvements For This App', 'Use replace instead of delete table and reinsert all rows\nOn Login, after create NoSyncNotes JSON delete all notes (Only on Login and Sign Up)\nOn Close Session delete only synced notes\n-Try to use Swipe Refresh Layout\n-Add search function in nav bar\n-Try to add multi select on Notes Fragment\n\nTwo possibilities to explain why losing notes sometimes:\n1.- Local notes JSONs don''t go out complete (Only 1 sync is neccesary to lose notes completely)\n2.- Remote JSON doesn''t arrive complete (2 syncs are needed to delete notes completely)', '23/09/2017 a las 12:53 PM', '27/09/2017 a las 09:58 PM', '1506567492', 'N', 'S'),
(1, 110, 'Inpüúùūût', '', '21/09/2017 a las 09:49 PM', '21/09/2017 a las 09:49 PM', '1506048554', 'N', 'S'),
(106, 1, 'Me muero por ella :(', '', '24/07/2017 a las 01:01 AM', '24/07/2017 a las 01:27 AM', '1500877658', 'N', 'S'),
(107, 1, 'Poo', '', '24/07/2017 a las 01:02 AM', '24/07/2017 a las 01:02 AM', '1500876148', 'N', 'S'),
(108, 1, 'Pill', '', '24/07/2017 a las 01:03 AM', '24/07/2017 a las 01:03 AM', '1500876205', 'N', 'S'),
(109, 1, 'Yoooooo', '', '24/07/2017 a las 01:03 AM', '24/07/2017 a las 01:05 AM', '1500876305', 'N', 'S'),
(110, 1, 'Piillass', 'Jjjjjjjjjjjjjjj', '24/07/2017 a las 01:05 AM', '11/08/2017 a las 07:06 PM', '1502496400', 'N', 'S'),
(111, 1, 'Tal Vez ''\\\\ " \\ \\"  "  ^\\ \\', '', '24/07/2017 a las 01:18 AM', '28/07/2017 a las 01:37 AM', '1501223822', 'N', 'S'),
(147, 1, 'Idea Ahorrar Datos En Sincronización', '- Crear columna cambios_pendientes (S/N)\r\n- El JSON NotasSync solo deberá mandar notas con estatus cambios_pendientes = ''S''\r\n- Si se elimina una nota se deben guardar los IDs de notas eliminadas que ademas ya hayan sido sincronizadas (Las no sincronizadas no es importante llevar un registro de si se han eliminado o no)\r\n- Mandar otro parámetro que contenga un array con los IDs de notas que debe eliminar el servidor\r\n- Todas las notas que envié de vuelta el JSON del servidor deberan tener el estatus de cambios_pendientes = ''N''\r\n- Usar replace con todo lo que envie el servidor\r\n- Cuando el servidor responda se deberá eliminar el arreglo de IDs de notas eliminadas\r\n\r\n¿Porqué se eliminan las notas en el login?\r\nEsto se debe a que si se tienen notas locales por ejemplo (1, 2, 3) estas se enviaran al servidor, y si el usuario ya tiene notas (Suponiendo que empiezan sus IDs en 7), las notas (1, 2, 3) ocuparan los lugares (8, 9, 10) pero las notas (1, 2, 3) seguiran existiendo, por lo que habra registros duplicados y por eso se eliminan solo en el login. Despues ya no es necesario pues en futuras sincronizaciones ya se tendran los IDs correctos que estan en la base de datos remota', '02/10/2017 a las 11:06 PM', '02/10/2017 a las 11:07 PM', '1507003657', 'N', 'S'),
(1, 102, 'yy', '', '28/07/2017 a las 01:26 AM', '28/07/2017 a las 01:26 AM', '1501223175', 'N', 'S'),
(2, 102, 'asssdffg', '', '28/07/2017 a las 01:32 AM', '28/07/2017 a las 01:34 AM', '1501223685', 'N', 'S'),
(3, 102, 'assd', '', '28/07/2017 a las 01:34 AM', '28/07/2017 a las 01:34 AM', '1501223652', 'N', 'S'),
(1, 103, 'Mamadas :)', '', '28/07/2017 a las 02:05 AM', '28/07/2017 a las 02:05 AM', '1501225533', 'N', 'S'),
(2, 103, 'Pendejadas :/', '', '28/07/2017 a las 02:05 AM', '28/07/2017 a las 02:05 AM', '1501225526', 'N', 'S'),
(1, 104, 'Otro Pedo', 'Verga hasta tiene paea notas xD', '28/07/2017 a las 11:48 AM', '28/07/2017 a las 12:04 PM', '1501261487', 'N', 'S'),
(2, 104, 'kk', 'Ijfdbfbfnf', '28/07/2017 a las 11:52 AM', '28/07/2017 a las 12:04 PM', '1501261493', 'N', 'S'),
(3, 104, 'Nota sin título', 'QqFjfhhxhff', '28/07/2017 a las 11:53 AM', '28/07/2017 a las 12:10 PM', '1501261803', 'S', 'S'),
(4, 104, 'vtvtv', 'vvfvfvfvffvfvfvfvfvf', '28/07/2017 a las 11:59 AM', '28/07/2017 a las 12:00 PM', '1501261205', 'N', 'S'),
(121, 1, 'Sublime Text 3 Full', 'https://mega.nz/#!kA8h3Spa!2WtCECY5yLjBuqXnjmpbwaNCpYbveJBtOHrvCH0RO9Y\n', '14/08/2017 a las 01:55 PM', '21/08/2017 a las 12:11 AM', '1503292274', 'N', 'S'),
(122, 1, 'Volley Docs', 'www.smashingmagazine.com/2017/03/simplify-android-networking-volley-http-library\n', '15/08/2017 a las 12:11 AM', '01/09/2017 a las 02:15 PM', '1504293302', 'N', 'S'),
(118, 1, 'cdz', 'http:/www.facebook.com/112494728824787/videos/1549265325147713/', '30/07/2017 a las 10:56 PM', '30/07/2017 a las 11:35 PM', '1501475744', 'N', 'S'),
(123, 1, 'Jorge Jacobo Rendon', 'Jorge Macias Peraza\nEs el Coco\n\n', '16/08/2017 a las 03:54 PM', '20/08/2017 a las 11:29 PM', '1503289747', 'N', 'S'),
(137, 1, 'Pool', '', '23/09/2017 a las 03:40 PM', '23/09/2017 a las 04:59 PM', '1506203955', 'S', 'S'),
(119, 1, 'TS', 'collections@interdatesa.com\ncomsar_@hotmail.com\nElba5075\n\ncofesar@hotmail.com\nelba5077\navisofactura@hcmcollect.com\n\nApple ID\ncomsar_@hotmail.com && DonSera5075 \n\n', '01/08/2017 a las 02:10 PM', '22/11/2017 a las 06:55 PM', '1511398516', 'N', 'S'),
(120, 1, 'Mm', 'http://mega.nz/#!EhVmAZYT!3An3HaxUJlTTEYFVDDGoDKqc2otk6eUKHGitzJE5U8s', '10/08/2017 a las 03:40 PM', '10/08/2017 a las 03:40 PM', '1502397606', 'N', 'S'),
(2, 105, '2', 'Guay guay guay\\r\\nDhdkaldjdkskdkd', '06/08/2017 a las 08:07 PM', '06/08/2017 a las 08:08 PM', '1502068112', 'N', 'S'),
(1, 105, 'Notitukis', 'Hola Migue, esto esta muy guay.! :D', '06/08/2017 a las 07:56 PM', '06/08/2017 a las 08:05 PM', '1502067923', 'S', 'S'),
(109, 5, 'Tan', '', '24/09/2017 a las 02:18 PM', '27/09/2017 a las 11:14 PM', '1506572058', 'S', 'S'),
(108, 5, 'Cos', '', '24/09/2017 a las 02:18 PM', '27/09/2017 a las 11:14 PM', '1506572059', 'S', 'S'),
(1, 111, 'Poo', '', '23/09/2017 a las 01:45 PM', '23/09/2017 a las 01:45 PM', '1506192300', 'N', 'S'),
(124, 5, 'Si me', '', '26/09/2017 a las 06:29 PM', '27/09/2017 a las 11:29 PM', '1506572985', 'S', 'S'),
(125, 5, 'Falta el ruido', '', '26/09/2017 a las 06:29 PM', '27/09/2017 a las 11:29 PM', '1506572985', 'S', 'S'),
(1, 108, 'A', '', '20/09/2017 a las 10:37 PM', '20/09/2017 a las 10:37 PM', '1505965047', 'N', 'S'),
(2, 108, 'B', '', '20/09/2017 a las 10:37 PM', '20/09/2017 a las 10:37 PM', '1505965052', 'N', 'S'),
(3, 108, 'C', '', '20/09/2017 a las 10:37 PM', '20/09/2017 a las 10:37 PM', '1505965055', 'N', 'S'),
(1, 112, 'Hola', 'El correo ', '28/09/2017 a las 08:20 AM', '28/09/2017 a las 08:25 AM', '1506605104', 'N', 'S'),
(144, 1, 'Polm', '', '23/09/2017 a las 04:17 PM', '23/09/2017 a las 04:59 PM', '1506203950', 'S', 'S'),
(110, 5, 'Sec', '', '24/09/2017 a las 02:19 PM', '27/09/2017 a las 11:14 PM', '1506572058', 'S', 'S'),
(148, 1, 'My Google Maps API Key', 'AIzaSyB_NHavuVVw0WwMt4Oo8iOThVnMau3ONsc\nAIzaSyBMQHAMJwNj3gDeyw0ixRSw_BaliyZNtZo\r\nPOPO', '04/10/2017 a las 09:24 AM', '07/10/2017 a las 02:58 PM', '1507406322', 'N', 'S'),
(149, 1, 'Swift Help', 'import Foundation\r\n\r\nfunc getGoogleMapsAPIUrl(address: String) -> String{\r\n	/*Example: = https://maps.googleapis.com/maps/api/geocode/json?address=Tecnológico%20de%20Morelia+Morelia+Michoacán+México&key=AIzaSyB_NHavuVVw0WwMt4Oo8iOThVnMau3ONsc*/\r\n	let MAPS_API_KEY : String = "AIzaSyB_NHavuVVw0WwMt4Oo8iOThVnMau3ONsc"\r\n	let GoogleMapsAPIURL: String = "https://maps.googleapis.com/maps/api/geocode/json?"\r\n	/*The method folding is used to remove diacritics (í, ó , ú)of String*/\r\n	return GoogleMapsAPIURL+"address="+address.replacingOccurrences(of: ", " , with: "+").replacingOccurrences(of: " ", with: "%20").folding(options: .diacriticInsensitive, locale: .current)+"&key="+MAPS_API_KEY\r\n}\r\n\r\nfunc getFoursquareAPIUrl(latitude: String, longitude: String) -> String{\r\n	/*Example: = https://api.foursquare.com/v2/venues/search?v=20161016&ll=41.878114%2C%20-87.629798&client_id=SWP0JTQFJEUNYVQEJZFGYF0A0FRAB5CYGEJ52VW3RRAL0GRZ&client_secret=YGFBL0O00ATAZV424NGNFCXG13LQAEOHXKMU1LEKQQMUDHRK*/\r\n	let date = Date()\r\n	let formatter = DateFormatter()\r\n	formatter.dateFormat = "yyyyMMdd"\r\n	let FOURSQUARE_CLIENT_ID: String = "SWP0JTQFJEUNYVQEJZFGYF0A0FRAB5CYGEJ52VW3RRAL0GRZ"\r\n	let FOURSQUARE_CLIENT_SECRET: String = "YGFBL0O00ATAZV424NGNFCXG13LQAEOHXKMU1LEKQQMUDHRK"\r\n	let FOURSQUARE_API_URL: String = "https://api.foursquare.com/v2/venues/search?"\r\n	let DATE_FORMATTED: String = formatter.string(from: date)\r\n	return FOURSQUARE_API_URL+"v="+DATE_FORMATTED+"&ll="+latitude+","+longitude+"&client_id="+FOURSQUARE_CLIENT_ID+"&client_secret="+FOURSQUARE_CLIENT_SECRET+"&limit=3"\r\n}\r\n\r\nlet var1: String = "papu", var2: String = "Prrrrrrro"\r\nprint("Hello world!")\r\nprint(var1+"-"+var2)\r\nprint(getGoogleMapsAPIUrl(address: "Tecnológico de Morelia, Morelia, Michoacán, México"))\r\nprint(getFoursquareAPIUrl(latitude: "19.7229386", longitude: "-101.1858201"))\r\n\n\n', '12/10/2017 a las 01:17 PM', '14/10/2017 a las 10:51 PM', '1508039476', 'S', 'S'),
(151, 1, 'contra2@@', '', '16/10/2017 a las 05:14 PM', '16/10/2017 a las 05:14 PM', '1508192051', 'N', 'S'),
(152, 1, 'http:/stackoverflow.com/questions/6441594/how-to-add-same-view-to-parent-multiple-times-by-inflating', '', '03/11/2017 a las 01:25 PM', '03/11/2017 a las 01:25 PM', '1509737134', 'N', 'S'),
(153, 1, 'da', 'f', '03/11/2017 a las 09:22 PM', '03/11/2017 a las 09:22 PM', '1509765762', 'N', 'S'),
(154, 1, 'a', 'd', '03/11/2017 a las 09:23 PM', '03/11/2017 a las 09:23 PM', '1509765790', 'N', 'S'),
(155, 1, 'Gastos Pinta', 'Coca                         $8\nGarrafones              36\nTacos.                       30\nQueso y jamon.       20', '04/11/2017 a las 04:34 PM', '11/11/2017 a las 11:06 AM', '1510420017', 'N', 'S');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE IF NOT EXISTS `usuarios` (
  `id_usuario` int(11) NOT NULL AUTO_INCREMENT,
  `correo` varchar(255) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(64) NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `correo` (`correo`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=113 ;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id_usuario`, `correo`, `username`, `password`) VALUES
(1, 'migue300995@gmail.com', 'Miguel Ángel', '95af18b8cfc0af0ba96f6d32af140dbb600fb008f69e99d0eb85796834e76202'),
(5, 'zeus@gmail.com', 'Zeus', '5667ef73dac709effcfdc518a98ea9238365ec62b0eb5f478392e28818a2efd5'),
(16, 'zeuss@gmail.com', 'Zeuss', '5667ef73dac709effcfdc518a98ea9238365ec62b0eb5f478392e28818a2efd5'),
(17, 'Prueba1@gmail.com', 'Ppp', '88d4266fd4e6338d13b845fcf289579d209c897823b9217da3e161936f031589'),
(18, 'cach@gmail.com', 'Cachorro', '3e6062f7d2a10dfd491f71151023730270e4a6a01d0fed78b4bf0e58e7294f29'),
(19, 'cachi@gmail.com', 'Cachi', '3e6062f7d2a10dfd491f71151023730270e4a6a01d0fed78b4bf0e58e7294f29'),
(20, 'cachorro@gmail.com', 'Cachorro', 'bfbbe09ccbab1f103c89ed99b714865876c8f5e371d4a8ca34484b1e3ecae033'),
(21, 'oo@gmail.com', 'Moppp', 'fc4f20873ab0199bdb6d4f0130aa139e0922dfb5b66fcc805ec968979934b48c'),
(22, 'muyo@gmail.com', 'Muyo', '37654417098191a63d7320f31a88c2da0f449e47ece73d696c5a08aab0a9be0a'),
(23, 'peus@gmail.com', 'Idont', '29c7a7a5d3ee0a7a94e3bd1b378c819cf4b489b62507a44ecfe9a99fc48d0817'),
(24, 'ui@g.com', 'Pooo', '27cac5503836765cd10751d27ab4a6e17d7a80d4c948430a5a81513973f9b51e'),
(97, 'plrr@gmail.com', 'Poui', '6ee54c6709617433a48d1eeb1148896e4218d64967d8ff703173ec049fab81cf'),
(98, 'poolas@gmai.com', 'poo', 'e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855'),
(99, 'pooooooo@gmailc.ocm', 'pooo', '45ce3df4402937aea2e1ca6980cbc8db2ad675eadb9ab3be78d7308943e33071'),
(100, 'tyyyyy@gmail.com', 'poool', 'ab08cb6db706dd7e83ad76d6873df7b1b2c4211a5d3c867498a8e79603a341f5'),
(101, 'pool@gmail.com', 'oo', 'ee13adb1f49db0019c1d7cf46d0bcae31c53f6980695e4ae2dce16082b035017'),
(102, 'tangente@gmail.com', 'tan', '27cac5503836765cd10751d27ab4a6e17d7a80d4c948430a5a81513973f9b51e'),
(103, 'uii@gmail.com', 'Y', '96067a7838cc36a258f11f457d4b0ca2d5fe60b0b92436116853d41eb832b603'),
(104, 'pene@pene.com', 'Pene', '8f3b507988b781a449ed6b25014bae1fcc1c8db5782650dd74f8b2ca3a8c1695'),
(105, 'sinuhegomez@hotmail.com', 'sinuuhe', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),
(106, 'aronyanez@outlook.com', 'Arón', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4'),
(107, 'hdh@gmal.com', 'Uiop', '65e84be33532fb784c48129675f9eff3a682b27168c0ea744b2cf58ee02337c5'),
(108, 'pato@gmail.com', 'Pool', '5667ef73dac709effcfdc518a98ea9238365ec62b0eb5f478392e28818a2efd5'),
(109, 'ppjbvcj@gmailc.com', '8jnbj', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4'),
(110, 'podo@gmail.com', 'Podó', 'e7496076758f1d162aed9e9b98b2e41499ad849f8413e9d28d5382246dd50799'),
(111, 'poo@gmail.com', 'Poou', '2b04b1b979e22449b0ab97bc69786d8c9ed7cfc29ab9af1e8ad6757e53b74407'),
(112, 'a@outlook.com', 'Aron', '8ee779a5d189d04e3aa6fb4eaa3a62803cb86f3fe06b59b729b6654880b3720c');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
