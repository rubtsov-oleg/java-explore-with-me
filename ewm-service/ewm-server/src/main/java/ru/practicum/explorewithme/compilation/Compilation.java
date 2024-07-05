package ru.practicum.explorewithme.compilation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "compilations", schema = "public")
@Getter
@Setter
@ToString
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ElementCollection
    @CollectionTable(name = "compilation_events", joinColumns = @JoinColumn(name = "compilation_id"))
    @Column(name = "event_id")
    private List<Integer> events;

    @Column(nullable = false)
    private Boolean pinned;

    @Column(nullable = false, length = 50)
    private String title;
}
