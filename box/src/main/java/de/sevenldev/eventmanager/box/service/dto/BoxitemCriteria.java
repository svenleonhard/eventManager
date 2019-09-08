package de.sevenldev.eventmanager.box.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link de.sevenldev.eventmanager.box.domain.Boxitem} entity. This class is used
 * in {@link de.sevenldev.eventmanager.box.web.rest.BoxitemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /boxitems?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BoxitemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter toRepair;

    private StringFilter comment;

    private LongFilter boxId;

    private LongFilter itemId;

    public BoxitemCriteria(){
    }

    public BoxitemCriteria(BoxitemCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.toRepair = other.toRepair == null ? null : other.toRepair.copy();
        this.comment = other.comment == null ? null : other.comment.copy();
        this.boxId = other.boxId == null ? null : other.boxId.copy();
        this.itemId = other.itemId == null ? null : other.itemId.copy();
    }

    @Override
    public BoxitemCriteria copy() {
        return new BoxitemCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BooleanFilter getToRepair() {
        return toRepair;
    }

    public void setToRepair(BooleanFilter toRepair) {
        this.toRepair = toRepair;
    }

    public StringFilter getComment() {
        return comment;
    }

    public void setComment(StringFilter comment) {
        this.comment = comment;
    }

    public LongFilter getBoxId() {
        return boxId;
    }

    public void setBoxId(LongFilter boxId) {
        this.boxId = boxId;
    }

    public LongFilter getItemId() {
        return itemId;
    }

    public void setItemId(LongFilter itemId) {
        this.itemId = itemId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BoxitemCriteria that = (BoxitemCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(toRepair, that.toRepair) &&
            Objects.equals(comment, that.comment) &&
            Objects.equals(boxId, that.boxId) &&
            Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        toRepair,
        comment,
        boxId,
        itemId
        );
    }

    @Override
    public String toString() {
        return "BoxitemCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (toRepair != null ? "toRepair=" + toRepair + ", " : "") +
                (comment != null ? "comment=" + comment + ", " : "") +
                (boxId != null ? "boxId=" + boxId + ", " : "") +
                (itemId != null ? "itemId=" + itemId + ", " : "") +
            "}";
    }

}
