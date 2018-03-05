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
				$SQL="SELECT * FROM notas ORDER by id_nota";
				if (isset($_GET['cons'])){
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


