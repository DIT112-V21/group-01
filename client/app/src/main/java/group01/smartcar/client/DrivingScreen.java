package group01.smartcar.client;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import static group01.smartcar.client.Status.ACTIVE;

public class DrivingScreen extends AppCompatActivity implements JoystickView.JoystickListener {
    CarControl car;
    protected ImageView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Test", "DrivingScreen.onCreate: Reached!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);
        registerComponentCallbacks();
        cameraView = findViewById(R.id.imageView);
        car = new CarControl(this.getApplicationContext(), cameraView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        car.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("OnResume");
        // Reconnect to MQTT server if application is resumed
        car.resume();
    }

    private void registerComponentCallbacks() {
        Log.d("Test", "DrivingScreen.registerComponentCallbacks: Reached");
        findViewById(R.id.start_button).setOnClickListener(this::onStartClick);
        findViewById(R.id.stop_button).setOnClickListener(this::onStopClick);
    }

    private void onStartClick(View view) {
        car.start();
    }

    private void onStopClick(View view) {
        car.stop();
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int id){
        int angle = (int)((xPercent) * 100);
        int speed = (int)((yPercent) * -100);
        Log.d("joystick", "angle: " + angle + " speed: " + speed );
        if(car.getStatus() == ACTIVE) {
            car.setSteeringAngle(angle);
            car.throttle(speed);
        }
    }
}