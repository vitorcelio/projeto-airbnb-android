package com.celio.vitor.casaportemporada.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseHelper {

    private static FirebaseAuth auth;
    private static DatabaseReference reference;
    private static StorageReference storage;

    public static String getIdFirebase() {
        return getAuth().getUid();
    }

    public static boolean getAutenticado() {
        return getAuth().getCurrentUser() != null;
    }

    public static FirebaseAuth getAuth() {
        if(auth == null)
            auth = FirebaseAuth.getInstance();

        return auth;
    }

    public static DatabaseReference getDatabaseReference() {
        if(reference == null)
            reference = FirebaseDatabase.getInstance().getReference();

        return reference;
    }

    public static StorageReference getStorageReference() {
        if(storage == null)
            storage = FirebaseStorage.getInstance().getReference();

        return storage;
    }

}
