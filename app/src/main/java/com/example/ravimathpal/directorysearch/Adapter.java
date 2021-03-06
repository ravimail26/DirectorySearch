package com.example.ravimathpal.directorysearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ravi Mathpal on 20-03-2017.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Directory directory;
    private List<Object> listOfObjects;
    private Context context;
    private onDirectoryAction listener;

    public Adapter(Context context, Directory directory, onDirectoryAction listener) {
        this.context = context;
        this.listener = listener;
        this.directory = directory;
        listOfObjects = new ArrayList<>();

        if (directory.getParentDirectory() != null) {
            listOfObjects.add(0);
        }
        if (!directory.listOfSubDirectories.isEmpty()) {
            for (Directory d : directory.getListOfSubDirectories()) {
                listOfObjects.add(d);
            }
        }
        if (!directory.getListOfDirectoryFiles().isEmpty()) {
            for (DirectoryFile file : directory.getListOfDirectoryFiles()) {
                listOfObjects.add(file);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (listOfObjects.get(position) instanceof Integer) {
            holder.image.setImageResource(R.drawable.folder);
            holder.name.setText("...");
            holder.lastModified.setText("--");
            holder.size.setText("--");
            holder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDirectoryTouched(directory.getParentDirectory());
                }
            });

        } else if (listOfObjects.get(position) instanceof Directory) {
            holder.image.setImageResource(R.drawable.folder);
            holder.name.setText(((Directory) listOfObjects.get(position)).getDirectoryName());
            holder.lastModified.setText("--");
            holder.size.setText("--");
            holder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDirectoryTouched((Directory) listOfObjects.get(position));
                }
            });

        } else if (listOfObjects.get(position) instanceof DirectoryFile) {
            Glide.with(context)
                    .load(JsonFile.AMAZON_URL + ((DirectoryFile) listOfObjects.get(position)).getThumb())
                    .crossFade()
                    .into(holder.image);
            holder.name.setText(((DirectoryFile) listOfObjects.get(position)).getName());
            holder.lastModified.setText(((DirectoryFile) listOfObjects.get(position)).getLastModified().substring(0, 10));
            holder.size.setText("" + (((DirectoryFile) listOfObjects.get(position)).getSize() / 1000) + " kb");
            holder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onImageTouched((DirectoryFile) listOfObjects.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listOfObjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, lastModified, size;
        private ImageView image;
        private LinearLayout row;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            lastModified = (TextView) itemView.findViewById(R.id.lastModified);
            size = (TextView) itemView.findViewById(R.id.size);
            image = (ImageView) itemView.findViewById(R.id.image);

            row = (LinearLayout) itemView.findViewById(R.id.row);
        }
    }

    interface onDirectoryAction {
        void onDirectoryTouched(Directory directory);

        void onImageTouched(DirectoryFile file);
    }
}
