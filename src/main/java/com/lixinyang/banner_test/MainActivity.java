package com.lixinyang.banner_test;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bwie.xlistviewlibrary.View.XListView;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements XListView.IXListViewListener {
    List<String> list = new ArrayList<String>();
    private com.nostra13.universalimageloader.core.ImageLoader instance;
    String uri = "http://api.expoon.com/AppNews/getNewsList/type/2/p/1";
    List<ListBean.DataBean> list2 = new ArrayList<ListBean.DataBean>();
    int aa = 1;
    private MyAdapter adapter;
    String uri1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int qq = msg.what;
            switch (qq) {
                case 0:
                    //创造方法实现加载图片
                    banner.setImageLoader(new MLoader());
                    //把集合传过去
                    banner.setImages(list);
                    banner.start();

                    //banner点击事件
                          banner.setOnBannerListener(new OnBannerListener() {
                               @Override
                               public void OnBannerClick(int position) {
                                   Intent intent=new Intent(MainActivity.this,TwoActivity.class);
                                   intent.putExtra("title",list2.get(position).getNews_title());
                                   intent.putExtra("id",list2.get(position).getNews_id());
                                   intent.putExtra("image",list2.get(position).getPic_url());
                                   intent.putExtra("summary",list2.get(position).getNews_summary());
                                   startActivity(intent);
                                  // Toast.makeText(MainActivity.this,"++++++++++++++++"+position,Toast.LENGTH_LONG).show();
                               }
                           });
                    break;
                case 1:
                    adapter.notifyDataSetChanged();
                    uiComplete();
                    break;

            }
        }
    };
    private Banner banner;
    private TabLayout tabLayout;
    private XListView xv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        banner = (Banner) findViewById(R.id.banner);

        ConnlDef();
        tablayout_tag();
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setScrollPosition((position - 1) % 4, 1F, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        xv = (XListView) findViewById(R.id.xlv);
        xv.setPullLoadEnable(true);  //让XListView有上拉加载的功能;
        xv.setXListViewListener(MainActivity.this);// 接口回调要把接口实现类设置进去, MainActivity就是(XListViewListener)它的实现了
        setUri(1);
        httpclick();
        adapter = new MyAdapter();
        xv.setAdapter(adapter);
    }

    private void tablayout_tag() {
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        TabLayout.Tab tab1 = tabLayout.newTab().setText("全部");
        tabLayout.addTab(tab1, true);
        TabLayout.Tab tab2 = tabLayout.newTab().setText("呵呵呵");
        tabLayout.addTab(tab2, false);
        TabLayout.Tab tab3 = tabLayout.newTab().setText("嘻嘻嘻");
        tabLayout.addTab(tab3, false);
        TabLayout.Tab tab4 = tabLayout.newTab().setText("嘿嘿嘿");
        tabLayout.addTab(tab4, false);
        /*tabLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = View.inflate(MainActivity.this, R.layout.activity_poper, null);
                PopupWindow popupWindow = new PopupWindow(view1, 100, 100);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
                popupWindow.showAsDropDown(tabLayout, 70, -20);
                popupWindow.setOutsideTouchable(true);

            }
        });*/

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //tabLayout.setScrollPosition(0, 1F, true);
                View view1 = View.inflate(MainActivity.this, R.layout.activity_poper, null);
                PopupWindow popupWindow = new PopupWindow(view1, 100, 100);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
                popupWindow.showAsDropDown(tabLayout, 70, -20);
                popupWindow.setOutsideTouchable(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void ConnlDef() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    URL url = new URL(uri);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = null;
                    StringBuffer sb = new StringBuffer();
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                    Gson gson = new Gson();
                    ImageBean imageBean = gson.fromJson(sb.toString(), ImageBean.class);
                    List<ImageBean.DataBean> data = imageBean.getData();
                    for (int i = 0; i < data.size(); i++) {
                        list.add(data.get(i).getPic_url());
                    }
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    class MLoader extends ImageLoader {


        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            instance = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
            instance.displayImage((String) path, imageView);
        }
    }

    public void setUri(int cc) {
        uri1 = "http://api.expoon.com/AppNews/getNewsList/type/1/p/" + cc;

    }

    public void httpclick() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(uri1);
                try {
                    SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());
                    //得到服务器返回的数据;
                    HttpResponse response = defaultHttpClient.execute(httpPost);
                    //得到状态码
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        //entiry 里面封装的数据;
                        HttpEntity entity = response.getEntity();
                        //这个result就是json字符串，剩下的就是解析工作了；
                        String result = EntityUtils.toString(entity);
                        Gson gson = new Gson();
                        ListBean listBean = gson.fromJson(result, ListBean.class);
                        List<ListBean.DataBean> aa = listBean.getData();
                        list2.addAll(aa);
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onRefresh() {
        list2.clear();
        aa = 1;
        setUri(aa);
        httpclick();
    }

    @Override
    public void onLoadMore() {
        aa++;
        setUri(aa);
        httpclick();
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list2.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(MainActivity.this, R.layout.activity_xlist, null);
            }
            ImageView iv = view.findViewById(R.id.iv_list);
            TextView tv = view.findViewById(R.id.tv_list);
            tv.setText(list2.get(i).getNews_title());
            instance.displayImage(list2.get(i).getPic_url(), iv);
            return view;
        }
    }

    private void uiComplete() {
        xv.stopRefresh();//停止刷新
        xv.stopLoadMore();//停止上拉加载更多
        Date date = new Date();
        //Calendar instance = Calendar.getInstance();
        xv.setRefreshTime(date.getHours() + ";" + date.getMinutes() + ";" + date.getSeconds());
    }
}
