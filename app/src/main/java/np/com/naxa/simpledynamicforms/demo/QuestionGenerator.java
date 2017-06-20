package np.com.naxa.simpledynamicforms.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.naxa.simpledynamicforms.R;
import np.com.naxa.simpledynamicforms.formhost.FormEntryActivity;
import np.com.naxa.simpledynamicforms.uitils.ToastUtils;

/**
 * Created by Nishon Tandukar on 19 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

public class QuestionGenerator extends AppCompatActivity {


    JSONArray form;
    JSONObject question;


    @BindView(R.id.spinnerQuestionTypes)
    Spinner spinnerQuestionTypes;
    @BindView(R.id.etQuestion)
    EditText etQuestion;
    @BindView(R.id.btnAddQuestion)
    Button btnAddQuestion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generator_question);
        form = new JSONArray();
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnAddQuestion, R.id.btnFormGen})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnAddQuestion:

                if (etQuestion.getText().length() == 0) {
                    ToastUtils.showLongSafe("Questions cannot be empty");
                    return;
                }

                btnAddQuestion.setText(getString(R.string.btn_question_add,form.length()));

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("question", etQuestion.getText().toString());
                    jsonObject.put("question_type", spinnerQuestionTypes.getSelectedItem().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                form.put(jsonObject);

                break;
            case R.id.btnFormGen:

                Intent toFormEntry = new Intent(this, FormEntryActivity.class);
                toFormEntry.putExtra("form", form.toString());
                startActivity(toFormEntry);
                break;
        }
    }
}
