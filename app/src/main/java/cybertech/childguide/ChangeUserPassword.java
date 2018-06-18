package cybertech.childguide;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ChangeUserPassword extends AppCompatActivity {
    EditText pass_ED, conPass__ED;
    TextView display_changePass;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_password);
        pass_ED = (EditText) findViewById(R.id.pass_Change);
        conPass__ED = (EditText) findViewById(R.id.confirm_Pass_Change);
        display_changePass = (TextView) findViewById(R.id.changePass_display);
        tinyDB = new TinyDB(this);
    }

    public void OnCHanggePassword(View view) {
        if (!pass_ED.getText().toString().trim().isEmpty() && !conPass__ED.getText().toString().trim().isEmpty()) {
            if (pass_ED.getText().toString().trim().equals(conPass__ED.getText().toString().trim())) {
                tinyDB.remove("password");
                tinyDB.putString("password", conPass__ED.getText().toString().trim());
                AlertDialog.Builder builder = new AlertDialog.Builder(ChangeUserPassword.this);
                builder.setCancelable(false);
                builder.setMessage("Your password has been change");
                DialogInterface.OnClickListener onOK = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(ChangeUserPassword.this, Login.class));
                        finish();
                    }
                };
                builder.setNegativeButton("OK", onOK);
                builder.show();
            }
        } else {
            display_changePass.setText(R.string.requiredfields);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ChangeUserPassword.this, ParentHome.class));
        finish();
    }
}
