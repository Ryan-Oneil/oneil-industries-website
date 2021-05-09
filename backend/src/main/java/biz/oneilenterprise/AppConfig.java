package biz.oneilenterprise;

import java.util.ArrayList;
import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

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

}









