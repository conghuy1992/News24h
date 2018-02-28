package news24.conghuy.com.news24h.common;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import news24.conghuy.com.news24h.R;
import news24.conghuy.com.news24h.model.XmlDto;

/**
 * Created by maidinh on 7/25/2017.
 */

public class Const {
    public static String TAG = "Const";
    public static String ABOUT_BLANK = "about:blank";
    public static String LIST_ADV = "LIST_ADV";

    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }

    public static void copyToClipBoard(Context context, String code) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copy", code);
        clipboard.setPrimaryClip(clip);
    }

    public static ArrayList<String> get_adv(Context context) {
        ArrayList<String> list = new ArrayList<>();
        String data;
        InputStream in = context.getResources().openRawResource(R.raw.advfile);
        InputStreamReader inreader = new InputStreamReader(in);
        BufferedReader bufreader = new BufferedReader(inreader);
//        StringBuilder builder = new StringBuilder();
        if (in != null) {
            try {
                while ((data = bufreader.readLine()) != null) {
//                    Log.d(TAG, "data:" + data);
//                    builder.append(data);
//                    builder.append("\n");
                    list.add(data);
                }
                in.close();
            } catch (IOException ex) {
                Log.e("ERROR", ex.getMessage());
            }
        }
        return list;
    }

    public static boolean isContains(ArrayList<String> list, String url) {
        int n = list.size();
        for (int i = 0; i < n; i++) {
            if (url.contains(list.get(i)))
                return true;
        }
        return false;
    }

    public static List<XmlDto> parseXml(String response) {
        List<XmlDto> xmlDtoList = new ArrayList<>();
        XmlDto xmlDto = new XmlDto();
        InputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(response.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        inputStream = new ByteArrayInputStream(response.getBytes());

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, "UTF-8");
            String nodeName = "";
            boolean insideItem = false;
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                eventType = parser.next();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:

                        nodeName = parser.getName();
                        if (nodeName.equals("item")) {
                            insideItem = true;
                        } else if (nodeName.equals("title") && insideItem) {
                            xmlDto = new XmlDto();
                            xmlDtoList.add(xmlDto);
                            xmlDto.title = parser.nextText().trim();
                        } else if (nodeName.equals("description") && insideItem) {
                            String str = parser.nextText().trim();
                            Document cddoc = getDomElement("<root>" + str + "</root>");

//                            NodeList alist = cddoc.getElementsByTagName("a");
//                            String shref = "";
//                            if (alist.getLength() != 0) {
//                                Node anode = alist.item(0);
//                                shref= anode.getAttributes().getNamedItem("href").getNodeValue();
//                                Log.d(TAG,"shref:"+shref);
//                            }

                            NodeList img = cddoc.getElementsByTagName("img");
                            String imgStr = "";
                            if (img.getLength() != 0) {
                                Node anode = img.item(0);
                                imgStr = anode.getAttributes().getNamedItem("src").getNodeValue();
//                                Log.d(TAG,"imgStr:"+imgStr);
                            }

                            xmlDto.description = imgStr;

//                            String result = str.substring(str.indexOf("src='") + 5, str.indexOf("jpg") + 3).trim();// substring to get avatar
//                            xmlDto.description = result;

                        } else if (nodeName.equals("pubDate") && insideItem) {
                            xmlDto.pubDate = parser.nextText().trim();
                        } else if (nodeName.equals("link") && insideItem) {
                            xmlDto.link = parser.nextText().trim();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        nodeName = parser.getName();
                        if (nodeName.equals("item")) {
                            insideItem = false;
                        }
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlDtoList;
    }

    public static Document getDomElement(String xml) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setCoalescing(true);
        dbf.setNamespaceAware(true);
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }

        return doc;
    }

    public static void setPictureFromURL(final Context context, String url, final ImageView iv) {
        if (url != null && url.trim().length() > 0) {
//            Log.d(TAG, "url:" + url);
            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            // call callback when loading error

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            // call callback when loading success

                            return false;
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                            int srcWidth = resource.getWidth();
//                            int srcHeight = resource.getHeight();
//                            int dstWidth = (int) (srcWidth * ratio);
//                            int dstHeight = (int) (srcHeight * ratio);

//                            Bitmap putImage = createScaledBitmap(resource, Const.getDimenInPx(context, R.dimen.location_w), Const.getDimenInPx(context, R.dimen.location_h), true);
//                            iv.setImageBitmap(putImage);

                            iv.setImageBitmap(resource);

                        }
                    });
        } else {
            iv.setImageResource(R.drawable.temp_img);
        }
    }
}
