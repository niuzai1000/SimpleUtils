package cn.wqy.ReciteEnglishWords;

import cn.wqy.InformationDialog;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Search {

    private static final String YOUDAO_URL_NOT_FREE = "https://openapi.youdao.com/api";

    private static final String YOUDAO_URL_PRONUNCIATION_FREE = "https://dict.youdao.com/dictvoice";

    private static final String YOUDAO_URL_TRANSLATION_FREE = "https://fanyi.youdao.com/translate";

    public static SearchResult search(String english, boolean isFreeAPI) throws IOException {
        english = english.trim();
        if (!english.matches("^(\\b[a-zA-Z]+\\s*\\b)+$")) {
            InformationDialog.INFO_DIALOG.setInfo("请正确输入英文（不包括标点符号）");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        if (!isFreeAPI) return requestForNotFree(english);
        else return requestForFree(english);
    }

    private static SearchResult requestForNotFree(String english) throws IOException {
        String appKey = Config.getAppKey();
        Map<String,String> params = new HashMap<>();
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("from", "en");
        params.put("to", "zh-CHS");
        params.put("signType", "v3");
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        params.put("curtime", curtime);
        String signStr = appKey + truncate(english) + salt + curtime + Config.getAppSecret();
        String sign = getDigest(signStr);
        params.put("appKey", appKey);
        params.put("q", english);
        params.put("salt", salt);
        params.put("sign", sign);
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
            ArrayList<String> translation = new ArrayList<>();
            for (Object obj : jsonObj.getJSONArray("translation")) translation.add(obj.toString());
            if (english.matches("^[a-zA-z]+$")){
                JSONObject basic = jsonObj.getJSONObject("basic");
                String uk_phonetic = basic.getString("uk-phonetic");
                String us_phonetic = basic.getString("us-phonetic");
                String uk_speech_URL = basic.getString("uk-speech");
                String us_speech_URL = basic.getString("us-speech");
                ArrayList<String> explains = new ArrayList<>();
                for (Object obj : basic.getJSONArray("explains")) explains.add(obj.toString());
                InformationDialog.INFO_DIALOG.setInfo("搜索成功");
                return new SearchResult(0 , translation , uk_phonetic , us_phonetic , new HttpPost(uk_speech_URL) , new HttpPost(us_speech_URL) , explains);
            }else{
                InformationDialog.INFO_DIALOG.setInfo("搜索成功");
                return new SearchResult(0 , translation , null , null , getUk_speech_Post(english) , getUs_speech_Post(english) , null);
            }
        }catch (JSONException e){
            InformationDialog.INFO_DIALOG.setInfo("搜索失败");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            return null;
        }finally {
            try{
                if(httpResponse != null){
                    httpResponse.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    private static SearchResult requestForFree(String english) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(YOUDAO_URL_TRANSLATION_FREE);
        List<NameValuePair> paramsList = new ArrayList<>();
        paramsList.add(new BasicNameValuePair("doctype" , "json"));
        paramsList.add(new BasicNameValuePair("type" , "AUTO"));
        paramsList.add(new BasicNameValuePair("i" , english));
        httpPost.setEntity(new UrlEncodedFormEntity(paramsList));
        InformationDialog.INFO_DIALOG.setInfo("发送请求中...");
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        InformationDialog.INFO_DIALOG.setInfo("处理响应中...");
        HttpEntity httpEntity = httpResponse.getEntity();
        String json = EntityUtils.toString(httpEntity);
        EntityUtils.consume(httpEntity);
        JSONObject jsonObj = new JSONObject(json);
        int errorCode = jsonObj.getInt("errorCode");
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
        String trans = jsonObj.getJSONArray("translateResult").getJSONArray(0).getJSONObject(0).getString("tgt");
        ArrayList<String> translation = new ArrayList<>();
        translation.add(trans);
        return new SearchResult(0 , translation , null , null , getUk_speech_Post(english) , getUs_speech_Post(english) , null);
    }

    private static HttpPost getUk_speech_Post(String english) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(YOUDAO_URL_PRONUNCIATION_FREE);
        List<NameValuePair> paramsList = new ArrayList<>();
        paramsList.add(new BasicNameValuePair("audio" , english));
        paramsList.add(new BasicNameValuePair("type" , "1"));
        httpPost.setEntity(new UrlEncodedFormEntity(paramsList));
        return httpPost;
    }

    private static HttpPost getUs_speech_Post(String english) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(YOUDAO_URL_PRONUNCIATION_FREE);
        List<NameValuePair> paramsList = new ArrayList<>();
        paramsList.add(new BasicNameValuePair("audio" , english));
        paramsList.add(new BasicNameValuePair("type" , "2"));
        httpPost.setEntity(new UrlEncodedFormEntity(paramsList));
        return httpPost;
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
