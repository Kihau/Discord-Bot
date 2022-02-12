package youtube_lib.downloader.cipher;


public interface CipherFunction {

    char[] apply(char[] array, String argument);
}
