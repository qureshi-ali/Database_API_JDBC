import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;



public class Customer {

    public static Connection conn = dbConnect.conn;
    public static int cust_id;
    public static int service_centre_no;
    public static Scanner scan = new Scanner(System.in);

    public static void customerMenu(int c_id,int sc_no){
        cust_id = c_id;
        service_centre_no = sc_no;
        int choice;
        System.out.println("===============Welcome to the Customer Panel==============");
        do {
        System.out.println("Press any of the following options from the menu to proceed further:");
        System.out.println("1. View Profile");
        System.out.println("2. Add Car");
        System.out.println("3. Delete Car");
        System.out.println("4. Go Back");
        choice = Integer.parseInt(scan.nextLine());
            switch (choice) {
            case 1:
                viewCustomerProfile();
                break;
            case 2:
                addCar();
                break;
            case 3:
                deleteCar();
                break;
            case 4:
                return;
            default:
                System.out.println("Selection is invalid");
                break;
            }
        } while (choice != 4);

    }
    public static void viewCustomerProfile(){
        try{
            System.out.println("-----------Customer Details-------------------");
            PreparedStatement ps = conn.prepareStatement("select * from Customer where id = ?");
            ps.setInt(1, cust_id);
            ResultSet rs = ps.executeQuery();rs.next();
            System.out.println("Customer ID: "+rs.getInt(1));
            System.out.println("Service Center ID: "+rs.getInt(2));
            System.out.println("First Name: "+rs.getString(3));
            System.out.println("Last Name: "+rs.getString(4));
            System.out.println("Address: "+rs.getInt(7));
            System.out.println("Phone: "+rs.getInt(9));
            System.out.println("Email: "+rs.getInt(8));
            System.out.println("Status: "+rs.getInt(5));
            System.out.println("Standing: "+rs.getInt(6));
            System.out.println("-----------Customer Car Details----------------");
            PreparedStatement ps_car = conn.prepareStatement("select car.vin,car.mileage,car.manufacturer,"+
                                    "car.last_schedule,car.year from customer, car, cust_has_car chc "
                                    +"where customer.id=? and customer.S_id=? and customer.Id=chc.Id and customer.S_Id=chc.S_Id and chc.vin=car.vin");
            ps_car.setInt(1, cust_id);
            ps_car.setInt(2, service_centre_no);
            ResultSet rs_car = ps_car.executeQuery();
            while(rs_car.next()){ 
            System.out.println("VIN: "+rs_car.getString("vin"));
            System.out.println("Mileage: "+rs_car.getInt("mileage"));
            System.out.println("Manufacturer: "+rs_car.getString("manufacturer"));
            System.out.println("Last Schedule: "+rs_car.getString("last_schedule"));
            System.out.println("Year: "+rs_car.getString("year"));
            System.out.println("-----------------------------------------------");
            }
            System.out.println("1.Go Back");
            int choice;
            do{
            choice = Integer.parseInt(scan.nextLine());
            if(choice == 1)
                return;
            else
                System.out.println("Invalid Input");
            }
            while(choice!=1);
        }catch(SQLException e){e.printStackTrace();}
    }

    public static void addCar(){
        try{
            System.out.println("Please enter the following details to add the car.");
            System.out.println("1.VIN\n2.Car Manufacturer\n3.Mileage\n4.Year");
            String vin = scan.nextLine();
            String manf = scan.nextLine();
            int mileage = Integer.parseInt(scan.nextLine());
            int year = Integer.parseInt(scan.nextLine());
            PreparedStatement ps = conn.prepareStatement("select s_id from customer where id=?");
            ps.setInt(1, cust_id);
            ResultSet rs = ps.executeQuery();rs.next();
            int service_centre_no = rs.getInt("s_id");
            String insertCar = "insert into car(Vin,mileage,manufacturer,last_schedule,year)"+
                                "VALUES (?,?,?,'A',?)";
            String insertCustHasCar = "insert into cust_has_car(vin,Id,S_id)"+
                                        "VALUES (?,?,?)";
            PreparedStatement ps_car = conn.prepareStatement(insertCar);
            PreparedStatement ps_custhascar = conn.prepareStatement(insertCustHasCar);
            ps_car.setString(1, vin);
            ps_car.setInt(2, mileage);
            ps_car.setString(3, manf);
            ps_car.setInt(4, year);
            ps_custhascar.setString(1,vin);
            ps_custhascar.setInt(2,cust_id);
            ps_custhascar.setInt(3,service_centre_no);
            System.out.println("Press any of the following options from the menu to proceed further:");
            System.out.println("1.Add Car\n2.Go Back");
            int choice = Integer.parseInt(scan.nextLine());
            if(choice == 1){
                try {
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
            System.out.println("Success! Adding New Car...");
        }catch(SQLException e){e.printStackTrace();}
    }

    public static void deleteCar(){
        try{
            System.out.println("Please select the car to delete.");
            System.out.println("-----------------------------------------");
            PreparedStatement ps_car = conn.prepareStatement("select car.vin,car.mileage,car.manufacturer,"+
                                    "car.last_schedule,car.year from customer, car, cust_has_car chc "
                                    +"where customer.id=? and customer.S_id=? and customer.Id=chc.Id and customer.S_Id=chc.S_Id and chc.vin=car.vin");
            ps_car.setInt(1, cust_id);
            ps_car.setInt(2, service_centre_no);                                    
            ResultSet rs_car = ps_car.executeQuery();
            while(rs_car.next()){ 
            System.out.println("VIN: "+rs_car.getString("vin"));
            System.out.println("Mileage: "+rs_car.getInt("mileage"));
            System.out.println("Manufacturer: "+rs_car.getString("manufacturer"));
            System.out.println("Last Schedule: "+rs_car.getString("last_schedule"));
            System.out.println("Year: "+rs_car.getString("year"));
            System.out.println("-----------------------------------------");
            }
            System.out.println("Press any of the following options from the menu to proceed further:");
            System.out.println("1.Delete Car\n2.Go Back");
            int choice = Integer.parseInt(scan.nextLine());
            if(choice == 1){
                try {
                    System.out.println("Enter the VIN of the car you want to delete:");
                    String deleteCar = scan.nextLine();
                    PreparedStatement delCar = conn.prepareStatement("Delete from car where vin=?");
                    delCar.setString(1, deleteCar);
                    delCar.executeUpdate();
                    System.out.println("Success! Deleted Car...");
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
        }catch(SQLException e){e.printStackTrace();}
    }
}