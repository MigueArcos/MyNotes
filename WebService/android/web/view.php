<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Visor de notas</title>
</head>
<body>
<?php
	echo 'Versión actual de PHP: ' . phpversion()."<br>";

	$response = file_get_contents('http://www.convert-unix-time.com/api?timestamp=now');
	$result = json_decode($response);
	var_dump($result);

	date_default_timezone_set('America/Mexico_City');
	$Fecha_Servidor = new DateTime();
	$Fecha_Servidor->setTimezone(new DateTimeZone('America/Mexico_City'));
	$timestamp = ($Fecha_Servidor->getTimestamp() + 30)*1000 - 6*60*60*1000 ;
	echo "<br>$timestamp<br>";
	if (1520486458561 < 1520486308000){
		echo "Server is bigger than local";
		
	}
	else{
		echo "Local is bigger than server";
	}
	?>
	<style>
		* {
		    font-family: Arial, Helvetica, sans-serif;
		}
		table {
			border-collapse: collapse;
			width: 100%;
		}

		th, td {
			text-align: left;
			padding: 8px;
			border: 1px solid black;
		}


		th {
			background-color: #4CAF50;
			color: white;
		}
	</style>
	<div style="overflow-x:auto;">
		<table>
			<?php
				include_once '../conexion.php';
				$userId = $_GET['userId'];
				$SQL="SELECT * FROM $NOTES_TABLE_NAME WHERE $NOTES_USER_ID = $userId ORDER BY $NOTE_ID";
				if (isset($_GET['query'])){
					$conn->query($_GET['cons']);
				}
				if ($resultado = $conn->query($SQL) ){

					/* Obtener la información del campo para todas las columnas */
					$info_campo = $resultado->fetch_fields();
					$campos=0;
					echo "<tr>";
					foreach ($info_campo as $valor) {
						echo "<th>".$valor->name."</th>";
						$campos++;
					}
					echo "</tr>";
					$registros=$resultado->num_rows;
					while ($filas=$resultado->fetch_array()){
						echo "<tr>";
						for ($j=0; $j<$campos; $j++){
							echo "<td>".$filas[$j]."</td>";
						}
						echo "</tr>";
					}
					$resultado->free();
				}
				$conn->close();
			?>
		</table>
	</div>
</body>
</html>


