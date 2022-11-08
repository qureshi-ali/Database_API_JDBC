import java.sql.*;
import java.util.Scanner;

public class Login {

    public static void loginPage() {
        try{

        Connection conn = dbConnect.conn;
        System.out.println("================Welcome to the Login Page=================");
        boolean triedLogin = false;
        boolean hasLoggedin = false;
        int choice;
        Scanner scan = new Scanner(System.in);
        int userID;
        String userPassword;
        ResultSet results = null;
        do {
            if(triedLogin)
                System.out.print("Invalid UserID or Password. Please re-enter your password\n");
            System.out.println("Please enter your username:");
            userID = Integer.parseInt(scan.nextLine());
            System.out.println("Please enter your password: ");
            userPassword = scan.nextLine();
            System.out.println("1)Press 1 to Sign-in");
            System.out.println("2)Press 2 to Go Back");
            choice = Integer.parseInt(scan.nextLine());
            PreparedStatement stmt = conn.prepareStatement("select type from valid where id=? and password=?");
            stmt.setInt(1, userID);
            stmt.setString(2, userPassword);
            if(choice == 1){
                results = stmt.executeQuery();
                triedLogin = true;
                String type;
                while(results.next()){
                    hasLoggedin = true;
                    type = results.getString("type");
                    if(type.equals("admin"))
                        Admin.adminMenu();
                    else if (type == "customer")
                        {//Go to Customer
                        }
                    else if (type == "reception")
                    {Receptionist.receptionMenu(userID);
                    }
                    else if (type == "manager")
                        {//Go to Manager
                        }
                }
            }    
            else if (choice == 2){
                scan.close();
                return;
            }
            else{
                System.out.println("Invalid Input");
            }
            
        } while (hasLoggedin == false);
        System.out.println("Logging in....\n");
        scan.close();
    } catch (SQLException e){e.printStackTrace();};
    }
}



