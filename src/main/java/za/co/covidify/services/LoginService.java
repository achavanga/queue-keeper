package za.co.covidify.services;

import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.security.spec.InvalidKeySpecException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import za.co.covidify.model.Login;
import za.co.covidify.model.User;
import za.co.covidify.util.Utils;

@ApplicationScoped
@Transactional(SUPPORTS)
public class LoginService {

  @Inject
  UserService userService;

  public Response login(Login login) throws InvalidKeySpecException {
    User user = userService.findByName(login.getUsername());
    if (user != null) {
      if (Utils.checkPasswordIsSame(user.getPassword(), login.getPassword())) {
        return Response.ok(user).build();
      }
      else {
        return Response.status(Status.UNAUTHORIZED).build();
      }
    }
    return Response.status(Status.BAD_REQUEST).build();
  }

}
