package za.co.covidify.scheduler;

import javax.enterprise.context.ApplicationScoped;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class QueueScheduler {

  // @Scheduled(every = "60s")
  public void cronJobWithExpressionInConfig() {
    log.info("Cron expression configured in application.properties");
  }
}
