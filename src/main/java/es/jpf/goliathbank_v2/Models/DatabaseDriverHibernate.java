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
import java.sql.*;
import java.time.LocalDate;
import java.util.Collections;
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


    public List<Transaccion> getTransaccion(int idUsuario, int limite) {
        try (Session session = sessionFactory.openSession()) {
            Query<Transaccion> query = session.createQuery(
                    "FROM Transaccion WHERE origen = :idUsuario OR destino = :idUsuario ORDER BY fecha DESC",
                    Transaccion.class
            );
            query.setParameter("idUsuario", idUsuario);
            query.setMaxResults(limite);

            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al obtener transacciones: " + e.getMessage());
            return null;
        }
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

    public boolean actualizarSaldoCuenta(int idUsuario, BigDecimal newBalance) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                String hql = "UPDATE Cuenta SET balance = :newBalance WHERE duenio = :idUsuario";
                Query query = session.createQuery(hql);
                query.setParameter("newBalance", newBalance.doubleValue());
                query.setParameter("idUsuario", idUsuario);

                int filasAfectadas = query.executeUpdate();

                System.out.println("Filas afectadas por la actualización: " + filasAfectadas);

                transaction.commit();

                return filasAfectadas > 0;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
                return false;
            }
        }
    }

    public boolean actualizarSaldoCuentaPorID(int idCuenta, BigDecimal newBalance) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                String hql = "UPDATE Cuenta SET balance = :newBalance WHERE id = :idCuenta";
                Query query = session.createQuery(hql);
                query.setParameter("newBalance", newBalance.doubleValue());
                query.setParameter("idCuenta", idCuenta);

                int filasAfectadas = query.executeUpdate();

                System.out.println("Filas afectadas por la actualización: " + filasAfectadas);

                transaction.commit();

                return filasAfectadas > 0;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
                return false;
            }
        }
    }

    public boolean realizarTransaccion(int idEmisor, int idReceptor, BigDecimal cantidad, String mensaje) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                BigDecimal emisorBalance = obtenerSaldoCuentaPorUsuario(idEmisor);
                if (emisorBalance.compareTo(cantidad) < 0) {
                    transaction.rollback();
                    return false;
                }

                Transaccion transaccion = new Transaccion(idEmisor, idReceptor, cantidad.doubleValue(), new Date(System.currentTimeMillis()).toLocalDate(), mensaje);
                session.save(transaccion);

                BigDecimal nuevoSaldoEmisor = emisorBalance.subtract(cantidad);
                BigDecimal receptorBalance = obtenerSaldoCuentaPorUsuario(idReceptor);
                BigDecimal nuevoSaldoReceptor = receptorBalance.add(cantidad);

                boolean actualizacionEmisor = actualizarSaldoCuenta(idEmisor, nuevoSaldoEmisor);
                boolean actualizacionReceptor = actualizarSaldoCuenta(idReceptor, nuevoSaldoReceptor);

                if (actualizacionEmisor && actualizacionReceptor) {
                    transaction.commit();
                    return true;
                } else {
                    transaction.rollback();
                    return false;
                }
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
                return false;
            }
        }
    }

    public BigDecimal obtenerSaldoCuentaPorUsuario(int idUsuario) {
        try (Session session = sessionFactory.openSession()) {
            Query<BigDecimal> query = session.createQuery("SELECT balance FROM Cuenta WHERE duenio = :idUsuario", BigDecimal.class);
            query.setParameter("idUsuario", idUsuario);

            List<BigDecimal> resultList = query.list();
            if (!resultList.isEmpty()) {
                return resultList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("No se pudo obtener el saldo de la cuenta");
    }

    public int obtenerIdUsuarioPorCuenta(String numCuenta) {
        try (Session session = sessionFactory.openSession()) {
            Query<Integer> query = session.createQuery("SELECT duenio FROM Cuenta WHERE numCuenta = :numCuenta", Integer.class);
            query.setParameter("numCuenta", numCuenta);

            List<Integer> resultList = query.list();
            return resultList.isEmpty() ? -1 : resultList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int obtenerIdCuenta(String numCuenta) {
        try (Session session = sessionFactory.openSession()) {
            Query<Integer> query = session.createQuery("SELECT id FROM Cuenta WHERE numCuenta = :numCuenta", Integer.class);
            query.setParameter("numCuenta", numCuenta);

            List<Integer> resultList = query.list();
            return resultList.isEmpty() ? -1 : resultList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Cliente obtenerClientePorId(int idUsuario) {
        try (Session session = sessionFactory.openSession()) {
            Query<Cliente> query = session.createQuery("FROM Cliente WHERE id = :idUsuario", Cliente.class);
            query.setParameter("idUsuario", idUsuario);

            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Cliente> obtenerClientes() {
        try (Session session = sessionFactory.openSession()) {
            Query<Cliente> query = session.createQuery("FROM Cliente", Cliente.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    public BigDecimal obtenerSaldoCuenta(int idCuenta) {
        try (Session session = sessionFactory.openSession()) {
            Query<BigDecimal> query = session.createQuery("SELECT balance FROM Cuenta WHERE id = :idCuenta", BigDecimal.class);
            query.setParameter("idCuenta", idCuenta);

            List<BigDecimal> resultList = query.list();
            if (!resultList.isEmpty()) {
                return resultList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("No se pudo obtener el saldo de la cuenta");
    }

    public List<Cuenta> getCuenta(int idUsuario) {
        try (Session session = sessionFactory.openSession()) {
            Query<Cuenta> query = session.createQuery(
                    "FROM Cuenta WHERE duenio = :idUsuario",
                    Cuenta.class
            );
            query.setParameter("idUsuario", idUsuario);

            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al obtener cuentas: " + e.getMessage());
            return null;
        }
    }

    public boolean insertarTransaccion(int idEmisor, int idReceptor, BigDecimal cantidad, LocalDate fecha, String mensaje) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            Transaccion transaccion = new Transaccion(idEmisor, idReceptor, cantidad.doubleValue(), fecha, mensaje);
            session.save(transaccion);

            transaction.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al insertar transacción: " + e.getMessage());
            return false;
        }
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
