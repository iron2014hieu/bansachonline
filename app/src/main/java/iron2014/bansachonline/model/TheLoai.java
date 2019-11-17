package iron2014.bansachonline.model;

public class TheLoai {
    private int maloai;
    private String tenloai;
    private String image;

    public TheLoai(int maloai, String tenloai, String image) {
        this.maloai = maloai;
        this.tenloai = tenloai;
        this.image = image;
    }

    public int getMaloai() {
        return maloai;
    }

    public void setMaloai(int maloai) {
        this.maloai = maloai;
    }

    public String getTenloai() {
        return tenloai;
    }

    public void setTenloai(String tenloai) {
        this.tenloai = tenloai;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
