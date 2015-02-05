package com.izv.angel.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class VistaTarea extends View {

    public static Paint pincel;
    public static Bitmap mapaDeBits;
    public static Canvas lienzoFondo;
    private int alto, ancho;
    private float x0=-1,y0=-1,xi=-1,yi=-1;
    private Path[] rutasPath = new Path[3];
    private Coordenadas[] coordenadasPath = new Coordenadas[3];
    public static int opcion =0;
    private float radio=0;
    public static ArrayList<Bitmap> lista = new ArrayList<Bitmap>();

    public class Coordenadas{
        public float x0,y0,xi,yi;

        Coordenadas(float x0, float y0, float xi, float yi) {
            this.x0 = x0;
            this.y0 = y0;
            this.xi = xi;
            this.yi = yi;
        }
    }


    public VistaTarea(Context context) {
        super(context);
        pincel = new Paint();
        pincel.setColor(Color.BLACK);
        pincel.setAntiAlias(true);
        pincel.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < 3; i++) {
            rutasPath[i] = new Path();
            coordenadasPath[i] = new Coordenadas(-1,-1,-1,-1);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mapaDeBits, 0, 0,null );
        switch (opcion){
            case 0:
                for (Path ruta : rutasPath){
                    canvas.drawPath(ruta,pincel);
                }
                break;
            case 1:
                //dibujar rectangulo
                float xorigen = Math.min(x0,xi);
                float xdestino = Math.max(x0,xi);
                float yorigen = Math.min(y0,yi);
                float ydestino = Math.max(y0,yi);
                canvas.drawRect(xorigen,yorigen,xdestino,ydestino,pincel);
                break;
            case 2:
                canvas.drawLine(x0,y0,xi,yi,pincel);
                break;
            case 3:
                canvas.drawCircle(x0, y0, radio, pincel);
                break;
            case 4:

                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mapaDeBits=Bitmap.createBitmap(w, h,Bitmap.Config.ARGB_8888);
        //mapaDeBits.eraseColor(Color.WHITE);
        lienzoFondo= new Canvas(mapaDeBits);
        alto=h;
        ancho=w;
    }


    public boolean onTouchEvent(MotionEvent event) {
        float x;
        float y;
        switch (opcion){
            case 0:
                int puntos = event.getPointerCount();
                for (int i = 0; i < Math.min(puntos,3); i++) {
                    int accion = event.getActionMasked();
                    x = event.getX();
                    y = event.getY();
                    int dedo = event.getPointerId(i);
                    if(dedo>2){
                        continue;
                    }
                    switch (accion) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_POINTER_DOWN:
                            coordenadasPath[dedo].x0 = coordenadasPath[dedo].xi = x;
                            coordenadasPath[dedo].y0 = coordenadasPath[dedo].yi = y;
                            rutasPath[dedo].moveTo(coordenadasPath[dedo].x0, coordenadasPath[dedo].y0);
                            invalidate();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            rutasPath[dedo].quadTo((x+coordenadasPath[dedo].xi)/2,(y+coordenadasPath[dedo].yi)/2,x,y);
                            coordenadasPath[dedo].xi=x;
                            coordenadasPath[dedo].yi=y;
                            invalidate();
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_POINTER_UP:
                        case MotionEvent.ACTION_CANCEL:
                            lienzoFondo.drawPath(rutasPath[dedo],pincel);
                            rutasPath[dedo].reset();
                            coordenadasPath[dedo].x0 = coordenadasPath[dedo].xi=-1;
                            coordenadasPath[dedo].y0 = coordenadasPath[dedo].yi=-1;
                            invalidate();
                            lista.add(mapaDeBits);
                            break;
                    }
                }
                break;
            case 1:
                x = event.getX();
                y = event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x0=x;
                        y0=y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        xi=x;
                        yi=y;
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        xi=x;
                        yi=y;
                        lienzoFondo.drawRect(x0, y0, xi, yi, pincel);
                        break;
                }
                break;
            case 2:
                x = event.getX();
                y = event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x0=x;
                        y0=y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        xi=x;
                        yi=y;
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        xi=x;
                        yi=y;
                        lienzoFondo.drawLine(x0, y0, xi, yi, pincel);
                        invalidate();
                        break;
                }
                break;
            case 3:
                x = event.getX();
                y = event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x0=x;
                        y0=y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        xi = x;
                        yi = y;
                        radio= (float) Math.sqrt(Math.pow(xi - x0, 2) + Math.pow(yi - y0, 2));
                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        xi=x;
                        yi=y;
                        radio= (float) Math.sqrt(Math.pow(xi - x0, 2) + Math.pow(yi - y0, 2));
                        lienzoFondo.drawCircle(x0,y0,radio,pincel);
                        invalidate();
                        radio=0;
                        break;
                }
                break;
        }

        return true;
    }




}
