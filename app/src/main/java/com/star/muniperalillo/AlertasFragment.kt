package com.star.muniperalillo

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.*
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import es.dmoral.toasty.Toasty
import java.io.IOException
import java.util.*

class AlertasFragment : Fragment(), DialogoConfirmacion.FinalizoCuadroDialogo {
    var lista_sectores = arrayOf("Calleuque - San Miguel De Calleuque", "Calleuque - Santa Blanca", "Calleuque - La Viroca", "Calleuque - Santa Victoria", "Los Cardos - Patria Nueva", "Los Cardos - El Barco", "Los Cardos - 21 de Mayo", "Rinconada de Molineros - Los Mayos", "Rinconada de Molineros - Rincón los Caceres", "Rinconada de Molineros - Rinconada de Molineros", "Mata Redonda - Molineros", "Puquillay - Puquillay", "Población - San Isidro La Bomba", "Población - Población", "Santa Ana - Santa Ana", "El Cortijo - El Cortijo", "Rinconada De Peralillo - Rinconada De Peralillo", "El Olivar - El Olivar", "Troya Sur - Troya Sur", "Parrones - Parrones", "Peralillo - Troya Centro", "Peralillo - Troya Norte", "Peralillo - Peralillo Centro", "Población - Calle Arturo Prat", "Peralillo - Peralillo", "Calleuque - Calleuque", "Los Cardos - Los Cardos")
    var root: View? = null
    var descr: EditText? = null
    var sector: AutoCompleteTextView? = null
    var enviar_informacion: Button? = null
    var enviar_violencia: Button? = null
    var llamada: Button? = null
    var preferences: SharedPreferences? = null
    var buscador_alert: Spinner? = null
    var mensaje_flotante_incorrecto: Context? = null
    var progressDialog_carga: ProgressDialog? = null
    var latitud: String? = null
    var longitud = "----------"
    var direccion = "----------"
    var varseleccion = 0
    var varseleccion2 = 0
    var alert: TextView? = null
    var mensaje_flotante_conf: Context? = null
    var valor = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_alertas, container, false)
        mensaje_flotante_conf = context
        buscador_alert = root!!.findViewById(R.id.buscador_alert)
        descr = root!!.findViewById(R.id.descr)
        alert = root!!.findViewById(R.id.alert)
        sector = root!!.findViewById(R.id.sector)
        enviar_informacion = root!!.findViewById(R.id.enviar_informacion)
        enviar_violencia = root!!.findViewById(R.id.enviar_violencia)
        llamada = root!!.findViewById(R.id.llamada)
        val adaptador_listasectores = ArrayAdapter(context!!, android.R.layout.simple_dropdown_item_1line, lista_sectores)
        sector!!.setThreshold(1)
        sector!!.setAdapter(adaptador_listasectores)
        mensaje_flotante_incorrecto = context
        spinner_alerta()
        llamada!!.setOnClickListener{
            when{
                ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED -> {
                    varseleccion2 = 3
                    val ms = DialogoConfirmacion(mensaje_flotante_conf, this@AlertasFragment)
                    ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
                    ms.contenido_mensaje.setText(R.string.act_ptelefono)
                }
                else -> { varseleccion2 = 4
                    val ms = DialogoConfirmacion(mensaje_flotante_conf, this@AlertasFragment)
                    ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
                    ms.contenido_mensaje.setText(R.string.llamar_seguridad_ciudadana)
                }
            }
        }
        enviar_informacion!!.setOnClickListener {
            val valor_descripcion = descr!!.getText().toString()
            val valor_sector = sector!!.getText().toString()
            val valor_alerta = buscador_alert!!.getItemAtPosition(buscador_alert!!.getSelectedItemPosition()).toString()
            when{
                valor_alerta == "Seleccione una Alerta..." -> {
                    val ms = DialogoIncorrecto(mensaje_flotante_incorrecto)
                    ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
                    ms.contenido_mensaje.setText(R.string.selecione_error)
                    buscador_alert!!.requestFocus()
                }
                valor_descripcion == "" -> {
                    val ms = DialogoIncorrecto(mensaje_flotante_incorrecto)
                    ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
                    ms.contenido_mensaje.setText(R.string.descri_error)
                    descr!!.requestFocus()
                }
                valor_sector == "" -> {
                    val ms = DialogoIncorrecto(mensaje_flotante_incorrecto)
                    ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
                    ms.contenido_mensaje.setText(R.string.sect_error)
                    sector!!.requestFocus()
                }
                else -> {
                    progressDialog_carga = ProgressDialog(context)
                    progressDialog_carga!!.dismiss()
                    progressDialog_carga!!.setTitle("Enviando Alerta")
                    progressDialog_carga!!.setMessage("Espere un Momento...")
                    progressDialog_carga!!.setCancelable(false)
                    progressDialog_carga!!.show()
                    ingresar_alerta()
                }
            }
        }
        enviar_violencia!!.setOnClickListener {
            varseleccion2 = 1
            val ms = DialogoConfirmacion(mensaje_flotante_conf, this@AlertasFragment)
            ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
            ms.contenido_mensaje.setText(R.string.pregunta_violencia)
        }
        val charactersForbiden0 = "!ΔΔ∆Δδ₲₡✓ÇüæęĘąǍǎČčĎďĚěǦǧȞȟǏǐ̌ǰǨǩĽľŇňǑǒŘřŠšṦṧŤťǓǔǙǚŽžǮǯĄłŁżŻĖČčėÆâôäöàòåûçùêÿëÖèÜï¢î£ì¥Ä₧Åƒ░▒▓│┤╡ª╢º╖¿╕⌐╣¬║½╗¼╝¡╜«╛»┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ËÌÎÏÐÒÔÕÖ×ØÙÛÜÞßàâãäåæçèêëìîïðòôõö÷øùûüþÿ¯°±²³´µ¶·¸¹º»¼½¾¿ÀÂÃÄÅĒēĀāǢǣĪīŌōŪūǕǖȲȳÆÇÈÊ’“”•–—˜™š›œžŸ¡¢£¤¥¦§¨©ª«¬®{|}~Δ€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘[\\]^_`=>?@:;<\"#$%&'()*+,-./"
        val filtro_descripcion = InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                when{
                    source != null && charactersForbiden0.contains("" + source) || !Character.isLetterOrDigit(source[i]) && !Character.isSpaceChar(source[i]) -> return@InputFilter ""
                }
            }
            null
        }
        val charactersForbiden1 = "!ΔΔ∆Δδ₲₡✓ÇüæęĘąǍǎČčĎďĚěǦǧȞȟǏǐ̌ǰǨǩĽľŇňǑǒŘřŠšṦṧŤťǓǔǙǚŽžǮǯĄłŁżŻĖČčėÆâôäöàòåûçùêÿëÖèÜï¢î£ì¥Ä₧Åƒ░▒▓│┤╡ª╢º╖¿╕⌐╣¬║½╗¼╝¡╜«╛»┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ËÌÎÏÐÒÔÕÖ×ØÙÛÜÞßàâãäåæçèêëìîïðòôõö÷øùûüþÿ¯°±²³´µ¶·¸¹º»¼½¾¿ÀÂÃÄÅĒēĀāǢǣĪīŌōŪūǕǖȲȳÆÇÈÊ’“”•–—˜™š›œžŸ¡¢£¤¥¦§¨©ª«¬®{|}~Δ€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘[\\]^_`=>?@:;<\"#$%&'()*+,./"
        val filtro_sector = InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                when {
                    source != null && charactersForbiden1.contains("" + source) && !Character.isSpaceChar(source[i]) -> return@InputFilter ""
                }
            }
            null
        }
        descr!!.setFilters(arrayOf(LengthFilter(250), filtro_descripcion))
        sector!!.setFilters(arrayOf(LengthFilter(70), filtro_sector))
        descr!!.setOnFocusChangeListener(object: View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                when {
                    !hasFocus -> {
                        descr!!.setTextColor(resources.getColor(R.color.blancopuro))
                        descr!!.setHintTextColor(resources.getColor(R.color.blancopuro))
                    }
                    else -> {
                        descr!!.setTextColor(resources.getColor(R.color.blancopuro))
                        descr!!.setHintTextColor(resources.getColor(R.color.blancopuro))
                    }
                }
            }
        })
        sector!!.setOnFocusChangeListener(object: View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                when{
                    !hasFocus -> {
                        sector!!.setTextColor(resources.getColor(R.color.blancopuro))
                        sector!!.setHintTextColor(resources.getColor(R.color.blancopuro))
                    }
                    else -> {
                        sector!!.setTextColor(resources.getColor(R.color.blancopuro))
                        sector!!.setHintTextColor(resources.getColor(R.color.blancopuro))
                    }
                }
            }
        })
        /*buscador_alert!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                try{
                    val valor_alerta = buscador_alert!!.getItemAtPosition(buscador_alert!!.getSelectedItemPosition()).toString()
                    when (valor_alerta) {
                        "Seleccione una Alerta..." -> alert!!.setText(R.string.seleccionar_alerta)
                        "Basura / Escombros" -> alert!!.setText(R.string.b_sel)
                        "Calle / Vereda en Mal Estado" -> alert!!.setText(R.string.c_sel)
                        "Infraestructura Urbana en Mal Estado" -> alert!!.setText(R.string.d_sel)
                        "Luminaria en Mal Estado" -> alert!!.setText(R.string.e_sel)
                        "Poda por Obstrucción" -> alert!!.setText(R.string.f_sel)
                        "Juegos Infantiles y Maquinas de Ejercicios" -> alert!!.setText(R.string.g_sel)
                    }
                }catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                alert!!.setText(R.string.seleccionar_alerta)
            }
        })*/
        return root
    }

    fun ingresar_alerta() {
        preferences = activity!!.getSharedPreferences("credenciales", Context.MODE_PRIVATE)
        val valor_rut = preferences!!.getString("valor_rut", "")
        val login_perfil = preferences!!.getString("login_perfil", "")
        var registro = "https://systemchile.com/appmunicipal/alerta/ingresar_alerta.php?a=$valor_rut&b=$login_perfil&c=" + buscador_alert!!.getItemAtPosition(buscador_alert!!.selectedItemPosition).toString() + "&d=" + descr!!.text + "&e=" + sector!!.text
        registro = registro.replace(" ", "%20")
        EnviarRecibirDatos(registro)
    }

    fun EnviarRecibirDatos(URL: String?) {
        val queue = Volley.newRequestQueue(activity)
        val stringRequest = StringRequest(Request.Method.GET, URL, Response.Listener { enviar_correo() },
            Response.ErrorListener { error ->
                Log.i("Mensaje Vollley Error: ", error.toString() + "")
                Handler().postDelayed({
                    Toasty.warning(activity!!, "Consultando Datos, Espere un Momento...", Toasty.LENGTH_SHORT, true).show()
                    ingresar_alerta()
                }, 7000)
            })
        stringRequest.setShouldCache(false)
        queue.add(stringRequest)
    }

    fun enviar_correo() {
        preferences = activity!!.getSharedPreferences("credenciales", Context.MODE_PRIVATE)
        val valor_rut = preferences!!.getString("valor_rut", "")
        val login_perfil = preferences!!.getString("login_perfil", "")
        var registro = "https://systemchile.com/appmunicipal/alerta/mail.php?a=$valor_rut&b=$login_perfil&c=" + buscador_alert!!.getItemAtPosition(buscador_alert!!.selectedItemPosition).toString() + "&d=" + descr!!.text + "&e=" + sector!!.text
        registro = registro.replace(" ", "%20")
        Enviar_Correo(registro)
    }

    fun Enviar_Correo(URL: String?) {
        val queue = Volley.newRequestQueue(activity)
        val stringRequest = StringRequest(
            Request.Method.GET, URL, Response.Listener { Toasty.success(activity!!, "Datos Enviados Exitosamente", Toasty.LENGTH_SHORT, true).show()
                progressDialog_carga!!.dismiss()
                limpiar_residuo()
            },
            Response.ErrorListener { error -> Log.i("Mensaje Vollley Error: ", error.toString() + "")
                Handler().postDelayed({
                    Toasty.warning(activity!!, "Consultando Datos, Espere un Momento...", Toasty.LENGTH_SHORT, true).show()
                    enviar_correo()
                }, 7000)
            })
        stringRequest.setShouldCache(false)
        queue.add(stringRequest)
    }

    fun enviar_correo_violencia() {
        locationStart()
    }

    fun Enviar_Correo_violencia(URL: String?) {
        val queue = Volley.newRequestQueue(activity)
        val stringRequest = StringRequest(
            Request.Method.GET, URL, Response.Listener { Toasty.success(activity!!, "Datos Enviados Exitosamente", Toasty.LENGTH_SHORT, true).show()
                progressDialog_carga!!.dismiss()
                varseleccion = 0
            },
            Response.ErrorListener { error -> Log.i("Mensaje Vollley Error: ", error.toString() + "")
                Handler().postDelayed({
                    Toasty.warning(activity!!, "Consultando Datos, Espere un Momento...", Toasty.LENGTH_SHORT, true).show()
                    enviar_correo_violencia()
                }, 7000)
            })
        stringRequest.setShouldCache(false)
        queue.add(stringRequest)
    }

    fun spinner_alerta() {
        val datostipodocumento = arrayOf("Seleccione una Alerta...", "Basura / Escombros", "Calle / Vereda en Mal Estado", "Infraestructura Urbana en Mal Estado", "Luminaria en Mal Estado", "Poda por Obstrucción", "Juegos Infantiles y Maquinas de Ejercicios")
        val adapter = ArrayAdapter(activity!!, R.layout.spinner_alerta_item, datostipodocumento)
        buscador_alert!!.adapter = adapter
    }

    fun limpiar_residuo() {
        buscador_alert!!.setSelection(0)
        descr!!.setText("")
        sector!!.setText("")
        buscador_alert!!.requestFocus()
    }

    private fun locationStart() {
        val Local = Localizacion()
        Local.mainActivity = this
        val mlocManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        when{!gpsEnabled -> { val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(settingsIntent) }
            ActivityCompat.checkSelfPermission(context!! as Activity, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!! as Activity, Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED ->{
                ActivityCompat.requestPermissions(context!! as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1000)
                return
            }
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, Local as LocationListener)
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, Local as LocationListener)
    }

    fun setLocation(loc: Location) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        when{ loc.latitude != 0.0 && loc.longitude != 0.0 -> {
                try { val geocoder = Geocoder(activity!!, Locale.getDefault())
                    val list = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
                    when { !list.isEmpty() -> {
                            val DirCalle = list[0]
                            direccion = (DirCalle.getAddressLine(0))
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /* Aqui empieza la Clase Localizacion */
    inner class Localizacion : LocationListener {
        var mainActivity: AlertasFragment? = null

        override fun onLocationChanged(loc: Location) {
            when (varseleccion) {
                1 -> {
                    // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
                    // debido a la deteccion de un cambio de ubicacion
                    loc.latitude
                    loc.longitude
                    val sLatitud = loc.latitude.toString()
                    val sLongitud = loc.longitude.toString()
                    latitud = sLatitud
                    longitud = sLongitud
                    when{ latitud != null -> latitud = sLatitud }
                    try {
                        mainActivity?.setLocation(loc)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    preferences = activity!!.getSharedPreferences("credenciales", Context.MODE_PRIVATE)
                    val login_perfil = preferences!!.getString("login_perfil", "")
                    val valor_rut = preferences!!.getString("valor_rut", "")
                    val valor_email = preferences!!.getString("valor_email", "")
                    val valor_telefono = preferences!!.getString("valor_telefono", "")
                    val valor_direccion = preferences!!.getString("valor_direccion", "")
                    var registro = "https://systemchile.com/appmunicipal/alerta/mail_violencia.php?a=$login_perfil&b=$valor_rut&c=$valor_email&d=$valor_telefono&e=$valor_direccion&f=" + latitud + "&g=" + longitud + "&h=" + direccion
                    registro = registro.replace(" ", "%20")
                    varseleccion = 0
                    Enviar_Correo_violencia(registro)
                }
            }
        }

        override fun onProviderDisabled(provider: String) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            val ms = DialogoIncorrecto(mensaje_flotante_incorrecto)
            ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
            ms.contenido_mensaje.setText(R.string.activargps)
           //latitud.setText("GPS Desactivado")
        }

        override fun onProviderEnabled(provider: String) {
            // Este metodo se ejecuta cuando el GPS es activado
            //latitud.setText("GPS Activado")
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            when (status) {
                LocationProvider.AVAILABLE -> Log.d("debug", "LocationProvider.AVAILABLE")
                LocationProvider.OUT_OF_SERVICE -> Log.d("debug", "LocationProvider.OUT_OF_SERVICE")
                LocationProvider.TEMPORARILY_UNAVAILABLE -> Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE")
            }
        }
    }
    override fun ResultadoCuadroDialogo(res: Boolean) {
        valor = res
        when (valor) {
            true -> {
                when (varseleccion2) {
                    1 -> {
                        progressDialog_carga = ProgressDialog(context)
                        progressDialog_carga!!.dismiss()
                        progressDialog_carga!!.setTitle("Enviando Aviso")
                        progressDialog_carga!!.setMessage("Espere un Momento...")
                        progressDialog_carga!!.setCancelable(false)
                        progressDialog_carga!!.show()
                        varseleccion = 1
                        enviar_correo_violencia()
                    }
                    3 -> { Toasty.info(activity!!, "* Pinche en Permisos \u000A* Luego Pinche en Teléfono", Toasty.LENGTH_LONG, true).show()
                        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.fromParts("package", activity!!.packageName, null)));
                    }
                    4 -> { Toasty.info(activity!!, "Seguridad Ciudadana: \u000A Llamando...", Toasty.LENGTH_SHORT, true).show()
                        val callIntent = Intent(Intent.ACTION_CALL)
                        callIntent.data = Uri.parse("tel:+56 9 7623 2889")
                        startActivity(callIntent)
                        activity!!.overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                    }
                }
            }
            false -> { Log.d("False", "Pincho en Falso")
                varseleccion2 = 0}
        }
    }
}