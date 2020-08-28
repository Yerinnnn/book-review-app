package com.example.book;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.soundcloud.android.crop.Crop;

public class AddBookActivity extends Activity {

    private String bScore;
    private String bTitle;
    private String bWriter;
    private String bPublisher;
    private String bStartDate;
    private String bFinishDate;
    private String bComment;
    private String bCover;

    private ImageView bookCover;
    private RatingBar bookRatingBar;
    private TextView bookScore;
    private TextView bookTitle;
    private TextView bookWriter;
    private TextView bookPublisher;
    private Button bookStartDate;
    private Button bookFinishDate;
    private TextView bookComment;
    private Button btnSave;

    private Boolean isPermission = true;

    private static final String TAG = "AddBookActivity";

    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;

    private File tempFile;

    private static final int REQUEST_CODE = 0;
    private Uri uri;
    private Uri savingUri;

    private Boolean isCamera = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_book);

        bookCover = findViewById(R.id.bookCover);
        bookRatingBar = findViewById(R.id.ratingBar);
        bookScore = findViewById(R.id.bookScore);
        bookTitle = findViewById(R.id.bookTitle);
        bookWriter = findViewById(R.id.bookWriter);
        bookPublisher = findViewById(R.id.bookPublisher);
        bookComment = findViewById(R.id.bookComment);
        btnSave = findViewById(R.id.btnSaveBook);
        bookStartDate = findViewById(R.id.btnReadingStartDate);
        bookFinishDate = findViewById(R.id.btnReadingFinishDate);

        bookCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tedPermission();

                if (isPermission) goToAlbum();
                else
                    Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
            }
        });

        bookRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                bookScore.setText(Float.toString(rating));
            }
        });

        bookStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReadingStartDatePicker();
            }
        });

        bookFinishDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReadingFinishDatePicker();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookDBHelper bookDbHelper = new BookDBHelper(getApplicationContext(), "book", null, 1);

                bScore = bookScore.getText().toString();
                bTitle = bookTitle.getText().toString();
                bWriter = bookWriter.getText().toString();
                bPublisher = bookPublisher.getText().toString();
                bStartDate = bookStartDate.getText().toString();
                bFinishDate = bookFinishDate.getText().toString();
                bComment = bookComment.getText().toString();

                Log.d(TAG, "onClick: bInsert ========================================================================================" + bTitle);

                BitmapDrawable bitmapDrawable = (BitmapDrawable) bookCover.getDrawable();

                if (bitmapDrawable == null) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(AddBookActivity.this);
                    dialog.setMessage("사진을 넣어주세요 :)");
                    dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    dialog.show();

                    return;
                } else {
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    bCover = savingUri.toString();
                }

                bookDbHelper.bInsert(bScore, bTitle, bWriter, bPublisher, bStartDate, bFinishDate, bComment, bCover);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void ReadingStartDatePicker() {
        Calendar c = Calendar.getInstance();
        int cyear = c.get(Calendar.YEAR);
        int cmonth = c.get(Calendar.MONTH);
        int cday = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    // onDateSet method
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        bookStartDate.setText(String.valueOf(year) + "년 "
                                + String.valueOf(monthOfYear + 1) + "월 "
                                + String.valueOf(dayOfMonth) + "일");
                    }
                };
        DatePickerDialog alert =
                new DatePickerDialog(this, mDateSetListener, cyear, cmonth, cday);
        alert.show();
    }

    private void ReadingFinishDatePicker() {
        Calendar c = Calendar.getInstance();
        int cyear = c.get(Calendar.YEAR);
        int cmonth = c.get(Calendar.MONTH);
        int cday = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    // onDateSet method
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        bookFinishDate.setText((year) + "년 "
                                + (monthOfYear + 1) + "월 "
                                + (dayOfMonth) + "일");
                    }
                };
        DatePickerDialog alert =
                new DatePickerDialog(this, mDateSetListener, cyear, cmonth, cday);
        alert.show();
    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != Activity.RESULT_OK) {
//            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
//
//            if (tempFile != null) {
//                if (tempFile.exists()) {
//                    if (tempFile.delete()) {
//                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
//                        tempFile = null;
//                    }
//                }
//            }
//            return;
//        }
//
//        if (requestCode == PICK_FROM_ALBUM) {
//
//            Uri photoUri = data.getData();
//            Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);
//
//            Cursor cursor = null;
//
//            try {
//                /*
//                 *  Uri 스키마를
//                 *  content:/// 에서 file:/// 로  변경한다.
//                 */
//                String[] proj = {MediaStore.Images.Media.DATA};
//
//                assert photoUri != null;
//                cursor = getContentResolver().query(photoUri, proj, null, null, null);
//
//                assert cursor != null;
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//
//                cursor.moveToFirst();
//
//                tempFile = new File(cursor.getString(column_index));
//
//                Log.d(TAG, "tempFile Uri : " + Uri.fromFile(tempFile));
//
//            } finally {
//                if (cursor != null) {
//                    cursor.close();
//                }
//            }
//
//            setImage();
//
//        } else if (requestCode == PICK_FROM_CAMERA) {
//            setImage();
//        }
//    }
//
//    /**
//     * 앨범에서 이미지 가져오기
//     */
//    private void goToAlbum() {
//        isCamera = false;
//
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//        startActivityForResult(intent, PICK_FROM_ALBUM);
//    }
//
//
//    /**
//     * 카메라에서 이미지 가져오기
//     */
//    private void takePhoto() {
//        isCamera = true;
//
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        try {
//            tempFile = createImageFile();
//        } catch (IOException e) {
//            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
//            finish();
//            e.printStackTrace();
//        }
//        if (tempFile != null) {
//
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//
//                Uri photoUri = FileProvider.getUriForFile(this,
//                        "book.provider", tempFile);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                startActivityForResult(intent, PICK_FROM_CAMERA);
//
//            } else {
//
//                Uri photoUri = Uri.fromFile(tempFile);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                startActivityForResult(intent, PICK_FROM_CAMERA);
//            }
//        }
//    }
//
//    /**
//     * 폴더 및 파일 만들기
//     */
//    private File createImageFile() throws IOException {
//
//        // 이미지 파일 이름 ( book_{시간}_ )
//        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
//        String imageFileName = "book_" + timeStamp + "_";
//
//        // 이미지가 저장될 폴더 이름 ( book )
//        File storageDir = new File(Environment.getExternalStorageDirectory() + "/book/");
//        if (!storageDir.exists()) storageDir.mkdirs();
//
//        // 파일 생성
//        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
//        Log.d(TAG, "createImageFile : " + image.getAbsolutePath());
//
//        return image;
//    }
//
//    /**
//     * tempFile 을 bitmap 으로 변환 후 ImageView 에 설정한다.
//     */
//    private void setImage() {
//
//        ImageView imageView = findViewById(R.id.bookCover);
//
////        ImageResizeUtils.resizeFile(tempFile, tempFile, 1280, isCamera);
//        ImageResizeUtils.resizeFile(tempFile, tempFile, 1280, isCamera);
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
//        Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());
//
//        imageView.setImageBitmap(originalBm);
//
//        /**
//         *  tempFile 사용 후 null 처리를 해줘야 합니다.
//         *  (resultCode != RESULT_OK) 일 때 tempFile 을 삭제하기 때문에
//         *  기존에 데이터가 남아 있게 되면 원치 않은 삭제가 이뤄집니다.
//         */
//        tempFile = null;
//
//    }
//
//    /**
//     * 권한 설정
//     */
//    private void tedPermission() {
//
//        PermissionListener permissionListener = new PermissionListener() {
//            @Override
//            public void onPermissionGranted() {
//                // 권한 요청 성공
//                isPermission = true;
//
//            }
//
//            @Override
//            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//                // 권한 요청 실패
//                isPermission = false;
//
//            }
//        };
//
//        TedPermission.with(this)
//                .setPermissionListener(permissionListener)
////                .setRationaleMessage(getResources().getString(R.string.permission_2))
////                .setDeniedMessage(getResources().getString(R.string.permission_1))
//                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
//                .check();
//
//    }


    /**
     * 앨범에서 이미지 가져오기
     */
    private void goToAlbum() {
        isCamera = false;

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);

//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, REQUEST_CODE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if (tempFile != null) {
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
//            case PICK_FROM_CAMERA: {
//
//                Uri photoUri = Uri.fromFile(tempFile);
//                Log.d(TAG, "takePhoto photoUri : " + photoUri);
//
//                cropImage(photoUri);
//
//                break;
//            }
            case Crop.REQUEST_CROP: {
                //File cropFile = new File(Crop.getOutput(data).getPath());
                setImage();
            }
        }
    }


    // 이미지 crop
    private void cropImage(Uri photoUri) {
//
//      // 갤러리에서 선택한 경우에는 tempFile 이 없으므로 새로 생성해야함
        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            e.printStackTrace();
        }
