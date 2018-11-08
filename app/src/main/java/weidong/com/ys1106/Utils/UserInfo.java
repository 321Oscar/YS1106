package weidong.com.ys1106.Utils;

public class UserInfo {
    public String Account;
    public String name;
    public String pass;
    public String qq;

    public UserInfo(){

    }

    public UserInfo(String account, String name, String qq){
        this.qq = qq;
        this.Account = account;
        this.name = name;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
