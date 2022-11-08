import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;



public class Admin {

    public static Connection conn = dbConnect.conn;
    public static void adminMenu(){
        System.out.println("===============Welcome to the Admin Panel==============");
        int choice;
        Scanner scan = new Scanner(System.in);
        do{
        System.out.println("Press any of the following options from the menu to proceed further:");
        System.out.println("1. Add New Store");
        System.out.println("2. Add New Service");
        System.out.println("3. Logout");
        choice = Integer.parseInt(scan.nextLine());
        switch (choice) {
        case 1:
            addNewStore();
            break;
        case 2:
            addNewService();
            break;
        case 3:
            return;
        default:
            System.out.println("Selection is invalid");
            break;
        }
    } while(choice!=3);
        scan.close();
    }    

    public static void addNewStore(){
        try{
            System.out.println("Please enter the following details to add the store.");
            System.out.println("1.Store Id\n2.Address\n3.Telephone No.\n4.Min wage for Employee\n"+
                                "5.Max wage for Employee\n6.State\n7.Rate\n8.Manager ID\n9.Manager Name\n"+
                                "10.Manager Address\n11.Manager Telephone\n12.Manager Email\n13.Manager Salary");
            Scanner scan = new Scanner(System.in);
            int store_id = Integer.parseInt(scan.nextLine());
            String address = scan.nextLine();
            String tele_no = scan.nextLine();
            int min_wage = Integer.parseInt(scan.nextLine());
            int max_wage = Integer.parseInt(scan.nextLine());
            String state = scan.nextLine();
            int hourly_wage = Integer.parseInt(scan.nextLine());
            int mgr_eid = Integer.parseInt(scan.nextLine());
            String mgr_name = scan.nextLine();
            String mgr_addr = scan.nextLine();
            String mgr_tel = scan.nextLine();
            String mgr_email = scan.nextLine();
            int mgr_sal = Integer.parseInt(scan.nextLine());
            String insertSql = "insert into service_centre(Id,Addr,tele_number,state,minimum_wage,maximum_wage,rate)"
                                +"VALUES (?,?,?,?,?,?,?)";
            String insertMgr = "insert into employee(E_Id, Pno , Name ,Addr ,Email ,Id)"+
                               "values(?, ? , ? ,? ,? ,?)"; 
            PreparedStatement ps = conn.prepareStatement(insertSql);
            
            ps.setInt(1, store_id);
            ps.setString(2, address);
            ps.setString(3, tele_no);
            ps.setString(4, state);
            ps.setInt(5, min_wage);
            ps.setInt(6, max_wage);
            ps.setInt(7, hourly_wage);

            PreparedStatement ps_mgr = conn.prepareStatement(insertMgr);
            ps_mgr.setInt(1,mgr_eid);
            ps_mgr.setString(2,mgr_tel);
            ps_mgr.setString(3,mgr_name);
            ps_mgr.setString(4,mgr_addr);
            ps_mgr.setString(5,mgr_email);
            ps_mgr.setInt(6,store_id);

            PreparedStatement ps_contract_emp = conn.prepareStatement("insert into CONTRACT_EMPLOYEES( E_Id ,Salary) values (? ,?)");
            ps_contract_emp.setInt(1, mgr_eid);
            ps_contract_emp.setInt(2,mgr_sal);

            PreparedStatement ps_mgrtable = conn.prepareStatement("insert into manager(E_id) values(?)");
            ps_mgrtable.setInt(1, mgr_eid);

            System.out.println("Press any of the following options from the menu to proceed further:");
            System.out.println("1.Add Store\n2.Go Back");
            int choice = Integer.parseInt(scan.nextLine());
            if(choice == 1){
                try {
                    ps.executeUpdate();
                    ps_mgr.executeUpdate();
                    ps_contract_emp.executeUpdate();
                    ps_mgrtable.executeUpdate();
                    System.out.println("Success! Adding New Store...");
                } catch (SQLException e) {
                    System.out.println("Could not insert. Please try again");
                    e.printStackTrace();
                }
            }
            else if (choice == 2){
                adminMenu();
            }
            else{
                System.out.println("Invalid Input");
            }
            
            scan.close();
        }
        catch(SQLException e){
            System.out.println("Failure! Could not add new store\n");
            e.printStackTrace();}
    }

    public static void addNewService(){
        Scanner scan = new Scanner(System.in);
        try{
            System.out.println("Please enter the following details to add the service.");
            System.out.println("1.Service Number\n2.Service Category\n3.Service Name\n4.Service Duration");
            String service_no = scan.nextLine();
            String category = scan.nextLine();
            String name = scan.nextLine();
            String duration = scan.nextLine();
            PreparedStatement insertService = conn.prepareStatement("insert into services(service_no,name) values(?,?)");
            PreparedStatement insertRepair = conn.prepareStatement("insert into repairs(service_no) values (?)");
            PreparedStatement insertRepairType = conn.prepareStatement("insert into "+category+"(service_no) values (?)");
            insertService.setString(1, service_no);
            insertService.setString(2, name);
            insertRepairType.setString(1, service_no);
            insertRepair.setString(1, service_no);

            System.out.println("Press any of the following options from the menu to proceed further:");
            System.out.println("1.Add Service\n2.Go Back");
            int choice = Integer.parseInt(scan.nextLine());
            if(choice == 1){{
                try {
                    insertService.executeQuery();
                    insertRepair.executeQuery();
                    insertRepairType.executeQuery();
                    ResultSet manfResults = Manufacturer.getManfNames();
                    while(manfResults.next()){                        
                        PreparedStatement manfPS = conn.prepareStatement("insert into car_needs_service(manufacturer,service_no,duration)"+
                                                    "values (?,?,?)"); 
                        manfPS.setString(1, manfResults.getString("name"));
                        manfPS.setString(2, service_no); 
                        manfPS.setString(3, duration);
                        manfPS.executeUpdate();
                    }
                        System.out.println("Success! Adding New Service...\n");
                    }
                  catch (SQLException e) {
                    System.out.println("Could not insert. Please try again");
                    e.printStackTrace();
                }}
            }
            else if (choice == 2){
                return;
            }
            else{
                System.out.println("Invalid Input");
            }
        }
        catch(SQLException e){
            System.out.println("Failure! Could not add new service\n");
            e.printStackTrace();}
        scan.close();
    }


}