package kz.kazmoto.org.ejb;



import kz.kazmoto.glob.exceptions.UniqueFieldCodeException;
import kz.kazmoto.glob.utils.EJBUtils;
import kz.kazmoto.org.model.User;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
@LocalBean
public class UserEjb {
    @PersistenceContext(unitName = "kazmoto-org")
    private EntityManager em;

    public User findById(Long id){
        return em.find(User.class, id);
    }

    public User findByUsername(String username){
        TypedQuery<User> q = em.createNamedQuery("User.findByUsername", User.class);
        q.setParameter("username", username);
        return EJBUtils.getSingleResult(q);
    }
    public User create(User user){
        User existUser = findByUsername(user.getUsername());
        if (existUser != null) throw new UniqueFieldCodeException("username already exist");

        return em.merge(user);
    }

    public User update(User editedUser){
        User user = findById(editedUser.getId());

        user.setFirstName(editedUser.getFirstName());
        user.setLastName(editedUser.getLastName());

        return em.merge(user);
    }

    public void updatedPassword(){

    }
}
