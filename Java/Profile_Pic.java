package com.example.dinr;

/**
 * @author Merlin Thomas
 * @date 4/25/19
 */

import android.support.v7.app.AppCompatActivity;


public class Profile_pic extends AppCompatActivity {

    private static final int SELECT_PICTURE = 0;
    private ImageView imageView;

    Intent intent = new Intent();
  intent.setType("image/*");
  intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_CODE);



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        imageView = (ImageView) findViewById(android.R.id.icon);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bitmap bitmap = getPath(data.getData());
            imageView.setImageBitmap(bitmap);
        }
    }

    private Bitmap getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index);

        Bitmap bitmap = BitmapFactory.decodeFile(filePath);

        return bitmap;
    }

            Intent.ACTION_PICK,
  android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI
  ),
    GET_FROM_GALLERY
);

    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

}


