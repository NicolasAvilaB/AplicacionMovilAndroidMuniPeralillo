<?php
$a=$_REQUEST['a'];
$b=$_REQUEST['b'];
$c=$_REQUEST['c'];
$d=$_REQUEST['d'];
$e=$_REQUEST['e'];
ini_set( 'display_errors', 1 );
            error_reporting( E_ALL );
            $from = "apkmuniperalillo@systemchile.com";
            $to = "alertas@muniperalillo.cl";
            $asunto = "Alerta ".$c." - Aplicación MuniPeralillo";
            $message = "Aviso de Alerta: <br>\n".
              "Se ha enviado un reporte sobre Alerta desde la Aplicación MuniPeralillo.<br>\n".
              "El usuario ".$b." realizó la alerta al correo electrónico.<br>\n". 
              "<br>\n".
              "<h3>Resumen de su Perfil: </h3>".
               "<br>\n".
                "Rut del Usuario:<br>\n".
                "$a<br>\n".
                "<br>\n".
                "Nombre: <br>\n".
                "$b<br>\n".
               "<br>\n".
                "Alerta:<br>\n".
                "$c<br>\n".
                "<br>\n".
                "Descripción: <br>\n".
                "$d<br>\n".
                "<br>\n".
                "Sector o Lugar: <br>\n".
                "$e<br>\n".
                "<br>\n".
              "Recuerda, para acceder a las opciones de la App solo Inicia Sesión<br>\n".
              "<br>\n".
              "-- OJO: NO RESPONDER ESTE MENSAJE AUTOMATICO --<br>\n".
                "<br>\n".
                "siguenos en nuestra Página Web: www.muniperalillo.cl <br>\n".
              "siguenos en Facebook: www.facebook.com/mperalillo/ <br>\n".
              "Atte:<br>\n".
              "Departamento Informática <br>\n".
              "Ilustre Municipalidad de Peralillo <br>\n".
            "<br>\n".
              "<img src='systemchile.com/MuniSocial/Imagenes/logomuni.png' width='100' height='140'>";
            $headers = "MIME-Version: 1.0\r\n".		//En el envio de cabeceras se usa el \r\n como salto de linea.
               "Content-type: text/html;charset=charset=UTF-8\r\n".
               "From: $from\r\n".				//Nota. Dirección del remitente. 
               "Reply-To: $from\r\n".			//Nota. Dirección de respuesta. //Creo que sobra.
               "Return-path: $from\r\n".		//Nota. Ruta del mensaje desde origen a destino.  //Creo que sobra.
               "\r\n";
            mail($to,$asunto,$message, $headers);

?>