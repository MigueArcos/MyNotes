<?php 

	//El arreglo $_FILES contiene todos los archivos subidos, el campo name de cada archivo contiene el nombre original con el que llegan al servidor y tmp_name hace referencia a un nombre temporal con el que se suben (un archivo .tmp), desde ese directorio temporal hay que recuperalas y subirlas a nuestro servior si asi se desea
	$audiosPath = '../audios/';

	if (!file_exists($audiosPath)) {
	    mkdir($audiosPath, 0755, true);
	}

	$userID = $_POST['userID'];

	$audiosPath.=$userID."/";

	if (!file_exists($audiosPath)) {
	    mkdir($audiosPath, 0755, true);
	}

	$audioName = basename($_FILES["audio"]["name"]);
	$tmp = $_FILES['audio']['tmp_name'];
	echo $audiosPath.$audioName;
	if(move_uploaded_file($tmp, $audiosPath.$audioName)){
		echo "The file was uploaded successfully";
	}
	else{
		echo "Failed";	
	}
  
?>