package group01.smartcar.client.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import group01.smartcar.client.R;

// Adapted from: https://www.instructables.com/A-Simple-Android-UI-Joystick/

public class Joystick extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private float centerX;
    private float centerY;
    private float baseRadius;
    private float hatRadius;
    private JoystickListener joystickCallback;

    private void setupDimensions(){
        centerX = getWidth() / 2f;
        centerY = findViewById(R.id.joystick).getHeight()/2f;
        baseRadius = Math.min(getWidth(), getHeight()) / 4f;
        hatRadius = Math.min(getWidth(), getHeight()) / 5f;

    }

    public Joystick(Context context){
        super(context);

        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }
    }

    public Joystick(Context context, AttributeSet attributes, int style){
        super(context, attributes, style);

        getHolder().addCallback(this);
        setOnTouchListener(this);

        if(context instanceof JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }
    }

    public Joystick(Context context, AttributeSet attributes){
        super(context, attributes);

        getHolder().addCallback(this);
        setOnTouchListener(this);

        if(context instanceof JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }
    }

    private void drawJoystick(float newX, float newY) {
        if (!getHolder().getSurface().isValid()) {
            return;
        }

        final Canvas canvas = getHolder().lockCanvas();
        final Paint colors = new Paint();
        canvas.drawColor(Color.parseColor("#6C6C6C")); // Clear the BG

        colors.setARGB(255, 8, 29, 61); //base
        canvas.drawCircle(centerX, centerY, baseRadius, colors);
        colors.setARGB(255, 13, 1, 23); //base
        canvas.drawCircle(centerX, centerY, (float) (baseRadius * 0.95), colors);

        colors.setARGB(225,35,74,132); //hat
        canvas.drawCircle(newX, newY, hatRadius, colors);

        getHolder().unlockCanvasAndPost(canvas);
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        setupDimensions();
        drawJoystick(centerX, centerY);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    public interface JoystickListener {
        void onJoystickMoved(float xPercent, float yPercent, int id);
    }

    public boolean onTouch(View view, MotionEvent event){
        if (!view.equals(this)) {
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            drawJoystick(centerX, centerY);
            joystickCallback.onJoystickMoved(0, 0, getId());
            return true;
        }

        final float displacement = (float) Math.sqrt((Math.pow(event.getX() - centerX, 2)) + Math.pow(event.getY() - centerY, 2));

        if (displacement < baseRadius) {
            drawJoystick(event.getX(), event.getY());
            joystickCallback.onJoystickMoved((event.getX() - centerX) / baseRadius, (event.getY() - centerY) / baseRadius, getId());

            return true;
        }

        final float ratio = baseRadius / displacement;
        final float constrainedX = centerX + (event.getX() - centerX) * ratio;
        final float constrainedY = centerY + (event.getY() - centerY) * ratio;

        drawJoystick(constrainedX, constrainedY);
        joystickCallback.onJoystickMoved((constrainedX - centerX) / baseRadius, (constrainedY - centerY) / baseRadius, getId());

        return true;
    }



}