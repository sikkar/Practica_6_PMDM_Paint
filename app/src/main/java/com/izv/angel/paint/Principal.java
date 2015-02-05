package com.izv.angel.paint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class Principal extends ActionBarActivity implements ColorPickerDialog.OnColorSelectedListener {

     private int op;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.actividad_principal);
        setContentView(new VistaTarea(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cambiarColor) {
            op= VistaTarea.opcion;
            cambiarColor(op);
        }
        if (id == R.id.action_lapiz);{
            VistaTarea.opcion = 0;
            VistaTarea.pincel.setXfermode(null);
        }
        if (id == R.id.action_rectangle){
            VistaTarea.opcion = 1;
            VistaTarea.pincel.setXfermode(null);
        }
        if (id == R.id.action_line){
            VistaTarea.opcion = 2;
            VistaTarea.pincel.setXfermode(null);
        }
        if (id == R.id.action_circle){
            VistaTarea.opcion = 3;
            VistaTarea.pincel.setXfermode(null);
        }
        if (id == R.id.action_grosor){
            stroke();
        }
        if (id == R.id.action_borrar){
            borrar();
        }
        if (id == R.id.action_deshacer){
            VistaTarea.lista.remove(VistaTarea.lista.size()-1);
            Log.v("size", VistaTarea.lista.size() + "");
        }
        if(id ==R.id.action_guardar){
            File archivo = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "paint.jpg");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(archivo);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            VistaTarea.mapaDeBits.compress(Bitmap.CompressFormat.PNG, 90, fos);
        }
        return super.onOptionsItemSelected(item);
    }


    public void cambiarColor(final int op){
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this, VistaTarea.pincel.getColor(), new ColorPickerDialog.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                VistaTarea.pincel.setColor(color);
                VistaTarea.opcion=op;
            }

        });
        colorPickerDialog.show();
    }

    @Override
    public void onColorSelected(int color) {
        VistaTarea.opcion=op;
    }

    public void stroke(){
        AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setTitle("Selector de Trazo:");
        LayoutInflater inflater= LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.detalle, null);
        dialog.setView(vista);
        final EditText et1 = (EditText) vista.findViewById(R.id.etTrazo);
        et1.setText(String.valueOf(VistaTarea.pincel.getStrokeWidth()));
        final SeekBar sk = (SeekBar)vista.findViewById(R.id.seekBar);
        sk.setProgress((int)VistaTarea.pincel.getStrokeWidth());
        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                et1.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBar.setSecondaryProgress(seekBar.getProgress());
                int value=seekBar.getProgress();
                et1.setText(String.valueOf(value));
            }
        });
        dialog.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                       VistaTarea.pincel.setStrokeWidth(Integer.parseInt(et1.getText().toString()));
                    }
                });
        dialog.setNegativeButton("Cancelar",null);
        dialog.show();
    }

    public void borrar(){
        VistaTarea.pincel.setStrokeWidth(15);
        VistaTarea.opcion=0;
        VistaTarea.pincel.setAlpha(0xFF);
        VistaTarea.pincel.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

}
