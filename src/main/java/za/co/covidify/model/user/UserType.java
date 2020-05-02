package za.co.covidify.model.user;

public enum UserType {
  MANAGER("Manage of a company"),
  WORKER("Genral employee"),
  GENERAL_USER("Just a user"),
  SYS_ADMIN("System administrator of the system");

  private final String userTypeCode;

  private UserType(String userTypeCode) {
    this.userTypeCode = userTypeCode;
  }

}