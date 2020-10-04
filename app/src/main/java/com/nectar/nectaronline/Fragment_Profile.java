package com.nectar.nectaronline;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Profile extends Fragment implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST = 1;
    TextInputLayout name;
    TextInputLayout phone;
    TextInputLayout address;
    TextInputLayout description;
    TextView name_person;
    TextView email_person;
    Context context;
    String EMAIL;
    com.mikhaellopez.circularimageview.CircularImageView circularImageView;
    Button edit;
    Button finish;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.badge. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Profile newInstance(String param1, String param2) {
        Fragment_Profile fragment = new Fragment_Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment__profile, container, false);
        context = getActivity().getApplicationContext();
        name = v.findViewById(R.id.name);
        phone = v.findViewById(R.id.phone);
        address = v.findViewById(R.id.address);
        description = v.findViewById(R.id.desc);
        name_person = v.findViewById(R.id.name_person);
        email_person = v.findViewById(R.id.email_person);
        circularImageView = v.findViewById(R.id.circularImageView);
        edit = v.findViewById(R.id.edit);
        finish = v.findViewById(R.id.finish);
        circularImageView.setOnClickListener(this);
        edit.setOnClickListener(this);
        finish.setOnClickListener(this);

        SharedPreferences preferences = context.getSharedPreferences("nectar", Context.MODE_PRIVATE);
        String NAME = preferences.getString("name", "");
        EMAIL = preferences.getString("email", "");
        String PHONE = preferences.getString("number", "");
        String ADDRESS = preferences.getString("address", "");
        String DESCRIPTION = preferences.getString("desc", "");

        name.getEditText().setText(NAME);
        phone.getEditText().setText("+"+PHONE);
        address.getEditText().setText(ADDRESS);
        description.getEditText().setText(DESCRIPTION);
        name_person.setText(NAME);
        email_person.setText(EMAIL);

        name.setEnabled(false);
        phone.setEnabled(false);
        address.setEnabled(false);
        description.setEnabled(false);

        if (preferences.contains("picture")) {
            String picString = preferences.getString("picture", "");
            byte[] decodedByte = Base64.decode(picString, 0);
            Bitmap bitmap = BitmapFactory
                    .decodeByteArray(decodedByte, 0, decodedByte.length);
            circularImageView.setImageBitmap(bitmap);
            Glide.with(context).load(bitmap).into(circularImageView);
        } else {
            circularImageView.setImageResource(R.drawable.ic_outline_account_circle_24);
            //Glide.with(context).load(R.drawable.ic_outline_account_circle_24).into(circularImageView);
            Snackbar.make(circularImageView, "Add a profile picture by clicking the picture", Snackbar.LENGTH_LONG).show();
        }
        return v;


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
        String Number = phone.getEditText().getText().toString().trim();
        if (Number.isEmpty()) {
            phone.setError("field cannot be empty");
            return false;

        } else {
            if (!Number.startsWith("+254")) {
                phone.setError("please use +254 format");
                Snackbar.make(phone, "Phone number should start with the country code(+254) then your number", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return false;

            } else {
                if (Number.length() > 13) {//error when string is too long
                    phone.setError("number too long");
                    return false;

                } else if (Number.length() < 13) {//error when string is too short
                    phone.setError("number too short");
                    return false;

                } else {
                    phone.setError(null);//return true when all scode is satisfied
                    return true;
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit:
                name.setEnabled(true);
                phone.setEnabled(true);
                address.setEnabled(true);
                description.setEnabled(true);
                Log.i("EDIT", "CLICKED");
                break;
            case R.id.circularImageView:
                Log.i("CIRCULAR_IMAGE", "CLICKED");
                getImage();
                break;
            case R.id.finish:
                Log.i("FINISH", "CLICKED");

                if (!validateName() | !validateNumber() | !validateAddress() | !validateDesc()) {
                    return;
                }
                final String NAME = name.getEditText().getText().toString().trim();
                final String NUMBER = phone.getEditText().getText().toString().trim().substring(1);
                final String ADDRESS = address.getEditText().getText().toString().trim();
                final String DESCRIPTION = description.getEditText().getText().toString().trim();
                final AlertDialog dialogBuilder = new AlertDialog.Builder(getActivity()).create();
                LayoutInflater layoutInflater = getActivity().getLayoutInflater();
                View dialogView = layoutInflater.inflate(R.layout.dialog_login, null);
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
                                String url = getString(R.string.website_adress) + "/nectar/editcontactdetails.php";
                                RequestBody formBody = new FormBody.Builder()
                                        .add("name", NAME)
                                        .add("email", EMAIL)
                                        .add("phone", NUMBER)
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
                                String desc = obj.getString("RESPONSE_DESC");
                                //
                                if (code.contentEquals("SUCCESS")) {
                                    JSONArray array = obj.getJSONArray("DETAILS");
                                    JSONObject object = array.getJSONObject(0);
                                    String NAME_NEW = object.getString("name");
                                    String EMAIL_NEW = object.getString("email");
                                    String PASSWORD_NEW = object.getString("password");
                                    String NUMBER_NEW = object.getString("number");
                                    String ADDRESS_NEW = object.getString("address");
                                    String DESCRIPTION_NEW = object.getString("description");
                                    Log.i("name", NAME_NEW);
                                    Log.i("email", EMAIL_NEW);
                                    Log.i("password", PASSWORD_NEW);
                                    Log.i("number", NUMBER_NEW);
                                    Log.i("address", ADDRESS_NEW);
                                    Log.i("desc", DESCRIPTION_NEW);
                                    SharedPreferences preferences = context.getSharedPreferences("nectar", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("email", EMAIL_NEW);
                                    editor.putString("password", PASSWORD_NEW);
                                    editor.putString("number", NUMBER_NEW);
                                    editor.putString("address", ADDRESS_NEW);
                                    editor.putString("desc", DESCRIPTION_NEW);
                                    editor.putString("name", NAME_NEW);
                                    updateDetail(NAME_NEW, EMAIL_NEW, PASSWORD_NEW, NUMBER_NEW, ADDRESS_NEW, DESCRIPTION_NEW);
                                    editor.apply();
                                    dismissDialoge(dialogBuilder);
                                    toast("Edited Succesfully");
                                } else {
                                    toast(desc);
                                }
                                dismissDialoge(dialogBuilder);
                                break;

                            } catch (Exception e) {
                                Log.i("ERROR", e.getLocalizedMessage());
                            }

                        }
                    }
                });
                thread.start();

                break;


        }
    }

    private void updateDetail(final String name_new, final String email_new, String password_new, final String number_new, final String address_new, final String description_new) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                name.getEditText().setText(name_new);
                phone.getEditText().setText("+"+number_new);
                address.getEditText().setText(address_new);
                description.getEditText().setText(description_new);
                name_person.setText(name_new);
                email_person.setText(email_new);

            }
        });


    }

    private void toast(String s) {
        Snackbar.make(email_person, s, Snackbar.LENGTH_LONG).show();
    }

    private void getImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            Uri filePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                context.getContentResolver(),
                                filePath);
                circularImageView.setImageBitmap(bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] b = baos.toByteArray();
                String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
                SharedPreferences sharedPreferences = context.getSharedPreferences("nectar", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("picture", imageEncoded);
                editor.apply();
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void dismissDialoge(final AlertDialog dialogBuilder) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogBuilder.dismiss();
            }
        });
    }
}