package com.chinna.librarymanagement;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.chinna.librarymanagement.config.AppConfig;
import com.chinna.librarymanagement.service.CustomerService;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try(AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(AppConfig.class)){
        	CustomerService customerService=context.getBean(CustomerService.class);
        	        	
        }
    }
}
