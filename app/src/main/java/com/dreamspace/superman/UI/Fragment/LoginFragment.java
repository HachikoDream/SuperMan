package com.dreamspace.superman.UI.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dreamspace.superman.API.GetService;
import com.dreamspace.superman.API.SupermanService;
import com.dreamspace.superman.R;
import com.dreamspace.superman.model.ErrorRes;
import com.dreamspace.superman.model.LoginReq;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private SupermanService smService;
    private Button mButton;
    private EditText phoneEt;
    private EditText pwdEt;
    public LoginFragment() {
        // Required empty public constructor
    }
    private void initRestClient() {

        smService= GetService.getService(GetService.getRestClient());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_login, container, false);
        mButton= (Button) view.findViewById(R.id.mybtn);
        phoneEt=(EditText)view.findViewById(R.id.username_ev);
        pwdEt=(EditText)view.findViewById(R.id.pwd_ed);
        initRestClient();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginReq req=new LoginReq();
                req.setPassword(pwdEt.getText().toString());
                req.setPhone(phoneEt.getText().toString());
                smService.createAccessToken(req, new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        Log.i("INFO",s);
                        Log.i("INFO",response.getReason());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                      Log.i("INFO",error.getMessage());
                      Log.i("INFO",error.getBodyAs(ErrorRes.class).toString());

                    }
                });
            }
        });
        return view;
    }


}
