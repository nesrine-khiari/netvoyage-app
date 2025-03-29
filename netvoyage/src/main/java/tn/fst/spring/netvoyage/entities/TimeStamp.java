package tn.fst.spring.netvoyage.entities;
import jakarta.persistence.*;
        import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass // Specifies that this class is not an entity by itself but is meant to be inherited by other entities.
@EntityListeners(AuditingEntityListener.class) //  Enables automatic timestamp management using @CreatedDate and @LastModifiedDate.
@Getter
@Setter
@SQLDelete(sql = "UPDATE #{#entityName} SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")  // Ensures soft-deleted records are not fetched
public abstract class TimeStamp {

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Version
    private int version;
}
