import model.Polaznik;
import model.ProgramObrazovanja;
import model.Upis;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Scanner;

public class EvidencijaPolaznikaApp {

    private static SessionFactory sessionFactory;

    public static void main(String[] args) {

        sessionFactory = new Configuration().configure().buildSessionFactory();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\nIzbornik:");
                System.out.println("1. Unos novog polaznika");
                System.out.println("2. Unos novog programa obrazovanja");
                System.out.println("3. Upis polaznika na program obrazovanja");
                System.out.println("4. Prebaci polaznika iz jednog u drugi program obrazovanja");
                System.out.println("5. Ispiši polaznike za zadani program obrazovanja");
                System.out.println("6. Izlaz");
                System.out.print("Odaberite opciju iz izbornika: ");

                int izbor = scanner.nextInt();
                scanner.nextLine();

                switch (izbor) {
                    case 1 -> unosNovogPolaznika(scanner);
                    case 2 -> unosNovogProgramaObrazovanja(scanner);
                    case 3 -> upisPolaznika(scanner);
                    case 4 -> transferPolaznika(scanner);
                    case 5 -> ispisPolaznikaZaProgram(scanner);
                    case 6 -> {
                        sessionFactory.close();
                        return;
                    }
                    default -> System.out.println("Greška, pokušajte ponovno!");
                }
            }
        }
    }

    private static void unosNovogPolaznika(Scanner scanner) {

        System.out.print("Unesite ime polaznika: ");
        String ime = scanner.nextLine();
        System.out.print("Unesite prezime polaznika: ");
        String prezime = scanner.nextLine();

        Polaznik polaznik = new Polaznik();
        polaznik.setIme(ime);
        polaznik.setPrezime(prezime);

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(polaznik);
            transaction.commit();
            System.out.println("Polaznik je dodan!");
        }
    }

    private static void unosNovogProgramaObrazovanja(Scanner scanner) {

        System.out.print("Unesite naziv programa obrazovanja: ");
        String naziv = scanner.nextLine();
        System.out.print("Unesite broj CSVET bodova: ");
        int csvetBodovi = scanner.nextInt();

        ProgramObrazovanja program = new ProgramObrazovanja();
        program.setNaziv(naziv);
        program.setCsvetBodovi(csvetBodovi);

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(program);
            transaction.commit();
            System.out.println("Program obrazovanja je dodan!");
        }
    }

    private static void upisPolaznika(Scanner scanner) {

        System.out.print("Unesite ID polaznika: ");
        int idPolaznika = scanner.nextInt();
        System.out.print("Unesite ID programa obrazovanja: ");
        int idPrograma = scanner.nextInt();

        try (Session session = sessionFactory.openSession()) {
            Transaction transakcija = session.beginTransaction();

            Polaznik polaznik = session.get(Polaznik.class, idPolaznika);
            ProgramObrazovanja program = session.get(ProgramObrazovanja.class, idPrograma);

            if (polaznik != null && program != null) {
                Upis upis = new Upis();
                upis.setPolaznik(polaznik);
                upis.setProgramObrazovanja(program);
                session.save(upis);
                transakcija.commit();
                System.out.println("Polaznik je upisan u program!");
            } else {
                System.out.println("Polaznik ili program ne postoji!");
            }
        }
    }

    private static void transferPolaznika(Scanner scanner) {

        System.out.print("Unesite ID polaznika: ");
        int idPolaznika = scanner.nextInt();
        System.out.print("Unesite ID aktualnog programa: ");
        int idAktualnogPrograma = scanner.nextInt();
        System.out.print("Unesite ID novog programa: ");
        int idNovogPrograma = scanner.nextInt();

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Upis upis = session.createQuery("FROM Upis WHERE student.id = :studentId AND programObrazovanja.id = :programId", Upis.class)
                    .setParameter("polaznikId", idPolaznika)
                    .setParameter("programId", idAktualnogPrograma)
                    .uniqueResult();

            if (upis != null) {
                session.delete(upis);

                ProgramObrazovanja noviProgram = session.get(ProgramObrazovanja.class, idNovogPrograma);
                if (noviProgram != null) {
                    Upis noviUpis = new Upis();
                    noviUpis.setPolaznik(upis.getPolaznik());
                    noviUpis.setProgramObrazovanja(noviProgram);
                    session.save(noviUpis);
                    transaction.commit();
                    System.out.println("Polaznik je prebačen!");
                } else {
                    System.out.println("Novi program ne postoji!");
                }
            } else {
                System.out.println("Upis nije pronađen!");
            }
        }
    }

    private static void ispisPolaznikaZaProgram(Scanner scanner) {

        System.out.print("Unesite ID programa obrazovanja: ");
        int programId = scanner.nextInt();

        try (Session session = sessionFactory.openSession()) {
            ProgramObrazovanja program = session.get(ProgramObrazovanja.class, programId);

            if (program != null) {

                for (Upis upis : program.getUpisi()) {
                    Polaznik polaznik = upis.getPolaznik();
                    System.out.printf("Ime: %s, Prezime: %s, Program: %s, CSVET: %d%n",
                            polaznik.getIme(), polaznik.getPrezime(), program.getNaziv(), program.getCsvetBodovi());
                }
            } else {
                System.out.println("Program ne postoji!");
            }
        }
    }
}
