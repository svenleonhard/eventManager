package de.sevenldev.eventmanager.box.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A MaterialList.
 */
@Entity
@Table(name = "material_list")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MaterialList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "event_start")
    private Integer eventStart;

    @Column(name = "event_end")
    private Integer eventEnd;

    @Column(name = "location")
    private String location;

    @Column(name = "open")
    private Boolean open;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public MaterialList name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEventStart() {
        return eventStart;
    }

    public MaterialList eventStart(Integer eventStart) {
        this.eventStart = eventStart;
        return this;
    }

    public void setEventStart(Integer eventStart) {
        this.eventStart = eventStart;
    }

    public Integer getEventEnd() {
        return eventEnd;
    }

    public MaterialList eventEnd(Integer eventEnd) {
        this.eventEnd = eventEnd;
        return this;
    }

    public void setEventEnd(Integer eventEnd) {
        this.eventEnd = eventEnd;
    }

    public String getLocation() {
        return location;
    }

    public MaterialList location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean isOpen() {
        return open;
    }

    public MaterialList open(Boolean open) {
        this.open = open;
        return this;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaterialList)) {
            return false;
        }
        return id != null && id.equals(((MaterialList) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MaterialList{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", eventStart=" + getEventStart() +
            ", eventEnd=" + getEventEnd() +
            ", location='" + getLocation() + "'" +
            ", open='" + isOpen() + "'" +
            "}";
    }
}
