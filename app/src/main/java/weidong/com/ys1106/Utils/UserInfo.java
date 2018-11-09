package weidong.com.ys1106.Utils;

public class UserInfo {
    private String Account;
    private String name;
    private String qq;
    private String Pass;
    private String age;
    private String email;
    private String ph;
    private String sex;//1为男性，0为女性

    public UserInfo() {

    }

    public UserInfo(String account, String name, String qq, String pss) {
        this.qq = qq;
        this.Account = account;
        this.name = name;
        this.Pass = pss;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public String getPass() {
        return Pass;
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
