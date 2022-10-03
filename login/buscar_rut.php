<?php
header("Content-Type: application/json; charset=UTF-8");
header("Content-Type: text/html;charset=utf-8");
include('functions.php'); 
$a=$_REQUEST['a'];
//date_default_timezone_set('America/Santiago');
//$horainicio=strftime("%H:%M:%S", time());
//$fechainicio=strftime("%Y-%m-%d", time() );
if ($resultset = getSQLResultSet("Call Buscar_Rut_Alerta('".$a."')")) {
    while ($row = $resultset->fetch_array(MYSQLI_NUM)) {
        //utf8_decode($row);
        echo json_encode($row, JSON_UNESCAPED_UNICODE);
        //para ver problemas igual utilizar var_dump($row);
    }	
}
?>
