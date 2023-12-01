package com.seguo.service.impl;

import com.aliyun.alimt20181012.Client;
import com.aliyun.alimt20181012.models.TranslateGeneralRequest;
import com.aliyun.alimt20181012.models.TranslateGeneralResponse;
import com.aliyun.teautil.models.RuntimeOptions;
import com.seguo.service.TextTranslatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.aliyun.teaopenapi.models.Config;

@Service
public class AliTextTranslatorServiceImpl implements TextTranslatorService {
    private final Client client;

    public AliTextTranslatorServiceImpl(@Autowired Environment environment) {
        Config config = new Config()
                .setAccessKeyId(environment.getProperty("custom.ali.translation.accessKeyId"))
                .setAccessKeySecret(environment.getProperty("custom.ali.translation.secretAccessKey"))
                .setEndpoint(environment.getProperty("custom.ali.translation.endpoint", "mt.cn-hangzhou.aliyuncs.com"));
        try {
            this.client = new Client(config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String translateText(String text, String sourceLanguageCode, String targetLanguageCode) {
        TranslateGeneralRequest translateGeneralRequest = new TranslateGeneralRequest()
                .setFormatType("text")
                .setSourceLanguage(sourceLanguageCode)
                .setTargetLanguage(targetLanguageCode)
                .setSourceText(text)
                .setScene("general");

        TranslateGeneralResponse translateGeneralResponse;
        try {
            translateGeneralResponse = this.client.translateGeneralWithOptions(translateGeneralRequest, new RuntimeOptions());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (translateGeneralResponse.getBody().getCode() == 200) {
            return translateGeneralResponse.getBody().getData().getTranslated();
        } else {
            return "出错了 ===> " + translateGeneralResponse.getBody().toMap();
        }
    }
}

