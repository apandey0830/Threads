package org.example;

public class Main {
    public static void main(String[] args) {

        DownloadServiceClass downloadService = new DownloadServiceClass();
        UploadServiceClass uploadService = new UploadServiceClass();
        FileTransfer fileTransfer = new FileTransfer();

        long packageId = 123;

        OperationReport report = fileTransfer.transferFiles(packageId, downloadService, uploadService);

        System.out.println("---- File Transfer Report ----");
        System.out.println("Total Time: " + report.getTotalTime() + " ms");
        System.out.println("Number of Successes: " + report.getSuccesses());
        System.out.println("Number of Failures: " + report.getFailures());

    }
}