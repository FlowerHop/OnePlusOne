package com.example.caizongming.oneplusone;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class RankActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new RankFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rank, menu);
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
    public static class RankFragment extends Fragment {

        private final static int PORT = 12345;
        private final static String IP = "140.115.205.68";
        private String msg;
        private Handler UIHandler;
        private Socket socket;

        private ListView list_rank;
        private ArrayAdapter adapter;
        public RankFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_rank, container, false);
            list_rank = (ListView) rootView.findViewById(R.id.list_rank);
            UIHandler = new Handler();
            initConnection.start();
            return rootView;
        }

        private Thread initConnection = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Log.e("DEBUG", "Start Connection.");
                    socket = new Socket(IP, PORT);
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    bw.write("[Rank]" + "\n");
                    bw.flush();


                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    msg = br.readLine();

                    UIHandler.post(updateList);
                    Log.e("DEBUG", "Read");
                    bw.close();
                    br.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        @Override
        public void onPause() {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            getActivity().finish();
            super.onPause();
        }

        private Runnable updateList = new Runnable() {
            @Override
            public void run() {
                String msgArray[] = msg.split(",");
                String items[] = new String[msgArray.length];

                for (int i = 0; i < msgArray.length; i++) {
                    String temp = (i + 1) + ".   " + msgArray[i].split(":")[0] + "     -    " + msgArray[i].split(":")[2];
                    items[i] = temp;
                }

                adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, items);
                list_rank.setAdapter(adapter);
            }
        };
    }
}
