package kz.kazmoto.glob.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class AppEjb {

    public String getSecretKey(){
        return "JKLSDUIBklfeis646541";
    }
}
