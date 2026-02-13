package ir.ammari.rasad;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    private static final Map<String, String> sites = new HashMap<>() {{
        put("GitHub Gists", "https://gist.githubusercontent.com/xmha97/94f6ba425d9874179fdd73fc0e2dc899/raw/e3a7ca5109c651f711000b3a02b4e032bd1d695f/status");
        put("GitHub Repository", "https://raw.githubusercontent.com/xmha97/test/refs/heads/main/status");
        put("GitHub Releases", "https://github.com/xmha97/test/releases/download/v1.0.0/status");
        put("GitHub Pages", "https://xmha97.github.io/status");
        put("Codeberg Repository", "https://codeberg.org/xmha97/test/raw/branch/main/status");
        put("Codeberg Releases", "https://codeberg.org/xmha97/test/releases/download/v1.0.0/status");
        put("GitLab", "https://gitlab.com/xmha97/test/-/raw/master/status");
        put("DropBox", "https://www.dropbox.com/scl/fi/dovory2z1y9xnj6kxwyq7/status?rlkey=48kb8gpm3fjnx76oglv1bm3u0&st=hy5uz0th&dl=1");
        put("Archive", "https://archive.org/download/xmha97/status");
        put("Pastebin", "https://pastebin.com/raw/ER5BRSx7");
        put("HamGit", "https://hamgit.ir/xmha97/test/-/raw/main/status");
        put("AbreHamrahi", "https://abrehamrahi.ir/o/public/EaGlAEy6");
    }};
    private final Map<String, Boolean> status = new HashMap<>();

    private void testURL(@NonNull TextView textView, @NonNull String name, @NonNull URL url) {
        new Thread(() -> {
            var result = false;
            try (final var inputStream = url.openStream()) {
                final var outputStream = new ByteArrayOutputStream();
                final var buf = new byte[1024];
                int readLen;
                while ((readLen = inputStream.read(buf)) != -1) {
                    outputStream.write(buf, 0, readLen);
                }
                if (outputStream.toString().strip().equals("200")) result = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            final var finalResult = result;
            runOnUiThread(() -> {
                status.put(name, finalResult);
                displayResult(textView);
            });
        }).start();
    }

    private void displayResult(@NonNull TextView textView) {
        final var text = new SpannableStringBuilder();
        text.append("Begin\n\n\n\n");
        for (final var entry : sites.entrySet()) {
            final var key = entry.getKey();
            text.append(key);
            if (status.containsKey(key)) {
                text.append(" - ");
                final var success = Boolean.TRUE.equals(status.get(key));
                final var color = new ForegroundColorSpan(success ? Color.GREEN : Color.RED);
                final var string = success ? "success" : "fail";
                SpannableString spannable = new SpannableString(string);
                spannable.setSpan(color, 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                text.append(spannable);
                text.append("\n");
            } else text.append("…\n");
        }
        textView.setText(text);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final var textView = new TextView(this);
        setContentView(textView);
        testAll(textView);
        textView.setOnClickListener((v) -> testAll(textView));
    }

    private void testAll(TextView textView) {
        status.clear();
        displayResult(textView);
        for (final var entry : sites.entrySet()) {
            try {
                testURL(textView, entry.getKey(), new URL(entry.getValue()));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
