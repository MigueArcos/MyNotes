<?php
	session_start();
	setcookie("id_usuario", '', 1, "/android/web");
	session_destroy();
	header("Location: login.php");
?>