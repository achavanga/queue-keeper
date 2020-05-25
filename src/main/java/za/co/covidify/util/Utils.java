package za.co.covidify.util;

import java.security.spec.InvalidKeySpecException;

import org.wildfly.security.credential.PasswordCredential;
import org.wildfly.security.evidence.PasswordGuessEvidence;
import org.wildfly.security.password.util.ModularCrypt;

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

  public static boolean checkPasswordIsSame(String encrypteDBPassword, String loginPassword) throws InvalidKeySpecException {
    PasswordCredential producedPasswordCredential = new PasswordCredential(ModularCrypt.decode(encrypteDBPassword));
    PasswordGuessEvidence correctPasswordEvidence = new PasswordGuessEvidence(loginPassword.toCharArray());
    return (producedPasswordCredential.verify(correctPasswordEvidence));
  }
}
