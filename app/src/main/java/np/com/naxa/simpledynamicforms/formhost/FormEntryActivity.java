package np.com.naxa.simpledynamicforms.formhost;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import np.com.naxa.simpledynamicforms.R;
import np.com.naxa.simpledynamicforms.demo.JSONFormatter;
import np.com.naxa.simpledynamicforms.form.components.AutoCompleteTextFragment;
import np.com.naxa.simpledynamicforms.form.components.DateTimeFragment;
import np.com.naxa.simpledynamicforms.form.components.EditTextFragment;
import np.com.naxa.simpledynamicforms.form.components.FormEndFragment;
import np.com.naxa.simpledynamicforms.form.components.FormStartFragment;
import np.com.naxa.simpledynamicforms.form.components.LocationFragment;
import np.com.naxa.simpledynamicforms.form.components.MultiSelectSpinnerFragment;
import np.com.naxa.simpledynamicforms.form.components.NoteComponent;
import np.com.naxa.simpledynamicforms.form.components.PhotoFragment;
import np.com.naxa.simpledynamicforms.form.components.SpinnerFragment;
import np.com.naxa.simpledynamicforms.form.components.SpinnerWithOtherFragment;
import np.com.naxa.simpledynamicforms.form.listeners.fragmentStateListener;
import np.com.naxa.simpledynamicforms.form.listeners.onAnswerSelectedListener;
import np.com.naxa.simpledynamicforms.form.listeners.onFormFinishedListener;
import np.com.naxa.simpledynamicforms.form.listeners.onPageVisibleListener;
import np.com.naxa.simpledynamicforms.form.listeners.shouldAllowViewPagerSwipeListener;
import np.com.naxa.simpledynamicforms.model.Form;
import np.com.naxa.simpledynamicforms.savedform.QuestionAnswer;
import np.com.naxa.simpledynamicforms.savedform.QuestionFactory;
import np.com.naxa.simpledynamicforms.savedform.QuestionFactory.QuestionType;
import np.com.naxa.simpledynamicforms.savedform.SavedFormActivity;
import np.com.naxa.simpledynamicforms.uitils.DialogFactory;
import np.com.naxa.simpledynamicforms.uitils.SnackBarUtils;
import np.com.naxa.simpledynamicforms.uitils.TabLayoutUtils;
import np.com.naxa.simpledynamicforms.uitils.TimeUtils;
import np.com.naxa.simpledynamicforms.uitils.ToastUtils;
import timber.log.Timber;


