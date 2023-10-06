package enteties;

import java.time.LocalDate;
import java.util.Objects;

public class Souvenir implements Item {
    private int id;
    private String name;
    private int manufacturerId;
    private LocalDate date;
    private Double price;
    private Manufacturer manufacturer;

    public Souvenir() {
    }

    public Souvenir(int id, Souvenir souvenir) {
        this.id = id;
        name = souvenir.getName();
        manufacturerId = souvenir.getManufacturerId();
        date = souvenir.getDate();
        price = souvenir.getPrice();
    }

    public Souvenir(int id, String name, int manufacturerId, LocalDate date, Double price) {
        this.id = id;
        this.name = name;
        this.manufacturerId = manufacturerId;
        this.date = date;
        this.price = price;
    }

    public Souvenir(int id, String name, Manufacturer manufacturer, LocalDate date, Double price) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.date = date;
        this.price = price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // перевірка імені на пусту строку
    public void setName(String name) throws RuntimeException {
        if (name.matches("^[\\s]*$")) {
            System.out.println("Souvenir name mustn't be empty!");
            throw new RuntimeException();
        }
        else {
            this.name = name;
        }
    }

    public int getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public LocalDate getDate() {
        return date;
    }

    //перевірка дати, чи вона не більша за теперішню
    public void setDate(LocalDate date) throws RuntimeException {
        if (date != null && date.isAfter(LocalDate.now())) {
            System.out.println("Entered date have to be earlier or equals current date!");
            throw new RuntimeException();
        }
        else {
            this.date = date;
        }
    }

    public Double getPrice() {
        return price;
    }

    // перевірка ціни, чи вона не від'ємна
    public void setPrice(Double price) throws RuntimeException {
        if (price != null && price < 0) {
            System.out.println("Price mustn't be less, then 0!");
            throw new RuntimeException();
        }
        else {
            this.price = price;
        }
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Souvenir)) return false;
        Souvenir souvenir = (Souvenir) o;
        return manufacturerId == souvenir.manufacturerId &&
                name.equals(souvenir.name) &&
                Objects.equals(date, souvenir.date) &&
                Objects.equals(price, souvenir.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, manufacturerId, date, price);
    }

    @Override
    public String toString() {
        return "Souvenir{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", manufacturerId='" + manufacturerId + '\'' +
                ", date=" + date +
                ", price=" + price +
                '}';
    }
}
