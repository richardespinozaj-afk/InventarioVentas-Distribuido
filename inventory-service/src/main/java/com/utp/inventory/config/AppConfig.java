package com.utp.inventory.config;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;
import com.utp.inventory.resource.ProductoResource;
import com.utp.inventory.resource.VentaResource;
import com.utp.inventory.resource.HealthResource;
import com.utp.inventory.resource.CorsFilter;

@ApplicationPath("/api")
public class AppConfig extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(ProductoResource.class);
        classes.add(VentaResource.class);
        classes.add(HealthResource.class);
        classes.add(CorsFilter.class);
        return classes;
    }
}
