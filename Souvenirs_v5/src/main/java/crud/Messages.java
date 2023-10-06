package crud;

public class Messages {

    static final String INTERNAL_MENU =
            "Please, choose an option and enter the option number\n" +
                    "1. Create a database from the file\n" +
                    "2. Show/change an information in the database\n" +
                    "3. Create a file from the database\n" +
                    "4. Rollback status\n" +
                    "5. Exit the program\n\n" +
                    "To optimize lists, please, enter 0";

    static final String GOODBYE = "Goodbye";

    static final String FIRST_ANSWER_2 = "Please, choose a database variant\n" +
            "1. Souvenirs\n" +
            "2. Manufacturers\n\n" +
            "To exit, please, enter 3\n" +
            "To back to the previous menu, please, enter 4";

    public static final String CRUD_ACTION_SOUVENIRS = "Please, choose an option\n" +
            "1. Show all\n" +
            "2. Remove all\n" +
            "3. Add an instance\n" +
            "4. Remove an instance\n" +
            "5. Update an instance\n\n" +
            "6. Show souvenirs by their manufacturer\n" +
            "7. Show souvenirs by the made country\n" +
            "8. Show souvenirs grouped by produce year\n\n" +
            "To exit, please, enter 9\n" +
            "To back to the previous menu, please, enter 10";

    static final String CRUD_ACTION_MANUFACTURERS = "Please, choose an option\n" +
            "1. Show all\n" +
            "2. Remove all\n" +
            "3. Add an instance\n" +
            "4. Remove an instance\n" +
            "5. Update an instance\n\n" +
            "6. Show manufacturers of the less souvenir price then defined\n" +
            "7. Show manufactures and their souvenirs\n" +
            "8. Show manufactures by the souvenir name produced in the defined year\n\n" +
            "To exit, please, enter 9\n" +
            "To back to the previous menu, please, enter 10";

    static final String CHOOSE_SOUVENIR_FIELD_TO_UPDATE = "Please, choose field to update\n" +
            "1. Name\n" +
            "2. Manufacturer information\n" +
            "3. Date\n" +
            "4. Price\n\n" +
            "To back to the previous menu, please, enter 5";

    static final String CHOOSE_MANUFACTURER_FIELD_TO_UPDATE = "Please, choose field to update\n" +
            "1. Name\n" +
            "2. Country\n\n" +
            "To back to the previous menu, please, enter 3";


    static final String FILE_PATH = "Please, enter a file path\n" +
            "To back to the previous menu, please, enter 1";
    static final String FILE_NOT_FOUND = "File not found. Please, create the file and restart the program";
    static final String READING_ERROR = "Something went wrong while reading the file. Please, check data in the file and try again";
    static final String WRITING_ERROR = "Something went wrong while writing the database. Please, try again";


    static final String NEW_VALUE = "Please, enter a new value";
    static final String INFORMATION_TO_INSERT = "Please, fill the fields";
    static final String MANUFACTURER_NAME = "Please, enter a manufacturer name";
    static final String MANUFACTURER_COUNTRY = "Please, enter a manufacturer country";
    static final String INFORMATION_TO_REMOVE_UPDATE = "Please, enter a name";
    static final String CHOOSE_ID = "Please, choose an id";
    static final String PRICE = "Please, enter a price value";
    static final String YEAR = "Please, enter a year";


    static final String SUCCESS = "The operation is completed successfully";
    static final String INVALID_NAME = "Object(-s) not found";
    static final String INVALID_ID = "Invalid id number, please, try again";
    static final String INVALID_FORMAT = "Denied. Please, check a format of values, it might be written incorrectly";
    static final String EXIST = "The instance already exists";
    static final String EMPTY_LIST = "The list is empty. Please, fill it first";
    static final String NO_SOUVENIRS_WITH_LESS_PRICE = "There is no souvenirs with less price";
    static final String INVALID_ANSWER = "Invalid answer, please, try again";
    static final String INVALID_TYPE = "Invalid type of instances in list. Please, check it up";


}
