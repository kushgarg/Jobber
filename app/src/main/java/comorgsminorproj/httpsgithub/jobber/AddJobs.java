package comorgsminorproj.httpsgithub.jobber;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.support.design.widget.Snackbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddJobs extends AppCompatActivity implements View.OnClickListener{

    Button mlocation;
    Button add;
    String[] listitems;
    boolean[] checkitems;
    ArrayList<Integer> mUseritems =new ArrayList<>();
    final ArrayList<String> selected = new ArrayList<>();
    AutoCompleteTextView textView;
    String desg;
    String type;
    String salary;
    EditText sal;
    Spinner t;

    RelativeLayout rl;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jobs);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        textView = (AutoCompleteTextView) findViewById(R.id.designation);
        String[] array = getResources().getStringArray(R.array.designation_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
        textView.setAdapter(adapter);

        mlocation = (Button) findViewById(R.id.location_id);
        add = (Button) findViewById(R.id.add);
        sal = (EditText) findViewById(R.id.salary_id);
        t = (Spinner) findViewById(R.id.t);
        rl = (RelativeLayout) findViewById(R.id.rl);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.type, android.R.layout.simple_spinner_item);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        t.setAdapter(adapter2);

        mlocation.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    private void location() {
        listitems = getResources().getStringArray(R.array.Location);
        checkitems = new boolean[listitems.length];


                AlertDialog.Builder mbuilder = new AlertDialog.Builder(AddJobs.this);
                mbuilder.setTitle("Location");
                mbuilder.setMultiChoiceItems(listitems, checkitems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        if (isChecked) {
                            if (!mUseritems.contains(which)) {
                                mUseritems.add(which);
                                selected.add(listitems[which]);
                            } else {
                                mUseritems.remove(which);
                                selected.remove(which);
                            }
                        }

                    }
                });
                mbuilder.setCancelable(false);
                mbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        for (int i = 0; i < mUseritems.size(); i++) {
                            item = item + listitems[mUseritems.get(i)];
                            if (i != mUseritems.size() - 1) {
                                item = item + ";";
                            }
                        }


                    }
                });
                mbuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();


                    }
                });
                mbuilder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkitems.length; i++) {
                            checkitems[i] = false;
                            mUseritems.clear();

                        }

                    }
                });
                AlertDialog mDialog = mbuilder.create();
                mDialog.show();



}
    private void submit() {

        desg = textView.getText().toString();
        salary = sal.getText().toString().trim();
        type = t.getSelectedItem().toString();

        Job j = new Job(desg,type,salary,selected);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userID = user.getUid();

            mDatabase.child("recruiter").child(userID).child("Jobs").push().setValue(j,new DatabaseReference.CompletionListener(){
                public void onComplete(DatabaseError error,DatabaseReference reference){
                    if (error != null){
                        Snackbar s = Snackbar.make(rl,"Unsuccessful",Snackbar.LENGTH_SHORT);
                        s.show();
                    }else {
                        Snackbar s = Snackbar.make(rl,"Job for "+desg+" Added",Snackbar.LENGTH_SHORT);
                        s.show();
                        selected.clear();
                    }
                }
            });

        }

    }

    @Override
    public void onClick(View v) {
        if(v == mlocation)
            location();
        if(v == add)
            submit();
    }
}



