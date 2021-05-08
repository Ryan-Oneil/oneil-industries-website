package biz.oneilenterprise;

import java.util.ArrayList;
import java.util.Properties;
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

@Configuration
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

	@Value("${mail.host}")
	private String emailHost;

	@Value("${mail.port}")
	private Integer emailPort;

	@Value("${mail.username}")
	private String emailUsername;

	@Value("${mail.password}")
	private String emailPassword;

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









