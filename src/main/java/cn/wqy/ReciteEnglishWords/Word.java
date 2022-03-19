package cn.wqy.ReciteEnglishWords;

import java.io.File;
import java.util.ArrayList;

public class Word {

    private final String word;

    private ArrayList<String> translation = new ArrayList<>();

    private final String uk_phonetic;

    private final String us_phonetic;

    private final ArrayList<String> explains = new ArrayList<>();

    private final File uk_speech_File;

    private final File us_speech_File;

    public Word(String word, ArrayList<String> translation , File uk_speech_File , File us_speech_File) {
        this.word = word;
        this.translation = translation;
        this.uk_phonetic = null;
        this.us_phonetic = null;
        this.uk_speech_File = uk_speech_File;
        this.us_speech_File = us_speech_File;
    }

    public Word(String word, SearchResult result) {
        this.word = word;
        this.uk_phonetic = result.getUk_phonetic();
        this.us_phonetic = result.getUs_phonetic();
        this.uk_speech_File = result.getUk_speech_File();
        this.us_speech_File = result.getUs_speech_File();
        if (result.getTranslation() != null) for (Object tran : result.getTranslation()) translation.add(tran.toString());
        if (result.getExplains() != null) for (Object exp : result.getExplains()) explains.add(exp.toString());
    }

    public String getWord() {
        return word;
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

    public ArrayList<String> getExplains() {
        return explains;
    }

    public File getUk_speech_File() {
        return uk_speech_File;
    }

    public File getUs_speech_File() {
        return us_speech_File;
    }

}
