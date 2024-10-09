package com.craig.phonebook;


import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class Adaptor extends RecyclerView.Adapter<Adaptor.ContactCardViewHolder> {
    private ArrayList<ContactModel> contactModelList;
    private final Context context;

    public Adaptor(ArrayList<ContactModel> contactModelList, Context context) {
        this.contactModelList = contactModelList;
        this.context = context;
    }
    public void setContactModelsList(ArrayList<ContactModel> contactModelList) {
        this.contactModelList = contactModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list, parent, false);
        return new ContactCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactCardViewHolder holder, int position) {
        ContactModel contactModel = contactModelList.get(position);
        holder.txtName.setText(contactModel.getName());
        holder.txtTitle.setText(contactModel.getTitle());
        holder.txtPhoneNumber.setText(contactModel.getPhoneNumber());
        holder.txtEmail.setText(contactModel.getEmail());
        //holder.imageProfile.setImageResource(context.getResources().getIdentifier(contactModel.getImage(position), "drawable", context.getPackageName()));
        holder.imageProfile.setImageURI(contactModel.getImage());
        holder.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(context, UpdateContactActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contactModelList.size();
    }

    public static class ContactCardViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private TextView txtTitle;
        private TextView txtPhoneNumber;
        private TextView txtEmail;
        private View cardView;
        private CircleImageView imageProfile;

        public ContactCardViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.textViewName);
            txtTitle = itemView.findViewById(R.id.textViewTitle);
            txtPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
            txtEmail = itemView.findViewById(R.id.textViewEmail);
            imageProfile = itemView.findViewById(R.id.circleImageView);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
















