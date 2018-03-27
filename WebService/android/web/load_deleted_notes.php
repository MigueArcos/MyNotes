<ul class="collection" id="notas">
<?php
include_once '../conexion.php';
session_start();
extract($_SESSION);
$SQL="SELECT * FROM $NOTES_TABLE_NAME WHERE $NOTES_USER_ID=$id_usuario AND $NOTE_DELETED=1 ORDER BY $NOTE_MODIFICATION_DATE DESC";
$resultado=$conn->query($SQL);
while ($fila=$resultado->fetch_object()){ 
?>
<li class="collection-item c-i<?= $fila->$NOTE_ID ?>" data-id_nota="<?= $fila->$NOTE_ID ?>" data-titulo="<?= htmlspecialchars($fila->$NOTE_TITLE, ENT_QUOTES) ?>" data-contenido="<?= htmlspecialchars($fila->$NOTE_CONTENT, ENT_QUOTES) ?>">
<div><?= htmlspecialchars($fila->$NOTE_TITLE, ENT_QUOTES) ?>
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