package com.zhcx.netcar.pojo.DruidEntity;

import java.io.Serializable;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-09 10:45
 * 签到签退统计
 */
public class SignInBack implements Serializable {

    private String companyName;

    /**
     * 签到数
     */
    private int signIn;

    /**
     * 签退数
     */
    private int signBack;

    /**
     * 总数
     */
    private int total;

    public int getSignIn() {
        return signIn;
    }

    public void setSignIn(int signIn) {
        this.signIn = signIn;
    }

    public int getSignBack() {
        return signBack;
    }

    public void setSignBack(int signBack) {
        this.signBack = signBack;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "SignInBack{" +
                "companyName='" + companyName + '\'' +
                ", signIn=" + signIn +
                ", signBack=" + signBack +
                ", total=" + total +
                '}';
    }

//    public static void main(String[] args)throws Exception{
//        String str = "{'signIn':123,'signBack':456,'total':789}";
//        System.out.println(JsonUtil.toJSON(str,SignInBack.class).toString());
//    }

}
