package com.example.miwok;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Words>{
    private int mColorResourceID;

    public WordAdapter(Context context, ArrayList<Words> words, int colorResourceID){
        super(context, 0, words);
        mColorResourceID = colorResourceID;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // x was supposed to be listItemView
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        Words currentWord = getItem(position);

        TextView miwokTextView = (TextView) view.findViewById(R.id.miwokword);
        miwokTextView.setText(currentWord.getMiwokTranslation());

        TextView englishTextView = (TextView) view.findViewById(R.id.englishword);
        englishTextView.setText(currentWord.getEnglishTranslation());

        ImageView imageView = (ImageView) view.findViewById(R.id.inimage);

        if(currentWord.hasImage()){
            imageView.setImageResource(currentWord.getImageResourceID());
            imageView.setVisibility(View.VISIBLE);
        }
        else{
            imageView.setVisibility(View.GONE);
        }

        View textContainer = view.findViewById(R.id.textContainer);
        int color = ContextCompat.getColor(getContext(), mColorResourceID);
        textContainer.setBackgroundColor(color);
        return view;
    }
}
