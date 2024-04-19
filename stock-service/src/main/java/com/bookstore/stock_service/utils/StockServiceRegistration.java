package com.bookstore.stock_service.utils;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.ext.consul.ConsulClientOptions;
import io.vertx.ext.consul.ServiceOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.consul.ConsulClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class StockServiceRegistration {

  private static final Logger LOGGER = Logger.getLogger(StockServiceRegistration.class);
  private final String QUARKUS_HTTP_PORT = "quarkus.http.port";

  @ConfigProperty(name = "consul.host")
  String consulHost;

  @ConfigProperty(name = "consul.port")
  int consulPort;

  @ConfigProperty(name = "quarkus.application.name")
  String appName;

  @ConfigProperty(name = "environment")
  String env;

  private ConsulClient consulClient;

  public void init(@Observes StartupEvent ev, Vertx vertx) {

    try (ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor()) {
      executorService.schedule(
          () -> {
            consulClient =
                ConsulClient.create(
                    vertx, new ConsulClientOptions().setHost(consulHost).setPort(consulPort));
            String appPort = ConfigProvider.getConfig().getValue(QUARKUS_HTTP_PORT, String.class);

            LOGGER.infof("registering service: %s, port: %s", appName, appPort);

            consulClient.registerServiceAndAwait(
                new ServiceOptions()
                    .setName(appName)
                    .setAddress(env)
                    .setPort(Integer.parseInt(appPort))
                    .setId("stock-service"));
          },
          5,
          TimeUnit.SECONDS);
    }
  }

  void onStop(@Observes ShutdownEvent ev, Vertx vertx) {
    consulClient.deregisterServiceAndAwait("stock-service");

    LOGGER.infof("Instance de-registered: id= %s", appName);
  }
}
