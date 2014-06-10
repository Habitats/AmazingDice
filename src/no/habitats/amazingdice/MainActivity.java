package no.habitats.amazingdice;

import no.habitats.amazingdice.ShakeDetector.OnShakeListener;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
  // good practice to do it like this
  private static final String TAG = MainActivity.class.getName();

  private TextView description;
  private TextView result;

  private SensorManager sensorManager;
  private Sensor accelerometer;
  private ShakeDetector shakeDetector;
  private boolean loaded;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    result = (TextView) findViewById(R.id.textViewResult);
    description = (TextView) findViewById(R.id.textViewDecription);

    sensorStuff();
  }

  private void loadDice() {
    loaded = true;
    description.setText("Shake it!");
  }

  private void sensorStuff() {
    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    shakeDetector = new ShakeDetector(new OnShakeListener() {


      @Override
      public void onShake() {
        log("shake it babeh!");
        if (loaded) {
          rollDice();
        }
      }
    });
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    loadDice();
    return super.onTouchEvent(event);
  }

  @Override
  public void onResume() {
    super.onResume();
    log("resumed!");
    sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI);
  }

  private void log(String msg) {
    boolean enableToast = false;
    if (enableToast) {
      Toast toastMessage = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
      toastMessage.setGravity(Gravity.CENTER, (int) (Math.random() * 300), (int) (Math.random() * 600));
      toastMessage.show();
    } else {
      Log.d(TAG, msg);
    }
  }


  @Override
  public void onPause() {
    super.onPause();
    log("pause!");
    sensorManager.unregisterListener(shakeDetector);
  }

  private void rollDice() {
    int diceValue = (int) (Math.random() * 6 + 1);
    result.setText(Integer.toString(diceValue));
    description.setText("Load it!");
    loaded = false;
    playSound();
  }


  private void playSound() {
    MediaPlayer player = MediaPlayer.create(this, R.raw.pop);
    player.start();

    // good practice
    player.setOnCompletionListener(new OnCompletionListener() {

      @Override
      public void onCompletion(MediaPlayer mp) {
        mp.release();
      }
    });
  }

  // @Override
  // public boolean onCreateOptionsMenu(Menu menu) {
  // // Inflate the menu; this adds items to the action bar if it is present.
  // getMenuInflater().inflate(R.menu.main, menu);
  // return true;
  // }
}
