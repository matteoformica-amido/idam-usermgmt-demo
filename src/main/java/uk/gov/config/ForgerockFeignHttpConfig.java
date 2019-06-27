package uk.gov.config;

import feign.Client;
import feign.Request;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.idam.api.client.invoker.ApiClient;
import uk.gov.hmcts.reform.idam.api.external.OpenIdConnectApi;
import uk.gov.hmcts.reform.idam.api.external.UserManagementApi;
import uk.gov.hmcts.reform.idam.api.external.UserRoleManagementApi;

import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class ForgerockFeignHttpConfig {


    private final Client httpClient;

    private final Request.Options options;

    @Value("${idam.api.root}")
    private String baseUrl;

    @Autowired
    public ForgerockFeignHttpConfig() {
        OkHttpClient client = new OkHttpClient.Builder()
            //.sslSocketFactory(sslSocketFactory, sslSocketFactory.getTrustManager())
            //.hostnameVerifier(hostnameVerifier)
            .readTimeout(20000, TimeUnit.MILLISECONDS)
            .followRedirects(false)
            .build();
        this.httpClient = new feign.okhttp.OkHttpClient(client);

        this.options = new Request.Options(10 * 1000, 60 * 1000, false);
    }

    @Bean
    public OpenIdConnectApi openIdConnectApi() {
        return buildFeignClient(OpenIdConnectApi.class);
    }

    @Bean
    public UserManagementApi userApi() {
        return buildFeignClient(UserManagementApi.class);
    }

    @Bean
    public UserRoleManagementApi roleApi() {
        return buildFeignClient(UserRoleManagementApi.class);
    }

    private <T> T buildFeignClient(Class<T> clazz) {
        ApiClient client = new ApiClient();
        client.getFeignBuilder()
            //.errorDecoder(amFeignErrorDecoder())
            //.requestInterceptors(amInterceptors)
            .options(options)
            .client(httpClient);
        return client.getFeignBuilder()
            .logger(new Slf4jLogger(clazz))
            .target(clazz, baseUrl);
    }

}
