package org.javasel.config;

import java.io.Serializable;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:org/javasel/optimisticlocks/applicationContext.xml")
public class OptimisticLocksConfig implements Serializable {

}
