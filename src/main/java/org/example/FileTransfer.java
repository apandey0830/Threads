package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FileTransfer {

    private static final int MAX_CONCURRENT_SIZE = 100 * 1024 * 1024; // 100 MB
    private static final String[] DISALLOWED_EXTENSIONS = {"cmd", "com", "dll", "dmg", "exe", "iso", "jar", "js"};

    public OperationReport transferFiles(long packageId, DownloadService downloadService, UploadService uploadService) {
        AtomicInteger successes = new AtomicInteger();
        AtomicInteger failures = new AtomicInteger();
        long startTime = System.currentTimeMillis();

        try {
            List<DownloadInfo> downloadInfos = downloadService.getDownloadInfos(packageId);

            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            List<Future<FileTransferResult>> futures = new ArrayList<>();

            for (DownloadInfo downloadInfo : downloadInfos) {
                Callable<FileTransferResult> fileTransferTask = () -> {
                    // Check file size constraint
                    if (downloadInfo.getSize() > MAX_CONCURRENT_SIZE) {
                        return new FileTransferResult(downloadInfo.getOriginalFileName(), false, "File size exceeds limit", System.currentTimeMillis());
                    }

                    // Check file name uniqueness
                    if (!isFileNameUnique(downloadInfo.getOriginalFileName(), downloadInfos)) {
                        return new FileTransferResult(downloadInfo.getOriginalFileName(), false, "File name is not unique", System.currentTimeMillis());
                    }

                    // Check file extension constraint
                    if (!isAllowedExtension(downloadInfo.getOriginalFileName())) {
                        return new FileTransferResult(downloadInfo.getOriginalFileName(), false, "File extension not allowed", System.currentTimeMillis());
                    }

                    // Download and upload file
                    try {
                        long uploadStartTime = System.currentTimeMillis();
                        uploadService.doUpload(downloadInfo.getFileKey(), downloadService.downloadFile(downloadInfo), downloadInfo.getSize());
                        long uploadEndTime = System.currentTimeMillis();
                        successes.getAndIncrement();
                        return new FileTransferResult(downloadInfo.getOriginalFileName(), true, "Success", (uploadEndTime - uploadStartTime));
                    } catch (UploadException e) {
                        failures.getAndIncrement();
                        return new FileTransferResult(downloadInfo.getOriginalFileName(), false, e.getMessage(), System.currentTimeMillis());
                    }
                };

                futures.add(executorService.submit(fileTransferTask));
            }

            // Shutdown the executor service and wait for all tasks to complete
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            // Process the results
            for (Future<FileTransferResult> future : futures) {
                try {
                    FileTransferResult result = future.get();
                    System.out.println("File: " + result.getFileName() +
                            ", Time to Upload: " + (result.isSuccess() ? result.getUploadTime() + " ms" : "N/A") +
                            ", Status: " + (result.isSuccess() ? "Success" : "Failure") +
                            (result.isSuccess() ? "" : ", Error: " + result.getError()));
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println("Operation failed due to an exception: " + e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        return new OperationReport(successes.get(), failures.get(), totalTime);
    }


    private boolean isFileNameUnique(String fileName, List<DownloadInfo> downloadInfos) {
        return downloadInfos.stream().filter(info -> info.getOriginalFileName().equals(fileName)).count() == 1;
    }

    private boolean isAllowedExtension(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        for (String disallowedExtension : DISALLOWED_EXTENSIONS) {
            if (disallowedExtension.equals(fileExtension)) {
                return false;
            }
        }
        return true;
    }


    private static class FileTransferResult {
        private final String fileName;
        private final boolean success;
        private final String error;
        private final long uploadTime;

        public FileTransferResult(String fileName, boolean success, String error, long uploadTime) {
            this.fileName = fileName;
            this.success = success;
            this.error = error;
            this.uploadTime = uploadTime;
        }

        public String getFileName() {
            return fileName;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getError() {
            return error;
        }

        public long getUploadTime() {
            return uploadTime;
        }
    }


}
