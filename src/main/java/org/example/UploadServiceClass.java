package org.example;

import java.io.InputStream;

public class UploadServiceClass implements UploadService {

    @Override
    public void doUpload(String key, InputStream data, int size) throws UploadException {
        // We should implement the logic to upload data to the remote service
        // For simplicity, printing a message and throw an UploadException for testing
        try {
            // We should replace this with the actual upload logic
            System.out.println("Uploading data with key: " + key + ", Size: " + size + " bytes");
            // Simulating a failure for testing
            throw new UploadException("Upload failed");
        } catch (Exception e) {
            throw new UploadException("Error during upload: " + e.getMessage());
        }
    }



}
