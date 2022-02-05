package cn.wqy.ReciteEnglishWords.SearchWordOnline;

import cn.wqy.InformationDialog;
import cn.wqy.ReciteEnglishWords.Config;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Search {

    private static final String YOUDAO_URL_NOT_FREE = "https://openapi.youdao.com/api";

    private static final String YOUDAO_URL_FREE = "https://dict.youdao.com/dictvoice";

    public static SearchResult search(String word , boolean isFreeAPI) throws IOException {
        if (!isFreeAPI){
            String appKey = Config.getAppKey();
            Map<String,String> params = new HashMap<>();
            String salt = String.valueOf(System.currentTimeMillis());
            params.put("from", "en");
            params.put("to", "zh-CHS");
            params.put("signType", "v3");
            String curtime = String.valueOf(System.currentTimeMillis() / 1000);
            params.put("curtime", curtime);
            String signStr = appKey + truncate(word) + salt + curtime + Config.getAppSecret();
            String sign = getDigest(signStr);
            params.put("appKey", appKey);
            params.put("q", word);
            params.put("salt", salt);
            params.put("sign", sign);
            return requestForHttp(params);
        }else {
            return requestForPronunciation(word);
        }
    }

    private static SearchResult requestForHttp(Map<String, String> params) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(YOUDAO_URL_NOT_FREE);
        List<NameValuePair> paramsList = new ArrayList<>();
        for (Map.Entry<String, String> en : params.entrySet()) {
            paramsList.add(new BasicNameValuePair(en.getKey(), en.getValue()));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(paramsList,"UTF-8"));
        InformationDialog.INFO_DIALOG.setInfo("发送请求中...");
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        InformationDialog.INFO_DIALOG.setInfo("处理响应中...");
        try{
            HttpEntity httpEntity = httpResponse.getEntity();
            String json = EntityUtils.toString(httpEntity,"UTF-8");
            EntityUtils.consume(httpEntity);
            JSONObject jsonObj = new JSONObject(json);
            int errorCode = Integer.parseInt(jsonObj.getString("errorCode"));
            if (errorCode != 0){
                InformationDialog.INFO_DIALOG.setInfo("搜索失败");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                try{
                    throw new JSONException("搜索失败");
                }catch (JSONException e){
                    return null;
                }
            }
            JSONArray translation = jsonObj.getJSONArray("translation");
            JSONObject basic = jsonObj.getJSONObject("basic");
            String uk_phonetic = basic.getString("uk-phonetic");
            String us_phonetic = basic.getString("us-phonetic");
            String uk_speech_URL = basic.getString("uk-speech");
            String us_speech_URL = basic.getString("us-speech");
            JSONArray explains = basic.getJSONArray("explains");
            InformationDialog.INFO_DIALOG.setInfo("搜索成功");
            return new SearchResult(errorCode , translation , uk_phonetic , us_phonetic , uk_speech_URL , us_speech_URL , explains);
        }catch (JSONException e){
            InformationDialog.INFO_DIALOG.setInfo("搜索失败");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            return null;
        }
        finally {
            try{
                if(httpResponse!=null){
                    httpResponse.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    private static SearchResult requestForPronunciation(String word){
        String uk_speech_URL = YOUDAO_URL_FREE + "?audio=" + word + "&type=1";
        String us_speech_URL = YOUDAO_URL_FREE + "?audio=" + word + "&type=2";
        uk_speech_URL = uk_speech_URL.replaceAll(" " , "%20");
        us_speech_URL = us_speech_URL.replaceAll(" " , "%20");
        return new SearchResult(0 , null , null , null , uk_speech_URL , us_speech_URL , null);
    }



    /**
     * 生成加密字段
     */
    private static String getDigest(String string) {
        if (string == null) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }


}
