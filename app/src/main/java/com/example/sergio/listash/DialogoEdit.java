package com.example.sergio.listash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;


public class DialogoEdit extends Activity  {
    private Adaptador adapter;
    private ListView lv = null;
    private Boolean unico = true;
    ArrayList<Componente> datos;
    private TextView etPV;
    private TextView etNV;
    Bundle extras;
    int indice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogoedit);
        etNV = (TextView) findViewById(R.id.etNViejo);
        etPV = (TextView) findViewById(R.id.etPViejo);
        datos = new ArrayList<Componente>();

        extras= getIntent().getExtras();
        if(extras!=null){
            etNV.setText(extras.getString("nombre"));
            etPV.setText(extras.getString("precio"));
            indice = extras.getInt("indice");
            datos = extras.getParcelableArrayList("datos");

        }
        start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dialogo_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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


    }

    public void editar(View v){
        unico = true;
        for (int i = 0 ; i<datos.size();i++){
            if(datos.get(i).getNombre().equalsIgnoreCase(etNV.getText().toString())){
                unico = false;
            }else{

            }
        }
        if(unico){
            datos.get(indice).setPrecio((Integer.valueOf(etPV.getText().toString())));
            datos.get(indice).setNombre(etNV.getText().toString());
            adapter.notifyDataSetChanged();
        }
        Intent data = new Intent();
        setResult(Activity.RESULT_OK,data.putParcelableArrayListExtra("datos",datos));

        finish(); // Para cerrar este intent e ir al anterior
    }
    public void start(){
        lv = Principal.lv;
        adapter = new Adaptador(this, R.layout.elemento, datos);
        lv.setAdapter(adapter);

    }

}
