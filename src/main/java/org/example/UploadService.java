package org.example;

import java.io.InputStream;

public interface UploadService {

    void doUpload(String key, InputStream data, int size) throws UploadException;


}
