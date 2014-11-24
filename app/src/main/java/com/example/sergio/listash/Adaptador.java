package com.example.sergio.listash;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Sergio on 18/10/2014.
 */
public class Adaptador extends ArrayAdapter {
    private Context contexto;
    private ArrayList<Componente> lista;
    private int recurso;
    private static LayoutInflater i; // Para no crear inflater cada vez que se llame a getView()

    public static class ViewHolder{
        public TextView tvt, tvd1, tvd2;
        public ImageView img;
    }

    public Adaptador(Context context, int resource, ArrayList<Componente> objects) {
        super(context, resource, objects);
        this.contexto = context;
        this.lista = objects;
        this.recurso = resource;
        this.i = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if(convertView == null){
            convertView = i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.tvt = (TextView)convertView.findViewById(R.id.tvTitulo);
            vh.tvd1 = (TextView)convertView.findViewById(R.id.tvdato1);
            vh.tvd2 = (TextView)convertView.findViewById(R.id.tvdato2);
            vh.img = (ImageView) convertView.findViewById(R.id.imagen);

            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        vh.tvt.setText(lista.get(position).getTipo());
        vh.tvd1.setText(lista.get(position).getNombre());
        vh.tvd2.setText(lista.get(position).getPrecio()+ " euros");
        if((lista.get(position).getTipo().equalsIgnoreCase("hardware"))) {
            vh.img.setImageResource(R.drawable.hardware);
        }
        if((lista.get(position).getTipo().equalsIgnoreCase("software"))) {
            vh.img.setImageResource(R.drawable.software);
        }


        return convertView;
    }
}
