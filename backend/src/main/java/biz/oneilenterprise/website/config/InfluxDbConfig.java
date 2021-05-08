package biz.oneilenterprise.website.config;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.ipc.http.OkHttpSender;
import io.micrometer.influx.InfluxConfig;
import io.micrometer.influx.InfluxMeterRegistry;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDbConfig {

    @Value("${management.metrics.export.influx.org}")
    private String org;

    @Value("${management.metrics.export.influx.bucket}")
    private String bucket;

    @Value("${management.metrics.export.influx.token}")
    private String token;

    @Bean
    public InfluxMeterRegistry influxMeterRegistry(InfluxConfig influxConfig, Clock clock) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            // skip others than write
            if (!original.url().pathSegments().contains("write")) {
                return  chain.proceed(original);
            }
            HttpUrl url = original.url()
                .newBuilder()
                .removePathSegment(0)
                .addEncodedPathSegments("api/v2/write")
                .removeAllQueryParameters("db")
                .removeAllQueryParameters("consistency")
                .addQueryParameter("org", org)
                .addQueryParameter("bucket", bucket)
                .build();

            Request request = original.newBuilder()
                .url(url)
                .header("Authorization", "Token " + token)
                .build();

            return chain.proceed(request);
        });

        return InfluxMeterRegistry.builder(influxConfig)
            .clock(clock)
            .httpClient(new OkHttpSender(httpClient.build()))
            .build();
    }

}
