package cn.yaoht.onlinechat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import cn.yaoht.onlinechat.R;
import cn.yaoht.onlinechat.backend.CoreService;
import cn.yaoht.onlinechat.fragment.FriendFragment;
import cn.yaoht.onlinechat.fragment.SessionFragment;
import cn.yaoht.onlinechat.midware.ServerMidware;

public class MainActivity extends AppCompatActivity {

    static final int LOGIN_REQUEST = 1;
    private int[] imageResId = {
            R.drawable.ic_textsms_24dp,
            R.drawable.ic_quick_contacts_mail_24dp
    };
    private MenuItem loginMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(imageResId[i]);
        }

        ServerMidware serverMidware = ServerMidware.getInstance();
        if (serverMidware.getUsername() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST);
        } else {
            try {
                serverMidware.Login();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer, menu);
        loginMenu = menu.findItem(R.id.action_login);
        ServerMidware serverMidware = ServerMidware.getInstance();
        loginMenu.setIcon((serverMidware.getIs_online()) ? R.drawable.ic_person_24dp : R.drawable.ic_person_outline_24dp);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:
                ServerMidware serverMidware = ServerMidware.getInstance();
                serverMidware.setOnlineStateChangedListener(new ServerMidware.OnlineStateChangedListener() {
                    @Override
                    public void ChangeOnlineState(boolean is_online) {
                        loginMenu.setIcon(is_online ? R.drawable.ic_person_24dp : R.drawable.ic_person_outline_24dp);
                    }
                });

                if (serverMidware.getIs_online()) {
                    serverMidware.Logout();
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivityForResult(intent, LOGIN_REQUEST);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST && resultCode == RESULT_OK) {
            ServerMidware serverMidware = ServerMidware.getInstance();
            loginMenu.setIcon((serverMidware.getIs_online()) ? R.drawable.ic_person_24dp : R.drawable.ic_person_outline_24dp);
            if (serverMidware.getIs_online()) {
                CoreService.startActionListen(this);
            }
        }
    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;

        public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SessionFragment();
                case 1:
                    return new FriendFragment();
            }
            return null;
        }
    }
}
