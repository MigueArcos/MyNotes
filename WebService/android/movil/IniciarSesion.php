<?php
	include("../conexion.php");
	extract($_POST);
	$stmt = $conn->prepare("SELECT * FROM usuarios WHERE correo=? AND password=SHA2(?,256)");
	$stmt->bind_param("ss", $email, $password);
	$stmt->execute();
	$stmt->bind_result($id_usuario,$correo,$username,$password); //$result is of type mysqli_result
	$jsondata=array();
	if ($stmt->fetch()){
		$jsondata['username']=$username;
		$jsondata['id_usuario']=$id_usuario;
		$jsondata['email']=$correo;
		$jsondata['respuesta']="Encontrado";
		//Para que PHP respete los acentos
		echo json_encode($jsondata, JSON_UNESCAPED_SLASHES);
	}
	else{
		echo "No encontrado";
	}
	$stmt->close();
	mysqli_close($conn);
?>