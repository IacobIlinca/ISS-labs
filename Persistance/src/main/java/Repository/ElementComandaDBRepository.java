package Repository;

import domain.ElementComanda;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Collection;
import java.util.List;

public class ElementComandaDBRepository implements IElementComandaRepository{

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
        if(elem!=null){
            System.out.println(elem);
            return true;
        }
        return false;
    }

    @Override
    public void delete(ElementComanda elem) {

    }

    @Override
    public void update(ElementComanda elem, Integer id) {

    }

    @Override
    public ElementComanda findById(Integer id) {
        return null;
    }

    @Override
    public List<ElementComanda> findAll() {
        return null;
    }

    @Override
    public Collection<ElementComanda> getAll() {
        return null;
    }
}
