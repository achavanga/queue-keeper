package za.co.covidify.model;

public enum UserType {
  SHOP_USER("Shop user"),
  GENERAL_USER("Just a user"),
  SYS_ADMIN("System administrator of the system");

  private final String userTypeCode;

  private UserType(String userTypeCode) {
    this.userTypeCode = userTypeCode;
  }

}