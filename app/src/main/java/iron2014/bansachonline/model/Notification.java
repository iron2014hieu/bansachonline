package iron2014.bansachonline.model;

public class Notification {
    private int id;
    private String tieude;
    private String mota;
    private String thoigian;
    private int mahoadon;
    private int mauser;
    private String loaithongbao;

    public Notification(int id, String tieude, String mota, String thoigian, int mahoadon, int mauser, String loaithongbao) {
        this.id = id;
        this.tieude = tieude;
        this.mota = mota;
        this.thoigian = thoigian;
        this.mahoadon = mahoadon;
        this.mauser = mauser;
        this.loaithongbao = loaithongbao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTieude() {
        return tieude;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getThoigian() {
        return thoigian;
    }

    public void setThoigian(String thoigian) {
        this.thoigian = thoigian;
    }

    public int getMahoadon() {
        return mahoadon;
    }

    public void setMahoadon(int mahoadon) {
        this.mahoadon = mahoadon;
    }

    public int getMauser() {
        return mauser;
    }

    public void setMauser(int mauser) {
        this.mauser = mauser;
    }

    public String getLoaithongbao() {
        return loaithongbao;
    }

    public void setLoaithongbao(String loaithongbao) {
        this.loaithongbao = loaithongbao;
    }
}
