package com.example.michal.dialapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private ImageButton bt1, bt2;
    String url = "192168099";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //-------------------------------------------------
        bt2 = (ImageButton) findViewById(R.id.crossButton2);


        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Wait 3 seconds")
                .setNegativeButton(android.R.string.no, null)
                .create();

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Dzwonię pod podany numer")
                .setMessage(" " + url)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, null)
                .create();
        //-------------------------------------------------
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + url));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    private static final int AUTO_DISMISS_MILLIS = 3000;

                    @Override
                    public void onShow(final DialogInterface dialog) {
                        final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                        final CharSequence positiveButtonText = defaultButton.getText();
                        final CountDownTimer timer = new CountDownTimer(AUTO_DISMISS_MILLIS, 100) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                defaultButton.setText(String.format(
                                        Locale.getDefault(), "%s (%d)",
                                        positiveButtonText,
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
                                ));
                            }

                            @Override
                            public void onFinish() {
                                if (((AlertDialog) dialog).isShowing()) {
                                    dialog.dismiss();
                                }
                            }
                        }.start();

                        ((AlertDialog) dialog).setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(intent);
                            }

                        });
                    }
                });
                dialog.show();
            }
        });
        //----------------------------
        bt1 = (ImageButton) findViewById(R.id.crossButton);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + url));
                //  intent.setClassName("com.android.phone","com.android.phone.OutgoingCallBroadcaster");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                alertDialog.show();
                final CountDownTimer timer = new CountDownTimer(3000, 1) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        alertDialog.setTitle("Please wait " + (millisUntilFinished / 1000) + " sec");
                    }

                    @Override
                    public void onFinish() {
                        alertDialog.dismiss();
                        startActivity(intent);
                    }
                }.start();

                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timer.cancel();
                    }

                });
            }
        });
    }
}