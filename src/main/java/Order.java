import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "customer_product")
public class Order {

    @Embeddable
    public static class Id implements Serializable {
        @Column(name = "product_id")
        private int productId;

        @Column(name = "customer_id")
        private int customerId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Id id = (Id) o;
            return productId == id.productId && customerId == id.customerId;
        }

        @Override
        public int hashCode() {
            return productId + customerId;
        }
    }

    @EmbeddedId
    private Id id;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable=false, updatable=false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "customer_id", insertable=false, updatable=false)
    private Customer customer;

    @Column(name = "price")
    private float price;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Order " + id + "customer " + customer + " product " + product;
    }
}
