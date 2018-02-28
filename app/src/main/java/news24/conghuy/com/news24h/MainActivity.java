package news24.conghuy.com.news24h;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import news24.conghuy.com.news24h.common.Apis;
import news24.conghuy.com.news24h.common.ConnectionDetector;
import news24.conghuy.com.news24h.common.Const;
import news24.conghuy.com.news24h.common.PrefManager;
import news24.conghuy.com.news24h.common.RecyclerTouchListener;
import news24.conghuy.com.news24h.common.RootActivity;
import news24.conghuy.com.news24h.common.adapters.AdapterLeftMenu;
import news24.conghuy.com.news24h.controller.OneFragment;
import news24.conghuy.com.news24h.common.interfaces.ClickListener;
import news24.conghuy.com.news24h.model.News24h;

public class MainActivity extends RootActivity {
    private ConnectionDetector cd;
    private String TAG = "MainActivity";
    private ArrayList<News24h> listNews24h;
    private AdapterLeftMenu mAdapter;
    private RecyclerView recyclerView;
    private String list_title[];
    private String list_link[];
    private int temp = 0;
    private Context context;
    private PrefManager prefManager;
    private MenuItem action_settings;

    public void initLeftMenu() {
        recyclerView = (RecyclerView) findViewById(R.id.sliding_menu);
        mAdapter = new AdapterLeftMenu(this, listNews24h);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                closeDraw();
                setTitle(list_title[position]);
                if (temp == position)
                    return;
                temp = position;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            addFragment(new OneFragment(list_link[position], listAdv));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 500);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.addToBackStack("fragment");
        transaction.replace(R.id.root, fragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefManager = new PrefManager(this);

        listNews24h = new ArrayList<>();
        list_title = getResources().getStringArray(R.array.list_title);
        list_link = getResources().getStringArray(R.array.list_link);

        initListNews24h();
        initLeftMenu();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        cd = new ConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet()) {
//            Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setTitle("Notification");
            dialog.setMessage("Disconnected");
            dialog.show();
        }

        setTitle(list_title[0]);

        listAdv = Const.get_adv(context);
        if (listAdv == null) listAdv = new ArrayList<>();

        addFragment(new OneFragment(list_link[0], listAdv));
    }

    private ArrayList<String> listAdv;

    public void closeDraw() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
    }

    private Handler myHandler = new Handler();
    private Runnable myRunnable = new Runnable() {
        public void run() {
            mIsExit = false;
        }
    };
    boolean mIsExit = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!isTaskRoot()) {
                super.onBackPressed();
            } else {
                if (mIsExit) {
                    super.onBackPressed();
                } else {
                    // press 2 times to exit app feature
                    this.mIsExit = true;
                    String Str = "Press back again to quit.";
                    Toast.makeText(this, Str, Toast.LENGTH_SHORT).show();
                    myHandler.postDelayed(myRunnable, 2000);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        action_settings = menu.findItem(R.id.action_settings);
        if (!prefManager.isFirstTimeLaunch()) {
            action_settings.setChecked(false);
        } else {
            action_settings.setChecked(true);
        }
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
            if (action_settings.isChecked()) {
                action_settings.setChecked(false);
                prefManager.setFirstTimeLaunch(false);
            } else {
                action_settings.setChecked(true);
                prefManager.setFirstTimeLaunch(true);
            }
//            Intent intent = new Intent(MainActivity.this, Setting.class);
//            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void initListNews24h() {
        int n = list_link.length;
        for (int i = 0; i < n; i++) {
            News24h ob = new News24h(list_link[i], list_title[i]);
            listNews24h.add(ob);
        }
    }
}
