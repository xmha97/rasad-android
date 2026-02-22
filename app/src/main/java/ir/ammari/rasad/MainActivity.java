package ir.ammari.rasad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        put("Archive (Tor)", "https://archivep75mbjunhxc6x4j5mwjmomyxb573v42baldlqu56ruil2oiad.onion/download/xmha97/status");
        put("Pastebin", "https://pastebin.com/raw/ER5BRSx7");
        put("Wikipedia (English)", "https://en.wikipedia.org/w/index.php?title=User:Xmha97/status.js&action=raw&ctype=text/javascript");
        put("Wikipedia (Persian)", "https://fa.wikipedia.org/w/index.php?title=User:Xmha97/status.js&action=raw&ctype=text/javascript");
        put("Wikipedia (Arabic)", "https://ar.wikipedia.org/w/index.php?title=User:Xmha97/status.js&action=raw&ctype=text/javascript");
        put("HamGit (GitLab)", "https://hamgit.ir/xmha97/test/-/raw/main/status");
        put("AbreHamrahi", "https://abrehamrahi.ir/o/public/EaGlAEy6");

        put("GitHub Gists (HTTP)", "http://gist.githubusercontent.com/xmha97/94f6ba425d9874179fdd73fc0e2dc899/raw/e3a7ca5109c651f711000b3a02b4e032bd1d695f/status");
        put("GitHub Repository (HTTP)", "http://raw.githubusercontent.com/xmha97/test/refs/heads/main/status");
        put("GitHub Releases (HTTP)", "http://github.com/xmha97/test/releases/download/v1.0.0/status");
        put("GitHub Pages (HTTP)", "http://xmha97.github.io/status");
        put("Codeberg Repository (HTTP)", "http://codeberg.org/xmha97/test/raw/branch/main/status");
        put("Codeberg Releases (HTTP)", "http://codeberg.org/xmha97/test/releases/download/v1.0.0/status");
        put("GitLab (HTTP)", "http://gitlab.com/xmha97/test/-/raw/master/status");
        put("DropBox (HTTP)", "http://www.dropbox.com/scl/fi/dovory2z1y9xnj6kxwyq7/status?rlkey=48kb8gpm3fjnx76oglv1bm3u0&st=hy5uz0th&dl=1");
        put("Archive (HTTP)", "http://archive.org/download/xmha97/status");
        put("Archive (Tor) (HTTP)", "http://archivep75mbjunhxc6x4j5mwjmomyxb573v42baldlqu56ruil2oiad.onion/download/xmha97/status");
        put("Pastebin (HTTP)", "http://pastebin.com/raw/ER5BRSx7");
        put("Wikipedia (English) (HTTP)", "http://en.wikipedia.org/w/index.php?title=User:Xmha97/status.js&action=raw&ctype=text/javascript");
        put("Wikipedia (Persian) (HTTP)", "http://fa.wikipedia.org/w/index.php?title=User:Xmha97/status.js&action=raw&ctype=text/javascript");
        put("Wikipedia (Arabic) (HTTP)", "http://ar.wikipedia.org/w/index.php?title=User:Xmha97/status.js&action=raw&ctype=text/javascript");
        put("HamGit (GitLab) (HTTP)", "http://hamgit.ir/xmha97/test/-/raw/main/status");
        put("AbreHamrahi (HTTP)", "http://abrehamrahi.ir/o/public/EaGlAEy6");
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
                final var color = new ForegroundColorSpan(success ? 0xFF007500 : Color.RED);
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
        textView.setId(R.id.result);
        textView.setFreezesText(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            textView.setTextIsSelectable(true);
        }
        textView.setHorizontallyScrolling(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
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
        final var pingButton = new Button(this);
        pingButton.setText(R.string.ping);
        pingButton.setOnClickListener((v) -> ping(textView));
        pingButton.setLayoutParams(buttonLayoutParams);
        buttonBar.addView(pingButton);
        root.addView(buttonBar);
        setContentView(root);
    }

    @SuppressLint("SetTextI18n")
    private void ping(TextView textView) {
        textView.setText("");
        new Thread(() -> {
            final var runtime = Runtime.getRuntime();
            try (final var inputStream = runtime.exec("ping -c 4 google.com").getInputStream()) {
                final var br = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = br.readLine()) != null) {
                    final var finalLine = line;
                    runOnUiThread(() -> textView.setText(textView.getText() + "\n" + finalLine));
                }
                br.close();
            } catch (IOException e) {
                textView.setText(e.getMessage());
            }
        }).start();
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
