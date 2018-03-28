package com.sliit.budgetcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.sliit.budgetcalculator.Utils.IEAdapter;
import com.sliit.budgetcalculator.Utils.IEDBHelper;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private IEDBHelper dbHelper;
    private IEAdapter adapter;
    private String filter = "";
    private String sort = "desc";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //initialize the variables
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //populateSortSpinner();
        //populate recyclerview
        populaterecyclerView(filter,sort);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabMove);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Home.class));
            }
        });


    }

//    private void populateSortSpinner() {
//        String[] data= {"Ascending","Descending"};
//        Spinner sortspinner = (Spinner) findViewById(R.id.sortSpinner);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,data);
//
//        sortspinner.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//
//        final ArrayAdapter<CharSequence> sortadapter = ArrayAdapter.createFromResource(this,
//                R.array.sortOptions, android.R.layout.simple_spinner_item);
//        sortadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sortspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                sort = adapterView.getSelectedItem().toString();
//                populaterecyclerView(filter,sort);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                populaterecyclerView(filter,sort);
//            }
//        });
//
//
//    }

    private void populaterecyclerView(String filter,String sort){
        dbHelper = new IEDBHelper(this);
        adapter = new IEAdapter(dbHelper.IElist(filter,sort), this, mRecyclerView);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);

        MenuItem item = menu.findItem(R.id.filterSpinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filterOptions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filter = parent.getSelectedItem().toString();
                populaterecyclerView(filter,sort);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                populaterecyclerView(filter,sort);
            }
        });

        spinner.setAdapter(adapter);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.addMenu:
                goToAddUserActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToAddUserActivity(){
        Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
