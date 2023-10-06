package crud;

import enteties.*;

import java.util.*;


public class ProgramInterface {

    private static Scanner scanner = new Scanner(System.in);
    private static CRUD crud = CRUD.createCRUD();
    private static IOHandler ioHandler = IOHandler.createIOHandler();
    private static List<Souvenir> copySList;
    private static List<Manufacturer> copyMList;

    public void run() {

        while (true) {
            System.out.println(Messages.INTERNAL_MENU);
            String answer1 = scanner.nextLine();
            if (checkFirstAnswer(answer1)) {
                System.out.println(Messages.INVALID_ANSWER);
                continue;
            }
            if (answer1.equals("1")) {  //перша відвопідь
                createLists();
                continue;
            }
            if (answer1.equals("2")) {  //друга відповідь
                if(doCRUDAction().equals("exit")) {
                    System.out.println(Messages.GOODBYE);
                    return;
                }
                continue;
            }
            if (answer1.equals("3")) { //третя відповідь
                createFile();
                continue;
            }
            if (answer1.equals("4")) { //відкат до попереднього вигляду списків
                crud.souvenirList = copySList;
                crud.manufacturerList = copyMList;
                System.out.println(Messages.SUCCESS);
                continue;
            }
            if (answer1.equals("5")) { // вихід
                System.out.println(Messages.GOODBYE);
                return;
            }
            if (answer1.equals("0")) { // оптимізація списків
                optimizeLists();
            }
        }
    }

    // метод створює перед виконанням копію початкових списків
    private void createLists() {

        // при створенні списків з файлу завжди робимо копію перед зчинуванням -
        // якщо щось піде не так, іде відкат до попереднього вигляду списків
        copySList = new ArrayList<>(crud.souvenirList);
        copyMList = new ArrayList<>(crud.manufacturerList);

        while (true) {
            System.out.println(Messages.FILE_PATH);
            String filePath = scanner.nextLine();
            if (filePath.equals("1")) {
                break;
            }
            try {
                if (ioHandler.createDatabaseFromFile(crud.souvenirList, crud.manufacturerList, filePath)) {
                    System.out.println(Messages.SUCCESS);
                    break;
                }
            } catch (RuntimeException e) { //відкат до попереднього вигляду списків
                crud.souvenirList = copySList;
                crud.manufacturerList = copyMList;
                break;
            }
        }
    }


    private String doCRUDAction() {
        ;
        while (true) {
            System.out.println(Messages.FIRST_ANSWER_2);
            String answer2 = scanner.nextLine();
            if (checkSecondAnswer(answer2)) {
                System.out.println(Messages.INVALID_ANSWER);
                continue;
            }
            if (answer2.equals("1")){  //робота з сувенірами
                if (souvenirsAction().equals("exit")) {
                    return "exit";
                }
                continue;
            }
            if(answer2.equals("2")) { //робота з виробниками
                if (manufacturersAction().equals("exit")) {
                    return "exit";
                }
                continue;
            }
            if (answer2.equals("3")) {
                return "exit";
            }
            if (answer2.equals("4")) {
                break;
            }
        }
        return "";
    }

    //маніпуляції з сувенірами
    //---------------------------------------------------------------
    private String souvenirsAction() {
        while (true) {
            System.out.println(Messages.CRUD_ACTION_SOUVENIRS);
            String answer3 = scanner.nextLine();

            if (checkAnswerForCRUDAction(answer3)) {
                System.out.println(Messages.INVALID_ANSWER);
                continue;
            }
            if (answer3.equals("9")) {
                return "exit";
            }
            if (answer3.equals("10")) {
                break;
            }
            if (crud.souvenirList.isEmpty() && !answer3.equals("3")) {
                System.out.println(Messages.EMPTY_LIST);
                continue;
            }
            switch (answer3) {
                case "1":
                    crud.showList(crud.souvenirList);
                    System.out.println(Messages.SUCCESS);
                    break;
                case "2":
                    crud.removeList(crud.souvenirList);
                    System.out.println(Messages.SUCCESS);
                    break;
                case "3":
                    insertSouvenir();
                    break;
                case "4":
                    removeSouvenir();
                    break;
                case "5":
                    updateSouvenir();
                    break;
                case "6":
                    showSouvenirByManufacturer();
                    break;
                case "7":
                    showSouvenirByCountry();
                    break;
                case "8":
                    crud.showSouvenirsGroupedByYear(crud.souvenirList);
                    System.out.println(Messages.SUCCESS);
                    break;
            }
        }
        return "";
    }

