package youtube_lib.downloader;

import youtube_lib.downloader.cipher.CachedCipherFactory;
import youtube_lib.downloader.downloader.*;
import youtube_lib.downloader.downloader.request.*;
import youtube_lib.downloader.downloader.response.Response;
import youtube_lib.downloader.downloader.response.ResponseImpl;
import youtube_lib.downloader.extractor.ExtractorImpl;
import youtube_lib.downloader.model.videos.VideoInfo;
import youtube_lib.downloader.model.playlist.PlaylistInfo;
import youtube_lib.downloader.model.subtitles.SubtitlesInfo;
import youtube_lib.downloader.parser.ParserImpl;
import youtube_lib.downloader.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static youtube_lib.downloader.model.Utils.createOutDir;

public class YoutubeDownloader {

    private final Config config;
    private final Downloader downloader;
    private final Parser parser;

    public YoutubeDownloader() {
        this(Config.buildDefault());
    }

    public YoutubeDownloader(Config config) {
        this.config = config;
        this.downloader = new DownloaderImpl(config);
        this.parser = new ParserImpl(config, downloader, new ExtractorImpl(downloader), new CachedCipherFactory(downloader));
    }

    public YoutubeDownloader(Config config, Downloader downloader) {
        this(config, downloader, new ParserImpl(config, downloader, new ExtractorImpl(downloader), new CachedCipherFactory(downloader)));
    }

    public YoutubeDownloader(Config config, Downloader downloader, Parser parser) {
        this.config = config;
        this.parser = parser;
        this.downloader = downloader;
    }

    public Config getConfig() {
        return config;
    }

    public Response<VideoInfo> getVideoInfo(RequestVideoInfo request) {
        return parser.parseVideo(request);
    }

    public Response<List<SubtitlesInfo>> getSubtitlesInfo(RequestSubtitlesInfo request) {
        return parser.parseSubtitlesInfo(request);
    }

    public Response<PlaylistInfo> getChannelUploads(RequestChannelUploads request) {
        return parser.parseChannelsUploads(request);
    }

    public Response<PlaylistInfo> getPlaylistInfo(RequestPlaylistInfo request) {
        return parser.parsePlaylist(request);
    }

    public Response<File> downloadVideoFile(RequestVideoFileDownload request) {
        File outDir = request.getOutputDirectory();
        try {
            createOutDir(outDir);
        } catch (IOException e) {
            return ResponseImpl.error(e);
        }

        return downloader.downloadVideoAsFile(request);
    }

    public Response<Void> downloadVideoStream(RequestVideoStreamDownload request) {
        return downloader.downloadVideoAsStream(request);
    }

    public Response<String> downloadSubtitle(RequestWebpage request) {
        return downloader.downloadWebpage(request);
    }

}
