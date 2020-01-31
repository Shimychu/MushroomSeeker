package com.example.jimmy.mushroomseeker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class HelpScreenActivity extends AppCompatActivity {

    TextView tvHelpDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_screen);
        htmlHelper();
    }

    private void htmlHelper(){
        tvHelpDescription = findViewById(R.id.tvHelpDescription);
        tvHelpDescription.setText(Html.fromHtml(getString(R.string.helpDescription)));
        tvHelpDescription.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
