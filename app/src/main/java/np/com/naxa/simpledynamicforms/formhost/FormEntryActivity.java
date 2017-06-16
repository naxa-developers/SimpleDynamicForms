package np.com.naxa.simpledynamicforms.formhost;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.buildchange.R;
import com.motherOfForms.fragments.EditTextFragment;
import com.motherOfForms.fragments.FormEndFragment;
import com.motherOfForms.fragments.FormStartFragment;
import com.motherOfForms.fragments.onAnswerSelectedListener;
import com.motherOfForms.fragments.onFormFinishedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FormEntryActivity extends AppCompatActivity implements onAnswerSelectedListener, onFormFinishedListener {

    Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public final String TAG = this.getClass().getSimpleName();
    private ViewPagerAdapter adapter;
    public String jsonToSend;
    private JSONObject header;
    private int fragmentCount = 1;

    private final int SCROLL_EVENT_START = 1;
    private final int SCROLL_EVENT_ONGOING = 2;
    private final int SCROLL_EVENT_END = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_entry);
        startFormFilling();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("मेरो निर्माण");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        //fix for strange viewpager glitch
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public int fragmentPositionInViewPager;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int fragmentPositionInViewPager) {

                this.fragmentPositionInViewPager = fragmentPositionInViewPager;

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                notifyScrollOngoingToFrag(state, fragmentPositionInViewPager);

            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });


    }

    private void notifyScrollOngoingToFrag(int state, int fragmentPositionInViewPager) {
        switch (state) {
            case SCROLL_EVENT_START:

                fragmentStateListener fragmentStateListener = (fragmentStateListener) adapter.instantiateItem(viewPager, fragmentPositionInViewPager);
                if (fragmentStateListener != null) {
                    fragmentStateListener.fragmentStateChange(state, fragmentPositionInViewPager);
                }

                break;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FormStartFragment(), " ");

        EditTextFragment etfragOwnerName = new EditTextFragment();
        etfragOwnerName.prepareQuestionAndAnswer("घरधनीको पूरा नाम ", " ", 1);
        adapter.addFragment(etfragOwnerName, generateFragmentName());

        EditTextFragment etfragContactNumber = new EditTextFragment();
        etfragContactNumber.prepareQuestionAndAnswer("जिल्ला ", " ", 2);
        adapter.addFragment(etfragContactNumber, generateFragmentName());

        EditTextFragment etFragVDCorMun = new EditTextFragment();
        etFragVDCorMun.prepareQuestionAndAnswer("नगरपालिका / गाउँपालिका  ", " ", 3);
        adapter.addFragment(etFragVDCorMun, generateFragmentName());


        adapter.addFragment(new FormEndFragment(), "");
        viewPager.setAdapter(adapter);

    }

    private String generateFragmentName() {
        String fragmentName = "प्रशन नम्बर " + fragmentCount;
        fragmentCount = fragmentCount + 1;
        return fragmentName;

    }

    @Override
    public void onAnswerSelected(String question, String answer) {
        addAnswerToJSON(question, answer);
    }

    @Override
    public void uploadForm() {
        //todo call upload method here
        finalizeAnswers();
        Log.i(TAG, jsonToSend);
        Toast.makeText(this, jsonToSend, Toast.LENGTH_LONG).show();
    }

    @Override
    public void saveForm() {
        //todo imlement listenrs for save
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    public interface onFragmentVisibleListener {
        void fragmentBecameVisible(int position);
    }

    public interface fragmentStateListener {
        void fragmentStateChange(int state, int pos);
    }


    public void startFormFilling() {
        header = new JSONObject();
    }

    public void addAnswerToJSON(String questionId, String answer) {
        try {
            if (header.has(questionId)) {
                header.remove(questionId);
            }
            header.put(questionId, answer);
        } catch (JSONException e) {
            Log.e(TAG, "Error - While Adding Answer to JSON \n" + e);

            e.printStackTrace();
        }
    }

    public void finalizeAnswers() {
        try {
            header.put("token", "534545sDfkjHuy589io8gj983jtdfkjj&ihs@->89<-ioj389OiJijor9834%67");
        } catch (JSONException e) {
            Log.e(TAG, "Error - While Adding Token to JSON \n" + e);
            e.printStackTrace();
        }
        jsonToSend = header.toString();

    }

    public void nextFragment(View view) {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    public void prevFragment(View view) {
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }
}
