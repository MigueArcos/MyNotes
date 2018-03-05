<?php
date_default_timezone_set('America/Mexico_City');
$Fecha_Servidor = new DateTime();
$timestamp=intval($Fecha_Servidor->getTimeStamp())+50;
//$timestamp=intval($Fecha_Servidor->getTimeStamp());
$Fecha_Servidor->setTimestamp($timestamp);
$fecha_hora=$Fecha_Servidor->format("d/m/Y")." a las ".$Fecha_Servidor->format("h:i A");
include("../conexion.php");
extract($_POST);
$titulo_bd=$conn->real_escape_string($titulo);
$contenido_bd=$conn->real_escape_string($contenido);
$SQL="UPDATE $NOTES_TABLE_NAME SET $NOTE_TITLE='$titulo_bd', $NOTE_CONTENT='$contenido_bd', $NOTE_MODIFICATION_DATE=$timestamp WHERE $NOTE_ID=$id_nota AND $NOTES_USER_ID=$id_usuario";
if ($conn->query($SQL)){
	echo "<li class='collection-item c-i$id_nota' data-id_nota='$id_nota' data-titulo='".htmlspecialchars($titulo_bd, ENT_HTML5)."' data-contenido='".htmlspecialchars($contenido_bd, ENT_HTML5)."'>
	        <div>".htmlspecialchars($titulo_bd, ENT_HTML5)."
		        <a href='#!' class='secondary-content'>
		        <i class='material-icons'>info</i></a>
	        </div>
	        </li>";	
}
//Error de inserción
else{
	echo "Error";
}
mysqli_close($conn);
?>