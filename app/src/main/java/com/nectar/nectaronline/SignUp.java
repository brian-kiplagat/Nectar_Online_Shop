package com.nectar.nectaronline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUp extends AppCompatActivity {

    private TextInputLayout email;
    private TextInputLayout password;
    private TextInputLayout name;
    private TextInputLayout number;
    private TextInputLayout address;
    private AutoCompleteTextView dropDownText;
    private TextInputLayout description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        number = findViewById(R.id.phone);
        dropDownText = findViewById(R.id.dropdown_text);
        address = findViewById(R.id.address);
        description = findViewById(R.id.desc);
        TextInputEditText name_edit = findViewById(R.id.name_edit);
        TextInputEditText email_edit = findViewById(R.id.email_edit);
        TextInputEditText password__edit = findViewById(R.id.password__edit);
        TextInputEditText number_edit = findViewById(R.id.number_edit);
        TextInputEditText desc_edit = findViewById(R.id.desc_edit);

        name_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String x = name.getEditText().getText().toString().trim();
                if (x.isEmpty()) {
                } else {

                    if (x.length() < 2) {
                        name.setError("name too short");

                    } else {
                        name.setError(null);
                    }
                }
            }
        });
        email_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String x = email.getEditText().getText().toString().trim();
                if (x.isEmpty()) {
                    email.setError("field cannot be empty");
                } else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(x).matches()) {
                        email.setError("please enter a valid email address");

                    } else {
                        email.setError(null);
                    }

                }
            }
        });
        password__edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String Upass = password.getEditText().getText().toString().trim();
                if (Upass.isEmpty()) {
                    password.setError("field cannot be empty");

                } else {
                    password.setError(null);


                }
            }
        });
        number_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String Number = number.getEditText().getText().toString().trim();
                if (Number.isEmpty()) {
                    number.setError("field cannot be empty");

                } else {
                    if (!Number.startsWith("+254")) {
                        number.setError("please use +254 format");
                        Snackbar.make(number, "Phone number should start with the country code(+254) then your number", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    } else {
                        if (Number.length() > 13) {//error when string is too long
                            number.setError("number too long");
                        } else if (Number.length() < 13) {//error when string is too short
                            number.setError("number too short");

                        } else {
                            number.setError(null);//return true when all scode is satisfied

                        }
                    }
                }
            }
        });
        desc_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = description.getEditText().getText().toString().trim();
                if (string.isEmpty()) {
                    description.setError("field cannot be empty");

                } else {
                    if (string.length() < 5) {
                        description.setError("your description is too short");

                    } else {
                        description.setError(null);

                    }
                }
            }
        });

        String[] items = new String[]{

                "Baringo", "Bomet", "Bungoma",
                "Busia", "Elgeyo Marakwet", "Embu",
                "Garissa", "Homa Bay", "Isiolo",
                "Kajiado", "Kakamega", "Kericho",
                "Kiambu", "Kilifi", "Kirinyaga",
                "Kisii", "Kisumu", "Kitui",
                "Kwale", "Laikipia", "Lamu",
                "Machakos", "Makueni", "Mandera",
                "Meru", "Migori", "Marsabit",
                "Mombasa", "Muranga", "Nairobi",
                "Nakuru", "Nandi", "Narok",
                "Nyamira", "Nyandarua", "Nyeri",
                "Samburu", "Siaya", "Taita Taveta",
                "Tana River", "Tharaka Nithi", "Trans Nzoia",
                "Turkana", "Uasin Gishu", "Vihiga",
                "Wajir", "West Pokot"};


        ArrayAdapter<String> adapter = new ArrayAdapter<>(SignUp.this, R.layout.dropdown_item, items);
        dropDownText.setAdapter(adapter);


    }

    private Boolean validateName() {
        String x = name.getEditText().getText().toString().trim();
        if (x.isEmpty()) {
            name.setError("field cannot be empty");
            return false;
        } else {

            if (x.length() < 2) {
                name.setError("name too short");
                return false;
            } else {
                name.setError(null);
                return true;
            }
        }

    }

    private Boolean validateEmail() {
        String x = email.getEditText().getText().toString().trim();
        if (x.isEmpty()) {
            email.setError("field cannot be empty");
            return false;
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(x).matches()) {
                email.setError("please enter a valid email address");
                return false;
            } else {
                email.setError(null);
                return true;
            }

        }
    }

    private Boolean validateAddress() {
        String add = address.getEditText().getText().toString().trim();
        if (add.isEmpty()) {
            address.setError("field cannot be empty");
            return false;
        } else {
            address.setError(null);
            return true;

        }
    }

    private Boolean validatePassword() {
        String Upass = password.getEditText().getText().toString().trim();
        if (Upass.isEmpty()) {
            password.setError("field cannot be empty");
            return false;
        } else {
            password.setError(null);
            return true;

        }
    }

    private Boolean validateDesc() {
        String string = description.getEditText().getText().toString().trim();
        if (string.isEmpty()) {
            description.setError("field cannot be empty");
            return false;
        } else {
            if (string.length() < 5) {
                description.setError("your description is too short");
                return false;

            } else {
                description.setError(null);
                return true;

            }
        }
    }

    private Boolean validateNumber() {
        String Number = number.getEditText().getText().toString().trim();
        if (Number.isEmpty()) {
            number.setError("field cannot be empty");
            return false;

        } else {
            if (!Number.startsWith("+254")) {
                number.setError("please use +254 format");
                Snackbar.make(number, "Phone number should start with the country code(+254) then your number", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;

            } else {
                if (Number.length() > 13) {//error when string is too long
                    number.setError("number too long");
                    return false;

                } else if (Number.length() < 13) {//error when string is too short
                    number.setError("number too short");
                    return false;

                } else {
                    number.setError(null);//return true when all scode is satisfied
                    return true;
                }
            }
        }
    }


    public void signUp(View view) {
        if (!validateName() | !validateEmail() | !validatePassword() | !validateNumber() | !validateAddress() | !validateDesc()) {
            return;
        }
        final String NAME = name.getEditText().getText().toString().trim();
        final String EMAIL = email.getEditText().getText().toString().trim();
        final String PASSWORD = password.getEditText().getText().toString().trim();
        final String NUMBER = number.getEditText().getText().toString().trim().substring(1);
        final String ADDRESS = address.getEditText().getText().toString().trim();
        final String DESCRIPTION = description.getEditText().getText().toString().trim();

        final AlertDialog dialogBuilder = new AlertDialog.Builder(SignUp.this).create();
        LayoutInflater layoutInflater = SignUp.this.getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.dialog_shimmer_with_logo, null);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        dialogBuilder.show();
        //Use a for loop to iterate via failed trials
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 3; i++) {
                    try {
                        if (i == 1) {
                            toast("Could not connect, trying again");
                        }
                        if (i == 2) {
                            toast("This taking too long check your internet connection");
                        }
                        if (i == 3) {
                            toast("Check your internet connection, then try again");
                            dialogBuilder.dismiss();
                            break;
                        }
                        String url = getString(R.string.website_adress) + "/nectar/buy/signup.php";
                        RequestBody formBody = new FormBody.Builder()
                                .add("name", NAME)
                                .add("email", EMAIL)
                                .add("password", PASSWORD)
                                .add("number", NUMBER)
                                .add("address", ADDRESS)
                                .add("description", DESCRIPTION)
                                .build();

                        OkHttpClient client = new OkHttpClient();
                        final Request request = new Request.Builder()
                                .url(url)
                                .post(formBody)
                                .build();

                        Response response = client.newCall(request).execute();
                        final String res = response.body().string().trim();
                        Log.i("response", res);
                        JSONObject obj = new JSONObject(res);
                        String code = obj.getString("RESPONSE_CODE");


                        if (code.contentEquals("SUCCESS")) {
                            SharedPreferences preferences = getSharedPreferences("nectar", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("email", EMAIL);
                            editor.putString("password", PASSWORD);
                            editor.putString("number", NUMBER);
                            editor.putString("address", ADDRESS);
                            editor.putString("desc", DESCRIPTION);
                            editor.putString("name", NAME);
                            dismissDialoge(dialogBuilder);
                            toast("Hello " + NAME);

                            Log.i("SUCCESS", "run: ");
                            //String itemsArray = obj.getString("RESPONSE_SHOP");
                            // editor.putString("items", itemsArray);
                            editor.apply();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                            break;


                        } else {

                            String desc = obj.getString("RESPONSE_DESC");
                            Log.i("ERROR", desc);
                            toast(desc);
                            dismissDialoge(dialogBuilder);
                            break;

                        }


                    } catch (Exception e) {
                        Log.i("ERROR", e.getLocalizedMessage());
                    }

                }
            }
        });
        thread.start();

    }

    private void dismissDialoge(final AlertDialog dialogBuilder) {
        SignUp.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogBuilder.dismiss();
            }
        });
    }

    private void toast(final String s) {
        SignUp.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        });
    }
}