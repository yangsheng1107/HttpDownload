package com.example.download;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements DownloadFileCallback {
	private static final String DOWNLOAD_URL = "http://192.168.0.102/download/images.jpg";

	private TextView messageText;
	private Button downloadButton;
	private ProgressDialog pDialog = null;

	private String filePathName = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		filePathName = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/downloadedfile.jpg";
		initView();
		initUploadEvent();
	}

	private void initView() {
		// TODO Auto-generated method stub
		downloadButton = (Button) findViewById(R.id.downloadButton);
		messageText = (TextView) findViewById(R.id.messageText);

		messageText.setText(getResources().getString(
				R.string.main_activity_download_text)
				+ filePathName);
	}

	private void initUploadEvent() {
		// TODO Auto-generated method stub
		downloadButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new DownloadFileFromURL(MainActivity.this).execute(
						DOWNLOAD_URL, filePathName);
			}
		});
	}

	public void Notification() {
		// TODO Auto-generated method stub
		String strtitle = getResources().getString(
				R.string.main_activity_notification_title);
		String strtext = getResources().getString(
				R.string.main_activity_notification_text);
		Intent i = new Intent(this, DetailActivity.class);

		PendingIntent pIntent = PendingIntent.getActivity(this, 0, i,
				PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this)
				.setSmallIcon(R.drawable.ic_stat_notification)
				.setTicker(strtext)
				.setContentTitle(strtitle)
				.setContentText(strtext)
				.addAction(R.drawable.ic_stat_notification, "Action Button",
						pIntent).setContentIntent(pIntent).setAutoCancel(true);

		NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationmanager.notify(0, builder.build());
	}

	@Override
	public void onDownloadFilePreExecute() {
		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(MainActivity.this);
		pDialog.setMessage(getResources().getString(
				R.string.main_activity_download_progress_dialog_context));
		pDialog.setIndeterminate(false);// 取消進度條
		pDialog.setCancelable(true);// 開啟取消
		pDialog.setMax(100);
		pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pDialog.show();
	}

	@Override
	public void onDownloadFileProgressUpdate(int value) {
		// TODO Auto-generated method stub
		pDialog.setProgress(value);
	}

	@Override
	public void doDownloadFilePostExecute(String result) {
		// TODO Auto-generated method stub
		pDialog.dismiss();
		Notification();
		if (result != null) {
			Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
		}
	}
}