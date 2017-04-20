package com.mottc.patrol.staff;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.mottc.patrol.Constant;
import com.mottc.patrol.R;
import com.mottc.patrol.utils.CommonUtils;
import com.mottc.patrol.utils.PermissionsUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_CANCELED;
import static com.mottc.patrol.R.id.upload;


public class PatrolFragment extends Fragment {


    @BindView(upload)
    Button mUpload;
    @BindView(R.id.help)
    Button mHelp;
    Unbinder unbinder;


    private String fileName;
    private static final int CAMERA_REQUEST_CODE = 1;
    public static final int REQUEST_CODE_ASK_CAMERA = 123;
    private String imagePath;



    public PatrolFragment() {
        // Required empty public constructor
    }

    public static PatrolFragment newInstance() {
        PatrolFragment fragment = new PatrolFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patrol, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    startCamera();
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "未授权使用相机", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @OnClick({upload, R.id.help})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case upload:
                uploadPhoto();
                break;
            case R.id.help:
                callForHelp();
                break;
        }
    }

    private void callForHelp() {
        EMClient.getInstance().contactManager().aysncGetAllContactsFromServer(new EMValueCallBack<List<String>>() {
            @Override
            public void onSuccess(final List<String> value) {
                if (value.size() != 0) {
                    String content = Constant.ASK_FOR_HELP;
                    EMMessage message = EMMessage.createTxtSendMessage(content, value.get(0));
                    EMClient.getInstance().chatManager().sendMessage(message);
                }

            }

            @Override
            public void onError(int error, String errorMsg) {

            }
        });

    }

    private void uploadPhoto() {
        PermissionsUtils.verifyStoragePermissions(getActivity());
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_CAMERA);
                return;
            } else {
                startCamera();
            }
        } else {
            startCamera();
        }
    }

    private void startCamera() {
        fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PatrolPic/upload/");
        if (!folder.exists()) {
            folder.mkdirs();//创建文件夹
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                CommonUtils.getUriForFile(getActivity(), new File(folder.getAbsolutePath(), fileName)));
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {

            switch (requestCode) {

                case CAMERA_REQUEST_CODE:
                    File picture = new File(
                            Environment.getExternalStorageDirectory().getPath() + "/PatrolPic/upload/" + fileName);

                    sendImage(picture.getAbsolutePath());
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendImage(String path) {

        imagePath = path;
        Log.i("PatrolFragment", "sendImage: " + imagePath);
        EMClient.getInstance().contactManager().aysncGetAllContactsFromServer(new EMValueCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> value) {
                if (value.size() != 0) {
                    EMMessage message = EMMessage.createImageSendMessage(imagePath, false, value.get(0));
                    EMClient.getInstance().chatManager().sendMessage(message);
                }

            }

            @Override
            public void onError(int error, String errorMsg) {

            }
        });

    }
}
