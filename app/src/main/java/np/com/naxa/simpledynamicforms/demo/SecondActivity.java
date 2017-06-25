package np.com.naxa.simpledynamicforms.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import np.com.naxa.simpledynamicforms.R;
import np.com.naxa.simpledynamicforms.model.Test;
import np.com.naxa.simpledynamicforms.uitils.ToastUtils;

/**
 * Created by Nishon Tandukar on 21 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.second);
        ButterKnife.bind(this);

        Intent i = getIntent();
        Test test = (Test) i.getSerializableExtra("form");

        ToastUtils.showLongSafe(test.getTest());
    }
}

