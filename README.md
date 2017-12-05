# Validator

<pre>
 FormValidationUtils fvu = new FormValidationUtils(MainActivity.this);
 fvu.set_rules(edtName, "Name", "required|max_length[12]", new String[]{"your field is required"}, "toast");
 fvu.set_rules(edtName, "Name", "required|emailCheck", new String[]{"your field is required"}, "toast");
 fvu.set_rules(edtName, "Name", "required|emailCheck", new String[]{"your field 1234156 is required"}, "seterror");
 fvu.set_rules(edtNumber, "edtNumber", "mobileNumber", new String[]{"your field  sdfs  is required"}, "seterror");
 if(fvu.run()){
    Log.e("", "*****\n*\n* Validation Done \n * \n ******");
 }else {
    Log.e("", "*****\n*\n* Validation Error \n * \n ******");
 }
</pre>
