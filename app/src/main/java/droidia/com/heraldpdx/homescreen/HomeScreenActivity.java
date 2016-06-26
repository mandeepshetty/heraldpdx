package droidia.com.heraldpdx.homescreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import droidia.com.heraldpdx.R;
import droidia.com.heraldpdx.arrivalchecker.MainActivity;

public class HomeScreenActivity extends AppCompatActivity implements RecentSearchesFragment.OnFragmentInteractionListener {

    private HomeScreenPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();
//        pager.setPageTransformer(true, new DepthPageTransformer());
    }

    @BindView(R.id.pager) ViewPager pager;
    @BindView(R.id.tabs) TabLayout tabs;
    @BindView(R.id.fab) FloatingActionButton fab;
    private void initViews() {

        ButterKnife.bind(this);
        pagerAdapter = new HomeScreenPagerAdapter(getSupportFragmentManager());

        tabs.setupWithViewPager(pager);
        pager.setAdapter(pagerAdapter);

        fab.setOnClickListener(view -> startArrivalCheckerActivity());
    }

    private void startArrivalCheckerActivity() {

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
