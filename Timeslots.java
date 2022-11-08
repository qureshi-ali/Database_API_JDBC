import java.sql.*;
import java.util.ArrayList;

public class Timeslots {

    public static Connection conn = dbConnect.conn;
    
    public static void getTimesSlots(int c_id,int duration){
        ArrayList<ArrayList<Integer>> result_available=new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> slots = new ArrayList<Integer>();
        try{
            ArrayList<Integer> all_mechanics=new ArrayList<Integer>();
            PreparedStatement ps_get_mechanics = conn.prepareStatement("select e_Id from mechanics m, employee e where m.e_id=e.e_id and e.id=(select c.s_Id from customer c where c.id=?");
            ps_get_mechanics.setInt(1,c_id);
            ResultSet rs=ps_get_mechanics.executeQuery();
            while(rs.next()){
                int mech=rs.getInt("e_id");
                all_mechanics.add(mech);
            }
            for (int mechanic_id:all_mechanics){
                for (int i=1; i<=5; i++){
                    for (int j=1;i<=6;j++){
                        PreparedStatement ps_get_available = conn.prepareStatement("((select t.id, m.e_Id from mechanics m cross join time_slot t, employee e where e.e_id=m.e_id and t.day=? and t.week=? and m.e_id=? e.id=(select e1.Id from employee e1 where e1.e_id=?)) minus (select t_id, m_id from bookings)) minus (select t_id,m_id from mech_time_off)");
                        ps_get_available.setInt(1, j);
                        ps_get_available.setInt(2, i);
                        ps_get_available.setInt(3, mechanic_id);
                        ps_get_available.setInt(4, mechanic_id);
                        ResultSet rs_get_available=ps_get_available.executeQuery();rs.next();
                        int previous; int current;int start;
                        start=rs_get_available.getInt("t.id");
                        previous=rs_get_available.getInt("t.id");
                        while(rs_get_available.next()){
                            current=rs.getInt("t.id");
                            if (current!=previous+1){
                                if (duration<previous-start){
                                    slots.add(mechanic_id);
                                    slots.add(start);
                                    slots.add(i);
                                    slots.add(j);
                                    result_available.add(slots);
                                    start=current;
                                }
                            else{
                                previous=current;
                            }
                            }
                        }
                    }
                }
                
            }
            for (ArrayList<Integer> i:result_available){
                for(int j:i){
                    System.out.print(j);
                }
                System.out.println();
            }

        }catch(SQLException e){e.printStackTrace();}
    }
}
