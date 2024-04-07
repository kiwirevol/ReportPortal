package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private  DataGenerator() {

    }

    public static int random() { // генерация случайного числа от 3 до 30
        double random = Math.random() * 28;
        int rnd = (int) random + 3;
        return rnd;
    }

    public static String genDate(int addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String genCity() {
        var cities = new String[]{
                "Архангельск", "Белгород", "Волгоград", "Екатеринбург", "Краснодар",
                "Липецк", "Москва", "Новосибирск", "Омск", "Пенза", "Рязань", "Самара",
                "Тюмень", "Ульяновск", "Челябинск", "Ярославль"};
        return cities[new Random().nextInt(cities.length)];
    }

    public static String genName(String locale) {
        var faker = new Faker(new Locale("ru"));
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    public static String genPhone(String locale) {
        var faker = new Faker(new Locale("ru"));
        return faker.phoneNumber().phoneNumber();
    }

    public static class Registration {
        private Registration() {
        }
        public static UserInfo genUser(String locale) {
            return new UserInfo(genCity(), genName(locale), genPhone(locale));
        }
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }

}