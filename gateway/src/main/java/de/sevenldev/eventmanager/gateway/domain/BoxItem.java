package de.sevenldev.eventmanager.gateway.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A BoxItem.
 */
@Entity
@Table(name = "box_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "boxitem")
public class BoxItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "to_repair")
    private Boolean toRepair;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JsonIgnoreProperties("boxItems")
    private Box box;

    @ManyToOne
    @JsonIgnoreProperties("boxItems")
    private Item item;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isToRepair() {
        return toRepair;
    }

    public BoxItem toRepair(Boolean toRepair) {
        this.toRepair = toRepair;
        return this;
    }

    public void setToRepair(Boolean toRepair) {
        this.toRepair = toRepair;
    }

    public String getComment() {
        return comment;
    }

    public BoxItem comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Box getBox() {
        return box;
    }

    public BoxItem box(Box box) {
        this.box = box;
        return this;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public Item getItem() {
        return item;
    }

    public BoxItem item(Item item) {
        this.item = item;
        return this;
    }

    public void setItem(Item item) {
        this.item = item;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BoxItem)) {
            return false;
        }
        return id != null && id.equals(((BoxItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "BoxItem{" +
            "id=" + getId() +
            ", toRepair='" + isToRepair() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
