package ast.bstu.oopproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    int SPLASH_TIME = 2000; //This is 3 seconds
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView t= findViewById(R.id.texthi);
        ImageView i= findViewById(R.id.finger);
        Animation ani= AnimationUtils.loadAnimation(this,R.anim.fadein);
        t.startAnimation(ani);
        Animation ani1= AnimationUtils.loadAnimation(this,R.anim.rotate);
        i.startAnimation(ani1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mySuperIntent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(mySuperIntent);
                //This 'finish()' is for exiting the app when back button pressed from Home page which is ActivityHome
                finish();
            }
        }, SPLASH_TIME);
    }
}