package com.example.sergio.listash;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Sergio on 18/10/2014.
 */
public class Componente implements Parcelable{
    private String tipo;
    private String nombre;
    private int precio;

    public Componente(int precio, String tipo, String nombre) {
        this.precio = precio;
        this.tipo = tipo;
        this.nombre = nombre;
    }

    public Componente() {
    }
    public Componente(Parcel in){
        this.tipo = in.readString();
        this.nombre = in.readString();
        this.precio = in.readInt();
    }



    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tipo);
        parcel.writeString(nombre);
        parcel.writeInt(precio);
    }
    public static final Parcelable.Creator<Componente> CREATOR = new Parcelable.Creator<Componente>() {
        public Componente createFromParcel(Parcel in) {
            return new Componente(in);
        }

        public Componente[] newArray(int size) {

            return new Componente[size];
        }
    };
}
