/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aegean.eIdEuSmartClass;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * @author nikos
 */
@Configuration
//@EnableWebMvc
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/tmp/**")
                .addResourceLocations("file:/tmp/");
       
//        if (!registry.hasMappingForPattern("/js/**")) {
//		registry.addResourceHandler("/js/**").addResourceLocations(
//				"classpath:/resources/static/js/");
//	}
        
        super.addResourceHandlers(registry);
    }
}
