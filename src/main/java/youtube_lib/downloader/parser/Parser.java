package youtube_lib.downloader.parser;


import youtube_lib.downloader.downloader.request.RequestChannelUploads;
import youtube_lib.downloader.downloader.request.RequestPlaylistInfo;
import youtube_lib.downloader.downloader.request.RequestSubtitlesInfo;
import youtube_lib.downloader.downloader.request.RequestVideoInfo;
import youtube_lib.downloader.downloader.response.Response;
import youtube_lib.downloader.model.videos.VideoInfo;
import youtube_lib.downloader.model.playlist.PlaylistInfo;
import youtube_lib.downloader.model.subtitles.SubtitlesInfo;

import java.util.List;

public interface Parser {

    /* Video */

    Response<VideoInfo> parseVideo(RequestVideoInfo request);

    /* Playlist */

    Response<PlaylistInfo> parsePlaylist(RequestPlaylistInfo request);

    /* Channel uploads */
    Response<PlaylistInfo> parseChannelsUploads(RequestChannelUploads request);

    /* Subtitles */

    Response<List<SubtitlesInfo>> parseSubtitlesInfo(RequestSubtitlesInfo request);
}