    // метод створює перед виконанням копію початкових списків
    private void insertSouvenir() {

        copySList = new ArrayList<>(crud.souvenirList);
        copyMList = new ArrayList<>(crud.manufacturerList);

        try {
            if (crud.insertSouvenir(crud.souvenirList, crud.manufacturerList, souvenirInformation())) {
                System.out.println(Messages.SUCCESS);
            } else {
                System.out.println(Messages.EXIST);
            }
        } catch (RuntimeException e) {
            System.out.println(Messages.INVALID_FORMAT);
        }
    }

    // метод створює перед виконанням копію початкових списків
    private void removeSouvenir() {

        copySList = new ArrayList<>(crud.souvenirList);
        copyMList = new ArrayList<>(crud.manufacturerList);

        crud.showList(crud.souvenirList);
        System.out.println(Messages.CHOOSE_ID);
        String id = scanner.nextLine();
        try {
            if (crud.removeSouvenirById(crud.souvenirList, id)) {
                System.out.println(Messages.SUCCESS);
            } else {
                System.out.println(Messages.INVALID_NAME);
            }
        } catch (RuntimeException e) {
            System.out.println(Messages.INVALID_FORMAT);
        }
    }

    // метод створює перед виконанням копію початкових списків
    private void updateSouvenir() {

        copySList = new ArrayList<>(crud.souvenirList);
        copyMList = new ArrayList<>(crud.manufacturerList);

        crud.showList(crud.souvenirList);
        System.out.println(Messages.CHOOSE_ID);
        String id = scanner.nextLine();
        try {
            if (updateSouvenirsInList(crud.souvenirList, crud.manufacturerList, id)) {
                System.out.println(Messages.SUCCESS);
            }
        } catch (RuntimeException e) {
            System.out.println(Messages.INVALID_FORMAT);
        }
    }

    private void showSouvenirByManufacturer() {
        System.out.println(Messages.MANUFACTURER_NAME);
        String name = scanner.nextLine();
        System.out.println(Messages.MANUFACTURER_COUNTRY);
        String country = scanner.nextLine();
        try {
            if (crud.showSouvenirsByManufacturer(crud.souvenirList, crud.manufacturerList, name, country)) {
                System.out.println(Messages.SUCCESS);
            } else {
                System.out.println(Messages.INVALID_NAME);
            }
        } catch (RuntimeException e) {
            System.out.println(Messages.INVALID_NAME);
        }
    }

    private void showSouvenirByCountry() {
        System.out.println(Messages.MANUFACTURER_COUNTRY);
        String country = scanner.nextLine();
        try {
            if (crud.showSouvenirsByCountry(crud.souvenirList, crud.manufacturerList, country)) {
                System.out.println(Messages.SUCCESS);
            }
        } catch (RuntimeException e) {
            System.out.println(Messages.INVALID_NAME);
        }
    }
    //---------------------------------------------------------------

    //маніпуляції з виробниками
    //---------------------------------------------------------------
    private String manufacturersAction() {
        while (true) {
            System.out.println(Messages.CRUD_ACTION_MANUFACTURERS);
            String answer3 = scanner.nextLine();

            if (checkAnswerForCRUDAction(answer3)) {
                System.out.println(Messages.INVALID_ANSWER);
                continue;
            }
            if (answer3.equals("9")) {
                return "exit";
            }
            if (answer3.equals("10")) {
                break;
            }
            if ((crud.manufacturerList.isEmpty() || crud.souvenirList.isEmpty()) &&
                    !answer3.equals("3")) {
                System.out.println(Messages.EMPTY_LIST);
                continue;
            }
            switch (answer3) {
                case "1":
                    crud.showList(crud.manufacturerList);
                    System.out.println(Messages.SUCCESS);
                    break;
                case "2":
                    crud.removeList(crud.manufacturerList); // при видаленні списку виробників потрібно автоматично видалити
                    crud.removeList(crud.souvenirList);     // список сувенірів, щоб не порушилась цілісність даних
                    System.out.println(Messages.SUCCESS);
                    break;
                case "3":
                    insertManufacturer();
                    break;
                case "4":
                    removeManufacturer();
                    break;
                case "5":
                    updateManufacturer();
                    break;
                case "6":
                    showManufacturersByLessSouvenirPrice();
                    break;
                case "7":
                    crud.showAllManufacturersAndTheirSouvenirs(crud.souvenirList, crud.manufacturerList);
                    System.out.println(Messages.SUCCESS);
                    break;
                case "8":
                    manufacturerInfoBySouvenirNameAndYear();
                    break;
            }
        }
        return "";
    }

