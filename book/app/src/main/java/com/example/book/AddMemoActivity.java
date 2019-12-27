package com.example.book;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.soundcloud.android.crop.Crop;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class AddMemoActivity extends Activity {
    private EditText memoContent;
    private Button memoBtnSave;
    private Button memoBtnCamera;
    private Button memoBtnAlbum;
    private Button memoBtnOCR;
    private Button memoBtnDraw;
    private TextView bookTitle;

    private byte[] mOcrImage;
    private String mOcrText;

    public String mContent;

    public byte[] bCover;
    public String bScore;
    public String bTitle;
    public String bWriter;
    public String bPublisher;
    public String bStartDate;
    public String bFinishDate;
    public String bComment;

    private Boolean isPermission = true;

    private static final String TAG = "AddMemoActivity";

    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;

    private File tempFile;

    private Boolean isCamera = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_memo);

        // 화면 클릭 시 키보드 hidden
//        LinearLayout add_edit_memo = findViewById(R.id.add_edit_memo);
//
//        add_edit_memo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(memoContent.getWindowToken(), 0);
//            }
//        });

        // 책 정보 가져오기
        bCover = getIntent().getByteArrayExtra("bCover");
        bScore = getIntent().getStringExtra("bScore");
        bTitle = getIntent().getStringExtra("bTitle");
        bWriter = getIntent().getStringExtra("bWriter");
        bPublisher = getIntent().getStringExtra("bPublisher");
        bStartDate = getIntent().getStringExtra("bStartDate");
        bFinishDate = getIntent().getStringExtra("bFinishDate");
        bComment = getIntent().getStringExtra("bComment");
        Log.d(TAG, "getIntent().getStringExtra('bTitle')" + bTitle + "=======================================================================================");

        mOcrText = getIntent().getStringExtra("mOcrText");

        memoContent = findViewById(R.id.memoContent);
        memoBtnSave = findViewById(R.id.btnSaveMemo);
        memoBtnCamera = findViewById(R.id.btnCamera);
        memoBtnAlbum = findViewById(R.id.btnAlbum);
        memoBtnOCR = findViewById(R.id.btnOCR);
        memoBtnDraw = findViewById(R.id.btnDraw);
        bookTitle = findViewById(R.id.bookTitle);

        memoContent.setText(mOcrText);

        bookTitle.setText(bTitle);

        memoBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                tedPermission();
//
//                if (isPermission) takePhoto();
//                else Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
            }
        });

        memoBtnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                tedPermission();
//
//                if (isPermission) goToAlbum();
//                else Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
            }
        });

        memoBtnOCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tedPermission();

                if (isPermission) {
                    goToAlbum();
                }
                else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
                }
            }
        });

        memoBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContent = memoContent.getText().toString();

                MemoDBHelper memoDbHelper = new MemoDBHelper(getApplicationContext(), "memo", null, 1);
                memoDbHelper.mInsert(mContent, bTitle);

                Intent intent = new Intent(getApplicationContext(), BookContent.class);

                intent.putExtra("bCover", bCover);
                intent.putExtra("bScore", bScore);
                intent.putExtra("bTitle", bTitle);
                intent.putExtra("bWriter", bWriter);
                intent.putExtra("bPublisher", bPublisher);
                intent.putExtra("bStartDate", bStartDate);
                intent.putExtra("bFinishDate", bFinishDate);
                intent.putExtra("bComment", bComment);

                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if(tempFile != null) {
                if (tempFile.exists()) {

                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }

            return;
        }

        switch (requestCode) {
            case PICK_FROM_ALBUM: {

                Uri photoUri = data.getData();
                Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);

                cropImage(photoUri);

                break;
            }
            case PICK_FROM_CAMERA: {

                Uri photoUri = Uri.fromFile(tempFile);
                Log.d(TAG, "takePhoto photoUri : " + photoUri);

                cropImage(photoUri);

                break;
            }
            case Crop.REQUEST_CROP: {
                //File cropFile = new File(Crop.getOutput(data).getPath());
                setImage();

                Intent intent = new Intent(AddMemoActivity.this, OCRActivity.class);

                intent.putExtra("mOcrImage", mOcrImage);

                intent.putExtra("bCover", bCover);
                intent.putExtra("bScore", bScore);
                intent.putExtra("bTitle", bTitle);
                intent.putExtra("bWriter", bWriter);
                intent.putExtra("bPublisher", bPublisher);
                intent.putExtra("bStartDate", bStartDate);
                intent.putExtra("bFinishDate", bFinishDate);
                intent.putExtra("bComment", bComment);

                startActivity(intent);

                Log.d(TAG, "onActivityResult: 197 startActivity(intent) ################################################################################");
            }
        }
    }


    // 이미지 crop
    private void cropImage(Uri photoUri) {

        Log.d(TAG, "cropImage tempFile : " + tempFile);

        /**
         *  갤러리에서 선택한 경우에는 tempFile 이 없으므로 새로 생성해야함
         */
        if (tempFile == null) {
            try {
                tempFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                finish();
                e.printStackTrace();
            }
        }

        //크롭 후 저장할 Uri
        Uri savingUri = Uri.fromFile(tempFile);

        Crop.of(photoUri, savingUri).start(this);
//        Crop.of(photoUri, savingUri).asSquare().start(this);
    }


    /**
     * 앨범에서 이미지 가져오기
     */
    private void goToAlbum() {
        isCamera = false;

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }


    /**
     * 카메라에서 이미지 가져오기
     */
    private void takePhoto() {
        isCamera = true;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
        if (tempFile != null) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                Uri photoUri = FileProvider.getUriForFile(this,
                        "book.provider", tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            } else {

                Uri photoUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);
            }
        }
    }

    /**
     * 폴더 및 파일 만들기
     */
    private File createImageFile() throws IOException {

        // 이미지 파일 이름 ( book_{시간}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "book_" + timeStamp + "_";

        // 이미지가 저장될 폴더 이름 ( book )
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/book/");
        if (!storageDir.exists()) storageDir.mkdirs();

        // 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        Log.d(TAG, "createImageFile : " + image.getAbsolutePath());

        return image;
    }

    /**
     * tempFile 을 bitmap 으로 변환 후 ImageView 에 설정한다.
     */
    private void setImage() {
        ImageResizeUtils.resizeFile(tempFile, tempFile, 1280, isCamera);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Log.d(TAG, "AddMemoActivity 300 setImage : " + originalBm + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        mOcrImage = bitmapToByteArray(originalBm);

        Log.d(TAG, "AddMemoActivity 304 ocrImage:" + mOcrImage + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

//        if (originalBm != null && !mOcrImage.isRecycled()) {
//
//            originalBm.recycle();
//
//            originalBm = null;
//        }

        /**
         *  tempFile 사용 후 null 처리를 해줘야 함
         *  (resultCode != RESULT_OK) 일 때 tempFile 을 삭제하기 때문에
         *  기존에 데이터가 남아 있게 되면 원치 않은 삭제가 이루어질 수 있음
         */
        tempFile = null;
        }


    public byte[] bitmapToByteArray( Bitmap bitmap ) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
        bitmap.compress( Bitmap.CompressFormat.JPEG, 100, stream) ;
        byte[] byteArray = stream.toByteArray() ;
        return byteArray ;
    }


    /**
     * 권한 설정
     */
    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
                isPermission = true;

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
                isPermission = false;

            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
//                .setRationaleMessage(getResources().getString(R.string.permission_2))
//                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }
}
