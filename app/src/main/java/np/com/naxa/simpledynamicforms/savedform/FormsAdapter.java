package np.com.naxa.simpledynamicforms.savedform;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import np.com.naxa.simpledynamicforms.R;
import np.com.naxa.simpledynamicforms.demo.JSONFormatter;
import np.com.naxa.simpledynamicforms.formhost.FormEntryActivity;
import np.com.naxa.simpledynamicforms.model.Form;
import np.com.naxa.simpledynamicforms.uitils.DialogFactory;

/**
 * Created by Nishon Tandukar on 19 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

public class FormsAdapter extends
        RecyclerView.Adapter<FormsAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public Button btnEditForm, btnReviewForm;
        private final TextView tvDateTime;

        public ViewHolder(final View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.form_name);
            btnEditForm = (Button) itemView.findViewById(R.id.edit_button);
            btnReviewForm = (Button) itemView.findViewById(R.id.review_form);
            tvDateTime = (TextView) itemView.findViewById(R.id.date_time);
            // Setup the click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });

        }
    }


    private List<Form> forms;
    private Context mContext;

    public FormsAdapter(Context context, List<Form> forms) {
        this.forms = forms;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }


    @Override
    public FormsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_form, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FormsAdapter.ViewHolder viewHolder, final int position) {
        final Form form = forms.get(position);

        TextView textView = viewHolder.nameTextView;
        textView.setText(form.getFormName());

        TextView tvDateTime = viewHolder.tvDateTime;
        tvDateTime.setText(form.getFilledDateTime());

        Button btnEditForm = viewHolder.btnEditForm;
        btnEditForm.setText("Edit");

        Button btnReviewForm = viewHolder.btnReviewForm;
        btnReviewForm.setText("Review");

        btnEditForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("form",form);
                Intent toFormEntry = new Intent(getContext(), FormEntryActivity.class);
                toFormEntry.putExtra("form",bundle);
                getContext().startActivity(toFormEntry);
            }
        });

        btnReviewForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFactory.createSimpleOkErrorDialog(getContext(), forms.get(position).getFormName(), JSONFormatter.formatQuestionAnswer(forms.get(position).getFormJson())).show();
            }
        });


    }


    @Override
    public int getItemCount() {
        return forms.size();
    }


    // Define listener member variable
    private OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