    // метод створює перед виконанням копію початкових списків
    private void insertManufacturer() {

        copySList = new ArrayList<>(crud.souvenirList);
        copyMList = new ArrayList<>(crud.manufacturerList);

        try {
            if (crud.insertManufacturer(crud.manufacturerList, manufacturerInformation())) {
                System.out.println(Messages.SUCCESS);
            } else {
                System.out.println(Messages.EXIST);
            }
        } catch (RuntimeException e) {
            System.out.println(Messages.INVALID_FORMAT);
        }
    }

    // метод створює перед виконанням копію початкових списків
    private void removeManufacturer() {

        copySList = new ArrayList<>(crud.souvenirList);
        copyMList = new ArrayList<>(crud.manufacturerList);

        crud.showList(crud.manufacturerList);
        System.out.println(Messages.CHOOSE_ID);
        String id = scanner.nextLine();
        try {
            if (crud.removeManufacturerById(crud.souvenirList, crud.manufacturerList, id)) {
                System.out.println(Messages.SUCCESS);
            } else {
                System.out.println(Messages.INVALID_NAME);
            }
        } catch (RuntimeException e) {
            System.out.println(Messages.INVALID_FORMAT);
        }
    }

    // метод створює перед виконанням копію початкових списків
    private void updateManufacturer() {

        copySList = new ArrayList<>(crud.souvenirList);
        copyMList = new ArrayList<>(crud.manufacturerList);

        System.out.println(Messages.CHOOSE_ID);
        String id = scanner.nextLine();
        try {
            if (updateManufacturersInList(crud.manufacturerList, id)) {
                System.out.println(Messages.SUCCESS);
            }
        } catch (RuntimeException e) {
            System.out.println(Messages.INVALID_FORMAT);
        }
    }

    private void showManufacturersByLessSouvenirPrice() {
        System.out.println(Messages.PRICE);
        String price = scanner.nextLine();
        try {
            if (crud.showManufacturersByLessSouvenirPrice(crud.souvenirList,
                    crud.manufacturerList, price)) {
                System.out.println(Messages.SUCCESS);
            } else {
                System.out.println(Messages.NO_SOUVENIRS_WITH_LESS_PRICE);
            }
        } catch (RuntimeException e) {
            System.out.println(Messages.INVALID_FORMAT);
        }
    }

    private void manufacturerInfoBySouvenirNameAndYear() {
        System.out.println(Messages.INFORMATION_TO_REMOVE_UPDATE);
        String name = scanner.nextLine();
        System.out.println(Messages.YEAR);
        String year = scanner.nextLine();
        try {
            if (crud.manufacturerInfoBySouvenirNameAndYear(crud.souvenirList,
                    crud.manufacturerList, name, year)) {
                System.out.println(Messages.SUCCESS);
            } else {
                System.out.println(Messages.INVALID_NAME);
            }
        } catch (RuntimeException e) {
            System.out.println(Messages.INVALID_FORMAT);
        }
    }
    //-------------------------------------------------------------------

    private void createFile() {
        while (true) {
            System.out.println(Messages.FILE_PATH);
            String filePath = scanner.nextLine();
            if (filePath.equals("1")) {
                break;
            }
            try {
                if (ioHandler.writeFileFromDatabases(crud.souvenirList, crud.manufacturerList, filePath)) {
                    System.out.println(Messages.SUCCESS);
                    break;
                }
            } catch (RuntimeException e) {
                System.out.println(Messages.WRITING_ERROR);
                break;
            }
        }
    }


    private void optimizeLists() {
        try {
            if (!crud.souvenirList.isEmpty()) {
                crud.souvenirList = (List<Souvenir>) ioHandler.optimizeLists(crud.souvenirList);
                crud.manufacturerList = (List<Manufacturer>) ioHandler.optimizeLists(crud.manufacturerList);
                System.out.println(Messages.SUCCESS);
            } else {
                System.out.println(Messages.EMPTY_LIST);
            }

        } catch (RuntimeException e) {
            System.out.println(Messages.INVALID_TYPE);
        }
    }


