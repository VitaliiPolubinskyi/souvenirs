package crud;

import enteties.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IOHandler {

    private static IOHandler instance;

    private IOHandler() {}

    public static IOHandler createIOHandler() {
        if (instance == null) return new IOHandler();
        return instance;
    }

    // запис даних з списків у файл
    public boolean writeFileFromDatabases(List<Souvenir> sList, List<Manufacturer> mList, String path) throws RuntimeException {


        if (sList.isEmpty()) {
            System.out.println(Messages.EMPTY_LIST);
            throw new RuntimeException();
        }

        // оптимізація списку сувенірів перед записом у файл
        // щоб не було можливих дублікатів
        List <Souvenir> newSList = (List<Souvenir>) optimizeLists(sList);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, false))) {
            List<Souvenir> list = new ArrayList<>();
            for (Souvenir souvenir : newSList) {
                int id = souvenir.getId();
                String name = souvenir.getName();
                LocalDate date = souvenir.getDate();
                Double price = souvenir.getPrice();

                Manufacturer manufacturer = mList.stream()
                        .filter(o -> o.getId() == souvenir.getManufacturerId())
                        .findFirst().get();
                list.add(new Souvenir(id, name, manufacturer, date, price));
            }

            //якщо поля null - то замінюємо їх на пусту строку
            String information = list.stream()
                    .map(o -> "souvenirName = " + o.getName() + ", " +
                            "manufacturerName = " + o.getManufacturer().getName() + ", " +
                            "manufacturerCountry = " + (o.getManufacturer().getCountry() == null ?
                            "" : o.getManufacturer().getCountry()) + ", " +
                            "date = " + (o.getDate() == null ? "" : o.getDate()) + ", " +
                            "price = " + (o.getPrice() == null ? "" : o.getPrice()))
                    .collect(Collectors.joining(System.lineSeparator()));
            writer.write(information);
            writer.flush();
        } catch (IOException | RuntimeException e) {
            throw new RuntimeException();
        }
        return true;
    }

    // створення двох списків з файлу
    public boolean createDatabaseFromFile(List<Souvenir> sList, List<Manufacturer> mList, String path) throws
            RuntimeException {

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.length() == 0) continue; // пропускаємо пусті строки у файлі
                String[] array = line.split(", ");

                Map<String, String> map = Stream.of(array)
                        .map(x -> x.split(" = "))
                        .filter(e -> e.length != 1) //тут відкидаємо всі поля, для яких немає значень
                        .collect(Collectors.toMap(e -> e[0], e -> e[1]));

                if (!map.containsKey("souvenirName")) {
                    System.out.println("Souvenir name mustn't be empty!");
                    throw new RuntimeException();
                }
                if (!map.containsKey("manufacturerName")) {
                    System.out.println("Manufacturer name mustn't be empty!");
                    throw new RuntimeException();
                }

                if (!createInstancesFromMap(sList, mList, map)) {
                    throw new RuntimeException();
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(Messages.FILE_NOT_FOUND);
            throw new RuntimeException();
        } catch (IOException e) {
            System.out.println(Messages.READING_ERROR);
            throw new RuntimeException();
        } catch (RuntimeException e) {
            System.out.println(Messages.INVALID_FORMAT);
            throw new RuntimeException();
        }
        return true;
    }

    // обробка отриманої мапи з BufferedReader
    private boolean createInstancesFromMap
    (List<Souvenir> sList, List<Manufacturer> mList, Map<String, String> map) throws RuntimeException {

        Souvenir souvenir = new Souvenir();
        Manufacturer manufacturer = new Manufacturer();

        // новий айді для сутностей - останній айді + 1,
        // якщо такого немає (список пустий) - то тоді 1,
        // в такому випадку айді буде завжди унікальним, не зважаючи
        // на будь-які операції зі списком - додавання, редагування,
        // видалення (у будь-якому місці списка) елементів списку
        int souvenirId = sList.isEmpty() ? 1 : sList.get(sList.size() - 1).getId() + 1;
        int manufacturerId = mList.isEmpty() ? 1 : mList.get(mList.size() - 1).getId() + 1;

        //заповнюємо поля екземплярів з даних отриманої мапи
        for (Map.Entry<String, String> pair : map.entrySet()) {
            if (pair.getKey().equals("souvenirName")) {
                souvenir.setName(pair.getValue());
            } else if (pair.getKey().equals("manufacturerName")) {
                manufacturer.setName(pair.getValue());
            } else if (pair.getKey().equals("manufacturerCountry")) {
                manufacturer.setCountry(pair.getValue());
            } else if (pair.getKey().equals("date")) {
                souvenir.setDate(LocalDate.parse(pair.getValue()));
            } else if (pair.getKey().equals("price")) {
                souvenir.setPrice(new Double(pair.getValue()));
            }
        }

        //перевірка, чи є вже такі обєкти в списках, якщо є - не заносимо їх знову
        if (mList.contains(manufacturer)) {
            manufacturer = mList.get(mList.indexOf(manufacturer));
        } else {
            manufacturer.setId(manufacturerId);
            mList.add(manufacturer);
        }

        souvenir.setManufacturerId(manufacturer.getId());

        if (!sList.contains(souvenir)) {
            souvenir.setId(souvenirId);
            sList.add(souvenir);
        }
        return true;
    }

    // для оптимізації списків після редагування -
    // якщо будуть однакові строки, то даний метод
    // їх видаляє
    public List<? extends Item> optimizeLists(List<? extends Item> list) {
        if (list.size() > 1) {
            return list.stream()
                    .distinct()
                    .sorted(Comparator.comparingInt(Item::getId))
                    .collect(Collectors.toList());
        }
        return list;
    }
}
