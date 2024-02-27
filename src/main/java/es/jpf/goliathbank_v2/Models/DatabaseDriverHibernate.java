package es.jpf.goliathbank_v2.Models;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.sql.ResultSet;

public class DatabaseDriverHibernate{
    private final SessionFactory sessionFactory;

    public DatabaseDriverHibernate() {
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(ssr).getMetadataBuilder().build();
        this.sessionFactory = metadata.getSessionFactoryBuilder().build();
    }

    public ResultSet getClientDatos(String email, String password) {
        return null;
    }

    public Admin getAdminDatos(String email, String password) {
        try (Session session = sessionFactory.openSession()) {
            // Supongo que tienes una entidad Admin mapeada en Hibernate
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

    public ResultSet getTransaccion(int idUsuario, int limite){
        return null;
    }

    public int obtenerIdUsuarioPorMovil(String s) {
        return 0;
    }

    public String obtenerNombreUsuario(int idEmisor) {
        return null;
    }

    public ResultSet getCuenta(int i) {
        return null;
    }

    public boolean insertarUsuario(String email, String password, String name, String apellidos, String movil) {
        return false;
    }

    public boolean insertarCuenta(int idUsuario, String numCuenta, BigDecimal saldo) {
        return false;
    }
}
