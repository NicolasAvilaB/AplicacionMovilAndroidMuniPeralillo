<?php
include('functions.php'); 
header("Content-Type: application/json; charset=UTF-8");
header("Content-Type: text/html;charset=utf-8");
$a=$_REQUEST['a'];
$b=$_REQUEST['b'];
$c=$_REQUEST['c'];
$d=$_REQUEST['d'];
$e=$_REQUEST['e'];
//date_default_timezone_set('America/Santiago');
//$horainicio=strftime("%H:%M:%S", time());
//$fechainicio=strftime("%Y-%m-%d", time() );
ejecutarSQLCommand("Call Crear_Alerta('$a','$b','$c','$d','$e')");
?>
