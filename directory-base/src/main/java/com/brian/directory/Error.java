package com.brian.directory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;


/**
 * Created by bchang on 11/3/17.
 */

public class Error extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        Button retryButton = (Button) findViewById(R.id.retry);
        retryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context = v.getContext();
                ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {
                    Intent intent = getIntent();
                    if (intent.hasExtra("saved")) {
                        String uri = intent.getExtras().getString("saved");
                        Log.i("uri", uri);
                        Intent i = new Intent(context, Error.class);
                        i.putExtra("branch", uri);
                        i.putExtra("branch_force_new_session", true);
                        finish();
                        startActivity(i);
                    }
                } else {
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, "No Internet", duration);
                    toast.show();

                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // Branch init
        Branch.getInstance(this).initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    Log.i("BRANCH SDK", referringParams.toString());
                } else {
                    Log.i("BRANCH SDK", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }
}
