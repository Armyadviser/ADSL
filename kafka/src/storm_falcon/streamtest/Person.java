package storm_falcon.streamtest;

/**
 * Created by Storm_Falcon on 2016/8/16.
 */
public class Person {
    public String name;
    public int age;
    public Sex sex;

    public Person() {
    }

    public Person(String name, int age, Sex sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public static Person parse(String s) {
        String[] item = s.split("\\|");
        Person p = new Person();
        p.name = item[0];
        p.age = Integer.parseInt(item[1]);
        if (String.valueOf(item[2]).contains("FEMALE")) {
            p.sex = Sex.FEMALE;
        } else {
            p.sex = Sex.MALE;
        }
        return p;
    }

    public enum Sex {
        MALE, FEMALE
    }

    @Override
    public String toString() {
        return name + '|' + age + "|" + sex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return age == person.age && name.equals(person.name) && sex == person.sex;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + age;
        result = 31 * result + sex.hashCode();
        return result;
    }
}
