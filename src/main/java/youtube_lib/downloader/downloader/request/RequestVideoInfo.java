package youtube_lib.downloader.downloader.request;

import youtube_lib.downloader.model.videos.VideoInfo;

public class RequestVideoInfo extends Request<RequestVideoInfo, VideoInfo> {

    private final String videoId;

    public RequestVideoInfo(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoId() {
        return videoId;
    }
}
