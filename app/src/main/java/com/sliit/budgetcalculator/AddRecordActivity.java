package com.sliit.budgetcalculator;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sliit.budgetcalculator.Utils.DatePickerFragment;
import com.sliit.budgetcalculator.Utils.IEDBHelper;
import com.sliit.budgetcalculator.model.IncomeExpense;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;

public class AddRecordActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    private EditText mAmountEditText;
    private EditText mDespEditText;
    private TextView mDateEditText;
    private Button mAddBtn;

    private IEDBHelper dbHelper;
    private String ie_type="";

    private Spinner spinner;

    private List<String> selectedTagList = new ArrayList<String>();;
    private String[] tagsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        //init
        mAmountEditText = (EditText) findViewById(R.id.Amount);
        mDespEditText = (EditText) findViewById(R.id.Description);
        mDateEditText = (TextView) findViewById(R.id.dateadd);
        mAddBtn = (Button) findViewById(R.id.addNewUserButton);

        mDateEditText.setText("Today");

        //listen to add button click
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call the save person method
                saveIncomeOrExpense();
            }
        });

        //change date button
        Button button = (Button) findViewById(R.id.buttonChangeDate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

    }

    public void loadTagsToDropDown(){
        spinner = (Spinner) findViewById(R.id.spinner);

        if(ie_type.equals("income")){
            tagsArray = getResources().getStringArray(R.array.incomeTagOptions);
        }
        else{
            tagsArray = getResources().getStringArray(R.array.expensetagOptions);
        }

        tagsArray.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddRecordActivity.this,
                android.R.layout.simple_spinner_item, tagsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void loadTagToWindow() {

        String[] selectedTagArray = selectedTagList.toArray(new String[selectedTagList.size()]);
        TagGroup mTagGroup = (TagGroup) findViewById(R.id.tag_group);
        mTagGroup.setTags(selectedTagArray);

    }

    private void saveIncomeOrExpense() {
        String amount = mAmountEditText.getText().toString().trim();
        String desp = mDespEditText.getText().toString().trim();
        String date = mDateEditText.getText().toString().trim();

        //checking if its "Today"
        if(date.equals("Today")){
            Calendar c = Calendar.getInstance();
            String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
            date = currentDateString;
        }

        dbHelper = new IEDBHelper(this);

        if (amount.isEmpty()) {

            Toast.makeText(this, "You must enter a amount", Toast.LENGTH_SHORT).show();
        }

        else if (desp.isEmpty()) {

            Toast.makeText(this, "You must enter a description", Toast.LENGTH_SHORT).show();
        }

        else if (ie_type.equals("")) {

            Toast.makeText(this, "You must enter a type", Toast.LENGTH_SHORT).show();
        }

        else{
            //create new incomeExpense
            IncomeExpense incomeExpense = new IncomeExpense(desp, date, Double.parseDouble(amount), ie_type);
            dbHelper.saveNewIE(incomeExpense);

            //finally redirect back home
            // NOTE you can implement an sqlite callback then redirect on success delete
            goBackHome();
        }


    }

    private void goBackHome() {
        startActivity(new Intent(AddRecordActivity.this, MainActivity.class));
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        mDespEditText = (EditText) findViewById(R.id.Description);
        switch (view.getId()) {
            case R.id.incomeRadioBtn:
                if (checked)
                    ie_type = "income";
                mDespEditText.setText(ie_type.toUpperCase() + " : ");
                loadTagsToDropDown();
                break;
            case R.id.expenseRadioBtn:
                if (checked)
                    ie_type = "expenses";
                mDespEditText.setText(ie_type.toUpperCase() + " : ");
                loadTagsToDropDown();
                break;
        }

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        TextView textView = (TextView) findViewById(R.id.dateadd);
        textView.setText(currentDateString);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
//        if(selectedTagList.size()==1){
//            selectedTagList.remove(0);
//        }
        String selectedItem = (String) parent.getSelectedItem();
        selectedTagList.add(selectedItem);
        loadTagToWindow();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
//        selectedTagList.remove(selectedTagList);
//        loadTagToWindow();
    }
}
