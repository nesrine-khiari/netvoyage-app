package tn.fst.spring.netvoyage.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.io.Serializable;

import lombok.*;

@Entity
@Table(name = "MotInterdit")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MotInterdit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numMotInterdit")
    private Long numMotInterdit; // Clé primaire
    private String mot;

    public MotInterdit(String mot) {
        this.mot = mot;
    }


}
