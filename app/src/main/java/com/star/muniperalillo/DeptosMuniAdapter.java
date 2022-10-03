package com.star.muniperalillo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class DeptosMuniAdapter extends RecyclerView.Adapter<DeptosMuniAdapter.ViewHolder> implements Filterable, DialogoConfirmacion.FinalizoCuadroDialogo{

    private ArrayList<DatosDeptoMuni> datosdepto;
    private ArrayList<DatosDeptoMuni> datos;
    int varseleccion = 0;
    boolean valor = false;
    CustomFilter mFilter;
    String telefono, dep = "";
    Context ventana_telefono;

    public DeptosMuniAdapter(ArrayList<DatosDeptoMuni> datos){
        this.datos = datos;
        this.datosdepto = new ArrayList<>();
        this.datosdepto.addAll(datos);
        this.mFilter = new CustomFilter(DeptosMuniAdapter.this);
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    @NonNull
    @Override
    public DeptosMuniAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_deptos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return datosdepto.size();
    }

    @Override
    public void ResultadoCuadroDialogo(boolean res) {
        valor = res;
        if (valor) {
            if (varseleccion == 1) {
                Toasty.info(ventana_telefono, "* Pinche en Permisos"+(char)10+"Luego Pinche en Teléfono", Toasty.LENGTH_LONG, true).show();
                ventana_telefono.startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.fromParts("package", ventana_telefono.getPackageName(), null)));
            }
            else if (varseleccion == 2) {
                Toasty.info(ventana_telefono, dep+": "+(char)10+"Llamando...", Toasty.LENGTH_SHORT, true).show();
                telefono = telefono.replace("Fono: ","");
                telefono = telefono.replace("Celular: ","");
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + telefono));
                ventana_telefono.startActivity(callIntent);
            }
        }else{
            varseleccion = 0;
            dep = "";
            telefono = "";
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button llamada_depto;
        TextView dep, nom, tel, cor;

        public ViewHolder(@NonNull View v) {
            super(v);
            ventana_telefono = v.getContext();
            dep = v.findViewById(R.id.dep);
            nom = v.findViewById(R.id.nom);
            tel = v.findViewById(R.id.tel);
            cor = v.findViewById(R.id.cor);
            llamada_depto = v.findViewById(R.id.llamada_depto);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewholder, final int position) {
        //CovidCountry covidCountry = covidCountries.get(position);
        viewholder.dep.setText(datosdepto.get(position).getDepto());
        viewholder.nom.setText(datosdepto.get(position).getNombres());
        viewholder.tel.setText(datosdepto.get(position).getTelefono());
        viewholder.cor.setText(datosdepto.get(position).getCorreo());
        if(datosdepto.get(position).getDepto().equals("Alcalde")){
            viewholder.llamada_depto.setText(R.string.llamar_alcaldia);
        }else{
            viewholder.llamada_depto.setText(R.string.llamar_al_departamento);
        }
        viewholder.llamada_depto.setOnClickListener(new View.OnClickListener() {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ventana_telefono, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    varseleccion = 1;
                    DialogoConfirmacion ms = new DialogoConfirmacion(ventana_telefono, DeptosMuniAdapter.this);
                    ms.getContenido_mensaje().findViewById(R.id.contenido_mensaje);
                    ms.getContenido_mensaje().setText(R.string.act_ptelefono);
                }
                else{
                    varseleccion = 2;
                    telefono = datosdepto.get(position).getTelefono();
                    dep = datosdepto.get(position).getDepto();
                    DialogoConfirmacion ms = new DialogoConfirmacion(ventana_telefono, DeptosMuniAdapter.this);
                    ms.getContenido_mensaje().findViewById(R.id.contenido_mensaje);
                    if (dep.equals("Alcalde")){ dep = "Alcaldía"; }
                    ms.getContenido_mensaje().setText("¿Desea Llamar a "+dep+"?");
                }
            }
        });
    }

    //FILTRO PAISEEES
    public class CustomFilter extends Filter {
        private DeptosMuniAdapter listAdapter;

        private CustomFilter(DeptosMuniAdapter listAdapter) {
            super();
            this.listAdapter = listAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            datosdepto.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                datosdepto.addAll(datos);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final DatosDeptoMuni person : datos) {
                    if (person.getDepto().toLowerCase().contains(filterPattern)) {
                        datosdepto.add(person);
                    }
                    else if (person.getNombres().toLowerCase().contains(filterPattern)){
                        datosdepto.add(person);
                    }
                    else if (person.getTelefono().toLowerCase().contains(filterPattern)){
                        datosdepto.add(person);
                    }
                    else if (person.getCorreo().toLowerCase().contains(filterPattern)){
                        datosdepto.add(person);
                    }
                }
            }
            results.values = datosdepto;
            results.count = datosdepto.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            this.listAdapter.notifyDataSetChanged();
        }
    }
}
