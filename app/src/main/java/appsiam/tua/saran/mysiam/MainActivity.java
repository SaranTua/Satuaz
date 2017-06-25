package appsiam.tua.saran.mysiam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import static appsiam.tua.saran.mysiam.R.id.btnLogin;

public class MainActivity extends AppCompatActivity {


    //Explicit คือ การประกาศตัวแปร
    private EditText userEditText, passwordEditText;
    private TextView textView; //shift+ctrl+enterเป็นตัวช่วยในการ Autocorrect
    private Button button;
    private String userString, passwordString;
    private MyAlert myAlert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initail View
        initialView();

        //TextView Controller
        textViewController();

        //Button Controller
        buttonController();

    }   // Method Main

    private void buttonController() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get Value From Edit Text
                userString = userEditText.getText().toString().trim();
                passwordString = passwordEditText.getText().toString();

                //Check Space
                if (userString.length() == 0 || passwordString.length() == 0) {
                    //Have Space
                    myAlert.myDialog(getResources().getString(R.string.titleHaveSpace),
                            getResources().getString(R.string.messageHaveSpace));

                } else {
                    //No Space
                    checkUserAndPass();
                }
            }
        });
    }

    private void checkUserAndPass() {
        try {

            String urlPHP = "http://androidthai.in.th/siam/getAllDataMaster.php";
            GetAllData getAllData = new GetAllData(MainActivity.this);
            getAllData.execute(urlPHP);
            String strJSON = getAllData.get();
            Log.d("SiamV1", "JSON ==>" + strJSON);

            JSONArray jsonArray = new JSONArray(strJSON);
            boolean b = true;

            String[] strings = new String[]{"id", "Name", "User", "Password"};
            String[] loginString1 = new String[strings.length];

            for (int i = 0; i < jsonArray.length(); i += 1) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if (userString.equals(jsonObject.getString("User"))) {


                    b = false;

                    for (int i1 = 0; i1 < strings.length; i1 += 1) {
                        loginString1[i1] = jsonObject.getString(strings[i1]);
                        Log.d("SiamV1", "loginString[" + i1 + "] ==> " + loginString1[i1]);
                    }

                }

            }//for
            if (b) {
                myAlert.myDialog(getResources().getString(R.string.titleUserFlase),
                        getResources().getString(R.string.messageUserFlase));
            } else if (passwordString.equals(loginString1[3])) {
                Toast.makeText(MainActivity.this, "Welcome" + loginString1[1],
                        Toast.LENGTH_SHORT).show();
            } else {
                myAlert.myDialog(getResources().getString(R.string.titlePasswordFlase),
                        getResources().getString(R.string.messagePasswordFlase));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void textViewController() {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent to NewRegisterActivity เป็นการสืบทอด
                Intent intent = new Intent(MainActivity.this, NewRegisterAct.class);
                startActivity(intent);//ให้มีการเปิดหน้า 2 และมันจะทับกับหน้าแรก


            }
        });
    }

    private void initialView() {

        myAlert = new MyAlert(MainActivity.this);

        userEditText = (EditText) findViewById(R.id.edtUser);
        passwordEditText = (EditText) findViewById(R.id.edtPassword);
        textView = (TextView) findViewById(R.id.txtNewRegister);
        button = (Button) findViewById(R.id.btnLogin);

    }

}   //Main Class นี่คือ คลาสหลัก
