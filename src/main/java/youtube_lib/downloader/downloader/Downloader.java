package youtube_lib.downloader.downloader;

import youtube_lib.downloader.downloader.request.RequestVideoFileDownload;
import youtube_lib.downloader.downloader.request.RequestVideoStreamDownload;
import youtube_lib.downloader.downloader.request.RequestWebpage;
import youtube_lib.downloader.downloader.response.Response;

import java.io.File;

public interface Downloader {

    Response<String> downloadWebpage(RequestWebpage request);

    Response<File> downloadVideoAsFile(RequestVideoFileDownload request);

    Response<Void> downloadVideoAsStream(RequestVideoStreamDownload request);

}
