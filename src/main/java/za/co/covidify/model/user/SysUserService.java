package za.co.covidify.model.user;

import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;

@ApplicationScoped
@Transactional(SUPPORTS)
public class SysUserService {
  private static final Logger LOGGER = Logger.getLogger(SysUserService.class);

  public List<SysUser> findAllUsers() {
    return SysUser.listAll();
  }

  public SysUser findUserById(Long id) {
    return SysUser.findById(id);

  }
  //
  // @Transactional(REQUIRED)
  // public User persistUser(User fighters) {
  // User user;
  //
  // user.persist(user);
  //
  // return user;
  // }
}
