package np.com.naxa.simpledynamicforms.form.components;

import butterknife.BindView;
import butterknife.ButterKnife;
import np.com.naxa.simpledynamicforms.R;
import np.com.naxa.simpledynamicforms.form.listeners.fragmentStateListener;
import np.com.naxa.simpledynamicforms.form.listeners.onPageVisibleListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created on 7/29/17
 * by nishon.tan@gmail.com
 */

public class NoteComponent extends Fragment  implements fragmentStateListener, onPageVisibleListener  {

    @BindView(R.id.tv_note)
    public TextView tvNote;

    private String displayedNote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_note, container, false);
        ButterKnife.bind(this, rootView);

        tvNote.setText(displayedNote);
        return rootView;
    }


    public void setNote(String displayedNote){
        this.displayedNote = displayedNote;
    }

    @Override
    public void fragmentStateChange(int state, int fragmentPositionInViewPager) {

    }

    @Override
    public void fragmentIsVisible() {

    }
}
