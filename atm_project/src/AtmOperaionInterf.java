import java.sql.*;

public interface AtmOperaionInterf {

  public void addNewClient(String firstName , String lastName , String birthDate , String citizenship , String address , String phone , String email ,long iin,  String atmNumber ,String pincode);

  public void viewBalance(String atmNumber ,String pincode ,Connection conn);

  public void withdrawCash(int cash , String atmNumber,Connection conn);

  public void depositCash(int cash , String atmNumber,Connection conn);

  public void viewOperations(String atmNumber,Connection conn);

  public void changePincode(String atmNumber,Connection conn);
  
}