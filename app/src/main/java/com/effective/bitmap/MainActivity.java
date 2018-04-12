package com.effective.bitmap;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.effective.bitmap.utils.EffectiveBitmapUtils;
import com.effective.bitmap.utils.UriToPathUtils;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // https://github.com/bither/bither-android-lib/blob/master/REASON.md
    // 参考资料：https://blog.csdn.net/talkxin/article/details/50696511
    // http://www.cnblogs.com/MaxIE/p/3951294.html
    // https://github.com/bither/bither-android-lib
    // https://blog.csdn.net/carryWorld/article/details/75026171
    // https://www.jianshu.com/p/5f29fd671750
    // https://blog.csdn.net/fpcc/article/details/69942540

    public static final int REQUEST_CODE_IMAGE = 0;
    public static final int REQUEST_CODE_KITKAT_IMAGE = 1;
    public static final int REQUEST_CODE_PERMISSION = 2;

    TextView tv_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_path = findViewById(R.id.tv_path);
    }

    public void selectPhoto(View v) {

        int result = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        } else {
            pickPhoto();
        }

    }

    private void pickPhoto() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                    REQUEST_CODE_IMAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_KITKAT_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0) {
                pickPhoto();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_IMAGE:
                    if (data != null) {
                        Uri uri = data.getData();
                        compressImage(uri);
                    }
                    break;
                case REQUEST_CODE_KITKAT_IMAGE:
                    if (data != null) {
                        Uri uri = ensureUriPermission(this, data);
                        compressImage(uri);
                    }
                    break;
            }
        }
    }

    @SuppressWarnings("ResourceType")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Uri ensureUriPermission(Context context, Intent intent) {
        Uri uri = intent.getData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final int takeFlags = intent.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
            context.getContentResolver().takePersistableUriPermission(uri, takeFlags);
        }
        return uri;
    }

    public void compressImage(final Uri uri) {
        Toast.makeText(MainActivity.this, "start compress...", Toast.LENGTH_LONG).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                    File fromQualityFile = new File(getExternalCacheDir(), "FromQuality.jpg");
                    EffectiveBitmapUtils.compressByQuality(bitmap, fromQualityFile);

                    File fromSizeFile = new File(getExternalCacheDir(), "FromSize.jpg");
                    EffectiveBitmapUtils.compressBySize(bitmap, fromSizeFile);

                    File fromSample = new File(getExternalCacheDir(), "FromSample.jpg");
                    File f = new File(getPathByUri(getApplicationContext(), uri));
                    EffectiveBitmapUtils.compressBySample(f.getAbsolutePath(), fromSample);

                    File fromJniFile = new File(getExternalCacheDir(), "FromJNI.jpg");
                    EffectiveBitmapUtils.compressByJNI(bitmap, fromJniFile.getAbsolutePath(), true);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_path.setText(getExternalCacheDir().getAbsolutePath());
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String getPathByUri(Context context, Uri uri) {
        String path;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            path = UriToPathUtils.getPath(context, uri);
        } else {
            path = UriToPathUtils.getEncodedPath(context, uri);
        }
        return path;
    }

}
