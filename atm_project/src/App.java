import java.util.*;

// import com.mysql.cj.jdbc.ConnectionGroup;
// import com.mysql.cj.protocol.Resultset;
// import com.mysql.cj.xdevapi.PreparableStatement;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class App {

    static Scanner scanner ;
    static Connection conn;
    static AtmOperationImplement atmOperationImplementation;
    public static void main(String[] args) throws Exception {

        scanner = new Scanner(System.in);

        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/atmmachine", "root", "2641");

        atmOperationImplementation = new AtmOperationImplement(conn);

        int choice;
        
        System.out.println("Welcome to the ATM");
        System.out.println("Please make a choice");

        while (true) {

            System.out.println("1.Become Client of the Bank");
            System.out.println("2.Log in to your account");
            System.out.println("3.Exit");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    registration();
                    break;
    
                case 2:
                    logIn();
                    break;
    
                case 3:
                    System.out.println("You exited the system");
                    System.exit(0);
                    break;
            
                default:
                    System.out.println("Invalid Option. Please , Try Again");
                    break;
            }
        }
    }

    public static void registration(){

        System.out.print("First Name:");
        String firstName = scanner.nextLine();

        System.out.print("Last Name:");
        String lastName = scanner.nextLine();

        System.out.print("Birth Date:");
        String dateInput = scanner.nextLine();

        System.out.print("Citizenship:");
        String citizenship = scanner.nextLine();

        System.out.print("Address:");
        String address  = scanner.nextLine();

        System.out.print("Phone Number:");
        String phone  = scanner.nextLine();

        System.out.print("Email:");
        String email  = scanner.nextLine();

        System.out.print("IIN:");
        long iin  = scanner.nextLong();
        scanner.nextLine();

        String atmNumber =  generateAtmNumber();
        String pincode =  generatePincode();

        atmOperationImplementation.addNewClient(firstName, lastName, dateInput, citizenship, address, phone, email, iin ,  atmNumber , pincode);
    }

    public static String generateAtmNumber(){

        String atmNumber = "";

        for (int i = 0; i < 16; i++) {
            atmNumber += (int)(Math.random() * 9);
        }

        return atmNumber;

    }

    public static String generatePincode(){
        
        String pincodeString = ""; 

        for (int i = 0; i < 4; i++) {
           int random = (int) (Math.random() * 9);
           pincodeString += random;
        }

        return pincodeString;

    }

    public static void logIn(){

        System.out.print("ATM NUMBER:");
        String atmNumber = scanner.nextLine();

        System.out.print("PINCODE:");
        String pincode = scanner.nextLine();

        String query = "select * from bankClients where atm_number = ? AND pincode = ?";

        try (PreparedStatement prep = conn.prepareStatement(query)) {
            
            prep.setString(1, atmNumber);
            prep.setString(2, pincode);

            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    atmOperaions(atmNumber , pincode);
                } else {
                    System.out.println("CAN NOT ENTER");
                } 
            } catch (Exception e) {
                System.out.println(e);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void atmOperaions(String atmNumber ,String pincode){
        System.out.println("Please , choose an option");

        while (true) {

            System.out.println("1.View Balance");
            System.out.println("2.Withdraw cash");
            System.out.println("3.Deposit cash");
            System.out.println("4.History of Operations");
            System.out.println("5.Change Pincode");
            System.out.println("6.Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    atmOperationImplementation.viewBalance(atmNumber , pincode , conn);
                    break;

                case 2:

                    System.out.print("How much do you want to withdraw:");
                    int withdrawCash = scanner.nextInt();
                    scanner.nextLine();

                    atmOperationImplementation.withdrawCash(withdrawCash , atmNumber , conn);
                    break;

                case 3:

                    System.out.print("How much do you want to insert:");
                    int insertCash = scanner.nextInt();
                    scanner.nextLine();

                    atmOperationImplementation.depositCash(insertCash , atmNumber , conn);
                    break;

                case 4:
                    atmOperationImplementation.viewOperations(atmNumber , conn);
                    break;

                case 5:
                    atmOperationImplementation.changePincode(atmNumber, conn);
                    break;

                case 6:
                    System.out.println("You exited the system");
                    System.exit(0);
                    break;
            
                default:
                    System.out.println("Invalid Option");
                    break;
            }
        }
    }
}