<?php
include('functions.php'); 
header("Content-Type: application/json; charset=UTF-8");
header("Content-Type: text/html;charset=utf-8");
$a=$_REQUEST['a'];
$b=$_REQUEST['b'];
$c=$_REQUEST['c'];
$c2=$_REQUEST['c2'];
$d=$_REQUEST['d'];
$e=$_REQUEST['e'];
$f=$_REQUEST['f'];
$g=$_REQUEST['g'];
//date_default_timezone_set('America/Santiago');
//$horainicio=strftime("%H:%M:%S", time());
//$fechainicio=strftime("%Y-%m-%d", time() );
ejecutarSQLCommand("Call Crear_Perfil_Usuario('$a','$b','$c','$c2','$d','$e','$f','$g')");
//ejecutarSQLCommand("insert into respaldo_login values ('$a');");
?>
