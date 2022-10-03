<?php
header("Content-Type: application/json; charset=UTF-8");
header("Content-Type: text/html;charset=utf-8");
include('functions.php'); 
$a=$_REQUEST['a'];
$b=$_REQUEST['b'];
$c=$_REQUEST['c'];
$d=$_REQUEST['d'];
$e=$_REQUEST['e'];
$f=$_REQUEST['f'];
$g=$_REQUEST['g'];
$h=$_REQUEST['h'];
$i=$_REQUEST['i'];
$j=$_REQUEST['j'];
$k=$_REQUEST['k'];
$l=$_REQUEST['l'];
$m=$_REQUEST['m'];
$n=$_REQUEST['n'];
$o=$_REQUEST['o'];
$p=$_REQUEST['p'];
$q=$_REQUEST['q'];
//date_default_timezone_set('America/Santiago');
//$horainicio=strftime("%H:%M:%S", time());
//$fechainicio=strftime("%Y-%m-%d", time() );
ejecutarSQLCommand("Call Crear_Registro('$a','$b','$c','$d','$e','$f','$g','$h','$i','$j','$k','$l','$m','$n','$o','$p','$q')");
?>
