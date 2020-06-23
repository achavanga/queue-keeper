package za.co.covidify.request.to;

import lombok.Data;

@Data
public class CompanyRS {

  long id;

  String companyName;

  String websiteUrl;

  String emailAddress;

  String cellphoneNumber;

  String phone;

  String logo = "";
}
