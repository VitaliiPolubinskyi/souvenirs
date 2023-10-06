import crud.CRUD;
import enteties.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CRUDTest {


    CRUD crudInstance;
    List<Souvenir> sList;
    List<Manufacturer> mList;

    @BeforeEach
    void create() {
        crudInstance = CRUD.createCRUD();

        //треба замінити назви на нормальні :)
        sList = new ArrayList<>();
        sList.add(new Souvenir(1, "A", 1, null, 848.0));
        sList.add(new Souvenir(2, "B", 2, null, null));
        sList.add(new Souvenir(3, "C", 3, LocalDate.parse("1990-11-10"), null));
        sList.add(new Souvenir(4, "D", 4, null, 800.3));
        sList.add(new Souvenir(5, "E", 5, null, null));

        mList = new ArrayList<>();
        mList.add(new Manufacturer(1, "V", "X"));
        mList.add(new Manufacturer(2, "S", "X"));
        mList.add(new Manufacturer(3, "Y", null));
        mList.add(new Manufacturer(4, "Z", "Q"));
        mList.add(new Manufacturer(5, "S", null));
    }



    @Test
    void showListTest() {
        crudInstance.showList(sList);
        crudInstance.showList(mList);
    }



    @Test
    void removeListTest() {
        crudInstance.removeList(sList);
        crudInstance.removeList(mList);
        assertEquals(0, sList.size());
        assertEquals(0, mList.size());
    }



    @ParameterizedTest
    @MethodSource("DataSourceForCRUDTest#isInstancePresentInList")
    void isInstancePresentInListTest(String input, Object expected, RuntimeException e) {
        if (e == null) {
            assertEquals(expected, crudInstance.isInstancePresentInList(sList, input));
            assertEquals(expected, crudInstance.isInstancePresentInList(mList, input));
        } else {
            assertThrows(RuntimeException.class,
                    () -> crudInstance.isInstancePresentInList(sList, input));
            assertThrows(RuntimeException.class,
                    () -> crudInstance.isInstancePresentInList(mList, input));
        }
    }



    @Test
    void removeSouvenirByIdTest() {
        assertFalse(crudInstance.removeSouvenirById(sList, String.valueOf(sList.get(sList.size() - 1).getId() + 1)));
        crudInstance.removeSouvenirById(sList, String.valueOf(sList.size()));
        assertEquals(4, sList.size());
        assertNull(sList.stream()
                .filter(o -> o.getId() == sList.size() + 1)
                .findAny()
                .orElse(null));
    }



    @ParameterizedTest
    @MethodSource("DataSourceForCRUDTest#insertSouvenir")
    void insertSouvenirTest(String[] args, Object expected) {
        int initialSListSize = sList.size();
        int initialMListSize = mList.size();
        assertEquals(expected, crudInstance.insertSouvenir(sList, mList, args));
        if ((boolean) expected) {
            assertEquals(initialSListSize + 1, sList.size());
            assertEquals(initialMListSize + 1, mList.size());
        }
    }



    @ParameterizedTest
    @MethodSource("DataSourceForCRUDTest#insertSouvenirException")
    void insertSouvenirExceptionTest(String[] args) {
        assertThrows(RuntimeException.class,
                () -> crudInstance.insertSouvenir(sList, mList, args));
    }



    @ParameterizedTest
    @MethodSource("DataSourceForCRUDTest#showSouvenirsByManufacturer")
    void showSouvenirsByManufacturerTest(String name, String country, Object expected, RuntimeException e) {
        if (e == null) {
            assertEquals(expected, crudInstance.showSouvenirsByManufacturer(sList, mList, name, country));
        } else {
            assertThrows(RuntimeException.class,
                    () -> crudInstance.showSouvenirsByManufacturer(sList, mList, name, country));
        }
    }



    @ParameterizedTest
    @MethodSource("DataSourceForCRUDTest#showSouvenirsByCountry")
    void showSouvenirsByCountryTest(String country, Object expected, RuntimeException e) {
        if (e == null) {
            assertEquals(expected, crudInstance.showSouvenirsByCountry(sList, mList, country));
        } else {
            assertThrows(RuntimeException.class,
                    () -> crudInstance.showSouvenirsByCountry(sList, mList, country));
        }
    }



    @Test
    void setNewSouvenirValueTest() {
        String[] args = new String[] {"1","2", "Z", "Q"};
        int initialMListSize = mList.size();
        crudInstance.setNewSouvenirValue(sList, mList, args);
        assertEquals(initialMListSize, mList.size());
        assertEquals(4, sList.stream()
                .filter(o -> o.getId() == Integer.parseInt(args[0]))
        .map(Souvenir::getManufacturerId).findAny().orElse(-1));

    }



    @ParameterizedTest
    @MethodSource("DataSourceForCRUDTest#setNewSouvenirValueException")
    void setNewSouvenirValueExceptionTest(Object expected, RuntimeException e, String[] args) {
        if (e == null) {
            assertEquals(expected, crudInstance.setNewSouvenirValue(sList, mList, args));
        } else {
            assertThrows(RuntimeException.class,
                    () -> crudInstance.setNewSouvenirValue(sList, mList, args));
        }
    }



    @Test
    void showSouvenirsGroupedByYearTest() {
        crudInstance.showSouvenirsGroupedByYear(sList);
    }



    @ParameterizedTest
    @MethodSource("DataSourceForCRUDTest#insertManufacturer")
    void insertManufacturerTest(String[] args, Object expected, RuntimeException e) {
        if (e == null) {
            int initialMListSize = mList.size();
            assertEquals(expected, crudInstance.insertManufacturer(mList, args));
            if ((boolean) expected) {
                assertEquals(initialMListSize + 1, mList.size());
            }
        } else {
            assertThrows(RuntimeException.class,
                    () -> crudInstance.insertManufacturer(mList, args));
        }
    }



    @ParameterizedTest
    @MethodSource("DataSourceForCRUDTest#removeManufacturerById")
    void removeManufacturerByIdTest(String id, Object expected, RuntimeException e) {
        if (e == null) {
            assertEquals(expected, crudInstance.removeManufacturerById(sList, mList, id));
        } else {
            assertThrows(RuntimeException.class,
                    () -> crudInstance.removeManufacturerById(sList, mList, id));
        }

    }



    @ParameterizedTest
    @MethodSource("DataSourceForCRUDTest#setNewManufacturerValue")
    void setNewManufacturerValueTest(String id, String fieldToUpdate,
                                     String value, Object expected, RuntimeException e) {
        if (e == null) {
            assertEquals(expected,
                    crudInstance.setNewManufacturerValue(sList, mList, id, fieldToUpdate, value));

            Manufacturer manufacturer = mList.stream()
                                        .filter(o -> o.getId() == Integer.parseInt(id))
                                        .findAny()
                                        .orElse(new Manufacturer(0, null, null));

            assertEquals(manufacturer, mList.get(0));

            assertEquals(2, sList.stream()
                                    .filter(o -> o.getManufacturerId() ==
                                            Math.min(manufacturer.getId(), mList.get(0).getId()))
                                    .mapToInt(Souvenir::getId)
                                    .toArray().length);
        } else {
            assertThrows(RuntimeException.class,
                    () -> crudInstance.setNewManufacturerValue(sList, mList, id, fieldToUpdate, value));
        }
    }



    @ParameterizedTest
    @MethodSource("DataSourceForCRUDTest#showManufacturersByLessSouvenirPrice")
    void showManufacturersByLessSouvenirPriceTest(String price, Object expected, RuntimeException e) {
        if (e == null) {
            assertEquals(expected,
                    crudInstance.showManufacturersByLessSouvenirPrice(sList, mList, price));
        } else {
            assertThrows(RuntimeException.class,
                    () -> crudInstance.showManufacturersByLessSouvenirPrice(sList, mList, price));
        }
    }



    @Test
    void showAllManufacturersAndTheirSouvenirsTest() {
        crudInstance.showAllManufacturersAndTheirSouvenirs(sList, mList);
    }



    @ParameterizedTest
    @MethodSource("DataSourceForCRUDTest#manufacturerInfoBySouvenirNameAndYear")
    void manufacturerInfoBySouvenirNameAndYearTest(String name, String year,
                                                   Object expected, RuntimeException e) {
        if (e == null) {
            assertEquals(expected,
                    crudInstance.manufacturerInfoBySouvenirNameAndYear(sList, mList, name, year));
        } else {
            assertThrows(RuntimeException.class,
                    () -> crudInstance.manufacturerInfoBySouvenirNameAndYear(sList, mList, name, year));
        }
    }
}
