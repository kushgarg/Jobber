package comorgsminorproj.httpsgithub.jobber;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by MK on 17-11-2017.
 */

public class Rlogin extends Fragment implements View.OnClickListener {

    private EditText emailid;
    private  EditText passwd;
    private Button login;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.r_login,container,false);

        progressDialog = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();


        emailid = (EditText) view.findViewById(R.id.remail);
        passwd = (EditText) view.findViewById(R.id.rpassword);
        login = (Button) view.findViewById(R.id.rlogin);

        login.setOnClickListener(this);


        return view;
    }

    private void loginRecruiter(){

        String email = emailid.getText().toString().trim();
        String password = passwd.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "Please enter email !", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Please enter password !", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Logging In ...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user != null) {
                                if (user.isEmailVerified()) {
                                    Intent i = new Intent(getActivity(),Rpage.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getActivity(),"Please verify your EmailId",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else{
                            Toast.makeText(getActivity(),"Please Register first",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if(v == login)
            loginRecruiter();
    }
}
