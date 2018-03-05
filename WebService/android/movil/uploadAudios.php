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
	/*
	$audioName = basename($_FILES["audio"]["name"]);
	$tmp = $_FILES['audio']['tmp_name'];
	echo $audiosPath.$audioName;
	if(move_uploaded_file($tmp, $audiosPath.$audioName)){
		echo "The file was uploaded successfully";
	}
	else{
		echo "Failed";	
	}
	*/
	echo count($_FILES["audios"]["name"]);

	for ($i = 0; $i < count($_FILES["audios"]["name"]); $i++){
		$audioName = basename($_FILES["audios"]["name"][$i]);
		$tmp = $_FILES['audios']['tmp_name'][$i];
		if(move_uploaded_file($tmp, $audiosPath.$audioName)){
			echo "The file ".$i." was uploaded successfully";
		}
		else{
			echo "There was a problem uploading the file".$i;
		}
	}

	if (file_exists($audiosPath."/voiceNotes.zip")) {
	    unlink($audiosPath."/voiceNotes.zip");
	} 

  	$zip = new ZipArchive;
	$zip->open($audiosPath."/voiceNotes.zip", ZipArchive::CREATE);
	foreach (glob($audiosPath."/*") as $file) {
	    $zip->addFile($file, basename($file));
	    //if ($file != $audiosPath."/important.txt") unlink($file);
	}
	$zip->close();
?>