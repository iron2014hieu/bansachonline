package iron2014.bansachonline.Activity.AppChatActivity;

public class Modelchat {
   public  String Tennguoidung;
   public  String Noidungchat;
   public String idnguoidui;
   public String idchat;

    public Modelchat() {
    }

    public Modelchat(String tennguoidung, String noidungchat, String idnguoidui, String idchat) {
        Tennguoidung = tennguoidung;
        Noidungchat = noidungchat;
        this.idnguoidui = idnguoidui;
        this.idchat = idchat;
    }

    public String getTennguoidung() {
        return Tennguoidung;
    }

    public void setTennguoidung(String tennguoidung) {
        Tennguoidung = tennguoidung;
    }

    public String getNoidungchat() {
        return Noidungchat;
    }

    public void setNoidungchat(String noidungchat) {
        Noidungchat = noidungchat;
    }

    public String getIdnguoidui() {
        return idnguoidui;
    }

    public void setIdnguoidui(String idnguoidui) {
        this.idnguoidui = idnguoidui;
    }

    public String getIdchat() {
        return idchat;
    }

    public void setIdchat(String idchat) {
        this.idchat = idchat;
    }
}
