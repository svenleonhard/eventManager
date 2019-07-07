package de.sevenldev.eventmanager.box.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;

/**
 * A Boxitem.
 */
@Document(collection = "boxitem")
public class Boxitem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("to_repair")
    private Boolean toRepair;

    @Field("comment")
    private String comment;

    @DBRef
    @Field("box")
    @JsonIgnoreProperties("boxitems")
    private Box box;

    @DBRef
    @Field("item")
    @JsonIgnoreProperties("boxitems")
    private Item item;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean isToRepair() {
        return toRepair;
    }

    public Boxitem toRepair(Boolean toRepair) {
        this.toRepair = toRepair;
        return this;
    }

    public void setToRepair(Boolean toRepair) {
        this.toRepair = toRepair;
    }

    public String getComment() {
        return comment;
    }

    public Boxitem comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Box getBox() {
        return box;
    }

    public Boxitem box(Box box) {
        this.box = box;
        return this;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public Item getItem() {
        return item;
    }

    public Boxitem item(Item item) {
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
        if (!(o instanceof Boxitem)) {
            return false;
        }
        return id != null && id.equals(((Boxitem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Boxitem{" +
            "id=" + getId() +
            ", toRepair='" + isToRepair() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
