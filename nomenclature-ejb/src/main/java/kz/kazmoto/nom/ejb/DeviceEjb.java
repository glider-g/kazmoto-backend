package kz.kazmoto.nom.ejb;

import kz.kazmoto.glob.exceptions.UniqueFieldCodeException;
import kz.kazmoto.glob.utils.EJBUtils;
import kz.kazmoto.glob.utils.UniqueFieldChecker;
import kz.kazmoto.nom.model.Device;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Objects;

/**
 * @author Yernar 23.07.2019
 */
@Stateless
@LocalBean
public class DeviceEjb {
    @PersistenceContext(unitName = "kazmoto-nom")
    private EntityManager em;

    private UniqueFieldChecker<Device> fieldChecker = new UniqueFieldChecker<Device>()
            .addChecker("name", device -> findByNameExact(device.getName()))
            .addChecker("code", device -> findByCode(device.getCode()));


    public Device findById(Long id) {
        return em.find(Device.class, id);
    }

    public List<Device> findAll() {
        TypedQuery<Device> q = em.createNamedQuery("Device.findAll", Device.class);
        return q.getResultList();
    }

    private Device findByNameExact(String name) {
        TypedQuery<Device> q = em.createNamedQuery("Device.findByNameExact", Device.class);
        q.setParameter("name", name);
        return EJBUtils.getSingleResult(q);
    }

    private Device findByCode(Integer code) {
        TypedQuery<Device> q = em.createNamedQuery("Device.findByCode", Device.class);
        q.setParameter("code", code);
        return EJBUtils.getSingleResult(q);
    }

    public Device create(Device device) {
        fieldChecker.validate(device, false);

        return em.merge(device);
    }

    public Device update(Device device) {
        fieldChecker.validate(device, true);

        return em.merge(device);
    }

}
