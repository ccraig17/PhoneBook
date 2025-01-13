package com.craig.phonebook;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.craig.phonebook.RoomDatabase.Contact;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class Adaptor extends RecyclerView.Adapter<Adaptor.ContactCardViewHolder> {
    private List<Contact> contactList = new ArrayList<>(); //MUST initial ArrayList in order to get the items for getCount() or crash
    private OnImageClickListener listener;

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
        notifyDataSetChanged(); //updates the recycler view
    }

    public void setListener(OnImageClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list, parent, false);
        return new ContactCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactCardViewHolder holder, int position) {
       Contact contact = contactList.get(position);
        holder.txtName.setText(contact.getName());
        holder.txtTitle.setText(contact.getTitle());
        holder.txtPhoneNumber.setText(contact.getPhoneNumber());
        holder.txtEmail.setText(contact.getEmail());
        holder.imageProfile.setImageBitmap(BitmapFactory.decodeByteArray(contact.getImage(),
                    0, contact.getImage().length));
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // int position = getAdapterPosition();
//                int position = holder.getAdapterPosition();
//                if(listener != null && position != RecyclerView.NO_POSITION){
//                    listener.onImageClick(contactList.get(position));
//                }
//            }
//        });
    }
    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ContactCardViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private TextView txtTitle;
        private TextView txtPhoneNumber;
        private TextView txtEmail;
        private CircleImageView imageProfile;
        private CardView cardView;

        public ContactCardViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.textViewName);
            txtTitle = itemView.findViewById(R.id.textViewTitle);
            txtPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
            txtEmail = itemView.findViewById(R.id.textViewEmail);
            imageProfile = itemView.findViewById(R.id.circleImageView);
            cardView = itemView.findViewById(R.id.cardView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onImageClick(contactList.get(position));
                    }
                }
            });
        }
    }
    /*
        used to get the contact at its position, returning a contact object needed for the delete() method
        allows for access of a contact object in the delete() method via its position in the recycler view.
    */
    public Contact getPosition(int position) {
        return contactList.get(position);
    }
    public interface OnImageClickListener{
        void onImageClick(Contact contact);
    }
}
















