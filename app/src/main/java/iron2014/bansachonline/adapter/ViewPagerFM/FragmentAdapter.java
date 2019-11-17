package iron2014.bansachonline.adapter.ViewPagerFM;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {
        private List<androidx.fragment.app.Fragment> Fragment = new ArrayList<>(); //Fragment List
        private List<String> NamePage = new ArrayList<>(); // Fragment Name List

        public FragmentAdapter(FragmentManager manager) {
            super(manager);
        }

        public void add(androidx.fragment.app.Fragment Frag, String Title) {
            Fragment.add(Frag);
            NamePage.add(Title);
        }

        @Override
        public androidx.fragment.app.Fragment getItem(int position) {
            return Fragment.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return NamePage.get(position);
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
