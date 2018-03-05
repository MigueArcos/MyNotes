<?php
	include("../conexion.php");
	extract($_POST);

	$stmt = $conn->prepare("INSERT INTO usuarios VALUES (null, ?, ?, SHA2(?,256))");
	$stmt->bind_param("sss", $email, $username, $password);
	/*$result = $stmt->get_result(); //$result is of type mysqli_result
	$jsondata=array();
	if ($result->num_rows>0){
		$fila=$result->fetch_object();
		$jsondata['username']=$fila->username;
		$jsondata['id_usuario']=$fila->id_usuario;
		$jsondata['respuesta']="Encontrado";
		echo json_encode($jsondata, JSON_UNESCAPED_UNICODE);
	}
	else{
		echo "No encontrado";
	}
	$stmt->close();*/
	if ($stmt->execute()){
		$SQL="SELECT * FROM usuarios WHERE id_usuario=(SELECT MAX(id_usuario) FROM usuarios)";
		$resultado=$conn->query($SQL)->fetch_object();
		$jsondata=array();
		$jsondata['username']=$resultado->username;
		$jsondata['id_usuario']=$resultado->id_usuario;
		$jsondata['email']=$resultado->correo;
		//Para que PHP respete los acentos
		echo json_encode($jsondata, JSON_UNESCAPED_SLASHES);
	}
	else{
		echo "Email repetido";
	}
	$stmt->close();
	mysqli_close($conn);
?>