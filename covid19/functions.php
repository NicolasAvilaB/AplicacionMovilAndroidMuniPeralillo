<?php 
header("Content-Type: text/html;charset=utf-8");
header("Content-Type: application/json; charset=UTF-8");

function ejecutarSQLCommand($commando){
  $mysqli = new mysqli("localhost", "cre37351_root", "lacomunagana", "cre37351_appmunicipal");
  mysqli_set_charset($mysqli,"utf8");
/* check connection */
if ($mysqli->connect_error) {
    echo "Connect failed: %s\n", $mysqli->connect_error;
}
else{
}

if ( $mysqli->multi_query($commando)) {
     if ($resultset = $mysqli->store_result()) {
           mysqli_set_charset($mysqli,"utf8");
    	while ($row = $resultset->fetch_array(MYSQLI_BOTH)) {
    	}
    	$resultset->free();
     }
     echo json_encode(["0"], JSON_UNESCAPED_UNICODE); 
}else{
echo json_encode(["1"], JSON_UNESCAPED_UNICODE); 
}
$mysqli->close();
}

function getSQLResultSet($commando){
  $mysqli = new mysqli("localhost", "cre37351_root", "lacomunagana", "cre37351_appmunicipal");
  mysqli_set_charset($mysqli,"utf8");
/* check connection */
if ($mysqli->connect_errno) {
    printf("Connect failed: %s\n", $mysqli->connect_error);
    exit();
}
if ( $mysqli->multi_query($commando)) {
      mysqli_set_charset($mysqli,"utf8");
	return $mysqli->store_result();
}
$mysqli->close();
}
?>