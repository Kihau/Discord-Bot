package bot.music;

import javax.sound.sampled.AudioInputStream;

//result wrapper
public class AudioConversionResult{
    protected byte[] bytes = null;
    protected AudioInputStream audioInputStream;

    protected AudioConversionResult(byte[] bytes, AudioInputStream audioInputStream){
        this.bytes = bytes;
        this.audioInputStream = audioInputStream;
    }
    protected AudioConversionResult(AudioInputStream audioInputStream){
        this.audioInputStream = audioInputStream;
    }

    private AudioConversionResult(){
    }
}
