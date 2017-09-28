package com.brian.directory;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;

/**
 * Created by bchang on 9/26/17.
 */

public class Employee extends Activity {

    private BranchUniversalObject buo;
    private LinkProperties lp;
    private String drawableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        final String employee_name = intent.getStringExtra("employee_name");
        final String employee_title = intent.getStringExtra("employee_title");
        final String employee_desc = intent.getStringExtra("employee_desc");

        // Capture the layout's TextView and set the string as its text
        TextView name = (TextView) findViewById(R.id.name);
        TextView title = (TextView) findViewById(R.id.title);
        TextView desc = (TextView) findViewById(R.id.desc);
        ImageView image = (ImageView) findViewById(R.id.image);

        String[] first_last = employee_name.split("\\s+");
        String first = first_last[0].toLowerCase();
        String last = first_last[1].toLowerCase();
        drawableName = first + "_" + String.valueOf(last.charAt(0));
        Log.i("NAME", drawableName);

//        int id = getResources().getIdentifier("com.brian.directory:drawable/" + drawableName, null, null);
//        image.setImageResource(id);
        int id = getResources().getIdentifier(drawableName, "drawable", getPackageName());
        image.setImageResource(id);

//        image.setImageDrawable(getDrawable(R.drawable.drawableName));


        name.setText(employee_name);
        title.setText(employee_title);
        desc.setText(employee_desc);

        buo = new BranchUniversalObject()
                .setTitle(employee_name)
                .setContentDescription(employee_title)
//                        .setContentImageUrl("https://lorempixel.com/400/400")
                .addContentMetadata("full_name", employee_name)
                .addContentMetadata("full_title", employee_title)
                .addContentMetadata("full_desc", employee_desc);

        lp = new LinkProperties();

        Button shareButton = (Button) findViewById(R.id.button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ShareSheetStyle ss = new ShareSheetStyle(Employee.this, "Check this out!", "Branch Employee Directory");

                buo.showShareSheet(Employee.this, lp,  ss,  new Branch.BranchLinkShareListener() {
                    @Override
                    public void onShareLinkDialogLaunched() {
                        Log.i("Launch", "Launch");
                    }
                    @Override
                    public void onShareLinkDialogDismissed() {
                        Log.i("Dismiss", "Dismiss");
                    }
                    @Override
                    public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {
                        Log.i("Sent", "Sent");
                    }
                    @Override
                    public void onChannelSelected(String channelName) {
                        Log.i("Channel", "Channel");
                    }
                });
            }
        });

    }


}


