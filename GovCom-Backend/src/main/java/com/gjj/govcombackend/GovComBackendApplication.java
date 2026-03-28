package com.gjj.govcombackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("com.gjj.govcombackend.mapper")
public class GovComBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GovComBackendApplication.class, args);
    }

}
