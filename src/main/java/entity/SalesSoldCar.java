package entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "vehicles.sales_sold_car")
public class SalesSoldCar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sales_id")
    private Long salesId;

    @Column(name = "car_id")
    private Long carId;

    @Column(name = "total_sold")
    private Integer totalSold;

}
