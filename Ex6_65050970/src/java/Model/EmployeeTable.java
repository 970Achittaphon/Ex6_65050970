package Model;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class EmployeeTable {

    private static final String PERSISTENCE_UNIT_NAME = "Ex6_65050970PU";
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    
    // Lock object for controlling access to the 'isLocked' status variable only
    private static final Object DML_LOCK = new Object();
    
    // Status variable to simulate the lock state (true = DML in progress)
    private static final AtomicBoolean isLocked = new AtomicBoolean(false);

    public static Employee findEmployeeById(String id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Employee.class, id);
        } finally {
            em.close();
        }
    }

    public static List<Employee> findAllEmployee() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Employee> query = em.createNamedQuery("Employee.findAll", Employee.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    

    public static int insertEmployee(Employee emp) {
        // --- Fail Fast Logic: Try to acquire the status lock ---
        synchronized (DML_LOCK) {
            if (isLocked.get()) {
                // If already locked, fail immediately (return 0)
                return 0; 
            }
            // If available, set the status to locked
            isLocked.set(true);
        }

        EntityManager em = emf.createEntityManager();
        
        try {
            // Check for Duplicate ID (Must be done while status is locked)
            if (findEmployeeById(emp.getEmployeeid()) != null) {
                return 0; // Duplicate ID error code
            }

            em.getTransaction().begin();
            // Thread A holds the 'isLocked' state while sleeping/persisting
            Thread.sleep(7000);
            
            em.persist(emp);
            em.getTransaction().commit();
            return 1;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return -1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return -1;
        } finally {
            em.close();
            // --- Release Lock Status: Must be synchronized ---
            synchronized (DML_LOCK) {
                isLocked.set(false);
            }
        }
    }

    public static int updateEmployee(Employee emp) {
        // --- Fail Fast Logic: Try to acquire the status lock ---
        synchronized (DML_LOCK) {
            if (isLocked.get()) {
                return 0; // Lock Contention: Fail Fast
            }
            isLocked.set(true);
        }
        
        EntityManager em = emf.createEntityManager();
        
        try {
            em.getTransaction().begin();
            Thread.sleep(7000);
            
            em.merge(emp);
            em.getTransaction().commit();
            return 1;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return -1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return -1;
        } finally {
            em.close();
            // --- Release Lock Status: Must be synchronized ---
            synchronized (DML_LOCK) {
                isLocked.set(false);
            }
        }
    }

    public static int deleteEmployee(String id) {
        // --- Fail Fast Logic: Try to acquire the status lock ---
        synchronized (DML_LOCK) {
            if (isLocked.get()) {
                return 0; // Lock Contention: Fail Fast
            }
            isLocked.set(true);
        }
        
        EntityManager em = emf.createEntityManager();
        
        try {
            em.getTransaction().begin();
            Thread.sleep(7000);
            
            Employee emp = em.find(Employee.class, id);
            if (emp != null) {
                em.remove(emp);
                em.getTransaction().commit();
                return 1;
            }
            return 0; // Not Found
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return -1;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return -1;
        } finally {
            em.close();
            // --- Release Lock Status: Must be synchronized ---
            synchronized (DML_LOCK) {
                isLocked.set(false);
            }
        }
    }
}