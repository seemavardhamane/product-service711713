package com.jbk.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {
	
	@Bean
	public ModelMapper mapper() {
		return new ModelMapper();
	}
	@Configuration
	public class ValidatorConfig {

	    @Bean
	    public LocalValidatorFactoryBean validator() {
	        return new LocalValidatorFactoryBean();
	    }
	    @Bean
	    public RestTemplate restTemplate() {
			return new RestTemplate();
	    	
	    }
	}
}
