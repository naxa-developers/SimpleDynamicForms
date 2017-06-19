package np.com.naxa.simpledynamicforms.savedform;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import np.com.naxa.simpledynamicforms.R;
import np.com.naxa.simpledynamicforms.model.Form;

/**
 * Created by Nishon Tandukar on 19 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

public class SavedFormActivity extends AppCompatActivity {


    private Iterator<Form> myIterator;
    private List<Form> forms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_form);

        RecyclerView rvforms = (RecyclerView) findViewById(R.id.rvforms);

        //ArrayList<Form> forms = Lists.newArrayList(myIterator);


        forms = Form.findWithQuery(Form.class, "SELECT * FROM Form ORDER BY ID DESC", null);


        FormsAdapter adapter = new FormsAdapter(this, forms);
        rvforms.setAdapter(adapter);
        rvforms.setLayoutManager(new LinearLayoutManager(this));


    }
}
