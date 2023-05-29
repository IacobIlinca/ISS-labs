import Repository.*;
import domain.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import server.SpitalServicesImpl;
import service.ISpitalService;
import utils.AbstractServer;
import utils.ServerException;
import utils.SpitalRpcConcurrentServer;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort = 55555;

    static SessionFactory sessionFactory;

    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Exception " + e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }

    }

    public static void main(String[] args) {
        Properties serverProps = new Properties();
        initialize();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties " + e);
            return;
        }
        //ArbitruDBRepository repoArbitru = new ArbitruDBRepository(serverProps);
        IComandaRepository repoComanda = new ComandaDBRepository(sessionFactory);
        IDateRepository repoData = new DateDBRepository(sessionFactory);
        IElementComandaRepository repoElementComanda = new ElementComandaDBRepository(sessionFactory);
        IFarmacistRepository repoFarmacist = new FarmacistDBRepository(sessionFactory);
        IMedicamentRepository repoMedicament = new MedicamentDBRepository(sessionFactory);
        IMedicRepository repoMedic = new MedicDBRepository(sessionFactory);
        ISpitalService spitalServerImpl = new SpitalServicesImpl(repoComanda, repoData,repoFarmacist, repoMedic, repoMedicament, repoElementComanda);

        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("spital.server.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong  Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }

        System.out.println("Starting server on port: " + serverPort);
        AbstractServer server = new SpitalRpcConcurrentServer(serverPort, spitalServerImpl);

        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        } finally {
            try {
                close();
                server.stop();
            } catch (ServerException e) {
                System.err.println("Error stopping server " + e.getMessage());
            }
        }

//        System.out.println("Hello world!");
//        initialize();
//        MedicDBRepository repoArbitru = new MedicDBRepository(sessionFactory);
//        ComandaDBRepository repoComanda = new ComandaDBRepository(sessionFactory);
//        DateDBRepository repoDate = new DateDBRepository(sessionFactory);
//        MedicamentDBRepository repoMedicamente = new MedicamentDBRepository(sessionFactory);
//        var medic = repoArbitru.getAccount("ana@gmail.com", "orange","oncologie");
//        System.out.println(medic);
//
//        Spital spital = new Spital(1,"Municipal Cluj Napoca");
//        Medic medic2 = new Medic(1, "ana popa", "oncologie", spital, "ana@gmail.com", "orange");
//        Farmacist farmacist2 = new Farmacist(1, "aurel popa", spital, "aurel@gmail.com", "rosii");
//        Comanda cmd = new Comanda(1, "pedodontie", StatusComanda.ONORATA, new DateTime(1, 12,12,2022,12,12),medic2 );
//        repoComanda.add(cmd);
//
//        System.out.println(repoComanda.findAll());
//
//        DateTime dt = new DateTime(2,11,11,2001,11,11);
//        repoDate.add(dt);
//
//        Medicament med = new Medicament(1, "algocalmin", "o data la 3 ore");
//        System.out.println(repoMedicamente.findAll());
//
//
//        close();
    }
}