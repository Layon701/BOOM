package itf221.gvi.boom;

import itf221.gvi.boom.data.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class RoomManagementUnit {

    private final char lastPossibleTimeslot = 'E';

    /**
     * <b>Deterministic</b> algorithm for distributing the offered presentations among the students considering their wishes.
     *
     * @param boomData the data holding the presentations, the student wishes and the available rooms.
     * @return a list of the PlannedPresentations after the
     */
    public List<PlannedPresentation> execute(BoomData boomData) {
        return null;
    }

    /**
     * Calculates the maximum possible presentations based on the number of time slots available for a presentation.
     * e.g. 'D' to 'E' = maximal presentation amount 2
     *
     * @param offeredPresentation the offered presentation.
     * @return the amount.
     */
    private int getMaxPossiblePresentations(OfferedPresentation offeredPresentation) {
        return this.lastPossibleTimeslot - offeredPresentation.getEarliestTime() + 1;
    }

    /**
     * Calculates the amount of presentations based on the student wishes.
     * Sets the calculated value as amountOfPresentations in the offeredPresentation.
     *
     * @param boomData the BoomData.
     */
    protected void setRequiredPresentationAmount(BoomData boomData) {
        Map<OfferedPresentation, Integer> studentWishesMap = new HashMap<>();

        for (Student student : boomData.getStudents()) {
            for (OfferedPresentation wishedPresentation : student.getWishedPresentations()) {
                studentWishesMap.merge(wishedPresentation, 1, Integer::sum);
            }
        }

        for (Map.Entry<OfferedPresentation, Integer> presentationWishAmountEntry : studentWishesMap.entrySet()) {
            int amountOfPresentations = (presentationWishAmountEntry.getValue() + 19) / 20;

            int maxPossiblePresentations = getMaxPossiblePresentations(presentationWishAmountEntry.getKey());
            if (amountOfPresentations > maxPossiblePresentations) {
                amountOfPresentations = maxPossiblePresentations;
            }

            presentationWishAmountEntry.getKey().setAmountOfPresentations(amountOfPresentations);
        }
    }

    /**
     * Calculates the completion score of all students adn prints the percentage in the console and returns the value.
     *
     * @param boomData the data holding the presentations, the student wishes and the available rooms.
     * @return int as the percentage of all scores
     */
    protected int calculateCompletionScore(BoomData boomData) {
        List<Student> studentList = boomData.getStudents();
        int studentScore = 0;
        int totalScore = 0;
        for (Student student : studentList) {
            for (int i = 0; i <= 5; i++) {
                if (isWishGranted(student, i)) {
                    studentScore += (6 - i);
                }
            }
            totalScore += studentScore;
            studentScore = 0;
        }
        int scorePercentage = totalScore / studentList.size() * 5;
        System.out.println("Total score:" + scorePercentage + "%");
        return scorePercentage;
    }

    /**
     * Checks if a wish from a student was granted and returns the boolean.
     *
     * @param student the student where we want to check if the wish was granted.
     * @param i       index of the wish.
     * @return boolean
     */
    private boolean isWishGranted(Student student, int i) {
        boolean isWished = false;
        for (PlannedPresentation plannedPresentation : student.getPlannedPresentations()) {
            if (plannedPresentation.getOfferedPresentation().equals(student.getWishedPresentations().get(i))) {
                isWished = true;
                break;
            }
        }
        return isWished;
    }

    /**
     * Assigns a timeslot and room to each planned presentation based on availability and capacity requirements.
     * <p>
     * This method ensures that:
     * <ul>
     *   <li>Every planned presentation receives a room and a timeslot.</li>
     *   <li>If a company is not available during a timeslot, it will not be scheduled there (the earliestTime is considered).</li>
     *   <li>Rooms are filled with consecutive timeslots, avoiding gaps.</li>
     *   <li>A company is never assigned a new room – all its offered presentations are scheduled in the same room.</li>
     *   <li>For the company "Polizei", the room "aula" is always assigned.</li>
     *   <li>Before scheduling, the method checks if the offered presentation is already full
     *       (i.e. the number of planned presentations equals the amountOfPresentations).</li>
     * </ul>
     *
     * @param boomData the data containing the presentations, companies, students, and available rooms.
     */
    protected void setTimeslotAndRoom(BoomData boomData) {
        int totalTimeslots = lastPossibleTimeslot - 'A' + 1;

        // Create a schedule for each room (index 0 corresponds to 'A', 1 to 'B', etc.)
        Map<Room, boolean[]> roomSchedules = new HashMap<>();
        for (Room room : boomData.getRooms()) {
            roomSchedules.put(room, new boolean[totalTimeslots]); // false means free
        }

        // Map to store the assigned room for each company (using the company name)
        Map<String, Room> companyRoomAssignment = new HashMap<>();

        // Iterate over all companies
        for (Company company : boomData.getCompanies()) {
            Room assignedRoom = companyRoomAssignment.get(company.getName());
            if (assignedRoom == null) {
                if ("Polizei".equals(company.getName())) {
                    assignedRoom = getPolizeiRoom(boomData.getRooms());
                    if (assignedRoom == null) {
                        System.out.println("No suitable room 'aula' found for Polizei.");
                        continue;
                    }
                    companyRoomAssignment.put(company.getName(), assignedRoom);
                } else {
                    assignedRoom = findRoomForCompany(company, boomData.getRooms());
                    if (assignedRoom == null) {
                        System.out.println("No suitable room found for company: " + company.getName());
                        continue;
                    }
                    companyRoomAssignment.put(company.getName(), assignedRoom);
                }
            }

            // Retrieve the schedule of the assigned room
            boolean[] schedule = roomSchedules.get(assignedRoom);

            // Process all offered presentations of the company
            for (OfferedPresentation op : company.getOfferedPresentations()) {
                if (op.getPlannedPresentations() == null) {
                    op.setPlannedPresentations(new ArrayList<>());
                }
                // Mark already assigned timeslots in the room schedule
                markExistingTimeslots(op, schedule);

                int alreadyScheduled = op.getPlannedPresentations().size();
                int presentationsToSchedule = op.getAmountOfPresentations() - alreadyScheduled;
                if (presentationsToSchedule <= 0) {
                    continue;
                }

                int startIndex = op.getEarliestTime() - 'A';
                int blockStart = findContiguousBlock(schedule, startIndex, presentationsToSchedule, totalTimeslots);
                if (blockStart != -1) {
                    // Add a contiguous block of timeslots
                    for (int i = blockStart; i < blockStart + presentationsToSchedule; i++) {
                        schedule[i] = true;
                        char timeslot = (char) ('A' + i);
                        PlannedPresentation pp = new PlannedPresentation(timeslot, assignedRoom, op, new ArrayList<>());
                        op.getPlannedPresentations().add(pp);
                    }
                } else {
                    // Fallback: assign individual free timeslots starting from startIndex
                    int scheduledCount = assignIndividualTimeslots(schedule, startIndex, presentationsToSchedule, totalTimeslots, op, assignedRoom);
                    if (scheduledCount < presentationsToSchedule) {
                        System.out.println("Not all presentations for " + op.getTitle() + " could be scheduled.");
                    }
                }
            }
        }
    }

    /**
     * Searches the list of rooms for the room "aula" (case-insensitive).
     *
     * @param rooms list of available rooms.
     * @return the room "aula", or null if not found.
     */
    private Room getPolizeiRoom(List<Room> rooms) {
        for (Room room : rooms) {
            if (room.getRoomNumber().equalsIgnoreCase("aula")) {
                return room;
            }
        }
        return null;
    }

    /**
     * Finds a suitable room for the given company based on aggregated capacity requirements.
     *
     * @param company the company for which a room is being searched.
     * @param rooms   list of available rooms.
     * @return a matching room, or null if none is found.
     */
    private Room findRoomForCompany(Company company, List<Room> rooms) {
        int requiredMinCapacity = 0;
        int allowedMaxCapacity = Integer.MAX_VALUE;
        for (OfferedPresentation op : company.getOfferedPresentations()) {
            if (op.getMinCapacity() > requiredMinCapacity) {
                requiredMinCapacity = op.getMinCapacity();
            }
            if (op.getMaxCapacity() < allowedMaxCapacity) {
                allowedMaxCapacity = op.getMaxCapacity();
            }
        }
        // Search for a room that ideally meets both criteria
        for (Room room : rooms) {
            if (room.getCapacity() >= requiredMinCapacity && room.getCapacity() <= allowedMaxCapacity) {
                return room;
            }
        }
        // Fallback: choose a room that at least meets the minimum capacity
        for (Room room : rooms) {
            if (room.getCapacity() >= requiredMinCapacity) {
                return room;
            }
        }
        return null;
    }

    /**
     * Marks in the given schedule all timeslots that are already assigned from the offered presentation.
     *
     * @param op       the offered presentation.
     * @param schedule the boolean array representing the room schedule.
     */
    private void markExistingTimeslots(OfferedPresentation op, boolean[] schedule) {
        for (PlannedPresentation pp : op.getPlannedPresentations()) {
            int idx = pp.getTimeslot() - 'A';
            if (idx >= 0 && idx < schedule.length) {
                schedule[idx] = true;
            }
        }
    }

    /**
     * Searches for a contiguous block of free timeslots in the schedule.
     *
     * @param schedule       the boolean array of the room schedule.
     * @param startIndex     the starting index (based on earliestTime).
     * @param required       the required number of consecutive free slots.
     * @param totalTimeslots the total number of available timeslots.
     * @return the starting index of the block if found, otherwise -1.
     */
    private int findContiguousBlock(boolean[] schedule, int startIndex, int required, int totalTimeslots) {
        int blockStart = -1;
        int consecutiveFree = 0;
        for (int i = startIndex; i < totalTimeslots; i++) {
            if (!schedule[i]) {
                if (blockStart == -1) {
                    blockStart = i;
                }
                consecutiveFree++;
                if (consecutiveFree == required) {
                    return blockStart;
                }
            } else {
                blockStart = -1;
                consecutiveFree = 0;
            }
        }
        return -1;
    }

    /**
     * Assigns individual free timeslots starting from the given startIndex.
     *
     * @param schedule       the boolean array of the room schedule.
     * @param startIndex     the starting index (based on earliestTime).
     * @param required       the number of required free slots.
     * @param totalTimeslots the total number of available timeslots.
     * @param op             the offered presentation.
     * @param assignedRoom   the assigned room.
     * @return the number of successfully assigned slots.
     */
    private int assignIndividualTimeslots(boolean[] schedule, int startIndex, int required, int totalTimeslots, OfferedPresentation op, Room assignedRoom) {
        int scheduledCount = 0;
        for (int i = startIndex; i < totalTimeslots && scheduledCount < required; i++) {
            if (!schedule[i]) {
                schedule[i] = true;
                char timeslot = (char) ('A' + i);
                PlannedPresentation pp = new PlannedPresentation(timeslot, assignedRoom, op, new ArrayList<>());
                op.getPlannedPresentations().add(pp);
                scheduledCount++;
            }
        }
        return scheduledCount;
    }

}
