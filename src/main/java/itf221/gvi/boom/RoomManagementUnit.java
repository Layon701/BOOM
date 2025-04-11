package itf221.gvi.boom;

import itf221.gvi.boom.data.*;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
public class RoomManagementUnit {

    private final char lastPossibleTimeslot = 'E';

    /**
     * List for students who still have free slots after the distribution.
     */
    private final ArrayList<Student> unfulfilledStudents = new ArrayList<>();

    /**
     * <b>Deterministic</b> algorithm for distributing the offered presentations among the students considering their wishes.
     *
     * @param boomData the data holding the presentations, the student wishes and the available rooms.
     */
    public double execute(BoomData boomData) {
        setRequiredPresentationAmount(boomData);
        instantiatePlannedPresentations(boomData.getCompanies());

        pseudoRandomizeStudentList(boomData.getStudents());
        distributeStudentsOnPlannedPresentations(boomData.getStudents());
        distributeUnfulfilledStudents(boomData);

        setTimeslotAndRoom(boomData);
        return calculateCompletionScore(boomData);
    }

    /**
     * @param students the student list.
     */
    protected void distributeStudentsOnPlannedPresentations(List<Student> students) {
        for (int i = 0; i < 5; i++) {
            for (Student student : students) {
                PlannedPresentation wishedPlannedPresentation =
                        student.getWishedPresentations().get(i).getPlannedPresentationWithLeastAmountOfAttendees();

                addStudentToPresentation(student, wishedPlannedPresentation);
            }
        }
    }

    /**
     * Adds student to the planned presentation. If the wish can not be granted the 6th wish will be used as a supplement.
     * <p>
     * In case that the 6th wish is already planned for that student or the 6th presentation is already full,
     * the student will be added to a list of unfulfilled students.
     *
     * @param student            the student.
     * @param wishedPresentation the wish.
     */
    protected void addStudentToPresentation(Student student, PlannedPresentation wishedPresentation) {
        if (!wishedPresentation.addStudent(student)) {
            boolean alreadyHas6thWish = student.getPlannedPresentations().stream()
                    .anyMatch(plannedPresentation ->
                            plannedPresentation.getOfferedPresentation().equals(student.getWishedPresentations().get(5))
                    );

            if (!alreadyHas6thWish) {
                addStudentToPresentation(student, student.getWishedPresentations().get(5).getPlannedPresentationWithLeastAmountOfAttendees());
            } else {
                this.unfulfilledStudents.add(student);
            }
        }
    }

    /**
     * Distributes remaining students (unfulfilled students) to critical planned presentations.
     * A planned presentation is considered critical when it has < min participants (but > 0).
     * This has the purpose of making those presentations possible for the other students.
     *
     * @param boomData the data.
     */
    protected void distributeUnfulfilledStudents(BoomData boomData) {
        // get all planned presentations from companies
        List<PlannedPresentation> allPlannedPresentations = getAllPlannedPresentations(boomData);

        // get planned presentations that are close to be held
        List<PlannedPresentation> almostRealizablePresentations = new ArrayList<>(allPlannedPresentations.stream()
                .filter(pres -> pres.getAttendees().size() < pres.getOfferedPresentation().getMinCapacity())
                .toList());

        // put the value closest to min capacity to beginning
        almostRealizablePresentations.sort(
                Comparator.comparingInt((PlannedPresentation pres) -> pres.getAttendees().size()).reversed()
        );

        // distribute unfilled students on the almost realizable Presentations
        Iterator<Student> studentIterator = unfulfilledStudents.iterator();
        while (studentIterator.hasNext()) {
            Student student = studentIterator.next();
            if (!almostRealizablePresentations.isEmpty()) {
                addToNextUnfilledPresentation(almostRealizablePresentations, student);
                studentIterator.remove();
            } else {
                for (PlannedPresentation pres : allPlannedPresentations) {
                    if (pres.addStudent(student)) {
                        break;
                    }
                }
            }
        }

        // collect remaining students from the almostRealizablePresentations and distribute those on next open spots
        redistributeStudentsFromUnrealizablePresentations(almostRealizablePresentations, allPlannedPresentations);
    }

