package com.example.spamovil.controllers;

import com.example.spamovil.models.Configs;
import com.example.spamovil.models.Users;

import io.realm.Realm;
import io.realm.RealmResults;

public class ControllerUsers {
    private Realm realm;
    private Users users;
    private RealmResults <Users> resultsUsers;

    public ControllerUsers(Realm realm) {
        this.realm = realm;
    }

    public Users getUser(String email) {
        return realm.where(Users.class).equalTo("correo_user", email).findFirst();
    }

    public Users getUser() {
        return realm.where(Users.class).findFirst();
    }

    public void createUser(
            String correo_user, String nombre_user, String apellido_p_user, String apellido_m_user, String direccion_user, String sucursal_user,
            String tipo_user, String access_to_user, Boolean activo_user, String principal
    ) {
        realm.beginTransaction();
        users = realm.createObject(Users.class, correo_user);
        users.setNombre_user(nombre_user);
        users.setApellido_p_user(apellido_p_user);
        users.setApellido_m_user(apellido_m_user);
        users.setDireccion_user(direccion_user);
        users.setSucursal_user(sucursal_user);
        users.setTipo_user(tipo_user);
        users.setAccess_to_user(access_to_user);
        users.setActivo_user(activo_user);
        users.setPrincipal(principal);
        realm.commitTransaction();
    }

    public void changeDataUser(
            String correo_user, String nombre_user, String apellido_p_user, String apellido_m_user, String direccion_user, String sucursal_user,
            String tipo_user, String access_to_user, Boolean activo_user, String principal
    ) {
        users = getUser(correo_user);
        realm.beginTransaction();
        users.setNombre_user(nombre_user);
        users.setApellido_p_user(apellido_p_user);
        users.setApellido_m_user(apellido_m_user);
        users.setDireccion_user(direccion_user);
        users.setSucursal_user(sucursal_user);
        users.setTipo_user(tipo_user);
        users.setAccess_to_user(access_to_user);
        users.setActivo_user(activo_user);
        users.setPrincipal(principal);
        realm.commitTransaction();
        users = null;
    }

    public boolean isActivoUser() {
        users = realm.where(Users.class).findFirst();
        if (users != null) return users.getActivo_user();
        return false;
    }

    public boolean thereAreRegisters() {
        users = realm.where(Users.class).findFirst();
        return users != null;
    }
}
