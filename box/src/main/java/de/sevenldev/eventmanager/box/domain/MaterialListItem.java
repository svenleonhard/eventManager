package de.sevenldev.eventmanager.box.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A MaterialListItem.
 */
@Entity
@Table(name = "material_list_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MaterialListItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "checked_in")
    private Boolean checkedIn;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JsonIgnoreProperties("materialListItems")
    private MaterialList materialList;

    @ManyToOne
    @JsonIgnoreProperties("materialListItems")
    private Boxitem boxitem;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isCheckedIn() {
        return checkedIn;
    }

    public MaterialListItem checkedIn(Boolean checkedIn) {
        this.checkedIn = checkedIn;
        return this;
    }

    public void setCheckedIn(Boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public String getComment() {
        return comment;
    }

    public MaterialListItem comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public MaterialList getMaterialList() {
        return materialList;
    }

    public MaterialListItem materialList(MaterialList materialList) {
        this.materialList = materialList;
        return this;
    }

    public void setMaterialList(MaterialList materialList) {
        this.materialList = materialList;
    }

    public Boxitem getBoxitem() {
        return boxitem;
    }

    public MaterialListItem boxitem(Boxitem boxitem) {
        this.boxitem = boxitem;
        return this;
    }

    public void setBoxitem(Boxitem boxitem) {
        this.boxitem = boxitem;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaterialListItem)) {
            return false;
        }
        return id != null && id.equals(((MaterialListItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MaterialListItem{" +
            "id=" + getId() +
            ", checkedIn='" + isCheckedIn() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
