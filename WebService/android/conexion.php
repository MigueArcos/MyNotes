<?php
	
	$servername = "localhost";
	$username = "root";
	$password = "";
	$dbname = "notas";
	
	/*
	$servername = "198.91.81.3";
	$username = "miguela6_migue";
	$password = "migue300995";
	$dbname = "miguela6_notas";
	*/
	$conn = mysqli_connect($servername, $username, $password, $dbname);
	mysqli_set_charset($conn,"utf8");
	
	/*echo ($conn)? "Conexión establecida correctamente<br>" : "Hubo algún error al establecer la conexión<br>";
	$cc;
	a();
	b("puto");
	$p="tangente";
	b($p);
	function a(){
		echo "Mamadas<br>";
	}
	function b($p){
		echo "Mamadas2 $p $cc<br>";
	}
	$fecha=new DateTime();
	echo $fecha->getTimeStamp()."<br>";
	// An array that represents a possible record set returned from a database
	
$a = array(
  array(
    'id' => 5698,
    'first_name' => 'Peter',
    'last_name' => 'Griffin',
  ),
  array(
    'id' => 4767,
    'first_name' => 'Ben',
    'last_name' => 'Smith',
  ),
  array(
    'id' => 3809,
    'first_name' => 'Joe',
    'last_name' => 'Doe',
  )
);

$ids = array_column($a, 'id');
print_r($ids);
if (in_array(5698, $ids))
  {
  echo "Match found";
  }
else
  {
  echo "Match not found";
  }*/
?>