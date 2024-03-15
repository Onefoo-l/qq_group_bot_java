package com.onefool.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.OpenAiService;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import retrofit2.Retrofit;


import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.theokanning.openai.service.OpenAiService.*;

/**
 * @Author linjiawei
 * @Date 2024/3/14 0:07
 */
public class HttpClientGptApi {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientGptApi.class);

    private static final String CHAR_GPT_URL = "https://api.openai.com/v1/chat/completions";
    private static final String CHAR_GPT_KEY = "";

    public static OpenAiApi proxy(){
        ObjectMapper mapper = defaultObjectMapper();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        OkHttpClient client =  defaultClient(CHAR_GPT_KEY, Duration.ofSeconds(10000))
                .newBuilder()
                .proxy(proxy)
                .build();
        Retrofit retrofit = defaultRetrofit(client, mapper);
        OpenAiApi api = retrofit.create(OpenAiApi.class);
        return api;
    }
    public static String openAi(String s){
        OpenAiApi api = proxy();
        logger.info("调用代理成功========>");
        OpenAiService service = new OpenAiService(api);
        var charMes = new ChatMessage();
        charMes.setContent(s);
        charMes.setRole(ChatMessageRole.SYSTEM.value());
        var list = new ArrayList<ChatMessage>();
        list.add(charMes);
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .temperature(0.0D)
                .topP(1.0)
                .model("gpt-3.5-turbo")
                .messages(list)
                        .build();
        ChatCompletionResult chatCompletion = service.createChatCompletion(completionRequest);
        List<ChatCompletionChoice> choices = chatCompletion.getChoices();
        String text = choices.get(0).getMessage().getContent();
        logger.info("获得gpt返回的全量数据=========>" + choices);
        logger.info("获得gpt返回的数据=========>" + text);
        return text;
    }

    public static String post(String s){
        // 创建RestTemplate实例
        RestTemplate restTemplate = new RestTemplate();

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", CHAR_GPT_KEY);

        // 构造API请求
        RequestEntity<String> requestEntity = null;
        try {
        requestEntity = new RequestEntity(headers, HttpMethod.GET, new URI(CHAR_GPT_URL));
        } catch (URISyntaxException e) {
        logger.error("API请求出错：" + e);
        }

        // 发送API请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        // 处理API响应
        if (responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()) {
        logger.error("API请求出错：" + responseEntity.getStatusCode());
        }
        String responseBody = responseEntity.getBody();
        System.out.println("API响应：" + responseBody);
        return responseBody;
    }
}
