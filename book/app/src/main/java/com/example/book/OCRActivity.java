package com.example.book;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OCRActivity extends Activity {

    public byte[] bCover;
    public String bScore;
    public String bTitle;
    public String bWriter;
    public String bPublisher;
    public String bStartDate;
    public String bFinishDate;
    public String bComment;

    public byte[] mOcrImageByte;
    public Bitmap mOcrImage;
    public String mOcrText;

    private ImageView memoOcrImage;
    private TextView memoOcrText;
    private Button memoBtnSaveOCR;

    private TessBaseAPI mTess; //Tess API reference
    private String datapath = "" ;

    private String[] languageList;

    private Boolean isPermission = true;

    private static final String TAG = "AddBookActivity";

    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;

    private File tempFile;

    private Boolean isCamera = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ocr);

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

        memoOcrImage = findViewById(R.id.ocrImage);
        memoOcrText = findViewById(R.id.ocrText);
        memoBtnSaveOCR = findViewById(R.id.btnSaveOCR);

        // ocrImage byte 배열 가져오기
        mOcrImageByte = getIntent().getByteArrayExtra("mOcrImage");

        Log.d(TAG, "OCRActivity 70 mOCRImageByte:" + mOcrImageByte + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        mOcrImage = byteArrayToBitmap(mOcrImageByte);

        Log.d(TAG, "OCRActivity 74 mOCRImage:" + mOcrImage + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        memoOcrImage.setImageBitmap(mOcrImage);

//        if (mOcrImage != null && !mOcrImage.isRecycled()) {
//
//            mOcrImage.recycle();
//
//            mOcrImage = null;
//        }



        // ocr
        //언어파일 경로
        datapath = getFilesDir()+ "/tesseract/";

        //트레이닝데이터가 카피되어 있는지 체크
        checkFile(new File(datapath + "tessdata/"));

        //Tesseract API
        String lang = "kor";

        mTess = new TessBaseAPI();
        mTess.init(datapath, lang);

        memoOcrImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tedPermission();

                memoOcrImage.setImageBitmap(null);

                if (isPermission) {
                    goToAlbum();
                }
                else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
                }
            }
        });

        memoBtnSaveOCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOcrText = memoOcrText.getText().toString();

                Log.d(TAG, "onClick: mOcrText:" + mOcrText + "########################################################################");

                Intent intent = new Intent(getApplicationContext(), AddMemoActivity.class);

                intent.putExtra("mOcrText", mOcrText);

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

    public Bitmap byteArrayToBitmap( byte[] byteArray ) {
        Bitmap bitmap = BitmapFactory.decodeByteArray( byteArray, 0, byteArray.length ) ;
        return bitmap ;
    }

    //Process an Image
    public void processImage(View view) {
        String OCRresult = null;
        mTess.setImage(mOcrImage);
        OCRresult = mTess.getUTF8Text();
        TextView OCRTextView = findViewById(R.id.ocrText);
        OCRTextView.setText(OCRresult);
    }

    //copy file to device
    private void copyFiles() {
        try{
            String filepath = datapath + "/tessdata/kor.traineddata";
            AssetManager assetManager = getAssets();
            InputStream instream = assetManager.open("tessdata/kor.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);
            byte[] buffer = new byte[4096];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //check file on the device
    private void checkFile(File dir) {
        //디렉토리가 없으면 디렉토리를 만들고 그후에 파일을 카피
        if(!dir.exists()&& dir.mkdirs()) {
            copyFiles();
        }
        //디렉토리가 있지만 파일이 없으면 파일카피 진행
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/kor.traineddata";
            File datafile = new File(datafilepath);
            if(!datafile.exists()) {
                copyFiles();
            }
        }
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

                Log.d(TAG, "onActivityResult: 197 startActivity(intent) ################################################################################");
            }
        }
    }


    /**
     * tempFile 을 bitmap 으로 변환 후 ImageView 에 설정한다.
     */
    private void setImage() {
        ImageResizeUtils.resizeFile(tempFile, tempFile, 1280, isCamera);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Log.d(TAG, "AddMemoActivity 300 setImage : " + originalBm + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        Log.d(TAG, "AddMemoActivity 304 ocrImage:" + mOcrImage + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        memoOcrImage.setImageBitmap(originalBm);

        /**
         *  tempFile 사용 후 null 처리를 해줘야 함
         *  (resultCode != RESULT_OK) 일 때 tempFile 을 삭제하기 때문에
         *  기존에 데이터가 남아 있게 되면 원치 않은 삭제가 이루어질 수 있음
         */
        tempFile = null;
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
