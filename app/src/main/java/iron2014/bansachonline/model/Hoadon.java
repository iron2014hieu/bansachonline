package iron2014.bansachonline.model;

public class Hoadon {
    int mahoadon;
    int mauser;
    String ngayxuat;
    int tongtien;
    String tenkh;
    String diachi;
    String sdt;
    String tinhtrang;

    public Hoadon(int mahoadon, int mauser, String ngayxuat, int tongtien, String tenkh, String diachi, String sdt, String tinhtrang) {
        this.mahoadon = mahoadon;
        this.mauser = mauser;
        this.ngayxuat = ngayxuat;
        this.tongtien = tongtien;
        this.tenkh = tenkh;
        this.diachi = diachi;
        this.sdt = sdt;
        this.tinhtrang = tinhtrang;
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

    public String getNgayxuat() {
        return ngayxuat;
    }

    public void setNgayxuat(String ngayxuat) {
        this.ngayxuat = ngayxuat;
    }

    public int getTongtien() {
        return tongtien;
    }

    public void setTongtien(int tongtien) {
        this.tongtien = tongtien;
    }

    public String getTenkh() {
        return tenkh;
    }

    public void setTenkh(String tenkh) {
        this.tenkh = tenkh;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getTinhtrang() {
        return tinhtrang;
    }

    public void setTinhtrang(String tinhtrang) {
        this.tinhtrang = tinhtrang;
    }
}
