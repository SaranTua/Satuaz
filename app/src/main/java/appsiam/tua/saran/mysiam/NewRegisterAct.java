package appsiam.tua.saran.mysiam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class NewRegisterAct extends AppCompatActivity {

    //Explicit
    private ImageView imageView;
    private EditText nameEditText, userEditText, paswordEditText;
    private Button button;
    private String nameString, userString, passwordString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_register);

        //Initial View
        initialView();

        //Back Controller
        backController();

        //NewRegister Controller
        newRegisterController();

    }   //Main Method

    private void newRegisterController() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Get Value From Edit Text
                nameString = nameEditText.getText().toString().trim();
                userString = userEditText.getText().toString().trim();
                passwordString = paswordEditText.getText().toString().trim();

                //Check Space
                if (nameString.equals("") || userString.equals("") || passwordString.equals("")) {
                    //Have space
                    MyAlert myAlert = new MyAlert(NewRegisterAct.this);
                    myAlert.myDialog("Have space", "Please Fill All Blank");
                } else {
                    //No space
                    uploadValueToServer();
                }

            }
        });
    }

    private void uploadValueToServer() {

        try {
            PostDataToServer postDataToServer = new PostDataToServer(NewRegisterAct.this);
            postDataToServer.execute(nameString, userString, passwordString, "http://androidthai.in.th/siam/addDataMaster.php");

            if (Boolean.parseBoolean(postDataToServer.get())) {
                finish();
            } else {
                MyAlert myAlert = new MyAlert(NewRegisterAct.this);
                myAlert.myDialog("Cannot Upload", "Please Try Again");
            }


        } catch (Exception e) {
            Log.d("$", "e upload ==> " + e.toString());
        }
    }

    private void backController() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initialView() {
        imageView = (ImageView) findViewById(R.id.btnBack);
        nameEditText = (EditText) findViewById(R.id.edtName);
        userEditText = (EditText) findViewById(R.id.edtUser);
        paswordEditText = (EditText) findViewById(R.id.edtPassword);
        button = (Button) findViewById(R.id.btnNewRegister);
    }

}   //Main Class
