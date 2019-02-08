package com.example.caizongming.oneplusone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new LoginFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class LoginFragment extends Fragment {

        private EditText edit_username;
        private Spinner spinner_icon;
        private Button btn_login;

        private String[] icons_array = {"Man 01", "Man 02", "Man 03", "Man 04", "Man 05", "Woman 01", "Woman 02", "Woman 03", "Woman 04", "Woman 05"};

        public LoginFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_login, container, false);

            edit_username = (EditText) rootView.findViewById(R.id.edit_username);
            btn_login = (Button) rootView.findViewById(R.id.btn_login);
            spinner_icon = (Spinner) rootView.findViewById(R.id.spinner_icon);

            edit_username.setText(Pref.getUsername(getActivity()));
            spinner_icon.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, icons_array));
            spinner_icon.setSelection(Pref.getIcon(getActivity()));

            setListeners();
            return rootView;
        }

        private void setListeners(){
            btn_login.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String username = edit_username.getText().toString();
                    if (!username.equals("")) {
                        Pref.setUsername(getActivity(), edit_username.getText().toString());
                        Pref.setIcon(getActivity(), spinner_icon.getSelectedItemPosition());


                        Intent intent = new Intent();
                        intent.setClass(getActivity(), HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "請輸入使用者名稱", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), Pref.class);
                        startActivity(intent);
                    }

                }
            });
        }
    }
}
