package com.emxcel.validator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by emxcel on 3/10/17.
 * <p>
 * DEMO FOR CALL
 * FormValidationUtils fvu = new FormValidationUtils(MainActivity.this);
 * fvu.set_rules(edtName, "Name", "required|max_length[12]", new String[]{"your field is required"}, "toast");
 * fvu.set_rules(edtName, "Name", "required|emailCheck", new String[]{"your field is required"}, "toast");
 * fvu.set_rules(edtName, "Name", "required|emailCheck", new String[]{"your field 1234156 is required"}, "seterror");
 * fvu.set_rules(edtNumber, "edtNumber", "mobileNumber", new String[]{"your field  sdfs  is required"}, "seterror");
 * if(fvu.run()){
 * Log.e("", "*****\n*\n* Validation Done \n * \n ******");
 * }else {
 * Log.e("", "*****\n*\n* Validation Error \n * \n ******");
 * }
 */

public class FormValidationUtils {

    /**
     * Whether the log has been display or not
     *
     * @var boolean
     */
    private static boolean log_enable = true;
    /**
     * Validation data for the current form submission
     *
     * @var array
     */
    protected HashMap<String, HashMap<String, Object>> arrFieldData;

    /**
     * Array of validation errors
     *
     * @var array
     */
    protected ArrayList<HashMap<String, Object>> arrErrorList;

    /**
     * Reference to the CodeIgniter instance
     *
     * @var object
     */
    private Context _c;


    public FormValidationUtils() {
    }

