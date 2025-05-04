package com.example.ToDo.Configuration;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.apache.lucene.analysis.CharArraySet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Properties;

@Configuration(proxyBeanMethods = false)
public class SpringConfiguration {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String PASSWORD = dotenv.get("PASSWORD");

    @Bean
    public JwtParser jwtParser() {
        return Jwts.parser();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CharArraySet charArraySet() {
        return new CharArraySet(Arrays.asList("men"), true);
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("makasrinivasulu11@gmail.com");
        mailSender.setPassword(PASSWORD);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
