package iron2014.bansachonline.Activity.AppChatActivity;

public class Modeltinnhan {
    public String sender;
    public String noidung;
    public String tennguoigui;
    public String idchattt;

    public Modeltinnhan() {
    }

    public Modeltinnhan(String sender, String noidung, String tennguoigui, String idchattt) {
        this.sender = sender;
        this.noidung = noidung;
        this.tennguoigui = tennguoigui;
        this.idchattt = idchattt;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getTennguoigui() {
        return tennguoigui;
    }

    public void setTennguoigui(String tennguoigui) {
        this.tennguoigui = tennguoigui;
    }

    public String getIdchattt() {
        return idchattt;
    }

    public void setIdchattt(String idchattt) {
        this.idchattt = idchattt;
    }
}
