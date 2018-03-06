<?php
	include_once '../conexion.php';
	extract($_POST);
	$TotalNumberOfNotes=0;
	$RespuestaJSON=array();
	if (isset($NotasSyncJSON)){
		$NotasSync=json_decode($NotasSyncJSON);
		CompararNotas();
	}
	RecuperarNotas();
	if (isset($NotasNoSyncJSON)){
		$NotasNoSync=json_decode($NotasNoSyncJSON);
		InsertarNuevasNotas();
	}
	$conn->close();
	//Post JSON response back to Android Application
	$RespuestaJSON[]=array("TotalNumberOfNotes"=> $TotalNumberOfNotes, "lastSyncedId"=>$UltimoIDSync);
	echo json_encode($RespuestaJSON,JSON_UNESCAPED_SLASHES);

	function CreateArrayFromColumn($Array, $Column_Name){
		$Result=array();
		$tam=count($Array);
		for ($i=0; $i<$tam; $i++){
			array_push($Result, $Array[$i]->$Column_Name);
		}
		return $Result;
	}
	function CompararNotas(){
		global	$NotasSync, $conn, $RespuestaJSON, $id_usuario, $UltimoIDSync, $TotalNumberOfNotes;
		$ids_notas=CreateArrayFromColumn($NotasSync,'id_nota');
		$stmt = $conn->prepare("UPDATE notas SET titulo=?, contenido=?, fecha_modificacion=?, fecha_modificacion_orden=?, eliminado=? WHERE id_nota=? AND id_usuario=$id_usuario");
		$notasServidor=$conn->query("SELECT id_nota,titulo,contenido,fecha_creacion,fecha_modificacion,fecha_modificacion_orden,eliminado,subida  FROM notas WHERE subida='S' AND id_usuario=$id_usuario AND id_nota<=$UltimoIDSync ORDER BY id_nota");
		$i=0;
		$TotalNumberOfNotes+=count($NotasSync);
		while ($fila=$notasServidor->fetch_object()){
			//echo $fila->id_nota."\n";
			if (in_array($fila->id_nota, $ids_notas)){
				//echo intval($NotasSync[$i]->fecha_modificacion_orden)." vs ".intval($fila->fecha_modificacion_orden)."<br>";
				if (intval($NotasSync[$i]->fecha_modificacion_orden)>intval($fila->fecha_modificacion_orden)){
					$RespuestaJSON[]=$NotasSync[$i];
				}
				else{
					//echo "Tronco";
					$RespuestaJSON[]=$fila;
				}
				$i++;
				$UltimoIDSync=$fila->id_nota;
			}
			else{
				$DeleteSQL="DELETE FROM notas WHERE id_nota=".$fila->id_nota." AND id_usuario=$id_usuario";
				$conn->query($DeleteSQL);
			}
		}
		$stmt->close();
	}
	function RecuperarNotas(){
		//Recupera las notas que fueron creadas del lado del servidor (Aplicación web donde se insertaron inmediatamente)
		global	$UltimoIDSync, $conn, $RespuestaJSON, $id_usuario,$TotalNumberOfNotes;
		$NuevasNotas=$conn->query("SELECT id_nota,titulo,contenido,fecha_creacion,fecha_modificacion,fecha_modificacion_orden,eliminado,subida  FROM notas WHERE subida='S' AND id_usuario=$id_usuario AND id_nota>$UltimoIDSync ORDER BY id_nota");
		while ($fila=$NuevasNotas->fetch_object()){
			$UltimoIDSync=$fila->id_nota;
			$RespuestaJSON[]=$fila;
			$TotalNumberOfNotes++;
		}
	}
	function InsertarNuevasNotas(){
		global	$NotasNoSync, $conn, $RespuestaJSON, $id_usuario, $UltimoIDSync,$TotalNumberOfNotes;
		$TotalNumberOfNotes+=count($NotasNoSync);
		for ($i=0; $i<count($NotasNoSync); $i++){
			$NotasNoSync[$i]->id_nota=$UltimoIDSync+1;
			$NotasNoSync[$i]->subida='S';
			$RespuestaJSON[]=$NotasNoSync[$i];
			$UltimoIDSync++;
		}
	}
?>
