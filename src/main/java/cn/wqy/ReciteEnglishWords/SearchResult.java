package cn.wqy.ReciteEnglishWords;

import cn.wqy.IOUtils.MyIOUtils;
import cn.wqy.InformationDialog;
import cn.wqy.OpenWindow;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.info.AudioInfo;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class SearchResult {

    private final int errorCode;

    private final ArrayList<String> translation;

    private final String uk_phonetic;

    private final String us_phonetic;

    private final ArrayList<String> explains;

    private final File uk_speech_File;

    private final File us_speech_File;

    public SearchResult(int errorCode, ArrayList<String> translation, String uk_phonetic, String us_phonetic, HttpPost uk_speech_Post, HttpPost us_speech_Post , ArrayList<String> explains) {
        InformationDialog.INFO_DIALOG.setInfo("初始化搜索结果中...");
        this.errorCode = errorCode;
        this.translation = translation;
        this.uk_phonetic = uk_phonetic;
        this.us_phonetic = us_phonetic;
        this.explains = explains;

        if (uk_speech_Post != null) {
            InformationDialog.INFO_DIALOG.setInfo("开始下载并转码音频...");
            uk_speech_File = download(uk_speech_Post);
        }
        else uk_speech_File = null;
        if (us_speech_Post != null) {
            InformationDialog.INFO_DIALOG.setInfo("开始下载并转码音频...");
            us_speech_File = download(us_speech_Post);
        }
        else us_speech_File = null;
    }

    private File download(HttpPost httpPost){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        InputStream is;
        OutputStream os = null;
        try {
            InformationDialog.INFO_DIALOG.setInfo("发送下载请求中...");
            httpResponse = httpClient.execute(httpPost);
            is = httpResponse.getEntity().getContent();
            File parentFile = new File(OpenWindow.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
            File temp = new File(parentFile.getAbsoluteFile() + "\\temp");
            if (!temp.exists()) if (!temp.mkdirs()) throw new IOException("无法创建temp文件夹");
            File downloadFile = new File(temp.getAbsoluteFile() + "\\" + System.currentTimeMillis() + ".mp3");
            os = new FileOutputStream(downloadFile);
            InformationDialog.INFO_DIALOG.setInfo("下载音频中...");
            byte[] data = new byte[1024 * 512 * 16];
            int read_count;
            while((read_count = is.read(data)) != -1){
                os.write(data , 0 , read_count);
            }

            return transcode(downloadFile);
        } catch (IOException | URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try{
                if(httpResponse!=null){
                    httpResponse.close();
                }
                MyIOUtils.close(os);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    private File transcode(File sourceFile) throws IOException, InterruptedException {
        InformationDialog.INFO_DIALOG.setInfo("转码音频中...");
        File target = new File(sourceFile.getAbsolutePath().replace(".mp3" , ".wav"));
        MultimediaObject source = new MultimediaObject(sourceFile);
        AudioInfo audioInfo;
        AudioAttributes audio;
        EncodingAttributes attrs = new EncodingAttributes();
        try {
            audio = new AudioAttributes();
            audioInfo = source.getInfo().getAudio();
            audio.setBitRate(audioInfo.getBitRate());
            audio.setChannels(audioInfo.getChannels());
            audio.setSamplingRate(audioInfo.getSamplingRate());
            attrs.setOutputFormat("wav");
            attrs.setAudioAttributes(audio);
        } catch (EncoderException encoderException) {
            encoderException.printStackTrace();
        }
        Encoder encoder = new Encoder();
        AtomicBoolean interrupted = new AtomicBoolean(false);
        AtomicBoolean throwException = new AtomicBoolean(false);
        try {
            Thread thread = new Thread(new Runnable() {
                private long beforeLength = 0;
                private boolean finished = false;
                @Override
                public void run() {
                    interrupted.set(true);
                    while (!finished){
                        Thread.yield();
                        if (beforeLength != target.length()) beforeLength = target.length();
                        else finished = true;
                    }
                    if (!sourceFile.delete()) throwException.set(true);
                    interrupted.set(false);
                }
            });
            thread.start();
            encoder.encode(source, target, attrs);
        } catch (EncoderException encoderException) {
            encoderException.printStackTrace();
        }
        while (interrupted.get()) {
            Thread.yield();
            if (throwException.get()) throw new IOException("无法删除源文件");
        }

        return target;

    }

    public int getErrorCode() {
        return errorCode;
    }

    public ArrayList<String> getTranslation() {
        return translation;
    }

    public String getUk_phonetic() {
        return uk_phonetic;
    }

    public String getUs_phonetic() {
        return us_phonetic;
    }

    public File getUk_speech_File() {
        return uk_speech_File;
    }

    public File getUs_speech_File() {
        return us_speech_File;
    }

    public ArrayList<String> getExplains() {
        return explains;
    }
}
