package org.sitenv.directtransportmessagesender.configuration;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

@EnableWebMvc
@Configuration
@ComponentScan("org.sitenv")
@PropertySource(value = "/META-INF/maven/org.sitenv/directtransportmessagesender/pom.properties", ignoreResourceNotFound=true)
public class MvcConfiguration extends WebMvcConfigurerAdapter {
    private static final String ENCRYPTEDKEY = "sitplatform@1234";
    private static List<String> files = new ArrayList<>();

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
    public JavaMailSender mailSender(final Environment environment, Properties siteMailProperties, String siteSmtpPassword){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(environment.getProperty("site.smtphostname"));
        mailSender.setPort(Integer.parseInt(environment.getProperty("smtpport")));
        mailSender.setUsername(environment.getProperty("site.smtpusername"));
        mailSender.setPassword(siteSmtpPassword);
        mailSender.setJavaMailProperties(siteMailProperties);
        return mailSender;
    }

    @Bean
    public Properties siteMailProperties(final Environment environment){
        Properties javaMailProperties = new Properties();
        // imap setting
        javaMailProperties.put("mail.imap.host", environment.getProperty("site.smtphostname"));
        javaMailProperties.put("mail.imap.port", String.valueOf(environment.getProperty("imapport")));
        javaMailProperties.put("mail.imap.ssl.enable", false);
        javaMailProperties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailProperties.setProperty("mail.imap.socketFactory.fallback", "true");
        javaMailProperties.setProperty("mail.imap.socketFactory.port", String.valueOf(environment.getProperty("imapport")));

        //smtp settings
        javaMailProperties.put("mail.smtp.host", environment.getProperty("site.smtpusername"));
        javaMailProperties.put("mail.smtp.port", Integer.parseInt(environment.getProperty("smtpport")));
        javaMailProperties.put("mail.smtp.starttls.enable", environment.getProperty("smtpenablessl"));
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.debug", "true");
        return javaMailProperties;
    }

    @Bean
    public String siteSmtpPassword(final Environment environment){
        String smtppswrd = null;
        try {
            smtppswrd = FileUtils.readFileToString(new File(environment.getProperty("site.smtppswdPath")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new DesEncrypter(ENCRYPTEDKEY).decrypt(smtppswrd);
    }

    @Bean
    public Properties hhsMailProperties(final Environment environment){
        Properties javaMailProperties = new Properties();
        // imap setting
        javaMailProperties.put("mail.imap.host", environment.getProperty("hhs.smtphostname"));
        javaMailProperties.put("mail.imap.port", 993);
        javaMailProperties.put("mail.imap.ssl.enable", true);
        javaMailProperties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailProperties.setProperty("mail.imap.socketFactory.fallback", "true");
        javaMailProperties.setProperty("mail.imap.socketFactory.port", "993");

        //smtp settings
        javaMailProperties.put("mail.smtp.host", environment.getProperty("hhs.smtpusername"));
        javaMailProperties.put("mail.smtp.port", Integer.parseInt(environment.getProperty("smtpport")));
        javaMailProperties.put("mail.smtp.starttls.enable", environment.getProperty("smtpenablessl"));
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.debug", "true");
        return javaMailProperties;
    }

    @Bean(name="hhsSmtpPassword")
    public String hhsSmtpPassword(final Environment environment){
        String hhssmtppswrd = null;
        try {
            hhssmtppswrd = FileUtils.readFileToString(new File(environment.getProperty("hhs.smtppswdPath")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String decryptedPass = new DesEncrypter(ENCRYPTEDKEY).decrypt(hhssmtppswrd);
        return decryptedPass;
    }

    @Bean
    public static final List<String> ccdaFileList(final Environment environment){
        List<String> fileNames = new ArrayList<>();
        try {
            Collection<File> filesb =
                    FileUtils.listFiles(new File(environment.getProperty("sampleCcdaDir")), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
            for (File file : filesb) {
                fileNames.add(file.getCanonicalPath());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return fileNames;
    }
}
