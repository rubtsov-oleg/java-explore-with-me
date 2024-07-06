package ru.practicum.explorewithme.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.practicum.explorewithme.event.model.PgSQLEnumType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests", schema = "public")
@Getter
@Setter
@ToString
@TypeDef(
        name = "pgsql_enum",
        typeClass = PgSQLEnumType.class
)
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "event_id")
    private Integer event;

    @Column(name = "requester_id")
    private Integer requester;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status")
    @Type(type = "pgsql_enum")
    private RequestStatus status;
}
