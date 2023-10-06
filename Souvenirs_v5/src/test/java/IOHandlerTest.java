
import crud.IOHandler;
import enteties.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IOHandlerTest {

    static final String filePath1 = "src/test/testResources/Souvenirs1.txt";
    static final String filePath2 = "src/test/testResources/Souvenirs2.txt";
    static final String filePath3 = "src/test/testResources/Souvenirs3.txt";
    static final String filePath4 = "src/test/testResources/Souvenirs4.txt";


    IOHandler ioHandlerInstance;
    List<Souvenir> sList;
    List<Souvenir> souvenirList;
    List<Manufacturer> manufacturerList;
    List<Manufacturer> mList;

    @BeforeEach
    void create() {
        ioHandlerInstance = IOHandler.createIOHandler();

        souvenirList = new ArrayList<>();
        manufacturerList = new ArrayList<>();

        //можна замінити назви на нормальні :)
        sList = new ArrayList<>();
        sList.add(new Souvenir(1, "A", 1, null, 848.0));
        sList.add(new Souvenir(2, "B", 2, null, null));
        sList.add(new Souvenir(3, "C", 3, LocalDate.parse("1990-11-08"), null));
        sList.add(new Souvenir(4, "D", 4, null, 300.0));
        sList.add(new Souvenir(5, "B", 5, null, null));
        sList.add(new Souvenir(6, "C", 4, null, 848.0));
        sList.add(new Souvenir(7, "B", 6, LocalDate.parse("1125-11-11"), null));
        sList.add(new Souvenir(8, "D", 5, null, 300.0));

        mList = new ArrayList<>();
        mList.add(new Manufacturer(1, "R", "X"));
        mList.add(new Manufacturer(2, "S", "Y"));
        mList.add(new Manufacturer(3, "B", null));
        mList.add(new Manufacturer(4, "V", "B"));
        mList.add(new Manufacturer(5, "S", null));
        mList.add(new Manufacturer(6, "S", "B"));
    }


    @Test
    void createDatabaseFromFileTest() {
        ioHandlerInstance.createDatabaseFromFile(souvenirList, manufacturerList, filePath1);
        ioHandlerInstance.createDatabaseFromFile(souvenirList, manufacturerList, filePath2);
        assertEquals(sList, souvenirList);
        assertEquals(mList, manufacturerList);
        assertThrows(RuntimeException.class,
                () -> ioHandlerInstance.createDatabaseFromFile(souvenirList, manufacturerList, filePath3));
    }

    @Test
    void optimizeListsTest() {
        Souvenir souvenir = sList.stream()
                .filter(o -> o.getId() == 8)
                .findAny()
                .orElse(new Souvenir(0, null, 0, null, null));
        souvenir.setManufacturerId(4);
        sList.set(7, souvenir);
        souvenirList = (List<Souvenir>) ioHandlerInstance.optimizeLists(sList);

        assertEquals(7, souvenirList.size());
        assertFalse(souvenirList.stream().anyMatch(o -> o.getId() == 8));
    }

    @Test
    void writeFileFromDatabasesTest() {
        Souvenir souvenir = sList.stream()
                .filter(o -> o.getId() == 8)
                .findAny()
                .orElse(new Souvenir(0, null, 0, null, null));
        souvenir.setManufacturerId(4);
        sList.set(7, souvenir);
        ioHandlerInstance.writeFileFromDatabases(sList, mList, filePath4);
        List<String> stringList = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath4))) {
            while (reader.ready()) {
                stringList.add(reader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(7, stringList.size());
        assertEquals("souvenirName = B, manufacturerName = S, manufacturerCountry = B, date = 1125-11-11, price = ",
                stringList.get(stringList.size() - 1));


    }


}
