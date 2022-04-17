package cn.wqy;

import cn.wqy.ReciteEnglishWords.Search;

import java.io.IOException;
import java.util.Objects;

public class Test6 {
    public static void main(String[] args) throws IOException {
        String word = "good";
        System.out.println("\u1000");
        System.out.println(Objects.requireNonNull(Search.search(word, true)).getTranslation());
    }
}
/*
 {"errorCode":0,
  "translateResult":[[{"tgt":"可笑的书",
                       "src":"ridiculous book"}]],
  "type":"EN2ZH_CN",
  "elapsedTime":5}

  {"errorCode":0,
   "translateResult":[[{"tgt":"极",
                        "src":"extremely"}]],
   "type":"EN2ZH_CN",
   "elapsedTime":0}

 */
