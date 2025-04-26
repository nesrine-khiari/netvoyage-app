package tn.fst.spring.netvoyage.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tn.fst.spring.netvoyage.enums.InvitationStatus;

import java.time.Instant;

@Entity
@Getter
@Setter
public class Invitation {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String emailInvite;
        private String token;
        private Instant dateEnvoi;

        @Enumerated(EnumType.STRING)
        private InvitationStatus status;

        @ManyToOne
        @JoinColumn(name = "entreprise_id")
        private Entreprise entreprise;


}
