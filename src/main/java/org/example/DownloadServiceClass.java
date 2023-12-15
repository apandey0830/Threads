package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DownloadServiceClass implements DownloadService{

    // Placeholder data for testing
    private List<DownloadInfo> downloadInfoList;

    public DownloadServiceClass() {
        // Initialize with some sample data
        this.downloadInfoList = new ArrayList<>();
        downloadInfoList.add(new TheDownloadInfo("file1.txt", "key1", "https://download-service.com/file1", 50));
        downloadInfoList.add(new TheDownloadInfo("file2.txt", "key2", "https://download-service.com/file2", 70));
    }

    @Override
    public List<DownloadInfo> getDownloadInfos(long packageId) {
        // Implement your logic to fetch DownloadInfo based on packageId
        // For simplicity, return the sample data
        return downloadInfoList;
    }

    @Override
    public InputStream downloadFile(DownloadInfo downloadInfo) {
        // Implement your logic to download file content based on DownloadInfo
        // For simplicity, return a placeholder byte array
        return new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
    }

    // Placeholder implementation of DownloadInfo
    private static class TheDownloadInfo implements DownloadInfo {
        private final String originalFileName;
        private final String fileKey;
        private final String downloadURL;
        private final int size;

        public TheDownloadInfo(String originalFileName, String fileKey, String downloadURL, int size) {
            this.originalFileName = originalFileName;
            this.fileKey = fileKey;
            this.downloadURL = downloadURL;
            this.size = size;
        }

        @Override
        public int getSize() {
            return size;
        }

        @Override
        public String getOriginalFileName() {
            return originalFileName;
        }

        @Override
        public String getFileKey() {
            return fileKey;
        }

        @Override
        public String getDownloadURL() {
            return downloadURL;
        }
    }


}
