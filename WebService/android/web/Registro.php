<?php
	include("../conexion.php");
	extract($_POST);
	$stmt = $conn->prepare("INSERT INTO usuarios VALUES (null, ?, ?, SHA2(?,256))");
	$stmt->bind_param("sss", $email, $username, $password);
	if ($stmt->execute()){
		$SQL="SELECT * FROM usuarios WHERE id_usuario=(SELECT MAX(id_usuario) FROM usuarios)";
		$resultado=$conn->query($SQL)->fetch_object();
		setcookie("id_usuario", $resultado->id_usuario, time()+86400*30, "/android/web");
		session_start();
		$_SESSION['id_usuario']=$resultado->id_usuario;
		$_SESSION['correo_elec']=$resultado->correo;
		$_SESSION['username']=$resultado->username;
	}
	else{
		echo "Email repetido";
	}
	$stmt->close();
	mysqli_close($conn);
?>