package iron2014.bansachonline.model;

public class CTHD {
    int id;
    int mahd;
    int masach;
    String tensach;
    int giaban;
    int soluong;
    String hinhanh;
    String noidungdanhgia;
    Double diemdanhgia;

    public CTHD(int id, int mahd, int masach, String tensach, int giaban, int soluong, String hinhanh, String noidungdanhgia, Double diemdanhgia) {
        this.id = id;
        this.mahd = mahd;
        this.masach = masach;
        this.tensach = tensach;
        this.giaban = giaban;
        this.soluong = soluong;
        this.hinhanh = hinhanh;
        this.noidungdanhgia = noidungdanhgia;
        this.diemdanhgia = diemdanhgia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMahd() {
        return mahd;
    }

    public void setMahd(int mahd) {
        this.mahd = mahd;
    }

    public int getMasach() {
        return masach;
    }

    public void setMasach(int masach) {
        this.masach = masach;
    }

    public String getTensach() {
        return tensach;
    }

    public void setTensach(String tensach) {
        this.tensach = tensach;
    }

    public int getGiaban() {
        return giaban;
    }

    public void setGiaban(int giaban) {
        this.giaban = giaban;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getNoidungdanhgia() {
        return noidungdanhgia;
    }

    public void setNoidungdanhgia(String noidungdanhgia) {
        this.noidungdanhgia = noidungdanhgia;
    }

    public Double getDiemdanhgia() {
        return diemdanhgia;
    }

    public void setDiemdanhgia(Double diemdanhgia) {
        this.diemdanhgia = diemdanhgia;
    }
}
