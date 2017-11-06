package com.brian.directory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


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
                        Intent override = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        override.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        override.setPackage("com.android.chrome");
                        override.putExtra("branch_force_new_session", true);
                        override.putExtra("branch_used", true);
                        startActivity(override);
                    }
                } else {
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, "No Internet", duration);
                    toast.show();

                }
            }
        });
    }
}
