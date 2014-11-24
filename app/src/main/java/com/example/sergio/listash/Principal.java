package com.example.sergio.listash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class Principal extends Activity {
    /*****DECLARAMOS VARIABLES******/
    ArrayList<Componente> datos;
    private Adaptador adapter;
    private RadioGroup rg;
    private EditText etN;
    private EditText etP;
    private String tipo;
    private String nombre;
    private int precio;
    static  ListView lv = null;
    private EditText etNV;
    private EditText etPV;
    private Componente aux;
    private boolean unico = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        start(); //metodo que reune lo necesario al arrancar la app
        visualizador();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuopciones, menu);
        return true;
    }
    @Override
    protected void onSaveInstanceState(Bundle savingInstanceState) {
        super.onSaveInstanceState(savingInstanceState);
        savingInstanceState.putParcelableArrayList("xml", datos);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        datos = savedInstanceState.getParcelableArrayList("xml");
        adapter = new Adaptador(this, R.layout.elemento, datos);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add) {
            return add();
        }else if(id == R.id.action_cargar){

             crear();
            return true;
        }else if (id == R.id.action_salvar){
            leer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Al hacer long clic sobre item del ListView */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menucontextual, menu);
    }
    // Vengo de DialogoEdit
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            datos=data.getParcelableArrayListExtra("datos");
            adapter.notifyDataSetChanged();

            adapter = new Adaptador(this, R.layout.elemento, datos);
            lv.setAdapter(adapter);


        }else{
            tostada("Error");
        }
        adapter.notifyDataSetChanged();
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int id = item.getItemId();
        if(id == R.id.action_add){
            return add();
        }else if (id == R.id.action_borrar){
            return del(info);
        }else if (id == R.id.action_edit){
            return edit(info);
        }
        return super.onContextItemSelected(item);
    }
    public void visualizador(){
        adapter = new Adaptador(this, R.layout.elemento, datos);
        final ListView lv = (ListView)findViewById(R.id.listavista);
        lv.setAdapter(adapter);
        registerForContextMenu(lv);
    }
    // añadiendo datos manualmente y pasando al adaptador

    public void start(){
        lv = (ListView)findViewById(R.id.listavista);
        // AÑADIR DATOS INICIALES ( DESACTIVADO )
        /* datos = new ArrayList<Componente>();
        Componente comp = new Componente();
        comp.setTipo("Hardware");
        comp.setNombre("Fuente de alimentacion");
        comp.setPrecio(50);

        datos.add(comp);
        comp = new Componente();
        comp.setTipo("Software");
        comp.setNombre("Microsoft Office 2013");
        comp.setPrecio(150);

        datos.add(comp);
        comp = new Componente();
        comp.setTipo("Hardware");
        comp.setNombre("WebCam");
        comp.setPrecio(20);

        datos.add(comp);


        adapter = new Adaptador(this, R.layout.elemento, datos);

        lv.setAdapter(adapter);
        registerForContextMenu(lv);*/
        datos = new ArrayList<Componente>();
        adapter = new Adaptador(this, R.layout.elemento, datos);
        lv.setAdapter(adapter);
        registerForContextMenu(lv);

    }

    public boolean add(){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.tv_titulodialogo);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogoadd, null);
        alert.setView(vista);

        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rg  = (RadioGroup) vista.findViewById(R.id.grupo);
                int selectedId = rg.getCheckedRadioButtonId();//saco el ID del radiobutton que seleccionamos
                RadioButton radio = (RadioButton) rg.findViewById(selectedId);
                etN = (EditText) vista.findViewById(R.id.etNombre);
                etP = (EditText) vista.findViewById(R.id.etPrecio);
                if(radio.getText().toString().equalsIgnoreCase("Software")){
                    tipo = "Software";
                }else{
                    tipo = "Hardware";
                }
                nombre = etN.getText().toString();
                try {
                    unico = true;
                    precio = (Integer.valueOf(etP.getText().toString()));
                    // COMPROBAMOS REGISTRO UNICO
                    if(!datos.isEmpty()){

                        for (int i = 0 ; i<datos.size();i++){
                            if(datos.get(i).getNombre().equalsIgnoreCase(nombre)){
                                tostada("Ya hay existe ese componente con ese nombre");
                                unico = false;
                            }
                        }
                        if(unico){
                            Componente compo = new Componente(precio, tipo, nombre);
                            datos.add(compo);
                        }
                    }else{
                        Componente compo = new Componente(precio, tipo, nombre);
                        datos.add(compo);
                        System.out.println("pasa por aqui");
                    }

                }catch (NumberFormatException e){
                    tostada("Campo 'precio' incompleto");
                }

                adapter.notifyDataSetChanged();
            }
            });
        alert.show();

        return true;

        }

    public boolean del(AdapterView.AdapterContextMenuInfo info){

        int indice = info.position;

        datos.remove(indice);
        adapter.notifyDataSetChanged();
        return true;
    }

    public boolean edit(AdapterView.AdapterContextMenuInfo info){
        /*
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.tv_titulodialogoedit);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogoedit, null);
        alert.setView(vista);
        final int indice = info.position;
        etNV = (EditText) vista.findViewById(R.id.etNViejo);
        etPV = (EditText) vista.findViewById(R.id.etPViejo);
        etNV.setText(datos.get(indice).getNombre());
        etPV.setText(String.valueOf(datos.get(indice).getPrecio()));
        alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        datos.get(indice).setPrecio((Integer.valueOf(etPV.getText().toString())));
                        datos.get(indice).setNombre(etNV.getText().toString());
                        adapter.notifyDataSetChanged();
                    }
                });
                adapter.notifyDataSetChanged();
        alert.show();
        */
        final int IdActividad = 1;
        final int indice = info.position;
        Intent edit = new Intent(this, DialogoEdit.class);
        Bundle b = new Bundle();
        b.putString("nombre",datos.get(indice).getNombre());
        b.putString("precio",String.valueOf(datos.get(indice).getPrecio()));
        b.putInt("indice", indice);
        b.putParcelableArrayList("datos",datos);
        edit.putExtras(b);
        startActivityForResult(edit, IdActividad );

        return true;
    }

    public void ordenar(View v){
        for (int i = 0; i <datos.size()-1; i++){
            for(int j = i+1; j<datos.size(); j++){
                if(datos.get(i).getTipo().compareToIgnoreCase(datos.get(j).getTipo())>0){
                    aux = datos.get(j);
                    datos.set(j,datos.get(i));
                    datos.set(i,aux);
                }
            }
        }
        tostada("Ordenados " +datos.size()+" elementos.");
        adapter.notifyDataSetChanged();
    }
    private void tostada(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void crear() {

        try {

            FileOutputStream fosxml = new FileOutputStream(new File(getExternalFilesDir(null),"datos.xml"));
            System.out.println(getExternalFilesDir(null));
            XmlSerializer docxml = Xml.newSerializer();
            docxml.setOutput(fosxml, "UTF-8");
            docxml.startDocument(null, Boolean.valueOf(true));
            docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            docxml.startTag(null, "componentes");
            for (int i = 0; i < datos.size(); i++) {
                if(datos.get(i).getTipo().compareToIgnoreCase("Software") == 0) {
                    docxml.startTag(null, "software");
                    docxml.attribute(null, "nombre", datos.get(i).getNombre());
                    docxml.text(String.valueOf(datos.get(i).getPrecio()));
                    docxml.endTag(null, "software");

                }else{
                    docxml.startTag(null, "hardware");
                    docxml.attribute(null, "nombre", datos.get(i).getNombre());
                    docxml.text(String.valueOf(datos.get(i).getPrecio()));
                    docxml.endTag(null, "hardware");

                }
            }

            docxml.endDocument();
            docxml.flush();
            fosxml.close();
            tostada("Guardado correctamente");
        } catch (IOException e) {
            e.printStackTrace();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();

        } catch (IllegalStateException e) {
            e.printStackTrace();

        }

    }
    public void leer (){

        Componente comp;
        XmlPullParser lectorxml = Xml.newPullParser();

        try {
            lectorxml.setInput(new FileInputStream(new File(getExternalFilesDir(null),"datos.xml")),"utf-8");
            int evento = lectorxml.getEventType();
            while (evento != XmlPullParser.END_DOCUMENT){
                if(evento == XmlPullParser.START_TAG){
                    String etiqueta = lectorxml.getName();
                    System.out.println(etiqueta);
                    if(etiqueta.compareToIgnoreCase("Software")==0){
                        comp = new Componente();
                        String nombre = lectorxml.getAttributeValue(null,"nombre");

                        String precio = lectorxml.nextText();
                        comp.setNombre(nombre);
                        try {
                            comp.setPrecio(Integer.valueOf(precio));
                        }catch (NumberFormatException e){

                        }
                        comp.setTipo("Software");
                        datos.add(comp);

                    }else{
                        if (etiqueta.compareToIgnoreCase("Hardware") == 0) {
                            comp = new Componente();
                            String nombre = lectorxml.getAttributeValue(null, "nombre");
                            String precio = lectorxml.nextText();
                            comp.setNombre(nombre);
                            try {
                                comp.setPrecio(Integer.valueOf(precio));
                            } catch (NumberFormatException e) {

                            }
                            comp.setTipo("Hardware");
                            datos.add(comp);

                        } else {

                        }

                    }

                }
                evento = lectorxml.next();
            }
            adapter.notifyDataSetChanged();
            tostada("Cargado correctamente");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
