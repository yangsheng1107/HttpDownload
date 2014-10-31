package com.example.download;

public interface DownloadFileCallback {
	void onDownloadFilePreExecute();

	void onDownloadFileProgressUpdate(int value);

	void doDownloadFilePostExecute(String result);
}
