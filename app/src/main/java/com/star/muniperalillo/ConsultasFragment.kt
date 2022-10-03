package com.star.muniperalillo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONException
import java.util.*

class ConsultasFragment : Fragment() {
    var root: View? = null
    var recycler_deptos: RecyclerView? = null
    var buscador: EditText? = null
    var deptoMuniAdapter: DeptosMuniAdapter? = null
    private val TAG: String = ConsultasFragment::class.java.getSimpleName()
    var datosdepto: ArrayList<DatosDeptoMuni>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_consultas, container, false)
        buscador = root!!.findViewById(R.id.buscador)
        recycler_deptos = root!!.findViewById(R.id.recycler_deptos)
        recycler_deptos!!.setLayoutManager(LinearLayoutManager(context))
        buscador!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (recycler_deptos!!.getAdapter() != null) {
                    if (recycler_deptos!!.getAdapter()!!.itemCount == 0) {
                        deptoMuniAdapter!!.getFilter().filter(s.toString())
                    } else {
                        deptoMuniAdapter!!.getFilter().filter(s.toString())
                    }
                } else {
                    deptoMuniAdapter!!.getFilter().filter(s.toString())
                }
                deptoMuniAdapter!!.getFilter().filter(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                if (recycler_deptos!!.getAdapter() != null) {
                    if (recycler_deptos!!.getAdapter()!!.itemCount == 0) {
                        deptoMuniAdapter!!.getFilter().filter(s.toString())
                    } else {
                        deptoMuniAdapter!!.getFilter().filter(s.toString())
                    }
                } else {
                    deptoMuniAdapter!!.getFilter().filter(s.toString())
                }
                deptoMuniAdapter!!.getFilter().filter(s.toString())
            }
        })
        getDataFromServer()
        buscador!!.setOnFocusChangeListener(object: View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                when{
                    !hasFocus -> {
                        buscador!!.setTextColor(resources.getColor(R.color.color_text))
                        buscador!!.setHintTextColor(resources.getColor(R.color.color_consulta))
                    }
                    else -> {
                        buscador!!.setTextColor(resources.getColor(R.color.blancopuro))
                        buscador!!.setHintTextColor(resources.getColor(R.color.plomoblanco))
                    }
                }
            }
        })
        return root
    }

    private fun showRecyclerView() {
        deptoMuniAdapter = DeptosMuniAdapter(datosdepto!!)
        recycler_deptos!!.adapter = deptoMuniAdapter
    }

    private fun getDataFromServer()
    {
        datosdepto = ArrayList()
        try {
            datosdepto!!.add(DatosDeptoMuni("Alcalde","Carlos Utman Goldschmidt","Fono: 72 235 1300","cutman@muniperalillo.cl"))
            datosdepto!!.add(DatosDeptoMuni("Administración Municipal","Juan Luis Bars","Fono: 72 235 1300","jbars@muniperalillo.cl"))
            datosdepto!!.add(DatosDeptoMuni("DIDECO","Mauricio Ortíz Espinoza","Celular: +56 9 9802 2789","mauricio@muniperalillo.cl"))
            datosdepto!!.add(DatosDeptoMuni("DAF","Luis Olea García","Fono: 72 235 1318","luisolea@muniperalillo.cl"))
            datosdepto!!.add(DatosDeptoMuni("DOM","Fernando Jara Sepúlveda","Fono: 72 235 1329","fjara@muniperalillo.cl"))
            datosdepto!!.add(DatosDeptoMuni("SECPLAC","Paola Olguin Caroca","Fono: 72 235 1343","polguin@muniperalillo.cl"))
            datosdepto!!.add(DatosDeptoMuni("Secretaría Municipal","Hernán Castro Moraga","Fono: 72 235 1307","hcastro@muniperalillo.cl"))
            datosdepto!!.add(DatosDeptoMuni("Depto. Tránsito","Rodrigo Vilches Duarte","Fono: 72 235 1323","rvilches@muniperalillo.cl"))
            datosdepto!!.add(DatosDeptoMuni("Depto. Rentas","Fidelicia Cornejo Pinto","Fono: 72 235 1319","fcornejo@muniperalillo.cl"))
            showRecyclerView()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}