<ul class="collection" id="notas">
<?php
include_once '../conexion.php';
session_start();
extract($_SESSION);
$SQL="SELECT * FROM notas WHERE id_usuario=$id_usuario AND eliminado='S' ORDER BY fecha_modificacion_orden DESC";
$resultado=$conn->query($SQL);
while ($fila=$resultado->fetch_object()){ 
?>
<li class="collection-item c-i<?= $fila->id_nota ?>" data-id_nota="<?= $fila->id_nota ?>" data-titulo="<?= htmlspecialchars($fila->titulo, ENT_QUOTES) ?>" data-contenido="<?= htmlspecialchars($fila->contenido, ENT_QUOTES) ?>">
<div><?= htmlspecialchars($fila->titulo, ENT_QUOTES) ?>
<a href="#!" class="secondary-content">
<i class="material-icons">info</i></a>
</div>
</li>		
<?php
} 
$resultado->free();
$conn->close();
?>
</ul>