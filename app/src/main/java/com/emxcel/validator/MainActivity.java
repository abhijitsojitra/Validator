package com.emxcel.validator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText edtName, edtNumber;
    Spinner spinner;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtName = (EditText) findViewById(R.id.edtName);
        edtNumber = (EditText) findViewById(R.id.edtNumber);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        spinner = (Spinner) findViewById(R.id.spinner);

        List<String> list = new ArrayList<String>();
        list.add("");
        list.add("list 2");
        list.add("list 3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                String[] ops = "required|max_length[12]".split("\\|(?![^\\[]*\\])");
                String[] ops = "max_length[12]".split("(.*?)\\[(.*)\\]");
                Log.e("dsadsd", ops.length+"");
                for (int i = 0; i < ops.length; i++) {
                    Log.e("", ops[i]);
                }


                FormValidationUtils fvu = new FormValidationUtils(MainActivity.this);
//                fvu.set_rules(edtName, "Name", "required|max_length[12]", new String[]{"your field is required"}, "toast");
//                fvu.set_rules(edtName, "Name", "required|emailCheck", new String[]{"your field is required"}, "toast");
                fvu.set_rules(spinner, "spinner", "required", new String[]{"your field 1234156 is required"}, "seterror");
                fvu.set_rules(edtName, "Name", "required|emailCheck", new String[]{"your field 1234156 is required"}, "seterror");
                fvu.set_rules(edtNumber, "edtNumber", "securePassword", new String[]{"your field  sdfs  is required"}, "seterror");
                if(fvu.run()){
                    Log.e("", "*****\n*\n* Validation Done \n * \n ******");
                }else {
                    Log.e("", "*****\n*\n* Validation Error \n * \n ******");
                }
            }
        });
    }
}
