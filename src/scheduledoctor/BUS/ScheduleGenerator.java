package scheduledoctor.BUS;

import java.util.ArrayList;
import java.util.Random;

import scheduledoctor.DTO.Doctor;
import scheduledoctor.DTO.ExamRoom;

public class ScheduleGenerator {
	private ArrayList<Doctor> doctors;
	private ArrayList<ExamRoom> examRooms;
	private Random random;
	private int populationSize = 100;
	private int maxGenerations = 100;
	private double mutationRate = 0.1;
	
	public ScheduleGenerator(ArrayList<Doctor> doctors, ArrayList<ExamRoom> examRooms) {
	    this.doctors = doctors;
	    this.examRooms = examRooms;
	    this.random = new Random();
	}
	
	public void generateSchedule() {
	    int[] bestSchedule = new int[doctors.size() * 30];
	    double bestFitness = Double.MAX_VALUE;

	    // Tạo quần thể ban đầu
	    ArrayList<int[]> population = createInitialPopulation();

	    for (int generation = 0; generation < maxGenerations; generation++) {
	        // Đánh giá độ thích nghi của quần thể
	        ArrayList<Double> fitnessList = evaluateFitness(population);

	        // Tìm cá thể có độ thích nghi tốt nhất
	        int bestIndex = findBestIndividualIndex(fitnessList);
	        double currentBestFitness = fitnessList.get(bestIndex);

	        // Nếu cá thể tốt nhất tới một ngưỡng đủ tốt, dừng giải thuật
	        if (currentBestFitness < bestFitness) {
	            bestSchedule = population.get(bestIndex);
	            bestFitness = currentBestFitness;
	            break;
	        }

	        // Chọn các cá thể cha mẹ cho quá trình lai ghép
	        ArrayList<int[]> parents = selectParents(population, fitnessList);

	        // Lai ghép và đột biến để tạo thế hệ mới
	        ArrayList<int[]> newGeneration = reproduce(parents);

	        // Thay thế thế hệ cũ bằng thế hệ mới
	        population = newGeneration;
	    }

	    // Gán lịch làm việc tốt nhất cho bác sĩ
	    assignSchedule(bestSchedule);
	}
	
	private ArrayList<int[]> createInitialPopulation() {
	    ArrayList<int[]> population = new ArrayList<>();

	    for (int i = 0; i < populationSize; i++) {
	        int[] individual = new int[doctors.size() * 30];
	        for (int j = 0; j < individual.length; j++) {
	            individual[j] = random.nextInt(examRooms.size());
	        }
	        population.add(individual);
	    }

	    return population;
	}

	private ArrayList<Double> evaluateFitness(ArrayList<int[]> population) {
	    ArrayList<Double> fitnessList = new ArrayList<>();

	    for (int[] individual : population) {
	        double fitness = calculateFitness(individual);
	        fitnessList.add(fitness);
	    }

	    return fitnessList;
	}
	
	private double calculateFitness(int[] individual) {
	    // Tính toán độ thích nghi của cá thể (ví dụ: số lần xếp trùng phòng khám)
	    double fitness = 0.0;
	    for (int day = 0; day < 30; day++) {
	        ArrayList<Integer> usedRooms = new ArrayList<>();
	        for (int i = 0; i < doctors.size(); i++) {
	            int examRoomIndex = individual[day * doctors.size() + i];
	            if (usedRooms.contains(examRoomIndex)) {
	                fitness++;
	            } else {
	                usedRooms.add(examRoomIndex);
	            }
	        }
	    }

	    return fitness;
	}
	
	private int findBestIndividualIndex(ArrayList<Double> fitnessList) {
	    int bestIndex = 0;
	    double bestFitness = Double.MAX_VALUE;

	    for (int i = 0; i < fitnessList.size(); i++) {
	        double fitness = fitnessList.get(i);
	        if (fitness < bestFitness) {
	            bestFitness = fitness;
	            bestIndex = i;
	        }
	    }

	    return bestIndex;
	}
	
	private ArrayList<int[]> selectParents(ArrayList<int[]> population, ArrayList<Double> fitnessList) {
	    ArrayList<int[]> parents = new ArrayList<>();

	    // Chọn các cá thể cha mẹ dựa trên một phương pháp lựa chọn (ví dụ: Roulette wheel selection)
	    double totalFitness = 0.0;
	    for (double fitness : fitnessList) {
	        totalFitness += fitness;
	    }

	    for (int i = 0; i < populationSize; i++) {
	        double randomFitness = random.nextDouble() * totalFitness;
	        double currentFitnessSum = 0.0;
	        int j = 0;
	        while (currentFitnessSum < randomFitness) {
	            currentFitnessSum += fitnessList.get(j);
	            j++;
	        }
	        parents.add(population.get(j - 1));
	    }

	    return parents;
	}
	
	private ArrayList<int[]> reproduce(ArrayList<int[]> parents) {
	    ArrayList<int[]> newGeneration = new ArrayList<>();

	    while (newGeneration.size() < populationSize) {
	        int[] parent1 = parents.get(random.nextInt(parents.size()));
	        int[] parent2 = parents.get(random.nextInt(parents.size()));

	        int crossoverPoint = random.nextInt(parent1.length);

	        int[] child1 = new int[parent1.length];
	        int[] child2 = new int[parent2.length];

	        // Lai ghép
	        for (int i = 0; i < crossoverPoint; i++) {
	            child1[i] = parent1[i];
	            child2[i] = parent2[i];
	        }
	        for (int i = crossoverPoint; i < parent1.length; i++) {
	            child1[i] = parent2[i];
	            child2[i] = parent1[i];
	        }

	        // Đột biến
	        if (random.nextDouble() < mutationRate) {
	            mutate(child1);
	        }
	        if (random.nextDouble() < mutationRate) {
	            mutate(child2);
	        }

	        newGeneration.add(child1);
	        newGeneration.add(child2);
	    }

	    return newGeneration;
	}
	
	private void mutate(int[] individual) {
	    int mutationPoint = random.nextInt(individual.length);
	    int newRoom = random.nextInt(examRooms.size());
	    individual[mutationPoint] = newRoom;
	}

	private void assignSchedule(int[] schedule) {
	    for (int day = 0; day < 30; day++) {
	        for (int i = 0; i < doctors.size(); i++) {
	            int examRoomIndex = schedule[day * doctors.size() + i];
	            doctors.get(i).setSchedule(day, examRoomIndex);
	        }
	    }
	}
}
