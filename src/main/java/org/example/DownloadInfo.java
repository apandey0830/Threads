package org.example;

public interface DownloadInfo {

    int getSize(); // bytes

    String getOriginalFileName();

    String getFileKey();

    String getDownloadURL();


}
