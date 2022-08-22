package com.max.rm.hr.Employee.infoPkg;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.max.rm.hr.Employee.Api;
import com.max.rm.hr.Employee.RequestInterface;
import com.max.rm.hr.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class info_archive extends Fragment {
    RecyclerView archiveRc;
    ProgressBar progressBar;
    Api api;
    SharedPreferences shared;
    String emp_code,org_id;
     int archive_id;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.info_archive,null);
        archiveRc=(RecyclerView) v.findViewById(R.id.archive_list);
        getActivity().setTitle(getString(R.string.archive));
        progressBar=(ProgressBar) v.findViewById(R.id.progress);
         intialize();
        viewData();
        return  v;
    }
    public void intialize(){
        api = new Api(getActivity());
        shared = getActivity().getSharedPreferences("user", 0);
        org_id = shared.getString("org_id", "");
        emp_code = shared.getString("emp_code", "");
    }

    public void viewData(){
        api.getEmpData(org_id,emp_code,"Get_Emp_Achive", new RequestInterface() {
            @Override

            public void onResponse(String response) {
                Log.d("responsen", response);
                 progressBar.setVisibility(View.GONE);
                String n= response.substring(1,response.length()-1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                Log.d("responsegover", n);

                try {
                    JSONObject ob= new JSONObject(n);
                   final ArrayList<info_archive_class> archive_list= new ArrayList<>();
                   JSONArray docs_arr= ob.getJSONArray("JS_EMP_ACHIVE");
                    for( int i =0; i <docs_arr.length();i++){
                        JSONObject docObj= docs_arr.getJSONObject(i);
                        String name= docObj.getString("ACHIVE_TITLE");
                       // String image= docObj.getString("ACHIVE_FILE");
                         int archive_id=docObj.getInt("ACHIVE_CODE");
                        info_archive_class doc= new info_archive_class(name,archive_id);
                        archive_list.add(doc);
                    }
                    GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 1);
                    archiveRc.setLayoutManager(lLayout);
                    archiveRc.setHasFixedSize(true);
                    inf_archive_adapter adapter= new inf_archive_adapter(archive_list, getActivity(), new rec_interface() {
                        @Override
                        public void onRecItemSelected(int position, View view) {
                             progressBar.setVisibility(View.VISIBLE);
                             archive_id=archive_list.get(position).getId();
                            getArchiveImage(archive_id);
                        }
                    });
                    archiveRc.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {
               // Log.d("response", "error");
               // viewData();
            }
        });
    }
     public void getArchiveImage(final int archiveId){
api.getArchiveImage(org_id, emp_code,archiveId, new RequestInterface() {
    @Override
    public void onResponse(String response) {
        //String image= docObj.getString("ACHIVE_FILE");
        String n= response.substring(1,response.length()-1);
        n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
        Log.d("responsegover", n);

        try {
            JSONObject ob= new JSONObject(n);
            JSONArray docs_arr= ob.getJSONArray("JS_EMP_ACHIVE");
            for( int i =0; i <docs_arr.length();i++){
                JSONObject docObj= docs_arr.getJSONObject(i);
                 String image= docObj.getString("ACHIVE_FILE");
                 progressBar.setVisibility(View.GONE);

                 //viewArchiveImage(image);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError() {
        getArchiveImage(archiveId);
    }
});


     }
      public void viewArchiveImage(String image){
         /*   Dialog dialog= new Dialog(getActivity());
             dialog.setContentView( R.layout.info_archive_image);
             ImageView imageView= (ImageView)dialog.findViewById(R.id.image);
             byte[] byte_arr= Base64.decode(image,Base64.DEFAULT);
             Bitmap b= BitmapFactory.decodeByteArray(byte_arr,0,byte_arr.length);
             imageView.setImageBitmap(b);
             PhotoViewAttacher pAttacher;
             pAttacher = new PhotoViewAttacher(imageView);
             pAttacher.update();
             dialog.show();*/
      }
}
