package com.example.download;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

public class DetailActivity extends Activity {
	private String filePathName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		ImageView imageView = (ImageView) findViewById(R.id.ImageView1);

		filePathName = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/downloadedfile.jpg";

		BitmapFactory.Options options = new BitmapFactory.Options();

		Bitmap bm = BitmapFactory.decodeFile(filePathName, options);
		imageView.setImageBitmap(bm);

	}

}
