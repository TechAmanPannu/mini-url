package com.miniurl.model.response;

import lombok.Data;

import java.util.Collection;


@Data
public class CollectionResponse<T> {

    private final Collection<T> items;
    private final Integer offSet;

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public CollectionResponse(Collection<T> items, Integer offSet) {
        this.items = items;
        this.offSet = offSet;
    }

    public static class Builder<T> {
        private Collection<T> items;
        private Integer offSet;

        public Builder<T> setItems(Collection<T> items) {
            this.items = items;
            return this;
        }

        public Builder<T> setOffSet(Integer offSet) {
            this.offSet = offSet;
            return this;
        }

        public CollectionResponse<T> build() {
            return new CollectionResponse<>(this.items, this.offSet);
        }
    }
}
