<?php
date_default_timezone_set('America/Mexico_City');
$Fecha_Servidor = new DateTime();
$timestamp=intval($Fecha_Servidor->getTimeStamp())+50;
//$timestamp=intval($Fecha_Servidor->getTimeStamp());
$Fecha_Servidor->setTimestamp($timestamp);
$fecha_hora=$Fecha_Servidor->format("d/m/Y")." a las ".$Fecha_Servidor->format("h:i A");
include("../conexion.php");
/*session_start();
$id_usuario=$_SESSION['id_usuario'];*/
extract($_POST);
$titulo_bd=$conn->real_escape_string($titulo_new);
$contenido_bd=$conn->real_escape_string($contenido_new);
$id_nota=$conn->query("SELECT MAX(id_nota)+1 AS id_n FROM notas WHERE id_usuario=$id_usuario")->fetch_object()->id_n;
$id_nota=($id_nota==NULL)? 1: $id_nota;
$SQL="INSERT INTO notas (id_nota,id_usuario, titulo, contenido, fecha_creacion, fecha_modificacion, fecha_modificacion_orden,eliminado, subida) VALUES ($id_nota,$id_usuario, '$titulo_bd','$contenido_bd','$fecha_hora','$fecha_hora','$timestamp', 'N','S')";
//Error de inserción
if ($conn->query($SQL)){
	echo "<li class='collection-item c-i$id_nota' data-id_nota='$id_nota' data-titulo='".htmlspecialchars($titulo_new, ENT_HTML5)."' data-contenido='".htmlspecialchars($contenido_new, ENT_HTML5)."'>
	        <div>".htmlspecialchars($titulo_new, ENT_HTML5)."
		        <a href='#!' class='secondary-content'>
		        <i class='material-icons'>info</i></a>
	        </div>
	        </li>";	
}
else{
	echo "Error";
}
mysqli_close($conn);
?>