import static np.com.naxa.simpledynamicforms.uitils.TimeUtils.DEFAULT_FORMAT;

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
        TabLayoutUtils.enableTabs(tabLayout, true);

        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    private void initVar() {
        jsonAnswerBuilder = new JSONAnswerBuilder();
        snackBarUtils = new SnackBarUtils(rootlayout);
        viewPager.addOnPageChangeListener(this);


    }


    private void initUI() {
        setupToolbar();
        setupTabLayout();
        //setupWizardForm();

        try {
            Form savedForm = (Form) getIntent().getBundleExtra("form").getSerializable("form");

            JSONObject savedFormJson = new JSONObject(savedForm.getFormJson());

            setupFormInViewpager(savedFormJson.getJSONArray("questionAnswers").toString());

        } catch (NullPointerException | JSONException e) {

            e.printStackTrace();

            setupRawJson();
            //setupDemoForm();

        }


    }

    private void setupDemoForm() {


        adapter.addFragment(new FormStartFragment(), "Start");

        EditTextFragment etfragOwnerName = new EditTextFragment();
        QuestionAnswer questionAnswer = QuestionFactory.getText(0, "Question", "Hint", "", InputType.TYPE_CLASS_TEXT, true);
        etfragOwnerName.prepareQuestionAndAnswer(questionAnswer);

        adapter.addFragment(etfragOwnerName, generateFragmentName());

        adapter.addFragment(new FormEndFragment(), "End of Form");


        viewPager.setAdapter(adapter);


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
                handleJSONForm(row, i);
            }

            ToastUtils.showLongSafe("Loading " + jsonArray.length() + " questions completed");
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtils.showLongSafe(e.toString());
        }


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
        String isRequiredRaw = "false";
        //todo pasrse hint from json
        String hint = "";
        Boolean isRequired = Boolean.parseBoolean(isRequiredRaw);
        String EmptyString = "";
        String answer = "";
        ArrayList<String> dropDownOptions = null;
        String answerInputType = "InputText";
        int answerInputId = InputType.TYPE_CLASS_TEXT;


        if (jsonObject.has("is_required")) {
            isRequired = Boolean.valueOf(jsonObject.getString("is_required"));
        }

        if (jsonObject.has("answer")) {
            answer = jsonObject.getString("answer");
        }


        switch (questionType) {
            case QuestionType.TEXT:




                if (jsonObject.has("answer_text_type")) {
                    answerInputType = jsonObject.getString("answer_text_type");

                }


                switch (answerInputType) {
                    case "InputText":
                        answerInputId = InputType.TYPE_CLASS_TEXT;
                        break;
                    case "InputNumber":

                        answerInputId = InputType.TYPE_CLASS_NUMBER;
                        break;
                }

                EditTextFragment etfragOwnerName = new EditTextFragment();
                QuestionAnswer questionAnswer = QuestionFactory.getText(pos, question, EmptyString, answer, answerInputId, isRequired);
                etfragOwnerName.prepareQuestionAndAnswer(questionAnswer);

                adapter.addFragment(etfragOwnerName, generateFragmentName());

                break;
            case QuestionType.DATETIME:

                DateTimeFragment dateTimeFragment = new DateTimeFragment();
                QuestionAnswer datetimeQuestion = QuestionFactory.getDateTime(pos, question, TimeUtils.getNowString(DEFAULT_FORMAT), isRequired);
                dateTimeFragment.prepareQuestionAndAnswer(datetimeQuestion);
                adapter.addFragment(dateTimeFragment, generateFragmentName());

                break;
            case "MultiSelectDropdown":


                if (jsonObject.has("drop_options")) {
                    String dropOptions = jsonObject.getString("drop_options");
                    Gson gson = new Gson();
                    dropDownOptions =
                            new ArrayList<>(Arrays.asList(
                                    gson.fromJson(dropOptions, String[].class)));
                }

                MultiSelectSpinnerFragment multiSelectionSpinner = new MultiSelectSpinnerFragment();
                multiSelectionSpinner.prepareQuestionAndAnswer(question, dropDownOptions, pos);
                adapter.addFragment(multiSelectionSpinner, generateFragmentName());

                break;
            case "Photo":

                PhotoFragment photoFragment = new PhotoFragment();
                photoFragment.prepareQuestionAndAnswer(question, pos);
                adapter.addFragment(photoFragment, generateFragmentName());

                break;
            case QuestionType.SINGLE_DROPDOWN:


                if (jsonObject.has("drop_options")) {
                    String dropOptions = jsonObject.getString("drop_options");
                    Gson gson = new Gson();
                    dropDownOptions =
                            new ArrayList<>(Arrays.asList(
                                    gson.fromJson(dropOptions, String[].class)));
                }

                QuestionAnswer singleDropdown = QuestionFactory.getSpinner(pos, question, EmptyString, dropDownOptions, isRequired);

                SpinnerFragment spinnerFragment = new SpinnerFragment();
                spinnerFragment.prepareQuestionAndAnswer(singleDropdown);

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
            case QuestionType.LOCATION:
                LocationFragment location = new LocationFragment();

                QuestionAnswer locationQuestion = QuestionFactory.getLocation(pos,question,answer,isRequired);
                location.prepareQuestionAndAnswer(locationQuestion);


                adapter.addFragment(location, generateFragmentName());

                break;
            case "Note":
                NoteComponent noteComponent = new NoteComponent();
                noteComponent.setNote(question);
                adapter.addFragment(noteComponent, "Note");

                //hide itself from form
                pos = pos - 1;

                break;

            case QuestionType.AUTO_COMPLETE_TEXT:

                if (jsonObject.has("drop_options")) {
                    String dropOptions = jsonObject.getString("drop_options");
                    Gson gson = new Gson();
                    dropDownOptions =
                            new ArrayList<>(Arrays.asList(
                                    gson.fromJson(dropOptions, String[].class)));

                }

                AutoCompleteTextFragment completeTextFragment = new AutoCompleteTextFragment();

                QuestionAnswer autocompletetext = QuestionFactory.getAutoCompleteText(pos,question,hint,answer,dropDownOptions,answerInputId,isRequired);
                completeTextFragment.prepareQuestionAndAnswer(autocompletetext);

                adapter.addFragment(completeTextFragment, generateFragmentName());

                break;

        }


    }

    private void setupWizardForm() {


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


    private void setupRawJson() {
        String form = readSingleForm(R.raw.debug_form);
        setupFormInViewpager(form);
    }

    private void setupFormInViewpager(String form) {
        adapter.addFragment(new FormStartFragment(), "Start");


        if (TextUtils.isEmpty(form)) {
            ToastUtils.showLongSafe(":( \n Failed to Load form");
        } else {

            try {
                formJsonArray = new JSONArray(form);

                loadForm(formJsonArray);
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }

        adapter.addFragment(new FormEndFragment(), "End of Form");
        viewPager.setAdapter(adapter);
    }


    private String readSingleForm(int rawId) {
        InputStream in = null;
        String form = null;
        try {
            Resources res = getResources();
            in = res.openRawResource(rawId);

            byte[] b = new byte[in.available()];
            in.read(b);
            form = new String(b);
            in.close();

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return form;
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

        Logger.json(jsonAnswerBuilder.finalizeAnswers());

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
    public void onAnswerSelected(QuestionAnswer questionAnswer) {

        jsonAnswerBuilder.addAnswer(questionAnswer);


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
    public void saveForm(String formName) {
        jsonToSend = jsonAnswerBuilder.getFinalizedForm(formName);


        Form form = Form.getInstance(formName, jsonToSend);
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
    public void onBackPressed() {
        DialogFactory.createActionDialog(this, "Close form?", "Your changes would be lost").setPositiveButton("Close form", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).setNegativeButton("No", null).show();
    }

    @Override
    public void stopViewpagerScroll(boolean stop) {


        this.shouldStopViewPagerSwipe = stop;
    }
}
