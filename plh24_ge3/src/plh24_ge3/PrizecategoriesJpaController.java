/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plh24_ge3;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import plh24_ge3.exceptions.NonexistentEntityException;
import plh24_ge3.exceptions.PreexistingEntityException;


public class PrizecategoriesJpaController implements Serializable {

    public PrizecategoriesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Prizecategories prizecategories) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Game drawid = prizecategories.getDrawid();
            if (drawid != null) {
                drawid = em.getReference(drawid.getClass(), drawid.getDrawid());
                prizecategories.setDrawid(drawid);
            }
            em.persist(prizecategories);
            if (drawid != null) {
                drawid.getPrizecategoriesList().add(prizecategories);
                drawid = em.merge(drawid);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPrizecategories(prizecategories.getId()) != null) {
                throw new PreexistingEntityException("Prizecategories " + prizecategories + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Prizecategories prizecategories) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Prizecategories persistentPrizecategories = em.find(Prizecategories.class, prizecategories.getId());
            Game drawidOld = persistentPrizecategories.getDrawid();
            Game drawidNew = prizecategories.getDrawid();
            if (drawidNew != null) {
                drawidNew = em.getReference(drawidNew.getClass(), drawidNew.getDrawid());
                prizecategories.setDrawid(drawidNew);
            }
            prizecategories = em.merge(prizecategories);
            if (drawidOld != null && !drawidOld.equals(drawidNew)) {
                drawidOld.getPrizecategoriesList().remove(prizecategories);
                drawidOld = em.merge(drawidOld);
            }
            if (drawidNew != null && !drawidNew.equals(drawidOld)) {
                drawidNew.getPrizecategoriesList().add(prizecategories);
                drawidNew = em.merge(drawidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = prizecategories.getId();
                if (findPrizecategories(id) == null) {
                    throw new NonexistentEntityException("The prizecategories with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Prizecategories prizecategories;
            try {
                prizecategories = em.getReference(Prizecategories.class, id);
                prizecategories.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The prizecategories with id " + id + " no longer exists.", enfe);
            }
            Game drawid = prizecategories.getDrawid();
            if (drawid != null) {
                drawid.getPrizecategoriesList().remove(prizecategories);
                drawid = em.merge(drawid);
            }
            em.remove(prizecategories);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Prizecategories> findPrizecategoriesEntities() {
        return findPrizecategoriesEntities(true, -1, -1);
    }

    public List<Prizecategories> findPrizecategoriesEntities(int maxResults, int firstResult) {
        return findPrizecategoriesEntities(false, maxResults, firstResult);
    }

    private List<Prizecategories> findPrizecategoriesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Prizecategories.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Prizecategories findPrizecategories(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Prizecategories.class, id);
        } finally {
            em.close();
        }
    }

    public int getPrizecategoriesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Prizecategories> rt = cq.from(Prizecategories.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
