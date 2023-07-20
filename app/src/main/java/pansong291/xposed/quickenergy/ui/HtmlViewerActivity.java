package pansong291.xposed.quickenergy.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;
import pansong291.xposed.quickenergy.R;

public class HtmlViewerActivity extends Activity {
    MyWebView mWebView;
    ProgressBar pgb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_viewer);

        mWebView = findViewById(R.id.mwv_webview);
        pgb = findViewById(R.id.pgb_webview);

        mWebView.setWebChromeClient(
                new WebChromeClient() {
                    @Override
                    public void onProgressChanged(WebView view, int progress) {
                        pgb.setProgress(progress);
                        if(progress < 100) {
                            setTitle("Loading...");
                            pgb.setVisibility(View.VISIBLE);
                        } else {
                            setTitle(mWebView.getTitle());
                            pgb.setVisibility(View.GONE);
                        }
                    }
                });
        mWebView.loadUrl(getIntent().getData().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "浏览器中打开");
        menu.add(0, 2, 0, "复制链接(可浏览器打开)");
        menu.add(0, 3, 0, "跳到顶部");
        menu.add(0, 4, 0, "跳到底部");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case 1:
                Intent it = new Intent(Intent.ACTION_VIEW);
                it.addCategory(Intent.CATEGORY_DEFAULT);
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                it.setDataAndType(getIntent().getData(), "text/html");
                startActivity(Intent.createChooser(it, "选择一个浏览器"));
                break;

            case 2:
                ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText(null, mWebView.getUrl()));
                Toast.makeText(this, "复制成功！", Toast.LENGTH_SHORT).show();
                break;

            case 3:
                mWebView.scrollTo(0, 0);
                break;

            case 4:
                mWebView.scrollToBottom();
                break;
        }
        return true;
    }
}
