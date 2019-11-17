package iron2014.bansachonline.model;

public class Nhaxuatban {
    int manxb;
    String tennxb;
    String hinhanh;
    String mota;

    public Nhaxuatban(int manxb, String tennxb, String hinhanh, String mota) {
        this.manxb = manxb;
        this.tennxb = tennxb;
        this.hinhanh = hinhanh;
        this.mota = mota;
    }

    public int getManxb() {
        return manxb;
    }

    public void setManxb(int manxb) {
        this.manxb = manxb;
    }

    public String getTennxb() {
        return tennxb;
    }

    public void setTennxb(String tennxb) {
        this.tennxb = tennxb;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }
}
