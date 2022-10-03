package com.star.muniperalillo

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.*
import android.content.pm.*
import android.location.*
import android.os.*
import android.provider.Settings
import android.text.InputFilter.*
import android.text.*
import android.util.*
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import es.dmoral.toasty.Toasty
import hari.bounceview.BounceView
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.util.*

class Covid19Fragment : Fragment() {
    var lista_examen = arrayOf("Examen No Realizado", "Examen Positivo", "Examen Negativo")
    var rut: EditText? = null
    var nombre: EditText? = null
    var correo: EditText? = null
    var telefono: EditText? = null
    var domicilio: EditText? = null
    var sintomas: EditText? = null
    var dias: EditText? = null
    var salud: EditText? = null
    var contacto: EditText? = null
    var comuna: EditText? = null
    var pais: EditText? = null
    var paciente: EditText? = null
    var red_apoyo: Switch? = null
    var conocer_ubicacion: Switch? = null
    var ingresar: Button? = null
    var root: View? = null
    var examen: AutoCompleteTextView? = null
    var valor_red_apoyo = "No"
    var valor_gps = "No Confirmada"
    var latitud: String? = null
    var longitud = "----------"
    var direccion = "----------"
    var mensaje_flotante_incorrecto: Context? = null
    var progressDialog_carga: ProgressDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_covid19, container, false)
        rut = root!!.findViewById(R.id.rut)
        nombre = root!!.findViewById(R.id.nombre)
        correo = root!!.findViewById(R.id.correo)
        telefono = root!!.findViewById(R.id.telefono)
        domicilio = root!!.findViewById(R.id.domicilio)
        sintomas = root!!.findViewById(R.id.sintomas)
        dias = root!!.findViewById(R.id.dias)
        salud = root!!.findViewById(R.id.salud)
        contacto = root!!.findViewById(R.id.contacto)
        comuna = root!!.findViewById(R.id.comuna)
        pais = root!!.findViewById(R.id.pais)
        paciente = root!!.findViewById(R.id.paciente)
        examen = root!!.findViewById(R.id.examen)
        red_apoyo = root!!.findViewById(R.id.red_apoyo)
        conocer_ubicacion = root!!.findViewById(R.id.conocer_ubicacion)
        ingresar = root!!.findViewById(R.id.ingresar)
        val adaptador_listasectores = ArrayAdapter(context!!, android.R.layout.select_dialog_item, lista_examen)
        examen!!.setThreshold(1)
        examen!!.setAdapter(adaptador_listasectores)
        mensaje_flotante_incorrecto = context
        val charactersForbiden4 = "a∆bcdefghijklmnopqrstuvwxyzñÑABCDEFGHIJLMNOPQRSTUVWXYZ!ΔΔΔδ₲₡✓ÇüæęĘąǍǎČčĎďĚěǦǧȞȟǏǐJ̌ǰǨǩĽľŇňǑǒŘřŠšṦṧŤťǓǔǙǚŽžǮǯĄłŁżŻĖČčėÆâôäöàòåûçùêÿëÖèÜï¢î£ì¥Ä₧Åƒ░▒▓│┤╡ª╢º╖¿╕⌐╣¬║½╗¼╝¡╜«╛»┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ËÌÎÏÐÒÔÕÖ×ØÙÛÜÞßàâãäåæçèêëìîïðòôõö÷øùûüþÿ¯°±²³´µ¶·¸¹º»¼½¾¿ÀÂÃÄÅĒēĀāǢǣĪīŌōŪūǕǖȲȳÆÇÈÊ’“”•–—˜™š›œžŸ¡¢£¤¥¦§¨©ª«¬®{|}~Δ€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘[\\]^_`=>?@:;<\"#$%&'()*+,/"
        val filtro_rut = InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                val type = Character.getType(source[i])
                if (source != null && charactersForbiden4.contains("" + source) || type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt()) {
                    return@InputFilter ""
                }
            }
            null
        }
        val charactersForbiden2 = "!ΔΔ∆Δδ₲₡✓ÇüæęĘąǍǎČčĎďĚěǦǧȞȟǏǐǰǨǩĽľŇňǑǒŘřŠšṦṧŤťǓǔǙǚŽžǮǯĄłŁżŻĖČčėÆâôäöàòåûçùêÿëÖèÜï¢î£ì¥Ä₧Åƒ░▒▓│┤╡ª╢º╖¿╕⌐╣¬║½╗¼╝¡╜«╛»┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ËÌÎÏÐÒÔÕÖ×ØÙÛÜÞßàâãäåæçèêëìîïðòôõö÷øùûüþÿ¯°±²³´µ¶·¸¹º»¼½¾¿ÀÂÃÄÅĒēĀāǢǣĪīŌōŪūǕǖȲȳÆÇÈÊ’“”•–—˜™š›œžŸ¡¢£¤¥¦§¨©ª«¬®{|}~Δ€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘[\\]^_`=>?@:;<\"#$%&'()*+,-./"
        val filtro_nombre = InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (source != null && charactersForbiden2.contains("" + source) || !Character.isLetter(
                        source[i]
                    ) && !Character.isSpaceChar(source[i])
                ) {
                    return@InputFilter ""
                }
            }
            null
        }
        val charactersForbiden3 = "!Δ∆ΔΔδ₲₡✓ÇüæęĘąǍǎČčĎďĚěǦǧȞȟǏǐǰǨǩĽľŇňǑǒŘřŠšṦṧŤťǓǔǙǚŽžǮǯĄłŁżŻĖČčėÆâôäöàòåûçùêÿëÖèÜï¢î£ì¥Ä₧Åƒ░▒▓│┤╡ª╢º╖¿╕⌐╣¬║½╗¼╝¡╜«╛»┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈∙·√ⁿ²■ËÌÎÏÐÒÔÕÖ×ØÙÛÜÞßàâãäåæçèêëìîïðòôõö÷øùûüþÿ¯±²³´µ¶·¸¹º»¼½¾¿ÀÂÃÄÅĒēĀāǢǣĪīŌōŪūǕǖȲȳÆÇÈÊ’“”•–—˜™š›œžŸ¡¢£¤¥¦§¨©ª«¬®{|}~Δ€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘[\\]^_`=>?@:;<\"#$%&'()*+,/"
        val filtro_direccion = InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (source != null && charactersForbiden3.contains("" + source)) {
                    return@InputFilter ""
                }
            }
            null
        }
        val filtro_telefono = InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!Character.isDigit(source[i])) {
                    return@InputFilter ""
                }
            }
            null
        }
        val charactersForbiden5 = "!ΔΔ∆Δδ₲₡✓ÇüæęĘąǍǎČčĎďĚěǦǧȞȟǏǐǰǨǩĽľŇňǑǒŘřŠšṦṧŤťǓǔǙǚŽžǮǯĄłŁżŻĖČčėÆâôäöàòåûçùêÿëÖèÜï¢î£ì¥Ä₧Åƒ░▒▓│┤╡ª╢º╖¿╕⌐╣¬║½╗¼╝¡╜«╛»┐ └┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ËÌÎÏÐÒÔÕÖ×ØÙÛÜÞßàâãäåæçèêëìîïðòôõö÷øùûüþÿ¯°±²³´µ¶·¸¹º»¼½¾¿ÀÂÃÄÅĒēĀāǢǣĪīŌōŪūǕǖȲȳÆÇÈÊ’“”•–—˜™š›œžŸ¡¢£¤¥¦§¨©ª«¬®{|}~Δ€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘[\\]^`=>?:;<\"#$%&'()*+,/"
        val filtro_correo = InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                val type = Character.getType(source[i])
                if (source != null && charactersForbiden5.contains("" + source) || type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt()) {
                    return@InputFilter ""
                }
            }
            null
        }
        val charactersForbiden8 = "!ΔΔ∆Δδ₲₡✓ÇüæęĘąǍǎČčĎďĚěǦǧȞȟǏǐ̌ǰǨǩĽľŇňǑǒŘřŠšṦṧŤťǓǔǙǚŽžǮǯĄłŁżŻĖČčėÆâôäöàòåûçùêÿëÖèÜï¢î£ì¥Ä₧Åƒ░▒▓│┤╡ª╢º╖¿╕⌐╣¬║½╗¼╝¡╜«╛»┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ËÌÎÏÐÒÔÕÖ×ØÙÛÜÞßàâãäåæçèêëìîïðòôõö÷øùûüþÿ¯°±²³´µ¶·¸¹º»¼½¾¿ÀÂÃÄÅĒēĀāǢǣĪīŌōŪūǕǖȲȳÆÇÈÊ’“”•–—˜™š›œžŸ¡¢£¤¥¦§¨©ª«¬®{|}~Δ€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘[\\]^_`=>?@:;<\"#$%&'()*+,-./"
        val filtro_sintomas = InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (source != null && charactersForbiden8.contains("" + source) || !Character.isLetter(
                        source[i]
                    ) && !Character.isSpaceChar(source[i])
                ) {
                    return@InputFilter ""
                }
            }
            null
        }
        val filtro_dias = InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!Character.isDigit(source[i])) {
                    return@InputFilter ""
                }
            }
            null
        }
        val charactersForbiden33 = "!Δ∆ΔΔδ₲₡✓ÇüæęĘąǍǎČčĎďĚěǦǧȞȟǏǐǰǨǩĽľŇňǑǒŘřŠšṦṧŤťǓǔǙǚŽžǮǯĄłŁżŻĖČčėÆâôäöàòåûçùêÿëÖèÜï¢î£ì¥Ä₧Åƒ░▒▓│┤╡ª╢º╖¿╕⌐╣¬║½╗¼╝¡╜«╛»┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈∙·√ⁿ²■ËÌÎÏÐÒÔÕÖ×ØÙÛÜÞßàâãäåæçèêëìîïðòôõö÷øùûüþÿ¯±²³´µ¶·¸¹º»¼½¾¿ÀÂÃÄÅĒēĀāǢǣĪīŌōŪūǕǖȲȳÆÇÈÊ’“”•–—˜™š›œžŸ¡¢£¤¥¦§¨©ª«¬®{|}~Δ€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘[\\]^_`=>?@:;<\"#$%&'()*+,/"
        val filtro_salud = InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (source != null && charactersForbiden33.contains("" + source)) {
                    return@InputFilter ""
                }
            }
            null
        }
        val charactersForbiden333 = "!Δ∆ΔΔδ₲₡✓ÇüæęĘąǍǎČčĎďĚěǦǧȞȟǏǐ̌ǰǨǩĽľŇňǑǒŘřŠšṦṧŤťǓǔǙǚŽžǮǯĄłŁżŻĖČčėÆâôäöàòåûçùêÿëÖèÜï¢î£ì¥Ä₧Åƒ░▒▓│┤╡ª╢º╖¿╕⌐╣¬║½╗¼╝¡╜«╛»┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈∙·√ⁿ²■ËÌÎÏÐÒÔÕÖ×ØÙÛÜÞßàâãäåæçèêëìîïðòôõö÷øùûüþÿ¯±²³´µ¶·¸¹º»¼½¾¿ÀÂÃÄÅĒēĀāǢǣĪīŌōŪūǕǖȲȳÆÇÈÊ’“”•–—˜™š›œžŸ¡¢£¤¥¦§¨©ª«¬®{|}~Δ€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘[\\]^_`=>?@:;<\"#$%&'()*+,/"
        val filtro_contacto = InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (source != null && charactersForbiden333.contains("" + source)) {
                    return@InputFilter ""
                }
            }
            null
        }
        val charactersForbiden82 = "!ΔΔ∆Δδ₲₡✓ÇüæęĘąǍǎČčĎďĚěǦǧȞȟǏǐǰǨǩĽľŇňǑǒŘřŠšṦṧŤťǓǔǙǚŽžǮǯĄłŁżŻĖČčėÆâôäöàòåûçùêÿëÖèÜï¢î£ì¥Ä₧Åƒ░▒▓│┤╡ª╢º╖¿╕⌐╣¬║½╗¼╝¡╜«╛»┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ËÌÎÏÐÒÔÕÖ×ØÙÛÜÞßàâãäåæçèêëìîïðòôõö÷øùûüþÿ¯°±²³´µ¶·¸¹º»¼½¾¿ÀÂÃÄÅĒēĀāǢǣĪīŌōŪūǕǖȲȳÆÇÈÊ’“”•–—˜™š›œžŸ¡¢£¤¥¦§¨©ª«¬®{|}~Δ€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘[\\]^_`=>?@:;<\"#$%&'()*+,-./"
        val filtro_comuna = InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (source != null && charactersForbiden82.contains("" + source) || !Character.isLetter(
                        source[i]
                    ) && !Character.isSpaceChar(source[i])
                ) {
                    return@InputFilter ""
                }
            }
            null
        }
        val charactersForbiden823 = "!ΔΔ∆Δδ₲₡✓ÇüæęĘąǍǎČčĎďĚěǦǧȞȟǏǐ̌ǰǨǩĽľŇňǑǒŘřŠšṦṧŤťǓǔǙǚŽžǮǯĄłŁżŻĖČčėÆâôäöàòåûçùêÿëÖèÜï¢î£ì¥Ä₧Åƒ░▒▓│┤╡ª╢º╖¿╕⌐╣¬║½╗¼╝¡╜«╛»┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ËÌÎÏÐÒÔÕÖ×ØÙÛÜÞßàâãäåæçèêëìîïðòôõö÷øùûüþÿ¯°±²³´µ¶·¸¹º»¼½¾¿ÀÂÃÄÅĒēĀāǢǣĪīŌōŪūǕǖȲȳÆÇÈÊ’“”•–—˜™š›œžŸ¡¢£¤¥¦§¨©ª«¬®{|}~Δ€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘[\\]^_`=>?@:;<\"#$%&'()*+,-./"
        val filtro_pais = InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (source != null && charactersForbiden823.contains("" + source) || !Character.isLetter(
                        source[i]
                    ) && !Character.isSpaceChar(source[i])
                ) {
                    return@InputFilter ""
                }
            }
            null
        }
        val charactersForbiden999 = "!ΔΔ∆Δδ₲₡✓ÇüæęĘąǍǎČčĎďĚěǦǧȞȟǏǐ̌ǰǨǩĽľŇňǑǒŘřŠšṦṧŤťǓǔǙǚŽžǮǯĄłŁżŻĖČčėÆâôäöàòåûçùêÿëÖèÜï¢î£ì¥Ä₧Åƒ░▒▓│┤╡ª╢º╖¿╕⌐╣¬║½╗¼╝¡╜«╛»┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ËÌÎÏÐÒÔÕÖ×ØÙÛÜÞßàâãäåæçèêëìîïðòôõö÷øùûüþÿ¯°±²³´µ¶·¸¹º»¼½¾¿ÀÂÃÄÅĒēĀāǢǣĪīŌōŪūǕǖȲȳÆÇÈÊ’“”•–—˜™š›œžŸ¡¢£¤¥¦§¨©ª«¬®{|}~Δ€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘[\\]^_`=>?@:;<\"#$%&'()*+,-./"
        val filtro_paciente = InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (source != null && charactersForbiden999.contains("" + source) || !Character.isLetter(
                        source[i]
                    ) && !Character.isSpaceChar(source[i])
                ) {
                    return@InputFilter ""
                }
            }
            null
        }
        val charactersForbiden9999 = "!ΔΔ∆Δδ₲₡✓ÇüæęĘąǍǎČčĎďĚěǦǧȞȟǏǐ̌ǰǨǩĽľŇňǑǒŘřŠšṦṧŤťǓǔǙǚŽžǮǯĄłŁżŻĖČčėÆâôäöàòåûçùêÿëÖèÜï¢î£ì¥Ä₧Åƒ░▒▓│┤╡ª╢º╖¿╕⌐╣¬║½╗¼╝¡╜«╛»┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αßΓπΣσµτΦΘΩδ∞φε∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■ËÌÎÏÐÒÔÕÖ×ØÙÛÜÞßàâãäåæçèêëìîïðòôõö÷øùûüþÿ¯°±²³´µ¶·¸¹º»¼½¾¿ÀÂÃÄÅĒēĀāǢǣĪīŌōŪūǕǖȲȳÆÇÈÊ’“”•–—˜™š›œžŸ¡¢£¤¥¦§¨©ª«¬®{|}~Δ€‚ƒ„…†‡ˆ‰Š‹ŒŽ‘[\\]^_`=>?@:;<\"#$%&'()*+,-./"
        val filtro_examen = InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (source != null && charactersForbiden9999.contains("" + source) || !Character.isLetter(
                        source[i]
                    ) && !Character.isSpaceChar(source[i])
                ) {
                    return@InputFilter ""
                }
            }
            null
        }
        rut!!.setFilters(arrayOf(LengthFilter(12), AllCaps(),  filtro_rut))
        nombre!!.setFilters(arrayOf(LengthFilter(70), filtro_nombre))
        domicilio!!.setFilters(arrayOf(LengthFilter(80), filtro_direccion))
        telefono!!.setFilters(arrayOf(LengthFilter(8), filtro_telefono))
        correo!!.setFilters(arrayOf(LengthFilter(60), filtro_correo))
        sintomas!!.setFilters(arrayOf(LengthFilter(250), filtro_sintomas))
        dias!!.setFilters(arrayOf(LengthFilter(9), filtro_dias))
        salud!!.setFilters(arrayOf(LengthFilter(250), filtro_salud))
        contacto!!.setFilters(arrayOf(LengthFilter(250), filtro_contacto))
        comuna!!.setFilters(arrayOf(LengthFilter(80), filtro_comuna))
        pais!!.setFilters(arrayOf(LengthFilter(250), filtro_pais))
        red_apoyo!!.setFilters(arrayOf(LengthFilter(80), filtro_pais))
        paciente!!.setFilters(arrayOf(LengthFilter(80), filtro_paciente))
        examen!!.setFilters(arrayOf(LengthFilter(80), filtro_examen))
        conocer_ubicacion!!.setFilters(arrayOf(LengthFilter(40), filtro_pais))
        rut!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val length = rut!!.getText().length
                if (length == 0) {
                    rut!!.setInputType(InputType.TYPE_CLASS_NUMBER)
                } else if (length == 2) {
                    rut!!.setText(rut!!.getText().toString() + ".")
                    rut!!.setSelection(rut!!.getText().length)
                } else if (length == 6) {
                    rut!!.setText(rut!!.getText().toString() + ".")
                    rut!!.setSelection(rut!!.getText().length)
                } else if (length == 10) {
                    rut!!.setText(rut!!.getText().toString() + "-")
                    rut!!.setSelection(rut!!.getText().length)
                    rut!!.setInputType(InputType.TYPE_CLASS_TEXT)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        rut!!.setOnClickListener { rut!!.setSelection(0, rut!!.getText().length) }
        red_apoyo!!.setOnClickListener {
            valor_red_apoyo = if (red_apoyo!!.isChecked()) {
                "Si"
            } else {
                "No"
            }
        }
        conocer_ubicacion!!.setOnClickListener {
            if (conocer_ubicacion!!.isChecked()) {
                valor_gps = "Aceptada"
                enviar_localizacion()
            } else {
                valor_gps = "No Confirmada"
                latitud = "----------"
                longitud = "----------"
            }
        }
        ingresar!!.setOnClickListener { campos_variables_proc() }
        return root
    }

    fun enviar_localizacion() {
        progressDialog_carga = ProgressDialog(context)
        progressDialog_carga!!.setTitle("Obteniendo Ubicación")
        progressDialog_carga!!.setMessage("Espere un Momento...")
        progressDialog_carga!!.setCancelable(false)
        progressDialog_carga!!.show()
        locationStart()
        progressDialog_carga!!.dismiss()
    }

    private fun registro_covid() {
        var registro = "https://systemchile.com/appmunicipal/covid19/crear_registro.php?a=" + rut!!.text + "&b=" + nombre!!.text + "&c=" + correo!!.text + "&d=9" + telefono!!.text + "&e=" + domicilio!!.text + "&f=" + sintomas!!.text + "&g=" + dias!!.text + "&h=" + salud!!.text + "&i=" + contacto!!.text + "&j=" + comuna!!.text + "&k=" + pais!!.text + "&l=" + valor_red_apoyo + "&m=" + paciente!!.text + "&n=" + examen!!.text + "&o=" + valor_gps + "&p=" + latitud + "&q=" + longitud
        registro = registro.replace(" ", "%20")
        EnviarRecibirDatos(registro)
    }

    private fun EnviarRecibirDatos(URL: String) {
        val queue = Volley.newRequestQueue(activity)
        val stringRequest = StringRequest(Request.Method.GET, URL, Response.Listener { response -> var response = response
                response = response.replace("][", ",")
                if (response.length > 0) {
                    try {
                        val ja = JSONArray(response)
                        val valor_r = ja.getString(0)
                        if (valor_r == "1") {
                            Toasty.warning(activity!!, "Su Rut ya está Registrado", Toasty.LENGTH_SHORT, true).show()
                            progressDialog_carga!!.dismiss()
                        } else if (valor_r == "0") {
                            metodo_enviar_correo()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            },
            Response.ErrorListener { error -> Log.i("Mensaje Vollley Error: ", error.toString() + "")
                Handler().postDelayed({
                    Toasty.warning(activity!!, "Consultando Datos, Espere un Momento...", Toasty.LENGTH_SHORT, true).show()
                    registro_covid()
                }, 7000)
            })
        stringRequest.setShouldCache(false)
        queue.add(stringRequest)
    }

    fun metodo_enviar_correo() {
        var registro = "https://systemchile.com/appmunicipal/covid19/mail.php?a=" + rut!!.text + "&b=" + nombre!!.text + "&c=" + correo!!.text + "&d=9" + telefono!!.text + "&e=" + domicilio!!.text + "&f=" + sintomas!!.text + "&g=" + dias!!.text + "&h=" + salud!!.text + "&i=" + contacto!!.text + "&j=" + comuna!!.text + "&k=" + pais!!.text + "&l=" + valor_red_apoyo + "&m=" + paciente!!.text + "&n=" + examen!!.text + "&o=" + valor_gps + "&p=" + latitud + "&q=" + longitud
        registro = registro.replace(" ", "%20")
        Enviar_Correo(registro)
    }

    fun Enviar_Correo(URL: String?) {
        val queue = Volley.newRequestQueue(activity)
        val stringRequest = StringRequest(Request.Method.GET, URL, Response.Listener {
                Toasty.success(activity!!, "Información Enviada Exitosamente", Toasty.LENGTH_SHORT, true).show()
                progressDialog_carga!!.dismiss()
                limpiar_residuo()
            },
            Response.ErrorListener { error -> Log.i("Mensaje Vollley Error: ", error.toString() + "")
                Handler().postDelayed({
                    Toasty.warning(activity!!, "Consultando Datos, Espere un Momento...", Toasty.LENGTH_SHORT, true).show()
                    metodo_enviar_correo()
                }, 7000)
            })
        stringRequest.setShouldCache(false)
        queue.add(stringRequest)
    }

    fun campos_variables_proc() {
        val valor_rut = rut!!.text.toString()
        val valor_nombre = nombre!!.text.toString()
        val valor_correo = correo!!.text.toString()
        val valor_telefono = telefono!!.text.toString()
        val valor_domicilio = domicilio!!.text.toString()
        val valor_sintomas = sintomas!!.text.toString()
        val valor_dias = dias!!.text.toString()
        val valor_salud = salud!!.text.toString()
        val valor_contacto = contacto!!.text.toString()
        val valor_comuna = comuna!!.text.toString()
        val valor_pais = pais!!.text.toString()
        val valor_paciente = paciente!!.text.toString()
        val valor_examen = examen!!.text.toString()
        val length_rut = rut!!.text.length
        val length_telefono = telefono!!.text.length
        if (valor_rut == "" || length_rut < 12) {
            val ms = DialogoIncorrecto(mensaje_flotante_incorrecto)
            ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
            ms.contenido_mensaje.setText(R.string.rut_error)
            rut!!.requestFocus()
        } else if (!validarRut(rut!!.text.toString())) {
            val ms = DialogoIncorrecto(mensaje_flotante_incorrecto)
            ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
            ms.contenido_mensaje.setText(R.string.rut_error_valido)
            rut!!.requestFocus()
        } else if (valor_nombre == "") {
            val ms = DialogoIncorrecto(mensaje_flotante_incorrecto)
            ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
            ms.contenido_mensaje.setText(R.string.nombre_compl_error)
            nombre!!.requestFocus()
        } else if (valor_correo == "") {
            val ms = DialogoIncorrecto(mensaje_flotante_incorrecto)
            ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
            ms.contenido_mensaje.setText(R.string.email_error)
            correo!!.requestFocus()
        } else if (!validarEmail(correo!!.text.toString())) {
            val ms = DialogoIncorrecto(mensaje_flotante_incorrecto)
            ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
            ms.contenido_mensaje.setText(R.string.correo_no_válido_error)
            correo!!.requestFocus()
        } else if (valor_telefono == "" || length_telefono < 8) {
            val ms = DialogoIncorrecto(mensaje_flotante_incorrecto)
            ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
            ms.contenido_mensaje.setText(R.string.telefono_error)
            telefono!!.requestFocus()
        } else if (valor_domicilio == "") {
            val ms = DialogoIncorrecto(mensaje_flotante_incorrecto)
            ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
            ms.contenido_mensaje.setText(R.string.direccion_error)
            domicilio!!.requestFocus()
        } else if (valor_sintomas == "") {
            val ms = DialogoIncorrecto(mensaje_flotante_incorrecto)
            ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
            ms.contenido_mensaje.setText(R.string.sintomas_error)
            sintomas!!.requestFocus()
        } else if (valor_dias == "") {
            val ms = DialogoIncorrecto(mensaje_flotante_incorrecto)
            ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
            ms.contenido_mensaje.setText(R.string.diassintomas_error)
            dias!!.requestFocus()
        } else if (valor_salud == "") {
            val ms = DialogoIncorrecto(mensaje_flotante_incorrecto)
            ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
            ms.contenido_mensaje.setText(R.string.antsalud_error)
            salud!!.requestFocus()
        } else if (valor_contacto == "") {
            val ms = DialogoIncorrecto(mensaje_flotante_incorrecto)
            ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
            ms.contenido_mensaje.setText(R.string.antcont_error)
            contacto!!.requestFocus()
        } else if (valor_comuna == "") {
            val ms = DialogoIncorrecto(mensaje_flotante_incorrecto)
            ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
            ms.contenido_mensaje.setText(R.string.comuna_error)
            comuna!!.requestFocus()
        } else if (valor_pais == "") {
            val ms = DialogoIncorrecto(mensaje_flotante_incorrecto)
            ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
            ms.contenido_mensaje.setText(R.string.pais_error)
            pais!!.requestFocus()
        } else if (valor_paciente == "") {
            val ms = DialogoIncorrecto(mensaje_flotante_incorrecto)
            ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
            ms.contenido_mensaje.setText(R.string.paciente_error)
            paciente!!.requestFocus()
        } else if (valor_examen == "") {
            val ms = DialogoIncorrecto(mensaje_flotante_incorrecto)
            ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
            ms.contenido_mensaje.setText(R.string.examen_error)
            examen!!.requestFocus()
        } else {
            progressDialog_carga = ProgressDialog(context)
            progressDialog_carga!!.dismiss()
            progressDialog_carga!!.setTitle("Enviando Información")
            progressDialog_carga!!.setMessage("Espere un Momento...")
            progressDialog_carga!!.setCancelable(false)
            progressDialog_carga!!.show()
            registro_covid()
        }
    }

    private fun validarEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun limpiar_residuo() {
        rut!!.setText("")
        rut!!.requestFocus()
        nombre!!.setText("")
        correo!!.setText("")
        telefono!!.setText("")
        domicilio!!.setText("")
        sintomas!!.setText("")
        dias!!.setText("")
        salud!!.setText("")
        contacto!!.setText("")
        comuna!!.setText("")
        pais!!.setText("")
        paciente!!.setText("")
    }

        fun validarRut(rut: String): Boolean {
            var rut = rut
            var validacion = false
            try {
                rut = rut.toUpperCase()
                rut = rut.replace(".", "")
                rut = rut.replace("-", "")
                var rutAux = rut.substring(0, rut.length - 1).toInt()
                val dv = rut[rut.length - 1]
                var m = 0
                var s = 1
                while (rutAux != 0) {
                    s = (s + rutAux % 10 * (9 - m++ % 6)) % 11
                    rutAux /= 10
                }
                if (dv == (if (s != 0) s + 47 else 75).toChar()) {
                    validacion = true
                }
            } catch (e: NumberFormatException) {
            } catch (e: Exception) {
            }
            return validacion
        }


    private fun locationStart() {
        val mlocManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val Local = Localizacion()
        Local.mainActivity = this
        val gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!gpsEnabled) {
            val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(settingsIntent)
        }
        if (ActivityCompat.checkSelfPermission(context!! as Activity, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!! as Activity, Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context!! as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1000)
            return
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, Local as LocationListener)
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, Local as LocationListener)
    }

    fun setLocation(loc: Location) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.latitude != 0.0 && loc.longitude != 0.0) {
            try {
                val geocoder = Geocoder(activity!!, Locale.getDefault())
                val list = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
                if (!list.isEmpty()) {
                    val DirCalle = list[0]
                    direccion = (DirCalle.getAddressLine(0))
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /* Aqui empieza la Clase Localizacion */
    inner class Localizacion : LocationListener {
        var mainActivity: Covid19Fragment? = null

        override fun onLocationChanged(loc: Location) {
                // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
                // debido a la deteccion de un cambio de ubicacion
                loc.latitude
                loc.longitude
                val sLatitud = loc.latitude.toString()
                val sLongitud = loc.longitude.toString()
                latitud = sLatitud
                longitud = sLongitud
                if (latitud != null) {
                    latitud = sLatitud
                }
                try {
                    mainActivity?.setLocation(loc)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
        }

        override fun onProviderDisabled(provider: String) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            if (conocer_ubicacion!!.isChecked()) {
                val ms = DialogoIncorrecto(mensaje_flotante_incorrecto)
                ms.contenido_mensaje.findViewById<View>(R.id.contenido_mensaje)
                ms.contenido_mensaje.setText(R.string.activargps)
            }else{
            }
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
}