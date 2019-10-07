package com.cookandroid.book;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AddBook extends Activity {

    private ImageView bookCover;
    private static final int REQUEST_CODE = 0;

    private RatingBar ratingbar;

    private EditText bookTitle;
    private EditText writer;
    private EditText publisher;
    private EditText comment;

    private String bTitle;
    private String bScore;
    private String bWriter;
    private String bPublisher;
    private String bComment;
    private String bReadingStartDate;
    private String bRedingFinishDate;

    private Button btnReadingStartDate;
    private Button btnReadingFinishDate;
    private Button btnSaveBook;

    private Boolean isPermission = true;

    private static final String TAG = "book";

    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;

    private File tempFile;

    private Boolean isCamera = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_book);

        bookCover = (ImageView) findViewById(R.id.bookCover);
        bookCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent, REQUEST_CODE);

                tedPermission();

//                setContentView(R.layout.photo_dialog);
//
//                Button btnCamera = (Button) findViewById(R.id.btnCamera);
//                Button btnPicture = (Button) findViewById(R.id.btnPicture);
//
//                if(isPermission) {
//                    btnCamera.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            takePhoto();
//                        }
//                    });
//                } else {
//                    Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
//                }
//
//                if(isPermission) {
//                    btnPicture.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            goToAlbum();
//                        }
//                    });
//                } else {
//                    Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
//                }

                // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.

//                if (isPermission) {
//                    setContentView(R.layout.photo_dialog);
//                    Button btnCamera = (Button) findViewById(R.id.btnCamera);
//                    Button btnPicture = (Button) findViewById(R.id.btnPicture);
//
//                    btnCamera.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            takePhoto();
//                        }
//                    });
//
//                    btnPicture.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            goToAlbum();
//                        }
//                    });
//
//                } else {
//                    Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
//                }
                if (isPermission) goToAlbum();
                else
                    Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();

//                if (isPermission) takePhoto();
//                else
//                    Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
            }
        });


        RatingBar ratingbar = findViewById(R.id.ratingBar);
        final TextView bookScore = (TextView) findViewById(R.id.bookScore);

        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                bookScore.setText(rating + "점");
            }
        });

        bookTitle = (EditText)findViewById(R.id.bookTitle);
        writer = (EditText)findViewById(R.id.writer);
        publisher = (EditText)findViewById(R.id.publisher);
        comment = (EditText)findViewById(R.id.comment);

        btnReadingStartDate = (Button) findViewById(R.id.btnReadingStartDate);
        btnReadingStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReadingStartDatePicker();
            }
        });

        btnReadingFinishDate = (Button) findViewById(R.id.btnReadingFinishDate);
        btnReadingFinishDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReadingFinishDatePicker();
            }
        });

        btnSaveBook = (Button) findViewById(R.id.btnSaveBook);
        btnSaveBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bScore = bookScore.getText().toString();
                bTitle = bookTitle.getText().toString();
                bWriter = writer.getText().toString();
                bPublisher = publisher.getText().toString();
                bReadingStartDate = btnReadingStartDate.getText().toString();
                bRedingFinishDate = btnReadingFinishDate.getText().toString();
                bComment = comment.getText().toString();

                DBHelper helper = new DBHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();

                db.execSQL("INSERT INTO bookData (bookDate, bookTitle, bookScore, writer, publisher," +
                        "readingStartDate, readingFinishDate, comment) VALUES(datetime(), ?, ?, ?, ?, ?, ?, ?)",
                        new String[]{bTitle, bScore, bWriter, bPublisher, bReadingStartDate, bRedingFinishDate, bComment});
                db.close();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
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

        if (requestCode == PICK_FROM_ALBUM) {

            Uri photoUri = data.getData();
            Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);

            Cursor cursor = null;

            try {
                /*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 */
                String[] proj = {MediaStore.Images.Media.DATA};

                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

                Log.d(TAG, "tempFile Uri : " + Uri.fromFile(tempFile));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            setImage();

        } else if (requestCode == PICK_FROM_CAMERA) {
            setImage();
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

    /**
     * tempFile 을 bitmap 으로 변환 후 ImageView 에 설정한다.
     */
    private void setImage() {

        ImageView imageView = findViewById(R.id.bookCover);

//        ImageResizeUtils.resizeFile(tempFile, tempFile, 1280, isCamera);
        ImageResizeUtils.resizeFile(tempFile, tempFile, 1280, isCamera);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());

        imageView.setImageBitmap(originalBm);

        /**
         *  tempFile 사용 후 null 처리를 해줘야 합니다.
         *  (resultCode != RESULT_OK) 일 때 tempFile 을 삭제하기 때문에
         *  기존에 데이터가 남아 있게 되면 원치 않은 삭제가 이뤄집니다.
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


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode == REQUEST_CODE)
//        {
//            if(resultCode == RESULT_OK)
//            {
//                try{
//                    InputStream in = getContentResolver().openInputStream(data.getData());
//
//                    Bitmap img = BitmapFactory.decodeStream(in);
//                    in.close();
//
//                    bookCover.setImageBitmap(img);
//                } catch(Exception e)
//                {
//
//                }
//            }
//            else if(resultCode == RESULT_CANCELED)
//            {
//                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
//            }
//        }
//
//    }


    private void ReadingStartDatePicker() {
        Calendar c = Calendar.getInstance();
        int cyear = c.get(Calendar.YEAR);
        int cmonth = c.get(Calendar.MONTH);
        int cday = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    // onDateSet method
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        btnReadingStartDate.setText(String.valueOf(year) + "년 "
                                + String.valueOf(monthOfYear + 1) + "월 "
                                + String.valueOf(dayOfMonth) + "일");
//                        Toast.makeText(AddBook.this, "Selected Date is = "
//                                + date_selected, Toast.LENGTH_SHORT).show();
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
                        btnReadingFinishDate.setText(String.valueOf(year) + "년 "
                                + String.valueOf(monthOfYear + 1) + "월 "
                                + String.valueOf(dayOfMonth) + "일");
//                        Toast.makeText(AddBook.this, "Selected Date is = "
//                                + date_selected, Toast.LENGTH_SHORT).show();
                    }
                };
        DatePickerDialog alert =
                new DatePickerDialog(this, mDateSetListener, cyear, cmonth, cday);
        alert.show();
    }

}

