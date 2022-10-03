<?php
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
ini_set( 'display_errors', 1 );
            error_reporting( E_ALL );
            $from = "apkmuniperalillo@systemchile.com";
            $to = "covid19@muniperalillo.cl";
            $asunto = "COVID19 Información - Aplicación MuniPeralillo";
            $message = "Aviso de Covid19 en la Comuna: <br>\n".
              "Se ha enviado un reporte con información sobre Covid19 desde la Aplicación MuniPeralillo.<br>\n".
              "El usuario ".$b." realizó el aviso y envió la información sobre Covid19.<br>\n". 
              "<br>\n".
              "<h3>Resumen de los Datos Recogidos: </h3>".
               "<br>\n".
                "Rut:<br>\n".
                "$a<br>\n".
                "<br>\n".
                "Nombre: <br>\n".
                "$b<br>\n".
               "<br>\n".
                "Email:<br>\n".
                "$c<br>\n".
                "<br>\n".
                "Teléfono: <br>\n".
                "$d<br>\n".
                "<br>\n".
                "Dirección: <br>\n".
                "$e<br>\n".
                "<br>\n".
                "Síntomas: <br>\n".
                "$f<br>\n".
                "<br>\n".
                "Dias con Síntomas: <br>\n".
                "$g<br>\n".
                "<br>\n".
                "Antecedentes de Salud: <br>\n".
                "$h<br>\n".
                "<br>\n".
                "Antecedentes de Contacto: <br>\n".
                "$i<br>\n".
                "<br>\n".
                "Comuna: <br>\n".
                "$j<br>\n".
                "<br>\n".
                "Paises Visitados: <br>\n".
                "$k<br>\n".
                "<br>\n".
                "Red de Apoyo: <br>\n".
                "$l<br>\n".
                "<br>\n".
                "Paciente Vive Con: <br>\n".
                "$m<br>\n".
                "<br>\n".
                "Examen Covid19: <br>\n".
                "$n<br>\n".
                "<br>\n".
                "Conocer la Ubicación: <br>\n".
                "$o<br>\n".
                "<br>\n".
                "Latitud: <br>\n".
                "$p<br>\n".
                "<br>\n".
                "Longitud: <br>\n".
                "$q<br>\n".
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