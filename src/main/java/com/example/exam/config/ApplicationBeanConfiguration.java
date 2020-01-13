package com.example.exam.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public static BeanFactoryPostProcessor removeTomcatWebServerCustomizer() {
        return (beanFactory) ->
                ((DefaultListableBeanFactory)beanFactory).removeBeanDefinition("tomcatWebServerFactoryCustomizer");
    }
}
