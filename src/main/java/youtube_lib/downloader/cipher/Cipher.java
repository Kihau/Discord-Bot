package youtube_lib.downloader.cipher;


public interface Cipher {

    String getSignature(String cipheredSignature);
}
