package edu.sharif.self_description;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;


import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

class FirstCreation {
    public static boolean firstCreation = true;
}

public class MainActivity extends AppCompatActivity {
    private ImageView emailButton, phoneButton;


    private LinearLayout paragraphListHolder;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch dayNightSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (FirstCreation.firstCreation) {
            Toast toast = Toast.makeText(this, R.string.welcome, Toast.LENGTH_SHORT);
            showToastManyTimes(toast);
            FirstCreation.firstCreation = false;
        }

        emailButton = findViewById(R.id.emailButton);
        phoneButton = findViewById(R.id.phoneButton);
        dayNightSwitch = findViewById(R.id.day_night_switch);
        paragraphListHolder = findViewById(R.id.paragraph_list_holder);

        String st = readJsonFile();
        JSONArray paragraphs = parseJsonStringToJavaObject(st);
        addParagraphsToLayout(paragraphListHolder, paragraphs);


        emailButton.setOnClickListener(view -> sendEmail());

        phoneButton.setOnClickListener(view -> makePhoneCall());

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
                for (int i = 0; i < 5; i++) {
                    Log.d("hi", "run: ");
                    toast.show();
                    try {
                        Thread.sleep(2050);
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

    String readJsonFile() {
        InputStream inputStream = getResources().openRawResource(R.raw.json_description);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toString();
    }


    JSONArray parseJsonStringToJavaObject(String jsonString) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    void addParagraphsToLayout(LinearLayout paragraphListHolder, JSONArray paragraphs) {
        for (int i = 0; i < paragraphs.length(); i++) {
            JSONObject paragraph = null;
            try {
                paragraph = paragraphs.getJSONObject(i);
                paragraphListHolder.addView(createParagraphView(paragraph));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private View createParagraphView(JSONObject paragraph) {
        View paragraphView = getLayoutInflater().inflate(R.layout.paragraph_holder, null, false);
        TextView titleTextView = (TextView) paragraphView.findViewById(R.id.paragraph_title);
        TextView textTextView = (TextView) paragraphView.findViewById(R.id.paragraph_content);
        try {
            titleTextView.setText(paragraph.getString("title"));
            textTextView.setText(paragraph.getString("content"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return paragraphView;
    }

}