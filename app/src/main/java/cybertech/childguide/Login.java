package cybertech.childguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    EditText et_username, et_password;
    TextView display;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = (EditText) findViewById(R.id.et_username_LOGIN);
        et_password = (EditText) findViewById(R.id.et_pass_login);
        display = (TextView) findViewById(R.id.tv_display_parentLogin);
        et_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display.setText("");
            }
        });
        et_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display.setText("");
            }
        });
        tinyDB = new TinyDB(this);
    }

    public void onLogin(View view) {
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();
        if (!username.isEmpty() && !password.isEmpty()) {
            if (username.equals(tinyDB.getString("username"))) {
                if (password.equals(tinyDB.getString("password"))) {
                    startActivity(new Intent(Login.this, ParentHome.class));
                    finish();
                } else {
                    display.setText(R.string.invalidpass);
                    et_password.setText("");
                }
            } else {
                display.setText(R.string.invaldusername);
                et_username.setText("");
            }
        } else {
            display.setText(R.string.requiredfields);
        }
    }
}
