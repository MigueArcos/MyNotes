<?php
	include_once '../conexion.php';
	extract($_POST);
	//echo $syncDataJson;
	$syncData = json_decode($syncDataJson);
	$newNotes = $syncData->newNotes;
	$modifiedNotes = $syncData->modifiedNotes;
	$syncInfo = $syncData->syncInfo;
	$idsToDelete = $syncData->idsToDelete;
	
	$lastSyncedId = $syncInfo->lastSyncedId;
	$userId = $syncInfo->userId;

	//print_r($syncInfo);
	$newServerNotesArray = fetchServerNotes();
	//In this point we already know which is the correct id to start inserting the remote notes on local database ($lastSyncedId)
	$syncInfo->lastSyncedId = $lastSyncedId;



	//$finalModificationsArray = createFinalModificationsArray();

	//print_r($finalModificationsArray);


	$returnJson = new stdClass();

	$finalModificationsArray = createFinalModificationsArray();

	if (count($finalModificationsArray) > 0){
		modifyNotes($finalModificationsArray);
		$returnJson->modifiedNotes = $finalModificationsArray;
	}

	if (count($newNotes) > 0){
		insertLocalNotes($lastSyncedId);
	}
	
	
	if (count($newServerNotesArray) > 0){
		$returnJson->newNotes = $newServerNotesArray;
	}

	if (count($idsToDelete) > 0){
		deleteLocalNotes($idsToDelete);
	}
	
	$returnJson->syncInfo = $syncInfo;
	echo json_encode($returnJson);


	function createArrayFromColumn($array, $columnName){
		$result = array();
		$size = count($array);
		for ($i=0; $i < $size; $i++){
			array_push($result, $array[$i]->$columnName);
		}
		return $result;
	}

	function deleteLocalNotes($idsToDelete){
		global $conn, $userId, $NOTES_TABLE_NAME, $NOTES_USER_ID, $NOTE_ID;
		
		foreach ($idsToDelete as $id){
			$conn->query("DELETE FROM $NOTES_TABLE_NAME WHERE $NOTE_ID = $id AND $NOTES_USER_ID = $userId");
		}

	}

	function fetchServerNotes(){
		//Recupera las notas que fueron creadas del lado del servidor (Aplicación web donde se insertaron inmediatamente)
		global $lastSyncedId, $conn, $userId;
		global $NOTE_ID, $NOTE_TITLE, $NOTE_CONTENT, $NOTE_CREATION_DATE, $NOTE_MODIFICATION_DATE, $NOTE_DELETED, $NOTE_UPLOADED, $NOTE_PENDING_CHANGES, $NOTES_TABLE_NAME, $NOTES_USER_ID;
		$newServerNotes = $conn->query("SELECT $NOTE_ID, $NOTE_TITLE, $NOTE_CONTENT, $NOTE_CREATION_DATE, $NOTE_MODIFICATION_DATE, $NOTE_DELETED, $NOTE_UPLOADED, $NOTE_PENDING_CHANGES FROM $NOTES_TABLE_NAME WHERE $NOTE_UPLOADED = 1 AND $NOTES_USER_ID = $userId AND $NOTE_ID > $lastSyncedId ORDER BY $NOTE_ID");
		$newServerNotesArray = array();
		while ($row = $newServerNotes->fetch_object()){
			$lastSyncedId = $row->$NOTE_ID;
			array_push($newServerNotesArray, $row);
		}
		return $newServerNotesArray;
	}

	function createFinalModificationsArray(){
		global $lastSyncedId, $conn, $userId, $modifiedNotes;
		global $NOTE_ID, $NOTE_TITLE, $NOTE_CONTENT, $NOTE_CREATION_DATE, $NOTE_MODIFICATION_DATE, $NOTE_DELETED, $NOTE_UPLOADED, $NOTE_PENDING_CHANGES, $NOTES_TABLE_NAME, $NOTES_USER_ID;

		$serverModifiedNotes = $conn->query("SELECT $NOTE_ID, $NOTE_TITLE, $NOTE_CONTENT, $NOTE_CREATION_DATE, $NOTE_MODIFICATION_DATE, $NOTE_DELETED, $NOTE_UPLOADED, $NOTE_PENDING_CHANGES  FROM $NOTES_TABLE_NAME WHERE $NOTES_USER_ID = $userId AND $NOTE_PENDING_CHANGES = 1 ORDER BY $NOTE_ID");
		
		$serverModifiedNotesArray = array();

		while ($row = $serverModifiedNotes->fetch_object()){
			array_push($serverModifiedNotesArray, $row);
		}
		//This array is gonna have the modifications of local notes and the remote changes, it will keeep unique ids by deleting the repeated ones in function of its modification date
		$finalModificationsArray = $modifiedNotes;
		$idsToModify = createArrayFromColumn($modifiedNotes, $NOTE_ID);
		
		foreach ($serverModifiedNotesArray as $serverNote) {
			//Index is a variable that tell us if the 2 arrays have the same id in one of its elements, in that case the final array stays with the element that has the bigger modification date
			$index = array_search($serverNote->$NOTE_ID, $idsToModify);
			if ($index === false){
				$finalModificationsArray[] = $serverNote;
			}
			else{
				if (intval($finalModificationsArray[$index]->$NOTE_MODIFICATION_DATE) < intval($serverNote->$NOTE_MODIFICATION_DATE)){
					$finalModificationsArray[$index] = $serverNote;
				}
			}
		}

		return $finalModificationsArray;
	}

	function modifyNotes($finalModificationsArray){
		global $conn, $userId;
		global $NOTE_ID, $NOTE_TITLE, $NOTE_CONTENT, $NOTE_CREATION_DATE, $NOTE_MODIFICATION_DATE, $NOTE_DELETED, $NOTE_UPLOADED, $NOTE_PENDING_CHANGES, $NOTES_TABLE_NAME, $NOTES_USER_ID;

		$updateSentence = $conn->prepare("UPDATE $NOTES_TABLE_NAME SET $NOTE_TITLE = ?, $NOTE_CONTENT = ?, $NOTE_MODIFICATION_DATE = ?,  $NOTE_DELETED = ?, $NOTE_UPLOADED = 1, $NOTE_PENDING_CHANGES = 0 WHERE $NOTE_ID = ? AND $NOTES_USER_ID = $userId");

		foreach ($finalModificationsArray as $modifiedItem){
			$updateSentence->bind_param("ssiii", $modifiedItem->$NOTE_TITLE, $modifiedItem->$NOTE_CONTENT, $modifiedItem->$NOTE_MODIFICATION_DATE, $modifiedItem->$NOTE_DELETED, $modifiedItem->$NOTE_ID);
			$updateSentence->execute();
		}

		$updateSentence->close();
	}

	function insertLocalNotes($lastSyncedId){
		//The last synced Id in this function is not gonna be the global one because we will only send the server notes in the response (This id will only work as an auxiliary id to know which is the correct id for the insertions of local notes)
		global $conn, $userId, $newNotes;
		global $NOTE_ID, $NOTE_TITLE, $NOTE_CONTENT, $NOTE_CREATION_DATE, $NOTE_MODIFICATION_DATE, $NOTE_DELETED, $NOTE_UPLOADED, $NOTE_PENDING_CHANGES, $NOTES_TABLE_NAME, $NOTES_USER_ID;

		$SQLMultiInsert="INSERT INTO $NOTES_TABLE_NAME ($NOTE_ID, $NOTES_USER_ID, $NOTE_TITLE, $NOTE_CONTENT, $NOTE_CREATION_DATE, $NOTE_MODIFICATION_DATE, $NOTE_DELETED, $NOTE_UPLOADED, $NOTE_PENDING_CHANGES) VALUES";
		for ($i = 0; $i < count($newNotes); $i++) {
			$newNotes[$i]->$NOTE_ID = $lastSyncedId + 1;

			$noteId = $newNotes[$i]->$NOTE_ID;
			$title = $conn->real_escape_string($newNotes[$i]->$NOTE_TITLE);
			$content = $conn->real_escape_string($newNotes[$i]->$NOTE_CONTENT);
			$creationDate = $newNotes[$i]->$NOTE_CREATION_DATE;
			$modificationDate = $newNotes[$i]->$NOTE_MODIFICATION_DATE;
			$deleted = $newNotes[$i]->$NOTE_DELETED;

			$newNotes[$i]->$NOTE_UPLOADED = 1;
			$newNotes[$i]->$NOTE_PENDING_CHANGES = 0;

			$rowToInsert = " ($noteId, $userId, '$title', '$content', $creationDate, $modificationDate, $deleted, 1, 0),";
			$SQLMultiInsert.=$rowToInsert;
			$lastSyncedId++;
		}
		$SQLMultiInsert = substr($SQLMultiInsert, 0, -1);

		$conn->query($SQLMultiInsert);
	}
?>