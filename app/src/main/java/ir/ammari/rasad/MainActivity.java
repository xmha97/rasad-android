package ir.ammari.rasad;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    private void testURL(Map<String, String> status, @NonNull TextView textView, @NonNull String name, @NonNull URL url) {
        new Thread(() -> {
            var result = "Invalid result";
            try (final var inputStream = url.openStream()) {
                if (inputStream.read() == '2' && inputStream.read() == '0' && inputStream.read() == '0')
                    result = "success";
            } catch (IOException e) {
                result = e.getMessage();
                e.printStackTrace();
            }
            final var finalResult = result;
            runOnUiThread(() -> {
                status.put(name, finalResult);
                displayResult(status, textView);
            });
        }).start();
    }

    private void displayResult(Map<String, String> status, @NonNull TextView textView) {
        final var text = new SpannableStringBuilder();
        for (final var entry : sites.entrySet()) {
            final var key = entry.getKey();
            text.append(key);
            if (status.containsKey(key)) {
                text.append(" - ");
                var result = status.get(key);
                result = result == null ? "" : result;
                final var success = result.equals("success");
                final var color = new ForegroundColorSpan(success ? Color.GREEN : Color.RED);
                final var spannable = new SpannableString(result);
                spannable.setSpan(color, 0, result.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            textView.setTextIsSelectable(true);
        }
        textView.setHorizontallyScrolling(true);
        final var scrollView = new ScrollView(this);
        scrollView.addView(textView);
        final var scrollViewParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0
        );
        scrollViewParams.weight = 1f;
        scrollView.setLayoutParams(scrollViewParams);
        final var root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.addView(scrollView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            root.setFitsSystemWindows(true);
        }
        final var buttonLayoutParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        buttonLayoutParams.weight = 1f;
        final var buttonBar = new LinearLayout(this);
        final var testButton = new Button(this);
        testButton.setText(R.string.test);
        testButton.setOnClickListener((v) -> testAll(textView));
        testButton.setLayoutParams(buttonLayoutParams);
        buttonBar.addView(testButton);
        final var clearButton = new Button(this);
        clearButton.setText(R.string.clear);
        clearButton.setOnClickListener((v) -> textView.setText(""));
        clearButton.setLayoutParams(buttonLayoutParams);
        buttonBar.addView(clearButton);
        root.addView(buttonBar);
        setContentView(root);
    }

    private void testAll(TextView textView) {
        final var status = new HashMap<String, String>();
        displayResult(status, textView);
        for (final var entry : sites.entrySet()) {
            try {
                testURL(status, textView, entry.getKey(), new URL(entry.getValue()));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
