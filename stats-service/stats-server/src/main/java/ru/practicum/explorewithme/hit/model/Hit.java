package ru.practicum.explorewithme.hit.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hits", schema = "public")
@Getter
@Setter
@ToString
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String app;

    @Column(nullable = false)
    private String uri;

    @Column(nullable = false)
    private String ip;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Override
    public boolean equals(Object o) {
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        return id != null && id.equals(((Hit) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}