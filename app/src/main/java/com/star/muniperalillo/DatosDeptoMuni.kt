package com.star.muniperalillo

class DatosDeptoMuni constructor(depto: String, nombres: String, telefono: String, correo: String){
    var depto: String
    var nombres: String
    var telefono: String
    var correo: String
    init {
        this.depto = depto
        this.nombres = nombres
        this.telefono = telefono
        this.correo = correo
    }
}