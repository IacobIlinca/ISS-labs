package Repository;

import domain.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ElementComandaDBRepository implements IElementComandaRepository {

    public final SessionFactory sessionFactory;

    public ElementComandaDBRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean add(ElementComanda elem) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(elem);

            session.getTransaction().commit();

        } catch (Exception e) {
            System.err.println(e);
        }
        if (elem != null) {
            System.out.println(elem);
            return true;
        }
        return false;
    }

    @Override
    public void delete(ElementComanda elem) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(elem);

            session.getTransaction().commit();

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @Override
    public void update(ElementComanda elem, Integer id) {
        try (
                Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query =
                    session.createSQLQuery("update Elemente_Comanda set cantitate=? where id=? ");
            query.setParameter(1, elem.getCantitate());
            query.setParameter(2, elem.getId());

            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @Override
    public ElementComanda findById(Integer id) {
        return null;
    }

    @Override
    public List<ElementComanda> findAll() {
        ElementComanda elem = null;
        Comanda comanda = null;
        Medic medic = null;
        Farmacist farmacist = null;
        Spital spitalMedic = null;
        Spital spitalFarmacist = null;
        DateTime data = null;
        Medicament medicament = null;
        List<ElementComanda> elemFindAll = new ArrayList<>();
        try (
                Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query =
                    session.createNativeQuery("select * from Elemente_Comanda ");

            List<Objects[]> elems = query.list();
            for (Object[] x : elems) {

                var query22 =
                        session.createNativeQuery("select * from Comenzi where id=? ");
                query22.setParameter(1, x[3]);

                List<Objects[]> comenzi = query22.list();
                for (Object[] a : comenzi) {
                    var query2 = session.createNativeQuery("SELECT * FROM Medici where id=?");
                    query2.setParameter(1, a[3]);
                    List<Object[]> medici = query2.list();

                    for (Object[] m : medici) {
                        var query3 = session.createNativeQuery("SELECT * FROM Spitale where id=?");
                        query3.setParameter(1, m[5]);
                        List<Object[]> spitale = query3.list();
                        for (Object[] s : spitale) {
                            spitalMedic = new Spital((Integer) s[0], s[1].toString());
                        }
                        medic = new Medic((Integer) m[0], m[2].toString(), m[4].toString(), spitalMedic, m[1].toString(), m[3].toString());
                    }

                    var query4 = session.createNativeQuery("SELECT * FROM Farmacisti where id=?");
                    query4.setParameter(1, a[5]);
                    List<Object[]> farmacisti = query4.list();

                    for (Object[] f : farmacisti) {
                        var query5 = session.createNativeQuery("SELECT * FROM Spitale where id=?");
                        query5.setParameter(1, f[4]);
                        List<Object[]> spitale = query5.list();
                        for (Object[] s : spitale) {
                            spitalFarmacist = new Spital((Integer) s[0], s[1].toString());
                        }
                        farmacist = new Farmacist((Integer) f[0], f[2].toString(), spitalFarmacist, f[1].toString(), f[3].toString());
                    }
                    var query6 = session.createNativeQuery("SELECT * FROM Date where id=?");
                    query6.setParameter(1, a[4]);
                    List<Object[]> date = query6.list();
                    for (Object[] d : date) {
                        data = new DateTime((Integer) d[0], (Integer) d[5], (Integer) d[2], (Integer) d[1], (Integer) d[4], (Integer) d[3]);
                    }


                    comanda = new Comanda((Integer) a[0], a[1].toString(), StatusComanda.valueOf((int) a[2]), data, medic);
                }
                var query8 =
                        session.createNativeQuery("select * from Medicamente where id=?");
                query8.setParameter(1, x[4]);

                List<Objects[]> medicamente = query8.list();
                for (Object[] pp : medicamente) {

                    medicament = new Medicament((Integer) pp[0], pp[1].toString(), pp[2].toString());
                    medicament.setId((Integer) pp[0]);
                }
                elem = new ElementComanda((Integer) x[0], (Integer) x[1], (String) x[2], medicament, comanda);

                elemFindAll.add(elem);
            }

        }
        return elemFindAll;
    }

    @Override
    public Collection<ElementComanda> getAll() {
        return null;
    }
}
