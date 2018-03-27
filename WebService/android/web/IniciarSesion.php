<?php
	include("../conexion.php");
	extract($_POST);
	$stmt = $conn->prepare("SELECT * FROM usuarios WHERE correo=? AND password=SHA2(?,256)");
	$stmt->bind_param("ss", $email, $password);
	$stmt->execute();
	$stmt->bind_result($id_usuario,$correo,$username,$password); //$result is of type mysqli_result
	$jsondata=array();
	if ($stmt->fetch()){
		setcookie("id_usuario", $id_usuario, time()+86400*30, "/android/web");
		session_start();
		$_SESSION['id_usuario']=$id_usuario;
		$_SESSION['correo_elec']=$correo;
		$_SESSION['username']=$username;
	}
	else{
		echo "No encontrado";
	}
	$stmt->close();
	mysqli_close($conn);
?>