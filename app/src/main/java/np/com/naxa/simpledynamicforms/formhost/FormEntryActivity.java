package np.com.naxa.simpledynamicforms.formhost;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import np.com.naxa.simpledynamicforms.R;
import np.com.naxa.simpledynamicforms.form.DataRepo;
import np.com.naxa.simpledynamicforms.form.JSONAnswerBuilder;
import np.com.naxa.simpledynamicforms.form.ViewPagerAdapter;
import np.com.naxa.simpledynamicforms.form.components.EditTextFragment;
import np.com.naxa.simpledynamicforms.form.components.FormEndFragment;
import np.com.naxa.simpledynamicforms.form.components.FormStartFragment;
import np.com.naxa.simpledynamicforms.form.listeners.fragmentStateListener;
import np.com.naxa.simpledynamicforms.form.listeners.onAnswerSelectedListener;
import np.com.naxa.simpledynamicforms.form.listeners.onFormFinishedListener;

public class FormEntryActivity extends AppCompatActivity implements onAnswerSelectedListener, onFormFinishedListener, ViewPager.OnPageChangeListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;


    private ViewPagerAdapter adapter;
    public String jsonToSend;
    private JSONObject header;
    private int fragmentCount = 1;
    private int fragmentPositionInViewPager;
    private JSONAnswerBuilder jsonAnswerBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_entry);
        ButterKnife.bind(this);
        initUI();

        jsonAnswerBuilder = new JSONAnswerBuilder();
    }

    private void initUI() {
        setupToolbar();
        setupTabLayout();
        setupViewPager(viewPager);
    }

    private void setupTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("मेरो निर्माण");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        jsonAnswerBuilder.addAnswerToJSON(question, answer);
    }

    @Override
    public void uploadForm() {

        jsonToSend = jsonAnswerBuilder.finalizeAnswers();
        Toast.makeText(this, jsonToSend, Toast.LENGTH_LONG).show();
    }

    @Override
    public void saveForm() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //we do nothing important here.
    }

    @Override
    public void onPageSelected(int fragmentPositionInViewPager) {
        this.fragmentPositionInViewPager = fragmentPositionInViewPager;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        notifyScrollOngoingToFrag(state, fragmentPositionInViewPager);
    }

    private void notifyScrollOngoingToFrag(int state, int fragmentPositionInViewPager) {
        switch (state) {
            case DataRepo.VIEW_PAGER_SCROLL_EVENT_START:

                fragmentStateListener fragmentStateListener = (fragmentStateListener) adapter.instantiateItem(viewPager, fragmentPositionInViewPager);
                if (fragmentStateListener != null) {
                    fragmentStateListener.fragmentStateChange(state, fragmentPositionInViewPager);
                }

                break;
        }
    }


    public void nextFragment(View view) {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    public void prevFragment(View view) {
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }
}
