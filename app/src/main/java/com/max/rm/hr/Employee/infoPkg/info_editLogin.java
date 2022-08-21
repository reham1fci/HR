package com.androidmax.max.hr.Employee.infoPkg;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidmax.max.hr.Employee.Api;
import com.androidmax.max.hr.Employee.RequestInterface;
import com.androidmax.max.hr.R;

import org.json.JSONException;
import org.json.JSONObject;

public class info_editLogin extends Fragment {
    EditText name,password;
    Button save;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.info_edite_login,null);
        getActivity().setTitle(getString(R.string.edit_login));
name=(EditText)v.findViewById(R.id.user_name);
password=(EditText)v.findViewById(R.id.password);
save=(Button) v.findViewById(R.id.save);
        SharedPreferences shared= getActivity().getSharedPreferences("user",0);
        String user_name=shared.getString("emp_name","");
        name.setText(user_name);

save.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        String newName=name.getText().toString();
        String newPassword=password.getText().toString();
        editLogin(newName, newPassword);

    }
});
        return  v;
    }
    public void editLogin(String user_name, String pass){
        Api api= new Api(getActivity());
        SharedPreferences shared= getActivity().getSharedPreferences("user",0);
        String org_id=shared.getString("org_id","");
        String emp_code=shared.getString("emp_code","");
        api.editLoginData(emp_code, org_id, user_name, pass, new RequestInterface() {
            @Override
            public void onResponse(String response) {
                String n = response.substring(1, response.length() - 1);
                n = n.replaceAll("(\\\\r\\\\n|\\\\|)", "");
                //  n = n.replaceAll("\\s+","");

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
