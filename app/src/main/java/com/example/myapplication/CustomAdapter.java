
package com.example.myapplication;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.AppInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CustomAdapter extends BaseAdapter {
    private ListActivity contextList;
    private Context context;
    private ArrayList<AppInfo> apps;
    private List<AppInfo> searchList;

    public CustomAdapter(ListActivity contextList, Context context) {
        this.context = context;
        this.contextList = contextList;
        installApps();
        this.searchList = new ArrayList<>(this.apps);
    }

    public void installApps() {
        PackageManager pm = this.context.getPackageManager();

        this.apps = new ArrayList<>();
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> mainApps = pm.queryIntentActivities(i, 0);

        for (ResolveInfo ri : mainApps) {
            AppInfo app = new AppInfo();
            app.label = ri.loadLabel(pm);
            app.packageName = ri.activityInfo.packageName;
            Log.i(" Log package ", app.packageName.toString());
            app.picture = ri.activityInfo.loadIcon(pm);
            app.date = new Date();
            if(app.label.equals("My Application"))
                continue;
            apps.add(app);
        }
    }

    public void filter(String s){
        s = s.toLowerCase(Locale.getDefault());
        this.apps.clear();
        if(s.length() == 0)
            this.apps.addAll(this.searchList);
        else
            for (AppInfo a : this.searchList){
                if(a.label.toString().toLowerCase(Locale.getDefault()).contains(s))
                    this.apps.add(a);
            }
        notifyDataSetChanged();
    }

    public void sortByName(){
        Log.e("CREATE", this.apps.get(this.apps.size() - 1).label.toString());
        for(int i = 0; i < this.apps.size(); i++)
            for(int j = i + 1; j < this.apps.size(); j++)
                if(this.apps.get(i).label.toString().compareTo(this.apps.get(j).label.toString()) > 0)
                {
                    Collections.swap(this.apps, i, j);

                }
        notifyDataSetChanged();
    }

    public void sortByRecentlyUsed(){
        for(int i = 0; i < this.apps.size(); i++)
            for(int j = i + 1; j < this.apps.size(); j++)
                if(this.apps.get(i).date.compareTo(this.apps.get(j).date) < 0)
                {
                    Collections.swap(this.apps, i, j);

                }
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return this.apps.size();
    }

    @Override
    public Object getItem(int i) {
        return this.apps.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View element;

        if(convertView == null){
            LayoutInflater inflater = this.contextList.getLayoutInflater();
            element = (View) inflater.inflate(R.layout.app, null);

            ViewHolderItem viewHolder =  new ViewHolderItem();
            viewHolder.name = (TextView) element.findViewById(R.id.elementName);
            viewHolder.image = (ImageView) element.findViewById(R.id.elementImage);

            element.setTag(viewHolder);
        }
        else element = convertView;

        ViewHolderItem viewHolder = (ViewHolderItem)element.getTag();
        viewHolder.name.setText(apps.get(i).label);
        viewHolder.image.setImageDrawable(apps.get(i).picture);

        this.contextList.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PackageManager pm = context.getPackageManager();
                Intent i = pm.getLaunchIntentForPackage(apps.get(position).packageName.toString());
                apps.get(position).date = new Date();
                context.startActivity(i);
            }
        });

        return element;
    }


}

