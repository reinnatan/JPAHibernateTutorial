import entity.Mobil;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;
import java.util.Queue;


public class Main {

    private String DB_URL = "jdbc:mysql://localhost:3306/vehicles";
    private String DB_USER_NAME = "root";
    private String DB_PASSWORD = "";


    public Main(){
        EntityManager em = configConnection();
        getListMobil(em);
    }

    protected EntityManager configConnection(){
        LocalContainerEntityManagerFactoryBean lemf = new LocalContainerEntityManagerFactoryBean();
        //setting koneksi mysql
        lemf.setDataSource(getMysqlDataSource());
        //setingan entity package
        lemf.setPackagesToScan(new String[]{"entity"});
        //setting provider hibernate
        lemf.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        //setting Provider Hibernate
        lemf.setJpaVendorAdapter( new HibernateJpaVendorAdapter() );
        //setting nama bundling settingan
        lemf.setPersistenceUnitName( "mytestdomain" );
        //notifikasi settingan koneksi
        lemf.afterPropertiesSet();
        //mengambil entity manager factory(koneksi db)
        EntityManagerFactory emf = lemf.getObject();
        //membuat object entity manager buat melakukan transaksi di database
        return emf.createEntityManager();
    }

    protected void deleteMobil(EntityManager em){
        Mobil mobil = em.find(Mobil.class,2L);
        em.getTransaction().begin();
        em.remove(mobil);
        em.getTransaction().commit();
    }

    protected void updateMobil(EntityManager em){
        Mobil mobil = em.find(Mobil.class,2L);
        mobil.setYear(2019);
        em.getTransaction().begin();
        em.persist(mobil);
        em.getTransaction().commit();
    }

    protected void getListMobil(EntityManager em){
        List<Mobil>listcar = em.createQuery("SELECT m FROM Mobil m").getResultList();
        for (Mobil mobil : listcar){
            System.out.println(mobil.getId());
            System.out.print(mobil.getName());
            System.out.println(mobil.getYear());
            System.out.println(mobil.getType());
        }
    }

    protected void searchMobilByKey(EntityManager em){
        Mobil search = (Mobil)em.find(Mobil.class, 1L);
        System.out.println(search.getName());
    }

    protected void tambahMobil(EntityManager em){
        /* add data mobil */
        Mobil mobil1 =  new Mobil();
        mobil1.setName("XPander Cross");
        mobil1.setYear(2022);
        mobil1.setType("SUV");
        em.getTransaction().begin();
        em.persist(mobil1);
        em.getTransaction().commit();
    }


    protected void cariMobil(EntityManager em){
        /* cara 1 memakai query builder
        references : https://www.baeldung.com/hibernate-criteria-queries
        */
        /*
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Mobil> cr = cb.createQuery(Mobil.class);
        Root<Mobil> root = cr.from(Mobil.class);
        cr.select(root).where(cb.equal(root.get("year"), 2022));
        List<Mobil> listMobil =  em.createQuery(cr).getResultList();
         */

        /* cara 2 dengan menggunakan JPQL / HQL */
        //List<Mobil> listMobil = em.createQuery("SELECT m FROM Mobil m WHERE m.year=2022").getResultList();

        /* cara 3 dengan menggukan Native Query, Bentuk Query database */
        Query query = em.createNativeQuery("select * from mobil where year=2022", Mobil.class);
        List<Mobil> listMobil = (List<Mobil>) query.getResultList();
        for (Mobil mobil: listMobil){
            System.out.println(mobil.getName() +" "+mobil.getYear());
        }
    }

    protected  Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        //properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        return properties;
    }


    protected DataSource getMysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(DB_USER_NAME);
        dataSource.setPassword(DB_PASSWORD);
        return dataSource;
    }


    public static void main(String[] args) {
        new Main();

    }


}
