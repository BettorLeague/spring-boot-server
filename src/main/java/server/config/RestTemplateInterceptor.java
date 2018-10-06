package server.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
@Component
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    @Value("${football.key}")
    private String token;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        //traceRequest(request, body);
        HttpHeaders headers = request.getHeaders();
        headers.add("X-Auth-Token", token);
        ClientHttpResponse response = execution.execute(request, body);
        //traceResponse(response);
        return response;

    }

    private void traceRequest(HttpRequest request, byte[] body) throws IOException {
        log.warn("===========================request begin================================================");
        log.warn("URI         : {}", request.getURI());
        log.warn("Method      : {}", request.getMethod());
        log.warn("Headers     : {}", request.getHeaders() );
        log.warn("Request body: {}", new String(body, "UTF-8"));
        log.warn("==========================request end================================================");
    }

    private void traceResponse(ClientHttpResponse response) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
        String line = bufferedReader.readLine();
        while (line != null) {
            inputStringBuilder.append(line);
            inputStringBuilder.append('\n');
            line = bufferedReader.readLine();
        }
        log.warn("============================response begin==========================================");
        log.warn("Status code  : {}", response.getStatusCode());
        log.warn("Status text  : {}", response.getStatusText());
        log.warn("Headers      : {}", response.getHeaders());
        log.warn("Response body: {}", inputStringBuilder.toString());
        log.warn("=======================response end=================================================");
    }

}
