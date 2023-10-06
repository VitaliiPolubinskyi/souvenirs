package crud;

import enteties.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class CRUD {

    List<Souvenir> souvenirList = new ArrayList<>();
    List<Manufacturer> manufacturerList = new ArrayList<>();


    private static CRUD instance;

    private CRUD() {}

    public static CRUD createCRUD() {
        if (instance == null) return new CRUD();
        return instance;
    }


    //Загальні методи

    //-------------------------------------------------------------------------------------------
    // Переглядаємо списки сувенірів чи виробників
    public void showList(List<? extends Item> list) {
        list.forEach(System.out::println);
    }

    // Видаляємо списки всіх сувенірів та виробників
    public void removeList(List<? extends Item> list) {
        list.clear();
    }

    // Перевіряємо наявність сувеніру чи виробника в списку за введеним id і виводимо його
    public boolean isInstancePresentInList(List<? extends Item> list, String id)
            throws RuntimeException {
        int number = Integer.parseInt(id);

        if (list.stream().anyMatch(o -> o.getId() == number)) {
            System.out.println(list.get(list.indexOf(list.stream()
                    .filter(o -> o.getId() == number).findAny().get())));
            return true;
        }
        return false;
    }

    //-------------------------------------------------------------------------------------------

    // Тільки для сувенірів

    //-------------------------------------------------------------------------------------------
    // Видаляємо певний сувенір
    public boolean removeSouvenirById(List<Souvenir> list, String id) throws RuntimeException {
        Souvenir souvenir = list.stream()
                .filter(o -> o.getId() == Integer.parseInt(id))
                .findAny()
                .orElse(new Souvenir(0, null, 0, null, null));
        return list.remove(souvenir);
    }

    // Додаємо сувеніри до списку, якщо він не унікальний
    // то додавання не буде,
    // автоматично онолюється список з виробниками, якщо такого ще немає
    public boolean insertSouvenir(List<Souvenir> sList, List<Manufacturer> mList, String[] args)
            throws RuntimeException {

        int souvenirId = sList.isEmpty() ? 1 : sList.get(sList.size() - 1).getId() + 1;
        int manufacturerId = mList.isEmpty() ? 1 : mList.get(mList.size() - 1).getId() + 1;

        String souvenirName = args[0];
        String manufacturerName = args[1];
        String manufacturerCountry = args[2].isEmpty() ? null : args[2];
        LocalDate date = args[3].isEmpty() ? null : LocalDate.parse(args[3]);
        Double price = args[4].isEmpty() ? null : new Double(args[4]);

        Souvenir souvenir = new Souvenir();
        Manufacturer manufacturer = new Manufacturer();

        manufacturer.setName(manufacturerName);
        manufacturer.setCountry(manufacturerCountry);

        souvenir.setName(souvenirName);
        souvenir.setDate(date);
        souvenir.setPrice(price);

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
        } else {
            return false;
        }
        return true;
    }

    // Виводимо сувеніри за введеним id виробника
    public boolean showSouvenirsByManufacturer(List<Souvenir> sList, List<Manufacturer> mList,
                                               String name, String country) throws RuntimeException {

        if (name.matches("^[\\s]*$")) {
            System.out.println("Manufacturer name mustn't be empty!");
            throw new RuntimeException();
        }

        int manufacturerId = mList.stream()
                .filter(o -> o.getName().equals(name) && o.getCountry().equals(country))
                .map(Manufacturer::getId).findAny().orElse(-1);

        if (sList.stream().anyMatch(o -> o.getManufacturerId() == manufacturerId)) {
            sList.stream()
                    .filter(o -> o.getManufacturerId() == manufacturerId)
                    .forEach(System.out::println);
            return true;
        }
        return false;
    }

    // Виводимо сувеніри за введеною назвою країни, якщо нічого не ввести,
    // то виведе інформацію для країн, які є null в списку
    public boolean showSouvenirsByCountry(List<Souvenir> sList, List<Manufacturer> mList,
                                          String name) throws RuntimeException {

        String finalName = name.matches("^[\\s]*$") ? null : name;

        Set<Integer> idSet =
                mList.stream()
                        .filter(o -> Objects.equals(o.getCountry(), finalName))
                        .map(Manufacturer::getId)
                        .collect(Collectors.toSet());

        if (idSet.size() == 0) {
            throw new RuntimeException();
        }
        for (int id : idSet) {
            isInstancePresentInList(sList, String.valueOf(id));
        }
        return true;
    }

    // Оновлюємо інформацію про сувеніри та автоматично додаємо
    // виробника списку виробників, якщо такого ще немає
    public boolean setNewSouvenirValue(List<Souvenir> sList, List<Manufacturer> mList,
                                       String... args) throws RuntimeException {

        Souvenir souvenir = new Souvenir(Integer.parseInt(args[0]), sList.stream()
                .filter(o -> o.getId() == Integer.parseInt(args[0]))
                .findAny().get());

        int listId = sList.indexOf(souvenir);
        int manufacturerId = sList.get(listId).getManufacturerId();

        Manufacturer manufacturer =
                new Manufacturer(manufacturerId, mList.stream()
                        .filter(o -> o.getId() == manufacturerId)
                        .findAny().get());

        switch (args[1]) {
            case "1":
                souvenir.setName(args[2]);
                break;
            case "2":
                manufacturer.setName(args[2]);
                manufacturer.setCountry(args[3].isEmpty() ? null : args[3]);
                if (mList.contains(manufacturer)) {
                    int newManufacturerId = mList.get(mList.indexOf(manufacturer)).getId();
                    souvenir.setManufacturerId(newManufacturerId);
                } else {
                    int newManufacturerId = mList.isEmpty() ? 1 : mList.get(mList.size() - 1).getId() + 1;
                    souvenir.setManufacturerId(newManufacturerId);
                    manufacturer.setId(newManufacturerId);
                    mList.add(manufacturer);
                }
                break;
            case "3":
                souvenir.setDate(args[2].isEmpty() ? null : LocalDate.parse(args[2]));
                break;
            case "4":
                souvenir.setPrice(args[2].isEmpty() ? null : new Double(args[2]));
                break;
        }

        sList.set(listId, souvenir);
        return true;
    }

    // Виводимо інформацію про сувеніри, згрупованих по роках
    public void showSouvenirsGroupedByYear(List<Souvenir> list) {

        Set<Integer> years =
                list.stream()
                        .map(o -> Objects.equals(o.getDate(), null) ? null : o.getDate().getYear())
                        .collect(Collectors.toSet());

        for (Integer year : years) {
            if (year == null) {
                System.out.println("Information of souvenirs, produced in unknown year");
                list.stream()
                        .filter(o -> o.getDate() == null)
                        .forEach(System.out::println);
                System.out.println();
            } else {
                System.out.printf("Information of souvenirs, produced in %s year\n", year);
                list.stream()
                        .filter(o -> o.getDate() != null)
                        .filter(o -> o.getDate().getYear() == year)
                        .forEach(System.out::println);
                System.out.println();
            }
        }
    }

    //-----------------------------------------------------------------------------------------------

    // Тільки для виробників

    //----------------------------------------------------------------------------------------------------

    // Додаємо виробників до списку
    public boolean insertManufacturer(List<Manufacturer> mList,
                                      String[] args) throws RuntimeException {

        Manufacturer manufacturer = new Manufacturer();

        int id = mList.isEmpty() ? 1 : mList.get(mList.size() - 1).getId() + 1;
        String name = args[0];
        String country = args[1].isEmpty() ? null : args[1];

        manufacturer.setId(id);
        manufacturer.setName(name);
        manufacturer.setCountry(country);

        if (!mList.contains(manufacturer)) {
            mList.add(manufacturer);
            return true;
        }

        return false;
    }

    // Видаляємо вказаних виробників і їх сувеніри,
    // при видаленні виробника сувеніри з відповідними айді виробників,
    // які видаляються, теж мають видалитися автоматично
    public boolean removeManufacturerById(List<Souvenir> sList,
                                          List<Manufacturer> mList, String id) throws RuntimeException {

        if (mList.stream().anyMatch(o -> o.getId() == Integer.parseInt(id))) {
            mList.removeIf(o -> o.getId() == Integer.parseInt(id));
            sList.removeIf(o -> o.getManufacturerId() == Integer.parseInt(id));
            return true;
        }
        return false;
    }

    // Оновлюємо інформацію про виробників, якщо ми отримуємо
    // таким чином два однакових виробника, то в списку сувенірів
    // оновлюється id виробника для сувеніра на менший зі збігів,
    // потім виробник з більшим id видаляється зі списку сувенірів
    public boolean setNewManufacturerValue(List<Souvenir> sList, List<Manufacturer> mList, String id,
                                           String fieldToUpdate, String value) throws RuntimeException {

        Manufacturer manufacturer =
                new Manufacturer(Integer.parseInt(id), mList.stream()
                        .filter(o -> o.getId() == Integer.parseInt(id))
                        .findAny().get());

        int listId = mList.indexOf(manufacturer);

        switch (fieldToUpdate) {
            case "1":
                manufacturer.setName(value);
                break;
            case "2":
                manufacturer.setCountry(value.isEmpty() ? null : value);
                break;
        }

        mList.set(listId, manufacturer);

        // шукаємо айді однакових виробників,
        // якщо такий з'явилися після редагування
        int[] arrayOfMSameIds = mList.stream()
                .filter(o -> o.equals(manufacturer))
                .mapToInt(Manufacturer::getId)
                .toArray();

        // замінюємо індекси однакових виробників в списку сувенірів на
        // найменший, все це потрібно зробити для безпечного
        // виконання оптимізації списку
        // Якщо цього не зробити, може виникнути ситуація, що при
        // оптимізації видаляться повтори для списку виробників
        // (залишиться тілки той, що буде в списку першим),
        // але посилання на них залишаться в списку сувенірів, що
        // призведе до порушення цілісності даних
        // Якщо ми будемо завжди мати посилання, як найменший індекс,
        // то при оптимізації списків такі виробники залишаться
        // і цілісніть списків не порушиться
        if (arrayOfMSameIds.length == 1) { // це і є наш один виробник, якого ми редагували
            return true;
        }

        // визначаємо менший айді виробника із значень для
        // об'єкту, який ми редагували та того, який був
        // знайдений у списку виробників як однаковий
        Arrays.sort(arrayOfMSameIds);
        int newMId = arrayOfMSameIds[0];

        // замінюємо більші айді виробника у списку сувенірів
        // на визначений нами менший для однакових виробників
        Arrays.stream(arrayOfMSameIds)
                .forEach(arrayValue -> sList.stream()
                        .filter(souvenir -> souvenir.getManufacturerId() == arrayValue)
                        .forEach(souvenir -> souvenir.setManufacturerId(newMId)));
        return true;
    }

    // Виводимо інформацію про виробників, якщо ціна сувенірів,
    // які вони виробляють, менша за вказану
    public boolean showManufacturersByLessSouvenirPrice
    (List<Souvenir> sList, List<Manufacturer> mList, String price) throws RuntimeException {

        double value = Double.parseDouble(price);
        if (value < 0) {
            System.out.println("Price mustn't be less, then 0!");
            throw new RuntimeException();
        }

        Set<Integer> manufacturersId = sList.stream()
                .filter(o -> o.getPrice() != null)
                .filter(o -> o.getPrice() < value)
                .map(Souvenir::getManufacturerId)
                .collect(Collectors.toSet());

        if (manufacturersId.isEmpty()) {
            return false;
        }
        for (Integer id : manufacturersId) {
            mList.stream().filter(o -> o.getId() == id).forEach(System.out::println);
        }
        return true;
    }

    // Виводимо інформацію про виробників і всі їх сувеніри
    public void showAllManufacturersAndTheirSouvenirs(List<Souvenir> sList, List<Manufacturer> mList) {

        for (Manufacturer manufacturer : mList) {
            System.out.println("Manufacturer name: " + manufacturer.getName() + ", manufacturer country: " + manufacturer.getCountry());
            System.out.println("Souvenirs produced by this manufacturer: ");
            sList.stream()
                    .filter(o -> o.getManufacturerId() == manufacturer.getId())
                    .forEach(o -> System.out.printf("[Souvenir name: %s, date: %s, price: %f], ", o.getName(), o.getDate(), o.getPrice()));
            System.out.println();
        }
    }

    // Виводимо інформацію про виробників за вказаними
    // сувенірами, виробленими у вказаному році
    public boolean manufacturerInfoBySouvenirNameAndYear(List<Souvenir> sList, List<Manufacturer> mList,
                                                         String name, String year) throws RuntimeException {

        if (name.matches("^[\\s]*$")) {
            System.out.println("Souvenir name mustn't be empty!");
            throw new RuntimeException();
        }

        String finalYear = year.matches("^[\\s]*$") ? null : year;

        if (finalYear != null) {
            if (Integer.parseInt(finalYear) > LocalDate.now().getYear()) {
                System.out.println("Entered year have to be earlier or equals current year!");
                throw new RuntimeException();
            }
        }

        Set<Integer> manufacturersId;

        if (finalYear == null) {
            manufacturersId = sList.stream()
                    .filter(o -> o.getName().equals(name))
                    .filter(o -> o.getDate() == null)
                    .map(Souvenir::getManufacturerId)
                    .collect(Collectors.toSet());
        } else {
            manufacturersId = sList.stream()
                    .filter(o -> o.getName().equals(name))
                    .filter(o -> o.getDate() != null)
                    .filter(o -> o.getDate().getYear() == Integer.parseInt(finalYear))
                    .map(Souvenir::getManufacturerId)
                    .collect(Collectors.toSet());
        }

        if (manufacturersId.size() == 0) return false;

        System.out.printf("Information about manufacturers for souvenir %s made in %s year\n", name, finalYear);

        for (Integer id : manufacturersId) {
            mList.stream().filter(o -> o.getId() == id).forEach(System.out::println);
        }
        return true;

    }



}
