package org.example;

import java.io.InputStream;
import java.util.List;

public interface DownloadService {

    List<DownloadInfo> getDownloadInfos(long packageId);

    // You may need to handle IOException for real-world scenarios
    InputStream downloadFile(DownloadInfo downloadInfo);


}
