/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plh24_ge3;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import plh24_ge3.exceptions.IllegalOrphanException;
import plh24_ge3.exceptions.NonexistentEntityException;
import plh24_ge3.exceptions.PreexistingEntityException;

public class GameJpaController implements Serializable {

    public GameJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Game game) throws PreexistingEntityException, Exception {
        if (game.getPrizecategoriesList() == null) {
            game.setPrizecategoriesList(new ArrayList<Prizecategories>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Prizecategories> attachedPrizecategoriesList = new ArrayList<Prizecategories>();
            for (Prizecategories prizecategoriesListPrizecategoriesToAttach : game.getPrizecategoriesList()) {
                prizecategoriesListPrizecategoriesToAttach = em.getReference(prizecategoriesListPrizecategoriesToAttach.getClass(), prizecategoriesListPrizecategoriesToAttach.getId());
                attachedPrizecategoriesList.add(prizecategoriesListPrizecategoriesToAttach);
            }
            game.setPrizecategoriesList(attachedPrizecategoriesList);
            em.persist(game);
            for (Prizecategories prizecategoriesListPrizecategories : game.getPrizecategoriesList()) {
                Game oldDrawidOfPrizecategoriesListPrizecategories = prizecategoriesListPrizecategories.getDrawid();
                prizecategoriesListPrizecategories.setDrawid(game);
                prizecategoriesListPrizecategories = em.merge(prizecategoriesListPrizecategories);
                if (oldDrawidOfPrizecategoriesListPrizecategories != null) {
                    oldDrawidOfPrizecategoriesListPrizecategories.getPrizecategoriesList().remove(prizecategoriesListPrizecategories);
                    oldDrawidOfPrizecategoriesListPrizecategories = em.merge(oldDrawidOfPrizecategoriesListPrizecategories);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGame(game.getDrawid()) != null) {
                throw new PreexistingEntityException("Game " + game + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Game game) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Game persistentGame = em.find(Game.class, game.getDrawid());
            List<Prizecategories> prizecategoriesListOld = persistentGame.getPrizecategoriesList();
            List<Prizecategories> prizecategoriesListNew = game.getPrizecategoriesList();
            List<String> illegalOrphanMessages = null;
            for (Prizecategories prizecategoriesListOldPrizecategories : prizecategoriesListOld) {
                if (!prizecategoriesListNew.contains(prizecategoriesListOldPrizecategories)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Prizecategories " + prizecategoriesListOldPrizecategories + " since its drawid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Prizecategories> attachedPrizecategoriesListNew = new ArrayList<Prizecategories>();
            for (Prizecategories prizecategoriesListNewPrizecategoriesToAttach : prizecategoriesListNew) {
                prizecategoriesListNewPrizecategoriesToAttach = em.getReference(prizecategoriesListNewPrizecategoriesToAttach.getClass(), prizecategoriesListNewPrizecategoriesToAttach.getId());
                attachedPrizecategoriesListNew.add(prizecategoriesListNewPrizecategoriesToAttach);
            }
            prizecategoriesListNew = attachedPrizecategoriesListNew;
            game.setPrizecategoriesList(prizecategoriesListNew);
            game = em.merge(game);
            for (Prizecategories prizecategoriesListNewPrizecategories : prizecategoriesListNew) {
                if (!prizecategoriesListOld.contains(prizecategoriesListNewPrizecategories)) {
                    Game oldDrawidOfPrizecategoriesListNewPrizecategories = prizecategoriesListNewPrizecategories.getDrawid();
                    prizecategoriesListNewPrizecategories.setDrawid(game);
                    prizecategoriesListNewPrizecategories = em.merge(prizecategoriesListNewPrizecategories);
                    if (oldDrawidOfPrizecategoriesListNewPrizecategories != null && !oldDrawidOfPrizecategoriesListNewPrizecategories.equals(game)) {
                        oldDrawidOfPrizecategoriesListNewPrizecategories.getPrizecategoriesList().remove(prizecategoriesListNewPrizecategories);
                        oldDrawidOfPrizecategoriesListNewPrizecategories = em.merge(oldDrawidOfPrizecategoriesListNewPrizecategories);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = game.getDrawid();
                if (findGame(id) == null) {
                    throw new NonexistentEntityException("The game with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Game game;
            try {
                game = em.getReference(Game.class, id);
                game.getDrawid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The game with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Prizecategories> prizecategoriesListOrphanCheck = game.getPrizecategoriesList();
            for (Prizecategories prizecategoriesListOrphanCheckPrizecategories : prizecategoriesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Game (" + game + ") cannot be destroyed since the Prizecategories " + prizecategoriesListOrphanCheckPrizecategories + " in its prizecategoriesList field has a non-nullable drawid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(game);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Game> findGameEntities() {
        return findGameEntities(true, -1, -1);
    }

    public List<Game> findGameEntities(int maxResults, int firstResult) {
        return findGameEntities(false, maxResults, firstResult);
    }

    private List<Game> findGameEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Game.class));
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

    public Game findGame(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Game.class, id);
        } finally {
            em.close();
        }
    }

    public int getGameCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Game> rt = cq.from(Game.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
