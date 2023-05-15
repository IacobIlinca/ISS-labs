package Repository;

import domain.DateTime;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Collection;
import java.util.List;

public class DateDBRepository implements IDateRepository{

    public final SessionFactory sessionFactory;

    public DateDBRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean add(DateTime elem) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
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
    public void delete(DateTime elem) {

    }

    @Override
    public void update(DateTime elem, Integer id) {

    }

    @Override
    public DateTime findById(Integer id) {
        return null;
    }

    @Override
    public List<DateTime> findAll() {
        return null;
    }

    @Override
    public Collection<DateTime> getAll() {
        return null;
    }
}
