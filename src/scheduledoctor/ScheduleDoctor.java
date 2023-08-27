
package scheduledoctor;

import java.sql.SQLException;
import schduledoctor.GUI.HospitalScheduler;

/**
 *
 * @author 
 */
public class ScheduleDoctor {

    public static void main(String[] args) throws SQLException {
    	 HospitalScheduler frame = new HospitalScheduler();
         frame.setVisible(true);
        System.out.println("System running!");
    }
}
