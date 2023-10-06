import java.util.stream.Stream;

public class DataSourceForCRUDTest {

    static Stream<Object[]> isInstancePresentInList() {
        return Stream.of(
                new Object[]{"1", true, null},
                new Object[]{"6", false, null},
                new Object[]{"", null, new RuntimeException()}
        );
    }

    static Stream<Object[]> insertSouvenir() {
        return Stream.of(
                new Object[]{new String[]{"A", "V", "X", "", "848"}, false}, // souvenir and manufacturer already exist
                new Object[]{new String[]{"F", "A", "B", "", "458"}, true} // new souvenir and manufacturer

        );
    }

    static Object[][] insertSouvenirException() {
        return new Object[][]{
                {new String[]{"", "B", "", "", ""}, new RuntimeException()}, // souvenirName != null
                {new String[]{"A", "", "", "", ""}, new RuntimeException()}, // manufacturerName != null
                {new String[]{"A", "A", "", "4000-01-01", ""}, new RuntimeException()}, // date < currentDate
                {new String[]{"A", "A", "", "1990-01-01", "-20"}, new RuntimeException()}, // price > 0
                {new String[]{"A", "A", "", "xxx", "200"}, new RuntimeException()}, // date have to be parsed
                {new String[]{"A", "A", "", "1990-01-01", "xxx"}, new RuntimeException()} // price have to be parsed
        };
    }

    static Stream<Object[]> showSouvenirsByManufacturer() {
        return Stream.of(
                new Object[]{"V", "X", true, null},
                new Object[]{"V", "", false, null},
                new Object[]{"", "X", null, new RuntimeException()}
        );
    }

    static Stream<Object[]> showSouvenirsByCountry() {
        return Stream.of(
                new Object[]{"X", true, null},
                new Object[]{"", true, null},
                new Object[]{"A", null, new RuntimeException()}
        );
    }

    static Object[][] setNewSouvenirValueException() {
        return new Object[][]{
                {true, null, new String[]{"1", "1", "B"}},
                {null, new RuntimeException(), new String[]{"xxx", "1", "B"}},
                {true, null, new String[]{"1", "2", "X", ""}},
                {null, new RuntimeException(), new String[]{"1", "2", "", ""}},
                {true, null, new String[]{"1", "3", "1111-11-11"}},
                {null, new RuntimeException(), new String[]{"1", "3", "xxx"}},
                {true, null, new String[]{"1", "4", "200"}},
                {null, new RuntimeException(), new String[]{"1", "4", "-20"}}
        };
    }

    static Stream<Object[]> insertManufacturer() {
        return Stream.of(
                new Object[]{new String[]{"V", "X"}, false, null}, // souvenir and manufacturer already exist
                new Object[]{new String[]{"V", "A"}, true, null}, // new souvenir and manufacturer
                new Object[]{new String[]{"", "A"}, null, new RuntimeException()}

        );
    }

    static Stream<Object[]> removeManufacturerById() {
        return Stream.of(
                new Object[]{"7", false, null}, // souvenir and manufacturer already exist
                new Object[]{"3", true, null},
                new Object[]{"xxx", true, new RuntimeException()}// new souvenir and manufacturer

        );
    }

    static Stream<Object[]> setNewManufacturerValue() {
        return Stream.of(
                new Object[]{"2", "1", "V", true, null},
                new Object[]{"xxx", "1", "V", null, new RuntimeException()},
                new Object[]{"1", "1", "", null, new RuntimeException()}

        );
    }

    static Stream<Object[]> showManufacturersByLessSouvenirPrice() {
        return Stream.of(
                new Object[]{"0", false, null},
                new Object[]{"900", true, null},
                new Object[]{"-200", null, new RuntimeException()},
                new Object[]{"xxx", null, new RuntimeException()}

        );
    }

    static Stream<Object[]> manufacturerInfoBySouvenirNameAndYear() {
        return Stream.of(
                new Object[]{"C", "1991", false, null},
                new Object[]{"C", "1990", true, null},
                new Object[]{"", "1990", null, new RuntimeException()},
                new Object[]{"C", "3500", null, new RuntimeException()},
                new Object[]{"C", "xxx", null, new RuntimeException()}

        );
    }

}
