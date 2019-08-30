package kz.kazmoto.org.ejb;



import kz.kazmoto.glob.utils.EJBUtils;
import kz.kazmoto.glob.utils.StdEjb;
import kz.kazmoto.glob.utils.UniqueFieldChecker;
import kz.kazmoto.org.model.User;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
@LocalBean
public class UserEjb implements StdEjb<User> {
    @PersistenceContext(unitName = "kazmoto-org")
    private EntityManager em;

    private UniqueFieldChecker<User> fieldChecker = new UniqueFieldChecker<User>()
            .addChecker("username", user -> findByUsername(user.getUsername()));

    @Override
    public User findById(Long id){
        return em.find(User.class, id);
    }

    public User findByUsername(String username){
        TypedQuery<User> q = em.createNamedQuery("User.findByUsername", User.class);
        q.setParameter("username", username);
        return EJBUtils.getSingleResult(q);
    }

    @Override
    public User create(User user){
        fieldChecker.check(user, false);

        return em.merge(user);
    }

    @Override
    public User update(User user){
        fieldChecker.check(user, true);

        return em.merge(user);
    }

    public void updatedPassword(){

    }
}
