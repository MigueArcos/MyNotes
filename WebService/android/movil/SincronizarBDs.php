<?php
	include_once '../conexion.php';
	extract($_POST);
	$BDJSON=json_decode($JSONCompleto);
	$length=count($BDJSON)-1;
	$stmt = $conn->prepare("REPLACE INTO notas (id_nota,id_usuario, titulo, contenido, fecha_creacion, fecha_modificacion, fecha_modificacion_orden, eliminado, subida) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'S')");
	for ($i=0; $i<$length; $i++){
		$id_nota=$BDJSON[$i]->id_nota;
		//$BDJSON[$i]->titulo=str_replace("'", "''",$BDJSON[$i]->titulo);
		//$BDJSON[$i]->contenido=str_replace("'", "''",$BDJSON[$i]->contenido);
		$titulo=$BDJSON[$i]->titulo;
		$contenido=$BDJSON[$i]->contenido;
		$fecha_creacion=$BDJSON[$i]->fecha_creacion;
		$fecha_modificacion=$BDJSON[$i]->fecha_modificacion;
		$fecha_modificacion_orden=$BDJSON[$i]->fecha_modificacion_orden;
		$eliminado=$BDJSON[$i]->eliminado;

		//bind_param sirve para decirle a la sentencia de que tipo es cada parametro (i - integer, d - double, s - string, b - BLOB)
		$stmt->bind_param("iissssss", $id_nota, $id_usuario, $titulo, $contenido, $fecha_creacion, $fecha_modificacion, $fecha_modificacion_orden, $eliminado);
		$stmt->execute();
	}
	$conn->close();
?>
