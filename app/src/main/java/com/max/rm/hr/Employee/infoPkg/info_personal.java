package com.max.rm.hr.Employee.infoPkg;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.max.rm.hr.Employee.Api;
import com.max.rm.hr.Employee.RequestInterface;
import com.max.rm.hr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class info_personal extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    TextView nameAr, nameEn, Email, phone, Gender, socialState, birthDate_H, birthDate_G, B_city, B_country, religion, note, nationality;
    ImageView image;
    ProgressBar progress,progress2;
    Api api;
    SharedPreferences shared;
    SwipeRefreshLayout swipeRefreshLayout;

    String emp_code,org_id, nameAr_txt, nameEn_txt, nationality_txt, gender_txt, religion_txt, soicalstate_txt, birthdate_g,
            birthcountry_txt, birthcity_txt, phone_txt, email_txt, image_txt;

    public static void check(String text, TextView v) {
        if ( text .equals("null")) {
            v.setText("");

        } else {
            v.setText(text);

        }
    }


    public static void longInfo(String str) {
        if (str.length() > 4000) {
            Log.i("taggs", str.substring(0, 4000));
            longInfo(str.substring(4000));
        } else
            Log.i("taggs", str);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.personal_info, container, false);
        getActivity().setTitle(getString(R.string.personal_data));

        nameEn = (TextView) v.findViewById(R.id.englishName);
        nameAr = (TextView) v.findViewById(R.id.name);
        Email = (TextView) v.findViewById(R.id.email);
        phone = (TextView) v.findViewById(R.id.phone);
        Gender = (TextView) v.findViewById(R.id.gender);
        image = (ImageView) v.findViewById(R.id.profile_img);
        progress = (ProgressBar) v.findViewById(R.id.progress);
        progress2 = (ProgressBar) v.findViewById(R.id.progress2);
        swipeRefreshLayout = (SwipeRefreshLayout)v. findViewById(R.id.swipe_refresh_layout);
        //nationality=(TextView)v.findViewById(R.id.nationality);
        socialState = (TextView) v.findViewById(R.id.socialState);
        birthDate_H = (TextView) v.findViewById(R.id.birthDate_H);
        birthDate_G = (TextView) v.findViewById(R.id.birthDate_G);
        B_city = (TextView) v.findViewById(R.id.city);
        B_country = (TextView) v.findViewById(R.id.country);
        religion = (TextView) v.findViewById(R.id.religion);
        note = (TextView) v.findViewById(R.id.note);

        swipeRefreshLayout.setOnRefreshListener(this);

        intialize();
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        viewData();
                                    }
                                }
        );


        // test("Get_Emp_cities");
        //test("Get_Emp_Soicalstate");
        //test("Get_Emp_countries");
        return v;
    }
     public void intialize(){
         api = new Api(getActivity());
         shared = getActivity().getSharedPreferences("user", 0);
         org_id = shared.getString("org_id", "");
         emp_code = shared.getString("emp_code", "");
         Log.d("emp_code", emp_code);
         Log.d("org_id", org_id);
       //  Log.d("emp_code", emp_code);
     }

    public void viewData() {
        api.getEmpData(org_id, emp_code, "Get_PersonalData", new RequestInterface() {
            @Override
            public void onResponse(String response) {

                progress.setVisibility(View.GONE);
                Log.d("response", response);

                String n = response.substring(1, response.length() - 1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                //   n = n.replaceAll("\\s+","");

                // String jsonResponse="{\n"+"\"employee\":"+n+"}";
                Log.d("response", n);

                try {
                    JSONObject fileObj = new JSONObject(n);
                    JSONArray arr = fileObj.getJSONArray("PersonalData");
                    JSONObject emp = arr.getJSONObject(0);
                    String EMP_CODE = emp.getString("EMP_CODE");
                    String EMP_id = emp.getString("EMP_ID");
                    String barCode = emp.getString("BARCOD");
                    nameAr_txt = emp.getString("NM_AR");
                    nameEn_txt = emp.getString("NM_EN");
                    nationality_txt = emp.getString("NATIONALITY_TXT");
                    gender_txt = emp.getString("GENDER_TXT");
                    religion_txt = emp.getString("RELIGION_TXT");
                    soicalstate_txt = emp.getString("SOICALSTATE_TXT");
                    birthdate_g = emp.getString("BIRTHDATE_G");
                    String birthdate_h = emp.getString("BIRTHDATE_H");
                    birthcountry_txt = emp.getString("BIRTHCOUNTRY_TXT");
                    birthcity_txt = emp.getString("BIRTHCITY_TXT");
                    phone_txt = emp.getString("PHONE");
                    email_txt = emp.getString("EMAIL");
                    String note_txt = emp.getString("NOTE");
                 //   image_txt = emp.getString("IMG");


                    check(nameAr_txt, nameAr);
                    check(nameEn_txt, nameEn);
                    check(email_txt, Email);
                    check(phone_txt, phone);
                    check(religion_txt, religion);
                    check(birthdate_g, birthDate_G);
                    check(birthdate_h, birthDate_H);
                    check(birthcity_txt, B_city);
                    check(birthcountry_txt, B_country);
                    check(gender_txt, Gender);
                    check(note_txt, note);
                    check(soicalstate_txt, socialState);
                    getProfileImage();
                  /*  byte[] byte_arr = Base64.decode(image_txt, Base64.DEFAULT);
                    Bitmap b = BitmapFactory.decodeByteArray(byte_arr, 0, byte_arr.length);
                    image.setImageBitmap(b);*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {
                progress.setVisibility(View.GONE);
                //Toast.makeText(getActivity(), " connection error", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void getProfileImage(){
        progress2.setVisibility(View.VISIBLE);
        image.setVisibility(View.GONE);
        api.getEmpData(org_id, emp_code, "Get_Emp_Photo", new RequestInterface() {
            @Override
            public void onResponse(String response) {
                String n = response.substring(1, response.length() - 1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                //   n = n.replaceAll("\\s+","");

                // String jsonResponse="{\n"+"\"employee\":"+n+"}";
                Log.d("responseImage", n);
                JSONObject fileObj = null;
                try {
                    fileObj = new JSONObject(n);
                    JSONArray arr = fileObj.getJSONArray("Emp_Photo");
                    JSONObject emp = arr.getJSONObject(0);
                     image_txt = emp.getString("IMG");
progress2.setVisibility(View.GONE);
image.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    byte[] byte_arr = Base64.decode(image_txt, Base64.DEFAULT);
                    Bitmap b = BitmapFactory.decodeByteArray(byte_arr, 0, byte_arr.length);
                    image.setImageBitmap(b);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.edit_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit) {

            Fragment f = new info_personal_edit();
            Bundle b = new Bundle();
            b.putString("emp_code", emp_code);
            b.putString("ar_name", nameAr_txt);
            b.putString("en_name", nameEn_txt);
            b.putString("nationality", nationality_txt);
            b.putString("gender", gender_txt);
            b.putString("country", birthcountry_txt);
            b.putString("city", birthcity_txt);
            b.putString("birth", birthdate_g);
            b.putString("phone", phone_txt);
            b.putString("email", email_txt);
            b.putString("social", soicalstate_txt);
            b.putString("image", image_txt);
            b.putString("religion", religion_txt);
            f.setArguments(b);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, f, "info_personal_edit").addToBackStack("info_personal_edit").commit();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void test(String function_name) {
        Api api = new Api(getActivity());
        api.getCities("001", function_name, new RequestInterface() {
            @Override
            public void onResponse(String response) {
                String n = response.substring(1, response.length() - 1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                longInfo(n);
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void onRefresh() {
        viewData();
    }
}
