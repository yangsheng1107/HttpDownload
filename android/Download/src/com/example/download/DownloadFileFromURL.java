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

import android.os.AsyncTask;
import android.util.Log;

public class DownloadFileFromURL extends AsyncTask<String, String, String> {
	DownloadFileCallback callback;
	// constant value
	private static final String ACTIVITY_TAG = "DOWNLOAD";

	public DownloadFileFromURL(DownloadFileCallback callback) {
		this.callback = callback;
	}

	/**
	 * Before starting background thread Show Progress Dialog
	 * */
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		callback.onDownloadFilePreExecute();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		// TODO Auto-generated method stub
		callback.onDownloadFileProgressUpdate(Integer.parseInt(values[0]));
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
		return null;
	}

	/**
	 * After completing background task Dismiss the progress dialog
	 * **/
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		callback.doDownloadFilePostExecute(result);
	}

}
