package ru.practicum.explorewithme.event.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.practicum.explorewithme.category.Category;
import ru.practicum.explorewithme.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events", schema = "public")
@Getter
@Setter
@ToString
@TypeDef(
        name = "pgsql_enum",
        typeClass = PgSQLEnumType.class
)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String annotation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Embedded
    private Location location;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean paid;

    @Column(name = "participant_limit", columnDefinition = "integer default 0")
    private int participantLimit;

    @Column(name = "request_moderation", nullable = false, columnDefinition = "boolean default true")
    private boolean requestModeration;

    @Column(nullable = false)
    private String title;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private User initiator;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_state")
    @Type(type = "pgsql_enum")
    private EventState state;

    @Embeddable
    @Getter
    @Setter
    public static class Location {
        @Column(nullable = false)
        private double lat;

        @Column(nullable = false)
        private double lon;
    }
}