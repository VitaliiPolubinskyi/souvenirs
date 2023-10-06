package enteties;

import java.util.Objects;

public class Manufacturer implements Item {
    private int id;
    private String name;
    private String country;

    public Manufacturer() {
    }

    public Manufacturer(int id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public Manufacturer(int id, Manufacturer manufacturer) {
        this.id = id;
        name = manufacturer.getName();
        country = manufacturer.getCountry();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    // перевірка імені на пусту строку
    public void setName(String name) throws RuntimeException {
        if (name.matches("^[\\s]*$")) {
            System.out.println("Manufacturer name mustn't be empty!");
            throw new RuntimeException();
        }
        else {
            this.name = name;
        }
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Manufacturer)) return false;
        Manufacturer that = (Manufacturer) o;
        return name.equals(that.name) &&
                Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, country);
    }

    @Override
    public String toString() {
        return "Manufacturer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }


}
