package com.example.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final int NOTIFICATION_ID = 1;
	TextView messageText;
	Button downloadButton;
	ProgressDialog pDialog = null;

	private static final String ACTIVITY_TAG = "DOWNLOAD";
	private static final String DOWNLOAD_URL = "http://192.168.56.1/download/images.jpg";
	
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

		messageText.setText("loading file path : " + filePathName);
	}

	private void initUploadEvent() {
		// TODO Auto-generated method stub
		downloadButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new DownloadFileFromURL().execute(DOWNLOAD_URL, filePathName);

			}
		});
	}

	
	public void Notification() {
		// TODO Auto-generated method stub
		String strtitle = "Done";
		String strtext = "Download file success";
		Intent i = new Intent(this, DetailActivity.class);
		
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_stat_notification)
				.setTicker(strtext)
				.setContentTitle(strtitle)
				.setContentText(strtext)
				.addAction(R.drawable.ic_stat_notification,"Action Button", pIntent)
				.setContentIntent(pIntent)
				.setAutoCancel(true);
		
		NotificationManager notificationmanager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		notificationmanager.notify(0, builder.build());
		
	}	
	class DownloadFileFromURL extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Downloading file. Please wait...");
			pDialog.setIndeterminate(false);// 取消進度條
			pDialog.setCancelable(true);// 開啟取消
			pDialog.setMax(100);
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pDialog.show();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			pDialog.setProgress(Integer.parseInt(values[0]));
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			InputStream inputStream = null;
			OutputStream outputStream = null;
			long lengthOfFile;
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(args[0]);

				// Http connect success.
				HttpResponse httpResponse;
				httpResponse = httpClient.execute(httpGet);
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity httpEntity = httpResponse.getEntity();
					inputStream = httpEntity.getContent();
					lengthOfFile = httpEntity.getContentLength();
				} else {
					return null;
				}

				outputStream = new FileOutputStream(new File(args[1]));

				int read = 0;
				byte[] bytes = new byte[1024];
				long total = 0;

				while ((read = inputStream.read(bytes)) != -1) {
					total += read;
					publishProgress("" + (int) ((total * 100) / lengthOfFile));
					outputStream.write(bytes, 0, read);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(ACTIVITY_TAG, e.getMessage());
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Log.e(ACTIVITY_TAG, e.getMessage());
					}
				}

				if (outputStream != null) {
					try {
						outputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Log.e(ACTIVITY_TAG, e.getMessage());
					}
				}
			}


			return "Done";
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			Notification();			
			if (result != null) {
				Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG)
						.show();
			}

		}

	}
}