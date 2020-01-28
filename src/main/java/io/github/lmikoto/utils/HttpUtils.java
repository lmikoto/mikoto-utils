package io.github.lmikoto.utils;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public abstract class HttpUtils {

    public static String get(String path, Map<String,String> param){
        List<NameValuePair> parametersBody = Lists.newArrayListWithExpectedSize(param.size());
        for (Map.Entry<String,String> entry: param.entrySet()){
            parametersBody.add(new BasicHeader(entry.getKey(),entry.getValue()));
        }
        return get(path,parametersBody);
    }

    public static String get(URIBuilder uriBuilder){
        HttpGet get = null;
        try {
            get = new HttpGet(uriBuilder.build());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpClient client = HttpClientBuilder.create().build();
        try {
            HttpResponse response = client.execute(get);
            int code = response.getStatusLine().getStatusCode();
            if (code >= 400)
                throw new RuntimeException((new StringBuilder()).append("Could not access protected resource. Server returned http code: ").append(code).toString());
            return EntityUtils.toString(response.getEntity());
        }
        catch (ClientProtocolException e) {
            throw new RuntimeException("postRequest -- Client protocol exception!", e);
        }
        catch (IOException e) {
            throw new RuntimeException("postRequest -- IO error!", e);
        }
        finally {
            get.releaseConnection();
        }
    }

    public static String get(String path, List<NameValuePair> parametersBody) {
        URIBuilder uriBuilder = null;
        try {
            uriBuilder = new URIBuilder(path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        uriBuilder.setParameters(parametersBody);
        return get(uriBuilder);
    }

    public static String get(String path) {
        URIBuilder uriBuilder = null;
        try {
            uriBuilder = new URIBuilder(path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return get(uriBuilder);
    }

    public static String post(String path, List<NameValuePair> parametersBody) {
        HttpEntity entity = new UrlEncodedFormEntity(parametersBody, Charsets.UTF_8);
        return post(path, "application/x-www-form-urlencoded", entity);
    }

    public static String post(String path, String json) {
        StringEntity entity = new StringEntity(json, Charsets.UTF_8);
        return post(path, "application/json", entity);
    }

    public static String post(String path, String json,Map<String,String> header) {
        StringEntity entity = new StringEntity(json, Charsets.UTF_8);
        return post(path, "application/json", entity,header);
    }

    public static String post(String path,Object object){
        return post(path,JacksonUtils.toJson(object));
    }

    public static String post(String path,Object object,Map<String,String> header){
        return post(path,JacksonUtils.toJson(object),header);
    }

    public static String post(String path, String mediaType, HttpEntity entity) throws RuntimeException {
        return post(path,mediaType,entity, Maps.newHashMap());
    }

    public static String post(String path, String mediaType, HttpEntity entity,Map<String,String> headers) throws RuntimeException {
        final HttpPost post = new HttpPost(path);
        post.addHeader("Content-Type", mediaType);
        post.addHeader("Accept", "application/json");
        headers.forEach((k,v)-> {
            post.addHeader(k,v);
        });
        post.setEntity(entity);
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(post);
            int code = response.getStatusLine().getStatusCode();
            if (code >= 400)
                throw new RuntimeException(EntityUtils.toString(response.getEntity()));
            return EntityUtils.toString(response.getEntity());
        }
        catch (ClientProtocolException e) {
            throw new RuntimeException("postRequest -- Client protocol exception!", e);
        }
        catch (IOException e) {
            throw new RuntimeException("postRequest -- IO error!", e);
        }
        finally {
            post.releaseConnection();
        }
    }

}
