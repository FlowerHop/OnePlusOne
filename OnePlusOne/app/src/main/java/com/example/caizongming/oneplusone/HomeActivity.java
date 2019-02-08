package com.example.caizongming.oneplusone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new HomeFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
    public static class HomeFragment extends Fragment {
        private TextView txtSingle;
        private TextView txtVS;
        private TextView txtRank;

        private Handler mHandler;

        public HomeFragment() {


        }

        private TextView.OnClickListener imgBtnActions = new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                switch (v.getId()) {
                    case R.id.image_button_single:
                        // todo to SingleActvity
                        intent.setClass(getActivity(), GameActivity.class);
                        startActivity(intent);

                        break;
                    case R.id.image_button_vs:
                        // todo to VSActivity
                        intent.setClass(getActivity(), PvPActivity.class);
                        startActivity(intent);

                        break;
                    case R.id.image_button_rank:
                        // todo to RankActivity
                        intent.setClass(getActivity(), RankActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            txtSingle = (TextView) rootView.findViewById(R.id.image_button_single);
            txtVS = (TextView) rootView.findViewById(R.id.image_button_vs);
            txtRank = (TextView) rootView.findViewById(R.id.image_button_rank);
            mHandler = new Handler();
            setListeners ();
            return rootView;
        }

        private void setListeners () {
            txtSingle.setOnClickListener(imgBtnActions);
            txtVS.setOnClickListener(imgBtnActions);
            txtRank.setOnClickListener(imgBtnActions);
        }


    }
}
