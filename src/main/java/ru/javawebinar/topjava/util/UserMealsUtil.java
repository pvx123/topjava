package ru.javawebinar.topjava.util;

import java.util.*;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.Month;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(13, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> totalCaloriesPerDay = new HashMap<>();

        meals.forEach(meal -> totalCaloriesPerDay.merge(meal.getDateTime().toLocalDate(),
                meal.getCalories(), Integer::sum)); //total calories per day

/*        for (UserMeal meal : meals) { //total calories per day
              // getOrDefault case
//            totalCaloriesPerDay.put(meal.getDateTime().toLocalDate(), totalCaloriesPerDay.getOrDefault(meal.getDateTime().toLocalDate(), 0) + meal.getCalories());


            if (totalCaloriesPerDay.containsKey(meal.getDateTime().toLocalDate())) {
            totalCaloriesPerDay.put(meal.getDateTime().toLocalDate(), totalCaloriesPerDay.get(meal.getDateTime().toLocalDate()) + meal.getCalories());
            } else {
                totalCaloriesPerDay.put(meal.getDateTime().toLocalDate(), meal.getCalories());
            }

        }
*/
        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();

        for (UserMeal meal : meals) { //copy from UserMeal to UserMealWithExcess
            if (TimeUtil.isBetweenInclusive(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealsWithExcess.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription()
                        , meal.getCalories(), totalCaloriesPerDay.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }


        return mealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> totalCaloriesPerDay = meals.stream()
                .collect(Collectors.groupingBy(UserMeal::getDate,
                        Collectors.summingInt(UserMeal::getCalories)));


        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenInclusive(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(meal,
                        totalCaloriesPerDay.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}