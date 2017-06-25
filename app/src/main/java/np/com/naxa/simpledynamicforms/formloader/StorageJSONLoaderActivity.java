package np.com.naxa.simpledynamicforms.formloader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.naxa.simpledynamicforms.R;

import static np.com.naxa.simpledynamicforms.form.components.PhotoFragment.REQUEST_MULTIPLE_PERMISSION;

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

            if (hasPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_MULTIPLE_PERMISSION)){
                AsyncStorageLoader storageLoader = new AsyncStorageLoader(FileUtils.getFileByPath(src));
                storageLoader.execute();
            }


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean hasPermissions(String permission[], int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            ArrayList<String> notAvaliablePermList = new ArrayList<>();

            for (String currentPermission : permission) {
                int permissionCheck = ContextCompat.checkSelfPermission(this, currentPermission);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    notAvaliablePermList.add(currentPermission);
                }
            }
            if (notAvaliablePermList.size() >= 1) {
                requestPermissions(notAvaliablePermList.toArray(new String[notAvaliablePermList.size()]), requestCode);
                return false;

            }
        }
        return true;
    }
}
