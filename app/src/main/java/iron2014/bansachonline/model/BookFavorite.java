package iron2014.bansachonline.model;

public class BookFavorite {
    int id;
    int masach;
    int mauser;
    String tensach;
    String anhbia;

    public BookFavorite(int id, int masach, int mauser, String tensach, String anhbia) {
        this.id = id;
        this.masach = masach;
        this.mauser = mauser;
        this.tensach = tensach;
        this.anhbia = anhbia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMasach() {
        return masach;
    }

    public void setMasach(int masach) {
        this.masach = masach;
    }

    public int getMauser() {
        return mauser;
    }

    public void setMauser(int mauser) {
        this.mauser = mauser;
    }

    public String getTensach() {
        return tensach;
    }

    public void setTensach(String tensach) {
        this.tensach = tensach;
    }

    public String getAnhbia() {
        return anhbia;
    }

    public void setAnhbia(String anhbia) {
        this.anhbia = anhbia;
    }
}
