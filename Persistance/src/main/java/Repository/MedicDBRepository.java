package Repository;

import domain.Medic;
import domain.Medicament;
import domain.Spital;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class MedicDBRepository implements IMedicRepository{
    public final SessionFactory sessionFactory;

    public MedicDBRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Medic getAccount(String email, String parola, String sectie) {
        Medic medic = null;
        Spital spital = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query =
                    session.createNativeQuery("select * from Medici where email=? and parola=? and sectie=?");
            query.setParameter(1, email);
            query.setParameter(2, parola);
            query.setParameter(3, sectie);


            List<Objects[]> medici = query.list();
            for (Object[] a : medici) {

                var query2 = session.createNativeQuery("SELECT * FROM Spitale where id=?");
                query2.setParameter(1, a[5]);
                List<Object[]> spitale = query2.list();
                for(Object[] s:spitale){
                    spital = new Spital((Integer) s[0], s[1].toString());
                }
                medic = new Medic((Integer) a[0], a[2].toString(), a[4].toString(), spital, a[1].toString(), a[3].toString());
                medic.setId((Integer) a[0]);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println(e);
        }
        return medic;
    }

    @Override
    public boolean add(Medic elem) {
        return false;
    }

    @Override
    public void delete(Medic elem) {

    }

    @Override
    public void update(Medic elem, Integer id) {

    }

    @Override
    public Medic findById(Integer id) {
        return null;
    }

    @Override
    public List<Medic> findAll() {
        Medic medic = null;
        Spital spitalMedic = null;
        List<Medic> medicFindAll = new ArrayList<>();
        try (
                Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query =
                    session.createNativeQuery("select * from Medici ");

            List<Objects[]> medici = query.list();
            for(Object[] m:medici){
                var query3 = session.createNativeQuery("SELECT * FROM Spitale where id=?");
                query3.setParameter(1, m[5]);
                List<Object[]> spitale = query3.list();
                for(Object[] s:spitale){
                    spitalMedic = new Spital((Integer) s[0], s[1].toString());
                }
                medic = new Medic((Integer) m[0], m[2].toString(), m[4].toString(), spitalMedic, m[1].toString(), m[3].toString());
                medic.setId((Integer) m[0]);
                medicFindAll.add(medic);
            }

        }
        return medicFindAll;
    }

    @Override
    public Collection<Medic> getAll() {
        return null;
    }
}
