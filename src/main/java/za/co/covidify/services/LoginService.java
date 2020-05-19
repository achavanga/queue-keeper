package za.co.covidify.services;

import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.security.spec.InvalidKeySpecException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.wildfly.security.credential.PasswordCredential;
import org.wildfly.security.evidence.PasswordGuessEvidence;
import org.wildfly.security.password.util.ModularCrypt;

import za.co.covidify.model.Login;
import za.co.covidify.model.User;

@ApplicationScoped
@Transactional(SUPPORTS)
public class LoginService {

  @Inject
  UserService userService;

  public Response login(Login login) throws InvalidKeySpecException {
    User user = userService.findByName(login.getUserName());
    if (user != null) {
      PasswordCredential producedPasswordCredential = new PasswordCredential(ModularCrypt.decode(user.getPassword()));
      PasswordGuessEvidence correctPasswordEvidence = new PasswordGuessEvidence(login.getPassword().toCharArray());
      if (producedPasswordCredential.verify(correctPasswordEvidence)) {
        return Response.ok(user).build();
      }
      else {
        return Response.status(Status.UNAUTHORIZED).build();
      }
    }
    return Response.status(Status.BAD_REQUEST).build();
  }
}
