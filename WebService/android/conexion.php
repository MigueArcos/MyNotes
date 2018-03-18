<?php
	/*
	$servername = "localhost";
	$username = "root";
	$password = "";
	$dbname = "misnotas";
	*/
	
	$servername = "198.91.81.3";
	$username = "miguela6_migue";
	$password = "migue300995";
	$dbname = "miguela6_notas";
	
	//Notes table field names
	$NOTES_TABLE_NAME = "notes";
    $NOTE_ID = "note_id";
    $NOTES_USER_ID = "user_id";
    $NOTE_TITLE = "title";
    $NOTE_CONTENT = "content";
    $NOTE_CREATION_DATE = "creation_date";
    $NOTE_MODIFICATION_DATE = "modification_date";
    $NOTE_DELETED = "deleted";
    $NOTE_UPLOADED = "uploaded";
    $NOTE_PENDING_CHANGES = "pending_changes";
    

	$conn = mysqli_connect($servername, $username, $password, $dbname);
	mysqli_set_charset($conn,"utf8");
	

	/*
SELECT UNIX_TIMESTAMP(STR_TO_DATE(CONCAT( SUBSTRING(  '11/07/2017 a las 11:02 PM', 1, 10 ) ,  " ", SUBSTRING(  '11/07/2017 a las 11:02 PM', 18, 23 ) ), '%d/%m/%Y %H:%i')) AS TIMESTAMP

(SELECT CONCAT(SUBSTRING(fecha_creacion, 1, 10), " ", SUBSTRING(fecha_creacion, 18, 23)) FROM notes WHERE id_nota = 2 AND id_usuario = 17)


SELECT UNIX_TIMESTAMP( STR_TO_DATE(  'Apr 15 2012 12:00AM',  '%M %d %Y %h:%i%p' ) ) AS TIMESTAMP

SELECT UNIX_TIMESTAMP(STR_TO_DATE((SELECT CONCAT(SUBSTRING(fecha_creacion, 1, 10), " ", SUBSTRING(fecha_creacion, 18, 23)) FROM notes WHERE id_nota = 2 AND id_usuario = 17), '%d/%m/%Y %H:%i')) AS TIMESTAMP


UPDATE notes SET fecha_creacion2 = UNIX_TIMESTAMP( STR_TO_DATE( CONCAT( SUBSTRING( fecha_creacion, 1, 10 ) ,  " ", SUBSTRING( fecha_creacion, 18, 23 ) ) ,  '%d/%m/%Y %H:%i' ) )
*/



?>
