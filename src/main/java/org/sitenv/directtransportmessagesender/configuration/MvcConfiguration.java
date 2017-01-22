package org.sitenv.directtransportmessagesender.configuration;

import org.apache.commons.io.FileUtils;
import org.sitenv.common.utilities.encryption.DesEncrypter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

@EnableWebMvc
@Configuration
@ComponentScan("org.sitenv")
@PropertySource(value = "/META-INF/maven/org.sitenv/directtransportmessagesender/pom.properties", ignoreResourceNotFound=true)
public class MvcConfiguration extends WebMvcConfigurerAdapter {
    private static final String ENCRYPTEDKEY = "sitplatform@1234";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }

    @Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		return multipartResolver;
	}

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public JavaMailSender mailSender(final Environment environment, String smtpPassword){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(environment.getProperty("smtphostname"));
        mailSender.setPort(Integer.parseInt(environment.getProperty("smtpport")));
        mailSender.setUsername(environment.getProperty("smtpusername"));
        mailSender.setPassword(smtpPassword);

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.starttls.enable", environment.getProperty("smtpenablessl"));
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.debug", "true");

        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }

    @Bean
    public String smtpPassword(final Environment environment){
        String smtppswrd = null;
        try {
            smtppswrd = FileUtils.readFileToString(new File(environment.getProperty("smtppswdPath")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new DesEncrypter(ENCRYPTEDKEY).decrypt(smtppswrd);
    }
}
