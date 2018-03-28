package com.sliit.budgetcalculator;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sliit.budgetcalculator.Utils.DatePickerFragment;
import com.sliit.budgetcalculator.Utils.IEDBHelper;
import com.sliit.budgetcalculator.model.IncomeExpense;

import java.text.DateFormat;
import java.util.Calendar;

public class UpdateRecordActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText mAmountEditText;
    private EditText mDespEditText;
    private TextView mDateEditText;
    private Button mUpdateBtn;

    private IEDBHelper dbHelper;
    private long receivedIEId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_record);

        //init
        mAmountEditText = (EditText)findViewById(R.id.IE_Amount_Update);
        mDespEditText = (EditText)findViewById(R.id.IE_Desp_Update);
        mDateEditText = (TextView) findViewById(R.id.IE_Date);

        mUpdateBtn = (Button) findViewById(R.id.updateUserButton);

        dbHelper = new IEDBHelper(this);

        try {
            //get intent to get person id
            receivedIEId = getIntent().getLongExtra("USER_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /***populate user data before update***/
        IncomeExpense queriedIncomeExpense = dbHelper.getIE(receivedIEId);
        //set field to this user data
        mDespEditText.setText(queriedIncomeExpense.getDescription());
        mDateEditText.setText(queriedIncomeExpense.getDate());
        mAmountEditText.setText(queriedIncomeExpense.getAmount().toString());



        //listen to add button click to update
        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call the save person method
                updatePerson();
            }
        });
        Button button = (Button) findViewById(R.id.buttonChangeDateUpdate);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DatePickerFragment datePicker = new DatePickerFragment();

                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });
    }
    private void updatePerson(){

        String amount = mAmountEditText.getText().toString().trim();
        String date = mDateEditText.getText().toString().trim();
        String description = mDespEditText.getText().toString().trim();



        if(amount.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter a name", Toast.LENGTH_SHORT).show();
        }

        if(date.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter an age", Toast.LENGTH_SHORT).show();
        }

        if(description.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter an occupation", Toast.LENGTH_SHORT).show();
        }
        //create updated person
        IncomeExpense updatedIncomeExpense = new IncomeExpense(description, date, Double.parseDouble(amount));

        //call dbhelper update
        dbHelper.updateIERecord(receivedIEId, this, updatedIncomeExpense);

        //finally redirect back home
        // NOTE you can implement an sqlite callback then redirect on success delete
        goBackHome();

    }

    private void goBackHome(){
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        TextView textView = (TextView) findViewById(R.id.IE_Date);
        textView.setText(currentDateString);

    }
}
