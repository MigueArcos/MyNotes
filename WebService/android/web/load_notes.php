<ul class="collection" id="notas">
<?php
include_once '../conexion.php';
session_start();
extract($_SESSION);
$SQL="SELECT * FROM notas WHERE id_usuario=$id_usuario AND eliminado='N' ORDER BY fecha_modificacion_orden DESC";
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
<a class="right btn-floating btn-large waves-effect waves-light red" href="#modal1" id="fab"><i class="material-icons">add</i></a>
<script>
	//Se utiliza esta forma para el click listener ya que de lo contrario al añadir nuevos elementos al DOM estos ya no funciona su evento click listener pues dicho evento no queda attached a los nuevos DOM Elements
	$("#notas").on("click", ".collection-item", function(){
  	//No se porque tengo que reiniciar el formulario, pero de lo contrario aveces falla al actualiza el campo de contenido
  	document.getElementById('update').reset();
  	$("#insert").hide();
  	$("#update").show();
  	$("input[name=titulo]").val($(this).data('titulo'));
  	$("input[name=id_nota]").val($(this).data('id_nota'));
  	$("#contenido").text($(this).data('contenido'));
  	Materialize.updateTextFields();
  	$('#modal1').modal('open');
  });

	$("#fab").click(function(){
  	$("#insert").show();
  	$("#update").hide();
  });

  $("#insert").submit(function (event) {
  	event.preventDefault();
  	var data = $(this).serializeArray();
  	//console.log(data);
  	data.push({name:'id_usuario',value: $("body").data('id_usuario')});
  	//alert($.param(data));
  	var url=$(this).attr("action");
    	$.ajax({
        type: "POST",
        url: url,
        data: data,
        success: function(data) {
        	if (data=="Error"){
        		alert("Hubo un error en la consulta");
        	}
        	else{
        		var old_content=$("#notas").html();
				    $("#notas").html(data+old_content);
				    document.getElementById('insert').reset();
        	}
        },
        fail: function( jqXHR, textStatus, errorThrown ) {
        if ( console && console.log ) {
            console.log( "La solicitud ha fallado: " +  textStatus+errorThrown+jqXHR);
        }
    }
    });
  });


  $("#update").submit(function (event) {
  	event.preventDefault();
  	var data = $(this).serializeArray();
  	//console.log(data);
  	data.push({name:'id_usuario',value: $("body").data('id_usuario')});
  	//alert($.param(data));
  	var url=$(this).attr("action");
    	$.ajax({
        type: "POST",
        url: url,
        data: data,
        success: function(data) {
        	if (data=="Error"){
        		alert("Hubo un error en la consulta");
        	}
        	else{
        		//alert(data);
		    		$("#notas .c-i"+$("#id_nota").val()).replaceWith(data);
        	}
        },
        fail: function( jqXHR, textStatus, errorThrown ) {
        if ( console && console.log ) {
            console.log( "La solicitud ha fallado: " +  textStatus+errorThrown+jqXHR);
        }
    }
    });
  });
</script>