package com.tianle;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.tianle.dto.QuickNewsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：tianLe
 */
@Service
@Slf4j
public class QuickNewsApiServiceImpl {
    public static void main(String[] args) {
        QuickNewsApiServiceImpl quickNewsApiService = new QuickNewsApiServiceImpl();
        quickNewsApiService.pageNews("https://cryptopanic.com/api/pro/v1/posts/", "en", "c3543d3082301e40453a4984228dfb1f1561a931");
    }


    public QuickNewsDTO pageNews(String url, String regionCode, String authToken) {
        // 接口参数
        Map<String, Object> reqMap = handleReq(regionCode, authToken);
        QuickNewsDTO resNewsDTO = new QuickNewsDTO();
        try {
            if (StrUtil.isBlank(url)) {
                throw new Exception("接口URL地址不能为空");
            }
            log.info("pageNews Api will request, URL is {}, params is {}", url, reqMap);

            // 接口请求客户端
            HttpRequest reqClient = HttpRequest.get(url).form(reqMap);
            // 请求接口
            HttpResponse execute = reqClient.execute();
            log.info("pageNews result {}", JSONUtil.toJsonStr(execute));
            // 接口响应状态码
            int resCode = execute.getStatus();
            if (resCode != 200) {
                resNewsDTO.setSuccess(Boolean.FALSE);
            } else {
                // 接口返回数据
                String body = execute.body();
                resNewsDTO = JSONUtil.toBean(body, QuickNewsDTO.class);
                resNewsDTO.setSuccess(Boolean.TRUE);
            }


            // 日志
            // log.warn("Api requested, URL is {}, params is {}, code is {}, msg or data is {}", apiUrl, reqMap, resCode, body);


        } catch (Exception e) {
            log.error("pageNews", e);

        } finally {
        }
        return resNewsDTO;
    }

    private Map<String, Object> handleReq(String regionCode, String authToken) {
        Map<String, Object> paraMaps = new HashMap<>();
        paraMaps.put("regions", regionCode);
        paraMaps.put("kind", "news");
        paraMaps.put("auth_token", authToken);
        paraMaps.put("metadata", true);
        paraMaps.put("approved", true);

        return paraMaps;
    }
}
