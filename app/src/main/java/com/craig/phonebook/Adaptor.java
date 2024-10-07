package com.craig.phonebook;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class Adaptor extends RecyclerView.Adapter<Adaptor.ContactCardViewHolder>{
    private ArrayList<ContactModel> contactModels;
    private Context context;

    public Adaptor(ArrayList<ContactModel> contactModels, Context context) {
        this.contactModels = contactModels;
        this.context = context;
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
        ContactModel contactModel = contactModels.get(position);
        holder.txtName.setText(contactModel.getName());
        holder.txtTitle.setText(contactModel.getTitle());
        holder.txtPhoneNumber.setText(contactModel.getPhoneNumber());
        holder.txtEmail.setText(contactModel.getEmail());
        holder.imageProfile.setImageResource(context.getResources().getIdentifier(contactModel.getImage(position),"drawable",context.getPackageName()));
    }

    @Override
    public int getItemCount() {
        return contactModels.size();
    }

    public static class ContactCardViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName;
        private TextView txtTitle;
        private TextView txtPhoneNumber;
        private TextView txtEmail;
        private CircleImageView imageProfile;;

    public ContactCardViewHolder(@NonNull View itemView) {
        super(itemView);
        txtName = itemView.findViewById(R.id.textViewName);
        txtTitle = itemView.findViewById(R.id.textViewTitle);
        txtPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
        txtEmail = itemView.findViewById(R.id.textViewEmail);
        imageProfile = itemView.findViewById(R.id.circleImageView);
    }
}
}
















