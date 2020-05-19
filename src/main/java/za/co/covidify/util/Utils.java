package za.co.covidify.util;

import io.quarkus.elytron.security.common.BcryptUtil;

public class Utils {

  /**
   * Default private constructor
   */
  private Utils() {
    super();
  }

  public static String generatePasswordHash(String password) {
    return BcryptUtil.bcryptHash(password);
  }

}
