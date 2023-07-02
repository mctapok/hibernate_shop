import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Scanner;


public class MainApp {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Order.class)
                .addAnnotatedClass(Product.class)
                .buildSessionFactory();
        Session session = null;

        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("выберите команду: \n" +
                    "/showProductsByCustomer [name_Customer]\n" +
                    "/findCustomersByProductTitle [title_product]\n" +
                    "/removeCustomer [name_Customer] \n" +
                    "/removeProduct [title_product] \n" +
                    "/buy [name_Customer] [title_product]");
            String command = scanner.nextLine();

            String[] commandParts = command.split(" ");
            switch (commandParts[0]) {
                case "/showProductsByCustomer":
                    System.out.println("product by Customer");
                    session = factory.getCurrentSession();
                    session.beginTransaction();
                    Customer customer = (Customer) session
                            .createQuery("FROM * Customer Customer WHERE Customer.name = :name")
                            .setParameter("name", commandParts[1])
                            .getSingleResult();
                    List<Order> orderList = customer.getOrder();
                    orderList.forEach(p -> System.out.println("Product for " +
                            customer.getName() + " " + p.getProduct().toString()));
                    session.getTransaction().commit();
                    break;
                case "/findCustomersByProductTitle":
                    System.out.println("Customers by product");
                    session = factory.getCurrentSession();
                    session.beginTransaction();
                    Product product = (Product) session
                            .createQuery("FROM Product product WHERE product.name = :name")
                            .setParameter("name", commandParts[1]).getSingleResult();
                    orderList = product.getOrders();
                    orderList.forEach(c-> System.out.println(product.getName() +
                            " купили :" + c.getCustomer().toString()));
                    break;
                case "exit":
                    break;
            }
        } finally {
            factory.close();
            session.close();
        }
    }
}

