package app.vt.incomingcall;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CallReceiver extends PhonecallReceiver {
    private OnImcomingServiceListener onImcomingServiceListener = null;

    public void setOnImcomingServiceListener(OnImcomingServiceListener onImcomingServiceListener) {
        this.onImcomingServiceListener = onImcomingServiceListener;
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        String msg = "start outgoing call: " + number + " at " + start;
        System.out.println("LocDX CallReceiver.onOutgoingCallStarted " + msg);
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        String msg = "end outgoing call: " + number + " at " + end;
        System.out.println("LocDX CallReceiver.onOutgoingCallEnded " + msg);
    }

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
        String msg = "Incoming call: " + number + " at " + dateToString(start) + System.getProperty("line.separator");
        System.out.println("LocDX CallReceiver.onIncomingCallStarted " + msg);

        SharedPreferences prefs = ctx.getSharedPreferences("VT_DEMO", MODE_PRIVATE);
        String oldMsg = prefs.getString("msg", "");

        SharedPreferences.Editor editor = ctx.getSharedPreferences("VT_DEMO", MODE_PRIVATE).edit();
        editor.putString("msg", oldMsg + msg);
        editor.apply();
        if (onImcomingServiceListener != null) {
            onImcomingServiceListener.updateUI();
        }
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        String msg = "end incoming call: " + number + " at " + end;
        System.out.println("LocDX CallReceiver.onIncomingCallEnded " + msg);
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date missed) {
        String msg = "missed call: " + number + " at " + missed;
        System.out.println("LocDX CallReceiver.onMissedCall " + msg);
    }

    private String dateToString(Date date) {
        String pattern = "MM/dd/yyyy HH:mm:ss";
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public interface OnImcomingServiceListener {
        void updateUI();
    }
}
