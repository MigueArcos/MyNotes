<?php
	include_once '../conexion.php';
	//$json = $_POST["notasJSON"];

	//Decode JSON into an Array
	//$data = json_decode($json);
	extract($_POST);
	$TotalNumberOfNotes=0;
	$RespuestaJSON= array();
	if (isset($NotasSyncJSON)){
		$NotasSync=json_decode($NotasSyncJSON);
		//echo $NotasSync;
		CompararNotas();
		//echo $ID_Inicio_Insertar;
	}
	RecuperarNotas();
	if (isset($NotasNoSyncJSON)){
		$NotasNoSync=json_decode($NotasNoSyncJSON);
		//echo $NotasNoSync;
		InsertarNuevasNotas();
	}
	
	$conn->close();
	//Post JSON response back to Android Application
	$RespuestaJSON[]=array("TotalNumberOfNotes"=> $TotalNumberOfNotes, "UltimoIDSync"=>$UltimoIDSync);
	echo json_encode($RespuestaJSON);
	
	/*foreach($RespuestaJSON as $array){
     echo $array->id_nota."			";
     echo $array->titulo."\n";
	}*/
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
					//echo "Entrada";
					$id_nota=$NotasSync[$i]->id_nota;
					$titulo=$NotasSync[$i]->titulo;
					$contenido=$NotasSync[$i]->contenido;
					$fecha_modificacion=$NotasSync[$i]->fecha_modificacion;
					$fecha_modificacion_orden=$NotasSync[$i]->fecha_modificacion_orden;
					$eliminado=$NotasSync[$i]->eliminado;
					$stmt->bind_param("sssssi", $titulo, $contenido, $fecha_modificacion, $fecha_modificacion_orden,$eliminado, $id_nota);
					$stmt->execute();
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
		//echo $i;
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
	$SQLMultiInsert="INSERT INTO notas (id_nota,id_usuario, titulo, contenido, fecha_creacion, fecha_modificacion, fecha_modificacion_orden, eliminado, subida) VALUES ";
	$TotalNumberOfNotes+=count($NotasNoSync);
	//Se prepara la sentencia
	//$stmt = $conn->prepare("INSERT INTO notas (id_nota,id_usuario, titulo, contenido, fecha_creacion, fecha_modificacion, fecha_modificacion_orden, eliminado, subida) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'S')");


	//Se dejara de usar la sentencia preparada debido a que por alguna razon los registros no se insertan en orden, se cambiara esto por la sentencia multi-insert de MySQL que se supone que es mas eficiente y que si inserta en orden
	for ($i=0; $i<count($NotasNoSync); $i++){
		$NotasNoSync[$i]->id_nota=$UltimoIDSync+1;
		$id_nota=$NotasNoSync[$i]->id_nota;
		//$NotasNoSync[$i]->titulo=str_replace("'", "''",$NotasNoSync[$i]->titulo);
		//$NotasNoSync[$i]->contenido=str_replace("'", "''",$NotasNoSync[$i]->contenido);
		$titulo=$conn->real_escape_string($NotasNoSync[$i]->titulo);
		$contenido=$conn->real_escape_string($NotasNoSync[$i]->contenido);
		$fecha_creacion=$NotasNoSync[$i]->fecha_creacion;
		$fecha_modificacion=$NotasNoSync[$i]->fecha_modificacion;
		$fecha_modificacion_orden=$NotasNoSync[$i]->fecha_modificacion_orden;
		$eliminado=$NotasNoSync[$i]->eliminado;
		$NotasNoSync[$i]->subida='S';
		$RespuestaJSON[]=$NotasNoSync[$i];

		//bind_param sirve para decirle a la sentencia de que tipo es cada parametro (i - integer, d - double, s - string, b - BLOB)
		//$stmt->bind_param("iissssss", $id_nota, $id_usuario, $titulo, $contenido, $fecha_creacion, $fecha_modificacion, $fecha_modificacion_orden, $eliminado);
		//$stmt->execute();

		$SQLMultiInsert.="(".$id_nota.",".$id_usuario.",'".$titulo."','".$contenido."','".$fecha_creacion."','".$fecha_modificacion."','".$fecha_modificacion_orden."','".$eliminado."','S'),";
		$UltimoIDSync++;
	}
	//$stmt->close();
	//Se elimina la coma del final
	$SQLMultiInsert=substr($SQLMultiInsert, 0, -1);
	//$SQL="INSERT INTO notas VALUES ($data[0]->id_nota, 1, '$data[0]->titulo', '$data[0]->contenido', '$data[0]->fecha_creacion', '$data[0]->fecha_modificacion', '$data[0]->fecha_modificacion_orden', '$data[0]->eliminado', 'S')";
	//echo $SQLMultiInsert;
	$conn->query($SQLMultiInsert);
	
	}
?>
