package droidia.com.heraldpdx.homescreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import timber.log.Timber;

/**
 * Created by mandeep on 25/6/16.
 */

public class HomeScreenPagerAdapter extends FragmentStatePagerAdapter{

    private final String TAB_TITLES[] = {"Saved", "Recent"};

    public HomeScreenPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment;

        Timber.e("position %d", position);
        switch (position){

            case 0:
                fragment = new SavedLocationsFragment();
                break;
            case 1:
                fragment = new RecentSearchesFragment();
                break;
            default:
                fragment =  new Fragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }

    @Override public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }
}
