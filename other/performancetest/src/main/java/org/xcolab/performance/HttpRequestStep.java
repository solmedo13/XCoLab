package org.xcolab.performance;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HttpRequestStep {

    private final String stepName;
    private Date startDateTime;
    private Date endDateTime;
    private Long elapsedTime;
    private final Map<String, String> parameterMap;
    private final String path;
    private final String serverUrl;
    private final String httpMethod;
    private final CloseableHttpClient httpClient;
    private final HttpContext context;
    private Integer httpResponseStatus;
    private Long contentLength;

    public static final String GET = "GET";
    public static final String POST = "POST";

    public HttpRequestStep(String serverUrl, String stepName, String path, String httpMethod, CloseableHttpClient httpClient, HttpContext context) {
        this.serverUrl = serverUrl;
        this.parameterMap = null;
        this.httpMethod = httpMethod;
        this.path = path;
        this.stepName = stepName;
        this.httpClient = httpClient;
        this.context = context;

    }

    public HttpRequestStep(String serverUrl, String stepName, String path, String httpMethod, Map<String, String> parameterMap, CloseableHttpClient httpClient, HttpContext context) {
        this.serverUrl = serverUrl;
        this.parameterMap = parameterMap;
        this.httpMethod = httpMethod;
        this.path = path;
        this.stepName = stepName;
        this.httpClient = httpClient;
        this.context = context;

    }

    public void printStatusReport() {
        System.out.println("");
        System.out.println("Finished with:" + this.stepName);
        System.out.println("Elapsed time:" + this.elapsedTime + " ms");
        System.out.println("Content length:" + this.contentLength + " bytes");
        System.out.println("Throughput:" + this.contentLength / this.elapsedTime + " bytes/ms");
    }

    public HttpRequestStep runStep() throws IOException {

        HttpUriRequest request;

        if (this.httpMethod.equals(GET)) {
            request = new HttpGet(serverUrl + path);
        } else {
            HttpPost req = new HttpPost(serverUrl + path);
            if (this.parameterMap != null) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                for (String key : parameterMap.keySet()) {
                    nvps.add(new BasicNameValuePair(key, parameterMap.get(key)));
                }
                req.setEntity(new UrlEncodedFormEntity(nvps));
            }
            request = req;

        }

            startDateTime = new Date();
            CloseableHttpResponse response = httpClient.execute(
                    request, context);

        try {
            HttpEntity entity = response.getEntity();
            endDateTime = new Date();
            elapsedTime = endDateTime.getTime() - startDateTime.getTime();
            this.httpResponseStatus = response.getStatusLine().getStatusCode();
            this.contentLength = (long) EntityUtils.toByteArray(response.getEntity()).length;

        } finally {
            response.close();
            this.printStatusReport();
        }
        return this;

    }

    public Long getElapsedTime() {
        return elapsedTime;
    }


    public Long getContentLength() {
        return contentLength;
    }


    public Integer getHttpResponseStatus() {
        return httpResponseStatus;
    }


}
