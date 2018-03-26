package com.sliit.budgetcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sliit.budgetcalculator.Utils.IEDBHelper;
import com.sliit.budgetcalculator.model.IncomeExpense;

public class UpdateRecordActivity extends AppCompatActivity {

    private EditText mAmountEditText;
    private EditText mDespEditText;
    private EditText mDateEditText;
    private RadioGroup mRbtnType;
    private Button mUpdateBtn;

    private IEDBHelper dbHelper;
    private long receivedIEId;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_record);

        //init
        mAmountEditText = (EditText)findViewById(R.id.IE_Amount_Update);
        mDespEditText = (EditText)findViewById(R.id.IE_Desp_Update);
        mDateEditText = (EditText)findViewById(R.id.IE_Date);

        mRbtnType = (RadioGroup)findViewById(R.id.radioTypeUpdate);
        int selectedId = mRbtnType.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
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
}
