package com.emxcel.validator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText edtName, edtNumber;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtName = (EditText) findViewById(R.id.edtName);
        edtNumber = (EditText) findViewById(R.id.edtNumber);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

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
                fvu.set_rules(edtName, "Name", "required|emailCheck", new String[]{"your field 1234156 is required"}, "seterror");
                fvu.set_rules(edtNumber, "edtNumber", "mobileNumber", new String[]{"your field  sdfs  is required"}, "seterror");
                if(fvu.run()){
                    Log.e("", "*****\n*\n* Validation Done \n * \n ******");
                }else {
                    Log.e("", "*****\n*\n* Validation Error \n * \n ******");
                }
            }
        });
    }
}
