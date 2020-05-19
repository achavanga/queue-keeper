package za.co.covidify;

import java.nio.charset.Charset;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.apache.commons.io.IOUtils;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class ApplicationLifeCycle {

  @SneakyThrows
  void onStart(@Observes StartupEvent ev) {
    log.info("The application is starting...");
    log.info("Active profile: {}", ProfileManager.getActiveProfile());
    log.info("\n{}", IOUtils.resourceToString("banner.txt", Charset.defaultCharset(), ApplicationLifeCycle.class.getClassLoader()));
    log.info("       Powered by Quarkus");
    log.info("       ");
  }

  void onStop(@Observes ShutdownEvent ev) {
    log.info("The application is stopping.......");
  }
}
