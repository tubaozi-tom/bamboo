package org.bamboo.util;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Created by Tom on 2017/6/17.
 */
public class HttpUtil {
    static SSLConnectionSocketFactory sslsf = null;
    static SSLContextBuilder builder = null;
    static{
        try {
            builder = new SSLContextBuilder();
            // 全部信任 不做身份鉴定
            builder.loadTrustMaterial(null, new TrustStrategy() {
               // @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            });
            sslsf = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
//            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
//                    .register("http", new PlainConnectionSocketFactory())
//                    .register("https", sslsf)
//                    .build();
//            cm = new PoolingHttpClientConnectionManager(registry);
//            cm.setMaxTotal(200);//max connection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static String httpGet(String url){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            try {
                if(response.getStatusLine().getStatusCode()==200){
                    return EntityUtils.toString(response.getEntity());
                }else{
                    return getErrorResponse(response);
                }
            } finally {
                response.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String httpGet(String url,Map<String,String> header){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            setHttpGetHeader(header, httpGet);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            try {
                if(response.getStatusLine().getStatusCode()==200){
                    return EntityUtils.toString(response.getEntity());
                }else{
                    return getErrorResponse(response);
                }
            } finally {
                response.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String httpsGet(String url){
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            try {
                if(response.getStatusLine().getStatusCode()==200){
                    return EntityUtils.toString(response.getEntity(),"utf-8");
                }else{
                    return getErrorResponse(response);
                }
            } finally {
                response.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String httpsGet(String url,Map<String,String> header){
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
            HttpGet httpGet = new HttpGet(url);
            setHttpGetHeader(header, httpGet);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            try {
                if(response.getStatusLine().getStatusCode()==200){
                    return EntityUtils.toString(response.getEntity());
                }else{
                    return getErrorResponse(response);
                }
            } finally {
                response.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String httpPost(String url,String json) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            HttpEntity entity=new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            CloseableHttpResponse response = httpclient.execute(httpPost);

            try {
                if(response.getStatusLine().getStatusCode()==200){
                    return EntityUtils.toString(response.getEntity());
                }else{
                    return getErrorResponse(response);
                }

            } finally {
                response.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String httpPost(String url,Map<String,String> header,String json) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            setHttpPostHeader(header,httpPost);
            HttpEntity entity=new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            CloseableHttpResponse response = httpclient.execute(httpPost);

            try {
                if(response.getStatusLine().getStatusCode()==200){
                    return EntityUtils.toString(response.getEntity());
                }else{
                    return getErrorResponse(response);
                }

            } finally {
                response.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String httpPost(String url,Map<String,String> param) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);

            //装填参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if(param!=null){
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            //设置参数到请求对象中
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            CloseableHttpResponse response = httpclient.execute(httpPost);

            try {
                if(response.getStatusLine().getStatusCode()==200){
                    return EntityUtils.toString(response.getEntity());
                }else{
                    return getErrorResponse(response);
                }

            } finally {
                response.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
    public static String httpPost(String url,Map<String,String> header,Map<String,String> param) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            setHttpPostHeader(header,httpPost);
            //装填参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if(param!=null){
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            //设置参数到请求对象中
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            CloseableHttpResponse response = httpclient.execute(httpPost);

            try {
                if(response.getStatusLine().getStatusCode()==200){
                    return EntityUtils.toString(response.getEntity());
                }else{
                    return getErrorResponse(response);
                }

            } finally {
                response.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String httpsPost(String url,String json) {

        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
            HttpPost httpPost = new HttpPost(url);
            HttpEntity entity=new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            CloseableHttpResponse response = httpclient.execute(httpPost);

            try {
                if(response.getStatusLine().getStatusCode()==200){
                    return EntityUtils.toString(response.getEntity());
                }else{
                    return getErrorResponse(response);
                }

            } finally {
                response.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String httpsPost(String url,Map<String,String> header,String json) {

        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
            HttpPost httpPost = new HttpPost(url);
            setHttpPostHeader(header,httpPost);
            HttpEntity entity=new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            CloseableHttpResponse response = httpclient.execute(httpPost);

            try {
                if(response.getStatusLine().getStatusCode()==200){
                    return EntityUtils.toString(response.getEntity());
                }else{
                    return getErrorResponse(response);
                }

            } finally {
                response.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
    private static void setHttpGetHeader(Map<String, String> header, HttpGet httpGet) {
        if(header!=null&&!header.isEmpty()){
            for(String key:header.keySet()){
                httpGet.setHeader(key,header.get(key));
            }
        }
    }
    private static void setHttpPostHeader(Map<String, String> header, HttpPost httpPost) {
        if(header!=null&&!header.isEmpty()){
            for(String key:header.keySet()){
                httpPost.setHeader(key,header.get(key));
            }
        }
    }

    private static String getErrorResponse(CloseableHttpResponse response) {
        return "{\"resultCode\":200,\"errorDescription:\":\""+response.getStatusLine()+"\"}";
    }


    public static void main(String[] args) throws Exception {
        for(int i=0;i<100;i++) {
            new Thread(() -> {
                while (true) {
                    String url = "https://flight.qunar.com/twell/flight/inter/search?depCity=%E5%8C%97%E4%BA%AC&arrCity=%E5%A4%9A%E4%BC%A6%E5%A4%9A&depDate=2017-06-23&adultNum=1&childNum=0&ex_track=&from=qunarindex&queryId=10.88.168.220%3Al%3A-76c5ecd%3A15cb48d46db%3A-4beb&es=KPu9zMMmXz9Yjc26QQh%2BjMOmXB29oMl6KZhYXffsYMu9oflK%7C1497686317545";

                    String result = HttpUtil.httpsGet(url);
                }
            }).start();
        }

        for (int i = 0; i < 1000; i++) {
            String url = "https://flight.qunar.com/twell/flight/inter/search?depCity=%E5%8C%97%E4%BA%AC&arrCity=%E5%A4%9A%E4%BC%A6%E5%A4%9A&depDate=2017-06-23&adultNum=1&childNum=0&ex_track=&from=qunarindex&queryId=10.88.168.220%3Al%3A-76c5ecd%3A15cb48d46db%3A-4beb&es=KPu9zMMmXz9Yjc26QQh%2BjMOmXB29oMl6KZhYXffsYMu9oflK%7C1497686317545";
            String result = HttpUtil.httpsGet(url);
            System.out.println(result + "######" + i);
        }
    }
}
