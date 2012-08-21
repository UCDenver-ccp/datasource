/**
 * 
 */
package edu.ucdenver.ccp.datasource.download;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.calendar.CalendarUtil;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class ParallelDataSourceDownloader {

	private static final Logger logger = Logger.getLogger(ParallelDataSourceDownloader.class);

	/**
	 * A list of resources that needs to be downloaded is retrieved sorted by approximate file size
	 * (and therefore approximate download time). We want to start the download of the larger files
	 * asap, however we don't want to simply wait until those files have been downloaded before any
	 * further processing takes place so we work our way from both ends to the list downloading both
	 * larger and smaller files simultaneously.
	 * 
	 * @param numDownloadThreads
	 * @param downloadDirectory
	 * @throws IOException
	 */
	public static void downloadSourceFiles(int numDownloadThreads, final File downloadDirectory) throws IOException {
		final List<DataSourceDownloader> dLoaderList = DataSourceDownloader.getDownloadersByFileSize();
		
		// we are excluding the UniProt TREMBL download here b/c it's not being used and it's huge
		dLoaderList.remove(DataSourceDownloader.UNIPROT_TREMBL_DAT_FILE);
		
		
//		final List<DataSourceDownloader> dLoaderList = new ArrayList<DataSourceDownloader>();
//		dLoaderList.add(DataSourceDownloader.MI_OBO_FILE);
		
		Set<DataSourceDownloader> dLoaderSet = new HashSet<DataSourceDownloader>(dLoaderList);

		for (DataSourceDownloader dLoader : dLoaderList)
			dLoader.touchFile(downloadDirectory);

		/* The larger (slower to download) files are at the top of the list, index = 0 */
		int slowerIndex = 0;
		/*
		 * The smaller (faster to download) files are at the bottome of the list, index =
		 * dLoaderList.size()-1
		 */
		int fasterIndex = dLoaderList.size() - 1;

		int downloadCount = 0;

		DownloaderThreadGroup downloadThreadGroup = new DownloaderThreadGroup();
		Thread[] downloadThreads = new Thread[numDownloadThreads];
		while (!dLoaderSet.isEmpty()) {
			logger.info("Checking download threads... " + CalendarUtil.getTimeStamp());
			for (int i = 0; i < downloadThreads.length; i++) {
				if (downloadThreads[i] == null || !downloadThreads[i].isAlive()) {
					/* when i is even download from the bottom of the list (faster/smaller files) */
					int dLoadIndex = -1;
					if (i % 2 == 0) {
						dLoadIndex = fasterIndex--;
					} else {
						dLoadIndex = slowerIndex++;
					}
					final int index = dLoadIndex;

					downloadCount++;
					dLoaderSet.remove(dLoaderList.get(index));
					downloadThreads[i] = new Thread(downloadThreadGroup, new Runnable() {

						@Override
						public void run() {
							try {
								logger.info("STARTING DOWNLOAD THREAD: " + dLoaderList.get(index).name());
								dLoaderList.get(index).downloadFile(downloadDirectory);
								logger.info("FINISHED DOWNLOAD THREAD: " + dLoaderList.get(index).name());
							} catch (IOException e) {
								logger.error("IOException detected while downloading: " + dLoaderList.get(index).name());
								throw new RuntimeException(e);
							}
						}
					});
					downloadThreads[i].start();
				}
				if (dLoaderSet.isEmpty()) {
					break;
				}
			}

			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		if (downloadCount != dLoaderList.size()) {
			throw new RuntimeException("Unexpected number of downloads occurred. Expected: " + dLoaderList.size()
					+ " but was " + downloadCount + " REMAINING: " + dLoaderSet.toString());
		}
		
		if (downloadThreadGroup.hasDetectedError()) {
			throw new RuntimeException("An error was detected during resource download. Check the logs.");
		}

	}

	/**
	 * Simple class to help log exceptions thrown by the agLoadThread
	 * 
	 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
	 * 
	 */
	public static class DownloaderThreadGroup extends ThreadGroup {

		private boolean errorDetected = false;

		private static final String DOWNLOADER_THREAD_GROUP_NAME = "DownloaderThreadGroup";

		/**
		 * @param name
		 */
		public DownloaderThreadGroup() {
			super(DOWNLOADER_THREAD_GROUP_NAME);
		}

		public boolean hasDetectedError() {
			return errorDetected;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.ThreadGroup#uncaughtException(java.lang.Thread, java.lang.Throwable)
		 */
		@Override
		public void uncaughtException(Thread t, Throwable e) {
			errorDetected = true;
			logger.error("Error during download: " + e.getMessage());
			logger.error(e);
		}
	}

	public static void main(String[] args) {
		BasicConfigurator.configure();
		int numDownloadThreads = Integer.valueOf(args[0]);
		File downloadDirectory = new File(args[1]);
		try {
			downloadSourceFiles(numDownloadThreads, downloadDirectory);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