    /**
     * @param almostRealizablePresentations the presentations that are under the min capacity.
     * @param student                       the student to add.
     */
    protected void addToNextUnfilledPresentation(List<PlannedPresentation> almostRealizablePresentations, Student student) {
        PlannedPresentation nextAlmostRealizablePresentation = almostRealizablePresentations.getFirst();

        nextAlmostRealizablePresentation.addStudent(student);
        unfulfilledStudents.remove(student);

        if (nextAlmostRealizablePresentation.getAttendees().size() >= nextAlmostRealizablePresentation.getOfferedPresentation().getMinCapacity()) {
            almostRealizablePresentations.remove(nextAlmostRealizablePresentation);
        }
    }

    /**
     * Distributes students from unrealizable presentations to other open slots.
     *
     * @param unrealizablePresentations Presentations that have not reached the minimum number of participants.
     * @param allPresentations          All scheduled presentations.
     */
    private void redistributeStudentsFromUnrealizablePresentations(List<PlannedPresentation> unrealizablePresentations,
                                                                   List<PlannedPresentation> allPresentations) {
        for (PlannedPresentation pres : unrealizablePresentations) {
            List<Student> studentsToReassign = new ArrayList<>(pres.getAttendees());
            for (Student student : studentsToReassign) {
                for (PlannedPresentation targetPres : allPresentations) {
                    if (targetPres != pres && targetPres.addStudent(student)) {
                        pres.removeStudent(student);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Collects all planned presentations from the companies.
     */
    private List<PlannedPresentation> getAllPlannedPresentations(BoomData boomData) {
        List<PlannedPresentation> presentations = new ArrayList<>();
        for (Company company : boomData.getCompanies()) {
            for (OfferedPresentation offered : company.getOfferedPresentations()) {
                presentations.addAll(offered.getPlannedPresentations());
            }
        }
        return presentations;
    }

    /**
     * Pseudo-randomizes given list.
     * <p>
     * Random uses a mathematical formula to generate numbers that appear random but are entirely determined by the seed.
     *
     * @param students the student list.
     */
    protected void pseudoRandomizeStudentList(List<Student> students) {
        Random random = new Random(12345);
        Collections.shuffle(students, random);
    }

    /**
     * Initializes the PlannedPresentations and adds them into the according OfferedPresentation.
     *
     * @param companies the company list.
     */
    protected void instantiatePlannedPresentations(List<Company> companies) {
        for (Company company : companies) {
            for (OfferedPresentation offeredPresentation : company.getOfferedPresentations()) {
                List<PlannedPresentation> plannedPresentations = new ArrayList<>(offeredPresentation.getAmountOfPresentations());
                for (int i = 0; i < offeredPresentation.getAmountOfPresentations(); i++) {
                    plannedPresentations.add(new PlannedPresentation(offeredPresentation));
                }
                offeredPresentation.setPlannedPresentations(plannedPresentations);
            }
        }
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
     * Calculates the completion score of all students, prints the percentage in the console and returns the value.
     *
     * @param boomData the data holding the presentations, the student wishes and the available rooms.
     * @return double as the percentage of all scores
     */
    protected double calculateCompletionScore(BoomData boomData) {
        List<Student> studentList = boomData.getStudents();
        int totalScore = 0;

        for (Student student : studentList) {
            int studentScore = 0;
            for (int i = 0; i <= 5; i++) {
                if (isWishGranted(student, i)) {
                    studentScore += (6 - i);
                }
            }
            totalScore += studentScore;
        }

        double scorePercentage = totalScore * 5.0 / studentList.size();
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
     * Assigns a room and a timeslot for every planned presentation based on the following criteria:
     * <ul>
     *     <li>Every planned presentation is given a timeslot (from 'A' to 'E') that is not earlier than its offered presentation’s earliest allowed time.</li>
     *     <li>All planned presentations of a given offered presentation (identified by its unique ID) are scheduled in the same room.</li>
     *     <li>The room is selected and timeslots are chosen so that gaps in the room’s schedule are minimized.</li>
     *     <li>If the offered presentation’s company (indicated by companyName) is "Polizei", the room with roomNumber "Aula" is used.</li>
     *     <li>For all other offered presentations, only rooms other than "Aula" and ones with too little capacity are considered .</li>
     *     <li>whereby the smallest rooms have processing priority in order to maximize room capacities</li>
     * </ul>
     *
     * @param boomData the data holding the companies (and their offered presentations), planned presentations, and available rooms.
     * @throws IllegalStateException if no candidate room is found for an offered presentation.
     */
    public void setTimeslotAndRoom(BoomData boomData) {
        // Create a schedule for each room: an array for timeslots 'A' to 'E' (indexes 0 to 4).
        // False indicates the timeslot is free.
        Map<Room, boolean[]> roomSchedules = new HashMap<>();
        for (Room room : boomData.getRooms()) {
            roomSchedules.put(room, new boolean[5]);
        }

        // Locate the room "Aula" (used exclusively by Polizei).
        Room aulaRoom = null;
        for (Room room : boomData.getRooms()) {
            if ("Aula".equals(room.getRoomNumber())) {
                aulaRoom = room;
                break;
            }
        }

        // Process every offered presentation individually.
        // Each OfferedPresentation is obtained via each Company.
        for (Company company : boomData.getCompanies()) {
            for (OfferedPresentation offered : company.getOfferedPresentations()) {
                List<PlannedPresentation> plannedPresentations = offered.getPlannedPresentations();
                if (plannedPresentations == null || plannedPresentations.isEmpty()) {
                    continue; // nothing to schedule
                }

                int maxStudentsOfPres = plannedPresentations.stream()
                        .mapToInt(p -> p.getAttendees().size())
                        .max()
                        .orElse(0);

                // Determine the earliest allowed timeslot index for these presentations.
                // Example: if earliestTime is 'C', then earliestSlot = 'C' - 'A' = 2.
                int earliestSlot = offered.getEarliestTime() - 'A';

                // Determine candidate rooms:
                // - For "Polizei", use only the Aula.
                // - For other offered presentations, consider all rooms except Aula.
                List<Room> candidateRooms = new ArrayList<>();
                if ("Polizei".equals(offered.getCompanyName())) {
                    if (aulaRoom == null) {
                        throw new IllegalStateException("Room 'Aula' not found for Polizei.");
                    }
                    candidateRooms.add(aulaRoom);
                } else {
                    for (Room room : boomData.getRooms()) {
                        if (!"Aula".equals(room.getRoomNumber()) || maxStudentsOfPres < room.getCapacity()) {
                            candidateRooms.add(room);
                        }
                    }
                }

                // Process priority for the smallest rooms
                candidateRooms.sort(Comparator.comparingInt(Room::getCapacity));

                // Try to schedule all planned presentations in one room.
                Room bestRoom = null;
                List<Integer> bestTimeslots = null;
                int minimalGap = Integer.MAX_VALUE;

                // Evaluate each candidate room.
                for (Room candidate : candidateRooms) {
                    boolean[] schedule = roomSchedules.get(candidate);
                    List<Integer> assignedSlots = new ArrayList<>();
                    int lastAssignedSlot = -1;
                    boolean candidateFits = true;

                    // For each planned presentation, choose the earliest free slot in the candidate room that
                    // (a) is not before the offered presentation’s earliest time and
                    // (b) comes after the previously assigned slot for this offered presentation.
                    for (int i = 0; i < plannedPresentations.size(); i++) {
                        int minSlot = Math.max(earliestSlot, lastAssignedSlot + 1);
                        int foundSlot = -1;
                        for (int t = minSlot; t < 5; t++) {
                            if (!schedule[t]) {
                                foundSlot = t;
                                break;
                            }
                        }
                        if (foundSlot == -1) {
                            candidateFits = false;
                            break;
                        } else {
                            assignedSlots.add(foundSlot);
                            lastAssignedSlot = foundSlot;
                        }
                    }

                    // If the candidate room fits, compute the gap of assigned slots.
                    // The gap is defined as (lastSlot - firstSlot) - (numberOfPresentations - 1),
                    // meaning how many unused slots exist between the first and last assignments.
                    if (candidateFits) {
                        int gap = assignedSlots.getLast() - assignedSlots.getFirst() - (assignedSlots.size() - 1);
                        if (gap < minimalGap) {
                            minimalGap = gap;
                            bestRoom = candidate;
                            bestTimeslots = assignedSlots;
                        }
                    }
                }

                if (bestRoom == null) {
                    throw new IllegalStateException("Unable to schedule OfferedPresentation ID " + offered.getId());
                }

                // Mark the timeslots as used in the chosen room and update each planned presentation.
                boolean[] bestRoomSchedule = roomSchedules.get(bestRoom);
                for (int i = 0; i < plannedPresentations.size(); i++) {
                    int slot = bestTimeslots.get(i);
                    bestRoomSchedule[slot] = true; // reserve this timeslot in the room
                    plannedPresentations.get(i).setRoom(bestRoom);
                    plannedPresentations.get(i).setTimeslot((char) ('A' + slot));
                }
            }
        }
    }


}
