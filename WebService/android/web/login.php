<?php
  session_start();
  if (isset($_COOKIE['id_usuario'])){
    header("Location: index.php");
  }
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
	<meta name="theme-color" content="#757575">
	<title>Iniciar Sesión</title>
	<link rel="stylesheet" href="materialize/login.css" media="screen">
	<link rel="stylesheet" href="sweet-modal/min/jquery.sweet-modal.min.css" />
	<style>
		html, body {
		    height: 100%;
		}
		html {
		    display: table;
		    margin: auto;
		}
		body {
		    display: table-cell;
		    vertical-align: middle;
		}
	</style>
</head>
<!-- Original: http://demo.geekslabs.com/materialize-v1.0/user-login.html -->
<body class="cyan">
 
  <div id="login-page" class="row">
    <div class="col s12 z-depth-4 card-panel">
      <form class="login-form" action="IniciarSesion.php" method="POST">
        <div class="row">
          <div class="input-field col s12 center">
            <img src="images/login-logo.png" alt="" class="circle responsive-img valign profile-image-login">
            <p class="center login-form-text">Notas de MigueLópez :D</p>
          </div>
        </div>
        <div class="row">
          <div class="input-field col s12">
          <i class="material-icons prefix">email</i>
          <input id="email" type="email" name="email" class="validate" required>
          <label for="email" data-error="Formato inválido">Email</label>
        </div>
        </div>
        <div class="row margin">
          <div class="input-field col s12">
            <i class="material-icons prefix">lock</i>
            <input id="password" name="password" type="password" pattern="^.{4,}$" class="validate" required>
            <label for="password" data-error="Al menos 4 caracteres">Contraseña</label>
          </div>
        </div>
        <div class="row">
          <div class="input-field col s12">
            <input type="submit" class="btn waves-effect waves-light col s12" value="Iniciar sesión">
          </div>
        </div>
        <div class="row">
          <div class="input-field col s6 m6 l6">
            <p class="margin medium-small"><a href="page-register.html" id="change-to-signup">Crear cuenta</a></p>
          </div>
          <div class="input-field col s6 m6 l6">
              <p class="margin right-align medium-small"><a href="page-forgot-password.html">¿Olvidaste tu contraseña ?</a></p>
          </div>          
        </div>
      </form>


      <form class="registro-form" action="Registro.php" method="POST" hidden>
        <div class="row">
          <div class="input-field col s12 center">
            <img src="images/login-logo.png" alt="" class="circle responsive-img valign profile-image-login">
            <p class="center login-form-text">Notas de MigueLópez :D</p>
          </div>
        </div>
        <div class="row margin">
	          <div class="input-field col s12">
		          <i class="material-icons prefix">email</i>
		          <input id="email_r" name="email" type="email" class="validate" required>
		          <label for="email_r" data-error="Formato inválido">Email</label>
        		</div>
        </div>
        <div class="row margin">
          <div class="input-field col s12">
            <i class="material-icons prefix">person</i>
            <input id="username" name="username" type="text" class="validate" pattern="^.{1,}$" required>
            <label for="username" data-error="El nombre de usuario no puede estar vacio">Nombre de usuario</label>
          </div>
        </div>
        <div class="row margin">
          <div class="input-field col s12">
            <i class="material-icons prefix">lock</i>
            <input id="password_r" name="password" type="password" pattern="^.{4,}$" class="validate" required>
            <label for="password_r" data-error="Al menos 4 caracteres">Contraseña</label>
          </div>
        </div>
        <div class="row margin">
          <div class="input-field col s12">
            <i class="material-icons prefix">lock</i>
            <input id="password_rc" type="password" pattern="^.{4,}$" class="validate" required>
            <label for="password_rc" data-error="Al menos 4 caracteres">Confirmar contraseña</label>
          </div>
        </div>
        <div class="row">
          <div class="input-field col s12">
            <input type="submit" class="btn waves-effect waves-light col s12" value="Registrarme">
          </div>
        </div>
        <div class="row">
          <div class="input-field">
            <p class="margin medium-small center"><a href="#" id="change-to-login">Iniciar sesión</a></p>
          </div>         
        </div>
      </form>
    </div>
  </div>

	<script type="text/javascript" src="materialize/js/jquery.js"></script>
	<script type="text/javascript" src="materialize/js/materialize.min.js"></script>
	<script src="sweet-modal/min/jquery.sweet-modal.min.js"></script>
	<script>
		$("#change-to-login").click(function(evt){
			evt.preventDefault();
			$(".registro-form").hide();
			$(".login-form").show();
		});
		$("#change-to-signup").click(function(evt){
			evt.preventDefault();
			$(".registro-form").show();
			$(".login-form").hide();
		});
		$(".login-form").submit(function (evt){
			evt.preventDefault();
      var variables = $(this).serialize();
      var url=$(this).attr("action");
      $.ajax({
        type: "POST",
        url: url,
        data: variables,
        success: function(data) {
          if (data=="No encontrado"){
            $.sweetModal({
              title: 'Notas de MigueLópez :D',
              content: 'No se encontraron datos :(',
              icon: $.sweetModal.ICON_WARNING,
              theme: $.sweetModal.THEME_DARK
            });
          }
          else{
            window.location.href="index.php";
          }
        },
        fail: function( jqXHR, textStatus, errorThrown ) {
          if ( console && console.log ) {
              console.log( "La solicitud ha fallado: " +  textStatus+errorThrown+jqXHR);
          }
        }
      });
		});
		$(".registro-form").submit(function (evt){
			evt.preventDefault();
			if ($("#password_r").val()!=$("#password_rc").val()){
				$.sweetModal({
          title: 'Notas de MigueLópez :D',
					content: 'Las contraseñas no coinciden',
					icon: $.sweetModal.ICON_WARNING,
					theme: $.sweetModal.THEME_DARK
				});
				return;
			}
			var variables = $(this).serialize();
      var url=$(this).attr("action");
      $.ajax({
        type: "POST",
        url: url,
        data: variables,
        success: function(data) {
          if (data=="Email repetido"){
            $.sweetModal({
              title: 'Notas de MigueLópez :D',
              content: 'Ese email ya ha sido registrado anteriormente, por favor inicia sesión :)',
              icon: $.sweetModal.ICON_WARNING,
              theme: $.sweetModal.THEME_DARK
            });
          }
          else{
            window.location.href="index.php";
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
</body>
</html>
