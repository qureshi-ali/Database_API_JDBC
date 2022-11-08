import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class Receptionist {

    public static Connection conn = dbConnect.conn;
    public static int r_id;
    public static Scanner scan = new Scanner(System.in);
    public static void receptionMenu(int id){
        r_id = id;
        int choice;
        System.out.println("===============Welcome to the Reception Panel==============");
        do{
        System.out.println("Press any of the following options from the menu to proceed further:");
        System.out.println("1. Add New Customer Profile");
        System.out.println("2. Find Customers with Pending Invoices");
        System.out.println("3. Logout");
        
        choice = Integer.parseInt(scan.nextLine());
            switch (choice) {
            case 1:
                addNewCustomer();
                break;
            case 2:
                // getPendingInvoices();
                break;
            case 3:
                return;
            default:
                System.out.println("Selection is invalid");
                break;
            }
        } while (choice != 3);

    }

    public static void addNewCustomer(){
        try{
            System.out.println("Please enter the following details to add the store.");
            System.out.println("1.First Name\n2.Last Name\n3.Address\n4.Email\n"+
                                "5.Phone Number\n6.VIN Number\n7.Car Manufacturer\n8.Current Mileage\n9.Year\n"+
                                "10.Customer ID");
            String fname = scan.nextLine();
            String lname = scan.nextLine();
            String address = scan.nextLine();
            String email = scan.nextLine();
            String phone_number = scan.nextLine();
            String vin = scan.nextLine();
            String manf = scan.nextLine();
            int mileage = Integer.parseInt(scan.nextLine());
            int year = Integer.parseInt(scan.nextLine());
            int cust_id = Integer.parseInt(scan.nextLine());
            PreparedStatement ps = conn.prepareStatement("select id from employee where e_id=?");
            ps.setInt(1, r_id);
            ResultSet rs = ps.executeQuery();rs.next();
            int service_centre_no = rs.getInt("id");
            String insertCustomer = "insert into customer(Id,S_Id,Fname,Lname,standing,status,address,email,phone)"+
                                    "VALUES(?,?,?,?,?,?,?,?,?)";
            String insertCar = "insert into car(Vin,mileage,manufacturer,last_schedule,year)"+
                                "VALUES (?,?,?,'A',?)";
            String insertCustHasCar = "insert into cust_has_car(vin,Id,S_id)"+
                                        "VALUES (?,?,?)";
            PreparedStatement ps_cust = conn.prepareStatement(insertCustomer);
            PreparedStatement ps_car = conn.prepareStatement(insertCar);
            PreparedStatement ps_custhascar = conn.prepareStatement(insertCustHasCar);
            ps_cust.setInt(1, cust_id);
            ps_cust.setInt(2, service_centre_no);
            ps_cust.setString(3, fname);
            ps_cust.setString(4, lname);            
            ps_cust.setInt(5, 1);            
            ps_cust.setInt(6, 1);            
            ps_cust.setString(7, address);            
            ps_cust.setString(8, email);            
            ps_cust.setString(9, phone_number);            
            ps_car.setString(1, vin);
            ps_car.setInt(2, mileage);
            ps_car.setString(3, manf);
            ps_car.setInt(4, year);
            ps_custhascar.setString(1,vin);
            ps_custhascar.setInt(2,cust_id);
            ps_custhascar.setInt(3,service_centre_no);
            System.out.println("Press any of the following options from the menu to proceed further:");
            System.out.println("1.Add Customer\n2.Go Back");
            int choice = scan.nextInt();
            if(choice == 1){
                try {
                    ps_cust.executeUpdate();
                    ps_car.executeUpdate();
                    ps_custhascar.executeUpdate();
                } catch (SQLException e) {
                    System.out.println("Could not insert. Please try again");
                    e.printStackTrace();
                }
            }
            else if (choice == 2){
                return;
            }
            else{
                System.out.println("Invalid Input");
            }
            System.out.println("Success! Adding New Customer...");
        }
        catch(SQLException e){
            System.out.println("Failure! Could not add new customer\n");
            e.printStackTrace();}
    }
}
