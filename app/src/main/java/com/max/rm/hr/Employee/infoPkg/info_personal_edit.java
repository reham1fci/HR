package com.max.rm.hr.Employee.infoPkg;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.max.rm.hr.Employee.Api;
import com.max.rm.hr.Employee.RequestInterface;
import com.max.rm.hr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class info_personal_edit extends Fragment {
    EditText fName , mName,lName,fName2 , mName2,lName2,Email, phone,Gender
            ,birthDate_G,religion, nationality;
    ImageView image,changeImage;
    ArrayAdapter<String> adapterCity, adapterCountry,adapterSocial;
    Spinner B_city,B_country,socialState;
    importatnt im;
    String emp_code;
    int selectCountryId,selectCityId,selectSocialId;
    String fName_ar, mName_ar, lName_ar, fName_en,phone_txt,email_txt, mName_en, lName_en,  image_txt,select_country,select_city,select_social, BirthDay;
  public static  final  int CAMERA_REQUEST=100;
  public static  final  int RESULT_LOAD_IMG=200;
    public static  final  int GALLERY_READ=300;
    ArrayList<importatnt>cityList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.info_personal_edit, container, false);

        Email=(EditText) v.findViewById(R.id.email);
        fName=(EditText) v.findViewById(R.id.fname);
        mName=(EditText) v.findViewById(R.id.Mname);
        lName=(EditText) v.findViewById(R.id.Lname);
        fName2=(EditText) v.findViewById(R.id.fName2);
        mName2=(EditText) v.findViewById(R.id.Mname2);
        lName2=(EditText) v.findViewById(R.id.Lname2);
        phone=(EditText)v.findViewById(R.id.phone);
        Gender=(EditText) v.findViewById(R.id.gender);
        image=(ImageView)v.findViewById(R.id.profile_img) ;
        changeImage=(ImageView)v.findViewById(R.id.changeImage) ;
        nationality=(EditText) v.findViewById(R.id.nationality);
        socialState=(Spinner) v.findViewById(R.id.socialState);
        birthDate_G=(EditText)v.findViewById(R.id.birthDate_G);
        B_city=(Spinner)v.findViewById(R.id.city);
        B_country=(Spinner)v.findViewById(R.id.country);
        religion=(EditText)v.findViewById(R.id.religion);
    cityList= new ArrayList<>();

       birthDate_G.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               changeDate();

           }
       });
         im= new importatnt();
        viewData();
        citySpinner();
         socialSpinner();
         countrySpinner();
        return v;
    }
    public void viewData(){

        Bundle b= getArguments();
        String emp_code=  b.getString("emp_code");
        String ar_name=  b.getString("ar_name");
        String en_name=    b.getString("en_name");
        String    nationality_txt  =b.getString("nationality");
        String gender_txt=   b.getString("gender");
        select_country=  b.getString("country");
        select_city=    b.getString("city");
        BirthDay=   b.getString("birth");
         phone_txt= b.getString("phone");
         email_txt=   b.getString("email");
        select_social= b.getString("social");
        image_txt= b.getString("image");
        String religion_txt= b.getString("religion");
        String[] nameAr_arr = ar_name.split(" ");
        String[] nameEn_arr = en_name.split(" ");
        displayImage(image_txt);
        if(Locale.getDefault().getDisplayLanguage().equals("العربية")){
            fName_ar=nameAr_arr[0];
            mName_ar=nameAr_arr[1];
            lName_ar=nameAr_arr[2];
            for(int i=0; i<nameEn_arr.length;i++){
                    if(i==0){
                        fName_en=nameEn_arr[0];
                        check(fName_en,fName2);
                    }
               else if(i==1){
                        mName_en=nameEn_arr[1];
                        check(mName_en,mName2);

                    }
               else if(i==2){
                        lName_en=nameEn_arr[2];
                        check(lName_en,lName2);
                    }
            }

            check(fName_ar,fName);
            check(mName_ar,mName);
            check(lName_ar,lName);

        }
        else {
            for(int i=0; i<nameEn_arr.length;i++){
                if(i==0){
                    fName_en=nameEn_arr[0];
                    check(fName_en,fName);
                }
                else if(i==1){
                    mName_en=nameEn_arr[1];
                    check(mName_en,mName);

                }
                else if(i==2){
                    lName_en=nameEn_arr[2];
                    check(lName_en,lName);
                }
            }

            fName_ar=nameAr_arr[0];
            mName_ar=nameAr_arr[1];
            lName_ar=nameAr_arr[2];
            check(fName_ar,fName2);
            check(mName_ar,mName2);
            check(lName_ar,lName2);

        }



        check(email_txt,Email);
        check(phone_txt,phone);
        //check(religion_txt,religion);
        check(BirthDay,birthDate_G);
        //check(city_txt,B_city);
        //  check(country_txt,B_country);
      //  check(gender_txt,Gender);
       // check(nationality_txt,nationality);
        //  check(social_txt,socialState);
        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageWindow();
            }
        });
    }


    public   void check( String text, TextView v){
        if (text.equals("null")){
            v.setText("");

        }
        else {
            v.setText(text);

        }
    }
    public void citySpinner(){
        adapterCity = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1);

        getCities();

        B_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectCityId=   cityList.get(i).getqTypeId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    public void countrySpinner(){
        adapterCountry = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1);
     int country_position=0;
    final ArrayList<importatnt>countries=im.getCountries();
    for ( int i=0;i<countries.size();i++){
         if(countries.get(i).getqName().equals(select_country)){
             country_position=i;
         }
        adapterCountry.add(countries.get(i).getqName());
    }
    B_country.setAdapter(adapterCountry);
    B_country.setSelection(country_position);
    B_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectCountryId=   countries.get(i).getqTypeId();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    });
}
    public void socialSpinner(){
        adapterSocial = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1);

        final ArrayList<importatnt>Social=im.getSocialState();
       int social_position=0;
    for ( int i=0;i <Social.size();i++){
        if(Social.get(i).getqName().equals(select_social)){
            social_position=i;
        }
        adapterSocial.add(Social.get(i).getqName());
    }
    socialState.setSelection(social_position);
    socialState.setAdapter(adapterSocial);
    socialState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            selectSocialId=Social.get(i).getqTypeId();
            Log.d("social", String.valueOf(selectSocialId));


        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    });
}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void intent_gallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),RESULT_LOAD_IMG);
    }
    public void intent_camera(){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }
    public void imageWindow() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.camera_gallary);
        ImageButton camera = (ImageButton) dialog.findViewById(R.id.camera);
        ImageButton gallery = (ImageButton) dialog.findViewById(R.id.gallery);
        Button cancle = (Button) dialog.findViewById(R.id.cancle);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               intent_camera();
                dialog.dismiss();

            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReadStorageAllowed()) {
                    intent_gallery();
                    dialog.dismiss();

                } else {

                    requestStoragePermission();

                    dialog.dismiss();

                }


            }
        });
        dialog.show();


}
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;

    }
    private void requestStoragePermission(){
        //And finally ask for the permission
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},GALLERY_READ);
        // intent_gallery();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == getActivity().RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                decodeUri(selectedImage);
            }
            else if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK ) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
               image.setImageBitmap(photo);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                image_txt = Base64.encodeToString(byte_arr, 0);
                //Log.d("image", image_str);
               // displayImage(image_txt);

            }
            else {
                Toast.makeText(this.getActivity(), "You haven't picked Image",Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this.getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == GALLERY_READ){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //  code here

                //Displaying a toast
                Toast.makeText(getActivity(),"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
                intent_gallery();
            }
            else{

            }

    }

}
    public void decodeUri(Uri uri) {
        ParcelFileDescriptor parcelFD = null;
        try {
            parcelFD = getActivity().getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor imageSource = parcelFD.getFileDescriptor();

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(imageSource, null, o);

            // the new size we want to scale to
            final int REQUIRED_SIZE = 1024;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(imageSource, null, o2);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] byte_arr = stream.toByteArray();
            image_txt = Base64.encodeToString(byte_arr, 0);
            image.setImageBitmap(bitmap);
           // displayImage(image_txt);

        } catch (FileNotFoundException e) {
            // handle errors
        } finally {
            if (parcelFD != null)
                try {
                    parcelFD.close();
                } catch (IOException e) {
                    // ignored
                }
        }
    }
    public void displayImage(String image_txt){
        byte[] byte_arr= Base64.decode(image_txt,Base64.DEFAULT);
        Bitmap b_image= BitmapFactory.decodeByteArray(byte_arr,0,byte_arr.length);
        image.setImageBitmap(b_image);
    }
    public  void getCities(){
        Api api= new Api(getActivity());
        SharedPreferences shared= getActivity().getSharedPreferences("user",0);
        String org_id=shared.getString("org_id","");
        api.getCities(org_id, "Get_Emp_cities", new RequestInterface() {
            @Override
            public void onResponse(String response) {
                String n= response.substring(1,response.length()-1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                int city_position=0;
                try {
                    JSONObject object= new JSONObject(n);
                 JSONArray cities =object.getJSONArray("Sys_CodeList");
                 for(int i=0; i<cities.length();i++){
                     JSONObject cityObject= cities.getJSONObject(i);
                String cityName=cityObject.getString("ITEM_NMAR");
                adapterCity.add(cityName);
                int cityId=cityObject.getInt("ITEM_CODE");
                if(cityName.equals(select_city)){
                    city_position=i;

                }
                     cityList.add(new importatnt(cityId,cityName));
                 }
                 B_city.setAdapter(adapterCity);
                 B_city.setSelection(city_position);
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
        getActivity().getMenuInflater().inflate(R.menu.save_edit,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.save){
            String fName1_txt=fName.getText().toString();
            String mName1_txt=mName.getText().toString();
            String lName1_txt=lName.getText().toString();
            String fName2_txt=fName2.getText().toString();
            String mName2_txt=mName2.getText().toString();
            String lName2_txt=lName2.getText().toString();
            email_txt=Email.getText().toString();
            phone_txt=phone.getText().toString();
            BirthDay=birthDate_G.getText().toString();

            if(Locale.getDefault().getDisplayLanguage().equals("العربية")){
                if(fName1_txt.isEmpty()){
                    fName.setText(getString(R.string.enter)+getString(R.string.fName));
                }
                else if(mName1_txt.isEmpty()){
                    mName.setText(getString(R.string.enter)+getString(R.string.mName));
                }
                else if(lName1_txt.isEmpty()){
                    lName.setText(getString(R.string.enter)+getString(R.string.lName));
                }
                else if(BirthDay.isEmpty()){
                    lName.setText(getString(R.string.enter)+getString(R.string.birth_day));

                }

                else {
                    fName_ar=fName1_txt;
                    mName_ar=mName1_txt;
                    lName_ar=lName1_txt;
                    fName_en=fName2_txt;
                    mName_en=mName2_txt;
                    lName_en=lName2_txt;

                    if(email_txt.isEmpty()){
                        email_txt="null";
                    }
                     if (phone_txt.isEmpty()){
                        email_txt="null";

                    }
                    saveProfileEdit();
                }
            }
            else {
                if(fName2_txt.isEmpty()){
                    fName2.setText(getString(R.string.enter)+getString(R.string.fName));
                }
                else if(mName2_txt.isEmpty()){
                    mName2.setText(getString(R.string.enter)+getString(R.string.mName));
                }
                else if(lName2_txt.isEmpty()){
                    lName2.setText(getString(R.string.enter)+getString(R.string.lName));
                }

              else {
                    fName_ar=fName2_txt;
                    mName_ar=mName2_txt;
                    lName_ar=lName2_txt;
                    fName_en=fName1_txt;
                    mName_en=mName1_txt;
                    lName_en=lName1_txt;
                if(email_txt.isEmpty()){
                    email_txt="null";
                }
                if (phone_txt.isEmpty()){
                    email_txt="null";

                }
                saveProfileEdit();
            }}
        }
        return false;
    }

    public  void changeDate(){
    final Dialog d = new Dialog(getActivity());
                d.setContentView(R.layout.date);
    CalendarView calendarView = (CalendarView) d.findViewById(R.id.calendarView);
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
        @Override
        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
            BirthDay = String.valueOf(dayOfMonth) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(year);
            //Log.d("date",date_str);
            birthDate_G.setText(BirthDay);
        }

    });
    Button done= (Button)d.findViewById(R.id.done);
                done.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            d.dismiss();
        }
    });
                d.show();}

public void saveProfileEdit() {
    Api api = new Api(getActivity());
    SharedPreferences shared = getActivity().getSharedPreferences("user", 0);
    String org_id = shared.getString("org_id", "");
    emp_code = shared.getString("emp_code", "");
    api.editProfile(org_id, emp_code, fName_ar, mName_ar, lName_ar, fName_en, mName_en, lName_en, selectSocialId, selectCountryId, selectCityId, BirthDay, phone_txt, email_txt, image_txt, new RequestInterface() {
        @Override
        public void onResponse(String response) {
            String n = response.substring(1, response.length() - 1);
            n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");

            Log.d("msg",n);
            try {
                JSONObject object = new JSONObject(n);
                String login = object.getString("Msg");
                Toast.makeText(getActivity(), login, Toast.LENGTH_SHORT).show();

                if (login.equals("Success")) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError() {


        }
    });
}


}

