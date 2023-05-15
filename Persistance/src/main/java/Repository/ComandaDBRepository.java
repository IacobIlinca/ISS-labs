package Repository;

import domain.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ComandaDBRepository implements IComandaRepository {
    public final SessionFactory sessionFactory;

    public ComandaDBRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean add(Comanda elem) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query =
                    session.createNativeQuery("INSERT INTO Comenzi (id, sectie,statusComanda,creator,data) VALUES (?,?,?,?,?); ");
            query.setParameter(1, elem.getId());
            query.setParameter(2, elem.getSectie());
            query.setParameter(3, elem.getStatusComanda());
            query.setParameter(4, elem.getCreator().getId());
            query.setParameter(5, elem.getData().getId());

//            List<Objects[]> comenzi = query.list();
//            for (Object[] a : comenzi) {
//                var query2 = session.createNativeQuery("SELECT * FROM Medici where id=?");
//                query2.setParameter(1, a[3]);
//                List<Object[]> medici = query2.list();
//
//                for(Object[] m:medici){
//                    var query3 = session.createNativeQuery("SELECT * FROM Spitale where id=?");
//                    query3.setParameter(1, m[5]);
//                    List<Object[]> spitale = query3.list();
//                    for(Object[] s:spitale){
//                        spitalMedic = new Spital((Integer) s[0], s[1].toString());
//                    }
//                    medic = new Medic((Integer) m[0], m[2].toString(), m[4].toString(), spitalMedic, m[1].toString(), m[3].toString());
//                }
//
//                var query4 = session.createNativeQuery("SELECT * FROM Farmacisti where id=?");
//                query4.setParameter(1, a[5]);
//                List<Object[]> farmacisti = query4.list();
//
//                for(Object[] f:farmacisti){
//                    var query5 = session.createNativeQuery("SELECT * FROM Spitale where id=?");
//                    query5.setParameter(1, f[4]);
//                    List<Object[]> spitale = query5.list();
//                    for(Object[] s:spitale){
//                        spitalFarmacist = new Spital((Integer) s[0], s[1].toString());
//                    }
//                     farmacist = new Farmacist((Integer) f[0], f[2].toString(), spitalFarmacist, f[1].toString(), f[3].toString());
//                }
//                var query6 = session.createNativeQuery("SELECT * FROM Date where id=?");
//                query6.setParameter(1, a[4]);
//                List<Object[]> date = query6.list();
//                for(Object[] d:date){
//                    data = new DateTime((Integer) d[0], (Integer)d[5], (Integer) d[2], (Integer) d[1], (Integer) d[4], (Integer) d[3]);
//                }
//
//
//                comanda = new Comanda((Integer) a[0], a[1].toString(), StatusComanda.valueOf(a[2].toString()), data, medic, farmacist);
//                comanda.setId((Integer) a[0]);
//            }
            session.save(elem);

            session.getTransaction().commit();

        } catch (Exception e) {
            System.err.println(e);
        }
        if(elem!=null){
            System.out.println(elem);
            return true;
        }
        return false;
    }


    @Override
    public void delete(Comanda elem) {

    }

    @Override
    public void update(Comanda elem, Integer id) {

    }

    @Override
    public Comanda findById(Integer id) {
        return null;
    }

    @Override
    public List<Comanda> findAll() {
        Comanda comanda = null;
        Medic medic = null;
        Farmacist farmacist = null;
        Spital spitalMedic = null;
        Spital spitalFarmacist = null;
        DateTime data = null;
        List<Comanda> comenziFindAll = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query =
                    session.createNativeQuery("select * from Comenzi ");

            List<Objects[]> comenzi = query.list();
            for (Object[] a : comenzi) {
                var query2 = session.createNativeQuery("SELECT * FROM Medici where id=?");
                query2.setParameter(1, a[3]);
                List<Object[]> medici = query2.list();

                for(Object[] m:medici){
                    var query3 = session.createNativeQuery("SELECT * FROM Spitale where id=?");
                    query3.setParameter(1, m[5]);
                    List<Object[]> spitale = query3.list();
                    for(Object[] s:spitale){
                        spitalMedic = new Spital((Integer) s[0], s[1].toString());
                    }
                    medic = new Medic((Integer) m[0], m[2].toString(), m[4].toString(), spitalMedic, m[1].toString(), m[3].toString());
                }

                var query4 = session.createNativeQuery("SELECT * FROM Farmacisti where id=?");
                query4.setParameter(1, a[5]);
                List<Object[]> farmacisti = query4.list();

                for(Object[] f:farmacisti){
                    var query5 = session.createNativeQuery("SELECT * FROM Spitale where id=?");
                    query5.setParameter(1, f[4]);
                    List<Object[]> spitale = query5.list();
                    for(Object[] s:spitale){
                        spitalFarmacist = new Spital((Integer) s[0], s[1].toString());
                    }
                     farmacist = new Farmacist((Integer) f[0], f[2].toString(), spitalFarmacist, f[1].toString(), f[3].toString());
                }
                var query6 = session.createNativeQuery("SELECT * FROM Date where id=?");
                query6.setParameter(1, a[4]);
                List<Object[]> date = query6.list();
                for(Object[] d:date){
                    data = new DateTime((Integer) d[0], (Integer)d[5], (Integer) d[2], (Integer) d[1], (Integer) d[4], (Integer) d[3]);
                }


                comanda = new Comanda((Integer) a[0], a[1].toString(), StatusComanda.valueOf((int)a[2]), data, medic);
                comanda.setId((Integer) a[0]);
                comenziFindAll.add(comanda);

            }

            session.getTransaction().commit();

        } catch (Exception e) {
            System.err.println(e);
        }
        return comenziFindAll;
    }

    @Override
    public Collection<Comanda> getAll() {
        return null;
    }
}
