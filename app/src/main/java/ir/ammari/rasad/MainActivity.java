package ir.ammari.rasad;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.media.MediaPlayer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    private Boolean testURL(String URL) {
        Boolean[] result = {false};
        Thread thread = new Thread(() -> {
            try {
                java.net.URL url = new java.net.URL(URL);
                InputStream inputStream = url.openStream();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int readLen;
                while ((readLen = inputStream.read(buf)) != -1) {
                    outputStream.write(buf, 0, readLen);
                }

                if (outputStream.toString().strip().equals("200")) {
                    result[0] = true;
                } else {
                    result[0] = false;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result[0];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final TextView view = new TextView(this);
        final StringBuilder outputString = new StringBuilder("\n\n\nBegin\n");
        view.setText(outputString);
        setContentView(view);

        Map<String, String> sites = new HashMap<>();
        sites.put("GitHub Gists", "https://gist.githubusercontent.com/xmha97/94f6ba425d9874179fdd73fc0e2dc899/raw/e3a7ca5109c651f711000b3a02b4e032bd1d695f/status");
        sites.put("GitHub Repository", "https://raw.githubusercontent.com/xmha97/test/refs/heads/main/status");
        sites.put("GitHub Releases", "https://github.com/xmha97/test/releases/download/v1.0.0/status");
        sites.put("GitHub Pages", "https://xmha97.github.io/status");
        sites.put("Codeberg Repository", "https://codeberg.org/xmha97/test/raw/branch/main/status");
        sites.put("Codeberg Releases", "https://codeberg.org/xmha97/test/releases/download/v1.0.0/status");
        sites.put("GitLab", "https://gitlab.com/xmha97/test/-/raw/master/status");
        sites.put("DropBox", "https://www.dropbox.com/scl/fi/dovory2z1y9xnj6kxwyq7/status?rlkey=48kb8gpm3fjnx76oglv1bm3u0&st=hy5uz0th&dl=1");
        sites.put("Archive", "https://archive.org/download/xmha97/status");
        sites.put("Pastebin", "https://pastebin.com/raw/ER5BRSx7");
        sites.put("HamGit", "https://hamgit.ir/xmha97/test/-/raw/main/status");
        sites.put("AbreHamrahi", "https://abrehamrahi.ir/o/public/EaGlAEy6");

        for (Map.Entry<String, String> entry : sites.entrySet()) {
            outputString.append("\n").append(entry.getKey() + " - " + testURL(entry.getValue()).toString());
            view.setText(outputString);
            MediaPlayer mp = MediaPlayer.create(this, R.raw.notification_simple_01);
            mp.start();
        }

        outputString.append("\n\n").append("End");
        view.setText(outputString);
    }
}
