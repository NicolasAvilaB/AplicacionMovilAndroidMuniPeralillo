<?php
$a=$_REQUEST['a'];
$b=$_REQUEST['b'];
$c=$_REQUEST['c'];
$d=$_REQUEST['d'];
$e=$_REQUEST['e'];
$f=$_REQUEST['f'];
$g=$_REQUEST['g'];
$h=$_REQUEST['h'];
ini_set( 'display_errors', 1 );
            error_reporting( E_ALL );
            $from = "apkmuniperalillo@systemchile.com";
            $to = "vif@muniperalillo.cl";
            $asunto = "Aviso de Violencia - Aplicación MuniPeralillo";
            $message = "Alerta sobre Violencia en la Comuna: <br>\n".
              "Se ha enviado un reporte sobre Violencia desde la Aplicación MuniPeralillo.<br>\n".
              "El usuario ".$a." realizó un aviso urgente al correo electrónico.<br>\n".
              "<br>\n".
              "<h3>Resumen de Datos Entregados: </h3>".
               "<br>\n".
                "Nombre del Usuario:<br>\n".
                "$a<br>\n".
                "<br>\n".
                "Rut: <br>\n".
                "$b<br>\n".
               "<br>\n".
                "Email:<br>\n".
                "$c<br>\n".
                "<br>\n".
                "Teléfono: <br>\n".
                "+56 $d<br>\n".
                "<br>\n".
                "Dirección Perfil: <br>\n".
                "$e<br>\n".
                "<br>\n".
                "Abrir Localización: <br>\n".
                "https://maps.google.com/?q=".$f.",".$g."<br>\n".
                "<br>\n".
                "Dirección de GPS: <br>\n".
                "$h<br>\n".
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