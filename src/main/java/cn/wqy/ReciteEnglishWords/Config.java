package cn.wqy.ReciteEnglishWords;

import cn.wqy.IOUtils.MyIOUtils;
import cn.wqy.OpenWindow;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class Config {

    private static final File CONFIG_FILE;

    private static final Document DOCUMENT;

    private static final Element ROOT;

    static {
        File configFile = null;
        try {
            configFile = new File(new File(OpenWindow.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getAbsolutePath() + "\\conf\\reciteEnglishWordSetting.xml");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        assert configFile != null;
        if (!configFile.getParentFile().exists() && !configFile.getParentFile().mkdirs()) try {
            throw new IOException("不能创建conf文件夹");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (!configFile.exists() ) if (!configFile.createNewFile())
                throw new IOException("不能创建reciteEnglishWordSetting.xml文件");
            else {
                FileOutputStream fos = new FileOutputStream(configFile);
                fos.write(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<settings>\n" +
                        "    <editWords>\n" +
                        "        <APIType>freeAPI</APIType>\n" +
                        "        <notFreeAPI>\n" +
                        "            <appKey></appKey>\n" +
                        "            <appSecret></appSecret>\n" +
                        "        </notFreeAPI>\n" +
                        "        <freeAPI/>\n" +
                        "        <translationType>self</translationType>\n" +
                        "    </editWords>\n" +
                        "    <traverseWords>\n" +
                        "        <traverseType>random</traverseType>\n" +
                        "    </traverseWords>\n" +
                        "    <reciteWords>\n" +
                        "        <pronunciationType>English</pronunciationType>\n" +
                        "    </reciteWords>\n" +
                        "</settings>").getBytes(StandardCharsets.UTF_8));
                MyIOUtils.close(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document document = null;
        try {
            document = new SAXReader().read(new FileInputStream(configFile));
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
        assert document != null;
        CONFIG_FILE = configFile;
        DOCUMENT = document;
        ROOT = document.getRootElement();
    }

    public static boolean isFreeAPI(){
        return "freeAPI".equals(ROOT.element("editWords").element("APIType").getText());
    }

    public static void setFreeAPI(boolean isFree){
        ROOT.element("editWords").element("APIType").setText(isFree ? "freeAPI" : "notFreeAPI");
    }

    public static String getAppKey(){
        return ROOT.element("editWords").element("notFreeAPI").element("appKey").getText();
    }

    public static String getAppSecret(){
        return ROOT.element("editWords").element("notFreeAPI").element("appSecret").getText();
    }

    public static void setAppKey(String appKey){
        ROOT.element("editWords").element("notFreeAPI").element("appKey").setText(appKey);
    }

    public static void setAppSecret(String appSecret){
        ROOT.element("editWords").element("notFreeAPI").element("appSecret").setText(appSecret);
    }

    public static boolean isRandomTraverse(){
        return "random".equals(ROOT.element("traverseWords").element("traverseType").getText());
    }

    public static void setRandomTraverse(boolean isRandom){
        ROOT.element("traverseWords").element("traverseType").setText(isRandom ? "random" : "order");
    }

    public static boolean isEnglishType(){
        return "English".equals(ROOT.element("reciteWords").element("pronunciationType").getText());
    }

    public static void setEnglishType(boolean isEnglishType){
        ROOT.element("reciteWords").element("pronunciationType").setText(isEnglishType ? "English" : "American");
    }

    public static void setSelfType(boolean isSelf){
        ROOT.element("editWords").element("translationType").setText(isSelf ? "self" : "official");
    }

    public static boolean isSelfType(){
        return "self".equals(ROOT.element("editWords").element("translationType").getText());
    }

    public static void applySettings(){
        XMLWriter writer = null;
        try {
            writer = new XMLWriter(new FileWriter(CONFIG_FILE));
            writer.write(DOCUMENT);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

