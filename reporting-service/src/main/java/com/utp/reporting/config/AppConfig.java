package com.utp.reporting.config;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;
import com.utp.reporting.resource.ReporteResource;
import com.utp.reporting.resource.HealthResource;

@ApplicationPath("/api")
public class AppConfig extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(ReporteResource.class);
        classes.add(HealthResource.class);
        return classes;
    }
}
