package za.co.covidify;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.jboss.logging.Logger;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class ApplicationLifeCycle {

  private static final Logger LOGGER = Logger.getLogger(ApplicationLifeCycle.class);

  void onStart(@Observes StartupEvent ev) {

    LOGGER.info("                         Powered by Quarkus");
  }

  void onStop(@Observes ShutdownEvent ev) {
    LOGGER.info("The application is stopping...");
  }
}
