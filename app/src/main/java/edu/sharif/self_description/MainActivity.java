package edu.sharif.self_description;

import static android.app.PendingIntent.getActivity;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import edu.sharif.self_description.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;


import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;
    private ImageView emailButton, phoneButton;
    private TextView description;

    private Switch dayNightSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toast.makeText(this, R.string.app_name, Toast.LENGTH_LONG).show();

        description = findViewById(R.id.description);
        emailButton = findViewById(R.id.emailButton);
        phoneButton = findViewById(R.id.phoneButton);
        dayNightSwitch = findViewById(R.id.day_night_switch);


        try {
            InputStream inputStream = this.getResources().openRawResource(R.raw.description);

            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            description.setText(Html.fromHtml(new String(b)));
        } catch (Exception e) {
            e.printStackTrace();
        }


        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });

        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                makePhoneCall();
            }
        });

        dayNightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (dayNightSwitch.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        boolean isNightModeOn = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
        dayNightSwitch.setChecked(isNightModeOn);
    }

    @Override
    public void recreate() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        startActivity(getIntent());

        overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
    }

    private void showToastManyTimes(Toast toast) {

        Thread t = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                    toast.show();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    toast.cancel();
                }
            }
        };
        t.start();
    }


    void sendEmail() {
        Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
        selectorIntent.setData(Uri.parse("mailto:"));
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{this.getResources().getString(R.string.email_address)});
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.setSelector(selectorIntent);
        startActivity(Intent.createChooser(emailIntent, "Sending Email to..."));

    }


    void makePhoneCall() {
        String dialStr = "tel:" + this.getResources().getString(R.string.telephone_num);
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse(dialStr));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(callIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


}