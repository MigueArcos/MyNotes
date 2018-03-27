<?php
	session_start();
	include_once '../conexion.php';
	if (!isset($_SESSION['id_usuario'])){
		if (isset($_COOKIE['id_usuario'])){
			//echo "putazo".$_COOKIE['id_usuario'];
			$id_cookie=$_COOKIE['id_usuario'];
			$user_data=$conn->query("SELECT * FROM usuarios WHERE id_usuario=$id_cookie")->fetch_object();
			$_SESSION['id_usuario']=$user_data->id_usuario;
			$_SESSION['correo_elec']=$user_data->correo;
			$_SESSION['username']=$user_data->username;
		}
		else{
			header("Location: login.php");
		}
	}
	extract($_SESSION);
?>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<!--Import Google Icon Font-->
	<link href="materialize/fuentes.css" rel="stylesheet">
	<!--Import materialize.css-->
	<link type="text/css" rel="stylesheet" href="materialize/css/materialize.min.css"  media="screen"/>
	<!--Let browser know website is optimized for mobile-->
	<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
	<title>Notas</title>
	<style>
	/*Hay que recordar que al añadirla clase fixed a la side-nav esta va a tener un width fijo de 300 px por eso hay que compensar ese contenido en pantallas de tamaño grande*/
		main{
      padding-left: 300px;
    }
    @media only screen and (max-width : 992px) {
      main{
        padding-left: 0;
      }
    }
	</style>
</head>
<body data-id_usuario="<?= $id_usuario ?>">
	<script type="text/javascript" src="materialize/js/jquery.js"></script>
	<script type="text/javascript" src="materialize/js/materialize.min.js"></script>
	<!--
	++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	|																								Estructura de la barra lateral												 											 |
	++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	-->
	<ul id="slide-out" class="side-nav fixed">
		<li><div class="user-view">
			<div class="background">
				<img src="images/ciudad.jpg" alt="Ciudad">
			</div>
			<a href="#!name"><span class="white-text name"><?= $_SESSION['username'] ?></span></a>
			<a href="#!email"><span class="white-text email"><?= $_SESSION['correo_elec'] ?></span></a>
		</div></li>
		<li><a href="#!" id="load_notes"><i class="waves-effect material-icons">note</i>Notas</a></li>
		<li><a href="#!" id="load_deleted_notes"><i class="waves-effect material-icons">delete</i>Notas eliminadas</a></li>
		<li><div class="divider"></div></li>
		<li><a href="CerrarSesion.php"><i class="waves-effect material-icons">exit_to_app</i>Cerrar sesión</a></li>
	</ul>
<main>
	<nav>
		<div class="nav-wrapper">
			<a href="#" class="brand-logo center">Notas :D</a>
			<a href="#" data-activates="slide-out" class="button-collapse"><i class="material-icons">menu</i></a>
		</div>
	</nav>
	<!--
	++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	|																								Estructura de la lista de notas												 											 |
	++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	-->
	<div id="notes_content">
		
	</div>
	
	<!--
	++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	|																							Estructura de la ventana modal												 												 |
	++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	-->
	<div id="modal1" class="modal">
		<div class="modal-content">
			<h4>Notas de MigueLópez :D</h4>
			<form class="form_notas col s12" action="insert_note.php" id="insert" enctype='application/json' method="POST">
				<div class="row">
					<div class="input-field col s12">
						<input id="titulo_new" name="titulo_new" type="text" class="validate">
						<label for="titulo_new">Título</label>
					</div>
				</div>
				<div class="row">
					<div class="input-field col s12">
						<textarea id="contenido_new" name="contenido_new" class="materialize-textarea"></textarea>
						<label for="contenido_new">Contenido</label>
					</div>
				</div>
				<div class="modal-footer">
					<input type="submit" class="modal-action modal-close waves-effect waves-green btn-flat" value="Enviar">
				</div>
			</form>
			<form class="col s12 form_notas" action="update_note.php" id="update" method="POST" hidden>
				<div class="row">
					<div class="input-field col s12">
						<input type="hidden" name="id_nota" id="id_nota">
						<input id="titulo" name="titulo" type="text" class="validate">
						<label for="titulo" class="active">Título</label>
					</div>
				</div>
				<div class="row">
					<div class="input-field col s12">
						<textarea id="contenido" name="contenido" class="materialize-textarea"></textarea>
						<label for="contenido" class="active">Contenido</label>
					</div>
				</div>
				<div class="modal-footer">
					<input type="submit" class="modal-action modal-close waves-effect waves-green btn-flat" value="Enviar">
				</div>
			</form>
		</div>
	</div>
</main>
<script>
	$(document).ready(function(){
		$("#notes_content").load("load_notes.php");
		$('.button-collapse').sideNav({
	      menuWidth: 300, // Default is 300
	      edge: 'left', // Choose the horizontal origin
	      closeOnClick: true, // Closes side-nav on <a> clicks, useful for Angular/Meteor
	      draggable: true, // Choose whether you can drag to open on touch screens,
	    }
	 );
		$('.modal').modal({
	      dismissible: true, // Modal can be dismissed by clicking outside of the modal
	      opacity: .5, // Opacity of modal background
	      inDuration: 300, // Transition in duration
	      outDuration: 200, // Transition out duration
	      startingTop: '4%', // Starting top style attribute
	      endingTop: '10%' // Ending top style attribute
	    }
	 );
	});
	$("#load_notes").click(function(){
		$("#notes_content").load("load_notes.php");
	});
	$("#load_deleted_notes").click(function(){
		$("#notes_content").load("load_deleted_notes.php",function(){
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
		});
	});
  

 
</script>
</body>
</html>