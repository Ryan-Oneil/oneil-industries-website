package biz.oneilenterprise;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.ipc.http.OkHttpSender;
import io.micrometer.influx.InfluxConfig;
import io.micrometer.influx.InfluxMeterRegistry;
import java.util.ArrayList;
import java.util.Properties;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableCaching
@EnableAspectJAutoProxy
@EnableSpringDataWebSupport
@SpringBootApplication(exclude= HibernateJpaAutoConfiguration.class)
public class AppConfig {

	public static final ArrayList<String> ADMIN_ROLES;

	static {
		ADMIN_ROLES = new ArrayList<>();
		ADMIN_ROLES.add("ROLE_ADMIN");
	}

	@Value("${management.metrics.export.influx.org}")
	private String org;

	@Value("${management.metrics.export.influx.bucket}")
	private String bucket;

	@Value("${management.metrics.export.influx.token}")
	private String token;

	@Value("${mail.host}")
	private String emailHost;

	@Value("${mail.port}")
	private Integer emailPort;

	@Value("${mail.username}")
	private String emailUsername;

	@Value("${mail.password}")
	private String emailPassword;

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

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(emailHost);
		mailSender.setPort(emailPort);
		mailSender.setUsername(emailUsername);
		mailSender.setPassword(emailPassword);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		//Ignores hibernate lazy collections
		modelMapper.getConfiguration().setPropertyCondition(context -> !(context.getSource() instanceof PersistentCollection));

		return modelMapper;
	}

	public static void main(String[] args) {
		SpringApplication.run(AppConfig.class, args);
	}

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}

}









