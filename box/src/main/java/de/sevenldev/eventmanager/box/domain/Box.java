package de.sevenldev.eventmanager.box.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Box.
 */
@Document(collection = "box")
public class Box implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("extened_id")
    private String extenedId;

    @NotNull
    @Size(min = 3)
    @Field("name")
    private String name;

    @Field("category")
    private String category;

    @Field("description")
    private String description;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExtenedId() {
        return extenedId;
    }

    public Box extenedId(String extenedId) {
        this.extenedId = extenedId;
        return this;
    }

    public void setExtenedId(String extenedId) {
        this.extenedId = extenedId;
    }

    public String getName() {
        return name;
    }

    public Box name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public Box category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public Box description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Box)) {
            return false;
        }
        return id != null && id.equals(((Box) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Box{" +
            "id=" + getId() +
            ", extenedId='" + getExtenedId() + "'" +
            ", name='" + getName() + "'" +
            ", category='" + getCategory() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
