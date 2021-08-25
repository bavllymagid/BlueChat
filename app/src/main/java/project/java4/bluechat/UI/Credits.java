package project.java4.bluechat.UI;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import project.java4.bluechat.R;

public class Credits extends AppCompatActivity {
    private Button b;
    private Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        b = (Button) findViewById(R.id.button);
        TextView credits = (TextView)findViewById(R.id.List_of_credits) ;
        String C = "Credits: \n"+ "Sief Essam \n" + "Bavlly Maged \n" +"Ahmed Tawfik \n" + "Shehab Ihab \n"+"Eslam Essam \n"+"Omar Khaled \n";
        credits.setText(C);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.setVisibility(View.GONE);
                animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.credits);
                credits.setVisibility(View.VISIBLE);
                credits.startAnimation(animation);
            }
        });
    }
}
