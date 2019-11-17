package iron2014.bansachonline.model;

public class Cart {
    int id, masach, maUser;
    String tenSach, giaBan;
    int dathanhtoan;

    public Cart(int id, int masach, int maUser, String tenSach, String giaBan, int dathanhtoan) {
        this.id = id;
        this.masach = masach;
        this.maUser = maUser;
        this.tenSach = tenSach;
        this.giaBan = giaBan;
        this.dathanhtoan = dathanhtoan;
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

    public int getMaUser() {
        return maUser;
    }

    public void setMaUser(int maUser) {
        this.maUser = maUser;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(String giaBan) {
        this.giaBan = giaBan;
    }

    public int getDathanhtoan() {
        return dathanhtoan;
    }

    public void setDathanhtoan(int dathanhtoan) {
        this.dathanhtoan = dathanhtoan;
    }
}
