package covoiturage.project.InnoCov.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Route {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer id;

    private String departure;

    private String arrival;

    private Date departureDate;

    private int numberOfPassengers;

    @ManyToOne
    private User driver;
}
