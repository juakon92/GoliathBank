package es.jpf.goliathbank_v2.Models;

import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;

public class DatabaseDriverHibernate{
    private final SessionFactory sessionFactory;

    public DatabaseDriverHibernate() {
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(ssr).getMetadataBuilder().build();
        this.sessionFactory = metadata.getSessionFactoryBuilder().build();
    }

    public Cliente getClientDatos(String email, String password) {
        try (Session session = sessionFactory.openSession()) {
            Query<Cliente> query = session.createQuery("FROM Cliente WHERE email = :email AND password = :password", Cliente.class);
            query.setParameter("email", email);
            query.setParameter("password", password);

            return query.uniqueResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al obtener datos del cliente: " + e.getMessage());
            return null;
        }
    }

    public Admin getAdminDatos(String email, String password) {
        try (Session session = sessionFactory.openSession()) {
            Query<Admin> query = session.createQuery("FROM Admin WHERE usuario = :email AND pass = :password", Admin.class);
            query.setParameter("email", email);
            query.setParameter("password", password);

            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al obtener datos de admin: " + e.getMessage());
        }
        return null;
    }

    public boolean insertarUsuario(String email, String password, String firstName, String lastName, String numTlfn) {
        try (Session session = sessionFactory.openSession()) {
            Cliente nuevoCliente = new Cliente(email, password, firstName, lastName, numTlfn);

            Transaction transaction = session.beginTransaction();
            session.save(nuevoCliente);
            transaction.commit();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }


    public ResultSet getTransaccion(int idUsuario, int limite){
        return null;
    }

    public int obtenerIdUsuario(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<Integer> query = session.createQuery("SELECT id FROM Cliente WHERE email = :email", Integer.class);
            query.setParameter("email", email);

            List<Integer> resultList = query.list();
            return resultList.isEmpty() ? -1 : resultList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int obtenerIdUsuarioPorMovil(String movil) {
        try (Session session = sessionFactory.openSession()) {
            Query<Integer> query = session.createQuery("SELECT id FROM Cliente WHERE movil = :movil", Integer.class);
            query.setParameter("movil", movil);

            List<Integer> resultList = query.list();
            return resultList.isEmpty() ? -1 : resultList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String obtenerNombreUsuario(int idUsuario) {
        try (Session session = sessionFactory.openSession()) {
            Query<String> query = session.createQuery("SELECT name FROM Cliente WHERE id = :idUsuario", String.class);
            query.setParameter("idUsuario", idUsuario);

            List<String> resultList = query.list();
            return resultList.isEmpty() ? null : resultList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public ResultSet getCuenta(int i) {
        return null;
    }

    public boolean insertarCuenta(int idUsuario, String numCuenta, BigDecimal saldo) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Cuenta cuenta = new Cuenta(idUsuario, numCuenta, saldo.doubleValue());
            session.save(cuenta);

            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
}
