package iron2014.bansachonline.model;

public class Tacgia {
    int matacgia;
    String tentacgia;
    String hinhanh;
    String mota;

    public Tacgia(int matacgia, String tentacgia, String hinhanh, String mota) {
        this.matacgia = matacgia;
        this.tentacgia = tentacgia;
        this.hinhanh = hinhanh;
        this.mota = mota;
    }

    public int getMatacgia() {
        return matacgia;
    }

    public void setMatacgia(int matacgia) {
        this.matacgia = matacgia;
    }

    public String getTentacgia() {
        return tentacgia;
    }

    public void setTentacgia(String tentacgia) {
        this.tentacgia = tentacgia;
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
