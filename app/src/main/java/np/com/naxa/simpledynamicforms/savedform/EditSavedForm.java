package np.com.naxa.simpledynamicforms.savedform;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import np.com.naxa.simpledynamicforms.R;
import np.com.naxa.simpledynamicforms.model.Form;
import np.com.naxa.simpledynamicforms.uitils.ToastUtils;

/**
 * Created by Nishon Tandukar on 20 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

public class EditSavedForm extends AppCompatActivity {

    public static final String NAME = "NAME";
    public String mContact;

    @BindView(R.id.form_name_details)
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_save_form);
        ButterKnife.bind(this);
        mContact = getIntent().getStringExtra(NAME);
        textView.setText(mContact);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           // setupTransitionListener();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupTransitionListener() {
        getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {

            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();


    }
}
