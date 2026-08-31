package com.example.ProyectoSpringAndresCastellanos.Config;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class Springcontextholder implements ApplicationContextAware {

    private static AplicationContext context;

    @Override
    public void setAplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(tipo);
    }
}
