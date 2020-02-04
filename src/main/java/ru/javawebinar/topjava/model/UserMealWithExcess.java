package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class UserMealWithExcess {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final static Map<LocalDate, Integer> totalCaloriesPerDay = new HashMap<>(); //if Integer < 0 - calories are exceeded

    private final boolean excess;

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public static int getCaloriesPerDay(LocalDate crDate){
        return totalCaloriesPerDay.get(crDate);
    }

    public static int addCaloriesPerDay(LocalDate crDate, int calories){ //return new value of calories per day

        if (totalCaloriesPerDay.containsKey(crDate)){
            totalCaloriesPerDay.put(crDate, (totalCaloriesPerDay.get(crDate)<0)?
                    (totalCaloriesPerDay.get(crDate)-calories):(totalCaloriesPerDay.get(crDate)+calories));
        } else {
            totalCaloriesPerDay.put(crDate, calories);
        }
        return totalCaloriesPerDay.get(crDate);
    }

    public static void setCaloriesIsExceeded(LocalDate crDate){
        totalCaloriesPerDay.put(crDate, (totalCaloriesPerDay.get(crDate)<0)?
                totalCaloriesPerDay.get(crDate): -totalCaloriesPerDay.get(crDate));
    }

    @Override
    public String toString() {

        boolean excess = totalCaloriesPerDay.get(dateTime.toLocalDate()) < 0;

        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}
