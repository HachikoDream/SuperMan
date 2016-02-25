package com.dreamspace.superman.model;

/**
 * Created by Wells on 2015/9/15.
 */
public class Catalog {
    private String id;
    private String name;
    private String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Catalog catalog = (Catalog) o;

        if (id != catalog.id) return false;
        if (!name.equals(catalog.name)) return false;
        return image.equals(catalog.image);

    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
