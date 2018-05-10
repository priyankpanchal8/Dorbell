package com.example.a.varnidoorbell;

/**
 * Created by a on 17-Feb-2016.
 */
public class updateUIThread implements Runnable {
    //public Values values= new Values();;

    private String msg;

    public updateUIThread(String str) {
        this.msg = str;
//        HomePage.msg.setText(str);
    }

    @Override
    public void run() {
        if(this.msg.length()>2)
            UserHome.change_UI(this.msg);
        else
            UserHome.reset_UI(this.msg);

    }

}

