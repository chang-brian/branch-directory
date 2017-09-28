package com.brian.directory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

public class MainActivity extends AppCompatActivity {


    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView lv = (ListView)findViewById(R.id.listViewEmployees);
        final ArrayList<String> arrayEmployees = new ArrayList<>();
        final ArrayList<String> arrayNames = new ArrayList<>();
//        final ArrayList<String> arrayTitles = new ArrayList<>();
//        final ArrayList<String> arrayDesc = new ArrayList<>();
        final Map<String, String[]> myMap = new HashMap<String, String[]>();
        arrayEmployees.addAll(Arrays.asList(getResources().getStringArray(R.array.array_employees)));
        for (String employee: arrayEmployees) {
            String name = employee.split(",", 3)[0];
            String title = employee.split(",", 3)[1];
            String desc = employee.split(",", 3)[2];
            arrayNames.add(name);
            myMap.put(name, new String[]{title, desc});

        }

        adapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                arrayNames);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Intent intent = new Intent(MainActivity.this, Employee.class);
                String name = (String)adapter.getItemAtPosition(position);
                String title = myMap.get(name)[0];
                String desc = myMap.get(name)[1];
                intent.putExtra("employee_name", name);
                intent.putExtra("employee_title", title);
                intent.putExtra("employee_desc", desc);
                startActivity(intent);
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
                    String name = referringParams.optString("full_name", "");
                    String title = referringParams.optString("full_title", "");
                    String desc = referringParams.optString("full_desc", "");
                    if (!name.equals("") && !title.equals("") && !desc.equals("")) {
                        Intent intent = new Intent(MainActivity.this, Employee.class);
                        intent.putExtra("employee_name", name);
                        intent.putExtra("employee_title", title);
                        intent.putExtra("employee_desc", desc);
                        startActivity(intent);
                    }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }
}

