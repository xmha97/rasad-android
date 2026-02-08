package ir.ammari.rasad;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TextView view = new TextView(this);
        view.setText("\n\n\nLoading");
        setContentView(view);
        new Thread(() -> {
            try {
                final InputStream inputStream = (InputStream)
                        new URL("https://raw.githubusercontent.com/xmha97/test/refs/heads/main/status").openConnection().getContent();
                final int bufLen = 1024;
                byte[] buf = new byte[bufLen];
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                int readLen;
                while ((readLen = inputStream.read(buf, 0, bufLen)) != -1)
                    outputStream.write(buf, 0, readLen);
                final String result = outputStream.toString();
                runOnUiThread(() -> view.setText("\n\n\n" + result));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
