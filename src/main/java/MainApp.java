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
            System.out.println("""
                    выберите команду:\s
                    /showProductsByCustomer [name_Customer]
                    /findCustomersByProductTitle [title_product]
                    /removeCustomer [name_Customer]\s
                    /removeProduct [title_product]\s
                    /buy [name_Customer] [title_product]
                    menu
                    exit""");
            while(scanner.hasNext()) {
                String command = scanner.nextLine();
                if (command.equals("exit")) break;

                String[] commandParts = command.split(" ");
                switch (commandParts[0]) {
                    case "/showProductsByCustomer":
                        System.out.println("product by Customer");
                        session = factory.getCurrentSession();
                        session.beginTransaction();
                        Customer customer = (Customer) session
                                .createQuery("FROM Customer customer WHERE customer.name = :name")
                                .setParameter("name", commandParts[1]).getSingleResult();
                        List<Order> orderList = customer.getOrder();
                        orderList.forEach(p -> System.out.println("Product for " +
                                customer.getName() + " " + p.getProduct().toString()));
                        session.getTransaction().commit();
                        System.out.println("enter command ...");
                        break;
                    case "/findCustomersByProductTitle":
                        System.out.println("Customers by product");
                        session = factory.getCurrentSession();
                        session.beginTransaction();
                        Product product = (Product) session
                                .createQuery("FROM Product product WHERE product.name = :name")
                                .setParameter("name", commandParts[1]).getSingleResult();
                        orderList = product.getOrders();
                        orderList.forEach(c -> System.out.println(product.getName() +
                                " купили :" + c.getCustomer().toString()));
                        session.getTransaction().commit();
                        System.out.println("enter command ...");
                        break;
                    case "/removeCustomer":
                        System.out.println("delete Customer");
                        session = factory.getCurrentSession();
                        session.beginTransaction();
                        customer = (Customer) session
                                .createQuery("FROM Customer customer WHERE customer.name =:name")
                                .setParameter("name", commandParts[1]).getSingleResult();
                        System.out.println(customer.toString());
                        session.createQuery("delete FROM Customer WHERE id =:id")
                                .setParameter("id", customer.getId()).executeUpdate();
                        System.out.println("removed customer: " + customer.toString());
                        session.getTransaction().commit();
                        System.out.println("enter command ...");
                        break;
                    case "/removeProduct":
                        System.out.println("remove product");
                        session = factory.getCurrentSession();
                        session.beginTransaction();
                        product = (Product) session.createQuery("From Product product WHERE product.name =:name")
                                .setParameter("name", commandParts[1]).getSingleResult();
                        session.remove(product);
                        System.out.println("product removed: " + product.toString());
                        session.getTransaction().commit();
                        System.out.println("enter command ...");
                        break;
                    case "/buy":
                        System.out.println("create order");
                        session = factory.getCurrentSession();
                        session.beginTransaction();
                        customer = (Customer) session.createQuery("From Customer customer WHERE customer.name =:name")
                                .setParameter("name", commandParts[1]).getSingleResult();
                        product = (Product) session.createQuery("From Product product WHERE product.name =:name")
                                .setParameter("name", commandParts[2]).getSingleResult();

                        Order.Id orderId = new Order.Id();
                        orderId.setCustomerId(customer.getId());
                        orderId.setProductId(product.getId());

                        Order order = new Order();
                        order.setId(orderId);
                        order.setPrice(product.getPrice());

                        session.persist(order);

                        System.out.println("Customer " + customer.getName() + " buy " + product.getName());
                        session.getTransaction().commit();
                        System.out.println("enter command ...");
                        break;
                    case "menu":
                        System.out.println("""
                                выберите команду:\s
                                /showProductsByCustomer [name_Customer]
                                /findCustomersByProductTitle [title_product]
                                /removeCustomer [name_Customer]\s
                                /removeProduct [title_product]\s
                                /buy [name_Customer] [title_product]
                                menu
                                exit""");
                    default:
                        break;
                }
            }
        } finally {
            if (session != null) session.close();
            factory.close();
        }
    }
}

