package np.com.naxa.simpledynamicforms.form.components;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import np.com.naxa.simpledynamicforms.R;
import np.com.naxa.simpledynamicforms.formhost.FormEntryActivity;



public class FormStartFragment extends Fragment implements FormEntryActivity.onFragmentVisibleListener, FormEntryActivity.fragmentStateListener {


    public FormStartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_start, container, false);

        return rootview;

    }

    @Override
    public void fragmentBecameVisible(int position) {

    }


    @Override
    public void fragmentStateChange(int state, int pos) {
        Timber.i(" State %s pos %s", state, pos);
    }
}
