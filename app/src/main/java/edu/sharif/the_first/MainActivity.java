package edu.sharif.the_first;

import static android.app.PendingIntent.getActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import edu.sharif.the_first.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;
    private ImageView emailButton, phoneButton;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, R.string.app_name, Toast.LENGTH_LONG).show();

        description = findViewById(R.id.description);
        emailButton = findViewById(R.id.emailButton);
        phoneButton = findViewById(R.id.phoneButton);


        try {
            InputStream inputStream = this.getResources().openRawResource(R.raw.description);

            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            description.setText(Html.fromHtml(new String(b)));
        } catch (Exception e) {
            e.printStackTrace();
//            description.setText("Error: can't show help.");
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
                makePhoneCall();
            }
        });
    }

    void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
//        emailIntent.setDataAndType(Uri.parse("mailto:"), "text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{this.getResources().getString(R.string.email_address)});

        try {
            startActivity(Intent.createChooser(emailIntent, "Choose"));
        } catch (Exception e) {
            Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
        }
    }


    void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dialStr = "tel:" + this.getResources().getString(R.string.telephone_num);
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dialStr)));
        }

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