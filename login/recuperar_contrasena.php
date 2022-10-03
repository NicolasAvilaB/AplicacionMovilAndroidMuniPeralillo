<?php
include('functions.php');
header("Content-Type: application/json; charset=UTF-8");
header("Content-Type: text/html;charset=utf-8");
$a=$_REQUEST['a'];
if ($resultset = getSQLResultSet("Call Buscar_Clave('".$a."')")) {
                while ($row = $resultset->fetch_array(MYSQLI_NUM)) {
                    $var = $row['0'];
                    $var2 = $row['1'];
        ini_set( 'display_errors', 1 );
            error_reporting( E_ALL );
            $from = "apkmuniperalillo@systemchile.com";
            $to = $a;
            $asunto = "Recuperación de Contraseña - Aplicación MuniPeralillo";
            $message = "Estimado Usuario: <br>\n".
              "Se ha solicitado recuperación de la contraseña de tu Perfil de Usuario en la App MuniPeralillo<br>\n".
              "<br>\n".
              "Datos de Solicitud: <br>\n".
                "<br>\n".
                "Nombre Usuario: $var<br>\n".
                "Clave: $var2<br>\n".
                "<br>\n".
              "Recuerda, no des tu contraseña a terceros<br>\n".
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
        }
    }
?>