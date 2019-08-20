package kz.kazmoto.nom.ejb;

import kz.kazmoto.glob.exceptions.UniqueFieldCodeException;
import kz.kazmoto.glob.utils.EJBUtils;
import kz.kazmoto.nom.model.Device;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Yernar 23.07.2019
 */
@Stateless
@LocalBean
public class DeviceEjb {
    @PersistenceContext(unitName = "kazmoto-nom")
    private EntityManager em;

    public Device findById(Long id) {
        return em.find(Device.class, id);
    }

    public List<Device> findAll() {
        TypedQuery<Device> q = em.createNamedQuery("Device.findAll", Device.class);
        return q.getResultList();
    }

    private Device findByName(String name) {
        TypedQuery<Device> q = em.createNamedQuery("Device.findByName", Device.class);
        q.setParameter("name", name);
        return EJBUtils.getSingleResult(q);
    }

    public Device create(Device device) {
        Device oldDevice = findByName(device.getName());
        if (oldDevice != null) throw new UniqueFieldCodeException("Device with this name already exist");

        return em.merge(device);
    }

    public Device update(Device editedDevice) {
        Device otherDevice = findByName(editedDevice.getName());
        if (otherDevice != null && !otherDevice.equals(editedDevice)) throw new UniqueFieldCodeException("Device with this name already exist");

        Device device = findById(editedDevice.getId());
        device.setName(editedDevice.getName());
        return em.merge(device);
    }

}
