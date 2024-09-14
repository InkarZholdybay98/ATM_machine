import java.sql.*;
import java.util.*;

// import com.mysql.cj.protocol.Resultset;
// import com.mysql.cj.xdevapi.PreparableStatement;

public class AtmOperationImplement implements AtmOperaionInterf {

  Connection conn;
  Scanner scanner = new Scanner(System.in);

  public AtmOperationImplement(){}

  public AtmOperationImplement(Connection conn){
    this.conn = conn;
  }
  
  @Override
  public void addNewClient(String firstName , String lastName , String birthDate , String citizenship , String address , String phone , String email ,long iin ,String atmNumber ,String pincode ){
    
    String addNewClientQuery = "INSERT INTO bankClients ( first_name , last_name ,  birth_date , citizenship , adress , phone_number , email , IIN , atm_number , pincode ) VALUES(?,?,?,?,?,?,?,?,?,?)";

    try (PreparedStatement prep = conn.prepareStatement(addNewClientQuery)) {

      prep.setString(1, firstName);
      prep.setString(2, lastName);
      prep.setString(3, birthDate);
      prep.setString(4, citizenship);
      prep.setString(5, address);
      prep.setString(6, phone);
      prep.setString(7, email);
      prep.setLong(8, iin);
      prep.setString(9, atmNumber);
      prep.setString(10, pincode);

      prep.executeUpdate();

      System.out.println("The New Client was successfully added!");

    } catch (Exception e) {
      System.out.println(e);
    }

  }

  @Override
  public void viewBalance(String atmNumber ,String pincode ,Connection conn){

    String query = "select *from bankClients where  atm_number = ? AND pincode = ?";

    try (PreparedStatement prep = conn.prepareStatement(query)) {
      
      prep.setString(1, atmNumber);
      prep.setString(2, pincode);

      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          System.out.println("Your Balance:"+rs.getString("deposit"));
        }
      } catch (Exception e) {
        System.out.println(e);
      }

    } catch (Exception e) {
      System.out.println(e);
    }
  };

  @Override
  public void withdrawCash(int cash , String atmNumber,Connection conn){

    boolean isCashEnough = checkBalance(cash , atmNumber ,conn);

    String query = "UPDATE bankClients SET deposit = deposit - ? WHERE  atm_number = ?";
    
    try (PreparedStatement prep = conn.prepareStatement(query)) {

      prep.setInt(1, cash);
      prep.setString(2, atmNumber);

      if (isCashEnough) {

        prep.executeUpdate();
        System.out.println("The operation was successfull!");

        String withdrawOperation = "Withdrawn:"+cash;

        addWithdrawOperation(cash , atmNumber , conn , withdrawOperation);

      } else {
        System.out.println("Insufficient funds in the account");
      }
      
    } catch (Exception e) {
      System.out.println(e);
    }

  };

  @Override
  public void depositCash(int cash , String atmNumber,Connection conn ){

    String insertCashQuery = "UPDATE bankClients SET deposit = COALESCE (deposit , 0) + ? WHERE  atm_number = ?";

    try (PreparedStatement prep = conn.prepareStatement(insertCashQuery)) {
      prep.setInt(1, cash);
      prep.setString(2, atmNumber);
      prep.executeUpdate();

      System.out.println("The operation was successful");

      String addOperationString = "Inserted:"+cash;

      addInsertOperation(atmNumber, addOperationString ,conn);

    } catch (Exception e) {
      System.out.println(e);
    }

  };

  @Override
  public void viewOperations(String atmNumber,Connection conn){
    String query = "select * from bankClients where atm_number = ?";

    try (PreparedStatement prep = conn.prepareStatement(query)) {
      prep.setString(1, atmNumber);

      try (ResultSet rs = prep.executeQuery()) {
        
        while (rs.next()) {
          System.out.println(rs.getString("history_of_operations"));
        }

      } catch (Exception e) {
        System.out.println(e);
      }

    } catch (Exception e) {
      System.out.println(e);
    }
  }

  @Override
  public void changePincode(String atmNumber,Connection conn){

    System.out.print("WRITE THE NEW PINCODE:");
    String newPincode = scanner.nextLine();

    String query = "UPDATE bankClients SET pincode = ? WHERE atm_number = ?";

    try (PreparedStatement prep = conn.prepareStatement(query)) {
      
      prep.setString(1, newPincode);
      prep.setString(2, atmNumber);

      prep.executeUpdate();

      System.out.println("The pincode was changed successfully");

    } catch (Exception e) {
      System.out.println(e);
    }

  }

  public boolean checkBalance(int cash , String atmNumber,Connection conn){

    String query = "select * from bankClients where atm_number = ?";

    try (PreparedStatement prep = conn.prepareStatement(query)) {

      prep.setString(1, atmNumber);

      try (ResultSet rs = prep.executeQuery()) {

        if (rs.next()) {
          
          int deposit = rs.getInt("deposit");

          if (deposit > cash) {
            return true;
          }else{
            return false;
          }

        }else{
          return false;
        }
        
      } catch (Exception e) {
        System.out.println(e);
        return false;
      }
      
    } catch (Exception e) {
      System.out.println(e);
      return false;
    }

  }

  public void addInsertOperation(String atmNumber,String addOperationString ,Connection conn){

    System.out.println(157);
    System.out.println(addOperationString);

    String query = "UPDATE bankClients SET history_of_operations = CONCAT(COALESCE(history_of_operations , '') , '\n' , ?) WHERE atm_number = ?";



    try (PreparedStatement prep = conn.prepareStatement(query)) {
      
      prep.setString(1, addOperationString);
      prep.setString(2, atmNumber);
      prep.executeUpdate();

    } catch (Exception e) {
      System.out.println(e);
    }

  }

  public void addWithdrawOperation(int cash , String atmNumber,Connection conn , String withdrawOperationString){

    String query = "UPDATE bankClients SET history_of_operations = CONCAT(history_of_operations , '\n' , ?) WHERE atm_number = ?";

    try (PreparedStatement prep = conn.prepareStatement(query)) {

      prep.setString(1, withdrawOperationString);
      prep.setString(2, atmNumber);

      prep.executeUpdate();
      
    } catch (Exception e) {
      System.out.println(e);
    }

  }

}