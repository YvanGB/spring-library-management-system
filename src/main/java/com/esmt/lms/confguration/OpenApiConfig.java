//package com.esmt.lms.confguration;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import org.springdoc.core.customizers.OpenApiCustomiser;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class OpenApiConfig {
//
//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .info(new Info()
//                        .title("Library Management System API")
//                        .version("1.0")
//                        .description("This is a sample Library Management System API"));
//    }
//
//    @Bean
//    public OpenApiCustomiser customizer() {
//        return openApi -> {
//            // Customize the OpenAPI object here
//        };
//    }
//}
