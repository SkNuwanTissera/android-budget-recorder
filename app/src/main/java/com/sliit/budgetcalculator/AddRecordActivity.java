package com.sliit.budgetcalculator;


import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sliit.budgetcalculator.Utils.DatePickerFragment;
import com.sliit.budgetcalculator.Utils.IEDBHelper;
import com.sliit.budgetcalculator.model.IncomeExpense;

import java.text.DateFormat;
import java.util.Calendar;

public class AddRecordActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private EditText mAmountEditText;
    private EditText mDespEditText;
    private TextView mDateEditText;
    private Button mAddBtn;

    private IEDBHelper dbHelper;
    private String ie_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        //init
        mAmountEditText = (EditText)findViewById(R.id.Amount);
        mDespEditText = (EditText)findViewById(R.id.Description);
        mDateEditText = (TextView) findViewById(R.id.dateadd);
        mAddBtn = (Button)findViewById(R.id.addNewUserButton);

        mDateEditText.setText("Today");

        //listen to add button click
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call the save person method
                savePerson();
            }
        });

        //change date button
        Button button = (Button) findViewById(R.id.buttonChangeDate);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

    }

    private void savePerson(){
        String amount = mAmountEditText.getText().toString().trim();
        String desp = mDespEditText.getText().toString().trim();
        String date = mDateEditText.getText().toString().trim();
        dbHelper = new IEDBHelper(this);

        if(amount.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter a amount", Toast.LENGTH_SHORT).show();
        }

        if(desp.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter a description", Toast.LENGTH_SHORT).show();
        }

        if(date.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter a date", Toast.LENGTH_SHORT).show();
        }

        //create new incomeExpense
        IncomeExpense incomeExpense = new IncomeExpense(desp,date,Double.parseDouble(amount),ie_type);
        dbHelper.saveNewIE(incomeExpense);

        //finally redirect back home
        // NOTE you can implement an sqlite callback then redirect on success delete
        goBackHome();

    }

    private void goBackHome(){
        startActivity(new Intent(AddRecordActivity.this, MainActivity.class));
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        mDespEditText = (EditText)findViewById(R.id.Description);
        switch(view.getId()) {
            case R.id.incomeRadioBtn:
                if (checked)
                    ie_type = "income";
                    mDespEditText.setText(ie_type + " : ");
                    break;
            case R.id.expenseRadioBtn:
                if (checked)
                    ie_type = "expenses";
                    mDespEditText.setText(ie_type + " : ");
                    break;
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        TextView textView = (TextView) findViewById(R.id.dateadd);
        textView.setText(currentDateString);

    }
}
