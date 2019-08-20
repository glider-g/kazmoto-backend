package kz.kazmoto.glob.utils;

import javax.persistence.TypedQuery;
import java.util.List;

public class EJBUtils {
    public static <T> T getSingleResult(TypedQuery<T> q){
        return getSingleResult(q,null);
    }

    public static <T> T getSingleResult(TypedQuery<T> q, T defaultValue){
        q.setMaxResults(1);
        List<T> result = q.getResultList();
        if (result.isEmpty()) return defaultValue;
        return result.get(0)!=null?result.get(0):defaultValue;
    }
}
