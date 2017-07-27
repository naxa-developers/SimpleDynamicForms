package np.com.naxa.simpledynamicforms.formhost;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import np.com.naxa.simpledynamicforms.R;
import np.com.naxa.simpledynamicforms.demo.JSONFormatter;
import np.com.naxa.simpledynamicforms.form.components.DateTimeFragment;
import np.com.naxa.simpledynamicforms.form.components.EditTextFragment;
import np.com.naxa.simpledynamicforms.form.components.FormEndFragment;
import np.com.naxa.simpledynamicforms.form.components.FormStartFragment;
import np.com.naxa.simpledynamicforms.form.components.LocationFragment;
import np.com.naxa.simpledynamicforms.form.components.MultiSelectSpinnerFragment;
import np.com.naxa.simpledynamicforms.form.components.PhotoFragment;
import np.com.naxa.simpledynamicforms.form.components.SpinnerFragment;
import np.com.naxa.simpledynamicforms.form.components.SpinnerWithOtherFragment;
import np.com.naxa.simpledynamicforms.form.listeners.fragmentStateListener;
import np.com.naxa.simpledynamicforms.form.listeners.onAnswerSelectedListener;
import np.com.naxa.simpledynamicforms.form.listeners.onFormFinishedListener;
import np.com.naxa.simpledynamicforms.form.listeners.onPageVisibleListener;
import np.com.naxa.simpledynamicforms.form.listeners.shouldAllowViewPagerSwipeListener;
import np.com.naxa.simpledynamicforms.model.Form;
import np.com.naxa.simpledynamicforms.savedform.SavedFormActivity;
import np.com.naxa.simpledynamicforms.uitils.DialogFactory;
import np.com.naxa.simpledynamicforms.uitils.SnackBarUtils;
import np.com.naxa.simpledynamicforms.uitils.ToastUtils;
import timber.log.Timber;