    public FormValidationUtils(Context c) {
        this._c = c;
        this.arrFieldData = new HashMap<>();
        this.arrErrorList = new ArrayList<>();
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showSnackbar(View v, String msg) {
        /*View snackbarView = v;
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(5);*/
        Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show();
    }

    public static void showSnackbarWithAction(View rootView, String msg, final View.OnClickListener listener) {

        Snackbar.make(rootView, msg, Snackbar.LENGTH_INDEFINITE)
                .setAction("Reload", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClick(v);
                    }
                }).show();

    }


    public void DisplayLog(String tag, String msg) {
        if (this.log_enable) {
            Log.e(tag, msg);
//            System.out.println(tag+"----"+ msg);
        }
    }

    /**
     * Function to display simple Alert Dialog
     *
     * @param edtField          - object of EditText
     * @param slabe             - EditText labe
     * @param sRule             - validation rule like {required,emailCheck,validName,validText,validPersonName,validSignNumber,min_length[10],max_length[10],between_length[5,10],mobileNumber}
     * @param arrError          - array of error message
     * @param sErrorDisplayType - error message Display Type {toast,seterror,alert}
     *                          don't want icon
     */

    public void set_rules(EditText edtField, String slabe, String sRule, String[] arrError, String sErrorDisplayType) {

        if (edtField == null || edtField.equals("")) {
            return;
        }

        if (sRule == null || sRule.equals("")) {
            return;
        }

        if (slabe == null || slabe.equals("")) {
            slabe = edtField.getHint().toString();
        }

        String sField = String.valueOf(edtField.getId());
        String[] arrRule = sRule.split("\\|(?![^\\[]*\\])");


        HashMap<String, Object> hmKeys = new HashMap<>();
        hmKeys.put("field", sField);
        hmKeys.put("edtfield", edtField);
        hmKeys.put("fieldValue", edtField.getText().toString());
        hmKeys.put("label", slabe);
        hmKeys.put("rules", arrRule);
        hmKeys.put("errors", arrError);
        hmKeys.put("sErrorDisplayType", sErrorDisplayType);
        hmKeys.put("is_array", false);
        hmKeys.put("error", "");

        this.arrFieldData.put(sField, hmKeys);


    }

    public boolean run() {


        DisplayLog("run :this.arrFieldData.size() ", "" + this.arrFieldData.size());
        if (this.arrFieldData != null && this.arrFieldData.size() > 0) {

            Iterator it = this.arrFieldData.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                DisplayLog("run key,value ", pair.getKey() + " = " + pair.getValue());

                HashMap<String, Object> hmKeys = (HashMap<String, Object>) pair.getValue();
                String[] rules = (String[]) hmKeys.get("rules");

                DisplayLog("run rules.length ", rules.length + "");
                String methodName = "";
                if (rules != null && rules.length > 0) {
                    for (int i = 0; i < rules.length; i++) {
                        DisplayLog("run rules[i] ", "" + rules[i]);

                        EditText edttyp = (EditText) hmKeys.get("edtfield");

                        methodName = rules[i];

                        if (methodName.trim().equals("required")) {
                            required(edttyp, i);
                        }
                        if (methodName.trim().equals("emailCheck")) {
                            emailCheck(edttyp, i);
                        }
                        if (methodName.trim().equals("validName")) {
                            validName(edttyp, i);
                        }
                        if (methodName.trim().equals("validText")) {
                            validText(edttyp, i);
                        }
                        if (methodName.trim().equals("validPersonName")) {
                            validPersonName(edttyp, i);
                        }
                        if (methodName.trim().equals("validSignNumber")) {
                            validSignNumber(edttyp, i);
                        }
                        if (methodName.trim().equals("mobileNumber")) {
                            mobileNumber(edttyp, i);
                        }


                        String[] numb = methodName.replace("]", "").split("\\[");
                        methodName = numb[0];
                        if (methodName.trim().equals("min_length") || methodName.trim().equals("max_length") || methodName.trim().equals("between_length")) {
                            if (methodName.trim().equals("min_length")) {
                                numberInBetween(edttyp, i, Integer.parseInt(numb[1]), 0);
                            }
                            if (methodName.trim().equals("max_length")) {
                                numberInBetween(edttyp, i, 0, Integer.parseInt(numb[1]));
                            }
                            if (methodName.trim().equals("between_length")) {
                                String[] betnum = numb[1].split("\\,");
                                numberInBetween(edttyp, i, Integer.parseInt(betnum[0]), Integer.parseInt(betnum[1]));
                            }
                        }
                        /*try {

                            //no paramater
                            Class noparams[] = {};

                            //String parameter
                            Class[] paramString = new Class[1];
                            paramString[0] = String.class;

                            //int parameter
                            Class[] paramInt = new Class[1];
                            paramInt[0] = Integer.TYPE;

                            //int parameter
                            Class[] paramEdt = new Class[1];
                            paramInt[0] = EditText.class;

//                            Class cls = this.getClass();
                            Class cls = Class.forName("com.emxcel.validator.FormValidationUtils");
                            Object obj = cls.newInstance();


                            DisplayLog("run key,value ", edttyp.getHint() + "");
                            Method method = cls.getDeclaredMethod(rules[i], new Class[]{EditText.class, Integer.TYPE});
                            if ((boolean) method.invoke(obj, new Object[]{edttyp, i})) {
                                break;
                            }
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }*/
                    }
                }

                it.remove(); // avoids a ConcurrentModificationException
            }

        }

        if (!setDisplayError()) {
            Log.e("", "*****\n* setDisplayError true \n *******");
            return false;
        }

        return true;
    }

    private boolean required(EditText edtField, int iErrorIndex) {

        String sField, sFieldValue, sErrorMessage;
        sField = String.valueOf(edtField.getId());

        DisplayLog("run required ", "sField :: " + sField);
        HashMap<String, Object> hmKeys = this.arrFieldData.get(sField);

        sFieldValue = (String) hmKeys.get("fieldValue");
        String[] serrors = (String[]) hmKeys.get("errors");

        if (sFieldValue == null || sFieldValue.equals("")) {
            sErrorMessage = (serrors[iErrorIndex] != null && !serrors[iErrorIndex].equals("")) ? serrors[iErrorIndex] : (hmKeys.get("label") != null && !hmKeys.get("label").toString().equals("")) ? "Your " + (String) hmKeys.get("label") + " is Empty" : "Your value is Empty";

            HashMap<String, Object> hmErrorKeys = detDefaultError(edtField);
            hmErrorKeys.put("label", (String) hmKeys.get("label"));
            hmErrorKeys.put("errorsStatus", true);
            hmErrorKeys.put("error", sErrorMessage);
            this.arrErrorList.add(hmErrorKeys);
        }

        return false;
    }

    private boolean emailCheck(EditText edtField, int iErrorIndex) {

        String sField, sFieldValue, sErrorMessage;
        sField = String.valueOf(edtField.getId());

        DisplayLog("run required ", "sField :: " + sField);
        HashMap<String, Object> hmKeys = this.arrFieldData.get(sField);

        sFieldValue = (String) hmKeys.get("fieldValue");
        String[] serrors = (String[]) hmKeys.get("errors");

//        if (sFieldValue == null || sFieldValue.equals("")) {
        if (!sFieldValue.trim().equalsIgnoreCase("") && !android.util.Patterns.EMAIL_ADDRESS.matcher(
                edtField.getText().toString()).matches()) {
            sErrorMessage = (serrors.length > iErrorIndex && serrors[iErrorIndex] != null && serrors[iErrorIndex].equals("")) ? "Your " + (String) hmKeys.get("label") + " is Invalid" : "Invalid Email Address";

            HashMap<String, Object> hmErrorKeys = detDefaultError(edtField);
            hmErrorKeys.put("label", (String) hmKeys.get("label"));
            hmErrorKeys.put("errorsStatus", true);
            hmErrorKeys.put("error", sErrorMessage);
            this.arrErrorList.add(hmErrorKeys);
        }

        return false;
    }

    // validation for proper title between a-z and 0-9
    public boolean validName(EditText edtField, int iErrorIndex) {

        String sField, sFieldValue, sErrorMessage;
        sField = String.valueOf(edtField.getId());

        DisplayLog("run required ", "sField :: " + sField);
        HashMap<String, Object> hmKeys = this.arrFieldData.get(sField);

        sFieldValue = (String) hmKeys.get("fieldValue");
        String[] serrors = (String[]) hmKeys.get("errors");

        if (required(edtField, iErrorIndex)) {
            return true;
        } else if (!sFieldValue.matches("^[a-zA-Z0-9 ]*$")) {
            sErrorMessage = (serrors.length > iErrorIndex && serrors[iErrorIndex] != null && serrors[iErrorIndex].equals("")) ? "Your " + (String) hmKeys.get("label") + " is Invalid" : "Accept Alphabets And Numbers Only.";

            HashMap<String, Object> hmErrorKeys = detDefaultError(edtField);
            hmErrorKeys.put("label", (String) hmKeys.get("label"));
            hmErrorKeys.put("errorsStatus", true);
            hmErrorKeys.put("error", "Accept Alphabets And Numbers Only.");
            this.arrErrorList.add(hmErrorKeys);

            return false;

        }

        return true;

    }

    // validation for proper title between a-z , 0-9, ., -,:,,
    public boolean validText(EditText edtField, int iErrorIndex) {

        String sField, sFieldValue, sErrorMessage;
        sField = String.valueOf(edtField.getId());

        DisplayLog("run required ", "sField :: " + sField);
        HashMap<String, Object> hmKeys = this.arrFieldData.get(sField);

        sFieldValue = (String) hmKeys.get("fieldValue");
        String[] serrors = (String[]) hmKeys.get("errors");

        if (required(edtField, iErrorIndex)) {
            return true;
        } else if (!sFieldValue.matches("^[A-Za-z0-9\\\\s,\\\\.\\\\-\\\\:]*$")) {
            sErrorMessage = (serrors.length > iErrorIndex && serrors[iErrorIndex] != null && serrors[iErrorIndex].equals("")) ? "Your " + (String) hmKeys.get("label") + " is Invalid" : "Accept Alphabets And Numbers Only.";

            HashMap<String, Object> hmErrorKeys = detDefaultError(edtField);
            hmErrorKeys.put("label", (String) hmKeys.get("label"));
            hmErrorKeys.put("errorsStatus", true);
            hmErrorKeys.put("error", "Some special character not allowed.");
            this.arrErrorList.add(hmErrorKeys);

            return false;

        }

        return true;

    }

    // validation for proper name between a-z
    public boolean validPersonName(EditText edtField, int iErrorIndex) {

        String sField, sFieldValue, sErrorMessage;
        sField = String.valueOf(edtField.getId());

        DisplayLog("run required ", "sField :: " + sField);
        HashMap<String, Object> hmKeys = this.arrFieldData.get(sField);

        sFieldValue = (String) hmKeys.get("fieldValue");
        String[] serrors = (String[]) hmKeys.get("errors");

        if (required(edtField, iErrorIndex)) {
            return true;
        } else if (!sFieldValue.matches("[a-zA-Z ]+")) {
            sErrorMessage = (serrors.length > iErrorIndex && serrors[iErrorIndex] != null && serrors[iErrorIndex].equals("")) ? "Your " + (String) hmKeys.get("label") + " is Invalid" : "Accept Alphabets And Numbers Only.";

            HashMap<String, Object> hmErrorKeys = detDefaultError(edtField);
            hmErrorKeys.put("label", (String) hmKeys.get("label"));
            hmErrorKeys.put("errorsStatus", true);
            hmErrorKeys.put("error", "Accept Alphabets Only.");
            this.arrErrorList.add(hmErrorKeys);

            return false;

        }

        return true;

    }

    // validation for proper number between min-value and max-value
    public boolean validSignNumber(EditText edtField, int iErrorIndex) {

        String sField, sFieldValue, sErrorMessage;
        sField = String.valueOf(edtField.getId());

        DisplayLog("run validSignNumber ", "sField :: " + sField);
        HashMap<String, Object> hmKeys = this.arrFieldData.get(sField);

        sFieldValue = (String) hmKeys.get("fieldValue");
        String[] serrors = (String[]) hmKeys.get("errors");

        if (required(edtField, iErrorIndex)) {
            return true;
        } else if (!sFieldValue.matches("^[0-9]*$")) {
            sErrorMessage = (serrors.length > iErrorIndex && serrors[iErrorIndex] != null && serrors[iErrorIndex].equals("")) ? "Your " + (String) hmKeys.get("label") + " is Invalid" : "Accept Alphabets And Numbers Only.";

            HashMap<String, Object> hmErrorKeys = detDefaultError(edtField);
            hmErrorKeys.put("label", (String) hmKeys.get("label"));
            hmErrorKeys.put("errorsStatus", true);
            hmErrorKeys.put("error", "Accept Number Only.");
            this.arrErrorList.add(hmErrorKeys);

            return false;

        }

        return true;

    }

    // validation for proper number between min-value and max-value
    public boolean numberInBetween(EditText edtField, int iErrorIndex, int MinLen, int MaxLen) {

        String sField, sFieldValue, sErrorMessage;
        sField = String.valueOf(edtField.getId());

        DisplayLog("run validSignNumber ", "sField :: " + sField);
        HashMap<String, Object> hmKeys = this.arrFieldData.get(sField);

        sFieldValue = (String) hmKeys.get("fieldValue");
        String[] serrors = (String[]) hmKeys.get("errors");

        if (required(edtField, iErrorIndex)) {
            return true;
        } else if (!sFieldValue.matches("^[0-9]*$")) {

            HashMap<String, Object> hmErrorKeys = detDefaultError(edtField);
            hmErrorKeys.put("label", (String) hmKeys.get("label"));
            hmErrorKeys.put("errorsStatus", true);
            hmErrorKeys.put("error", "Accept Number Only.");
            this.arrErrorList.add(hmErrorKeys);

            return false;

        } else if (sFieldValue.toString().length() < MinLen) {

            HashMap<String, Object> hmErrorKeys = detDefaultError(edtField);
            hmErrorKeys.put("label", (String) hmKeys.get("label"));
            hmErrorKeys.put("errorsStatus", true);
            hmErrorKeys.put("error", "Minimum length " + MinLen);
            this.arrErrorList.add(hmErrorKeys);

            return false;

        } else if (MaxLen > 0 && sFieldValue.toString().length() > MaxLen) {

            HashMap<String, Object> hmErrorKeys = detDefaultError(edtField);
            hmErrorKeys.put("label", (String) hmKeys.get("label"));
            hmErrorKeys.put("errorsStatus", true);
            hmErrorKeys.put("error", "Maximum length " + MaxLen);
            this.arrErrorList.add(hmErrorKeys);

            return false;

        }

        return true;

    }

    // validation for proper number between min-value and max-value
    public boolean mobileNumber(EditText edtField, int iErrorIndex) {

        String sField, sFieldValue, sErrorMessage;
        sField = String.valueOf(edtField.getId());

        DisplayLog("run validSignNumber ", "sField :: " + sField);
        HashMap<String, Object> hmKeys = this.arrFieldData.get(sField);

        sFieldValue = (String) hmKeys.get("fieldValue");
        String[] serrors = (String[]) hmKeys.get("errors");

        if (required(edtField, iErrorIndex)) {
            return true;
        } else if (!sFieldValue.matches("^[0-9]*$")) {
            sErrorMessage = (serrors.length > iErrorIndex && serrors[iErrorIndex] != null && serrors[iErrorIndex].equals("")) ? "Your " + (String) hmKeys.get("label") + " is Invalid" : "Accept Alphabets And Numbers Only.";

            HashMap<String, Object> hmErrorKeys = detDefaultError(edtField);
            hmErrorKeys.put("label", (String) hmKeys.get("label"));
            hmErrorKeys.put("errorsStatus", true);
            hmErrorKeys.put("error", "Accept Number Only.");
            this.arrErrorList.add(hmErrorKeys);

            return false;

        } else if (!sFieldValue.matches("\\d{10}")) {
            sErrorMessage = (serrors.length > iErrorIndex && serrors[iErrorIndex] != null && serrors[iErrorIndex].equals("")) ? "Your " + (String) hmKeys.get("label") + " is Invalid" : "Accept Alphabets And Numbers Only.";

            HashMap<String, Object> hmErrorKeys = detDefaultError(edtField);
            hmErrorKeys.put("label", (String) hmKeys.get("label"));
            hmErrorKeys.put("errorsStatus", true);
            hmErrorKeys.put("error", "Please enter proper number.");
            this.arrErrorList.add(hmErrorKeys);

            return false;

        }

        return true;

    }


    private HashMap<String, Object> detDefaultError(EditText edtField) {

        String sField = String.valueOf(edtField.getId());

        DisplayLog("run detDefaultError ", "sField :: " + sField);
        HashMap<String, Object> hmKeys = this.arrFieldData.get(sField);

        DisplayLog("run detDefaultError ", "sErrorDisplayType :: " + (String) hmKeys.get("sErrorDisplayType"));

        HashMap<String, Object> hmErrorKeys = new HashMap<>();
        hmErrorKeys.put("field", (EditText) edtField);
        hmErrorKeys.put("fieldId", sField);
        hmErrorKeys.put("fieldValue", edtField.getText().toString());
        hmErrorKeys.put("label", (String) hmKeys.get("label"));
        hmErrorKeys.put("errorsStatus", false);
        hmErrorKeys.put("errorDisplayType", (String) hmKeys.get("sErrorDisplayType"));
        hmErrorKeys.put("error", "");

//        this.arrErrorList.add(hmErrorKeys);

        return hmErrorKeys;
    }

    private boolean setDisplayError() {

        String sErrorDisplayType = null, sErrorMessage = "", sLastField = "";
        EditText field;
        boolean bReturnStatus = true;
        if (this.arrErrorList != null && this.arrErrorList.size() > 0) {
            for (int i = 0; i < this.arrErrorList.size(); i++) {
                DisplayLog("bReturnStatus ", "*****\n* bReturnStatus :: " + bReturnStatus+" * \n ******");
                if (bReturnStatus) {
                    bReturnStatus = false;
                }
                HashMap<String, Object> hmKeys = this.arrErrorList.get(i);
                sErrorDisplayType = (String) hmKeys.get("errorDisplayType");
//                sErrorMessage = (String) hmKeys.get("error");
                field = (EditText) hmKeys.get("field");
                DisplayLog("run setDisplayError ", "sErrorDisplayType :: " + sErrorDisplayType);
                if (sErrorDisplayType != null) {
                    if (sErrorDisplayType.equals("toast")) {
                        showToast(_c, hmKeys.get("error").toString());
                    } else if (sErrorDisplayType.equals("seterror")) {
//                        showToast(_c, sErrorMessage);
                        sErrorMessage = ((sLastField.equals(hmKeys.get("error").toString())) ? "\n" : "") + hmKeys.get("error").toString();
                        field.setError(sErrorMessage);
                    } else if (sErrorDisplayType.equals("alert")) {
//                        showToast(_c, sErrorMessage);
                        sErrorMessage += hmKeys.get("error").toString() + ",\n";
//                        sErrorMessage += sErrorMessage + "</br>";
//                        field.setError(sErrorMessage);
                    }
                    sLastField = hmKeys.get("error").toString();
                }
            }
            if (sErrorDisplayType != null && sErrorDisplayType.equals("alert")) {
//                        showToast(_c, sErrorMessage);
                DisplayLog("", sErrorMessage);
                AlertDialogManager.showAlertDialog(_c, "Ok", "Validate", sErrorMessage, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
//                field.setError(sErrorMessage);
            }

        }
        return bReturnStatus;
    }

    public static class AlertDialogManager {
        /**
         * Function to display simple Alert Dialog
         *
         * @param context  - application context
         * @param title    - alert dialog title
         * @param message  - alert message
         * @param listener - success/failure (used to set icon) - pass null if you
         *                 don't want icon
         */
        public static void showAlertDialog(final Context context, final String positiveButtonTitle, final String title,
                                           final String message, final OnClickListener listener) {
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            // Setting Dialog Title
            alertDialog.setTitle(title);
            // Setting Dialog Message
            alertDialog.setMessage(Html.fromHtml("<font color='#FF0000'>" + message + "</font>"));
            // Setting alert dialog icon
//            alertDialog.setIcon(R.drawable.ic_fail);
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            // Setting OK Button
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, positiveButtonTitle,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            dialog.dismiss();
                            if (listener != null) {
                                listener.onClick(dialog, which);
                            }
                        }
                    });
            // Showing Alert Message
            alertDialog.show();
        }

        /**
         * Function to display simple Alert Dialog
         *
         * @param context  - application context
         * @param title    - alert dialog title
         * @param message  - alert message
         * @param listener - success/failure (used to set icon) - pass null if you
         *                 don't want icon
         */
        public static void showInfoDialog(final Context context, final String positiveButtonTitle, final String title,
                                          final String message, final OnClickListener listener) {
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            // Setting Dialog Title
            alertDialog.setTitle(title);
            // Setting Dialog Message
            alertDialog.setMessage(Html.fromHtml("<font color='#009933'>" + message + "</font>"));
            // Setting alert dialog icon
//            alertDialog.setIcon(R.drawable.ic_success);
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            // Setting OK Button
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, positiveButtonTitle,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            dialog.dismiss();
                            if (listener != null) {
                                listener.onClick(dialog, which);
                            }
                        }
                    });
            // Showing Alert Message
            alertDialog.show();
        }

        /**
         * Function to display simple Alert Dialog
         *
         * @param context  - application context
         * @param title    - alert dialog title
         * @param message  - alert message
         * @param listener - success/failure (used to set icon) - pass null if you
         *                 don't want icon
         */
        public static void showInfoDialog(final Context context, final String positiveButtonTitle,
                                          final String negativeButtonTitle, final String title, final String message, final OnClickListener listener) {
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            // Setting Dialog Title
            alertDialog.setTitle(title);
            // Setting Dialog Message
            alertDialog.setMessage(Html.fromHtml("<font color='#009933'>" + message + "</font>"));
            // Setting alert dialog icon
//            alertDialog.setIcon(R.drawable.ic_success);
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            // Setting BUTTON_POSITIVE
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, positiveButtonTitle,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            dialog.dismiss();
                            if (listener != null) {
                                listener.onClick(dialog, which);
                            }
                        }
                    });
            // Setting BUTTON_NEGATIVE
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, negativeButtonTitle,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            dialog.dismiss();
                            if (listener != null) {
                                listener.onClick(dialog, which);
                            }
                        }
                    });
            // Showing Alert Message
            alertDialog.show();
        }

        /**
         * Function to display simple Alert Dialog
         *
         * @param context  - application context
         * @param title    - alert dialog title
         * @param message  - alert message
         * @param listener - success/failure (used to set icon) - pass null if you
         *                 don't want icon
         */
        public static void showInfoDialog(final Context context, final String positiveButtonTitle,
                                          final String negativeButtonTitle, final String neutralButtonTitle, final String title, final String message,
                                          final OnClickListener listener) {
            final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            // Setting Dialog Title
            alertDialog.setTitle(title);
            // Setting Dialog Message
            alertDialog.setMessage(Html.fromHtml("<font color='#009933'>" + message + "</font>"));
            // Setting alert dialog icon
//            alertDialog.setIcon(R.drawable.ic_success);
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            // Setting BUTTON_POSITIVE
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, positiveButtonTitle,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            dialog.dismiss();
                            if (listener != null) {
                                listener.onClick(dialog, which);
                            }
                        }
                    });
            // Setting BUTTON_NEGATIVE
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, negativeButtonTitle,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            dialog.dismiss();
                            if (listener != null) {
                                listener.onClick(dialog, which);
                            }
                        }
                    });
            // Setting BUTTON_NEUTRAL
            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, neutralButtonTitle,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            dialog.dismiss();
                            if (listener != null) {
                                listener.onClick(dialog, which);
                            }
                        }
                    });
            // Showing Alert Message
            alertDialog.show();
        }
    }

}