    private boolean checkFirstAnswer(String answer) {
        return !answer.matches("^[0-4]$");
    }
    private boolean checkSecondAnswer(String answer) {
        return !answer.matches("^[1-4]$");
    }
    private boolean checkAnswerForCRUDAction(String answer) {
        return !answer.matches("^(10|[1-9])$");
    }
    private boolean checkAnswerForUpdateSouvenir(String answer) {
        return !answer.matches("^[1-5]$");
    }
    private boolean checkAnswerForUpdateManufacturer(String answer) {
        return !answer.matches("^[1-3]$");
    }


    // для збору данних про сувенір, який потрібно додати вручну до списку
    private String[] souvenirInformation() {
        System.out.println(Messages.INFORMATION_TO_INSERT);

        System.out.println("Name");
        String souvenirName = scanner.nextLine();

        System.out.println("ManufacturerName");
        String manufacturerName = scanner.nextLine();

        System.out.println("ManufacturerCountry");
        String manufacturerCountry = scanner.nextLine();

        System.out.println("Date");
        String date = scanner.nextLine();

        System.out.println("Price");
        String price = scanner.nextLine();

        return new String[]{souvenirName, manufacturerName, manufacturerCountry, date, price};
    }

    // оновлюємо інформацію в вибраному по id сувенірі
    private boolean updateSouvenirsInList(List<Souvenir> sList, List<Manufacturer> mList, String id) throws
            RuntimeException {

        if (crud.isInstancePresentInList(sList, id)) {
            while (true) {
                System.out.println(Messages.CHOOSE_SOUVENIR_FIELD_TO_UPDATE);
                String fieldToUpdate = scanner.nextLine();
                if (checkAnswerForUpdateSouvenir(fieldToUpdate)) {
                    System.out.println(Messages.INVALID_ANSWER);
                    continue;
                }
                if (fieldToUpdate.equals("5")) {
                    return false;
                }
                if (fieldToUpdate.equals("2")) {
                    System.out.println(Messages.MANUFACTURER_NAME);
                    String name = scanner.nextLine();
                    System.out.println(Messages.MANUFACTURER_COUNTRY);
                    String country = scanner.nextLine();

                    if (crud.setNewSouvenirValue(sList, mList, id, fieldToUpdate, name, country)) {
                        return true;
                    }
                } else {
                    System.out.println(Messages.NEW_VALUE);
                    String newValue = scanner.nextLine();

                    if (crud.setNewSouvenirValue(sList, mList, id, fieldToUpdate, newValue)) {
                        return true;
                    }
                }
            }
        } else {
            System.out.println(Messages.INVALID_ID);
            return false;
        }
    }

    // для збору данних про виробника, який потрібно додати вручну до списку
    private String[] manufacturerInformation() {
        System.out.println(Messages.INFORMATION_TO_INSERT);

        System.out.println("Name");
        String name = scanner.nextLine();

        System.out.println("Country");
        String country = scanner.nextLine();

        return new String[]{name, country};
    }

    // оновлюємо інформацію в вибраному по id виробнику
    private boolean updateManufacturersInList(List<Manufacturer> mList, String id) throws RuntimeException {

        if (crud.isInstancePresentInList(mList, id)) {
            while (true) {
                System.out.println(Messages.CHOOSE_MANUFACTURER_FIELD_TO_UPDATE);
                String fieldToUpdate = scanner.nextLine();
                if (checkAnswerForUpdateManufacturer(fieldToUpdate)) {
                    System.out.println(Messages.INVALID_ANSWER);
                    continue;
                }
                if (fieldToUpdate.equals("3")) {
                    return false;
                }
                System.out.println(Messages.NEW_VALUE);
                String newValue = scanner.nextLine();

                if (crud.setNewManufacturerValue(crud.souvenirList, crud.manufacturerList, id, fieldToUpdate, newValue)) {
                    return true;
                }
            }
        } else {
            System.out.println(Messages.INVALID_ID);
            return false;
        }
    }

    private void savePreviousList(List<? extends Item> list) {

    }
}





