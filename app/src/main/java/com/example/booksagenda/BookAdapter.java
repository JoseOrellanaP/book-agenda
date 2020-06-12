package com.example.booksagenda;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BookAdapter extends BaseAdapter implements Filterable {

    Context context;
    private List<BooksItem> booksItems;
    private List<BooksItem> booksItemsFull;
    String chechedI;

    public BookAdapter(Context context, List<BooksItem> booksItems, String chechedI) {
        this.context = context;
        this.booksItems = booksItems;
        booksItemsFull = new ArrayList<>(booksItems);
        this.chechedI = chechedI;
    }

    @Override
    public int getCount() {
        return booksItems.size();
    }

    @Override
    public Object getItem(int i) {
        return booksItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View view1 = view;
        BookAdapter.ViewHolder viewHolder = new BookAdapter.ViewHolder();
        if (view1 == null){

            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view1 = inflater.inflate(R.layout.listvieweradapter, null);

            viewHolder.authorName = view1.findViewById(R.id.textViewAuthorN);
            viewHolder.bookName = view1.findViewById(R.id.textViewBookN);
            viewHolder.bookGender = view1.findViewById(R.id.textViewGenderN);
            viewHolder.index = view1.findViewById(R.id.textViewIndex);
            viewHolder.realIndex = view1.findViewById(R.id.textViewRInd);
            viewHolder.date = view1.findViewById(R.id.textViewDate);
            view1.setTag(viewHolder);

        }else {

            viewHolder = (BookAdapter.ViewHolder)view1.getTag();

        }

        viewHolder.bookName.setText(booksItems.get(i).bookName);
        viewHolder.authorName.setText(booksItems.get(i).authorName);
        viewHolder.bookGender.setText(booksItems.get(i).bookGender);
        viewHolder.realIndex.setText(booksItems.get(i).realIndex);
        viewHolder.date.setText(booksItems.get(i).date);
        viewHolder.index.setText(""+(i+1));


        return view1;
    }

    @Override
    public Filter getFilter() {
        return customizedFilter;
    }

    static class ViewHolder{
        TextView bookName, authorName, bookGender, index, realIndex, date;
    }

    private Filter customizedFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<BooksItem> filteredBooks = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filteredBooks.addAll(booksItemsFull);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (BooksItem booksItemC : booksItemsFull) {

                    if (chechedI.contains("book")) {

                        if (booksItemC.bookName.toLowerCase().startsWith(filterPattern)) {
                            filteredBooks.add(booksItemC);
                        }
                    }else if (chechedI.contains("author")){
                        if (booksItemC.authorName.toLowerCase().startsWith(filterPattern)) {
                            filteredBooks.add(booksItemC);
                        }
                    }else if (chechedI.contains("gender")){
                        if (booksItemC.bookGender.toLowerCase().startsWith(filterPattern)) {
                            filteredBooks.add(booksItemC);
                        }
                    }else {
                        if (booksItemC.date.toLowerCase().contains(filterPattern)){
                            filteredBooks.add(booksItemC);
                        }
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredBooks;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            booksItems.clear();
            booksItems.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };



}
