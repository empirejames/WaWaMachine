package com.james.wawamachine.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.james.wawamachine.R;
import com.james.wawamachine.SetDataActivity;
import com.james.wawamachine.User;

import java.util.ArrayList;


    /**
     * Created by 101716 on 2017/6/27.
     */

    public class ListAdapter extends BaseAdapter implements Filterable {
        private ArrayList<User> mListItems;
        private LayoutInflater inflater;
        Context context;
        String TAG = ListAdapter.class.getSimpleName();

        public ListAdapter(Context context) {
            this.context = context;
        }

        public ListAdapter(Context context, ArrayList<User> itemList) {
            this.context = context;
            mListItems = itemList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mListItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mListItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mListItems.indexOf(getItem(position));
        }
        static class ViewHolder
        {
            private TextView storeName;
            private TextView lineID;
            private TextView price;
            private TextView content;
        }
        @Override
        public View getView(int position, final View convertView, ViewGroup parent) {
            final ViewHolder holder = new ViewHolder();

            final View row = inflater.inflate(R.layout.listdata, parent, false);
            final User item = mListItems.get(position);

            final ImageView img_right = row.findViewById(R.id.wawa_image);
            final StorageReference  mStorageRef = FirebaseStorage.getInstance().getReference();
            final StorageReference riversRef = mStorageRef.child("images/" +item.getImgName());
            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                            .using(new FirebaseImageLoader())
                            .load(riversRef)
                            .into(img_right);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e(TAG, exception.getMessage());
                }
            });







            Log.e(TAG, " Get View " + item.getName());
            holder.storeName = (TextView) row.findViewById(R.id.wawa_store);
            holder.storeName.setText(item.getName());
            holder.lineID = (TextView) row.findViewById(R.id.wawa_line);
            holder.lineID.setText(item.getLineID());
            holder.price = (TextView) row.findViewById(R.id.wawa_price);
            holder.price.setText(item.getPrice());
            holder.content = (TextView) row.findViewById(R.id.wawa_content);
            holder.content.setText(item.getContanter());
            return row;
        }

        @Override
        public Filter getFilter() {

            return null;
        }
    }
