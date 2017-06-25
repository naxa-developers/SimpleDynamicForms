package np.com.naxa.simpledynamicforms.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.naxa.simpledynamicforms.R;
import np.com.naxa.simpledynamicforms.model.Test;

/**
 * Created by Nishon Tandukar on 21 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.first);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnOne)
    public void onViewClicked() {


        Test test = new Test();
        test.setTest("1");

        Intent intent = new Intent(this,SecondActivity.class);
        intent.putExtra("form",test);
        startActivity(intent);

    }
}
