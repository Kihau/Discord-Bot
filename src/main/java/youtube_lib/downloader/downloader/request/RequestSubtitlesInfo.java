package youtube_lib.downloader.downloader.request;

import youtube_lib.downloader.model.subtitles.SubtitlesInfo;

import java.util.List;

public class RequestSubtitlesInfo extends Request<RequestSubtitlesInfo, List<SubtitlesInfo>> {

    private final String videoId;

    public RequestSubtitlesInfo(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoId() {
        return videoId;
    }
}
