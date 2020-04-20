package person;

import java.awt.image.AreaAveragingScaleFilter;
import java.security.spec.RSAOtherPrimeInfo;
import java.sql.SQLOutput;
import java.time.Year;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.github.javafaker.Faker;
import com.sun.management.UnixOperatingSystemMXBean;
import legoset.model.LegoSet;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.ls.LSOutput;
import person.model.Address;
import person.model.Person;

@Log4j2
public class Main {

    private static Faker faker =new Faker();

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-example");

    private static Address generateAddress(){
        Address address = Address.builder()
                .country(faker.address().country())
                .state(faker.address().state())
                .city(faker.address().city())
                .streetAddress(faker.address().streetAddress())
                .zip(faker.address().zipCode())
                .build();
        return address;
    }
/*

// People generator v.0.1

    private static ArrayList<Person> createPeople(int numberofPeople) {
        ArrayList<Person> people=new ArrayList<>();
        for(int i=0;i<numberofPeople;i++) {
            Person person = Person.builder()
                    .name(faker.name().fullName())
                    .dob(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                    .gender(faker.options().option(Person.Gender.class))
                    .address(generateAddress())
                    .email(faker.internet().emailAddress())
                    .profession(faker.company().profession())
                    .build();
            log.trace(person);
            people.add(person);
        }
        return people;
    }


 */

    public static Person randomPerson(){
        Person person = Person.builder()
                .name(faker.name().fullName())
                .dob(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .gender(faker.options().option(Person.Gender.class))
                .address(generateAddress())
                .email(faker.internet().emailAddress())
                .profession(faker.company().profession())
                .build();

        return person;
    }

    private static List<Person> getPeople() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Person p ORDER BY p.id", Person.class).getResultList();
        } finally {
            em.close();
        }
    }

    private static void deletePeople() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            int count = em.createQuery("DELETE FROM Person").executeUpdate();
            System.out.println("Deleted "+ count +"number of people");
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        int numberofPeople=5;
        try {
            for (int i = 0; i < numberofPeople; i++) {
                em.getTransaction().begin();
                em.persist(randomPerson());
                em.getTransaction().commit();
            }
        }
        finally {
            em.close();
        }

        getPeople().forEach(System.out::println);

        deletePeople();
        emf.close();

    }


}
