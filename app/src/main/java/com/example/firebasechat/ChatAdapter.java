package com.example.firebasechat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    Context context;
    ArrayList<Chat> chats;
    int ALIGN;
    FirebaseUser us;
    boolean t;
    public ChatAdapter(Context context, ArrayList<Chat> chats)
    {
        this.context=context;
        this.chats= chats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType== 0)
        {
            t=true;
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat,parent,false);
            return new ViewHolder(view);
        }
        else
        {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_left,parent,false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat= chats.get(position);
        holder.txt.setText(chat.getMessage());

            if(position == chats.size()-1)
            {
                if(t)
                {
                    holder.txt_seen.setVisibility(View.VISIBLE);
                    if(chat.isseen())
                    {
                        holder.txt_seen.setText("Seen");
                    }
                    else
                    {
                        holder.txt_seen.setText("Delivered");
                    }
                }
                else
                {
                    holder.txt_seen.setVisibility(View.GONE);
                }

            }
            else
            {
                holder.txt_seen.setVisibility(View.GONE);
            }
    }

    @Override
    public int getItemViewType(int position) {
        us= FirebaseAuth.getInstance().getCurrentUser();
        String s=chats.get(position).getSender();

        if(s.equals(us.getUid()))
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txt;
        public RelativeLayout cv;
        public TextView txt_seen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cv= (RelativeLayout) itemView.findViewById(R.id.cardview);
            txt =(TextView)itemView.findViewById(R.id.textView2);
            txt_seen =(TextView)itemView.findViewById(R.id.text_seen);
        }
    }
}
