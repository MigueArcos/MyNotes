<?php
	include_once '../conexion.php';
	extract($_POST);
	$RespuestaJSON=array();
	RecuperarNotas();
	$conn->close();
	echo json_encode($RespuestaJSON,JSON_UNESCAPED_SLASHES);


	function RecuperarNotas(){
		//Recupera las notas que fueron creadas del lado del servidor (Aplicación web donde se insertaron inmediatamente)
		global	$UltimoIDSync, $conn, $RespuestaJSON, $id_usuario,$TotalNumberOfNotes;
		$NuevasNotas=$conn->query("SELECT id_nota,titulo,contenido,fecha_creacion,fecha_modificacion,fecha_modificacion_orden,eliminado,subida  FROM notas WHERE subida='S' AND id_usuario=1 ORDER BY id_nota");
		while ($fila=$NuevasNotas->fetch_object()){
			$UltimoIDSync=$fila->id_nota;
			$RespuestaJSON[]=$fila;
			$TotalNumberOfNotes++;
		}
	}

?>
