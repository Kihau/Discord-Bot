package music.youtube;

import com.github.kiulian.downloader.Config;
import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.request.RequestVideoStreamDownload;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.Format;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class Youtube{
    private final YoutubeDownloader downloader;
    private final Config config;

    public Config getConfig(){
        return config;
    }

    public Youtube(){
        this.downloader = new YoutubeDownloader();
        this.config = downloader.getConfig();
    }

    public YoutubeDownloader getDownloader(){
        return downloader;
    }

    public YoutubeVideoInfo getVideoInformation(String videoId){
        Response<VideoInfo> response = downloader.getVideoInfo(getRequestVideoInfo(videoId));
        if(!response.ok()){
            return null;
        }
        VideoInfo videoInfo = null;
        try{
            videoInfo = response.data(3, TimeUnit.SECONDS);
        }catch (TimeoutException timeoutExc){
            timeoutExc.printStackTrace();
        }
        return new YoutubeVideoInfo(videoInfo);
    }

    private RequestVideoInfo getRequestVideoInfo(String videoId){
        return new RequestVideoInfo(videoId);
    }
    protected RequestVideoStreamDownload getStream(StreamType type){
        Format format = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        RequestVideoStreamDownload streamDownload = new RequestVideoStreamDownload(null,baos);
        return new RequestVideoStreamDownload(null,null);
    }

}
