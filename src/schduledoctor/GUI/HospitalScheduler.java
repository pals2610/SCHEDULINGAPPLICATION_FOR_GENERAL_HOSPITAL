package schduledoctor.GUI;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import scheduledoctor.DTO.Doctor;
import scheduledoctor.DTO.ExamRoom;
import scheduledoctor.BUS.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class HospitalScheduler extends JFrame{
	private ArrayList<Doctor> doctors;
    private ArrayList<ExamRoom> examRooms;
    private int populationSize = 100;
    private int maxGenerations = 100;
    private double mutationRate = 0.1;

    public HospitalScheduler() {
        // Khởi tạo các bác sĩ và phòng khám
        doctors = new ArrayList<>();
        examRooms = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            doctors.add(new Doctor("Doctor " + i));
        }

        for (int i = 1; i <= 10; i++) {
            examRooms.add(new ExamRoom("Room " + i));
        }

        // Tạo lịch làm việc cho bác sĩ sử dụng giải thuật di truyền
        ScheduleGenerator scheduleGenerator = new ScheduleGenerator(doctors, examRooms);
        scheduleGenerator.generateSchedule();

        // Hiển thị lịch làm việc
        displaySchedule();

        // Tạo giao diện Swing
        setTitle("Hospital Scheduler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTable scheduleTable = createScheduleTable();
        JScrollPane scrollPane = new JScrollPane(scheduleTable);

        add(scrollPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private JTable createScheduleTable() {
        String[] columnNames = new String[31];
        columnNames[0] = "Doctor";
        for (int i = 1; i <= 30; i++) {
            columnNames[i] = "Day " + i;
        }

        String[][] data = new String[doctors.size()][31];

        for (int i = 0; i < doctors.size(); i++) {
            data[i][0] = doctors.get(i).getName();
            for (int j = 1; j <= 30; j++) {
                int examRoomIndex = doctors.get(i).getSchedule(j - 1);
                data[i][j] = examRooms.get(examRoomIndex).getName();
            }
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable scheduleTable = new JTable(model);

        return scheduleTable;
    }

    private void displaySchedule() {
        for (int day = 0; day < 30; day++) {
            for (int i = 0; i < doctors.size(); i++) {
                int examRoomIndex = doctors.get(i).getSchedule(day);
                System.out.println("Doctor " + (i + 1) + ": Day " + (day + 1) + " - Exam Room: " + (examRoomIndex + 1));
            }
        }
    }
}
