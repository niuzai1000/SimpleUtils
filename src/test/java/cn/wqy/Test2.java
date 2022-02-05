package cn.wqy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.*;

public class Test2 {
    public static void main(String[] args) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        String word = "werfasd";

        File mp3 = new File("E:\\TestFolder\\temp.mp3");
        File html = new File("E:\\TestFolder\\temp.html");
        if (mp3.exists()) mp3.delete();
        mp3.createNewFile();
        if (html.exists()) html.delete();
        html.createNewFile();


        HttpGet httpGet = new HttpGet("https://dict.youdao.com/dictvoice?audio=" + word + "&type=2");
        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        InputStream is = httpEntity.getContent();
        OutputStream os = new FileOutputStream("E:\\TestFolder\\temp.mp3");

        byte[] data = new byte[8388608];

        int read_count;
        while((read_count = is.read(data)) != -1) {
            os.write(data, 0, read_count);
        }

        os.flush();
        os.close();

//        HttpGet httpGet1 = new HttpGet("https://dict.youdao.com/w/" + word + "/");
//        HttpResponse httpResponse1 =  httpClient.execute(httpGet1);
//        HttpEntity httpEntity1 = httpResponse1.getEntity();
//        InputStream is1 = httpEntity1.getContent();
//        byte[] data1 = new byte[8388608];
//        OutputStream os1 = new FileOutputStream("E:\\TestFolder\\temp.html");
//
//        int read_count1;
//        while((read_count1 = is1.read(data1)) != -1) {
//            os1.write(data1, 0, read_count1);
//        }

//        Document document = Jsoup.parse(new File("E:\\TestFolder\\temp.html") , "UTF-8");
//        Elements elements = document.select("li");
//        for (Element element : elements) System.out.println(element);
//        Elements lastElements = document.select("[class=ptype_0 types]");
//        Element lastElt = null;
//        if (lastElements.size() > 0) lastElt = lastElements.get(0);
//        for (int i = 6 ; ; i++) {
//            String content = elements.get(i).html().replaceAll("&lt;" , "<").replaceAll("&gt;" , ">");
//            if (elements.get(i).equals(lastElt) || elements.get(i).html().contains("class=\"pos\"")) break;
//            System.out.println(content);
//        }





//        Player player = new Player(new FileInputStream("E:\\TestFolder\\temp.mp3"));
//        player.play();







    }

}
