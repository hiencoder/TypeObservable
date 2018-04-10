package vn.edu.imic.typeobservable.model;

/**
 * Created by MyPC on 10/04/2018.
 */

public class Note {
    private int id;
    private String note;

    public Note(int id, String note) {
        this.id = id;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return this.id + ", " + this.note;
    }
}