public class FormEntryActivity extends AppCompatActivity implements onAnswerSelectedListener, onFormFinishedListener, ViewPager.OnPageChangeListener, shouldAllowViewPagerSwipeListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    NonSwipeableViewPager viewPager;

    @BindView(R.id.root_layout_activity_form_entry)
    CoordinatorLayout rootlayout;


    @BindView(R.id.act_form_entry_linear_layout_btn)
    LinearLayout btnLayout;

    private ViewPagerAdapter adapter;
    public String jsonToSend;
    private JSONObject header;
    private int fragmentCount = 1;
    private int fragmentPositionInViewPager;
    private JSONAnswerBuilder jsonAnswerBuilder;
    private SnackBarUtils snackBarUtils;


    float mLastPositionOffset = 0f;
    private boolean shouldStopViewPagerSwipe;
    private JSONArray formJsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_entry);
        ButterKnife.bind(this);
        initUI();
        initVar();
    }

    private void initVar() {
        jsonAnswerBuilder = new JSONAnswerBuilder();
        snackBarUtils = new SnackBarUtils(rootlayout);
        viewPager.addOnPageChangeListener(this);


    }

    private void initUI() {
        setupToolbar();
        setupTabLayout();
        setupDemoForm();
    }

    private void setupTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("मेरो निर्माण");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void loadForm(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject row = jsonArray.getJSONObject(i);
                handleJSONForm(row, i + 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ToastUtils.showLongSafe("Loading " + jsonArray.length() + " questions completed");

    }

    //todo needs to be removed
    private JSONArray getForm() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("question", "Hello world");
        jsonObject.put("question_type", "text");
        jsonArray.put(jsonObject);

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("question", "Hello world2");
        jsonObject2.put("question_type", "text");
        jsonArray.put(jsonObject2);
        return jsonArray;

    }


    private void handleJSONForm(JSONObject jsonObject, int pos) throws JSONException {


        String questionType = jsonObject.getString("question_type");
        String question = jsonObject.getString("question");
        switch (questionType) {
            case "Text":
                EditTextFragment etfragOwnerName = new EditTextFragment();
                etfragOwnerName.prepareQuestionAndAnswer(question, question, InputType.TYPE_CLASS_TEXT, true, pos);
                adapter.addFragment(etfragOwnerName, generateFragmentName());
                break;
            case "Date Time":

                DateTimeFragment dateTimeFragment = new DateTimeFragment();
                dateTimeFragment.prepareQuestionAndAnswer(question, pos);
                adapter.addFragment(dateTimeFragment, generateFragmentName());

                break;
            case "MultiSelect Dropdown":

                ArrayList<String> songs = new ArrayList<>();
                songs.add("Yellow - Coldplay");
                songs.add("Pani Paryo - Rohit");
                songs.add("Jhilimili - Rohit");
                songs.add("Muskuraye - Astha Tamang Maskey");

                MultiSelectSpinnerFragment multiSelectionSpinner = new MultiSelectSpinnerFragment();
                multiSelectionSpinner.prepareQuestionAndAnswer(question, songs, pos);
                adapter.addFragment(multiSelectionSpinner, generateFragmentName());

                break;
            case "Photo":

                PhotoFragment photoFragment = new PhotoFragment();
                photoFragment.prepareQuestionAndAnswer(question, pos);
                adapter.addFragment(photoFragment, generateFragmentName());

                break;
            case "DropDown":
                ArrayList<String> options = new ArrayList<>();
                options.add("Yes");
                options.add("No");

                SpinnerFragment spinnerFragment = new SpinnerFragment();
                spinnerFragment.prepareQuestionAndAnswer(question, options, pos);
                adapter.addFragment(spinnerFragment, generateFragmentName());


                break;
            case "DropDown With Other":
                ArrayList<String> options2 = new ArrayList<>();
                options2.add("Yes");
                options2.add("No");

                SpinnerWithOtherFragment spinnerWithOtherFragment = new SpinnerWithOtherFragment();
                spinnerWithOtherFragment.prepareQuestionAndAnswer(question, options2, pos);
                adapter.addFragment(spinnerWithOtherFragment, generateFragmentName());

                break;
        }


    }

    private void setupDemoForm() {

        adapter.addFragment(new FormStartFragment(), "Start");


        if (getIntent().getStringExtra("form") != null && getIntent().getStringExtra("form").isEmpty()) {
            ToastUtils.showLongSafe(":(");
        } else {

            try {
                formJsonArray = new JSONArray(getIntent().getStringExtra("form"));

                loadForm(formJsonArray);
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }


        adapter.addFragment(new FormEndFragment(), "End of Form");
        viewPager.setAdapter(adapter);

    }

    private void setupForm() {

        adapter.addFragment(new FormStartFragment(), "Start");

        EditTextFragment etfragOwnerName = new EditTextFragment();
        etfragOwnerName.prepareQuestionAndAnswer("Surveyor Identify Number", "", InputType.TYPE_CLASS_TEXT, true, 1);
        adapter.addFragment(etfragOwnerName, generateFragmentName());

        EditTextFragment two = new EditTextFragment();
        etfragOwnerName.prepareQuestionAndAnswer("Name of Surveyor", "", InputType.TYPE_CLASS_TEXT, true, 2);
        adapter.addFragment(two, generateFragmentName());

        DateTimeFragment three = new DateTimeFragment();
        three.prepareQuestionAndAnswer("Date of Survey",3);
        adapter.addFragment(three, generateFragmentName());

        EditTextFragment four = new EditTextFragment();
        etfragOwnerName.prepareQuestionAndAnswer("Household Identify Code", "", InputType.TYPE_CLASS_NUMBER, true, 3);
        adapter.addFragment(four, generateFragmentName());

        EditTextFragment five = new EditTextFragment();
        etfragOwnerName.prepareQuestionAndAnswer("Municipality", "", InputType.TYPE_CLASS_NUMBER, true, 4);
        adapter.addFragment(five, generateFragmentName());

        EditTextFragment six = new EditTextFragment();
        etfragOwnerName.prepareQuestionAndAnswer("Ward Number", "", InputType.TYPE_CLASS_NUMBER, true, 5);
        adapter.addFragment(six, generateFragmentName());

        EditTextFragment seven = new EditTextFragment();
        etfragOwnerName.prepareQuestionAndAnswer("Address/Line", "", InputType.TYPE_CLASS_TEXT, true, 5);
        adapter.addFragment(seven, generateFragmentName());

        LocationFragment locationFragment = new LocationFragment();
        locationFragment.prepareQuestionAndAnswer("GPS Location", 7);
        adapter.addFragment(locationFragment, generateFragmentName());


        PhotoFragment eight = new PhotoFragment();
        eight.prepareQuestionAndAnswer("House Facade Photo", 8);
        adapter.addFragment(eight, generateFragmentName());

        ArrayList<String> options = new ArrayList<>();
        options.add("Male");
        options.add("Female");

        SpinnerFragment nine = new SpinnerFragment();
        nine.prepareQuestionAndAnswer("Respondent Sex", options, 9);
        adapter.addFragment(nine, generateFragmentName());

        EditTextFragment ten = new EditTextFragment();
        ten.prepareQuestionAndAnswer("Respondent Age", "", InputType.TYPE_CLASS_NUMBER, true, 10);
        adapter.addFragment(ten, generateFragmentName());

        EditTextFragment eleven = new EditTextFragment();
        eleven.prepareQuestionAndAnswer("Email", "", InputType.TYPE_CLASS_TEXT, true, 11);
        adapter.addFragment(eleven, generateFragmentName());

        EditTextFragment tweleve = new EditTextFragment();
        tweleve.prepareQuestionAndAnswer("What is the number of Inhabitants/Family Members in the Household?", "", InputType.TYPE_CLASS_TEXT, true, 11);
        adapter.addFragment(tweleve, generateFragmentName());



//        EditTextFragment seven = new EditTextFragment();
//        etfragOwnerName.prepareQuestionAndAnswer("Address/Line", "", InputType.TYPE_CLASS_TEXT, true, 5);
//        adapter.addFragment(seven, generateFragmentName());
//
//        EditTextFragment etfragContactNumber = new EditTextFragment();
//        etfragContactNumber.prepareQuestionAndAnswer("Age", "Enter your age ", InputType.TYPE_CLASS_NUMBER, true, 2);
//        etfragContactNumber.shouldStopSwipe();
//        adapter.addFragment(etfragContactNumber, generateFragmentName());
//
//        ArrayList<String> options = new ArrayList<>();
//        options.add("Yes");
//        options.add("No");
//
//        SpinnerFragment spinnerFragment = new SpinnerFragment();
//        spinnerFragment.prepareQuestionAndAnswer("Do you like dancing?", options, 3);
//        adapter.addFragment(spinnerFragment, generateFragmentName());
//
//        ArrayList<String> songs = new ArrayList<>();
//        songs.add("Yellow - Coldplay");
//        songs.add("Pani Paryo - Rohit");
//        songs.add("Jhilimili - Rohit");
//        songs.add("Muskuraye - Astha Tamang Maskey");
//
//        MultiSelectSpinnerFragment multiSelectionSpinner = new MultiSelectSpinnerFragment();
//        multiSelectionSpinner.prepareQuestionAndAnswer("Select at least two songs?", songs, 4);
//        adapter.addFragment(multiSelectionSpinner, generateFragmentName());
//
//        PhotoFragment photoFragment = new PhotoFragment();
//        photoFragment.prepareQuestionAndAnswer("Take a photo", 5);
//        adapter.addFragment(photoFragment, generateFragmentName());
//
//
//
//        SpinnerWithOtherFragment spinnerWithOtherFragment = new SpinnerWithOtherFragment();
//        spinnerWithOtherFragment.prepareQuestionAndAnswer("Choose other from drop down", options, 7);
//        adapter.addFragment(spinnerWithOtherFragment, generateFragmentName());
//
//
//        LocationFragment locationFragment = new LocationFragment();
//        locationFragment.prepareQuestionAndAnswer("GPS", 8);
//        adapter.addFragment(locationFragment, generateFragmentName());

        adapter.addFragment(new FormEndFragment(), "End of Form");
        viewPager.setAdapter(adapter);

    }

    private String generateFragmentName() {
        String fragmentName = "Q.no." + fragmentCount;
        fragmentCount = fragmentCount + 1;
        return fragmentName;
    }

    public void nextFragment(View view) {
        fakeScroll();
        if (shouldStopViewPagerSwipe) {
            return;
        }
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    public void prevFragment(View view) {
        fakeScroll();
        if (shouldStopViewPagerSwipe) {
            return;
        }

        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }

    private void fakeScroll() {
        fragmentStateListener fragStateListener = (fragmentStateListener) adapter.instantiateItem(viewPager, fragmentPositionInViewPager);
        if (fragStateListener != null) {
            fragStateListener.fragmentStateChange(DataRepo.VIEW_PAGER_SCROLL_EVENT_START, fragmentPositionInViewPager);
        }
    }

    @Override
    public void onAnswerSelected(String question, String answer) {
        jsonAnswerBuilder.addAnswerToJSON(question, answer);

    }

    @Override
    public void shoudStopSwipe(boolean shoudStopSwipe) {

        Timber.i(" Should Stop Swipe %s", shoudStopSwipe);
        if (shoudStopSwipe) {
            shouldStopViewPagerSwipe = true;
            viewPager.shoudStopSwipe(true);
        } else {
            shouldStopViewPagerSwipe = false;
            viewPager.shoudStopSwipe(false);
        }

    }


    @Override
    public void uploadForm() {
        jsonToSend = jsonAnswerBuilder.finalizeAnswers();
        String formatedJSON = JSONFormatter.formatString(jsonToSend);
        DialogFactory.createMessageDialog(this, "Answers formatted in JSON", formatedJSON);
    }

    @Override
    public void saveForm(Form form) {
        jsonToSend = jsonAnswerBuilder.finalizeAnswers();
        form.setFormJson(jsonToSend);
        SugarRecord.save(form);
        DialogFactory.createActionDialog(this, "Save Successful", "Your form has been save successfully").setPositiveButton("New Form", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(getApplicationContext(), FormEntryActivity.class);
                intent.putExtra("form", formJsonArray.toString());
                startActivity(intent);

            }
        }).setNegativeButton("View Saved Form", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(FormEntryActivity.this, SavedFormActivity.class);
                startActivity(intent);
                finish();

            }
        }).create().show();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //makeViewPagerSnappy(position, positionOffset, positionOffsetPixels);
    }

    private void makeViewPagerSnappy(int position, float positionOffset, int positionOffsetPixels) {

        if (positionOffset < mLastPositionOffset && positionOffset < 0.9) {
            viewPager.setCurrentItem(position);
        } else if (positionOffset > mLastPositionOffset && positionOffset > 0.1) {
            viewPager.setCurrentItem(position + 1);
        }
        mLastPositionOffset = positionOffset;
    }

    @Override
    public void onPageSelected(int fragmentPositionInViewPager) {
        this.fragmentPositionInViewPager = fragmentPositionInViewPager;
        hideBtnAtEndFrag();

        onPageVisibleListener pageVisibleListener = (onPageVisibleListener) adapter.instantiateItem(viewPager, fragmentPositionInViewPager);
        if (pageVisibleListener != null) {
            pageVisibleListener.fragmentIsVisible();
        }


    }

    private void hideBtnAtEndFrag() {
        if (fragmentPositionInViewPager == viewPager.getAdapter().getCount() - 1) {
            btnLayout.setVisibility(View.GONE);
        } else {
            btnLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

        Timber.d("onPageScrollStateChanged %s", state);

        notifyScrollOngoingToFrag(state, fragmentPositionInViewPager);
    }

    private void notifyScrollOngoingToFrag(int state, int fragmentPositionInViewPager) {
        switch (state) {
            case DataRepo.VIEW_PAGER_SCROLL_EVENT_START:

                fragmentStateListener fragStateListener = (fragmentStateListener) adapter.instantiateItem(viewPager, fragmentPositionInViewPager);
                if (fragStateListener != null) {
                    fragStateListener.fragmentStateChange(state, fragmentPositionInViewPager);
                }

                break;
        }
    }


    @Override
    public void stopViewpagerScroll(boolean stop) {


        this.shouldStopViewPagerSwipe = stop;
    }
}
