package br.com.riverfy.model;

import br.com.riverfy.model.enums.DevotionalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "devotionals")
public class Devotional extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "devotional", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DevotionalPage> pages = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DevotionalStatus status = DevotionalStatus.DRAFT;

    @Column(nullable = false)
    private boolean active = true;
}