//
//        //크롭 후 저장할 Uri
        savingUri = Uri.fromFile(tempFile);

        Crop.of(photoUri, savingUri).withAspect(115, 160).start(this);
    }

    /**
     * 폴더 및 파일 만들기
     */
    private File createImageFile() throws IOException {

        // 이미지 파일 이름 ( book_{시간}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "book_" + timeStamp + "_";
        Log.d(TAG, "createImageFile: imageFileName: " + imageFileName);

        // 이미지가 저장될 폴더 이름 ( book )
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/book/");
//        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//                + File.separator + "/book/");
        if (!storageDir.exists()) storageDir.mkdirs();
        Log.d(TAG, "createImageFile: storageDir: " + storageDir);

        // 파일 생성
        File image = new File (storageDir + imageFileName + ".jpg");
//        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        Log.d(TAG, "createImageFile : " + image.getAbsolutePath());

        return image;
    }


    /**
     * tempFile 을 bitmap 으로 변환 후 ImageView 에 설정한다.
     */
    private void setImage() {
//        ImageResizeUtils.resizeFile(tempFile, tempFile, 1280, isCamera);
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
//        Log.d(TAG, "setImage : " + originalBm + "======================================================================");
//
//        bookCover.setImageBitmap(originalBm);

        bookCover.setImageURI(savingUri);

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
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }
}
