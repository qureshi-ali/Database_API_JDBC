import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class Customer {

    public static Connection conn = dbConnect.conn;
    public static void customerMenu(){
        System.out.println("===============Welcome to the Customer Panel==============");
        System.out.println("Press any of the following options from the menu to proceed further:");
        System.out.println("1. View Profile");
        System.out.println("2. Add Car");
        System.out.println("3. Add Delete Car");
        System.out.println("4. Go Back");
        Scanner scan = new Scanner(System.in);
        int choice = scan.nextInt();
        do {
            choice = scan.nextInt();
            switch (choice) {
            case 1:
                //System Setup
                break;
            case 2:
                //Add New Store
            case 3:
                //Add New Service
            case 4:
                Menu.menuOptions();
                break;
            default:
                System.out.println("Selection is invalid");
                break;
            }

        } while (choice != 1);
        scan.close();

    }    