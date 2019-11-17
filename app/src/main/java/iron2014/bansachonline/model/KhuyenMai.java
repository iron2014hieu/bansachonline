package iron2014.bansachonline.model;

public class KhuyenMai {
    int id;
    String tenKM;
    String maKM;
    String tinhChat;
    String ngayBatDau;
    String hanSuDung;

    public KhuyenMai(int id, String tenKM, String maKM, String tinhChat, String ngayBatDau, String hangSuDung) {
        this.id = id;
        this.tenKM = tenKM;
        this.maKM = maKM;
        this.tinhChat = tinhChat;
        this.ngayBatDau = ngayBatDau;
        hanSuDung = hangSuDung;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenKM() {
        return tenKM;
    }

    public void setTenKM(String tenKM) {
        this.tenKM = tenKM;
    }

    public String getMaKM() {
        return maKM;
    }

    public void setMaKM(String maKM) {
        this.maKM = maKM;
    }

    public String getTinhChat() {
        return tinhChat;
    }

    public void setTinhChat(String tinhChat) {
        this.tinhChat = tinhChat;
    }

    public String getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(String ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public String getHangSuDung() {
        return hanSuDung;
    }

    public void setHangSuDung(String hangSuDung) {
        hanSuDung = hangSuDung;
    }
}
