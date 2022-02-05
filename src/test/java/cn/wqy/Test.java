package cn.wqy;

import org.apache.http.Header;
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
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class Test {

    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    private static final String YOUDAO_URL = "https://openapi.youdao.com/api";

    private static final String APP_KEY = "5447135ba17c1094";

    private static final String APP_SECRET = "jmqlsMLs4YImaNpWwneUV4hA5NQtTaPC";

    public static void main(String[] args) throws IOException {

        Map<String,String> params = new HashMap<>();
        String q = "good";
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("from", "en");
        params.put("to", "zh-CHS");
        params.put("signType", "v3");
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        params.put("curtime", curtime);
        String signStr = APP_KEY + truncate(q) + salt + curtime + APP_SECRET;
        String sign = getDigest(signStr);
        params.put("appKey", APP_KEY);
        params.put("q", q);
        params.put("salt", salt);
        params.put("sign", sign);
//        params.put("vocabId","您的用户词表ID");
        /** 处理结果 */
        requestForHttp(YOUDAO_URL,params);
    }

    public static void requestForHttp(String url,Map<String,String> params) throws IOException {

        /** 创建HttpClient */
        CloseableHttpClient httpClient = HttpClients.createDefault();

        /** httpPost */
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> paramsList = new ArrayList<>();
        for (Map.Entry<String, String> en : params.entrySet()) {
            String key = en.getKey();
            String value = en.getValue();
            paramsList.add(new BasicNameValuePair(key, value));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(paramsList,"UTF-8"));
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        try{
            Header[] contentType = httpResponse.getHeaders("Content-Type");
            logger.info("Content-Type:" + contentType[0].getValue());
            HttpEntity httpEntity = httpResponse.getEntity();
            String json = EntityUtils.toString(httpEntity,"UTF-8");
            EntityUtils.consume(httpEntity);
            logger.info(json);
            System.out.println(json);
            JSONObject jsonObj = new JSONObject(json);
            JSONArray translations = jsonObj.getJSONArray("translation");
            JSONArray explains = jsonObj.getJSONObject("basic").getJSONArray("explains");
            System.out.println(translations);
            System.out.println(explains);
        }finally {
            try{
                if(httpResponse!=null){
                    httpResponse.close();
                }
            }catch(IOException e){
                logger.info("## release resouce error ##" + e);
            }
        }
    }

    /**
     * 生成加密字段
     */
    public static String getDigest(String string) {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
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

    /**
     *
     * @param result 音频字节流
     * @param filePath 存储路径
     */
    private static void byte2File(byte[] result, String filePath) {
        File audioFile = new File(filePath);
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(audioFile);
            fos.write(result);

        }catch (Exception e){
            logger.info(e.toString());
        }finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }
}
/*
文本翻译服务
文本翻译 API 简介
概念解释

文本翻译：将一段源语言文本转换成目标语言文本，可根据语言参数的不同实现多国语言之间的互译。
说明

Hi，您好，欢迎使用有道智云文本翻译API接口服务。如果您想快速体验服务，建议您前往 翻译体验中心 或者在体验中心右下侧找到小程序二维码，扫描进行体验。

本文档主要针对需要集成HTTP API的技术开发工程师，详细描述文本翻译能力相关的技术内容。

如果您有与我们商务合作的需求，可以通过以下方式联系我们：

商务邮箱： AIcloud_Business@corp.youdao.com

如果您对文档内容有任何疑问，可以通过以下几种方式联系我们：

客服QQ：1906538062

智云翻译技术交流QQ 1群: 652880659

智云翻译技术交流QQ 2群: 669384425

智云翻译技术交流QQ 3群: 807539209

智云翻译技术交流QQ 4群: 936752411

联系邮箱： zhiyun@corp.youdao.com

温馨提示：

    本文档主要针对开发人员，接入测试前需要获取 应用ID 和 应用密钥 ，并绑定实例；如果您还没有，请按照 新手指南 获取。
    平台向每个账户赠送50元的体验金，供用户集成前测试所用，具体资费规则详见 文本翻译服务报价 。

接口说明

文本翻译API接口提供有道的翻译服务，包含了中英翻译和小语种翻译功能。您只需要通过调用文本翻译API，传入待翻译的内容，并指定要翻译的源语言（支持源语言语种自动检测）和目标语言种类，就可以得到相应的翻译结果。

文本翻译API HTTPS地址：

    https://openapi.youdao.com/api

协议须知

调用方在集成文本翻译API时，请遵循以下规则。
规则	描述
传输方式	HTTPS
请求方式	GET/POST
字符编码	统一使用UTF-8 编码
请求格式	表单
响应格式	JSON
接口调用参数

调用API需要向接口发送以下字段来访问服务。
字段名	类型	含义	必填	备注
q	text	待翻译文本	True	必须是UTF-8编码
from	text	源语言	True	参考下方 支持语言 (可设置为auto)
to	text	目标语言	True	参考下方 支持语言 (可设置为auto)
appKey	text	应用ID	True	可在 应用管理 查看
salt	text	UUID	True	uuid，唯一通用识别码
sign	text	签名	True	sha256(应用ID+input+salt+curtime+应用密钥)
signType	text	签名类型	True	v3
curtime	text	当前UTC时间戳(秒)	true	TimeStamp
ext	text	翻译结果音频格式，支持mp3	false	mp3
voice	text	翻译结果发音选择	false	0为女声，1为男声。默认为女声
strict	text	是否严格按照指定from和to进行翻译：true/false	false	如果为false，则会自动中译英，英译中。默认为false
vocabId	text	用户上传的词典	false	用户指定的词典 out_id，目前支持英译中

    签名生成方法如下：
    signType=v3；
    sign=sha256(应用ID+input+salt+curtime+应用密钥)；
    其中，input的计算方式为：input=q前10个字符 + q长度 + q后10个字符（当q长度大于20）或 input=q字符串（当q长度小于等于20）；

注意：

    voice 没有男声的，会输出女声。
    发音需要在控制台创建tts实例，并绑定应用才能使用，否则点击发音会报110错误。
    接口salt+curtime来防重放（即一个请求不可以被请求2次），所以salt最好为UUID。

    不同语言获取时间戳，请参看此链接

如果对签名有疑问，可以参看各语言demo。
用户词典使用

登录控制台，选择文本翻译服务，点击右侧的术语表，选择新建，填写表名称和语言方向，添加需要的术语表，然后获取对应词表id即可。
输出结果

返回的结果是json格式，包含字段与FROM和TO的值有关，具体说明如下：
字段名	类型	含义	备注
errorCode	text	错误返回码	一定存在
query	text	源语言	查询正确时，一定存在
translation	Array	翻译结果	查询正确时，一定存在
basic	text	词义	基本词典，查词时才有
web	Array	词义	网络释义，该结果不一定存在
l	text	源语言和目标语言	一定存在
dict	text	词典deeplink	查询语种为支持语言时，存在
webdict	text	webdeeplink	查询语种为支持语言时，存在
tSpeakUrl	text	翻译结果发音地址	翻译成功一定存在，需要应用绑定语音合成实例才能正常播放
否则返回110错误码
speakUrl	text	源语言发音地址	翻译成功一定存在，需要应用绑定语音合成实例才能正常播放
否则返回110错误码
returnPhrase	Array	单词校验后的结果	主要校验字母大小写、单词前含符号、中文简繁体

注：

a. 中文查词的basic字段只包含explains字段。

b. 英文查词的basic字段中又包含以下字段。
字段	含义
us-phonetic	美式音标，英文查词成功，一定存在
phonetic	默认音标，默认是英式音标，英文查词成功，一定存在
uk-phonetic	英式音标，英文查词成功，一定存在
uk-speech	英式发音，英文查词成功，一定存在
us-speech	美式发音，英文查词成功，一定存在
explains	基本释义
示例

使用good单词查询作为示例进行说明：

输出结果与FROM和TO的值有关：

1. 当FROM和TO的值都在{zh-CHS, EN}范围内时

{
  "errorCode":"0",
  "query":"good", //查询正确时，一定存在
  "translation": [ //查询正确时一定存在
      "好"
  ],
  "basic":{ // 有道词典-基本词典,查词时才有
      "phonetic":"gʊd",
      "uk-phonetic":"gʊd", //英式音标
      "us-phonetic":"ɡʊd", //美式音标
      "uk-speech": "XXXX",//英式发音
      "us-speech": "XXXX",//美式发音
      "explains":[
          "好处",
          "好的",
          "好",
      ]
  },
  "web":[ // 有道词典-网络释义，该结果不一定存在
      {
          "key":"good",
          "value":["良好","善","美好"]
      },
      {...}
  ],
  "dict":{
      "url":"yddict://m.youdao.com/dict?le=eng&q=good"
  },
  "webdict":{
      "url":"http://m.youdao.com/dict?le=eng&q=good"
  },
  "l":"EN2zh-CHS",
  "tSpeakUrl":"XXX",//翻译后的发音地址
  "speakUrl": "XXX" //查询文本的发音地址
}

2. 当FROM和TO的值有在{zh-CHS, EN}范围外的时候

{
   "errorCode": "0",
   "translation": ["大丈夫です"], //小语种翻译，一定存在
   "dict":{
       "url":"yddict://m.youdao.com/dict?le=jap&q=%E6%B2%A1%E5%85%B3%E7%B3%BB%E3%80%82"
   },
   "webdict":{
       "url":"http://m.youdao.com/dict?le=jap&q=%E6%B2%A1%E5%85%B3%E7%B3%BB%E3%80%82"
   },
   "l":"zh-CHS2ja",
   "tSpeakUrl":"XXX",//翻译后的发音地址
   "speakUrl": "XXX" //查询文本的发音地址
}

支持语言

下表为各语言对应代码：
语言	代码
中文	zh-CHS
英文	en
日文	ja
韩文	ko
法文	fr
西班牙文	es
葡萄牙文	pt
意大利文	it
俄文	ru
越南文	vi
德文	de
阿拉伯文	ar
印尼文	id
南非荷兰语	af
波斯尼亚语	bs
保加利亚语	bg
粤语	yue
加泰隆语	ca
克罗地亚语	hr
捷克语	cs
丹麦语	da
荷兰语	nl
爱沙尼亚语	et
斐济语	fj
芬兰语	fi
希腊语	el
海地克里奥尔语	ht
希伯来语	he
印地语	hi
白苗语	mww
匈牙利语	hu
斯瓦希里语	sw
克林贡语	tlh
拉脱维亚语	lv
立陶宛语	lt
马来语	ms
马耳他语	mt
挪威语	no
波斯语	fa
波兰语	pl
克雷塔罗奥托米语	otq
罗马尼亚语	ro
塞尔维亚语(西里尔文)	sr-Cyrl
塞尔维亚语(拉丁文)	sr-Latn
斯洛伐克语	sk
斯洛文尼亚语	sl
瑞典语	sv
塔希提语	ty
泰语	th
汤加语	to
土耳其语	tr
乌克兰语	uk
乌尔都语	ur
威尔士语	cy
尤卡坦玛雅语	yua
阿尔巴尼亚语	sq
阿姆哈拉语	am
亚美尼亚语	hy
阿塞拜疆语	az
孟加拉语	bn
巴斯克语	eu
白俄罗斯语	be
宿务语	ceb
科西嘉语	co
世界语	eo
菲律宾语	tl
弗里西语	fy
加利西亚语	gl
格鲁吉亚语	ka
古吉拉特语	gu
豪萨语	ha
夏威夷语	haw
冰岛语	is
伊博语	ig
爱尔兰语	ga
爪哇语	jw
卡纳达语	kn
哈萨克语	kk
高棉语	km
库尔德语	ku
柯尔克孜语	ky
老挝语	lo
拉丁语	la
卢森堡语	lb
马其顿语	mk
马尔加什语	mg
马拉雅拉姆语	ml
毛利语	mi
马拉地语	mr
蒙古语	mn
缅甸语	my
尼泊尔语	ne
齐切瓦语	ny
普什图语	ps
旁遮普语	pa
萨摩亚语	sm
苏格兰盖尔语	gd
塞索托语	st
修纳语	sn
信德语	sd
僧伽罗语	si
索马里语	so
巽他语	su
塔吉克语	tg
泰米尔语	ta
泰卢固语	te
乌兹别克语	uz
南非科萨语	xh
意第绪语	yi
约鲁巴语	yo
南非祖鲁语	zu
自动识别	auto

其中auto可以识别中文、英文、日文、韩文、法文、西班牙文、葡萄牙文、俄文、越南文、德文、阿拉伯文、印尼文、意大利文，其他语种无法识别，为提高准确率，请指定语种。
错误代码列表
错误码	含义
101	缺少必填的参数,首先确保必填参数齐全，然后确认参数书写是否正确。
102	不支持的语言类型
103	翻译文本过长
104	不支持的API类型
105	不支持的签名类型
106	不支持的响应类型
107	不支持的传输加密类型
108	应用ID无效，注册账号，登录后台创建应用和实例并完成绑定，可获得应用ID和应用密钥等信息
109	batchLog格式不正确
110	无相关服务的有效实例,应用没有绑定服务实例，可以新建服务实例，绑定服务实例。注：某些服务的翻译结果发音需要tts实例，需要在控制台创建语音合成实例绑定应用后方能使用。
111	开发者账号无效
112	请求服务无效
113	q不能为空
114	不支持的图片传输方式
116	strict字段取值无效，请参考文档填写正确参数值
201	解密失败，可能为DES,BASE64,URLDecode的错误
202	签名检验失败,如果确认应用ID和应用密钥的正确性，仍返回202，一般是编码问题。请确保翻译文本 q 为UTF-8编码.
203	访问IP地址不在可访问IP列表
205	请求的接口与应用的平台类型不一致，确保接入方式（Android SDK、IOS SDK、API）与创建的应用平台类型一致。如有疑问请参考入门指南
206	因为时间戳无效导致签名校验失败
207	重放请求
301	辞典查询失败
302	翻译查询失败
303	服务端的其它异常
304	会话闲置太久超时
401	账户已经欠费，请进行账户充值
402	offlinesdk不可用
411	访问频率受限,请稍后访问
412	长请求过于频繁，请稍后访问
1001	无效的OCR类型
1002	不支持的OCR image类型
1003	不支持的OCR Language类型
1004	识别图片过大
1201	图片base64解密失败
1301	OCR段落识别失败
1411	访问频率受限
1412	超过最大识别字节数
2003	不支持的语言识别Language类型
2004	合成字符过长
2005	不支持的音频文件类型
2006	不支持的发音类型
2201	解密失败
2301	服务的异常
2411	访问频率受限,请稍后访问
2412	超过最大请求字符数
3001	不支持的语音格式
3002	不支持的语音采样率
3003	不支持的语音声道
3004	不支持的语音上传类型
3005	不支持的语言类型
3006	不支持的识别类型
3007	识别音频文件过大
3008	识别音频时长过长
3009	不支持的音频文件类型
3010	不支持的发音类型
3201	解密失败
3301	语音识别失败
3302	语音翻译失败
3303	服务的异常
3411	访问频率受限,请稍后访问
3412	超过最大请求字符数
4001	不支持的语音识别格式
4002	不支持的语音识别采样率
4003	不支持的语音识别声道
4004	不支持的语音上传类型
4005	不支持的语言类型
4006	识别音频文件过大
4007	识别音频时长过长
4201	解密失败
4301	语音识别失败
4303	服务的异常
4411	访问频率受限,请稍后访问
4412	超过最大请求时长
5001	无效的OCR类型
5002	不支持的OCR image类型
5003	不支持的语言类型
5004	识别图片过大
5005	不支持的图片类型
5006	文件为空
5201	解密错误，图片base64解密失败
5301	OCR段落识别失败
5411	访问频率受限
5412	超过最大识别流量
9001	不支持的语音格式
9002	不支持的语音采样率
9003	不支持的语音声道
9004	不支持的语音上传类型
9005	不支持的语音识别 Language类型
9301	ASR识别失败
9303	服务器内部错误
9411	访问频率受限（超过最大调用次数）
9412	超过最大处理语音长度
10001	无效的OCR类型
10002	不支持的OCR image类型
10004	识别图片过大
10201	图片base64解密失败
10301	OCR段落识别失败
10411	访问频率受限
10412	超过最大识别流量
11001	不支持的语音识别格式
11002	不支持的语音识别采样率
11003	不支持的语音识别声道
11004	不支持的语音上传类型
11005	不支持的语言类型
11006	识别音频文件过大
11007	识别音频时长过长，最大支持30s
11201	解密失败
11301	语音识别失败
11303	服务的异常
11411	访问频率受限,请稍后访问
11412	超过最大请求时长
12001	图片尺寸过大
12002	图片base64解密失败
12003	引擎服务器返回错误
12004	图片为空
12005	不支持的识别图片类型
12006	图片无匹配结果
13001	不支持的角度类型
13002	不支持的文件类型
13003	表格识别图片过大
13004	文件为空
13301	表格识别失败
15001	需要图片
15002	图片过大（1M）
15003	服务调用失败
17001	需要图片
17002	图片过大（1M）
17003	识别类型未找到
17004	不支持的识别类型
17005	服务调用失败
 */
