package np.com.naxa.simpledynamicforms.formloader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.naxa.simpledynamicforms.R;
import np.com.naxa.simpledynamicforms.uitils.ToastUtils;

/**
 * Created by Nishon Tandukar on 25 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

public class StorageJSONLoaderActivity extends AppCompatActivity {

    private static final int PICKFILE_REQUEST_CODE = 301;

    @BindView(R.id.activity_loader_json_storage_btn)
    Button activityLoaderJsonStorageBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader_json_storage);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.activity_loader_json_storage_btn)
    public void openFileExplorer() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Open Form");
        startActivityForResult(chooseFile, PICKFILE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String src = uri.getPath();
            ToastUtils.showLongSafe(uri.toString() + " " + src);
        }
    }
}
