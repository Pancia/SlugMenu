package com.dayzerostudio.slugguide.slugmenu.menu.menuobjects;

public class MenuItem {

    private String name = "";
    private Float rating = (float) -1;

    public MenuItem(String name) {
        this.name = name;
    }

    public MenuItem(String name, Float rating) {
        this.name = name;
        this.rating = rating;
    }

    public String getName() {
        return this.name;
    }

    public MenuItem setName(String str) {
        this.name = str;
        return this;
    }

    public Float getRating() {
        return this.rating;
    }

    public String getRating(String dflt) {
        if (rating < 0) {
            return dflt;
        } else {
            return String.valueOf(this.rating);
        }
    }

    public MenuItem setRating(Float rating) {
        this.rating = rating;
        return this;
    }

    @Override
    public String toString() {
        return getName()+":"+getRating();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MenuItem))
            throw new IllegalArgumentException("Passed object "+o.toString()+" was not a MenuItem");
        MenuItem obj = (MenuItem) o;
        return this.getName().equals(obj.getName()) && this.getRating().equals(obj.getRating());
    }
}
