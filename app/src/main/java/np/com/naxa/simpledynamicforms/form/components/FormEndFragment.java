package np.com.naxa.simpledynamicforms.form.components;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.naxa.simpledynamicforms.R;
import np.com.naxa.simpledynamicforms.form.listeners.fragmentStateListener;
import np.com.naxa.simpledynamicforms.form.listeners.onFormFinishedListener;
import np.com.naxa.simpledynamicforms.form.listeners.onPageVisibleListener;
import np.com.naxa.simpledynamicforms.model.Form;


public class FormEndFragment extends Fragment implements fragmentStateListener, onPageVisibleListener {

    private onFormFinishedListener listener;

    @BindView(R.id.fragment_form_end_wrapper_form_name)
    TextInputLayout textInputLayout;


    public FormEndFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_form_end, container, false);
        ButterKnife.bind(this, rootView);
        setTextInputLayoutListener();
        return rootView;

    }


    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onFormFinishedListener) {
            listener = (onFormFinishedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onFormFinishedListener");
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) return;
        if (activity instanceof onFormFinishedListener) {
            listener = (onFormFinishedListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement onFormFinishedListener");
        }
    }


    private void notifyFormHasEnded() {

        try {
            listener.uploadForm();
        } catch (ClassCastException cce) {

        }
    }


    @OnClick(R.id.fragment_end_btn_save_form)
    public void prepareToNotify() {
        String formName = textInputLayout.getEditText().getText().toString().trim();
        if (formName.length() == 0) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError("This field cannot be empty");
            textInputLayout.requestFocus();
            return;
        }

        Form form = new Form();
        form.setFormName(formName);

        try {
            listener.saveForm(form);
        } catch (ClassCastException cce) {

        }
    }

    private void setTextInputLayoutListener(){
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayout.setCounterEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void fragmentStateChange(int state, int pos) {
        //notifyFormHasEnded(pos);
    }

    @Override
    public void fragmentIsVisible() {

    }
}
