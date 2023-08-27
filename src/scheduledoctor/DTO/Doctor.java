package scheduledoctor.DTO;

public class Doctor {
 	private String name;
    private int[] schedule;

    public Doctor(String name) {
        this.name = name;
        this.schedule = new int[30]; // Mỗi bác sĩ có 30 ngày làm việc trong tháng
    }

    public String getName() {
        return name;
    }

    public void setSchedule(int day, int examRoomIndex) {
        schedule[day] = examRoomIndex;
    }

    public int getSchedule(int day) {
        return schedule[day];
    }
}
